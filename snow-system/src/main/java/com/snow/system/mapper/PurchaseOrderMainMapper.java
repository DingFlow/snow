package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.PurchaseOrderMain;
import com.snow.system.domain.PurchaseOrderItem;

/**
 * 采购单主表Mapper接口
 * 
 * @author snow
 * @date 2021-01-07
 */
public interface PurchaseOrderMainMapper 
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
     *
      * @param purchaseOrderMain
     * @return
     */
    public int updatePurchaseOrderMainByOrderNo(PurchaseOrderMain purchaseOrderMain);
    /**
     * 删除采购单主表
     * 
     * @param id 采购单主表ID
     * @return 结果
     */
    public int deletePurchaseOrderMainById(Integer id);

    /**
     * 批量删除采购单主表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePurchaseOrderMainByIds(String[] ids);

    /**
     * 批量删除${subTable.functionName}
     * 
     * @param customerIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePurchaseOrderItemByPurchaseOrderNos(String[] ids);
    
    /**
     * 批量新增${subTable.functionName}
     * 
     * @param purchaseOrderItemList ${subTable.functionName}列表
     * @return 结果
     */
    public int batchPurchaseOrderItem(List<PurchaseOrderItem> purchaseOrderItemList);
    

    /**
     * 通过采购单主表ID删除${subTable.functionName}信息
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int deletePurchaseOrderItemByPurchaseOrderNo(String id);
}
