package com.snow.flowable.domain.payment;

import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.domain.AppForm;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author qimingjin
 * @Title: 付款申请表单
 * @Description:
 * @date 2022/2/21 15:40
 */
@Data
public class PaymentForm extends AppForm implements Serializable {
    private static final long serialVersionUID = -2763605585222075354L;
    private Long id;

    /** 支付单号 */
    private String paymentNo;

    /** 订单金额 */
    private BigDecimal orderPrice;

    /** 支付金额 */
    private BigDecimal paymentPrice;

    /** 支付标题 */
    private String paymentTitle;

    /** 关联单号 */
    private String relateNo;

    /** 关联单号类型（1--采购单） */
    private Long relateNoType;

    /** 支付时间 */
    private Date paymentTime;

    /** 支付状态（0--待支付，1-已支付，2--作废） */
    private Long paymentStatus;

    /** 流程状态（0--待发起，1-审批中，2--审批通过，3--已驳回，4--作废） */
    private Long processStatus;

    /** 申请人 */
    private String paymentUser;


    /** 备注 */
    private String paymentRemark;

    /**
     * 创建时间
     */
    private Date createTime;

    @Override
    public FlowDefEnum getFlowDef() {
        return FlowDefEnum.SNOW_FN_PAYMENT;
    }
}
