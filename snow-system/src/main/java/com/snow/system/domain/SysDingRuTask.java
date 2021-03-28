package com.snow.system.domain;

import java.util.Date;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 钉钉运行任务对象 sys_ding_ru_task
 * 
 * @author 没用的阿吉
 * @date 2021-03-24
 */
@Data
public class SysDingRuTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务id（来自钉钉taskId） */
    private String id;

    /** 版本号 */
    @Excel(name = "版本号")
    private Long rev;

    /** 流程实例id */
    @Excel(name = "流程实例id")
    private String procInstId;

    /** 钉钉流程code */
    @Excel(name = "钉钉流程code")
    private String procCode;

    /** 活动id */
    @Excel(name = "活动id")
    private String activityId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String name;

    /** 父任务id */
    @Excel(name = "父任务id")
    private String parentTaskId;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 任务定义key */
    @Excel(name = "任务定义key")
    private String taskDefKey;

    /** 待办人 */
    @Excel(name = "待办人")
    private String assignee;

    /** 结束时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date finishTime;

    /** 任务状态（NEW：未启动RUNNING：处理中PAUSED：暂停CANCELED：取消COMPLETED：完成TERMINATED：终止） */
    @Excel(name = "任务状态", readConverterExp = "N=EW：未启动RUNNING：处理中PAUSED：暂停CANCELED：取消COMPLETED：完成TERMINATED：终止")
    private String taskState;

    /** 分类 */
    @Excel(name = "分类")
    private String category;

    /** 任务挂起状态（0--正常，1--挂起） */
    @Excel(name = "任务挂起状态", readConverterExp = "0=--正常，1--挂起")
    private Long suspensionState;

    /** 租户id */
    @Excel(name = "租户id")
    private String tenantId;

    /** 外部表单key */
    @Excel(name = "外部表单key")
    private String formKey;

    private SysDingProcinst sysDingProcinst;

}
