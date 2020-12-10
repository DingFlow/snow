package com.snow.flowable.listener.leave;

import com.alibaba.fastjson.JSON;
import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.common.SpringContextUtil;
import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.listener.AbstractEventListener;
import com.snow.system.domain.SysOaLeave;
import com.snow.system.service.impl.SysOaLeaveServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/12/8 10:05
 */
@Slf4j
@Service
public class ManagerTaskEventListener extends AbstractEventListener {


    public ManagerTaskEventListener() {
        super(
                new HashSet<>(Arrays.asList(
                FlowableEngineEventType.TASK_ASSIGNED,
                FlowableEngineEventType.TASK_COMPLETED
                )), 
                new HashSet<>(Arrays.asList(
                        FlowDefEnum.SNOW_OA_LEAVE
                )));
    }



    @Override
    protected void taskAssigned(FlowableEngineEntityEvent event) {

        TaskEntity entity = (TaskEntity)event.getEntity();


        ProcessDefinitionEntity processDefinition = getProcessDefinition(event);
        //任务创建可发送短信，邮件通知接收人
       log.info("ManagerTaskEventListener----taskAssigned：{}",JSON.toJSONString(event));
    }

    @Override
    protected void taskCompleted(FlowableEngineEntityEvent event) {
        //任务完成做一些业务处理
        log.info("ManagerTaskEventListener----taskCompleted任务完成监听：{}",JSON.toJSONString(event));
        SysOaLeaveServiceImpl sysOaLeaveService = (SysOaLeaveServiceImpl)SpringContextUtil.getBean(SysOaLeaveServiceImpl.class);
        TaskEntity entity = (TaskEntity)event.getEntity();
        DelegateExecution execution = getExecution(event);
        SysOaLeave sysOaLeave=new SysOaLeave();
        sysOaLeave.setLeaveNo(execution.getProcessInstanceBusinessKey());
        Optional.ofNullable(entity.getVariableLocal(FlowConstants.IS_PASS)).ifPresent(t->{
            Boolean isPass = (Boolean)t;
            if(!isPass){
                sysOaLeave.setProcessStatus(ProcessStatus.REJECT.ordinal());
            }
        });
        sysOaLeaveService.updateSysOaLeaveByLeaveNo(sysOaLeave);
    }



    @Override
    public boolean isFailOnException() {
        return false;
    }
}
