package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 流程消息配置对象 sys_act_news_triggers
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
public class SysActNewsTriggers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** null */
    private Long id;

    /** 流程消息节点id(1-流程发起，2-任务创建，3-任务完成，4-流程结束) */
    @Excel(name = "流程消息节点id(1-流程发起，2-任务创建，3-任务完成，4-流程结束)")
    private Long newsNodeId;

    /** 消息类型（1-钉钉，2-邮件，3-短信，4-站内消息） */
    @Excel(name = "消息类型", readConverterExp = "1=-钉钉，2-邮件，3-短信，4-站内消息")
    private Long newsType;

    /** 消息开关（0--关，1--开） */
    @Excel(name = "消息开关", readConverterExp = "0=--关，1--开")
    private Long newsOnOff;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** 是否删除（0--否，1--是） */
    @Excel(name = "是否删除", readConverterExp = "0=--否，1--是")
    private Long isDelete;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setNewsNodeId(Long newsNodeId) 
    {
        this.newsNodeId = newsNodeId;
    }

    public Long getNewsNodeId() 
    {
        return newsNodeId;
    }
    public void setNewsType(Long newsType) 
    {
        this.newsType = newsType;
    }

    public Long getNewsType() 
    {
        return newsType;
    }
    public void setNewsOnOff(Long newsOnOff) 
    {
        this.newsOnOff = newsOnOff;
    }

    public Long getNewsOnOff() 
    {
        return newsOnOff;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setIsDelete(Long isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Long getIsDelete() 
    {
        return isDelete;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("newsNodeId", getNewsNodeId())
            .append("newsType", getNewsType())
            .append("newsOnOff", getNewsOnOff())
            .append("userId", getUserId())
            .append("isDelete", getIsDelete())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
