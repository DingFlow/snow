package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/20 13:43
 */
@Data
public class TaskBaseDTO extends FlowBaseDTO implements Serializable {

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 流程定义key
     */
    private String definitionKey;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 业务参数
     */
    private String businessKey;

    /**
     * 流程名称
     */
    private String processDefinitionName;


}
