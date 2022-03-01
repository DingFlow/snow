package com.snow.flowable.domain.payment;

import com.snow.flowable.domain.FinishTaskDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PaymentCashierTask extends FinishTaskDTO implements Serializable {
    private static final long serialVersionUID = 8984271252520753970L;

    /**
     * 支付账户
     */
    private String accountNo;

    /**
     * 支付金额
     */
    private BigDecimal paymentPrice;

    /** 支付标题 */
    private String paymentTitle;


    /**
     * 备注
     */
    private String paymentRemark;



}
