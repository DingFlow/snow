package com.snow.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
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
import com.snow.common.enums.SysEmailType;
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
public class SysOaEmailController extends BaseController {
    private String prefix = "system/email";

    @Autowired
    private ISysOaEmailService sysOaEmailService;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private ISysSequenceService sequenceService;


    @Autowired
    private SysMessageTransitionServiceImpl sysMessageTransitionService;

    @Autowired
    private ApplicationContext applicationContext;

    @RequiresPermissions("system:email:writeMail")
    @GetMapping()
    public String writeMail() {
        return prefix + "/write-email";
    }

    /**
     * 收件箱
     * @return 跳转收件箱
     */
    @RequiresPermissions("system:email:receivedMail")
    @GetMapping("/receivedMail")
    public String receivedMail() {
        return prefix + "/received-email";
    }

    @RequiresPermissions("system:email:receivedMail")
    @PostMapping("/getReceivedMail")
    @ResponseBody
    public TableDataInfo getReceivedMail(SysOaEmailDO sysOaEmail) {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaEmail.setMessageType(MessageEventType.SEND_EMAIL.getCode());
        sysOaEmail.setConsumerId(String.valueOf(sysUser.getUserId()));
        sysOaEmail.setSortField("m.message_read_status asc,o.update_time desc");
        return getDataTable(sysOaEmailService.selectEmailList(sysOaEmail));
    }

    /**
     * 发件箱
     * @return 跳转发件箱
     */
    @RequiresPermissions("system:email:sendMail")
    @GetMapping("/sendMail")
    public String sendMail() {
        return prefix + "/send-email";
    }

    @RequiresPermissions("system:email:sendMail")
    @PostMapping("/getSendMail")
    @ResponseBody
    public TableDataInfo getSendMail(SysOaEmailDO sysOaEmail) {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaEmail.setMessageType(MessageEventType.SEND_EMAIL.getCode());
        sysOaEmail.setProducerId(String.valueOf(sysUser.getUserId()));
        sysOaEmail.setSortField("m.message_read_status asc,o.update_time desc");
        return getDataTable(sysOaEmailService.selectEmailList(sysOaEmail));
    }

    /**
     * 草稿箱
     * @return 跳转草稿箱
     */
    @RequiresPermissions("system:email:draftsMail")
    @GetMapping("/drafts")
    public String drafts() {
        return prefix + "/drafts-email";
    }

    @RequiresPermissions("system:email:draftsMail")
    @PostMapping("/getDraftsMail")
    @ResponseBody
    public TableDataInfo getDraftsMail(SysOaEmailDTO sysOaEmail) {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaEmail.setBelongUserId(String.valueOf(sysUser.getUserId()));
        sysOaEmail.setEmailStatus(1L);
        return getDataTable(sysOaEmailService.selectSysOaEmailList(sysOaEmail));
    }

    /**
     * 标星邮件
     * @return 跳转标星邮件
     */
    @RequiresPermissions("system:email:importantMail")
    @GetMapping("/importantMail")
    public String importantMail() {
        return prefix + "/important-email";
    }

    @RequiresPermissions("system:email:importantMail")
    @PostMapping("/getImportantMail")
    @ResponseBody
    public TableDataInfo getImportantMail(SysOaEmailDO sysOaEmail) {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaEmail.setProducerOrConsumerId(String.valueOf(sysUser.getUserId()));
        sysOaEmail.setEmailStatus(3);
        sysOaEmail.setSortField("m.message_read_status asc,o.update_time desc");
        return getDataTable(sysOaEmailService.selectEmailList(sysOaEmail));
    }

    /**
     * 垃圾箱
     * @return 跳转垃圾箱
     */
    @RequiresPermissions("system:email:trashMail")
    @GetMapping("/trashMail")
    public String trash() {
        return prefix + "/trash-email";
    }

    @RequiresPermissions("system:email:trashMail")
    @PostMapping("/getTrashMail")
    @ResponseBody
    public TableDataInfo getTrashMail(SysOaEmailDO sysOaEmail) {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaEmail.setConsumerId(String.valueOf(sysUser.getUserId()));
        sysOaEmail.setEmailStatus(4);
        sysOaEmail.setSortField("m.message_read_status asc,o.update_time desc");
        return getDataTable(sysOaEmailService.selectEmailList(sysOaEmail));
    }


    /**
     * 获取是否已读 true 已读，false 未读received-mail
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
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        SysOaEmailDTO sysOaEmail = sysOaEmailService.selectSysOaEmailById(id);
        mmap.put("sysOaEmail", sysOaEmail);
        return prefix + "/edit-email";
    }

    /**
     * 修改保存邮件
     */
    @RequiresPermissions("system:email:edit")
    @Log(title = "邮件", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysOaEmailDTO sysOaEmail)
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
            SysOaEmailDTO sysOaEmail=new SysOaEmailDTO();
            sysOaEmail.setId(Long.parseLong(t));
            sysOaEmail.setEmailStatus(SysEmailType.TRASH.getCode().longValue());
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
        SysOaEmailDTO sysOaEmail=new SysOaEmailDTO();
        sysOaEmail.setIdList(Lists.newArrayList(Convert.toStrArray(ids)));
        List<SysOaEmailDTO> sysOaEmailList = sysOaEmailService.selectSysOaEmailList(sysOaEmail);
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
            SysOaEmailDTO sysOaEmail=new SysOaEmailDTO();
            sysOaEmail.setId(Long.parseLong(t));
            sysOaEmail.setEmailStatus(SysEmailType.IMPORTANT.getCode().longValue());
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

        SysOaEmailDTO sysOaEmail=new SysOaEmailDTO();
        sysOaEmail.setIdList(Lists.newArrayList(Convert.toStrArray(ids)));
        List<SysOaEmailDTO> sysOaEmailList = sysOaEmailService.selectSysOaEmailList(sysOaEmail);
        int j = sysMessageTransitionService.deleteSysMessageTransitionByOutsideId(sysOaEmailList.stream().map(SysOaEmailDTO::getEmailNo).collect(Collectors.toList()));
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
    public AjaxResult sendSysEmail(SysOaEmailDTO sysOaEmail){
        SysUser sysUser = ShiroUtils.getSysUser();
        if(StringUtils.isNotNull(sysOaEmail.getId())){
            SysOaEmail oaEmail = BeanUtil.copyProperties(sysOaEmail, SysOaEmail.class);
            oaEmail.setEmailToUser(Lists.newArrayList(sysOaEmail.getEmailTo()));
            sysOaEmailService.updateById(oaEmail);
        }else {
            String newSequenceNo = sequenceService.getNewSequenceNo(SequenceConstants.OA_EMAIL_SEQUENCE);
            sysOaEmail.setBelongUserId(String.valueOf(sysUser.getUserId()));
            sysOaEmail.setCreateBy(String.valueOf(sysUser.getUserId()));
            sysOaEmail.setUpdateBy(String.valueOf(sysUser.getUserId()));
            sysOaEmail.setEmailNo(newSequenceNo);
            sysOaEmail.setSendTime(new Date());
            SysOaEmail oaEmail = BeanUtil.copyProperties(sysOaEmail, SysOaEmail.class);
            oaEmail.setEmailToUser(Lists.newArrayList(sysOaEmail.getEmailTo()));
            sysOaEmailService.save(oaEmail);
        }

        String newSequenceNo=sysOaEmail.getEmailNo();
        //只有发送状态下才能发送
        if(sysOaEmail.getEmailStatus().intValue()== SysEmailType.COMMON.getCode()){
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
        SysOaEmailDTO sysOaEmail;
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
        return prefix + "/mail-detail";
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
                    t.getConsumerId().equals(String.valueOf(sysUser.getUserId())))&&t.getEmailStatus().intValue()== SysEmailType.TRASH.getCode()).
                    count();
            builder.trashTotal(trashTotal);

            //草稿标记，针对我发件
            long draftsTotal= list.stream().filter(t -> (
                    t.getProducerId().equals(String.valueOf(sysUser.getUserId())))&&t.getEmailStatus().intValue()== SysEmailType.DRAFTS.getCode()).
                    count();
            builder.draftsTotal(draftsTotal);
        }
        SysOaEmailDO importantSysOaEmail=new SysOaEmailDO();
        importantSysOaEmail.setMessageType(MessageEventType.SEND_EMAIL.getCode());
        importantSysOaEmail.setProducerOrConsumerId(String.valueOf(sysUser.getUserId()));
        List<SysOaEmailVO> importantSysOaEmailList = sysOaEmailService.selectEmailList(importantSysOaEmail);
        //重要标记：收件或者发件
        long importantTotal = importantSysOaEmailList.stream().filter(t -> t.getEmailStatus().intValue()== SysEmailType.IMPORTANT.getCode()).
                count();
        builder.importantTotal(importantTotal);
        return AjaxResult.success(builder.build());
    }


    /**
     * 查询邮件列表
     */
    @RequiresPermissions("system:email:getMyList")
    @PostMapping("/getMyList")
    @ResponseBody
    @Deprecated
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
                sysOaEmail.setEmailStatus(3);
                break;
            //发消息:草稿状态
            case 1:
                sysOaEmail.setProducerId(String.valueOf(sysUser.getUserId()));
                sysOaEmail.setEmailStatus(1);
                list = sysOaEmailService.selectEmailList(sysOaEmail);
                return getDataTable(list);
            //收件存在垃圾
            case 4:
                sysOaEmail.setConsumerId(String.valueOf(sysUser.getUserId()));
                sysOaEmail.setEmailStatus(4);
                break;
            default:
        }

        sysOaEmail.setMessageType(MessageEventType.SEND_EMAIL.getCode());
        sysOaEmail.setSortField("m.message_read_status asc,o.update_time desc");
        list=sysOaEmailService.selectEmailList(sysOaEmail);
        return getDataTable(list);
    }
}
