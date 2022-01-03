package com.snow.framework.web.service;

import cn.hutool.core.util.ObjectUtil;
import com.snow.common.constant.UserConstants;
import com.snow.common.utils.StringUtils;
import com.snow.framework.util.FreemarkUtils;
import com.snow.framework.web.domain.common.SysSendMessageRequest;
import com.snow.system.domain.SysMessageTemplate;
import com.snow.system.domain.SysMessageTransition;
import com.snow.system.service.ISysConfigService;
import com.snow.system.service.ISysMessageTemplateService;
import com.snow.system.service.ISysMessageTransitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

/**
 * @author qimingjin
 * @Title: 站内信
 * @Description:
 * @date 2021/6/29 16:33
 */
@Component
@Slf4j
public class InnerMessageService {

    @Resource
    private ISysMessageTemplateService sysMessageTemplateService;

    @Resource
    private ISysMessageTransitionService sysMessageTransitionService;

    @Resource
    private ISysConfigService configService;
    /**
     * 发送站内信息
     * @param sysSendMessageDTO
     */
    public void sendInnerMessage(SysSendMessageRequest sysSendMessageDTO) {
        SysMessageTransition message = new SysMessageTransition();
        SysMessageTemplate sysMessageTemplate= sysMessageTemplateService.getSysMessageTemplateByCode(sysSendMessageDTO.getTemplateByCode());
        if(ObjectUtil.isNull(sysMessageTemplate)){
            log.error("@发送站内信是模板code不正确...");
            throw new RuntimeException("模板code不正确");
        }
        Set<String> receiverSet = sysSendMessageDTO.getReceiverSet();
        try {
            //如果生产者为空，则取系统管理员id
            if(ObjectUtil.isEmpty(sysSendMessageDTO.getFrom())){
                message.setProducerId(configService.selectConfigByKey(UserConstants.SYS_ADMIN_ID_KEY));
            }else {
                message.setProducerId(sysSendMessageDTO.getFrom());
            }
            message.setTemplateCode(sysMessageTemplate.getTemplateCode());
            //组装参数消息体
            String messageContext = FreemarkUtils.process(sysMessageTemplate.getTemplateCode(), sysMessageTemplate.getTemplateBody(), sysSendMessageDTO.getParamMap());
            message.setMessageContent(messageContext);
            if(StringUtils.isNotEmpty(sysMessageTemplate.getPcUrl())){
                String pcUrl = FreemarkUtils.process(sysMessageTemplate.getTemplateCode(), sysMessageTemplate.getPcUrl(), sysSendMessageDTO.getParamMap());
                message.setPcUrl(pcUrl);
            }

            if(StringUtils.isNotEmpty(sysMessageTemplate.getAppUrl())){
                String appUrl = FreemarkUtils.process(sysMessageTemplate.getTemplateCode(), sysMessageTemplate.getAppUrl(), sysSendMessageDTO.getParamMap());
                message.setAppUrl(appUrl);
            }

        }catch (Exception e){
            log.error("@@消息中心在组装消息模板时出现异常：{}",e.getMessage());
            message.setMessageStatus(1L);
        }
        message.setMessageStatus(0L);
        message.setIconClass(sysMessageTemplate.getIconClass());
        Optional.ofNullable(sysSendMessageDTO.getMessageOutsideId()).ifPresent(t->message.setMessageOutsideId(sysSendMessageDTO.getMessageOutsideId()));
        if(ObjectUtil.isNotNull(sysSendMessageDTO.getMessageEventType())){
            message.setMessageType(sysSendMessageDTO.getMessageEventType().getCode());
        }
        message.setMessageShow(sysSendMessageDTO.getMessageShow());
        if(ObjectUtil.isNotNull(sysSendMessageDTO.getReceiver())){
            message.setConsumerId(sysSendMessageDTO.getReceiver());
            sysMessageTransitionService.insertSysMessageTransition(message);
        }

        if(!StringUtils.isEmpty(receiverSet)){
            receiverSet.forEach(t->{
                message.setConsumerId(t);
                sysMessageTransitionService.insertSysMessageTransition(message);
            });
        }
    }
}
