package com.snow.dingtalk.sync;

import com.snow.common.enums.DingTalkListenerType;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-15 11:18
 **/
public class SyncSysInfoFactory {


    public ISyncSysInfo getSyncSysInfoService(DingTalkListenerType dingTalkListenerType){
        Integer type = dingTalkListenerType.getType();
        if(type==DingTalkListenerType.DEPARTMENT_CREATE.getType()){
            return new SyncSysDepartmentService();
        }else if(type==DingTalkListenerType.BPMS_TASK_CHANGE.getType()){
            return new SyncFlowService();
        }else if(type==DingTalkListenerType.USER_CREATE.getType()){
            return new SyncSysUserService();
        }
        return null;

    }
}
