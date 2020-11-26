package com.snow.flowable.domain;

import com.snow.common.utils.StringUtils;
import com.snow.common.utils.bean.BeanUtils;
import lombok.Data;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.TaskServiceConfiguration;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/26 16:41
 */
@Data
public class HistoricTaskInstanceVO implements Serializable {
    public String executionId;
    public String processInstanceId;
    public String processDefinitionId;
    public String taskDefinitionId;
    public String scopeId;
    public String subScopeId;
    public String scopeType;
    public String scopeDefinitionId;
    public String propagatedStageInstanceId;
    public Date createTime;
    public Date endTime;
    public Long durationInMillis;
    public String deleteReason;
    public String name;
    public String localizedName;
    public String parentTaskId;
    public String description;
    public String localizedDescription;
    public String owner;
    /**
     * 用户ID
     */
    public String assignee;
    public String taskDefinitionKey;
    public String formKey;
    public int priority;
    public Date dueDate;
    public Date claimTime;
    public String category;
    public String tenantId = TaskServiceConfiguration.NO_TENANT_ID;
    public Date lastUpdateTime;

    public static List<HistoricTaskInstanceVO> warpList(List<HistoricTaskInstance> historicTaskInstanceList){
        return historicTaskInstanceList.stream().map(t->{
            HistoricTaskInstanceVO historicTaskInstanceVO=new HistoricTaskInstanceVO();
            BeanUtils.copyProperties(t,historicTaskInstanceVO);

            return historicTaskInstanceVO;
        }).collect(Collectors.toList());
    }
}
