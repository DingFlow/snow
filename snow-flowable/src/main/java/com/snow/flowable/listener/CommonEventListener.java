package com.snow.flowable.listener;

import com.snow.flowable.domain.AppForm;
import com.snow.flowable.service.FlowableService;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: snow
 * @description 执行监听器
 * @author: 没用的阿吉
 * @create: 2020-12-05 16:59
 **/
@Component
@Slf4j
public class CommonEventListener implements ExecutionListener {

    public ThreadLocal<AppForm> threadLocalAppForm=new ThreadLocal();

    @Autowired
    private FlowableService flowableService;

    @Override
    public void notify(DelegateExecution delegateExecution) {

        Map<String, Object> variables = delegateExecution.getVariables();
        String processInstanceId = delegateExecution.getProcessInstanceId();
        log.info("===================>{}",processInstanceId);
        setAppForms(processInstanceId);
    }


    

    public void setAppForms(String processInstanceId){
        HistoricProcessInstance historicProcessInstance = flowableService.getHistoricProcessInstanceById(processInstanceId);
        AppForm appForm=new AppForm();
        appForm.setBusinessKey(historicProcessInstance.getBusinessKey());
        appForm.setProcessInstanceId(historicProcessInstance.getId());
        appForm.setStartUserId(historicProcessInstance.getStartUserId());
        threadLocalAppForm.set(appForm);
    }


    public Object getVariable(String variableName){
       return null;
    }
}

   
