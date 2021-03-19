package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.bean.BeanUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.FlowExecuteTaskRequest;
import com.snow.dingtalk.model.FlowTerminateProcessInstanceRequest;
import com.snow.dingtalk.service.DingOfficialFlowService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: snow
 * @description 钉钉官方工作流服务
 * @author: 没用的阿吉
 * @create: 2021-03-17 10:24
 **/
@Slf4j
@Service
public class DingOfficialFlowServiceImpl extends BaseService implements DingOfficialFlowService {


    @Override
    public List<OapiProcessTemplateManageGetResponse.ProcessSimpleVO> getProcessTemplateManage(String userId) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_PROCESS_TEMPLATE_MANAGE);
        OapiProcessTemplateManageGetRequest req = new OapiProcessTemplateManageGetRequest();
        req.setUserid(userId);
        try {
            OapiProcessTemplateManageGetResponse response = client.execute(req, getDingTalkToken());
            if (response.getErrcode() != 0) {
                throw new SyncDataException(JSON.toJSONString(req), response.getErrmsg());
            }
            return response.getResult();
        } catch (ApiException e) {
            log.error("获取当前企业所有可管理的模版getProcessTemplateManage异常：{}", e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req), e.getErrMsg());
        }
    }

    @Override
    public OapiProcessListbyuseridResponse.HomePageProcessTemplateVo getProcessListByUserId( String userId) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_PROCESSLIST_USERID);
        OapiProcessListbyuseridRequest req = new OapiProcessListbyuseridRequest();
        req.setUserid(userId);
        req.setOffset(0L);
        req.setSize(100L);
        try {
            OapiProcessListbyuseridResponse response = client.execute(req, getDingTalkToken());
            if (response.getErrcode() != 0) {
                throw new SyncDataException(JSON.toJSONString(req), response.getErrmsg());
            }
            return response.getResult();
        } catch (ApiException e) {
            log.error("获取指定用户可见的审批表单列表getProcessListByUserId异常：{}", e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req), e.getErrMsg());
        }
    }

    @Override
    public Boolean executeProcessInstance(FlowExecuteTaskRequest flowExecuteTaskRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.EXECUTE_PROCESSINSTANCE);
        OapiProcessinstanceExecuteV2Request req = new OapiProcessinstanceExecuteV2Request();
        OapiProcessinstanceExecuteV2Request.ExecuteTaskRequest executeTaskRequest = new OapiProcessinstanceExecuteV2Request.ExecuteTaskRequest();
        executeTaskRequest.setProcessInstanceId(flowExecuteTaskRequest.getProcessInstanceId());
        executeTaskRequest.setActionerUserid(flowExecuteTaskRequest.getActionerUserid());
        executeTaskRequest.setTaskId(flowExecuteTaskRequest.getTaskId());
        executeTaskRequest.setRemark(flowExecuteTaskRequest.getRemark());
        executeTaskRequest.setResult(flowExecuteTaskRequest.getResult());
        if(StringUtils.isNotNull(flowExecuteTaskRequest.getFile())){
            OapiProcessinstanceExecuteV2Request.File file=new OapiProcessinstanceExecuteV2Request.File();
            if(StringUtils.isNotNull(flowExecuteTaskRequest.getFile().getAttachments())){
                List<OapiProcessinstanceExecuteV2Request.Attachment> attachments = BeanUtils.transformList(flowExecuteTaskRequest.getFile().getAttachments(), OapiProcessinstanceExecuteV2Request.Attachment.class);
                file.setAttachments(attachments);
            }
            file.setPhotos(flowExecuteTaskRequest.getFile().getPhotos());
            executeTaskRequest.setFile(file);
        }
        req.setRequest(executeTaskRequest);
        try {
            OapiProcessinstanceExecuteV2Response response = client.execute(req, getDingTalkToken());
            if (response.getErrcode() != 0) {
                throw new SyncDataException(JSON.toJSONString(req), response.getErrmsg());
            }
            return response.getResult();
        } catch (ApiException e) {
            log.error("执行审批操作带附件executeProcessInstance异常：{}", e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req), e.getErrMsg());
        }

    }

    /*@Override
    public void addProcessInstanceComment() {

    }*/

    @Override
    public OapiProcessinstanceGetResponse.ProcessInstanceTopVo getProcessInstanceDetail(String processInstanceId) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_PROCESSINSTANCE);
        OapiProcessinstanceGetRequest req = new OapiProcessinstanceGetRequest();
        req.setProcessInstanceId(processInstanceId);
        try {
            OapiProcessinstanceGetResponse response = client.execute(req, getDingTalkToken());
            if (response.getErrcode() != 0) {
                throw new SyncDataException(JSON.toJSONString(req), response.getErrmsg());
            }
            return response.getProcessInstance();
        } catch (ApiException e) {
            log.error("获取审批实例详情getProcessInstanceDetail异常：{}", e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req), e.getErrMsg());
        }

    }

    @Override
    public Boolean terminateProcessInstance(FlowTerminateProcessInstanceRequest flowTerminateProcessInstanceRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.TERMINATE_PROCESSINSTANCE);
        OapiProcessInstanceTerminateRequest req = new OapiProcessInstanceTerminateRequest();
        OapiProcessInstanceTerminateRequest.TerminateProcessInstanceRequestV2 processInstanceRequestV2 = new OapiProcessInstanceTerminateRequest.TerminateProcessInstanceRequestV2();
        processInstanceRequestV2.setProcessInstanceId(flowTerminateProcessInstanceRequest.getProcessInstanceId());
        processInstanceRequestV2.setIsSystem(flowTerminateProcessInstanceRequest.getIsSystem());
        processInstanceRequestV2.setRemark(flowTerminateProcessInstanceRequest.getRemark());
        processInstanceRequestV2.setOperatingUserid(flowTerminateProcessInstanceRequest.getOperatingUserid());
        req.setRequest(processInstanceRequestV2);

        try {
            OapiProcessInstanceTerminateResponse response = client.execute(req, getDingTalkToken());
            if (response.getErrcode() != 0) {
                throw new SyncDataException(JSON.toJSONString(req), response.getErrmsg());
            }
            return response.getSuccess();
        } catch (ApiException e) {
            log.error("终止流程terminateProcessInstance异常：{}", e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req), e.getErrMsg());
        }
    }

}
