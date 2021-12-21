package com.snow.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 财务支付宝流水对象 finance_alipay_flow
 * 
 * @author snow
 * @String 2020-11-09
 */
@Data
public class FinanceAlipayFlowImport
{
    private static final long serialVersionUID = 1L;



    /** 交易号 */
    @ExcelProperty(index = 0)
    private String tradeNo;

    /** 商家订单号 */
    @ExcelProperty(index = 1)
    private String orderNo;

    /** 交易创建时间 */
    @ExcelProperty(index = 2)
    private String tradeCreateTime;

    /** 交易支付时间 */
    @ExcelProperty(index = 3)
    private String payTime;

    /** 最近修改时间 */
    @ExcelProperty(index = 4)
    private String lastModifyTime;

    /** 交易来源地 */
    @ExcelProperty(index =5)
    private String tradeSourcePlace;

    /** 交易类型 */
    @ExcelProperty(index = 6)
    private String tradeType;

    /** 交易对方 */
    @ExcelProperty(index = 7)
    private String counterparty;

    /** 商品名称 */
    @ExcelProperty(index = 8)
    private String goodsName;
    /** 交易金额 */
    @ExcelProperty(index = 9)
    private BigDecimal tradePrice;

    /** 收支类型 */
    @ExcelProperty(index = 10)
    private String incomeExpenditureType;

    /** 交易状态 */
    @ExcelProperty(index = 11)
    private String tradeStatus;



    /** 服务费 */
    @ExcelProperty(index = 12)
    private BigDecimal serviceCharge;

    /** 退款金额 */
    @ExcelProperty(index = 13)
    private BigDecimal refundPrice;

    @ExcelProperty(index = 14)
    private String remark;

    /** 资金状态 */
    @ExcelProperty(index = 15)
    private String capitalStatus;

}
