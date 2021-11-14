package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.snow.common.constant.Constants;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.bean.BeanUtils;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.request.DepartmentCreateRequest;
import com.snow.dingtalk.model.request.FlowExecuteTaskRequest;
import com.snow.dingtalk.model.request.FlowTerminateProcessInstanceRequest;
import com.snow.dingtalk.model.request.StartFlowRequest;
import com.snow.dingtalk.service.DingOfficialFlowService;
import com.snow.system.service.impl.SysConfigServiceImpl;
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

    private SysConfigServiceImpl isysConfigService=SpringUtils.getBean(SysConfigServiceImpl.class);

    @Override
    public String saveProcess(DepartmentCreateRequest.SaveFlowRequest saveFlowRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.SAVE_PROCESS);

        OapiProcessSaveRequest request = new OapiProcessSaveRequest();
        OapiProcessSaveRequest.SaveProcessRequest saveProcessRequest = new OapiProcessSaveRequest.SaveProcessRequest();
       // saveProcessRequest.setDisableFormEdit(true);
        saveProcessRequest.setName(saveFlowRequest.getName());
        saveProcessRequest.setProcessCode(saveProcessRequest.getProcessCode());
        saveProcessRequest.setAgentid(Long.parseLong(isysConfigService.selectConfigByKey(Constants.AGENT_ID)));
        saveProcessRequest.setFakeMode(true);


       // List<OapiProcessSaveRequest.FormComponentVo> formComponentList = new ArrayList<>();

        // 注意，每种表单组件，对应的componentName是固定的
        List<OapiProcessSaveRequest.FormComponentVo> formComponentList = BeanUtils.transformList(saveFlowRequest.getFormComponentList(), OapiProcessSaveRequest.FormComponentVo.class);


        /*if(formComponentType.getDingType().equals(FormComponentType.TEXT_FIELD.getDingType())){
            // 单行文本框
            OapiProcessSaveRequest.FormComponentVo singleInput = new OapiProcessSaveRequest.FormComponentVo();
            singleInput.setComponentName(FormComponentType.TEXT_FIELD.getDingType());
            OapiProcessSaveRequest.FormComponentPropVo singleInputProp = new OapiProcessSaveRequest.FormComponentPropVo();

            singleInputProp.setRequired(saveFlowRequest.getRequired());
            singleInputProp.setLabel("单行输入框");
            singleInputProp.setPlaceholder("请输入");
            singleInputProp.setId("TextField-J78F056R");
            singleInput.setProps(singleInputProp);
            formComponentList.add(singleInput);
        }*/


       // 多行文本框
       /* OapiProcessSaveRequest.FormComponentVo multipleInput = new OapiProcessSaveRequest.FormComponentVo();
        multipleInput.setComponentName("TextareaField");
        OapiProcessSaveRequest.FormComponentPropVo multipleInputProp = new OapiProcessSaveRequest.FormComponentPropVo();
        multipleInputProp.setRequired(true);
        multipleInputProp.setLabel("多行输入框");
        multipleInputProp.setPlaceholder("请输入");
        multipleInputProp.setId("TextareaField-J78F056S");
        multipleInput.setProps(multipleInputProp);
        formComponentList.add(multipleInput);*/


        // 日期
   /*     OapiProcessSaveRequest.FormComponentVo dateComponent = new OapiProcessSaveRequest.FormComponentVo();
        dateComponent.setComponentName("DDDateField");
        OapiProcessSaveRequest.FormComponentPropVo dateComponentProp = new OapiProcessSaveRequest.FormComponentPropVo();
        dateComponentProp.setRequired(true);
        dateComponentProp.setLabel("日期");
        dateComponentProp.setPlaceholder("请选择");
        dateComponentProp.setUnit("小时"); // 小时或天
        dateComponentProp.setId("DDDateField-J8MTJZVE");
        dateComponent.setProps(dateComponentProp);
        formComponentList.add(dateComponent);*/


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
    public void bpmsInstanceChange() {

    }

    @Override
    public void bpmsTaskChange() {

    }

}
