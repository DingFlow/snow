package com.snow.flowable.domain;

import lombok.AllArgsConstructor;
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
 * @date 2020/11/25 16:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessInstanceDTO extends FlowBaseDTO implements Serializable {

    private static final long serialVersionUID = 5035187451211518026L;

    public String businessKey;

    public String deploymentId;


    public boolean finished;

    public boolean unfinished;

    public boolean deleted;

    public boolean notDeleted;

    public String startedUserId;

    public List<String> processDefinitionKeyIn;

    public List<String> processKeyNotIn;

    public Date startedBefore;

    public Date startedAfter;

    public Date finishedBefore;

    public Date finishedAfter;

    public String processDefinitionKey;

    public String processDefinitionCategory;

    public String processDefinitionName;

    public Integer processDefinitionVersion;

    public Set<String> processInstanceIds;

    public String involvedUser;

    public Set<String> involvedGroups;

    public boolean includeProcessVariables;

    public Integer processInstanceVariablesLimit;


    public String name;

    public String nameLike;

    public String nameLikeIgnoreCase;

}
