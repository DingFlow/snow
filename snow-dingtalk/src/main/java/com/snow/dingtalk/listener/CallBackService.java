package com.snow.dingtalk.listener;

import com.alibaba.fastjson.JSON;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.service.impl.CallBackServiceImpl;
import com.snow.system.domain.DingtalkCallBack;
import com.snow.system.event.SyncEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/3 17:05
 */
@Component
@Slf4j
public class CallBackService implements ISyncDingTalkInfo {

    private CallBackServiceImpl callBackServiceImpl=SpringUtils.getBean("callBackServiceImpl");

    @Override
    public void syncDingTalkInfoEvent(SyncEvent syncEvent) {
        log.info("调用创建钉钉注册回调传入的原始参数:{}"+JSON.toJSONString(syncEvent));
        Enum eventType =(Enum) syncEvent.getT();
        if(eventType.equals(DingTalkListenerType.CALL_BACK_REGISTER.getCode())){
            callBackServiceImpl.registerCallBack((DingtalkCallBack) syncEvent.getT());
        }
        else if( eventType.equals(DingTalkListenerType.CALL_BACK_UPDATE.getCode())){
            callBackServiceImpl.updateCallBack((DingtalkCallBack) syncEvent.getT());
        }

    }
}
