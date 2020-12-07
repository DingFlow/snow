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
 * @description 开始监听器
 * @author: 没用的阿吉 event="end"
 * @create: 2020-12-05 16:52
 **/
@Service
@Slf4j
public class LeaveStartListener extends AbstractExecutionListener<SysOaLeaveForm>  {

    @Autowired
    private SysOaLeaveServiceImpl sysOaLeaveService;


    protected void process() {

        String processInstanceId = getProcessInstanceId();
        String businessKey= getBusinessKey();
        SysOaLeave sysOaLeave=new SysOaLeave();
        sysOaLeave.setProcessStatus(ProcessStatus.CHECKING.ordinal());
        sysOaLeave.setApplyPerson(getStartUserName());
        sysOaLeave.setProcessInstanceId(processInstanceId);
        sysOaLeave.setLeaveNo(businessKey);
        sysOaLeaveService.updateSysOaLeaveByLeaveNo(sysOaLeave);
    }
}
