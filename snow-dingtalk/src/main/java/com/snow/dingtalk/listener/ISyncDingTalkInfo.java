package com.snow.dingtalk.listener;

import com.snow.system.event.SyncEvent;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/28 9:31
 */
public interface ISyncDingTalkInfo {
    /**
     * 同步钉钉事件
     * @param syncEvent
     */
    void syncDingTalkInfoEvent(SyncEvent syncEvent);
}
