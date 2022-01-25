package com.snow.system.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author qimingjin
 * @Title: 同步事件监听器
 * @Description:
 * @date 2020/9/17 17:34
 */
public class SyncEvent<T> extends ApplicationEvent {

    private Integer eventType;

    private T t;


    public SyncEvent(Object source) {
        super(source);
    }


    public SyncEvent(Object source,T t) {
        super(source);
        this.t=t;
    }
    public SyncEvent(Object source,Integer eventType) {
        super(source);
        this.eventType = eventType;

    }

    public SyncEvent(Integer eventType,Object source,T t) {
        super(source);
        this.eventType = eventType;
        this.t=t;

    }


    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

}
