package com.snow.common.utils.poi;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.snow.common.annotation.Excel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
public class EasyExcelUtil {

    private static OutputStream getOutputStream(HttpServletResponse response,
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
