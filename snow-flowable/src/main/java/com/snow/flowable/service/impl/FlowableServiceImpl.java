package com.snow.flowable.service.impl;

import com.snow.flowable.service.FlowableService;
import com.snow.system.domain.SysRole;
import com.snow.system.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/19 17:27
 */
@Slf4j
@Service
public class FlowableServiceImpl implements FlowableService {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ISysRoleService roleService;
    
    @Override
    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        return processInstance;
    }

    @Override
    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey);
        return processInstance;
    }

    @Override
    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, Map<String, Object> variables) {
        return null;
    }

    @Override
    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        return null;
    }

    @Override
    public ProcessInstance startProcessInstanceByKeyAndTenantId(String processDefinitionKey, String tenantId) {
        return null;
    }

    @Override
    public List<TaskEntity> findTasksByProcessInstanceId(String processInstanceId) {
        return null;
    }

    @Override
    public TaskEntity getTask(String id) {
        return null;
    }

    @Override
    public List<TaskEntity> findTasksByUserId(String userId) {
        List<SysRole> sysRoles = roleService.selectRolesByUserId(Long.parseLong(userId));
        List<Task> tasks = taskService.createTaskQuery()
                .or().taskCandidateOrAssigned(userId)
                .or().taskCandidateGroupIn(sysRoles.stream().map(SysRole::getRoleKey).collect(Collectors.toList()))
                .endOr()
                .listPage(1, 10);


        return null;
    }
}
