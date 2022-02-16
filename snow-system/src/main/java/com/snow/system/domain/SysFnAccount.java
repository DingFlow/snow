package com.snow.system.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableId;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

/**
 * 账户对象 sys_fn_account
 * 
 * @author Agee
 * @date 2022-02-16
 */
@Data
public class SysFnAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 账户号 */
    @Excel(name = "账户号")
    private String accountNo;

    /** 账户名称 */
    @Excel(name = "账户名称")
    private String accountName;

    /** 账户总金额 */
    @Excel(name = "账户总金额")
    private BigDecimal totalAmount;

    /** 账户冻结金额 */
    @Excel(name = "账户冻结金额")
    private BigDecimal freezeAmount;

    /** 删除标识 */
    private Integer isDelete;



}
