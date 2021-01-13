package com.snow.system.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 采购单主表对象 purchase_order_main
 * 
 * @author snow
 * @date 2021-01-07
 */
@Data
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
}
