package com.snow.framework.web.message.producer;

import com.snow.common.utils.spring.SpringUtils;
import com.snow.common.core.domain.MessageEventRequest;
import com.snow.framework.web.domain.common.SysSendMessageRequest;
import com.snow.framework.web.message.MessageEventStrategy;
import com.snow.framework.web.service.InnerMessageService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InnerMessageStrategy  implements MessageEventStrategy {

    @Resource
    private InnerMessageService innerMessageService= SpringUtils.getBean(InnerMessageService.class);

    @Override
    public void messageHandle(MessageEventRequest messageEventDTO) {
        SysSendMessageRequest sysSendMessageDTO= SysSendMessageRequest.builder().from(messageEventDTO.getProducerId())
                .receiverSet(messageEventDTO.getConsumerIds())
                .paramMap(messageEventDTO.getParamMap())
                .appUrl(messageEventDTO.getAppUrl())
                .pcUrl(messageEventDTO.getPcUrl())
                .templateByCode(messageEventDTO.getTemplateCode())
                .messageOutsideId(messageEventDTO.getMessageOutsideId())
                .messageEventType(messageEventDTO.getMessageEventType())
                .messageShow(messageEventDTO.getMessageShow())
                .build();
        innerMessageService.sendInnerMessage(sysSendMessageDTO);
    }
}
