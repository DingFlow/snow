package com.snow.dingtalk.sync;

import com.alibaba.fastjson.JSONObject;
import com.snow.common.enums.DingTalkListenerType;

public interface ISyncSysInfo {
    /**
     * 同步系统数据
     * @param jsonObject
     * @return
     */
    JSONObject SyncSysInfo(DingTalkListenerType dingTalkListenerType,JSONObject jsonObject);
}
