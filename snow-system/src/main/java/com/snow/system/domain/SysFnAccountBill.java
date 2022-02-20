package com.snow.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 账户流水详情对象 sys_fn_account_bill
 * 
 * @author Agee
 * @date 2022-02-16
 */
@Data
public class SysFnAccountBill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 账户流水号 */
    @Excel(name = "账户流水号")
    private String billNo;

    /** 账户号 */
    @Excel(name = "账户号")
    private String accountNo;

    /** 流水类型（1-收入，2-支出） */
    @Excel(name = "流水类型", readConverterExp = "1=-收入，2-支出")
    private Integer billType;

    /** 流水金额 */
    @Excel(name = "流水金额")
    private BigDecimal billAmount;

    @Excel(name = "备注")
    private String billRemark;

    /** 删除标识 */
    private Integer isDelete;



}
