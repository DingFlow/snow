package com.snow.flowable.listener.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.snow.common.constant.MessageConstants;
import com.snow.common.enums.MessageEventType;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.impl.FlowableUserServiceImpl;
import com.snow.common.core.domain.MessageEventRequest;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysUserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author qimingjin
 * @Title: 待办创建
 * @Description:
 * @date 2021/7/16 13:29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskCreateListener implements FlowableEventListener {

    @Resource
    private FlowableService flowableService;

    @Resource
    private FlowableUserServiceImpl flowableUserService;

    @Resource
    private SysUserServiceImpl sysUserServiceImpl;


    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        if (flowableEvent instanceof FlowableEngineEntityEvent) {
            FlowableEngineEntityEvent flowableEngineEvent = (FlowableEngineEntityEvent) flowableEvent;
            ProcessDefinitionEntity processDefinition = flowableService.getProcessDefinition(flowableEngineEvent);
            if(ObjectUtil.isNull(processDefinition)){
                return;
            }
            String key = Optional.ofNullable(processDefinition.getKey()).orElse("");
            for (FlowDefEnum flowDefEnum : flowableService.getAllFlowDefEnumsSet()) {
                //在流程中存在的才监听
                if (flowDefEnum.getCode().equals(key)) {
                    TaskEntity entity = (TaskEntity) flowableEngineEvent.getEntity();
                   //发送站内信
                    List<SysUser> candidateUsers = flowableUserService.getCandidateUsers(entity.getAssignee(), entity.getId());
                    if(CollectionUtils.isNotEmpty(candidateUsers)){
                        HistoricProcessInstance processInstance = flowableService.getHistoricProcessInstanceById(entity.getProcessInstanceId());
                        candidateUsers.forEach(t->
                            sendInnerMessage(t,entity,processInstance)
                        );
                    }
                }
            }
        }

    }



    private void sendInnerMessage(SysUser toUsers,TaskEntity entity, HistoricProcessInstance processInstance){
        SysUser startSysUser = sysUserServiceImpl.selectUserById(Long.parseLong(processInstance.getStartUserId()));
        MessageEventRequest messageEventDTO=new MessageEventRequest(MessageEventType.INNER_TASK_TODO.getCode());
        messageEventDTO.setProducerId(String.valueOf(0));
        messageEventDTO.setConsumerIds(Sets.newHashSet(String.valueOf(toUsers.getUserId())));
        messageEventDTO.setMessageEventType(MessageEventType.INNER_TASK_TODO);
        messageEventDTO.setMessageOutsideId(entity.getId());
        messageEventDTO.setMessageShow(2);
        Map<String,Object> map= Maps.newHashMap();
        map.put("startUser", startSysUser.getUserName());
        map.put("startTime", DateUtil.formatDateTime(processInstance.getStartTime()));
        map.put("businessKey",processInstance.getBusinessKey());
        map.put("processInstance", processInstance.getProcessDefinitionName());
        map.put("taskId", entity.getId());
        map.put("taskName", entity.getName());
        messageEventDTO.setParamMap(map);
        messageEventDTO.setTemplateCode(MessageConstants.INNER_TASK_CREATED_CODE);
        applicationContext.publishEvent(messageEventDTO);
    }




    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }

}
