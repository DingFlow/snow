package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 客户对象 sys_oa_customer
 * 
 * @author 没用的阿吉
 * @date 2021-04-09
 */
@Data
public class SysOaCustomer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 客户编号 */
    @Excel(name = "客户编号")
    private String customerNo;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 客户详细地址 */
    @Excel(name = "客户详细地址")
    private String customerAddress;

    /** 客户所在省名称 */
    @Excel(name = "客户所在省名称")
    private String customerProvinceName;

    /** 客户所在省 */
    @Excel(name = "客户所在省")
    private String customerProvinceCode;

    /** 客户所在市名称 */
    @Excel(name = "客户所在市名称")
    private String customerCityName;

    /** 客户所在市 */
    @Excel(name = "客户所在市")
    private String customerCityCode;

    /** 客户所在区名称 */
    @Excel(name = "客户所在区名称")
    private String customerAreaName;

    /** 客户所在区编码 */
    @Excel(name = "客户所在区编码")
    private String customerAreaCode;

    /** 客户状态 */
    @Excel(name = "客户状态")
    private String customerStatus;

    /** 客户来源 */
    @Excel(name = "客户来源")
    private String customerSource;

    /** 客户电话 */
    @Excel(name = "客户电话")
    private String customerPhone;

    /** 客户邮件 */
    @Excel(name = "客户邮件")
    private String customerEmail;

    /** 客户所属行业 */
    @Excel(name = "客户所属行业")
    private String customerIndustry;

    /** 客户负责人 */
    @Excel(name = "客户负责人")
    private String customerManager;

    private String customerManagerName;

    /** 客户主联系人 */
    @Excel(name = "客户主联系人")
    private String customerLinkeUser;

    /** 删除标识 */
    @Excel(name = "删除标识")
    private Long isDelete;

    /**
     * 0--不是公海，1--公海
     */
    private Integer isHighSeas=0;

}
