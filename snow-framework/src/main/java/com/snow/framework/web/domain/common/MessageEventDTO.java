package com.snow.framework.web.domain.common;

import com.snow.common.enums.MessageEventType;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.Set;

/**
 * @program: snow
 * @description 一次消息会话只存在一个生产者，可存在多个消费者（类似与广播事件）。每种类型事件保证外部id不重复
 * @author: 没用的阿吉
 * @create: 2021-03-30 13:46
 **/
@Data
public class MessageEventDTO extends ApplicationEvent implements Serializable {


    private static final long serialVersionUID = -8404237283199119018L;

    /** 生产者id */
    private String producerId;

    /** 消费者id */
    private Set<String> consumerIds;

    /** 消息外部id */
    private String messageOutsideId;
    /**
     * 事件类型
     */
    private MessageEventType messageEventType;

    public MessageEventDTO(Object source) {
        super(source);
    }
}
