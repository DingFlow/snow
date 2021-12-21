package com.snow.flowable.listener.common;

import cn.hutool.core.util.ObjectUtil;
import com.snow.common.core.domain.ProcessEventRequest;
import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.common.enums.FlowTypeEnum;
import com.snow.flowable.service.FlowableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.FlowableProcessStartedEvent;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qimingjin
 * @Title: 流程完结监听
 * @Description:
 * @date 2021/7/16 13:29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessStartListener implements FlowableEventListener {

    @Resource
    private FlowableService flowableService;

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        if (flowableEvent instanceof FlowableProcessStartedEvent) {
            FlowableProcessStartedEvent flowableProcessStartedEvent = (FlowableProcessStartedEvent) flowableEvent;

            ExecutionEntity execution = (ExecutionEntity) flowableProcessStartedEvent.getEntity();
            ProcessInstance processInstance = execution.getProcessInstance();

            //修改表单流程的状态---针对表单流程
            ProcessEventRequest processEventRequest=new ProcessEventRequest(processInstance);
            processEventRequest.setBusinessKey(processInstance.getBusinessKey());
            processEventRequest.setProcessStatus(ProcessStatus.CHECKING.name());
            applicationContext.publishEvent(processEventRequest);

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
