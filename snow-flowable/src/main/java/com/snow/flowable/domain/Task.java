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

    String getName;

    String getDescription;

    int getPriority;

    String getOwner;

    String getAssignee;

    String getProcessInstanceId;

    String getExecutionId;

    String getTaskDefinitionId;

    String getProcessDefinitionId;

    String getScopeId;

    String getSubScopeId;

    String getScopeType;

    String getScopeDefinitionId;

    Date getCreateTime;

    String getTaskDefinitionKey;

    Date getDueDate;

    String getCategory;

    String getParentTaskId;

    String getTenantId;

    String getFormKey;

    Map<String, Object> getTaskLocalVariables;

    Map<String, Object> getProcessVariables;

    List<? extends IdentityLinkInfo> getIdentityLinks;

    Date getClaimTime;
}
