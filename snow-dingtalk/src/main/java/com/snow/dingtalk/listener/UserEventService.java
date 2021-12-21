package com.snow.dingtalk.listener;

import com.alibaba.fastjson.JSON;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.service.impl.UserServiceImpl;
import com.snow.system.domain.SysUser;
import com.snow.system.event.SyncEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author qimingjin
 * @Title: 创建用户
 * @Description:
 * @date 2020/9/28 9:34
 */
@Component
@Slf4j
public class UserEventService implements ISyncDingTalkInfo {
    private UserServiceImpl userService=SpringUtils.getBean("userServiceImpl");

    @Override
    public void syncDingTalkInfoEvent(SyncEvent syncEvent) {
        log.info("调用钉钉用户回调传入的原始参数:{}"+JSON.toJSONString(syncEvent));
        DingTalkListenerType eventType =(DingTalkListenerType) syncEvent.getT();
        Integer code = eventType.getCode();
        if(code.equals(DingTalkListenerType.USER_CREATE.getCode())){
            userService.createUser((SysUser) syncEvent.getSource());
        }
        else if( code.equals(DingTalkListenerType.USER_DELETE.getCode())){
            userService.deleteUser((String) syncEvent.getSource());
        }
        else if(code.equals(DingTalkListenerType.USER_UPDATE.getCode())){
            userService.updateUserV2((SysUser) syncEvent.getSource());
        }

    }
}
