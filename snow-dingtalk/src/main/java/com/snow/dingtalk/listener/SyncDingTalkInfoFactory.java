package com.snow.dingtalk.listener;

import com.snow.common.enums.DingTalkListenerType;
import com.snow.system.event.SyncEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author qimingjin
 * @Title: 创建同步钉钉数据工厂类
 * @Description:
 * @date 2020/9/28 9:43
 */
@Slf4j
public class SyncDingTalkInfoFactory  {



    public ISyncDingTalkInfo getSyncDingTalkService(SyncEvent syncEvent){

        Integer eventType = syncEvent.getEventType();
        if(eventType.equals(DingTalkListenerType.DEPARTMENT_CREATE.getCode())){
            return  new DepartmentCreateEventService();
        }
        else if(eventType.equals(DingTalkListenerType.USER_CREATED.getCode())){
            return new UserCreateEventService();
        }else {
            throw new RuntimeException("不存在的监听类型");
        }

    }
}
