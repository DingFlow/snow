package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.dingtalktodo_1_0.Client;
import com.aliyun.dingtalktodo_1_0.models.CreateTodoTaskHeaders;
import com.aliyun.dingtalktodo_1_0.models.CreateTodoTaskRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.dingtalk.api.request.OapiWorkrecordGetbyuseridRequest;
import com.dingtalk.api.request.OapiWorkrecordUpdateRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiWorkrecordAddResponse;
import com.dingtalk.api.response.OapiWorkrecordGetbyuseridResponse;
import com.dingtalk.api.response.OapiWorkrecordUpdateResponse;
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
import com.snow.dingtalk.model.WorkrecordAddRequest;
import com.snow.dingtalk.model.WorkrecordGetbyuseridRequest;
import com.snow.dingtalk.service.WorkRecodeService;
import com.snow.framework.web.domain.common.SysSendMessageDTO;
import com.snow.system.domain.SysMessageTemplate;
import com.snow.system.service.ISysMessageTemplateService;
import com.snow.system.service.impl.SysConfigServiceImpl;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;


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


    public void createV2() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        Client client = new com.aliyun.dingtalktodo_1_0.Client(config);
        CreateTodoTaskHeaders createTodoTaskHeaders = new CreateTodoTaskHeaders();
        createTodoTaskHeaders.xAcsDingtalkAccessToken = getDingTalkTokenV2();
        CreateTodoTaskRequest.CreateTodoTaskRequestDetailUrl detailUrl = new CreateTodoTaskRequest.CreateTodoTaskRequestDetailUrl()
                .setAppUrl("dingtalk://dingtalkclient/action/open_mini_app?miniAppId={0}&ddMode=push&page=pages%2ftask-detail%2ftask-detail%3ftaskId%3d{1}")
                .setPcUrl("https://todo.dingtalk.com/ding-portal/detail/task/{0}");
        CreateTodoTaskRequest createTodoTaskRequest = new CreateTodoTaskRequest()
                .setSourceId("isv_dingtalkTodo1")
                .setSubject("接入钉钉待办")
                .setCreatorId("PUoiinWIpa2yH2ymhiiGiP6g")
                .setDescription("应用可以调用该接口发起一个钉钉待办任务，该待办事项会出现在钉钉客户端“待办”页面，需要注意的是，通过开放接口发起的待办，目前仅支持直接跳转ISV应用详情页（ISV在调该接口时需传入自身应用详情页链接）。")
                .setDueTime(1617675000000L)
                .setExecutorIds(Arrays.asList(
                        "PUoiinWIpa2yH2ymhiiGiP6g"
                ))
                .setParticipantIds(Arrays.asList(
                        "PUoiinWIpa2yH2ymhiiGiP6g"
                ))
                .setDetailUrl(detailUrl);
        try {
            client.createTodoTaskWithOptions("PUoiinWIpa2yH2ymhiiGiP6g", createTodoTaskRequest, createTodoTaskHeaders, new RuntimeOptions());
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

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
}
