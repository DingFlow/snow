package com.snow.framework.web.message.consumer;

import cn.hutool.core.bean.BeanUtil;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.common.core.domain.MessageEventRequest;
import com.snow.framework.web.message.MessageEventStrategy;
import com.snow.system.domain.SysMessageTransition;
import com.snow.system.service.ISysMessageTransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @program: snow 标记已读，针对消费者
 * @description 标记站内信已读
 * @author: 没用的阿吉
 * @create: 2021-03-30 14:11
 **/
@Component
public class SysMarkReadedStrategy implements MessageEventStrategy {

    @Autowired
    private ISysMessageTransitionService messageTransitionService=SpringUtils.getBean(ISysMessageTransitionService.class);

    @Override
    public void messageHandle(MessageEventRequest messageEvent) {
        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        BeanUtil.copyProperties(messageEvent,sysMessageTransition);
        sysMessageTransition.setMessageType(messageEvent.getMessageEventType().getCode());
        //标记为已读
        sysMessageTransition.setMessageReadStatus(1L);
        Set<String> consumerId = messageEvent.getConsumerIds();
        consumerId.forEach(t->{
            sysMessageTransition.setConsumerId(t);
            messageTransitionService.updateByCondition(sysMessageTransition);
        });

    }
}
