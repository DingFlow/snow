package com.snow.flowable.listener.leave;


import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.domain.leave.SysOaLeaveForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import com.snow.system.domain.SysOaLeave;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysDeptServiceImpl;
import com.snow.system.service.impl.SysOaLeaveServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private SysDeptServiceImpl sysDeptService;

    protected void process() {

        SysOaLeaveForm appForm = getVariable("appForm");
        String processInstanceId = getProcessInstanceId();
        String businessKey= getBusinessKey();
        SysOaLeave sysOaLeave=new SysOaLeave();
        sysOaLeave.setProcessStatus(ProcessStatus.CHECKING.ordinal());
        sysOaLeave.setApplyPerson(getStartUserName());
        sysOaLeave.setProcessInstanceId(processInstanceId);
        sysOaLeave.setLeaveNo(businessKey);
        sysOaLeaveService.updateSysOaLeaveByLeaveNo(sysOaLeave);
        //设置部门主管
        List<SysUser> deptLeaderList = sysDeptService.selectLeadsByUserId(getStartUserInfo().getUserId());
        if(CollectionUtils.isEmpty(deptLeaderList)){
            //管理员id
            setVariable("deptLeader",1);
        }else {
            setVariable("deptLeader",deptLeaderList.stream().map(s->String.valueOf(s.getUserId())).collect(Collectors.toList()));
        }
    }
}
