package com.snow.dingtalk.service;

import com.dingtalk.api.response.OapiCallBackGetCallBackFailedResultResponse;
import com.snow.system.domain.DingtalkCallBack;

import java.util.List;

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

    /**
     * 更新事件
     * @param dingtalkCallBack
     */
    Boolean updateCallBack(DingtalkCallBack dingtalkCallBack);

    /**
     * 删除事件
     *
     */
    void deleteCallBack();

    /**
     * 获取回调失败结果
     */
    List<OapiCallBackGetCallBackFailedResultResponse.Failed> getCallBackFailedResult();
}
