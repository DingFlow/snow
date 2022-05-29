package com.snow.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 系统任务对象 sys_oa_task
 * 
 * @author 没用的阿吉
 * @date 2021-07-29
 */
@Data
public class SysOaTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务编号 */
    @TableId(type = IdType.NONE)
    @Excel(name = "任务编号")
    private String taskNo;


    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** 任务内容 */
    @Excel(name = "任务内容")
    private String taskContent;

    @Excel(name = "紧急程度",dictType = "sys_oa_task_priority")
    private Integer priority;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "任务期望完成时间",dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expectedTime;

    /** 任务状态（详见数据字典） */
    @Excel(name = "任务状态", readConverterExp = "un_finish=执行中,finish=结束")
    private String taskStatus;

    /** 任务类型（详见数据字典） */
    @Excel(name = "任务类型", dictType = "sys_task_type")
    private String taskType;

    /** 任务来源 */
    @Excel(name = "任务来源")
    private String taskSource;

    @Excel(name = "任务状态", readConverterExp = "0=不同步,1=同步")
    private Integer syncDingtalk;

    /** 任务外部id即钉钉id*/
    private String taskOutsideId;

    /** 任务跳转URL */
    @Excel(name = "任务跳转URL")
    private String taskUrl;

    /** 任务分配人 */
    @Excel(name = "任务执行人")
    @TableField(exist = false)
    private List<String> taskDistributeId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date taskCompleteTime;


    private Long revision;


    @TableLogic
    private Long isDelete;

    @TableField(exist = false)
    private List<SysOaTaskDistribute> sysOaTaskDistributeList;


}
