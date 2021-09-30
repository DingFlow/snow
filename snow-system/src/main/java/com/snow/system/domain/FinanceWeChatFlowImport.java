package com.snow.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 财务微信流水对象 finance_alipay_flow
 */
@Data
public class FinanceWeChatFlowImport {

    /** 交易时间 */
    @ExcelProperty("交易时间")
    private String payTime;

    /** 交易类型 */
    @ExcelProperty("交易类型")
    private String tradeType;

    /** 交易创建时间 */
    @ExcelProperty("交易对方")
    private String counterparty;

    /** 商品 */
    @ExcelProperty("商品")
    private String  goodsName;

    /** 收/支 */
    @ExcelProperty("收/支")
    private String incomeExpenditureType;

    /** 金额(元) */
    @ExcelProperty("金额(元)")
    private BigDecimal tradePrice;

    /** 支付方式 */
    @ExcelProperty( "支付方式")
    private String payType;

    /** 当前状态 */
    @ExcelProperty("当前状态")
    private String tradeStatus;

    /** 交易单号 */
    @ExcelProperty("交易单号")
    private String tradeNo;
    /** 商户单号 */
    @ExcelProperty("商户单号")
    private String orderNo;

    /** 备注 */
    @ExcelProperty("备注")
    private String remark;


}
