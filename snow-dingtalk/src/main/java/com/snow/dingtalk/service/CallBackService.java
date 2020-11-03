package com.snow.dingtalk.service;

import com.snow.system.domain.DingtalkCallBack;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/3 11:18
 */
public interface CallBackService {
    /**
     * 注册事件
     */
    void registerCallBack(DingtalkCallBack dingtalkCallBack);
}
