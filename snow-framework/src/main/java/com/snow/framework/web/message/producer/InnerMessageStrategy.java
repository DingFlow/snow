package com.snow.framework.web.message.producer;

import com.snow.common.utils.spring.SpringUtils;
import com.snow.framework.web.domain.common.MessageEventDTO;
import com.snow.framework.web.domain.common.SysSendMessageDTO;
import com.snow.framework.web.message.MessageEventStrategy;
import com.snow.framework.web.service.InnerMessageService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InnerMessageStrategy  implements MessageEventStrategy {

    @Resource
    private InnerMessageService innerMessageService= SpringUtils.getBean(InnerMessageService.class);

    @Override
    public void messageHandle(MessageEventDTO messageEventDTO) {
        SysSendMessageDTO sysSendMessageDTO= SysSendMessageDTO.builder().from(messageEventDTO.getProducerId())
                .receiverSet(messageEventDTO.getConsumerIds())
                .paramMap(messageEventDTO.getParamMap())
                .appUrl(messageEventDTO.getAppUrl())
                .pcUrl(messageEventDTO.getPcUrl())
                .templateByCode(messageEventDTO.getTemplateCode())
                .messageEventType(messageEventDTO.getMessageEventType())
                .messageShow(messageEventDTO.getMessageShow())
                .build();
        innerMessageService.sendInnerMessage(sysSendMessageDTO);
    }
}
