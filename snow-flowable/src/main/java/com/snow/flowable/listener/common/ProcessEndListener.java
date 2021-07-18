package com.snow.flowable.listener.common;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.snow.common.constant.MessageConstants;
import com.snow.common.enums.MessageEventType;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.service.FlowableService;
import com.snow.framework.web.domain.common.MessageEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @author qimingjin
 * @Title: 流程完结监听
 * @Description:
 * @date 2021/7/16 13:29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessEndListener implements FlowableEventListener {

    @Resource
    private FlowableService flowableService;

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
                    HistoricProcessInstance processInstance = flowableService.getHistoricProcessInstanceById(flowableEngineEvent.getProcessInstanceId());
                    //发送站内信
                    sendInnerMessage(processInstance);
                }
            }


        }

    }

    private void sendInnerMessage(HistoricProcessInstance processInstance){
        MessageEventDTO messageEventDTO=new MessageEventDTO(MessageEventType.INNER_PROCESS_END.getCode());
        messageEventDTO.setProducerId(String.valueOf(0));
        messageEventDTO.setConsumerIds(Sets.newHashSet(String.valueOf(processInstance.getStartUserId())));
        messageEventDTO.setMessageEventType(MessageEventType.INNER_PROCESS_END);
        messageEventDTO.setMessageOutsideId(processInstance.getId());
        messageEventDTO.setMessageShow(2);
        //计算流程用时
        String spendTime= DateUtil.formatBetween(processInstance.getStartTime(), new Date(), BetweenFormater.Level.SECOND);
        Map<String,Object> map= Maps.newHashMap();
        map.put("businessKey", processInstance.getBusinessKey());
        map.put("startTime", DateUtil.formatDateTime(processInstance.getStartTime()));
        map.put("processInstance", processInstance.getProcessDefinitionName());
        map.put("spendTime", spendTime);
        map.put("processInstanceId", processInstance.getId());
        messageEventDTO.setParamMap(map);
        messageEventDTO.setTemplateCode(MessageConstants.INNER_PROCESS_END_CODE);
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
