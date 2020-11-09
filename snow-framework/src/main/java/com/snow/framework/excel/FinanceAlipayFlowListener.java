package com.snow.framework.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.snow.system.domain.FinanceAlipayFlow;
import com.snow.system.domain.FinanceAlipayFlowImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/4 10:43
 */

public class FinanceAlipayFlowListener extends AnalysisEventListener<FinanceAlipayFlowImport> {

    private static final Logger log = LoggerFactory.getLogger(FinanceAlipayFlowListener.class);
    //创建list集合封装最终的数据
    List<FinanceAlipayFlowImport> list = new ArrayList<>();

    //一行一行去读取excle内容
    @Override
    public void invoke(FinanceAlipayFlowImport productVO, AnalysisContext analysisContext) {
        list.add(productVO);
    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {


        buildSelectProductAppSql();
    }




    public void buildSelectProductAppSql(){
        StringBuilder stringBuilder=new StringBuilder();
        list.forEach(t->{
            if(StringUtils.isEmpty(t.getId())){
                //  System.out.println("select * from clms_product where code='"+t.getProductNo()+"'");
            }else {
                stringBuilder.append("update clms_product_app set brand_ratio_id=(select brand_ratio_id from clms_product  where code='"+t.getOrderNo()+"') where code='"+t.getTradeNo()+"';\r\n");
            }
        });
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("d:\\onlineProductAppSql.txt"));
            out.write(stringBuilder.toString());
            out.close();
            log.info("创建文件成功");
        } catch (IOException e) {
            log.error("写入文件异常");
        }
    }
}
