package com.snow.dingtalk.listener;

import com.snow.common.enums.DingTalkListenerType;
import com.snow.system.event.SyncEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qimingjin
 * @Title: 创建同步钉钉数据工厂类
 * @Description:
 * @date 2020/9/28 9:43
 */
@Slf4j
public class SyncDingTalkInfoFactory  {



    public ISyncDingTalkInfo getSyncDingTalkService(SyncEvent syncEvent){

        DingTalkListenerType dingTalkEnum = (DingTalkListenerType) syncEvent.getT();
        Integer type = dingTalkEnum.getType();

        if(type.equals(DingTalkListenerType.DEPARTMENT_CREATE.getType())){
            return  new DepartmentEventService();
        }
        else if(type.equals(DingTalkListenerType.USER_CREATE.getType())){
            return new UserEventService();
        }
        else if(type.equals(DingTalkListenerType.CALL_BACK_REGISTER.getType())){
            return new CallBackService();
        }
        else if(type.equals(DingTalkListenerType.WORK_RECODE_CREATE.getType())){
            return new WorkRecodeEventService();
        }
        else if(type.equals(DingTalkListenerType.BLACKBOARD_CREATE.getType())){
            return new BlackboardService();
        }

        else {
            throw new RuntimeException("不存在的监听类型");
        }

    }
}
