package com.snow.flowable.listener;


import com.alibaba.fastjson.JSON;
import com.snow.flowable.common.SpringContextUtil;
import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.service.FlowableService;

import com.snow.flowable.service.impl.FlowableServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.Task;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.event.FlowableEngineEventImpl;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.engine.history.HistoricProcessInstance;

import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;


import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description: 全局事件监听
 * @date 2020/12/7 18:13
 */
@Slf4j
public class AbstractEventListener implements FlowableEventListener {


    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        RepositoryService repositoryService = (RepositoryService)SpringContextUtil.getBean(RepositoryService.class);

        FlowableServiceImpl flowableService = (FlowableServiceImpl)SpringContextUtil.getBean(FlowableServiceImpl.class);

        if (!(flowableEvent instanceof FlowableEngineEventImpl)) {
            return;
        }
        FlowableEngineEventImpl entityEvent = (FlowableEngineEventImpl) flowableEvent;
        log.info("监听到的事件类型：{}",entityEvent.getType());




        FlowableEngineEventType type = entityEvent.getType();
     //   log.info("监听到的事件类型：{}",type.name());
        String processInstanceId = entityEvent.getProcessInstanceId();
        if(type.name().equals("TASK_COMPLETED")){
            //todo
            log.info("任务已完成，这个时候可以处理一些业务逻辑");
            ProcessDefinition processDefinition = repositoryService.getProcessDefinition(entityEvent.getProcessDefinitionId());
            if(processDefinition.getKey().equals(FlowDefEnum.SNOW_OA_LEAVE.getCode())){
                ProcessInstance processInstance= flowableService.getProcessInstanceById(processInstanceId);
                Map<String, Object> processVariables = processInstance.getProcessVariables();
                processVariables.get(FlowConstants.IS_PASS);
                log.info("processVariables：{}",JSON.toJSONString(processVariables));

            }
            FlowableEntityEventImpl flowableEntityEvent = (FlowableEntityEventImpl) flowableEvent;
            log.info("flowableEntityEvent:{}",JSON.toJSONString(flowableEntityEvent));
            TaskEntity entity = (TaskEntity)flowableEntityEvent.getEntity();
            Object variable = entity.getVariable(FlowConstants.IS_PASS);
            log.info("entity:{}",JSON.toJSONString(entity));



        }

        if(type.name().equals("TASK_ASSIGNED")){
            FlowableEntityEventImpl flowableEntityEvent = (FlowableEntityEventImpl) flowableEvent;
            log.info("TASK_ASSIGNED-flowableEntityEvent:{}",JSON.toJSONString(flowableEntityEvent));
            //todo
            log.info("任务已分配，这个时候可以发送短信，邮件通知");
        }

        if(type.name().equals("TASK_CREATED")){
            //todo
            log.info("任务创建，这个时候可以处理一些业务逻辑");
        }
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
