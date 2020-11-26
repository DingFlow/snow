package com.snow.flowable.domain;

import lombok.Data;


import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/26 16:41
 */
@Data
public class HistoricTaskInstanceDTO  extends FlowBaseDTO implements Serializable {
 
    /**
     * 用户ID
     */
    public String userId;
    public String taskDefinitionId;
    public String processDefinitionId;
    public String processDefinitionKey;
    public String processDefinitionKeyLike;
    public String processDefinitionName;
    public String processDefinitionNameLike;

    public String deploymentId;
    public Collection<String> deploymentIds;
    public String cmmnDeploymentId;

    public String processInstanceId;
   
    public String BusinessKey;
    public String BusinessKeyLike;

    public String executionId;
    public String scopeId;
    public String subScopeId;
    public String scopeType;
    public String scopeDefinitionId;
    public String propagatedStageInstanceId;
    public String processInstanceIdWithChildren;
    public String caseInstanceIdWithChildren;
    public String caseDefinitionKey;
    public String caseDefinitionKeyLike;
    public String caseDefinitionKeyLikeIgnoreCase;
    public String taskId;
    public String taskName;
    public String taskNameLike;
    public String taskNameLikeIgnoreCase;
    public String taskParentTaskId;
    public String taskDescription;
    public String taskDescriptionLike;
    public String taskDescriptionLikeIgnoreCase;
    public String taskDeleteReason;
    public String taskDeleteReasonLike;
    public String taskOwner;
    public String taskOwnerLike;
    public String taskOwnerLikeIgnoreCase;
    public String taskAssignee;
    public String taskAssigneeLike;
    public String taskAssigneeLikeIgnoreCase;
    public String taskDefinitionKey;
    public String taskDefinitionKeyLike;

    public String candidateUser;
    public String candidateGroup;

    public String involvedUser;
    public boolean ignoreAssigneeValue;
    public Integer taskPriority;
    public Integer taskMinPriority;
    public Integer taskMaxPriority;
    public boolean finished;
    public boolean unfinished;
    /**
     * 流程状态（0--进行中，1-结束）
     */
    public Integer processStatus;

    public Date dueDate;
    public Date dueAfter;
    public Date dueBefore;
    public boolean withoutDueDate;
    public Date creationDate;
    public Date creationAfterDate;
    public Date creationBeforeDate;
    public Date completedDate;
    public Date completedAfterDate;
    public Date completedBeforeDate;
    public String category;
    public boolean withFormKey;
    public String formKey;
    public String tenantId;
    public String tenantIdLike;
    public Integer taskVariablesLimit;

}
