package com.snow.system.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2022/2/28 17:09
 */
@Data
public class DeductionAccountRequest implements Serializable {
    private static final long serialVersionUID = 8556743227989881679L;


    /**
     * 账户号
     */
    private String accountNo;

    /**
     * 扣款金额
     */
    private BigDecimal deductionAccount;

    /**
     * 备注
     */
    private String deductionRemark;
}
