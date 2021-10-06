package com.snow.system.domain;

import java.util.Date;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 任务分配对象 sys_oa_task_distribute
 * 
 * @author 没用的阿吉
 * @date 2021-08-15
 */
public class SysOaTaskDistribute extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 任务编号 */
    @Excel(name = "任务编号")
    private String taskNo;

    /** 任务分配人 */
    @Excel(name = "任务分配人")
    private String taskDistributeId;

    /** 任务执行人 */
    @Excel(name = "任务执行人")
    private String taskExecuteId;

    /** 任务执行状态 */
    @Excel(name = "任务执行状态")
    private String taskExecuteStatus;

    /** 任务完成时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "任务完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskCompleteTime;

    /** 任务开始时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "任务开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskStartTime;

    /** 任务挂起时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "任务挂起时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskSuspendTime;

    /** 任务反馈 */
    @Excel(name = "任务反馈")
    private String taskFeedback;

    /** 乐观锁 */
    @Excel(name = "乐观锁")
    private Long revision;

    /** 删除标识 */
    @Excel(name = "删除标识")
    private Long isDelete;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTaskNo(String taskNo) 
    {
        this.taskNo = taskNo;
    }

    public String getTaskNo() 
    {
        return taskNo;
    }
    public void setTaskDistributeId(String taskDistributeId) 
    {
        this.taskDistributeId = taskDistributeId;
    }

    public String getTaskDistributeId() 
    {
        return taskDistributeId;
    }
    public void setTaskExecuteId(String taskExecuteId) 
    {
        this.taskExecuteId = taskExecuteId;
    }

    public String getTaskExecuteId() 
    {
        return taskExecuteId;
    }
    public void setTaskExecuteStatus(String taskExecuteStatus) 
    {
        this.taskExecuteStatus = taskExecuteStatus;
    }

    public String getTaskExecuteStatus() 
    {
        return taskExecuteStatus;
    }
    public void setTaskCompleteTime(Date taskCompleteTime) 
    {
        this.taskCompleteTime = taskCompleteTime;
    }

    public Date getTaskCompleteTime() 
    {
        return taskCompleteTime;
    }
    public void setTaskStartTime(Date taskStartTime) 
    {
        this.taskStartTime = taskStartTime;
    }

    public Date getTaskStartTime() 
    {
        return taskStartTime;
    }
    public void setTaskSuspendTime(Date taskSuspendTime) 
    {
        this.taskSuspendTime = taskSuspendTime;
    }

    public Date getTaskSuspendTime() 
    {
        return taskSuspendTime;
    }
    public void setTaskFeedback(String taskFeedback) 
    {
        this.taskFeedback = taskFeedback;
    }

    public String getTaskFeedback() 
    {
        return taskFeedback;
    }
    public void setRevision(Long revision) 
    {
        this.revision = revision;
    }

    public Long getRevision() 
    {
        return revision;
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
            .append("taskNo", getTaskNo())
            .append("taskDistributeId", getTaskDistributeId())
            .append("taskExecuteId", getTaskExecuteId())
            .append("taskExecuteStatus", getTaskExecuteStatus())
            .append("taskCompleteTime", getTaskCompleteTime())
            .append("taskStartTime", getTaskStartTime())
            .append("taskSuspendTime", getTaskSuspendTime())
            .append("taskFeedback", getTaskFeedback())
            .append("revision", getRevision())
            .append("createBy", getCreateBy())
            .append("isDelete", getIsDelete())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
