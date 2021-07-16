package com.snow.flowable.listener.common;

import cn.hutool.core.util.ObjectUtil;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.service.FlowableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/7/16 13:29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessEndListener implements FlowableEventListener {

    @Resource
    private FlowableService flowableService;

    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        if (flowableEvent instanceof FlowableEngineEvent) {
            FlowableEngineEvent flowableEngineEvent = (FlowableEngineEvent) flowableEvent;
            ProcessDefinitionEntity processDefinition = flowableService.getProcessDefinition(flowableEngineEvent);
            if(ObjectUtil.isNull(processDefinition)){
                return;
            }
            String key = Optional.ofNullable(processDefinition.getKey()).orElse("");
            for (FlowDefEnum flowDefEnum : flowableService.getAllFlowDefEnumsSet()) {
                //在流程中存在的才监听
                if (flowDefEnum.getCode().equals(key)) {
                   //发送站内信
                }
            }


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

    /**
     * 获取流程定义信息
     *
     * @param event
     * @return
     */
    protected ProcessDefinitionEntity getProcessDefinition(FlowableEngineEvent event) {
        String processDefinitionId = event.getProcessDefinitionId();
        if (processDefinitionId != null) {
            CommandContext commandContext = CommandContextUtil.getCommandContext();
            if (commandContext != null) {
                return CommandContextUtil.getProcessDefinitionEntityManager(commandContext).findById(processDefinitionId);
            }
        }
        return null;
    }
}
