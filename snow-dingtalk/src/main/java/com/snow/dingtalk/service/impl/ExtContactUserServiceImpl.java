package com.snow.dingtalk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.snow.common.annotation.DingTalkLog;
import com.snow.common.enums.DingTalkLogType;
import com.snow.common.exception.SyncDataException;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.request.ExtContactUserRequest;
import com.snow.dingtalk.service.ExtContactUserService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qimingjin
 * @Title: 钉钉外部联系人
 * @Description:
 * @date 2021/11/4 9:35
 */
@Service
@Slf4j
public class ExtContactUserServiceImpl extends BaseService implements ExtContactUserService {


    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.CREATE_EXT_CONTACT_USER,dingTalkUrl= BaseConstantUrl.CREATE_EXT_CONTACT_USER)
    public String createExtContactUser(ExtContactUserRequest extContactUserRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.CREATE_EXT_CONTACT_USER);
        OapiExtcontactCreateRequest req = new OapiExtcontactCreateRequest();
        OapiExtcontactCreateRequest.OpenExtContact  contact = BeanUtil.copyProperties(extContactUserRequest, OapiExtcontactCreateRequest.OpenExtContact .class);
        contact.setStateCode("86");
        req.setContact(contact);
        try {
            OapiExtcontactCreateResponse rsp = client.execute(req, getDingTalkToken());
            if (rsp.getErrcode()==0) {
                return rsp.getUserid();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@钉钉创建外部联系人异常：{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DELETE_EXT_CONTACT_USER,dingTalkUrl= BaseConstantUrl.DELETE_EXT_CONTACT_USER)
    public boolean deleteExtContactUser(String userId) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.DELETE_EXT_CONTACT_USER);
        OapiExtcontactDeleteRequest req = new OapiExtcontactDeleteRequest();
        req.setUserId(userId);
        try {
            OapiExtcontactDeleteResponse rsp = client.execute(req, getDingTalkToken());
            if (rsp.getErrcode()==0) {
                return rsp.isSuccess();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@钉钉删除外部联系人异常：{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }



    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.UPDATE_EXT_CONTACT_USER,dingTalkUrl= BaseConstantUrl.UPDATE_EXT_CONTACT_USER)
    public boolean updateExtContactUser(ExtContactUserRequest extContactUserRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.UPDATE_EXT_CONTACT_USER);
        OapiExtcontactUpdateRequest req = new OapiExtcontactUpdateRequest();
        OapiExtcontactUpdateRequest.OpenExtContact  contact = BeanUtil.copyProperties(extContactUserRequest, OapiExtcontactUpdateRequest.OpenExtContact .class);
        req.setContact(contact);
        try {
            OapiExtcontactUpdateResponse rsp = client.execute(req, getDingTalkToken());
            if (rsp.getErrcode()==0) {
                return rsp.isSuccess();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@钉钉编辑外部联系人异常：{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.EXT_CONTACT_USER_LIST,dingTalkUrl= BaseConstantUrl.EXT_CONTACT_USER_LIST)
    public List<ExtContactUserRequest>  getExtContactUserList(Long offset,Long size) {
        DingTalkClient client = new DefaultDingTalkClient( BaseConstantUrl.EXT_CONTACT_USER_LIST);
        OapiExtcontactListRequest req = new OapiExtcontactListRequest();
        if(ObjectUtil.isNotNull(size)){
            req.setSize(size);
        }
        if(ObjectUtil.isNotNull(offset)){
            req.setOffset(offset);
        }
        try {
            OapiExtcontactListResponse rsp = client.execute(req, getDingTalkToken());
            if (rsp.getErrcode()==0) {
                List<ExtContactUserRequest> extContactUserRequestList= Lists.newArrayList();
                List<OapiExtcontactListResponse.OpenExtContact> results = rsp.getResults();
                if(CollUtil.isNotEmpty(results)){
                    results.forEach(t->{
                        ExtContactUserRequest contact = BeanUtil.copyProperties(t, ExtContactUserRequest.class);
                        extContactUserRequestList.add(contact);
                    });
                }
                return extContactUserRequestList;
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@钉钉外部联系人列表异常：{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.GET_EXT_CONTACT_USER,dingTalkUrl= BaseConstantUrl.GET_EXT_CONTACT_USER)
    public ExtContactUserRequest getExtContactUserDetail(String userId) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_EXT_CONTACT_USER);
        OapiExtcontactGetRequest req = new OapiExtcontactGetRequest();
        req.setUserId(userId);
        try {
            OapiExtcontactGetResponse rsp = client.execute(req, getDingTalkToken());
            if (rsp.getErrcode()==0) {
                return BeanUtil.copyProperties(rsp.getResult(), ExtContactUserRequest.class);
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@钉钉外部联系人详情异常：{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    public List<OapiExtcontactListlabelgroupsResponse.OpenLabelGroup>  getExtContactUserLabelGroupsList() {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.EXT_CONTACT_USER_LABEL);
        OapiExtcontactListlabelgroupsRequest req = new OapiExtcontactListlabelgroupsRequest();
        req.setSize(100L);
        req.setOffset(0L);
        try {
            OapiExtcontactListlabelgroupsResponse rsp = client.execute(req, getDingTalkToken());
            if (rsp.getErrcode()==0) {
                return rsp.getResults();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@钉钉外部联系人标签异常：{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }
}
