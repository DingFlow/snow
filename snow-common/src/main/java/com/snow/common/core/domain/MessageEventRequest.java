package com.snow.common.core.domain;

import com.snow.common.enums.MessageEventType;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @program: snow
 * @description 一次消息会话只存在一个生产者，可存在多个消费者（类似与广播事件）。每种类型事件保证外部id不重复
 * @author: 没用的阿吉
 * @create: 2021-03-30 13:46
 **/
@Data
public class MessageEventRequest extends ApplicationEvent implements Serializable {

    private static final long serialVersionUID = -8404237283199119018L;

    /**
     * 消息id
     */
    private Long messageId;

    /** 生产者id */
    private String producerId;

    /** 消费者id */
    private Set<String> consumerIds;

    /** 消息外部id */
    private String messageOutsideId;

    /**
     * 模板code
     */
    private String templateCode;


    /**
     * 发送人
     */
    private String from;
    /**
     * 参数map
     */
    private Map<String,Object> paramMap;


    /**
     * pc端url
     */
    private String pcUrl;

    /**
     * app端url
     */
    private String appUrl;
    /**
     * 事件类型
     */
    private MessageEventType messageEventType;

    /**
     * 消息来源
     */
    private Integer messageShow;

    public MessageEventRequest(Object source) {
        super(source);
    }
}
