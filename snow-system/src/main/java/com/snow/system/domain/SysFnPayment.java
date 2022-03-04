package com.snow.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

/**
 * 支付申请对象 sys_fn_payment
 * 
 * @author Agee
 * @date 2022-02-19
 */
@Data
public class SysFnPayment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 支付单号 */
    @Excel(name = "支付单号")
    private String paymentNo;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private BigDecimal orderPrice;

    /**
     * 账单流水号
     */
    @Excel(name = "账单流水号")
    private String billNo;

    /** 支付金额 */
    @Excel(name = "支付金额")
    private BigDecimal paymentPrice;

    /** 支付标题 */
    @Excel(name = "支付标题")
    private String paymentTitle;

    /** 关联单号 */
    @Excel(name = "关联单号")
    private String relateNo;

    /** 关联单号类型（1--采购单） */
    @Excel(name = "关联单号类型", readConverterExp = "1=--采购单")
    private Long relateNoType;

    /** 支付时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date paymentTime;

    /** 支付状态（0--待支付，1-已支付，2--作废） */
    @Excel(name = "支付状态", readConverterExp = "0=--待支付，1-已支付，2--作废")
    private Long paymentStatus;

    /** 流程状态（0--待发起，1-审批中，2--审批通过，3--已驳回，4--作废） */
    @Excel(name = "流程状态", readConverterExp = "0=--待发起，1-审批中，2--审批通过，3--已驳回，4--作废")
    private Long processStatus;

    /** 申请人 */
    private String paymentUser;

    /** null */
    private Long isDelete;

    /** 备注 */
    @Excel(name = "备注")
    private String paymentRemark;



}
