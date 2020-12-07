package com.snow.flowable.listener;

import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;

/**
 * @author qimingjin
 * @Title:
 * @Description: 全局事件监听
 * @date 2020/12/7 18:13
 */
public class AbstartctEventListener implements FlowableEventListener {
    @Override
    public void onEvent(FlowableEvent flowableEvent) {

    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}
