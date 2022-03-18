package com.snow.flowable.listener.leave;

import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.domain.leave.LeaveRestartTask;
import com.snow.flowable.listener.AbstractTaskListener;
import com.snow.system.domain.SysOaLeave;
import com.snow.system.service.ISysOaLeaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-12-06 12:01
 **/
@Service("leaveStartTaskListener")
@Slf4j
public class LeaveStartTaskListener extends AbstractTaskListener<LeaveRestartTask> {

    @Autowired
    private ISysOaLeaveService sysOaLeaveService;


    @Override
    protected void processTask() {
        SysOaLeave sysOaLeave=new SysOaLeave();
        Boolean approvalResult = getApprovalResult();
        String businessKey = getBusinessKey();
        sysOaLeave.setLeaveNo(businessKey);
        if(!approvalResult){
            sysOaLeave.setProcessStatus(ProcessStatus.CANCEL.ordinal());
        }else {
            sysOaLeave.setProcessStatus(ProcessStatus.CHECKING.ordinal());
            String id = getDelegateTask().getId();
            log.info("获取到的taskID:{}",id);
        }
        sysOaLeaveService.updateSysOaLeaveByLeaveNo(sysOaLeave);
    }
}
