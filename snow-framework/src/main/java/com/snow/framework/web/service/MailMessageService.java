package com.snow.framework.web.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.snow.common.utils.StringUtils;
import com.snow.framework.util.FreemarkUtils;
import com.snow.framework.web.domain.common.SysSendEmailRequest;
import com.snow.framework.web.domain.common.SysSendMessageRequest;
import com.snow.system.domain.SysMessageTemplate;
import com.snow.system.service.ISysMessageTemplateService;
import com.snow.system.service.impl.SysOaEmailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Set;

/**
 * @program: snow
 * @description 发送email邮件
 * @author: 没用的阿吉
 * @create: 2021-02-27 16:13
 **/
@Component
@Slf4j
public class MailMessageService {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private ISysMessageTemplateService sysMessageTemplateService;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private SysOaEmailServiceImpl sysOaEmailService;
    /**
     * 简单文本邮件
     * @param sysSendMessageDTO 对象
     */
    public void sendTemplateSimpleMail(SysSendMessageRequest sysSendMessageDTO) {
        SysMessageTemplate sysMessageTemplate= sysMessageTemplateService.getSysMessageTemplateByCode(sysSendMessageDTO.getTemplateByCode());
        if(ObjectUtil.isNull(sysMessageTemplate)){
            log.error("@发送邮件的模板code不正确...");
            throw new RuntimeException("模板code不正确");
        }
        Set<String> receiverSet = sysSendMessageDTO.getReceiverSet();
        Set<String> ccSet = sysSendMessageDTO.getCCSet();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            if(!StringUtils.isEmpty(sysSendMessageDTO.getReceiver())){
                message.setTo(sysSendMessageDTO.getReceiver());
            }
            message.setSubject(sysMessageTemplate.getTemplateName());
            String messageContext = FreemarkUtils.process(sysMessageTemplate.getTemplateCode(), sysMessageTemplate.getTemplateBody(), sysSendMessageDTO.getParamMap());
            message.setText(messageContext);
            if(CollectionUtils.isNotEmpty(receiverSet)){
                message.setTo(receiverSet.toArray(new String[0]));
            }
            if(CollectionUtils.isNotEmpty(ccSet)){
                message.setCc(ccSet.toArray(new String[0]));
            }
            if(null != sysSendMessageDTO.getSentDate()){
                message.setSentDate(sysSendMessageDTO.getSentDate());
            }
            mailSender.send(message);
        }catch (Exception e){
            log.error("调用sendSimpleMail发送邮件失败:{}",e.getMessage());
            //todo 消息发送失败记录日志
        }

    }

    /**
     * HTML 文本邮件
     * @param  sysSendMessageDTO
     */
    public void sendTemplateHtmlMail(SysSendMessageRequest sysSendMessageDTO)  {
        SysMessageTemplate sysMessageTemplate= sysMessageTemplateService.getSysMessageTemplateByCode(sysSendMessageDTO.getTemplateByCode());
        //接收人set
        Set<String> receiverSet = sysSendMessageDTO.getReceiverSet();
        //抄送人
        Set<String> ccSet = sysSendMessageDTO.getCCSet();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            if(StringUtils.isNotBlank(sysSendMessageDTO.getReceiver())){
                helper.setTo(sysSendMessageDTO.getReceiver());
            }
            helper.setSubject(sysMessageTemplate.getTemplateName());
            if (CollectionUtils.isNotEmpty(sysSendMessageDTO.getReceiverSet())) {
                Convert.toStrArray(sysSendMessageDTO.getReceiverSet());
                helper.setTo(Convert.toStrArray(receiverSet));
            }
            if(CollectionUtils.isNotEmpty(ccSet)){
                helper.setCc(ccSet.toArray(new String[0]));
            }
            String messageContext = FreemarkUtils.process(sysMessageTemplate.getTemplateCode(), sysMessageTemplate.getTemplateBody(), sysSendMessageDTO.getParamMap());
            helper.setText(messageContext,true);
            helper.setFrom(from);
            if(null != sysSendMessageDTO.getSentDate()){
                helper.setSentDate(sysSendMessageDTO.getSentDate());
            }
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("调用sendSimpleMail发送邮件失败:"+e.getMessage());
            //todo 消息发送失败记录日志
        }
    }
    /**
     * 附件邮件
     * @param sysSendMessageDTO 接收者邮件
     * @throws MessagingException
     */
    public void sendTemplateAttachmentsMail(SysSendMessageRequest sysSendMessageDTO) {
        SysMessageTemplate sysMessageTemplate= sysMessageTemplateService.getSysMessageTemplateByCode(sysSendMessageDTO.getTemplateByCode());
        Set<String> receiverSet = sysSendMessageDTO.getReceiverSet();
        Set<String> ccSet = sysSendMessageDTO.getCCSet();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            if(StringUtils.isNotBlank(sysSendMessageDTO.getReceiver())){
                helper.setTo(sysSendMessageDTO.getReceiver());
            }
            helper.setSubject(sysMessageTemplate.getTemplateName());
            String messageContext = FreemarkUtils.process(sysMessageTemplate.getTemplateCode(), sysMessageTemplate.getTemplateBody(), sysSendMessageDTO.getParamMap());
            helper.setText(messageContext, true);
            helper.setFrom(from);
            if (CollectionUtils.isNotEmpty(sysSendMessageDTO.getReceiverSet())) {
                helper.setTo(receiverSet.toArray(new String[0]));
            }
            if(CollectionUtils.isNotEmpty(ccSet)){
                helper.setCc(ccSet.toArray(new String[0]));
            }
            if(null != sysSendMessageDTO.getSentDate()){
                message.setSentDate(sysSendMessageDTO.getSentDate());
            }
            FileSystemResource file = new FileSystemResource(new File(sysSendMessageDTO.getFilePath()));
            String fileName = file.getFilename();
            helper.addAttachment(fileName, file);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("调用sendSimpleMail发送邮件失败:"+e.getMessage());
            //todo 消息发送失败记录日志
        }

    }

    /**
     * 发送邮件
     * @param SysSendEmailDTO
     */
    @Deprecated
    public void sendHtmlMail(SysSendEmailRequest SysSendEmailDTO) {
        Set<String> receiverSet = SysSendEmailDTO.getReceiverSet();
        Set<String> ccSet = SysSendEmailDTO.getCCSet();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(SysSendEmailDTO.getFrom());
            helper.setSubject(SysSendEmailDTO.getSubject());
            helper.setText(SysSendEmailDTO.getContent());
            if (CollectionUtils.isNotEmpty(receiverSet)) {
                helper.setTo(receiverSet.toArray(new String[0]));
            }
            if(CollectionUtils.isNotEmpty(ccSet)){
                helper.setCc(ccSet.toArray(new String[0]));
            }
            if(null != SysSendEmailDTO.getSentDate()){
                helper.setSentDate(SysSendEmailDTO.getSentDate());
            }
            mailSender.send(message);
        }catch (Exception e){
            log.error("调用sendSimpleMail发送邮件失败:{}",e.getMessage());
        }

    }
}
