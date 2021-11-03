package com.snow.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 任务分配对象 sys_oa_task_distribute
 * 
 * @author 没用的阿吉
 * @date 2021-08-15
 */
@Data
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

    private List<String> taskExecuteStatusList;

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

    /**
     * 任务花费时间
     */
    private String spendTime;

    /**
     * 任务主表
     */
    private SysOaTask sysOaTask;

}
