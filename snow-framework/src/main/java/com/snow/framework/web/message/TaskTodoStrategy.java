package com.snow.framework.web.message;

import cn.hutool.core.bean.BeanUtil;
import com.snow.common.enums.MessageEventType;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.common.core.domain.MessageEventRequest;
import com.snow.system.domain.SysMessageTransition;
import com.snow.system.service.ISysMessageTransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @program: snow
 * @description 待办处理
 * @author: 没用的阿吉
 * @create: 2021-03-30 14:11
 **/
@Component
public class TaskTodoStrategy implements MessageEventStrategy {


    @Autowired
    private ISysMessageTransitionService messageTransitionService=SpringUtils.getBean(ISysMessageTransitionService.class);

    @Override
    public void messageHandle(MessageEventRequest messageEvent) {
        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        BeanUtil.copyProperties(messageEvent,sysMessageTransition);
        sysMessageTransition.setMessageType(MessageEventType.TASK_TODO.getCode());

        Set<String> consumerId = messageEvent.getConsumerIds();
        consumerId.forEach(t->{
            sysMessageTransition.setConsumerId(t);
            messageTransitionService.insertSysMessageTransition(sysMessageTransition);
        });

    }
}
