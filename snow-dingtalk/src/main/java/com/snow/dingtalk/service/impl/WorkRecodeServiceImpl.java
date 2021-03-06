package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
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
