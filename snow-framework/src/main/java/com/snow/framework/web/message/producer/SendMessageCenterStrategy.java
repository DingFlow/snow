package com.snow.framework.web.message.producer;

import cn.hutool.core.bean.BeanUtil;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.common.core.domain.MessageEventRequest;
import com.snow.framework.web.message.MessageEventStrategy;
import com.snow.system.domain.SysMessageTransition;
import com.snow.system.service.ISysMessageTransitionService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 发送消息到消息中心策略
 */
@Component
@Deprecated
public class SendMessageCenterStrategy  implements MessageEventStrategy {
    @Resource
    private ISysMessageTransitionService messageTransitionService= SpringUtils.getBean(ISysMessageTransitionService.class);

    @Override
    public void messageHandle(MessageEventRequest messageEventDTO) {
        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        BeanUtil.copyProperties(messageEventDTO,sysMessageTransition);
        sysMessageTransition.setMessageType(messageEventDTO.getMessageEventType().getCode());

        Set<String> consumerId = messageEventDTO.getConsumerIds();
        consumerId.forEach(t->{
            sysMessageTransition.setConsumerId(t);
            messageTransitionService.insertSysMessageTransition(sysMessageTransition);
        });
    }
}
