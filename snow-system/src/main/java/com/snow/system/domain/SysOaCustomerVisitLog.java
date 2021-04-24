package com.snow.system.domain;

import java.util.Date;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 客户拜访日志对象 sys_oa_customer_visit_log
 * 
 * @author 没用的阿吉
 * @date 2021-04-14
 */
@Data
public class SysOaCustomerVisitLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 拜访客户编号 */
    @Excel(name = "拜访客户编号")
    private String customerNo;

    /** 拜访内容 */
    @Excel(name = "拜访内容")
    private String visitContent;

    /** 拜访类型：电话拜访，实地拜访 */
    @Excel(name = "拜访类型：电话拜访，实地拜访")
    private String visitType;

    /** 拜访结果 */
    @Excel(name = "拜访结果")
    private String visitResult;

    /** 拜访附件URL */
    @Excel(name = "拜访附件URL")
    private String visitUrl;

    /** 拜访结果推送人 */
    @Excel(name = "拜访结果推送人")
    private String[] acceptUser;

    /** 拜访时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Excel(name = "拜访时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date visitTime;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String visitLinkPhone;

    /** 联系人 */
    @Excel(name = "联系人")
    private String visitLinkUser;

    /** 删除标识 */
    private Long isDelete;

}
