package com.snow.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 财务支付宝流水对象 finance_alipay_flow
 * 
 * @author snow
 * @String 2020-11-09
 */
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
    private Date tradeCreateTime;

    /** 交易支付时间 */
    @ExcelProperty(index = 3)
    private Date payTime;

    /** 最近修改时间 */
    @ExcelProperty(index = 4)
    private Date lastModifyTime;

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



    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getTradeCreateTime() {
        return tradeCreateTime;
    }

    public void setTradeCreateTime(Date tradeCreateTime) {
        this.tradeCreateTime = tradeCreateTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getTradeSourcePlace() {
        return tradeSourcePlace;
    }

    public void setTradeSourcePlace(String tradeSourcePlace) {
        this.tradeSourcePlace = tradeSourcePlace;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(BigDecimal tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getIncomeExpenditureType() {
        return incomeExpenditureType;
    }

    public void setIncomeExpenditureType(String incomeExpenditureType) {
        this.incomeExpenditureType = incomeExpenditureType;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(BigDecimal refundPrice) {
        this.refundPrice = refundPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCapitalStatus() {
        return capitalStatus;
    }

    public void setCapitalStatus(String capitalStatus) {
        this.capitalStatus = capitalStatus;
    }


}
