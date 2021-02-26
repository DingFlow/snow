package com.snow.system.domain;

import com.snow.common.enums.FinanceTradeType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author qimingjin
 * @Title: 财务账单概况返回实体
 * @Description:
 * @date 2021/2/26 13:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class FinanceBillSituationVO implements Serializable {
    /**
     * 即时到账金额
     */
   private BigDecimal immediatelyAccountTotal;
    /**
     * 支付宝担保交易金额
     */
    BigDecimal alipaySecuredTransactionTotal;
    /**
     * 商户消费
     */
    BigDecimal consumptionTotal;
    /**
     * 转账
     */
    BigDecimal transferAccountsTotal;
    /**
     * 微信红包
     */
    BigDecimal wxRedPacketsTotal;
    /**
     * 微信红包-退款
     */
    BigDecimal redPacketsRefundTotal;
    /**
     * 扫二维码付款
     */
    BigDecimal scannerQrCodePaymentTotal;
    /**
     * 二维码收款
     */
    BigDecimal qrCodePaymentTotal;
    //支出
    BigDecimal expenditureTotal;
    //收入
    BigDecimal inComeTotal;
}
