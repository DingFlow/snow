package com.snow.dingtalk.service;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.dingtalk.api.request.OapiWorkrecordGetbyuseridRequest;
import com.dingtalk.api.request.OapiWorkrecordUpdateRequest;
import com.dingtalk.api.response.OapiWorkrecordAddResponse;
import com.dingtalk.api.response.OapiWorkrecordGetbyuseridResponse;
import com.dingtalk.api.response.OapiWorkrecordUpdateResponse;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.WorkrecordAddRequest;
import com.snow.dingtalk.model.WorkrecordGetbyuseridRequest;
import com.snow.system.service.ISysConfigService;
import com.taobao.api.ApiException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/21 14:28
 */
@Service
public class WorkRecodeService extends BaseService {
    @Autowired
    private ISysConfigService isysConfigService;

    /**
     * 创建工作待办
     * @param workrecordAddRequest
     * @return
     */
    public String create(WorkrecordAddRequest workrecordAddRequest){
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.WORK_RECORD_CREATE);
        OapiWorkrecordAddRequest req = new OapiWorkrecordAddRequest();
        BeanUtils.copyProperties(workrecordAddRequest,req);
        OapiWorkrecordAddResponse rsp = null;
        try {
            rsp = client.execute(req, getDingTalkToken());
            if (rsp.getErrcode()==0){
                syncDingTalkErrorOperLog(BaseConstantUrl.WORK_RECORD_CREATE,rsp.getMessage(),"WorkRecordAddRequest",JSON.toJSONString(req));
                return rsp.getRecordId();
            }else {
                syncDingTalkErrorOperLog(BaseConstantUrl.WORK_RECORD_CREATE,rsp.getErrmsg(),"WorkRecordAddRequest",JSON.toJSONString(req));
            }
        } catch (ApiException e) {
            e.printStackTrace();
            syncDingTalkErrorOperLog(BaseConstantUrl.WORK_RECORD_CREATE,e.getMessage(),"WorkRecordAddRequest",JSON.toJSONString(req));
        }
        return null;
    }

    /**
     * 根据用户ID获取待办
     * @param workrecordGetbyuseridRequest
     * @return
     */
    public OapiWorkrecordGetbyuseridResponse.PageResult getWorkRecordByUserId(WorkrecordGetbyuseridRequest workrecordGetbyuseridRequest){
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_WORK_RECORD_USER_ID_);
        OapiWorkrecordGetbyuseridRequest req = new OapiWorkrecordGetbyuseridRequest();
        req.setUserid(workrecordGetbyuseridRequest.getUserid());
        req.setOffset(workrecordGetbyuseridRequest.getOffset());
        req.setLimit(workrecordGetbyuseridRequest.getLimit());
        req.setStatus(workrecordGetbyuseridRequest.getStatus());
        try {
            OapiWorkrecordGetbyuseridResponse rsp = client.execute(req, getDingTalkToken());
            if(rsp.getErrcode()==0){
                syncDingTalkErrorOperLog(BaseConstantUrl.GET_WORK_RECORD_USER_ID_,rsp.getMessage(),"WorkrecordGetbyuseridRequest",JSON.toJSONString(req));
                return rsp.getRecords();
            }else {
                syncDingTalkErrorOperLog(BaseConstantUrl.GET_WORK_RECORD_USER_ID_,rsp.getErrmsg(),"WorkrecordGetbyuseridRequest",JSON.toJSONString(req));
            }
        } catch (ApiException e) {
            e.printStackTrace();
            syncDingTalkErrorOperLog(BaseConstantUrl.GET_WORK_RECORD_USER_ID_,e.getMessage(),"WorkrecordGetbyuseridRequest",JSON.toJSONString(req));
        }
        return null;
    }

    /**
     * 更新待办
     * @param userId
     * @param recordId
     * @return
     */
    public Boolean update(String userId,String recordId){
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.WORK_RECORD_UPDATE);
        OapiWorkrecordUpdateRequest req = new OapiWorkrecordUpdateRequest();
        req.setUserid(userId);
        req.setRecordId(recordId);
        OapiWorkrecordUpdateResponse rsp = null;
        try {
            rsp = client.execute(req, getDingTalkToken());
            if(rsp.getErrcode()==0){
                syncDingTalkErrorOperLog(BaseConstantUrl.WORK_RECORD_UPDATE,rsp.getMessage(),"WorkrecordUpdateRequest",JSON.toJSONString(req));
                return rsp.getResult();
            }else {
                syncDingTalkErrorOperLog(BaseConstantUrl.WORK_RECORD_UPDATE,rsp.getErrmsg(),"WorkrecordUpdateRequest",JSON.toJSONString(req));
            }
        } catch (ApiException e) {
            e.printStackTrace();
            syncDingTalkErrorOperLog(BaseConstantUrl.WORK_RECORD_UPDATE,e.getMessage(),"WorkrecordUpdateRequest",JSON.toJSONString(req));
        }
        return false;
    }
}
