package com.snow.dingtalk.listener;

import com.alibaba.fastjson.JSON;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.service.impl.BlackboardServiceImpl;
import com.snow.system.domain.SysNotice;
import com.snow.system.event.SyncEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-14 15:33
 **/
@Component
@Slf4j
public class BlackboardService implements ISyncDingTalkInfo {

    private BlackboardServiceImpl blackboardService=SpringUtils.getBean(BlackboardServiceImpl.class);
    @Override
    public void syncDingTalkInfoEvent(SyncEvent syncEvent) {
        log.info("调用公告管理传入的原始参数:{}",JSON.toJSONString(syncEvent));
        DingTalkListenerType eventType =(DingTalkListenerType) syncEvent.getT();
        Integer code = eventType.getCode();

        if(code.equals(DingTalkListenerType.BLACKBOARD_CREATE.getCode())){
            SysNotice sysNotice=(SysNotice)syncEvent.getSource();
            blackboardService.createBlackboard(sysNotice);
        }

        else if(code.equals(DingTalkListenerType.BLACKBOARD_UPDATE.getCode())){

            SysNotice sysNotice=(SysNotice)syncEvent.getSource();
            blackboardService.updateBlackboard(sysNotice);
        }

        else if(code.equals(DingTalkListenerType.BLACKBOARD_DELETE.getCode())){
            String id=(String) syncEvent.getSource();
            blackboardService.deleteBlackboard(id);
        }
    }
}
