package com.snow.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.snow.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 财务支付宝流水对象 finance_alipay_flow
 * 
 * @author snow
 * @date 2020-11-09
 */
public class FinanceAlipayFlowImport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 交易号 */
    @ExcelProperty(index = 1)
    private String tradeNo;

    /** 商家订单号 */
    @ExcelProperty(index = 2)
    private String orderNo;

    /** 交易创建时间 */
    @ExcelProperty(index = 3)
    private Date tradeCreateTime;

    /** 交易支付时间 */
    @ExcelProperty(index = 4)
    private Date payTime;

    /** 最近修改时间 */
    @ExcelProperty(index = 5)
    private Date lastModifyTime;

    /** 交易来源地 */
    @ExcelProperty(index =6)
    private String tradeSourcePlace;

    /** 交易类型 */
    @ExcelProperty(index = 7)
    private Integer tradeType;

    /** 交易对方 */
    @ExcelProperty(index = 8)
    private String counterparty;

    /** 商品名称 */
    @ExcelProperty(index = 9)
    private String goodName;
    /** 交易金额 */
    @ExcelProperty(index = 10)
    private BigDecimal tradePrice;

    /** 交易状态 */
    @ExcelProperty(index = 12)
    private String tradeStatus;

    /** 收支类型 */
    @ExcelProperty(index = 11)
    private String incomeExpenditureType;

    /** 服务费 */
    @ExcelProperty(index = 13)
    private BigDecimal serviceCharge;

    /** 退款金额 */
    @ExcelProperty(index = 14)
    private BigDecimal refundPrice;

    /** 资金状态 */
    @ExcelProperty(index = 16)
    private String capitalStatus;

    /** 所属人 */

    private Long belongUserId;

    /** 交易主体账户 */

    private String tradeAccount;


    /** 交易真实姓名 */
    private String tradeRealName;
    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTradeNo(String tradeNo) 
    {
        this.tradeNo = tradeNo;
    }

    public String getTradeNo() 
    {
        return tradeNo;
    }
    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setTradeCreateTime(Date tradeCreateTime) 
    {
        this.tradeCreateTime = tradeCreateTime;
    }

    public Date getTradeCreateTime() 
    {
        return tradeCreateTime;
    }
    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }
    public void setLastModifyTime(Date lastModifyTime) 
    {
        this.lastModifyTime = lastModifyTime;
    }

    public Date getLastModifyTime() 
    {
        return lastModifyTime;
    }
    public void setTradeSourcePlace(String tradeSourcePlace) 
    {
        this.tradeSourcePlace = tradeSourcePlace;
    }

    public String getTradeSourcePlace() 
    {
        return tradeSourcePlace;
    }
    public void setTradeType(Integer tradeType) 
    {
        this.tradeType = tradeType;
    }

    public Integer getTradeType() 
    {
        return tradeType;
    }
    public void setCounterparty(String counterparty) 
    {
        this.counterparty = counterparty;
    }

    public String getCounterparty() 
    {
        return counterparty;
    }
    public void setTradePrice(BigDecimal tradePrice) 
    {
        this.tradePrice = tradePrice;
    }

    public BigDecimal getTradePrice() 
    {
        return tradePrice;
    }
    public void setTradeStatus(String tradeStatus)
    {
        this.tradeStatus = tradeStatus;
    }

    public String getTradeStatus()
    {
        return tradeStatus;
    }
    public void setIncomeExpenditureType(String incomeExpenditureType)
    {
        this.incomeExpenditureType = incomeExpenditureType;
    }

    public String getIncomeExpenditureType()
    {
        return incomeExpenditureType;
    }
    public void setServiceCharge(BigDecimal serviceCharge) 
    {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getServiceCharge() 
    {
        return serviceCharge;
    }
    public void setRefundPrice(BigDecimal refundPrice) 
    {
        this.refundPrice = refundPrice;
    }

    public BigDecimal getRefundPrice() 
    {
        return refundPrice;
    }
    public void setCapitalStatus(String capitalStatus)
    {
        this.capitalStatus = capitalStatus;
    }

    public String getCapitalStatus()
    {
        return capitalStatus;
    }
    public void setBelongUserId(Long belongUserId) 
    {
        this.belongUserId = belongUserId;
    }

    public Long getBelongUserId() 
    {
        return belongUserId;
    }
    public void setTradeAccount(String tradeAccount) 
    {
        this.tradeAccount = tradeAccount;
    }

    public String getTradeAccount() 
    {
        return tradeAccount;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getTradeRealName() {
        return tradeRealName;
    }

    public void setTradeRealName(String tradeRealName) {
        this.tradeRealName = tradeRealName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tradeNo", getTradeNo())
            .append("orderNo", getOrderNo())
            .append("tradeCreateTime", getTradeCreateTime())
            .append("payTime", getPayTime())
            .append("lastModifyTime", getLastModifyTime())
            .append("tradeSourcePlace", getTradeSourcePlace())
            .append("tradeType", getTradeType())
            .append("counterparty", getCounterparty())
            .append("tradePrice", getTradePrice())
            .append("tradeStatus", getTradeStatus())
            .append("incomeExpenditureType", getIncomeExpenditureType())
            .append("serviceCharge", getServiceCharge())
            .append("refundPrice", getRefundPrice())
            .append("capitalStatus", getCapitalStatus())
            .append("remark", getRemark())
            .append("belongUserId", getBelongUserId())
            .append("tradeAccount", getTradeAccount())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .toString();
    }
}
