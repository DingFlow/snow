package com.snow.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 财务支付宝流水对象 finance_alipay_flow
 *
 * @author snow
 * @date 2020-11-09
 */
@Data
public class FinanceAlipayFlow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 交易号 */
    @ExcelProperty(index = 1)
    @NotBlank(message = "交易号不能为空")
    private String tradeNo;

    /** 商家订单号 */
    @ExcelProperty(index = 2)
    private String orderNo;

    /** 交易创建时间 */
    @ExcelProperty(index = 3)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tradeCreateTime;

    /** 交易支付时间 */
    @ExcelProperty(index = 4)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /** 最近修改时间 */
    @ExcelProperty(index = 5)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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
    private String goodsName;
    /** 交易金额 */
    @ExcelProperty(index = 10)
    private BigDecimal tradePrice;

    /** 交易状态 */
    @ExcelProperty(index = 12)
    private Integer tradeStatus;

    /** 收支类型 */
    @ExcelProperty(index = 11)
    private Integer incomeExpenditureType;

    /** 服务费 */
    @ExcelProperty(index = 13)
    private BigDecimal serviceCharge;

    /** 退款金额 */
    @ExcelProperty(index = 14)
    private BigDecimal refundPrice;

    /** 资金状态 */
    @ExcelProperty(index = 16)
    private Integer capitalStatus;

    /** 所属人ID */

    private Long belongUserId;
    /** 所属人 */
    private String belongUserName;
    /** 交易主体账户 */

    private String tradeAccount;

    /** 交易真实姓名 */
    private String tradeRealName;
    /**账单类型**/
    private Integer billType;


    /**
     * 真实账务类型
     */
    private Integer realFinanceType;

    /**
     * 真实收支类型
     */
    private Integer realIncomeExpenditureType;

    private String year;
}
