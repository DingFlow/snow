package com.snow.dingtalk.service;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserCreateRequest;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.dingtalk.api.response.OapiUserCreateResponse;
import com.dingtalk.api.response.OapiWorkrecordAddResponse;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.WorkrecordAddRequest;
import com.snow.system.service.ISysConfigService;
import com.taobao.api.ApiException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/22 14:29
 */
@Service
public class UserService extends BaseService {
    @Autowired
    private ISysConfigService isysConfigService;

    /**
     * 创建工作待办
     * @param workrecordAddRequest
     * @return
     */
    public String create(WorkrecordAddRequest workrecordAddRequest){
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.USER_CREATE);
        OapiUserCreateRequest request = new OapiUserCreateRequest();
        BeanUtils.copyProperties(workrecordAddRequest,request);
        try {
            OapiUserCreateResponse response = client.execute(request, getDingTalkToken());
            if (response.getErrcode()==0){
                syncDingTalkErrorOperLog(BaseConstantUrl.USER_CREATE,response.getMessage(),"UserCreateRequest",JSON.toJSONString(request));
                return response.getUserid();
            }else {
                syncDingTalkErrorOperLog(BaseConstantUrl.USER_CREATE,response.getErrmsg(),"UserCreateRequest",JSON.toJSONString(request));
            }
        } catch (ApiException e) {
            e.printStackTrace();
            syncDingTalkErrorOperLog(BaseConstantUrl.USER_CREATE,e.getMessage(),"UserCreateRequest",JSON.toJSONString(request));
        }
        return null;
    }

}
