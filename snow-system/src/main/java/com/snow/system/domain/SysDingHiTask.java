package com.snow.system.domain;

import java.util.Date;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 对象 sys_ding_hi_task
 * 
 * @author
 * @date 2021-03-24
 */
@Data
public class SysDingHiTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String id;

    private String taskId;

    private Long rev;


    private String procInstId;

    private String procCode;

    private String activityId;


    private String name;

    private String parentTaskId;


    private String taskResult;

    private String taskState;

    private String taskDefKey;


    private String description;


    private String assignee;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;



    private String category;



    private Long suspensionState;

    private String tenantId;


    private String formKey;

    /**
     * 任务历时
     */
    private String taskSpendTime;

    private SysDingProcinst sysDingProcinst;


}
