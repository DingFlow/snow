package com.snow.dingtalk.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiProcessinstanceCreateRequest;
import com.dingtalk.api.response.OapiProcessinstanceCreateResponse;
import com.snow.common.constant.Constants;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.ProcessinstanceCreateRequest;
import com.snow.system.service.ISysConfigService;
import com.snow.system.service.ISysOperLogService;
import com.taobao.api.ApiException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/18 14:32
 */
@Service
public class ProcessInstanceService extends BaseService {
    @Autowired
    private ISysConfigService isysConfigService;

    /**
     * 创建流程
     * @param processinstanceCreateRequest
     */
    public String create(ProcessinstanceCreateRequest processinstanceCreateRequest){
        DefaultDingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.FLOW_CREATE);
        OapiProcessinstanceCreateRequest request = new OapiProcessinstanceCreateRequest();
        request.setAgentId(Long.parseLong(isysConfigService.selectConfigByKey(Constants.AGENT_ID)));
        BeanUtils.copyProperties(processinstanceCreateRequest,request);
        try {
            OapiProcessinstanceCreateResponse response = client.execute(request,getDingTalkToken());
            if(response.getErrcode()==0){
                syncDingTalkErrorOperLog(BaseConstantUrl.FLOW_CREATE,response.getMessage(),"ProcessInstanceCreateRequest",JSON.toJSONString(request));
                return response.getProcessInstanceId();
            }else {
                syncDingTalkErrorOperLog(BaseConstantUrl.FLOW_CREATE,response.getErrmsg(),"ProcessInstanceCreateRequest",JSON.toJSONString(request));
            }
        } catch (ApiException e) {
            syncDingTalkErrorOperLog(BaseConstantUrl.FLOW_CREATE,e.getMessage(),"ProcessInstanceCreateRequest",JSON.toJSONString(request));
            e.printStackTrace();
        }
        return null;
    }
}
