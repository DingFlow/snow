package com.snow.system.domain;

import java.math.BigDecimal;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ${subTable.functionName}对象 purchase_order_item
 * 
 * @author snow
 * @date 2021-01-07
 */
public class PurchaseOrderItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer id;

    /** 采购单号（主表单号） */
    @Excel(name = "采购单号", readConverterExp = "主=表单号")
    private String purchaseOrderNo;

    /** 名称 */
    @Excel(name = "名称")
    private String goodsName;

    /** 规格 */
    @Excel(name = "规格")
    private String goodsSize;

    /** 数量 */
    @Excel(name = "数量")
    private BigDecimal goodsQuantity;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal goodsPrice;

    /** 总价 */
    @Excel(name = "总价")
    private BigDecimal totalPrice;

    /** 货物编号 */
    @Excel(name = "货物编号")
    private String goodsNo;

    /** $column.columnComment */
    @Excel(name = "货物编号")
    private Long isDelete;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setPurchaseOrderNo(String purchaseOrderNo) 
    {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getPurchaseOrderNo() 
    {
        return purchaseOrderNo;
    }
    public void setGoodsName(String goodsName) 
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName() 
    {
        return goodsName;
    }
    public void setGoodsSize(String goodsSize) 
    {
        this.goodsSize = goodsSize;
    }

    public String getGoodsSize() 
    {
        return goodsSize;
    }
    public void setGoodsQuantity(BigDecimal goodsQuantity) 
    {
        this.goodsQuantity = goodsQuantity;
    }

    public BigDecimal getGoodsQuantity() 
    {
        return goodsQuantity;
    }
    public void setGoodsPrice(BigDecimal goodsPrice) 
    {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getGoodsPrice() 
    {
        return goodsPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) 
    {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() 
    {
        return totalPrice;
    }
    public void setGoodsNo(String goodsNo) 
    {
        this.goodsNo = goodsNo;
    }

    public String getGoodsNo() 
    {
        return goodsNo;
    }
    public void setIsDelete(Long isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Long getIsDelete() 
    {
        return isDelete;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("purchaseOrderNo", getPurchaseOrderNo())
            .append("goodsName", getGoodsName())
            .append("goodsSize", getGoodsSize())
            .append("goodsQuantity", getGoodsQuantity())
            .append("goodsPrice", getGoodsPrice())
            .append("totalPrice", getTotalPrice())
            .append("remark", getRemark())
            .append("goodsNo", getGoodsNo())
            .append("isDelete", getIsDelete())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .toString();
    }
}
