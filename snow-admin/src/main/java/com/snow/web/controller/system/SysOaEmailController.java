package com.snow.web.controller.system;

import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.snow.common.annotation.Log;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.constant.SequenceConstants;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.core.text.Convert;
import com.snow.common.enums.BusinessType;
import com.snow.common.enums.MessageEventType;
import com.snow.common.enums.SysEmailSearchType;
import com.snow.common.utils.StringUtils;
import com.snow.framework.util.ShiroUtils;
import com.snow.common.core.domain.MessageEventRequest;
import com.snow.system.domain.*;
import com.snow.system.service.ISysOaEmailService;
import com.snow.system.service.ISysSequenceService;
import com.snow.system.service.impl.SysMessageTransitionServiceImpl;
import com.snow.system.service.impl.SysUserServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 邮件Controller
 * 
 * @author 没用的阿吉
 * @date 2021-03-12
 */
@Controller
@RequestMapping("/system/email")
public class SysOaEmailController extends BaseController
{
    private String prefix = "system/email";

    @Autowired
    private ISysOaEmailService sysOaEmailService;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private ISysSequenceService sequenceService;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private SysMessageTransitionServiceImpl sysMessageTransitionService;

    @Autowired
    private ApplicationContext applicationContext;

    @RequiresPermissions("system:email:view")
    @GetMapping()
    public String email()
    {

        return prefix + "/mailCompose";
    }


    /**
     * 查询邮件列表
     */
    @RequiresPermissions("system:email:getMyList")
    @PostMapping("/getMyList")
    @ResponseBody
    public TableDataInfo getMyList(SysOaEmailDO sysOaEmail)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        Integer mailSearchType = sysOaEmail.getMailSearchType();
        List<SysOaEmailVO> list =Lists.newArrayList();
        switch (mailSearchType){
            //收件
            case 6:
                sysOaEmail.setConsumerId(String.valueOf(sysUser.getUserId()));
                break;
            //发件
            case 7:
                sysOaEmail.setProducerId(String.valueOf(sysUser.getUserId()));
                break;
            //重要(我发件的或者收件的可标记为重要)
            case 3:
                sysOaEmail.setProducerOrConsumerId(String.valueOf(sysUser.getUserId()));
                sysOaEmail.setEmailStatus(3L);
                break;
            //发消息:草稿状态
            case 1:
                sysOaEmail.setProducerId(String.valueOf(sysUser.getUserId()));
                sysOaEmail.setEmailStatus(1L);
                list = sysOaEmailService.selectEmailList(sysOaEmail);
                return getDataTable(list);
            //收件存在垃圾
            case 4:
                sysOaEmail.setConsumerId(String.valueOf(sysUser.getUserId()));
                sysOaEmail.setEmailStatus(4L);
                break;
            default:
        }

        sysOaEmail.setMessageType(MessageEventType.SEND_EMAIL.getCode());
        sysOaEmail.setSortField("m.message_read_status asc,o.update_time desc");
        list=sysOaEmailService.selectEmailList(sysOaEmail);
        return getDataTable(list);
    }

    /**
     * 获取是否已读 true 已读，false 未读
     * @param outsideId
     * @return
     */
    @GetMapping("/getEmailIsRead/{outsideId}")
    @ResponseBody
    public AjaxResult getEmailIsRead(@PathVariable("outsideId") String outsideId){
        SysUser sysUser = ShiroUtils.getSysUser();
        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        sysMessageTransition.setMessageOutsideId(outsideId);
        sysMessageTransition.setConsumerId(String.valueOf(sysUser.getUserId()));
        sysMessageTransition.setMessageType(MessageEventType.SEND_EMAIL.getCode());
        return AjaxResult.success(sysMessageTransitionService.getIsRead(sysMessageTransition));
    }
    /**
     * 修改邮件
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysOaEmail sysOaEmail = sysOaEmailService.selectSysOaEmailById(id);
        mmap.put("sysOaEmail", sysOaEmail);
        return prefix + "/mailComposeEdit";
    }

    /**
     * 修改保存邮件
     */
    @RequiresPermissions("system:email:edit")
    @Log(title = "邮件", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysOaEmail sysOaEmail)
    {
        return toAjax(sysOaEmailService.updateSysOaEmail(sysOaEmail));
    }

    /**
     * 垃圾箱邮件
     */
    @RequiresPermissions("system:email:remarkTrash")
    @Log(title = "邮件", businessType = BusinessType.OTHER)
    @PostMapping( "/remarkTrash")
    @ResponseBody
    public AjaxResult remarkTrash(String ids)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        ArrayList<String> idList = Lists.newArrayList(Convert.toStrArray(ids));
        idList.forEach(t->{
            SysOaEmail sysOaEmail=new SysOaEmail();
            sysOaEmail.setId(Long.parseLong(t));
            sysOaEmail.setEmailStatus(SysEmailSearchType.TRASH.getCode().longValue());
            sysOaEmail.setUpdateBy(String.valueOf(sysUser.getUserId()));
            sysOaEmailService.updateSysOaEmail(sysOaEmail);
        });
        return AjaxResult.success();
    }


    /**
     * 标记邮件为已读
     */
    @RequiresPermissions("system:email:remarkRead")
    @Log(title = "邮件", businessType = BusinessType.OTHER)
    @PostMapping( "/remarkRead")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult remarkRead(String ids)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        SysOaEmail sysOaEmail=new SysOaEmail();
        sysOaEmail.setIdList(Lists.newArrayList(Convert.toStrArray(ids)));
        List<SysOaEmail> sysOaEmailList = sysOaEmailService.selectSysOaEmailList(sysOaEmail);
        sysOaEmailList.forEach(t->{
            SysMessageTransition sysMessageTransition=new SysMessageTransition();
            sysMessageTransition.setConsumerId(String.valueOf(sysUser.getUserId()));
            sysMessageTransition.setMessageType(MessageEventType.SEND_EMAIL.getCode());
            sysMessageTransition.setMessageOutsideId(t.getEmailNo());
            sysMessageTransition.setMessageReadStatus(1L);
            sysMessageTransitionService.updateByCondition(sysMessageTransition);
        });

        return AjaxResult.success();
    }

    /**
     * 标记为重要
     * @param ids
     * @return
     */
    @RequiresPermissions("system:email:remarkImportant")
    @Log(title = "邮件", businessType = BusinessType.OTHER)
    @PostMapping( "/remarkImportant")
    @ResponseBody
    public AjaxResult remarkImportant(String ids)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        ArrayList<String> idList = Lists.newArrayList(Convert.toStrArray(ids));
        idList.forEach(t->{
            SysOaEmail sysOaEmail=new SysOaEmail();
            sysOaEmail.setId(Long.parseLong(t));
            sysOaEmail.setEmailStatus(SysEmailSearchType.IMPORTANT.getCode().longValue());
            sysOaEmail.setUpdateBy(String.valueOf(sysUser.getUserId()));
            sysOaEmailService.updateSysOaEmail(sysOaEmail);
        });
        return AjaxResult.success();
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @RequiresPermissions("system:email:markDelete")
    @Log(title = "邮件", businessType = BusinessType.DELETE)
    @PostMapping( "/markDelete")
    @ResponseBody
    @Transactional
    public AjaxResult markDelete(String ids)
    {

        SysOaEmail sysOaEmail=new SysOaEmail();
        sysOaEmail.setIdList(Lists.newArrayList(Convert.toStrArray(ids)));
        List<SysOaEmail> sysOaEmailList = sysOaEmailService.selectSysOaEmailList(sysOaEmail);
        int j = sysMessageTransitionService.deleteSysMessageTransitionByOutsideId(sysOaEmailList.stream().map(SysOaEmail::getEmailNo).collect(Collectors.toList()));
        int i = sysOaEmailService.deleteSysOaEmailByIds(ids);
        return AjaxResult.success(j);
    }
    

    /**
     * 发送邮件
     * @param sysOaEmail
     * @return
     */
    @RequiresPermissions("system:email:sendSysEmail")
    @Log(title = "邮件", businessType = BusinessType.OTHER)
    @PostMapping( "/sendSysEmail")
    @ResponseBody
    @RepeatSubmit
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult sendSysEmail(SysOaEmail sysOaEmail){
        SysUser sysUser = ShiroUtils.getSysUser();
        if(StringUtils.isNotNull(sysOaEmail.getId())){
            sysOaEmail.setUpdateBy(String.valueOf(sysUser.getUserId()));
            sysOaEmailService.updateSysOaEmail(sysOaEmail);
        }else {
            String newSequenceNo = sequenceService.getNewSequenceNo(SequenceConstants.OA_EMAIL_SEQUENCE);
            sysOaEmail.setBelongUserId(String.valueOf(sysUser.getUserId()));
            sysOaEmail.setCreateBy(String.valueOf(sysUser.getUserId()));
            sysOaEmail.setUpdateBy(String.valueOf(sysUser.getUserId()));
            sysOaEmail.setEmailNo(newSequenceNo);
            sysOaEmail.setSendTime(new Date());
            sysOaEmailService.insertSysOaEmail(sysOaEmail);
        }

        String newSequenceNo=sysOaEmail.getEmailNo();
        //只有发送状态下才能发送
        if(sysOaEmail.getEmailStatus().intValue()==SysEmailSearchType.COMMON.getCode()){
            MessageEventRequest messageEventDTO=new MessageEventRequest(MessageEventType.SEND_EMAIL.getCode());
            messageEventDTO.setProducerId(String.valueOf(sysUser.getUserId()));
            messageEventDTO.setConsumerIds(Sets.newHashSet(sysOaEmail.getEmailTo()));
            messageEventDTO.setMessageEventType(MessageEventType.SEND_EMAIL);
            messageEventDTO.setMessageOutsideId(newSequenceNo);
            applicationContext.publishEvent(messageEventDTO);
        }
        return AjaxResult.success();
    }

    /**
     * 跳转邮件列表
     */
    @GetMapping("/mailbox/{mailType}")
    public String mailbox(@PathVariable("mailType") Long mailType,ModelMap mmap)
    {
        mmap.put("mailType",mailType);
        return prefix + "/mailbox";
    }


    /**
     * 邮件详情
     */
    @GetMapping("/mailDetail/{id}")
    public String mailDetail(@PathVariable("id") String id,ModelMap mmap)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        SysOaEmail sysOaEmail;
        if(NumberUtil.isLong(id)){
             sysOaEmail = sysOaEmailService.selectSysOaEmailById(Long.parseLong(id));
        }else {
             sysOaEmail = sysOaEmailService.selectSysOaEmailByEmailNo(id);
        }

        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        sysMessageTransition.setMessageOutsideId(sysOaEmail.getEmailNo());
        sysMessageTransition.setMessageType(MessageEventType.SEND_EMAIL.getCode());
        sysMessageTransition.setMessageStatus(0L);
        List<SysMessageTransition> sysMessageTransitions = sysMessageTransitionService.selectSysMessageTransitionList(sysMessageTransition);
        if(CollectionUtils.isNotEmpty(sysMessageTransitions)){
            sysOaEmail.setEmailFromUser(sysUserService.selectUserById(Long.parseLong(sysMessageTransitions.get(0).getProducerId())));
            //sysOaEmail.setEmailToUser(sysMessageTransitions.stream().map(get));
        }
        MessageEventRequest messageEventDTO=new MessageEventRequest(MessageEventType.MARK_READED.getCode());
        messageEventDTO.setConsumerIds(Sets.newHashSet(String.valueOf(sysUser.getUserId())));
        messageEventDTO.setMessageOutsideId(sysOaEmail.getEmailNo());
        messageEventDTO.setMessageEventType(MessageEventType.SEND_EMAIL);
        applicationContext.publishEvent(messageEventDTO);
        mmap.put("sysOaEmail", sysOaEmail);
        return prefix + "/mailDetail";
    }




    /**
     * 获取邮件读取情况
     * @return
     */
    @GetMapping("/getSysOaEmailData")
    @ResponseBody
    public AjaxResult getSysOaEmailData()
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        SysOaEmailDO sysOaEmail=new SysOaEmailDO();
        sysOaEmail.setMessageType(MessageEventType.SEND_EMAIL.getCode());
        List<SysOaEmailVO> list = sysOaEmailService.selectEmailList(sysOaEmail);
        SysOaEmailDataVO.SysOaEmailDataVOBuilder builder = SysOaEmailDataVO.builder();
        if(CollectionUtils.isNotEmpty(list)){
            /**
             * 收件数
             */
            long inboxTotal = list.stream().filter(t -> t.getConsumerId().equals(String.valueOf(sysUser.getUserId()))).count();
            builder.inboxTotal(inboxTotal);

            long sendMailTotal = list.stream().filter(t -> t.getProducerId().equals(String.valueOf(sysUser.getUserId()))).count();
            builder.sendMailTotal(sendMailTotal);

            //是否已读，针对的是收件
            long readTotal = list.stream().filter(t -> t.getConsumerId().equals(String.valueOf(sysUser.getUserId()))&&t.getMessageReadStatus()==0).count();
            builder.readTotal(readTotal);

            //垃圾标记，针对收件messageStatus = null
            long trashTotal = list.stream().filter(t -> (
                    t.getConsumerId().equals(String.valueOf(sysUser.getUserId())))&&t.getEmailStatus().intValue()==SysEmailSearchType.TRASH.getCode()).
                    count();
            builder.trashTotal(trashTotal);

            //草稿标记，针对我发件
            long draftsTotal= list.stream().filter(t -> (
                    t.getProducerId().equals(String.valueOf(sysUser.getUserId())))&&t.getEmailStatus().intValue()==SysEmailSearchType.DRAFTS.getCode()).
                    count();
            builder.draftsTotal(draftsTotal);
        }
        SysOaEmailDO importantSysOaEmail=new SysOaEmailDO();
        importantSysOaEmail.setMessageType(MessageEventType.SEND_EMAIL.getCode());
        importantSysOaEmail.setProducerOrConsumerId(String.valueOf(sysUser.getUserId()));
        List<SysOaEmailVO> importantSysOaEmailList = sysOaEmailService.selectEmailList(importantSysOaEmail);
        //重要标记：收件或者发件
        long importantTotal = importantSysOaEmailList.stream().filter(t -> t.getEmailStatus().intValue()==SysEmailSearchType.IMPORTANT.getCode()).
                count();
        builder.importantTotal(importantTotal);
        return AjaxResult.success(builder.build());
    }

}
