package com.snow.framework.web.message.producer;

import cn.hutool.core.bean.BeanUtil;
import com.snow.common.enums.MessageEventType;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.framework.web.domain.common.MessageEventDTO;
import com.snow.framework.web.message.MessageEventStrategy;
import com.snow.system.domain.SysMessageTransition;
import com.snow.system.service.ISysMessageTransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-04-17 12:46
 **/
@Component
public class SysSendVisitLogStrategy  implements MessageEventStrategy {

    @Autowired
    private ISysMessageTransitionService messageTransitionService=SpringUtils.getBean(ISysMessageTransitionService.class);

    @Override
    public void messageHandle(MessageEventDTO messageEvent) {
        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        BeanUtil.copyProperties(messageEvent,sysMessageTransition);
        sysMessageTransition.setMessageType(MessageEventType.SEND_VISIT_LOG.getCode());

        Set<String> consumerId = messageEvent.getConsumerIds();
        consumerId.forEach(t->{
            sysMessageTransition.setConsumerId(t);
            messageTransitionService.insertSysMessageTransition(sysMessageTransition);
        });
    }
}
