package com.snow.dingtalk.listener;

import com.alibaba.fastjson.JSON;
import com.snow.system.event.SyncEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author qimingjin
 * @Title: 同步事件监听器工厂
 * @Description:
 * @date 2020/9/17 17:40
 */
@Component
@Slf4j
public class SyncEventListener implements ApplicationListener<SyncEvent> {

    @Override
    public void onApplicationEvent(SyncEvent syncEvent) {
        log.info("进入监听器.....");
        SyncDingTalkInfoFactory syncEventListenerFactory = new SyncDingTalkInfoFactory();
        ISyncDingTalkInfo syncDingTalkService = syncEventListenerFactory.getSyncDingTalkService(syncEvent);
        syncDingTalkService.syncDingTalkInfoEvent(syncEvent);
        log.info("监听到的事件类型:"+JSON.toJSONString(syncEvent));
    }

 
}
