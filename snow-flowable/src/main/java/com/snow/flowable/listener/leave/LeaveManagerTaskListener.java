package com.snow.flowable.listener.leave;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-12-06 12:01
 **/
@Service("leaveManagerTaskListener")
public class LeaveManagerTaskListener implements TaskListener {
    
    @Override
    public void notify(DelegateTask delegateTask) {
        Map<String, Object> variables = delegateTask.getVariables();
        Map<String, Object> variablesLocal = delegateTask.getVariablesLocal();
        String assignee = delegateTask.getAssignee();
    }
}
