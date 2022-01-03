package com.snow.framework.web.message;

import com.snow.common.core.domain.MessageEventRequest;

/**
 * @program: snow
 * @description 策略执行MessageEventContext类
 * @author: 没用的阿吉
 * @create: 2021-03-30 14:12
 **/
public class MessageEventContext {

    private MessageEventStrategy strategy;

    public void setMessageEventTypeStrategy(MessageEventStrategy strategy) {
        this.strategy = strategy;
    }

    public void messageHandle(MessageEventRequest messageEventDTO) {
        this.strategy.messageHandle(messageEventDTO);
    }
}
