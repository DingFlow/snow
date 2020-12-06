package com.snow.flowable.listener.leave;


import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.domain.AppForm;
import com.snow.flowable.listener.CommonEventListener;
import com.snow.flowable.service.FlowableService;
import com.snow.system.domain.SysOaLeave;
import com.snow.system.service.impl.SysOaLeaveServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: snow
 * @description 开始监听器
 * @author: 没用的阿吉 event="end"
 * @create: 2020-12-05 16:52
 **/
@Service
@Slf4j
public class LeaveStartListener extends CommonEventListener implements ExecutionListener {

    @Autowired
    private SysOaLeaveServiceImpl sysOaLeaveService;

    @Autowired
    private FlowableService flowableService;


    public void notify(DelegateExecution delegateExecution) {
        Map<String, Object> variables = delegateExecution.getVariables();
        VariableInstance variableInstance = delegateExecution.getVariableInstance(delegateExecution.getProcessInstanceId());
        String leaveNo = String.valueOf(variables.get("leaveNo"));
        String startUserId = String.valueOf(variables.get("startUserId"));
        AppForm appForm=new AppForm();
        appForm.setBusinessKey(leaveNo);
        appForm.setProcessInstanceId(delegateExecution.getProcessInstanceId());
        appForm.setStartUserId(startUserId);
        threadLocalAppForm.set(appForm);

        SysOaLeave sysOaLeave=new SysOaLeave();
        sysOaLeave.setProcessStatus(ProcessStatus.CHECKING.ordinal());
        sysOaLeave.setApplyPerson(appForm.getStartUserId());
        sysOaLeave.setProcessInstanceId(appForm.getProcessInstanceId());
        sysOaLeave.setLeaveNo(appForm.getBusinessKey());
        sysOaLeaveService.updateSysOaLeaveByLeaveNo(sysOaLeave);
    }

    public void setAppForms(String processInstanceId){

    }

}
