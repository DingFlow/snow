package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.snow.common.annotation.DingTalkLog;
import com.snow.common.enums.DingTalkLogType;
import com.snow.common.enums.DingTalkSyncType;
import com.snow.common.exception.SyncDataException;
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
    @DingTalkLog(dingTalkLogType = DingTalkLogType.CALL_BACK_REGISTER,dingTalkUrl=BaseConstantUrl.REGISTER_CALL_BACK,dingTalkSyncType=DingTalkSyncType.AUTOMATIC)
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
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("注册钉钉回调registerCallBack异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.CALL_BACK_UPDATE,dingTalkUrl=BaseConstantUrl.UPDATE_CALL_BACK,dingTalkSyncType=DingTalkSyncType.AUTOMATIC)
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
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("更新钉钉回调updateCallBack异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.CALL_BACK_DELETE,dingTalkUrl=BaseConstantUrl.DELETE_CALL_BACK,dingTalkSyncType=DingTalkSyncType.AUTOMATIC)
    public void deleteCallBack() {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.DELETE_CALL_BACK);
        OapiCallBackDeleteCallBackRequest request = new OapiCallBackDeleteCallBackRequest();
        request.setHttpMethod("GET");
        try {
            OapiCallBackDeleteCallBackResponse response = client.execute(request, getDingTalkToken());
            if(response.getErrcode()!=0){
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("删除钉钉回调deleteCallBack异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.CALL_BACK_FAILED_RESULT,dingTalkUrl=BaseConstantUrl.CALL_BACK_FAILED_RESULT,dingTalkSyncType=DingTalkSyncType.AUTOMATIC)
    public List<OapiCallBackGetCallBackFailedResultResponse.Failed> getCallBackFailedResult() {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.CALL_BACK_FAILED_RESULT);
        OapiCallBackGetCallBackFailedResultRequest request = new OapiCallBackGetCallBackFailedResultRequest();
        request.setHttpMethod("GET");
        try {
            OapiCallBackGetCallBackFailedResultResponse response = client.execute(request, getDingTalkToken());
            if(response.getErrcode()==0){
                List<OapiCallBackGetCallBackFailedResultResponse.Failed> failedList = response.getFailedList();
                return failedList;
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            throw new SyncDataException(JSON.toJSONString(request),e.getMessage());
        }
    }
}

