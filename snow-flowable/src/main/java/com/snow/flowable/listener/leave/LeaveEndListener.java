package com.snow.flowable.listener.leave;

import com.alibaba.fastjson.JSON;
import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.common.constants.FlowConstants;
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
        log.info("获取到的表单数据：{}",JSON.toJSONString(appForms));
        Boolean isPass = getVariable(FlowConstants.IS_PASS);
        String businessKey= getBusinessKey();
        SysOaLeave sysOaLeave=new SysOaLeave();
        sysOaLeave.setLeaveNo(businessKey);
        if(isPass){
            sysOaLeave.setProcessStatus(ProcessStatus.PASS.ordinal());
        }else {
            log.info("说明是重新申请取消的节点，上个节点的审批结果：{}",isPass);
            sysOaLeave.setProcessStatus(ProcessStatus.CANCEL.ordinal());
        }
        sysOaLeaveService.updateSysOaLeaveByLeaveNo(sysOaLeave);
    }
}
