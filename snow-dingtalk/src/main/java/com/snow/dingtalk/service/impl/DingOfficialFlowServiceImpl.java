package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.snow.common.constant.CacheConstants;
import com.snow.common.constant.Constants;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.CacheUtils;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.bean.BeanUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.request.*;
import com.snow.dingtalk.model.response.DingCreateTaskResponse;
import com.snow.dingtalk.service.DingOfficialFlowService;
import com.snow.system.service.impl.SysConfigServiceImpl;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.dingtalk.api.request.OapiProcessWorkrecordCreateRequest.*;

/**
 * @program: snow
 * @description 钉钉官方工作流服务
 * @author: 没用的阿吉
 * @create: 2021-03-17 10:24
 **/
@Slf4j
@Service
public class DingOfficialFlowServiceImpl extends BaseService implements DingOfficialFlowService {

    @Autowired
    private SysConfigServiceImpl sysConfigService;

    @Override
    public String saveProcess(SaveProcessRequest saveFlowRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.SAVE_PROCESS);
        OapiProcessSaveRequest request = new OapiProcessSaveRequest();
        OapiProcessSaveRequest.SaveProcessRequest saveProcessRequest = new OapiProcessSaveRequest.SaveProcessRequest();
        saveProcessRequest.setName(saveFlowRequest.getName());
        saveProcessRequest.setProcessCode(saveProcessRequest.getProcessCode());
        //获取钉钉配置agentId
        Object agentId = CacheUtils.getSysConfig(CacheConstants.AGENT_ID, sysConfigService.selectConfigByKey(Constants.AGENT_ID));
        saveProcessRequest.setAgentid(Long.parseLong(String.valueOf(agentId)));
        saveProcessRequest.setFakeMode(true);

        // 注意，每种表单组件，对应的componentName是固定的
        List<OapiProcessSaveRequest.FormComponentVo> formComponentList = BeanUtils.transformList(saveFlowRequest.getFormComponentList(), OapiProcessSaveRequest.FormComponentVo.class);

        saveProcessRequest.setFormComponentList(formComponentList);
        request.setSaveProcessRequest(saveProcessRequest);

        try {
            OapiProcessSaveResponse response = client.execute(request, getDingTalkToken());
            if (response.getErrcode() != 0) {
                throw new SyncDataException(JSON.toJSONString(request), response.getErrmsg());
            }
            return response.getResult().getProcessCode();
        } catch (ApiException e) {
            log.error("创建审批模板saveProcess异常：{}", e.getMessage());
            throw new SyncDataException(JSON.toJSONString(request), e.getErrMsg());
        }
    }

    @Override
    public String startProcessInstance(StartFlowRequest startFlowRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.START_PROCESS_INSTANCE);
        OapiProcessinstanceCreateRequest req = new OapiProcessinstanceCreateRequest();
        req.setProcessCode(startFlowRequest.getProcessCode());
        req.setOriginatorUserId(startFlowRequest.getOriginatorUserId());
        req.setDeptId(startFlowRequest.getDeptId());
        //req.setApprovers("manager01, manager02");
       // req.setCcList("user2,user3");
        //req.setCcPosition("START");
        //组件赋值
        List<OapiProcessinstanceCreateRequest.FormComponentValueVo> formComponentList = BeanUtils.transformList(startFlowRequest.getFormComponentValueVoList(), OapiProcessinstanceCreateRequest.FormComponentValueVo.class);
        req.setFormComponentValues(formComponentList);
        try {
            OapiProcessinstanceCreateResponse response = client.execute(req, getDingTalkToken());
            if (response.getErrcode() != 0) {
                throw new SyncDataException(JSON.toJSONString(req), response.getErrmsg());
            }
            return response.getProcessInstanceId();
        } catch (ApiException e) {
            log.error("发起审批实例startProcessInstance异常：{}", e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req), e.getErrMsg());
        }
    }

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

    @Override
    public String saveFakeProcessInstance(StartFakeProcessInstanceRequest startFakeProcessInstanceRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.START_FAKE_PROCESS_INSTANCE);
        OapiProcessWorkrecordCreateRequest req = new OapiProcessWorkrecordCreateRequest();
        SaveFakeProcessInstanceRequest saveFakeProcessInstanceRequest = new SaveFakeProcessInstanceRequest();
        //获取钉钉配置agentId
        Object agentId = CacheUtils.getSysConfig(CacheConstants.AGENT_ID, sysConfigService.selectConfigByKey(Constants.AGENT_ID));
        saveFakeProcessInstanceRequest.setAgentid(Long.parseLong(String.valueOf(agentId)));
        saveFakeProcessInstanceRequest.setProcessCode(startFakeProcessInstanceRequest.getProcessCode());
        saveFakeProcessInstanceRequest.setOriginatorUserId(startFakeProcessInstanceRequest.getOriginatorUserId());
        saveFakeProcessInstanceRequest.setTitle(startFakeProcessInstanceRequest.getTitle());
        saveFakeProcessInstanceRequest.setUrl(startFakeProcessInstanceRequest.getUrl());
        saveFakeProcessInstanceRequest.setRemark(startFakeProcessInstanceRequest.getRemark());
        List<FormComponentValueVo> formComponentValueVoList = BeanUtils.transformList(startFakeProcessInstanceRequest.getFormComponentValueVoList(),FormComponentValueVo.class);

        saveFakeProcessInstanceRequest.setFormComponentValues(formComponentValueVoList);
        req.setRequest(saveFakeProcessInstanceRequest);

        try {
            OapiProcessWorkrecordCreateResponse response = client.execute(req, getDingTalkToken());
            if (response.getErrcode() != 0) {
                throw new SyncDataException(JSON.toJSONString(req), response.getErrmsg());
            }
            return response.getResult().getProcessInstanceId();
        } catch (ApiException e) {
            log.error("创建实例saveFakeProcessInstance异常：{}", e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req), e.getErrMsg());
        }
    }

    @Override
    public List<DingCreateTaskResponse> createProcessTask(SaveTaskRequest saveTaskRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.CREATE_PROCESS_TASK);
        OapiProcessWorkrecordTaskCreateRequest req = new OapiProcessWorkrecordTaskCreateRequest();
        OapiProcessWorkrecordTaskCreateRequest.SaveTaskRequest taskRequest = new OapiProcessWorkrecordTaskCreateRequest.SaveTaskRequest();
        taskRequest.setAgentid(11L);
        taskRequest.setProcessInstanceId(saveTaskRequest.getProcessInstanceId());
        List<OapiProcessWorkrecordTaskCreateRequest.TaskTopVo> taskTopVos = BeanUtils.transformList(saveTaskRequest.getTasks(), OapiProcessWorkrecordTaskCreateRequest.TaskTopVo.class);
        taskRequest.setTasks(taskTopVos);
        req.setRequest(taskRequest);
        try {
            OapiProcessWorkrecordTaskCreateResponse response = client.execute(req, getDingTalkToken());
            if (response.getErrcode() != 0) {
                throw new SyncDataException(JSON.toJSONString(req), response.getErrmsg());
            }
            return BeanUtils.transformList(response.getTasks(), DingCreateTaskResponse.class);
        } catch (ApiException e) {
            log.error("创建createProcessTask异常：{}", e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req), e.getErrMsg());
        }

    }

    @Override
    public void updateProcessTask() {

    }



}
