package com.snow.flowable.domain;

import lombok.Data;
import org.flowable.identitylink.api.IdentityLinkInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/19 17:53
 */
@Data
public class Task implements Serializable {
   private String id;

    String name;

    String description;

    int priority;

    String owner;

    String assignee;

    String processInstanceId;

    String executionId;

    String taskDefinitionId;

    String processDefinitionId;

    String scopeId;



    Date createTime;

    String taskDefinitionKey;

    Date dueDate;

    String category;

    String parentTaskId;

    String tenantId;

    String formKey;


    Date claimTime;
}
