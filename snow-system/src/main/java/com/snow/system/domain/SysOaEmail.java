package com.snow.system.domain;

import java.util.Date;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 邮件对象 sys_oa_email
 * 
 * @author 没用的阿吉
 * @date 2021-03-12
 */
@Data
public class SysOaEmail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 邮件编号 */
    @Excel(name = "邮件编号")
    private String emailNo;

    /** 邮件主题 */
    @Excel(name = "邮件主题")
    private String emailSubject;

    /** 邮件内容 */
    @Excel(name = "邮件内容")
    private String emailContent;

    /** 收件人 */
    @Excel(name = "收件人")
    private String emailTo;
    /**
     * 收件人
     */
    private SysUser emailToUser;

    /** 发件人 */
    @Excel(name = "发件人")
    private String emailFrom;

    /**
     * 发件人邮件
     */
    private SysUser emailFromUser;

    /** 发送时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendTime;

    /** 邮件类型（0--发，1-收） */
    private Long emailType;

    /** 邮件状态（0--普通邮件，1-草稿，2--已发送，3--重要邮件，4--已删除） */
    @Excel(name = "邮件状态", readConverterExp = "0=--普通邮件，1-草稿，2--已发送，3--重要邮件，4--已删除")
    private Long emailStatus;

    /** 邮件标签 */
    @Excel(name = "邮件标签")
    private Long emailLabel;

    /** 所属人id */
    private String belongUserId;

    private String belongUserName;

    /** 是否已读（0--未读，1--已读） */
    @Excel(name = "是否已读", readConverterExp = "0=--未读，1--已读")
    private Long isRead;

    /** 删除标识（0--正常，1--删除） */
    private Long isDelete;

    /** 附件 */
    private String fileUrl;

    /**
     * 是否成功（0--成功）
     */
    private Integer isSuccess;

    /**
     * 失败信息
     */
    private String errorInfo;

    /**
     * 查询类型
     */
    private Integer mailSearchType;
}
