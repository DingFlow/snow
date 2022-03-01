package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.PurchaseOrderMain;

/**
 * 采购单主表Service接口
 * 
 * @author snow
 * @date 2021-01-07
 */
public interface IPurchaseOrderMainService 
{
    /**
     * 查询采购单主表
     * 
     * @param id 采购单主表ID
     * @return 采购单主表
     */
    public PurchaseOrderMain selectPurchaseOrderMainById(Integer id);

    /**
     * 查询采购单根据订单编号
     * @param orderNo 采购单号
     * @return
     */
    public PurchaseOrderMain selectPurchaseOrderMainByOrderNo(String orderNo);

    /**
     * 查询采购单主表列表
     * 
     * @param purchaseOrderMain 采购单主表
     * @return 采购单主表集合
     */
    public List<PurchaseOrderMain> selectPurchaseOrderMainList(PurchaseOrderMain purchaseOrderMain);

    /**
     * 新增采购单主表
     * 
     * @param purchaseOrderMain 采购单主表
     * @return 结果
     */
    public int insertPurchaseOrderMain(PurchaseOrderMain purchaseOrderMain);

    /**
     * 修改采购单主表
     * 
     * @param purchaseOrderMain 采购单主表
     * @return 结果
     */
    public int updatePurchaseOrderMain(PurchaseOrderMain purchaseOrderMain);

    /**
     * 批量删除采购单主表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePurchaseOrderMainByIds(String ids);

    /**
     * 删除采购单主表信息
     * 
     * @param id 采购单主表ID
     * @return 结果
     */
    public int deletePurchaseOrderMainById(Integer id);
}
