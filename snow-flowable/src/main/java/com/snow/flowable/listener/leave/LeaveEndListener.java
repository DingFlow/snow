package com.snow.flowable.listener.leave;

import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.domain.AppForm;
import com.snow.flowable.listener.CommonEventListener;
import com.snow.system.domain.SysOaLeave;
import com.snow.system.service.impl.SysOaLeaveServiceImpl;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-12-05 17:29
 **/
@Service
public class LeaveEndListener extends CommonEventListener {
    @Autowired
    private SysOaLeaveServiceImpl sysOaLeaveService;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        AppForm appForm = threadLocalAppForm.get();
        SysOaLeave sysOaLeave=new SysOaLeave();
        sysOaLeave.setProcessStatus(ProcessStatus.PASS.ordinal());
        sysOaLeave.setApplyPerson(appForm.getStartUserId());
        sysOaLeave.setProcessInstanceId(appForm.getProcessInstanceId());
        sysOaLeave.setLeaveNo(appForm.getBusinessKey());
        sysOaLeaveService.updateSysOaLeaveByLeaveNo(sysOaLeave);
    }
}
