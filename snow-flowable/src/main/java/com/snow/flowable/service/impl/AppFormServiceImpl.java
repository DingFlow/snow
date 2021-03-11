package com.snow.flowable.service.impl;

import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.domain.AppForm;
import com.snow.flowable.service.AppFormService;
import com.snow.flowable.service.FlowableService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/12/7 10:20
 */
@Service
@Slf4j
public class AppFormServiceImpl implements AppFormService {

    @Autowired
    private FlowableService flowableService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;



    @Override
    public <A extends AppForm> A getAppFrom(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance=flowableService.getHistoricProcessInstanceById(processInstanceId);
        return getVariable(historicProcessInstance, FlowConstants.APP_FORM);

    }


    /**
     * 获取流程变量
     * @param key
     * @param <V>
     * @return
     */
    protected <V> V  getVariable(HistoricProcessInstance historicProcessInstance,String key){

        return (V)historicProcessInstance.getProcessVariables().get(key);
    }

    /**
     * 根据执行id获取表单数据
     * @param executionId
     * @param <A>
     * @return
     */
    public <A extends AppForm> A getAppFromByExecutionId(String executionId) {
        return getVariableByExecutionId(executionId);

    }

    /**
     * 根据任务id获取表单数据
     * @param taskId
     * @param <A>
     * @return
     */
    @Override
    public <A extends AppForm> A getAppFromByTaskId(String taskId) {
        return getVariableByTaskId(taskId);
    }


    /**
     * 获取流程变量
     * @param executionId
     * @param <V>
     * @return
     */
    protected <V> V  getVariableByExecutionId(String executionId){
       // boolean b1 = runtimeService.hasVariable(executionId, FlowConstants.APP_FORM);
        return  (V)runtimeService.getVariable(executionId, FlowConstants.APP_FORM);
    }


    /**
     * 获取流程变量
     * @param taskId
     * @param <V>
     * @return
     */
    protected <V> V  getVariableByTaskId(String taskId){
        return  (V)taskService.getVariable(taskId,FlowConstants.APP_FORM);
    }
}
