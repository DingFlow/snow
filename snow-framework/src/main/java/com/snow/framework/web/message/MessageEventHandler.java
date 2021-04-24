package com.snow.framework.web.message;

import com.snow.common.utils.StringUtils;
import com.snow.framework.web.domain.common.MessageEventDTO;
import com.snow.framework.web.message.consumer.SysMarkReadedStrategy;
import com.snow.framework.web.message.producer.SysSendEmailStrategy;
import com.snow.framework.web.message.producer.SysSendVisitLogStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @program: snow
 * @description 消息事件处理器
 * @author: 没用的阿吉
 * @create: 2021-03-30 13:45
 **/
@Component
@Slf4j
public class MessageEventHandler implements ApplicationListener<MessageEventDTO> {

    @Override
    public void onApplicationEvent(MessageEventDTO messageEvent) {
        String messageEventType=String.valueOf(messageEvent.getSource());
       // MessageEventType messageEventType = messageEvent.getMessageEventType();
        if(StringUtils.isNull(messageEventType)){
            log.warn("没有传入消息事件类型");
            return;
        }
        MessageEventContext messageEventContext=new MessageEventContext();

        switch (messageEventType){
            case "TASK_TODO":
                messageEventContext.setMessageEventTypeStrategy(new TaskTodoStrategy());
                break;
            case "SEND_EMAIL":
                messageEventContext.setMessageEventTypeStrategy(new SysSendEmailStrategy());
                break;
            case "MARK_READED":
                messageEventContext.setMessageEventTypeStrategy(new SysMarkReadedStrategy());
                break;
            case "SEND_VISIT_LOG":
                messageEventContext.setMessageEventTypeStrategy(new SysSendVisitLogStrategy());
                break;
            default:


        }

        //执行
        messageEventContext.messageHandle(messageEvent);
    }
}
