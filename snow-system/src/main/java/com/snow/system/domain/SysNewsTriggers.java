package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 消息通知配置对象 sys_news_triggers
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
@Data
public class SysNewsTriggers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** null */
    private Integer id;

    /** 流程消息节点key */
    @Excel(name = "流程消息节点key")
    private Integer newsNodeId;

    /** 消息类型（1-钉钉，2-邮件，3-短信，4-站内消息） */
    @Excel(name = "消息类型", readConverterExp = "1=-钉钉，2-邮件，3-短信，4-站内消息")
    private Integer newsType;

    /** 消息开关（0--关，1--开） */
    @Excel(name = "消息开关", readConverterExp = "0=--关，1--开")
    private Integer newsOnOff;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** 是否删除（0--否，1--是） */
    @Excel(name = "是否删除", readConverterExp = "0=--否，1--是")
    private Long isDelete;

    /**
     * 节点信息
     */
    private SysNewsNode sysNewsNode;
}
