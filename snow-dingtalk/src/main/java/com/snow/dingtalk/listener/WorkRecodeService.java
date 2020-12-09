package com.snow.dingtalk.listener;

import com.alibaba.fastjson.JSON;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.model.WorkrecordAddRequest;
import com.snow.dingtalk.service.impl.WorkRecodeServiceImpl;
import com.snow.system.event.SyncEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/12/9 15:03
 */
@Component
@Slf4j
public class WorkRecodeService implements ISyncDingTalkInfo {
    private WorkRecodeServiceImpl workRecodeService=SpringUtils.getBean(WorkRecodeServiceImpl.class);
    @Override
    public void syncDingTalkInfoEvent(SyncEvent syncEvent) {
        log.info("调用工作通知传入的原始参数:{}",JSON.toJSONString(syncEvent));
        DingTalkListenerType eventType =(DingTalkListenerType) syncEvent.getT();
        Integer code = eventType.getCode();
        if(code.equals(DingTalkListenerType.WORK_RECODE_CREATE.getCode())){
            WorkrecordAddRequest workrecordAddRequest=(WorkrecordAddRequest)syncEvent.getSource();
            workRecodeService.create(workrecordAddRequest);
        }
    }
}
