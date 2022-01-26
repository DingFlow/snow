package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.snow.common.annotation.DingTalkLog;
import com.snow.common.constant.Constants;
import com.snow.common.enums.DingTalkLogType;
import com.snow.common.enums.DingTalkMessageType;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.service.MessageService;
import com.snow.framework.util.FreemarkUtils;
import com.snow.framework.web.domain.common.SysSendMessageRequest;
import com.snow.system.domain.SysMessageTemplate;
import com.snow.system.service.ISysMessageTemplateService;
import com.snow.system.service.impl.SysConfigServiceImpl;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/12 13:27
 */
@Service
@Slf4j
public class MessageServiceImpl extends BaseService implements MessageService {

    private SysConfigServiceImpl isysConfigService= SpringUtils.getBean(SysConfigServiceImpl.class);

    private ISysMessageTemplateService sysMessageTemplateService=SpringUtils.getBean(ISysMessageTemplateService.class);

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.ASYNCSEND_V2,dingTalkUrl=BaseConstantUrl.ASYNCSEND_V2)
    public Long sendWorkNotice(SysSendMessageRequest sysSendMessageDTO) {
        SysMessageTemplate sysMessageTemplate= sysMessageTemplateService.getSysMessageTemplateByCode(sysSendMessageDTO.getTemplateByCode());
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.ASYNCSEND_V2);
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();

        request.setAgentId(Long.parseLong(isysConfigService.selectConfigByKey(Constants.AGENT_ID)));

        String userIdList = StringUtils.join(sysSendMessageDTO.getReceiverSet(), ",");
        request.setUseridList(userIdList);
        request.setToAllUser(sysSendMessageDTO.getToAllUser());

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        //组装消息内容
        String content= FreemarkUtils.process(sysSendMessageDTO.getTemplateByCode(),sysMessageTemplate.getTemplateBody(),sysSendMessageDTO.getParamMap());

        if(sysSendMessageDTO.getDingTalkMessageType().equals(DingTalkMessageType.LINK)){
            msg.setMsgtype(DingTalkMessageType.LINK.getInfo());
            msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
            msg.getLink().setTitle(sysMessageTemplate.getTemplateName());
            msg.getLink().setText(content);
            msg.getLink().setMessageUrl(sysSendMessageDTO.getFilePath());
            msg.getLink().setPicUrl(sysSendMessageDTO.getFilePath());
        }


        if(sysSendMessageDTO.getDingTalkMessageType().equals(DingTalkMessageType.TEXT)){
            msg.setMsgtype(DingTalkMessageType.TEXT.getInfo());
            msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
            msg.getText().setContent(content);

        }

        if(sysSendMessageDTO.getDingTalkMessageType().equals(DingTalkMessageType.MARKDOWN)){
            msg.setMsgtype(DingTalkMessageType.MARKDOWN.getInfo());
            msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
            msg.getMarkdown().setText(content);
            msg.getMarkdown().setTitle(sysMessageTemplate.getTemplateName());
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
            log.error("@@钉钉发送工作通知异常：{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

}
