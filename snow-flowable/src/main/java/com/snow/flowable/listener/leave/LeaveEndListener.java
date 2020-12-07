package com.snow.flowable.listener.leave;

import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.domain.leave.SysOaLeaveForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import com.snow.system.domain.SysOaLeave;
import com.snow.system.service.impl.SysOaLeaveServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-12-05 17:29
 **/
@Service
@Slf4j
public class LeaveEndListener extends AbstractExecutionListener<SysOaLeaveForm> {
    @Autowired
    private SysOaLeaveServiceImpl sysOaLeaveService;

    @Override
    protected void process() {
        SysOaLeaveForm appForms = getAppForms();
        String businessKey= getBusinessKey();
        SysOaLeave sysOaLeave=new SysOaLeave();
        sysOaLeave.setProcessStatus(ProcessStatus.PASS.ordinal());
        sysOaLeave.setLeaveNo(businessKey);
        sysOaLeaveService.updateSysOaLeaveByLeaveNo(sysOaLeave);
    }
}
