package com.snow.framework.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.snow.system.domain.PurchaseOrderImport;
import com.snow.system.domain.SysUser;
import com.snow.system.service.IPurchaseOrderMainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/7 15:54
 */
public class PurchaseOrderListener extends AnalysisEventListener<PurchaseOrderImport> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisEventListener.class);

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */

    private IPurchaseOrderMainService purchaseOrderMainService;

    /**
     * 导入人
     */
    private SysUser sysUser;

    //创建list集合封装最终的数据
    public  List<PurchaseOrderImport> list = new ArrayList<>();

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param purchaseOrderMainService
     */
    public PurchaseOrderListener(IPurchaseOrderMainService purchaseOrderMainService, SysUser sysUser) {
        this.purchaseOrderMainService = purchaseOrderMainService;
        this.sysUser=sysUser;
    }


    @Override
    public void invoke(PurchaseOrderImport purchaseOrderImport, AnalysisContext analysisContext) {
        BigDecimal goodsPrice=Optional.ofNullable(purchaseOrderImport.getGoodsPrice()).orElse(new BigDecimal(0));
        BigDecimal goodsQuantity=Optional.ofNullable(purchaseOrderImport.getGoodsQuantity()).orElse(new BigDecimal(0));
        purchaseOrderImport.setTotalPrice(goodsPrice.multiply(goodsQuantity));
        list.add(purchaseOrderImport);
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
