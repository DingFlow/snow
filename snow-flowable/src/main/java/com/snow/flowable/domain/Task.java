package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/19 17:53
 */
@Data
public class Task implements Serializable {
    private static final long serialVersionUID = -1281053776739314567L;

    private String id;

    private String name;

    private String description;

    private int priority;

    private String owner;

    private String assignee;

    private String processInstanceId;

    private String executionId;

    private String taskDefinitionId;

    private String processDefinitionId;

    private String scopeId;


    private Date createTime;

    private String taskDefinitionKey;

    private Date dueDate;

    private String category;

    private String parentTaskId;

    private String tenantId;

    private String formKey;


    private Date claimTime;
}
