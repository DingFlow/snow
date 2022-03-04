package com.snow.system.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author qimingjin
 * @Title: 充值账户
 * @Description:
 * @date 2022/2/18 11:15
 */
@Data
public class RechargeAccountRequest implements Serializable {
    private static final long serialVersionUID = -1810523463388701478L;

    /**
     * 账户号
     */
    private String accountNo;

    /**
     * 充值金额
     */
    private BigDecimal rechargeAmount;

    /**
     * 充值备注
     */
    private String rechargeRemark;
}
