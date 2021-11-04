package com.snow.dingtalk.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.dingtalktodo_1_0.Client;
import com.aliyun.dingtalktodo_1_0.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.dingtalk.api.request.OapiWorkrecordGetbyuseridRequest;
import com.dingtalk.api.request.OapiWorkrecordUpdateRequest;
import com.dingtalk.api.response.*;
import com.snow.common.annotation.SyncLog;
import com.snow.common.constant.Constants;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.enums.DingTalkMessageType;
import com.snow.common.enums.SyncLogType;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.PatternUtils;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.request.WorkrecordAddRequest;
import com.snow.dingtalk.model.request.WorkrecordGetbyuseridRequest;
import com.snow.dingtalk.service.UserService;
import com.snow.dingtalk.service.WorkRecodeService;
import com.snow.framework.util.ShiroUtils;
import com.snow.framework.web.domain.common.SysSendMessageDTO;
import com.snow.system.domain.SysMessageTemplate;
import com.snow.system.domain.SysOaTask;
import com.snow.system.service.ISysMessageTemplateService;
import com.snow.system.service.ISysUserService;
import com.snow.system.service.impl.SysConfigServiceImpl;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author qimingjin
 * @Title: 待办事项
 * @Description:
 * @date 2020/12/9 14:53
 */
@Slf4j
@Service
public class WorkRecodeServiceImpl extends BaseService implements WorkRecodeService {

    private SysConfigServiceImpl isysConfigService=SpringUtils.getBean(SysConfigServiceImpl.class);


    private ISysMessageTemplateService sysMessageTemplateService=SpringUtils.getBean(ISysMessageTemplateService.class);

    private UserService userService=SpringUtils.getBean(UserService.class);

    private ISysUserService sysUserService=SpringUtils.getBean(ISysUserService.class);

    /**
     * 创建工作待办
     * @param workrecordAddRequest
     * @return
     */
    @SyncLog(dingTalkListenerType = DingTalkListenerType.WORK_RECODE_CREATE,dingTalkUrl=BaseConstantUrl.WORK_RECORD_CREATE)
    @Override
    public String create(WorkrecordAddRequest workrecordAddRequest){
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.WORK_RECORD_CREATE);
        OapiWorkrecordAddRequest req = new OapiWorkrecordAddRequest();
        BeanUtils.copyProperties(workrecordAddRequest,req);
        OapiWorkrecordAddResponse rsp = null;
        try {
            rsp = client.execute(req, getDingTalkToken());
            if (rsp.getErrcode()==0) {
                return rsp.getRecordId();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("钉钉workRecordAddRequest异常：{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }

    }

    @Override
    public String createTodoTask(SysOaTask sysOaTask) {
        List<String> taskDistributeId = sysOaTask.getTaskDistributeId();

        CreateTodoTaskHeaders createTodoTaskHeaders = new CreateTodoTaskHeaders();
        createTodoTaskHeaders.xAcsDingtalkAccessToken = getDingTalkTokenV2();
        CreateTodoTaskRequest.CreateTodoTaskRequestNotifyConfigs notifyConfigs = new CreateTodoTaskRequest.CreateTodoTaskRequestNotifyConfigs()
                .setDingNotify("1");
        CreateTodoTaskRequest.CreateTodoTaskRequestDetailUrl detailUrl = new CreateTodoTaskRequest.CreateTodoTaskRequestDetailUrl()
                .setAppUrl(sysOaTask.getTaskUrl())
                .setPcUrl(sysOaTask.getTaskUrl());

        CreateTodoTaskRequest createTodoTaskRequest = new CreateTodoTaskRequest()
                .setSourceId(sysOaTask.getTaskNo())
                .setSubject(sysOaTask.getTaskName())
                .setCreatorId(userService.getUnionIdBySysUserId(Long.parseLong(sysOaTask.getCreateBy())))
                .setDescription(sysOaTask.getTaskContent())
                .setDetailUrl(detailUrl)
                .setIsOnlyShowExecutor(true)
                .setPriority(sysOaTask.getPriority())
                .setNotifyConfigs(notifyConfigs);
        if (ObjectUtil.isNotNull(sysOaTask.getTaskCompleteTime())) {
            createTodoTaskRequest.setDueTime(sysOaTask.getTaskCompleteTime().getTime());
        }
        //执行者id
        if(CollUtil.isNotEmpty(taskDistributeId)){
            List<String> executorIds = taskDistributeId.stream().map(t -> {
                return userService.getUnionIdBySysUserId(Long.parseLong(t));
            }).collect(Collectors.toList());
            createTodoTaskRequest.setExecutorIds(executorIds);
        }
        try {
            CreateTodoTaskResponse response = createTodoClient().createTodoTaskWithOptions(userService.getUnionIdBySysUserId(Long.parseLong(sysOaTask.getCreateBy())), createTodoTaskRequest, createTodoTaskHeaders, new RuntimeOptions());
            return response.getBody().id;
        } catch (Exception err) {
            log.error("@@调用钉钉创建待办的时候出现异常，异常信息为:{}",err.getMessage());
            throw new SyncDataException(JSON.toJSONString(createTodoTaskRequest),err.getMessage());
        }
    }

    @Override
    public String deleteTodoTask(String taskId) {
        String dingUserId = ShiroUtils.getDingUserId();
        OapiV2UserGetResponse.UserGetResponse user = userService.getUserByUserId(dingUserId);
        DeleteTodoTaskHeaders deleteTodoTaskHeaders = new DeleteTodoTaskHeaders();
        deleteTodoTaskHeaders.xAcsDingtalkAccessToken = getDingTalkTokenV2();
        DeleteTodoTaskRequest deleteTodoTaskRequest = new DeleteTodoTaskRequest()
                .setOperatorId(user.getUnionid());
        try {
            DeleteTodoTaskResponse deleteTodoTaskResponse = createTodoClient().deleteTodoTaskWithOptions(user.getUnionid(), taskId, deleteTodoTaskRequest, deleteTodoTaskHeaders, new RuntimeOptions());
            return deleteTodoTaskResponse.getBody().requestId;
        } catch (Exception exception) {
            log.error("@@调用钉钉删除待办的时候出现异常，异常信息为:{}",exception.getMessage());
            throw new SyncDataException(JSON.toJSONString(deleteTodoTaskRequest),exception.getMessage());
        }
    }

    @Override
    public Boolean updateTodoTask(SysOaTask sysOaTask) {
        UpdateTodoTaskHeaders updateTodoTaskHeaders = new UpdateTodoTaskHeaders();
        updateTodoTaskHeaders.xAcsDingtalkAccessToken = getDingTalkTokenV2();
        UpdateTodoTaskRequest updateTodoTaskRequest = new UpdateTodoTaskRequest()
                .setSubject(sysOaTask.getTaskName())
                .setDescription(sysOaTask.getTaskContent())
                .setExecutorIds(Arrays.asList(
                        ""
                ));
        //设置是否完成
        if(ObjectUtil.isNotNull(sysOaTask.getTaskCompleteTime())){
            updateTodoTaskRequest.setDone(true);
        }else {
            updateTodoTaskRequest.setDone(false);
        }
        if(ObjectUtil.isNotNull(sysOaTask.getExpectedTime())){
            updateTodoTaskRequest.setDueTime(sysOaTask.getExpectedTime().getTime());
        }
        try {
            UpdateTodoTaskResponse updateTodoTaskResponse = createTodoClient().updateTodoTaskWithOptions(userService.getUnionIdBySysUserId(Long.parseLong(sysOaTask.getUpdateBy())), sysOaTask.getDingTaskId(), updateTodoTaskRequest, updateTodoTaskHeaders, new RuntimeOptions());
            return updateTodoTaskResponse.getBody().result;
        }catch (Exception err) {
            log.error("@@调用钉钉更新待办的时候出现异常，异常信息为:{}",err.getMessage());
            throw new SyncDataException(JSON.toJSONString(updateTodoTaskRequest),err.getMessage());
        }
    }

    @Override
    public Boolean updateTodoTaskExecutorStatus(String taskId,Boolean status) {
        UpdateTodoTaskExecutorStatusHeaders updateTodoTaskExecutorStatusHeaders = new UpdateTodoTaskExecutorStatusHeaders();
        updateTodoTaskExecutorStatusHeaders.xAcsDingtalkAccessToken = getDingTalkTokenV2();
        UpdateTodoTaskExecutorStatusRequest.UpdateTodoTaskExecutorStatusRequestExecutorStatusList executorStatusList0 = new UpdateTodoTaskExecutorStatusRequest.UpdateTodoTaskExecutorStatusRequestExecutorStatusList()
                .setId(taskId)
                .setIsDone(status);
        UpdateTodoTaskExecutorStatusRequest updateTodoTaskExecutorStatusRequest = new UpdateTodoTaskExecutorStatusRequest()
                .setExecutorStatusList(java.util.Arrays.asList(
                        executorStatusList0
                ));
        try {
            UpdateTodoTaskExecutorStatusResponse response = createTodoClient().updateTodoTaskExecutorStatusWithOptions("PUoiinWIpa2yH2ymhiiGiP6g", taskId, updateTodoTaskExecutorStatusRequest, updateTodoTaskExecutorStatusHeaders, new RuntimeOptions());
            return response.getBody().result;
        } catch (Exception err) {
            log.error("@@调用钉钉更新办状态的时候出现异常，异常信息为:{}",err.getMessage());
            throw new SyncDataException(JSON.toJSONString(updateTodoTaskExecutorStatusRequest),err.getMessage());
        }
    }

    @Override
    public GetTodoTaskBySourceIdResponseBody getTodoTaskByBusinessId(String businessId) {
        GetTodoTaskBySourceIdHeaders getTodoTaskBySourceIdHeaders = new GetTodoTaskBySourceIdHeaders();
        getTodoTaskBySourceIdHeaders.xAcsDingtalkAccessToken = getDingTalkTokenV2();
        try {
            GetTodoTaskBySourceIdResponse respose = createTodoClient().getTodoTaskBySourceIdWithOptions("user123", businessId, getTodoTaskBySourceIdHeaders, new RuntimeOptions());
            return respose.getBody();
        } catch (Exception err) {
            log.error("@@调用钉钉获取待办详情的时候出现异常，异常信息为:{}",err.getMessage());
            throw new SyncDataException(JSON.toJSONString(getTodoTaskBySourceIdHeaders),err.getMessage());
        }
    }

    /**
     * 根据用户ID获取待办
     * @param workrecordGetbyuseridRequest
     * @return
     */
    @Override
    @SyncLog(dingTalkListenerType = DingTalkListenerType.GET_WORK_RECORD_USER,dingTalkUrl=BaseConstantUrl.GET_WORK_RECORD_USER_ID_)
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
                return rsp.getRecords();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("钉钉workRecordAddRequest异常：{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    /**
     * 更新待办
     * @param userId
     * @param recordId
     * @return
     */
    @Override
    @SyncLog(dingTalkListenerType = DingTalkListenerType.WORK_RECORD_UPDATE,dingTalkUrl=BaseConstantUrl.WORK_RECORD_UPDATE)
    public Boolean update(String userId,String recordId){
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.WORK_RECORD_UPDATE);
        OapiWorkrecordUpdateRequest req = new OapiWorkrecordUpdateRequest();
        req.setUserid(userId);
        req.setRecordId(recordId);
        OapiWorkrecordUpdateResponse rsp = null;
        try {
            rsp = client.execute(req, getDingTalkToken());
            if(rsp.getErrcode()==0){
                return rsp.getResult();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("钉钉workRecordAddRequest异常：{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }


    @Override
    @SyncLog(dingTalkListenerType = DingTalkListenerType.ASYNCSEND_V2,dingTalkUrl=BaseConstantUrl.ASYNCSEND_V2,syncLogTpye=SyncLogType.SYNC_SYS)
    public Long sendCommonMessage(SysSendMessageDTO sysSendMessageDTO) {
        SysMessageTemplate sysMessageTemplate= sysMessageTemplateService.getSysMessageTemplateByCode(sysSendMessageDTO.getTemplateByCode());
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.ASYNCSEND_V2);
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();

        request.setAgentId(Long.parseLong(isysConfigService.selectConfigByKey(Constants.AGENT_ID)));

        String userIdList = StringUtils.join(sysSendMessageDTO.getReceiverSet(), ",");
        request.setUseridList(userIdList);
        request.setToAllUser(sysSendMessageDTO.getToAllUser());

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();

        if(sysSendMessageDTO.getDingTalkMessageType().equals(DingTalkMessageType.LINK)){
            msg.setMsgtype(DingTalkMessageType.LINK.getInfo());
            msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
            msg.getLink().setTitle(sysMessageTemplate.getTemplateName());
            String content= PatternUtils.builderTemplateBody(sysSendMessageDTO.getParamMap(), sysMessageTemplate.getTemplateBody());
            msg.getLink().setText(content);
            msg.getLink().setMessageUrl(sysSendMessageDTO.getFilePath());
            msg.getLink().setPicUrl(sysSendMessageDTO.getFilePath());
        }


        if(sysSendMessageDTO.getDingTalkMessageType().equals(DingTalkMessageType.TEXT)){
            msg.setMsgtype(DingTalkMessageType.TEXT.getInfo());
            msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
            String content= PatternUtils.builderTemplateBody(sysSendMessageDTO.getParamMap(), sysMessageTemplate.getTemplateBody());
            msg.getText().setContent(content);

        }

        request.setMsg(msg);
        OapiMessageCorpconversationAsyncsendV2Response rsp = null;
        try {
            rsp = client.execute(request, getDingTalkToken());
            if(rsp.getErrcode()==0){
                return rsp.getTaskId();
            }else {
                throw new SyncDataException(JSON.toJSONString(request),rsp.getErrmsg());
            }

        } catch (ApiException e) {
            log.error("钉钉workRecordAddRequest异常：{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    public Client createTodoClient(){
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        Client client = null;
        try {
            return new Client(config);
        } catch (Exception exception) {
            log.error("@@调用钉钉待办初始化client的时候出现异常，异常信息为:{}",exception.getMessage());
            throw new SyncDataException(JSON.toJSONString(config),exception.getMessage());
        }
    }
}
