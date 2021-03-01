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
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-02-27 16:13
 **/
@Component
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;

    @Value("${spring.mail.username}")
    private String from;


    public void sendSimpleMail(SysSendMessageDTO sysSendMessageDTO) {
        SysMessageTemplate sysMessageTemplate= sysMessageTemplateService.getSysMessageTemplateByCode(sysSendMessageDTO.getTemplateByCode());

        Set<String> receiverSet = sysSendMessageDTO.getReceiverSet();


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
            mailSender.send(message);
        }catch (Exception e){
            log.error("调用sendSimpleMail发送邮件失败:"+e.getMessage());
            //todo 消息发送失败记录日志
        }

    }

   /* public void sendSimpleMail(Set<String> to, String subject, String content) throws MailException {
        ThreadPoolExecutor executor = ExecutorBuilder.create().setCorePoolSize(5)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(100))
                .build();

        executor.execute(() ->{
            to.forEach(t->{
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(from); // 邮件发送者
                message.setTo(t); // 邮件接受者
                message.setSubject(subject); // 主题
                message.setText(content); // 内容

                mailSender.send(message);
            });
        });
        executor.shutdown();

    }*/
}
