package com.snow.flowable.domain;

import com.snow.common.enums.WorkRecordStatus;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/26 16:41
 */
@Data
public class HistoricTaskInstanceDTO  extends FlowBaseDTO implements Serializable {

    private static final long serialVersionUID = 4563401089865990999L;
    /**
     * 用户ID
     */
    private String userId;
    
    private String taskDefinitionId;
    
    private String processDefinitionId;
    
    private String processDefinitionKey;
    
    private String processDefinitionKeyLike;
    
    private String processDefinitionName;
    
    private String processDefinitionNameLike;

    private String deploymentId;



    private String processInstanceId;
   
    private String BusinessKey;
    
    private String BusinessKeyLike;

    private String executionId;
    
    
    private String taskId;
    
    private String taskName;
    
    private String taskNameLike;
    
    private String taskNameLikeIgnoreCase;
    
    private String taskParentTaskId;
    
    private String taskDescription;
    
    private String taskDescriptionLike;
    
    private String taskDescriptionLikeIgnoreCase;

    
    private String taskOwner;
    
    private String taskOwnerLike;
    
    private String taskOwnerLikeIgnoreCase;
    
    private String taskAssignee;
    
    private String taskAssigneeLike;
    
    private String taskAssigneeLikeIgnoreCase;
    
    private String taskDefinitionKey;
    
    private String taskDefinitionKeyLike;

    private String candidateUser;
    
    private String candidateGroup;

    private String involvedUser;
    
 
 

    /**
     * 流程状态（0--进行中，1-结束）
     */
    private WorkRecordStatus processStatus;

    /**
     * 任务状态（0--进行中，1--结束）
     */
    private WorkRecordStatus taskStatus;

    private Date dueDate;
    


    private String category;

    private String formKey;
    
    private String tenantId;
    
    private String tenantIdLike;
    
    private Integer taskVariablesLimit;

}
