package com.snow.dingtalk.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.snow.common.annotation.DingTalkSyncLog;
import com.snow.common.constant.Constants;
import com.snow.common.enums.DingTalkBusinessType;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.enums.DingTalkSyncType;
import com.snow.common.exception.DingTalkSyncException;
import com.snow.common.utils.StringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.service.CallBackService;
import com.snow.system.domain.DingtalkCallBack;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/3 11:19
 */
@Service
@Slf4j
public class CallBackServiceImpl extends BaseService implements CallBackService {


    @Override
    @DingTalkSyncLog(dingTalkListenerType = DingTalkListenerType.CALL_BACK_REGISTER,dingTalkUrl=BaseConstantUrl.REGISTER_CALL_BACK,dingTalkSyncType=DingTalkSyncType.AUTOMATIC)
    public void registerCallBack(DingtalkCallBack dingtalkCallBack) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.REGISTER_CALL_BACK);
        OapiCallBackRegisterCallBackRequest request = new OapiCallBackRegisterCallBackRequest();
        request.setUrl(dingtalkCallBack.getUrl());
        request.setAesKey(dingtalkCallBack.getAesKey());
        request.setToken(dingtalkCallBack.getToken());
        request.setCallBackTag(dingtalkCallBack.getEventNameList());
        try {
            OapiCallBackRegisterCallBackResponse response = client.execute(request,getDingTalkToken());
            if(response.getErrcode()!=0){
                throw new DingTalkSyncException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("注册钉钉回调registerCallBack异常：{}",e.getMessage());
            throw new DingTalkSyncException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    @Override
    @DingTalkSyncLog(dingTalkListenerType = DingTalkListenerType.CALL_BACK_UPDATE,dingTalkUrl=BaseConstantUrl.UPDATE_CALL_BACK,dingTalkSyncType=DingTalkSyncType.AUTOMATIC)
    public Boolean updateCallBack(DingtalkCallBack dingtalkCallBack) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.UPDATE_CALL_BACK);
        OapiCallBackUpdateCallBackRequest request = new OapiCallBackUpdateCallBackRequest();
        request.setUrl(dingtalkCallBack.getUrl());
        request.setAesKey(dingtalkCallBack.getAesKey());
        request.setToken(dingtalkCallBack.getToken());
        request.setCallBackTag(dingtalkCallBack.getEventNameList());
        try {
            OapiCallBackUpdateCallBackResponse response = client.execute(request,getDingTalkToken());
            if(response.getErrcode()==0){
                return response.isSuccess();
            }else {
                throw new DingTalkSyncException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("更新钉钉回调updateCallBack异常：{}",e.getMessage());
            throw new DingTalkSyncException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    @Override
    @DingTalkSyncLog(dingTalkListenerType = DingTalkListenerType.CALL_BACK_DELETE,dingTalkUrl=BaseConstantUrl.DELETE_CALL_BACK,dingTalkSyncType=DingTalkSyncType.AUTOMATIC)
    public void deleteCallBack(DingtalkCallBack dingtalkCallBack) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.DELETE_CALL_BACK);
        OapiCallBackDeleteCallBackRequest request = new OapiCallBackDeleteCallBackRequest();
        request.setHttpMethod("GET");
        try {
            OapiCallBackDeleteCallBackResponse response = client.execute(request, getDingTalkToken());
            if(response.getErrcode()!=0){
                throw new DingTalkSyncException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("删除钉钉回调deleteCallBack异常：{}",e.getMessage());
            throw new DingTalkSyncException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    @Override
    public List<OapiCallBackGetCallBackFailedResultResponse.Failed> getCallBackFailedResult() {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.CALL_BACK_FAILED_RESULT);
        OapiCallBackGetCallBackFailedResultRequest request = new OapiCallBackGetCallBackFailedResultRequest();
        request.setHttpMethod("GET");
        try {
            OapiCallBackGetCallBackFailedResultResponse response = client.execute(request, getDingTalkToken());
            if(response.getErrcode()==0){
                List<OapiCallBackGetCallBackFailedResultResponse.Failed> failedList = response.getFailedList();
                syncDingTalkSuccessOperLog(BaseConstantUrl.CALL_BACK_FAILED_RESULT,response.getMessage(),"getCallBackFailedResult()", JSON.toJSONString(request));
                return failedList;
            }else {
                //记录获取token失败日志
                syncDingTalkErrorOperLog(BaseConstantUrl.CALL_BACK_FAILED_RESULT,response.getErrmsg(),"getCallBackFailedResult()", JSON.toJSONString(request));
            }
        } catch (ApiException e) {
            syncDingTalkErrorOperLog(BaseConstantUrl.CALL_BACK_FAILED_RESULT,e.getMessage(),"getCallBackFailedResult()", JSON.toJSONString(request));
            e.printStackTrace();
        }
        return null;
    }
}

