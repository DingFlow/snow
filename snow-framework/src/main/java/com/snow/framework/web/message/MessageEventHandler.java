package com.snow.framework.web.message;

import com.snow.common.utils.StringUtils;
import com.snow.common.core.domain.MessageEventRequest;
import com.snow.framework.web.message.consumer.SysMarkReadedStrategy;
import com.snow.framework.web.message.producer.InnerMessageStrategy;
import com.snow.framework.web.message.producer.SendMessageCenterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @program: snow
 * @description 消息事件处理器
 * @author: 没用的阿吉
 * @create: 2021-03-30 13:45
 **/
@Component
@Slf4j
public class MessageEventHandler implements ApplicationListener<MessageEventRequest> {

    @Override
    @Async
    public void onApplicationEvent(MessageEventRequest messageEvent) {
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
                messageEventContext.setMessageEventTypeStrategy(new SendMessageCenterStrategy());
                break;
            case "SEND_VISIT_LOG":
            case "REGISTER_ACCOUNT_SUCCESS":
            case "INNER_TASK_TODO":
            case "INNER_PROCESS_END":
            case "INNER_SYS_TODO_TASK":
            case "INNER_SYS_TASK_COMPLETE":
                messageEventContext.setMessageEventTypeStrategy(new InnerMessageStrategy());
                break;
            case "MARK_READED":
                messageEventContext.setMessageEventTypeStrategy(new SysMarkReadedStrategy());
                break;
            default:


        }

        //执行
        messageEventContext.messageHandle(messageEvent);
    }

}
