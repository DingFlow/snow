package com.snow.flowable.listener.common;

import lombok.RequiredArgsConstructor;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventDispatcher;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

/**
 * Flowable全局监听器配置
 *
 */
@Configuration
@RequiredArgsConstructor
public class GlobalListenerConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final SpringProcessEngineConfiguration configuration;

    private final TaskCreateListener taskCreateListener;

    private final ProcessEndListener processEndListener;

    private final ProcessStartListener processStartListener;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        FlowableEventDispatcher dispatcher = configuration.getEventDispatcher();
        //流程开始
        dispatcher.addEventListener(processStartListener, FlowableEngineEventType.PROCESS_STARTED);
        // 流程结束事件
        dispatcher.addEventListener(processEndListener, FlowableEngineEventType.PROCESS_COMPLETED);
        //任务创建
        dispatcher.addEventListener(taskCreateListener, FlowableEngineEventType.TASK_CREATED);
    }

}
