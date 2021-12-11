package com.snow.flowable.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.system.domain.SysUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * ASSIGNEE_（受理人）：task任务的受理人，就是执行TASK的人，这个又分两种情况（有值，NULL）
 *     1），有值的情况：XML流程里面定义的受理人，TASK会直接填入这个人；
 *     2），NULL：XML没有指定受理人或者只指定了候选组；
 * 没有值的时候，可以使用签收功能去指定受理人，就是候选组里面谁签收谁就成了受理人：
 *OWNER_（委托人）：受理人委托其他人操作该TASK的时候，受理人就成了委托人OWNER_，其他人就成了受理人ASSIGNEE_
 * @date 2020/11/23 17:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskVO implements Serializable {

    private static final long serialVersionUID = -8748902102208523880L;
    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 流程定义名称
     */
    private String processDefinitionName;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 委托人
     */
    private String owner;

    /**
     * 父任务ID
     */
    private String parentTaskId;

    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 任务处理人
     */
    private String Assignee;

    /**
     * 任务创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 分类
     */
    private String category;

    /**
     * fromKey
     */
    private String formKey;

    /**
     * 流程类型
     */
    private String processType;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 流程发起人id
     */
    private String startUserId;

    /**
     * 发起人名称
     */
    private String startUserName;

    /**
     * 业务主键
     */
    private String businessKey;
    /**
     * 任务发起时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 任务完成时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    /**
     * 处理任务时间
     */
    private String handleTaskTime;
    /**
     * 任务状态（0--待处理，1--已处理）
     */
    private Integer taskStatus=0;
    /**
     * 下个节点
     */
    private String nextTaskName;
    /**
     * 任务定义key
     */
    private String taskDefinitionKey;
    /**
     * 任务待处理人
     */
    private List<String> handleUserList;
}

