package com.snow.system.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author qimingjin
 * @Title: 账户
 * @Description:
 * @date 2022/2/18 10:54
 */
@Data
public class SysFnAccountResponse implements Serializable {
    private static final long serialVersionUID = -4475531068106536295L;

    /**
     * 账户号
     */
    private String accountNo;

    /** 账户名称 */
    private String accountName;

    /** 账户总金额 */
    private BigDecimal totalAmount;

    /** 账户冻结金额 */
    private BigDecimal freezeAmount;

    /**
     * 可用金额
     */
    private BigDecimal usableAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
