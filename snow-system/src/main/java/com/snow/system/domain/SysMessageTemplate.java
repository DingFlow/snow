package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 消息模板对象 sys_message_template
 * 
 * @author qimingjin
 * @date 2021-02-27
 */
@Data
public class SysMessageTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 模板编码 */
    @Excel(name = "模板编码")
    private String templateCode;

    /** 模板名称 */
    @Excel(name = "模板名称")
    private String templateName;

    /** 模板体 */
    @Excel(name = "模板体")
    private String templateBody;

    /** 模板描述 */
    @Excel(name = "模板描述")
    private String templateDesc;

    /** 1-钉钉消息，2--邮件，3--短信,4--站内消息 */
    @Excel(name = "1-钉钉消息，2--邮件，3--短信,4--站内消息")
    private Long templateType;

    /**
     * 消息pc端url
     */
    private String pcUrl;

    /**
     * 消息app端url
     */
    private String appUrl;

    /**
     * 图标样式
     */
    private String iconClass="fa fa-envelope fa-fw";

    /** 0--正常，1--禁用 */
    @Excel(name = "0--正常，1--禁用")
    private Long templateStatus;

    /** 0--正常，1--删除 */
    private Long isDelete;

}
