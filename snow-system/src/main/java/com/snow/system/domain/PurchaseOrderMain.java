package com.snow.system.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 采购单主表对象 purchase_order_main
 * 
 * @author snow
 * @date 2021-01-07
 */
public class PurchaseOrderMain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 总数量 */
    @Excel(name = "总数量")
    private BigDecimal totalQuantity;

    /** 采购总金额 */
    @Excel(name = "采购总金额")
    private BigDecimal totalPrice;

    /** 采购标题 */
    @Excel(name = "采购标题")
    private String title;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 订货日期 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Excel(name = "订货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderTime;

    /** 交货日期 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Excel(name = "交货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryDate;

    /** 审批状态 */
    @Excel(name = "审批状态")
    private Long processStatus;

    /** 采购人 */
    @Excel(name = "采购人")
    private String belongUser;

    /** null */
    private Long isDelete;

    /** $table.subTable.functionName信息 */
    private List<PurchaseOrderItem> purchaseOrderItemList;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setTotalQuantity(BigDecimal totalQuantity) 
    {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalQuantity() 
    {
        return totalQuantity;
    }
    public void setTotalPrice(BigDecimal totalPrice) 
    {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() 
    {
        return totalPrice;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setSupplierName(String supplierName) 
    {
        this.supplierName = supplierName;
    }

    public String getSupplierName() 
    {
        return supplierName;
    }
    public void setOrderTime(Date orderTime) 
    {
        this.orderTime = orderTime;
    }

    public Date getOrderTime() 
    {
        return orderTime;
    }
    public void setDeliveryDate(Date deliveryDate) 
    {
        this.deliveryDate = deliveryDate;
    }

    public Date getDeliveryDate() 
    {
        return deliveryDate;
    }
    public void setProcessStatus(Long processStatus) 
    {
        this.processStatus = processStatus;
    }

    public Long getProcessStatus() 
    {
        return processStatus;
    }
    public void setBelongUser(String belongUser) 
    {
        this.belongUser = belongUser;
    }

    public String getBelongUser() 
    {
        return belongUser;
    }
    public void setIsDelete(Long isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Long getIsDelete() 
    {
        return isDelete;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItemList()
    {
        return purchaseOrderItemList;
    }

    public void setPurchaseOrderItemList(List<PurchaseOrderItem> purchaseOrderItemList)
    {
        this.purchaseOrderItemList = purchaseOrderItemList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("totalQuantity", getTotalQuantity())
            .append("totalPrice", getTotalPrice())
            .append("title", getTitle())
            .append("supplierName", getSupplierName())
            .append("orderTime", getOrderTime())
            .append("deliveryDate", getDeliveryDate())
            .append("processStatus", getProcessStatus())
            .append("belongUser", getBelongUser())
            .append("isDelete", getIsDelete())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .append("purchaseOrderItemList", getPurchaseOrderItemList())
            .toString();
    }
}
