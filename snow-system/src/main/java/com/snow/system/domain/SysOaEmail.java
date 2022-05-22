package com.snow.system.domain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.BaseEntity;
import com.snow.system.handler.JsonStringListTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class SysOaEmail extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8790267868126534635L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 邮件编号 */
    @Excel(name = "邮件编号")
    private String emailNo;

    /** 邮件主题 */
    @Excel(name = "邮件主题")
    private String emailSubject;

    /** 邮件内容 */
    @Excel(name = "邮件内容")
    private String emailContent;

    /** 发送时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendTime;

    /** 邮件类型（0--发，1-收） */
    @Excel(name = "邮件类型", readConverterExp = "0=--发，1-收")
    private Long emailType;

    /** 邮件状态（0--普通邮件，1-草稿，2--已发送，3--重要邮件，4--垃圾箱） */
    @Excel(name = "邮件状态", readConverterExp = "0=--普通邮件，1-草稿，2--已发送，3--重要邮件，4--垃圾箱")
    private Integer emailStatus;

    /** 邮件标签 */
    @Excel(name = "邮件标签")
    private Integer emailLabel;

    @Excel(name = "邮件接收人")

    @TableField(typeHandler= JsonStringListTypeHandler.class)
    private List<String> emailToUser;

    /** 所属人id */
    @Excel(name = "所属人id")
    private String belongUserId;

    /** 删除标识（0--正常，1--删除） */
    @Excel(name = "删除标识", readConverterExp = "0=--正常，1--删除")
    @TableLogic
    private Long isDelete;

    /** 附件 */
    @Excel(name = "附件")
    private String fileUrl;
}
