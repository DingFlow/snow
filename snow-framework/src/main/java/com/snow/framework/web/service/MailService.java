package com.snow.framework.web.service;

import cn.hutool.core.thread.ExecutorBuilder;
import com.snow.common.utils.PatternUtils;
import com.snow.common.utils.StringUtils;
import com.snow.framework.web.domain.common.SysSendMessageDTO;
import com.snow.system.domain.SysMessageTemplate;
import com.snow.system.service.ISysMessageTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-02-27 16:13
 **/
//@Component
@Slf4j
public class MailService {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private ISysMessageTemplateService sysMessageTemplateService;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 简单文本邮件
     * @param sysSendMessageDTO 对象
     */
    public void sendSimpleMail(SysSendMessageDTO sysSendMessageDTO) {
        SysMessageTemplate sysMessageTemplate= sysMessageTemplateService.getSysMessageTemplateByCode(sysSendMessageDTO.getTemplateByCode());
        Set<String> receiverSet = sysSendMessageDTO.getReceiverSet();
        Set<String> ccSet = sysSendMessageDTO.getCCSet();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            if(!StringUtils.isEmpty(sysSendMessageDTO.getReceiver())){
                message.setTo(sysSendMessageDTO.getReceiver());
            }
            message.setSubject(sysMessageTemplate.getTemplateName());
            message.setText(PatternUtils.builderTemplateBody(sysSendMessageDTO.getParamMap(),sysMessageTemplate.getTemplateBody()));
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
    public void sendHtmlMail(SysSendMessageDTO sysSendMessageDTO)  {
        SysMessageTemplate sysMessageTemplate= sysMessageTemplateService.getSysMessageTemplateByCode(sysSendMessageDTO.getTemplateByCode());
        Set<String> receiverSet = sysSendMessageDTO.getReceiverSet();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            if(StringUtils.isNotBlank(sysSendMessageDTO.getReceiver())){
                helper.setTo(sysSendMessageDTO.getReceiver());
            }
            helper.setSubject(sysMessageTemplate.getTemplateName());
            if (CollectionUtils.isNotEmpty(sysSendMessageDTO.getReceiverSet())) {
                helper.setTo(receiverSet.toArray(new String[0]));
            }
            helper.setText(PatternUtils.builderTemplateBody(sysSendMessageDTO.getParamMap(),sysMessageTemplate.getTemplateBody()), true);
            helper.setFrom(from);
            if(null != sysSendMessageDTO.getSentDate()){
                message.setSentDate(sysSendMessageDTO.getSentDate());
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
    public void sendAttachmentsMail(SysSendMessageDTO sysSendMessageDTO) {
        SysMessageTemplate sysMessageTemplate= sysMessageTemplateService.getSysMessageTemplateByCode(sysSendMessageDTO.getTemplateByCode());
        Set<String> receiverSet = sysSendMessageDTO.getReceiverSet();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            if(StringUtils.isNotBlank(sysSendMessageDTO.getReceiver())){
                helper.setTo(sysSendMessageDTO.getReceiver());
            }
            helper.setSubject(sysMessageTemplate.getTemplateName());
            helper.setText(PatternUtils.builderTemplateBody(sysSendMessageDTO.getParamMap(),sysMessageTemplate.getTemplateBody()), true);
            helper.setFrom(from);
            if (CollectionUtils.isNotEmpty(sysSendMessageDTO.getReceiverSet())) {
                helper.setTo(receiverSet.toArray(new String[0]));
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
}
