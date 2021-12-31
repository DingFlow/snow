package com.snow.dingtalk.service;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiProcessinstanceCreateRequest;
import com.dingtalk.api.response.OapiProcessinstanceCreateResponse;
import com.snow.common.constant.Constants;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.request.ProcessInstanceCreateRequest;
import com.snow.system.service.impl.SysConfigServiceImpl;
import com.taobao.api.ApiException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/18 14:32
 */
@Service
public class ProcessInstanceService extends BaseService {

    private SysConfigServiceImpl isysConfigService=SpringUtils.getBean(SysConfigServiceImpl.class);

    /**
     * 创建流程
     * @param processinstanceCreateRequest
     */
    public String create(ProcessInstanceCreateRequest processinstanceCreateRequest){
        DefaultDingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.FLOW_CREATE);
        OapiProcessinstanceCreateRequest request = new OapiProcessinstanceCreateRequest();
        request.setAgentId(Long.parseLong(isysConfigService.selectConfigByKey(Constants.AGENT_ID)));
        BeanUtils.copyProperties(processinstanceCreateRequest,request);
        try {
            OapiProcessinstanceCreateResponse response = client.execute(request,getDingTalkToken());
            if(response.getErrcode()==0){
                return response.getProcessInstanceId();
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getMessage());
            }
        } catch (ApiException e) {
            throw new SyncDataException(JSON.toJSONString(request),e.getMessage());
        }
    }
}
