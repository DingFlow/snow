package com.snow.flowable.listener;

import com.alibaba.fastjson.JSON;
import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.service.impl.FlowableServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.event.FlowableEngineEventImpl;
import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description: 全局事件监听
 * @date 2020/12/7 18:13
 */
@Component
@Slf4j
public class AbstractEventListener implements FlowableEventListener {
    @Autowired
    private FlowableServiceImpl flowableService;
    @Autowired
    private RepositoryService repositoryService;

    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        if (!(flowableEvent instanceof FlowableEntityEventImpl)) {
            return;
        }
        FlowableEntityEventImpl entityEvent = (FlowableEntityEventImpl) flowableEvent;
        log.info("监听到的事件类型：{}",entityEvent.getType());
        Object entity = entityEvent.getEntity();

        //是否是任务实体类
        if (!(entity instanceof TaskEntity)) {
            return;
        }

        TaskEntity taskEntity = (TaskEntity) entity;

        FlowableEngineEventType type = entityEvent.getType();
        log.info("监听到的事件类型：{}",type.name());
        String processInstanceId = entityEvent.getProcessInstanceId();
        if(type.name().equals("TASK_CREATED")){
            Object variableLocal = taskEntity.getVariableLocal(FlowConstants.IS_PASS);
            ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processInstanceId);
            if(processDefinition.getName().equals(FlowDefEnum.SNOW_OA_LEAVE.getCode())){
                HistoricProcessInstance historicProcessInstance = flowableService.getHistoricProcessInstanceById(processInstanceId);
                Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();
                processVariables.get(FlowConstants.IS_PASS);
                log.info("processVariables：{}",JSON.toJSONString(processVariables));

            }


        }

        if(type.name().equals("TASK_ASSIGNED")){
            //todo
            log.info("任务已分配，这个时候可以发送短信，邮件通知");
        }

        if(type.name().equals("TASK_COMPLETED")){
            //todo
            log.info("任务已完成，这个时候可以处理一些业务逻辑");
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
