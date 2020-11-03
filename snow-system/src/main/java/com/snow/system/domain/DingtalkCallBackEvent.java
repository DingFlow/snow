package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 事件表对象 dingtalk_call_back_event
 * 
 * @author snow
 * @date 2020-11-03
 */
public class DingtalkCallBackEvent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** null */
    private Integer id;

    /** null */
    @Excel(name = "null")
    private String eventName;

    /** null */
    @Excel(name = "null")
    private Long callBanckId;

    /** null */
    @Excel(name = "null")
    private String eventDesc;

    /** null */
    private Long delFlag;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setEventName(String eventName) 
    {
        this.eventName = eventName;
    }

    public String getEventName() 
    {
        return eventName;
    }
    public void setCallBanckId(Long callBanckId) 
    {
        this.callBanckId = callBanckId;
    }

    public Long getCallBanckId() 
    {
        return callBanckId;
    }
    public void setEventDesc(String eventDesc) 
    {
        this.eventDesc = eventDesc;
    }

    public String getEventDesc() 
    {
        return eventDesc;
    }
    public void setDelFlag(Long delFlag) 
    {
        this.delFlag = delFlag;
    }

    public Long getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("eventName", getEventName())
            .append("callBanckId", getCallBanckId())
            .append("eventDesc", getEventDesc())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
