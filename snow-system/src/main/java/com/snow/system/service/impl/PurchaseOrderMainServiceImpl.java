package com.snow.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.snow.system.domain.PurchaseOrderItem;
import com.snow.system.mapper.PurchaseOrderMainMapper;
import com.snow.system.domain.PurchaseOrderMain;
import com.snow.system.service.IPurchaseOrderMainService;
import com.snow.common.core.text.Convert;

/**
 * 采购单主表Service业务层处理
 * 
 * @author snow
 * @date 2021-01-07
 */
@Service
public class PurchaseOrderMainServiceImpl implements IPurchaseOrderMainService 
{
    @Autowired
    private PurchaseOrderMainMapper purchaseOrderMainMapper;

    /**
     * 查询采购单主表
     * 
     * @param id 采购单主表ID
     * @return 采购单主表
     */
    @Override
    public PurchaseOrderMain selectPurchaseOrderMainById(Integer id)
    {
        return purchaseOrderMainMapper.selectPurchaseOrderMainById(id);
    }

    /**
     * 查询采购单主表列表
     * 
     * @param purchaseOrderMain 采购单主表
     * @return 采购单主表
     */
    @Override
    public List<PurchaseOrderMain> selectPurchaseOrderMainList(PurchaseOrderMain purchaseOrderMain)
    {
        return purchaseOrderMainMapper.selectPurchaseOrderMainList(purchaseOrderMain);
    }

    /**
     * 新增采购单主表
     * 
     * @param purchaseOrderMain 采购单主表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertPurchaseOrderMain(PurchaseOrderMain purchaseOrderMain)
    {
        purchaseOrderMain.setCreateTime(DateUtils.getNowDate());
        int rows = purchaseOrderMainMapper.insertPurchaseOrderMain(purchaseOrderMain);
        insertPurchaseOrderItem(purchaseOrderMain);
        return rows;
    }

    /**
     * 修改采购单主表
     * 
     * @param purchaseOrderMain 采购单主表
     * @return 结果
     */
    @Transactional
    @Override
    public int updatePurchaseOrderMain(PurchaseOrderMain purchaseOrderMain)
    {
        purchaseOrderMain.setUpdateTime(DateUtils.getNowDate());
        purchaseOrderMainMapper.deletePurchaseOrderItemByPurchaseOrderNo(purchaseOrderMain.getId());
        insertPurchaseOrderItem(purchaseOrderMain);
        return purchaseOrderMainMapper.updatePurchaseOrderMain(purchaseOrderMain);
    }

    /**
     * 删除采购单主表对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePurchaseOrderMainByIds(String ids)
    {
        purchaseOrderMainMapper.deletePurchaseOrderItemByPurchaseOrderNos(Convert.toStrArray(ids));
        return purchaseOrderMainMapper.deletePurchaseOrderMainByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除采购单主表信息
     * 
     * @param id 采购单主表ID
     * @return 结果
     */
    @Override
    public int deletePurchaseOrderMainById(Integer id)
    {
        purchaseOrderMainMapper.deletePurchaseOrderItemByPurchaseOrderNo(id);
        return purchaseOrderMainMapper.deletePurchaseOrderMainById(id);
    }

    /**
     * 新增${subTable.functionName}信息
     * 
     * @param purchaseOrderMain 采购单主表对象
     */
    public void insertPurchaseOrderItem(PurchaseOrderMain purchaseOrderMain)
    {
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderMain.getPurchaseOrderItemList();
        String orderNo = purchaseOrderMain.getOrderNo();
        if (StringUtils.isNotNull(purchaseOrderItemList))
        {
            List<PurchaseOrderItem> list = new ArrayList<>();
            for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList)
            {
                purchaseOrderItem.setPurchaseOrderNo(orderNo);
                list.add(purchaseOrderItem);
            }
            if (list.size() > 0)
            {
                purchaseOrderMainMapper.batchPurchaseOrderItem(list);
            }
        }
    }
}
