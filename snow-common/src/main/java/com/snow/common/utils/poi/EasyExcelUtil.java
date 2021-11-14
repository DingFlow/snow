package com.snow.common.utils.poi;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author qimingjin
 * @Title: easyExcel 实体类
 * @Description:
 * @date 2020/12/31 14:06
 */
@Slf4j
public class EasyExcelUtil{
    /**
     * 写入Excel
     * @param fileName
     * @param response
     * @throws Exception
     */
    public static void writeExcel(String fileName,String sheetName, Class classClass,List list, HttpServletResponse response ) throws Exception{
        WriteWorkbook writeWorkbook=new WriteWorkbook();
        writeWorkbook.setOutputStream(getOutputStream(response,fileName,ExcelTypeEnum.XLSX));
        ExcelWriter excelWriter = new ExcelWriter(writeWorkbook);
        WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
        writeSheet.setSheetNo(1);
        writeSheet.setClazz(classClass);
        excelWriter.write(list, writeSheet);
        excelWriter.finish();
    }
    public static OutputStream getOutputStream(HttpServletResponse response,
                                                String fileName,
                                                ExcelTypeEnum excelTypeEnum) throws IOException {
        try {
            String finalFileName = URLEncoder.encode((fileName + System.currentTimeMillis() + excelTypeEnum.getValue()), "UTF-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            response.setHeader("Content-Disposition", "attachment;filename=" + finalFileName);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            return response.getOutputStream();
        } catch (IOException e) {
            log.error("获取输出流异常：", e);
            throw e;
        }
    }
}
