package com.snow.framework.web.message;

import com.snow.common.core.domain.MessageEventRequest;

/**
 * @program: snow
 * @description 消息事件类型策略
 * @author: 没用的阿吉
 * @create: 2021-03-30 14:04
 **/
public interface MessageEventStrategy {

    /**
     * 消息处理
     */
    void messageHandle(MessageEventRequest messageEventDTO);
}
