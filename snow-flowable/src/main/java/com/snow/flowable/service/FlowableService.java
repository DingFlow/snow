package com.snow.flowable.service;

import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

import java.util.List;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/19 17:27
 */
public interface FlowableService {
    /**
     * 通过processDefinitionKey开始流程
     * @param processDefinitionKey
     * @return
     */
     ProcessInstance startProcessInstanceByKey(String processDefinitionKey);

     ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey);

     ProcessInstance startProcessInstanceByKey(String processDefinitionKey, Map<String, Object> variables);

     ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables);

     ProcessInstance startProcessInstanceByKeyAndTenantId(String processDefinitionKey, String tenantId);

    /**
     * 根据流程实例查询任务
     * @param processInstanceId
     * @return
     */
     List<TaskEntity> findTasksByProcessInstanceId(String processInstanceId);

     TaskEntity getTask(String id);

     List<TaskEntity> findTasksByUserId(String userId);
}
