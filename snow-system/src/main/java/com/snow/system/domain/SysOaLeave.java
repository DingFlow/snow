package com.snow.system.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 请假单对象 sys_oa_leave
 * 
 * @author snow
 * @date 2020-11-22
 */
public class SysOaLeave extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 请假名称 */
    @Excel(name = "请假名称")
    private String name;

    /** 请假理由 */
    @Excel(name = "请假理由")
    private String reason;

    /** 开始时间 */
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 流程状态（0--待审批，1-审批中，2--审批通过，3--已驳回） */
    @Excel(name = "流程状态", readConverterExp = "0=--待审批，1-审批中，2--审批通过，3--已驳回")
    private Integer processStatus;

    /** 流程实例ID */
    @Excel(name = "流程实例ID")
    private String processInstanceId;

    /** 申请人 */
    @Excel(name = "申请人")
    private String applyPerson;

    /** 删除标识 */
    private Integer isDelete;

    /** 附件 */
    @Excel(name = "附件")
    private String fileUrl;
    /** 请假单号 */
    @Excel(name = "请假单号")
    private String leaveNo;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setReason(String reason) 
    {
        this.reason = reason;
    }

    public String getReason() 
    {
        return reason;
    }
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }
    public void setProcessStatus(Integer processStatus) 
    {
        this.processStatus = processStatus;
    }

    public Integer getProcessStatus() 
    {
        return processStatus;
    }
    public void setProcessInstanceId(String processInstanceId) 
    {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceId() 
    {
        return processInstanceId;
    }
    public void setApplyPerson(String applyPerson) 
    {
        this.applyPerson = applyPerson;
    }

    public String getApplyPerson() 
    {
        return applyPerson;
    }
    public void setIsDelete(Integer isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete() 
    {
        return isDelete;
    }
    public void setFileUrl(String fileUrl) 
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() 
    {
        return fileUrl;
    }

    public String getLeaveNo() {
        return leaveNo;
    }

    public void setLeaveNo(String leaveNo) {
        this.leaveNo = leaveNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("reason", getReason())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("processStatus", getProcessStatus())
            .append("processInstanceId", getProcessInstanceId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("applyPerson", getApplyPerson())
            .append("remark", getRemark())
            .append("isDelete", getIsDelete())
            .append("fileUrl", getFileUrl())
            .toString();
    }
}
