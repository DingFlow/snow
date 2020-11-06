package com.snow.dingtalk.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.snow.common.constant.Constants;
import com.snow.common.utils.StringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.service.CallBackService;
import com.snow.system.domain.DingtalkCallBack;
import com.taobao.api.ApiException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/3 11:19
 */
@Service
public class CallBackServiceImpl extends BaseService implements CallBackService {

    public static final String TOKEN="call_back_dingtalk_token";

    @Override
    public void registerCallBack(DingtalkCallBack dingtalkCallBack) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.REGISTER_CALL_BACK);
        OapiCallBackRegisterCallBackRequest request = new OapiCallBackRegisterCallBackRequest();
        request.setUrl(dingtalkCallBack.getUrl());
        request.setAesKey(dingtalkCallBack.getAesKey());
        request.setToken(dingtalkCallBack.getToken());
        request.setCallBackTag(dingtalkCallBack.getEventNameList());
        try {
            OapiCallBackRegisterCallBackResponse response = client.execute(request,getCallBackDingTalkToken(dingtalkCallBack));
            if(response.getErrcode()==0){
                syncDingTalkErrorOperLog(BaseConstantUrl.REGISTER_CALL_BACK,response.getMessage(),"registerCallBack()", JSON.toJSONString(request));
            }else {
                //记录获取token失败日志
                syncDingTalkErrorOperLog(BaseConstantUrl.REGISTER_CALL_BACK,response.getErrmsg(),"registerCallBack()", JSON.toJSONString(request));
            }
        } catch (ApiException e) {
            syncDingTalkErrorOperLog(BaseConstantUrl.REGISTER_CALL_BACK,e.getMessage(),"registerCallBack()", JSON.toJSONString(request));
            e.printStackTrace();
        }
    }

    @Override
    public void updateCallBack(DingtalkCallBack dingtalkCallBack) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.UPDATE_CALL_BACK);
        OapiCallBackUpdateCallBackRequest request = new OapiCallBackUpdateCallBackRequest();
        request.setUrl(dingtalkCallBack.getUrl());
        request.setAesKey(dingtalkCallBack.getAesKey());
        request.setToken(dingtalkCallBack.getToken());
        request.setCallBackTag(dingtalkCallBack.getEventNameList());
        try {
            OapiCallBackUpdateCallBackResponse response = client.execute(request,getCallBackDingTalkToken(dingtalkCallBack));
            if(response.getErrcode()==0){
                syncDingTalkErrorOperLog(BaseConstantUrl.UPDATE_CALL_BACK,response.getMessage(),"updateCallBack()", JSON.toJSONString(request));
            }else {
                //记录获取token失败日志
                syncDingTalkErrorOperLog(BaseConstantUrl.UPDATE_CALL_BACK,response.getErrmsg(),"updateCallBack()", JSON.toJSONString(request));
            }
        } catch (ApiException e) {
            syncDingTalkErrorOperLog(BaseConstantUrl.UPDATE_CALL_BACK,e.getMessage(),"updateCallBack()", JSON.toJSONString(request));
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCallBack(DingtalkCallBack dingtalkCallBack) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.DELETE_CALL_BACK);
        OapiCallBackDeleteCallBackRequest request = new OapiCallBackDeleteCallBackRequest();
        request.setHttpMethod("GET");
        try {
            OapiCallBackDeleteCallBackResponse response = client.execute(request, getDingTalkToken());
            if(response.getErrcode()==0){
                syncDingTalkErrorOperLog(BaseConstantUrl.DELETE_CALL_BACK,response.getMessage(),"deleteCallBack()", JSON.toJSONString(request));
            }else {
                //记录获取token失败日志
                syncDingTalkErrorOperLog(BaseConstantUrl.DELETE_CALL_BACK,response.getErrmsg(),"deleteCallBack()", JSON.toJSONString(request));
            }
        } catch (ApiException e) {
            syncDingTalkErrorOperLog(BaseConstantUrl.DELETE_CALL_BACK,e.getMessage(),"deleteCallBack()", JSON.toJSONString(request));
            e.printStackTrace();
        }
    }

    @Override
    public void getCallBackFailedResult() {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.CALL_BACK_FAILED_RESULT);
        OapiCallBackGetCallBackFailedResultRequest request = new OapiCallBackGetCallBackFailedResultRequest();
        request.setHttpMethod("GET");
        try {
            OapiCallBackGetCallBackFailedResultResponse response = client.execute(request, getDingTalkToken());
            if(response.getErrcode()==0){
                syncDingTalkErrorOperLog(BaseConstantUrl.DELETE_CALL_BACK,response.getMessage(),"getCallBackFailedResult()", JSON.toJSONString(request));
            }else {
                //记录获取token失败日志
                syncDingTalkErrorOperLog(BaseConstantUrl.DELETE_CALL_BACK,response.getErrmsg(),"getCallBackFailedResult()", JSON.toJSONString(request));
            }
        } catch (ApiException e) {
            syncDingTalkErrorOperLog(BaseConstantUrl.DELETE_CALL_BACK,e.getMessage(),"getCallBackFailedResult()", JSON.toJSONString(request));
            e.printStackTrace();
        }
    }
    /**
     * 获取token
     * @return
     */
    public String getCallBackDingTalkToken(DingtalkCallBack dingtalkCallBack){
        //创建缓存，缓存默认是7100S
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(7100);
        if(StringUtils.isEmpty(timedCache.get(TOKEN))){
            DefaultDingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_TOKEN_URL);
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(dingtalkCallBack.getAppKey());
            request.setAppsecret(dingtalkCallBack.getAppSecret());
            request.setHttpMethod(Constants.GET);
            try {
                OapiGettokenResponse response = client.execute(request);
                if(response.getErrcode()==0){
                    timedCache.put(TOKEN,response.getAccessToken());
                    syncDingTalkErrorOperLog(BaseConstantUrl.GET_TOKEN_URL,response.getMessage(),"getCallBackDingTalkToken()", JSON.toJSONString(request));
                    return response.getAccessToken();
                }else {
                    //记录获取token失败日志
                    syncDingTalkErrorOperLog(BaseConstantUrl.GET_TOKEN_URL,response.getErrmsg(),"getCallBackDingTalkToken()", JSON.toJSONString(request));
                    return null;
                }
            } catch (ApiException e) {
                syncDingTalkErrorOperLog(BaseConstantUrl.GET_TOKEN_URL,e.getMessage(),"getCallBackDingTalkToken()",JSON.toJSONString(request));
                e.printStackTrace();
            }
            return null;
        }else {
            return timedCache.get(TOKEN);
        }

    }
}

