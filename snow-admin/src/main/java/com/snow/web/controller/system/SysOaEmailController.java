package com.snow.web.controller.system;

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
import com.snow.common.enums.SysEmailSearchType;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.framework.util.ShiroUtils;
import com.snow.framework.web.domain.common.SysSendEmailDTO;
import com.snow.framework.web.service.MailService;
import com.snow.system.domain.SysOaEmail;
import com.snow.system.domain.SysOaEmailDataVO;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysOaEmailService;
import com.snow.system.service.ISysSequenceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private MailService mailService;

    @Autowired
    private ISysSequenceService sequenceService;

    @Value("${spring.mail.username}")
    private String from;


    @RequiresPermissions("system:email:view")
    @GetMapping()
    public String email(ModelMap mmap)
    {

        return prefix + "/mailCompose";
    }

    /**
     * 查询邮件列表
     */
    @RequiresPermissions("system:email:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysOaEmail sysOaEmail)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        Integer mailSearchType = sysOaEmail.getMailSearchType();
        switch (mailSearchType){
            case 6:
                //目前支持单个发
                sysOaEmail.setEmailTo(String.valueOf(sysUser.getUserId()));
                sysOaEmail.setIsSuccess(0);
                break;
            case 7:
                sysOaEmail.setEmailFrom(String.valueOf(sysUser.getUserId()));
                break;
                //重要
            case 3:
                sysOaEmail.setEmailTo(String.valueOf(sysUser.getUserId()));
                sysOaEmail.setEmailFrom(String.valueOf(sysUser.getUserId()));
                sysOaEmail.setEmailStatus(3L);
                List<SysOaEmail> sysOaEmailList = sysOaEmailService.selectImportantEmailList(sysOaEmail);
                return getDataTable(sysOaEmailList);
                //草稿
            case 1:
                sysOaEmail.setEmailFrom(String.valueOf(sysUser.getUserId()));
                sysOaEmail.setEmailStatus(1L);
                break;
                //垃圾
            case 4:
                sysOaEmail.setEmailTo(String.valueOf(sysUser.getUserId()));
                sysOaEmail.setEmailStatus(4L);
                break;
                default:
        }
        List<SysOaEmail> list = sysOaEmailService.selectSysOaEmailList(sysOaEmail);
        return getDataTable(list);
    }

    /**
     * 导出邮件列表
     */
    @RequiresPermissions("system:email:export")
    @Log(title = "邮件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOaEmail sysOaEmail)
    {
        List<SysOaEmail> list = sysOaEmailService.selectSysOaEmailList(sysOaEmail);
        ExcelUtil<SysOaEmail> util = new ExcelUtil<SysOaEmail>(SysOaEmail.class);
        return util.exportExcel(list, "email");
    }


    /**
     * 修改邮件
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysOaEmail sysOaEmail = sysOaEmailService.selectSysOaEmailById(id);
        mmap.put("sysOaEmail", sysOaEmail);
        return prefix + "/edit";
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
        ArrayList<String> idList = Lists.newArrayList(Convert.toStrArray(ids));
        idList.forEach(t->{
            SysOaEmail sysOaEmail=new SysOaEmail();
            sysOaEmail.setId(Long.parseLong(t));
            sysOaEmail.setIsRead(1L);
            sysOaEmail.setUpdateBy(String.valueOf(sysUser.getUserId()));
            sysOaEmailService.updateSysOaEmail(sysOaEmail);
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
     * 标记为
     * @param ids
     * @return
     */
    @RequiresPermissions("system:email:markDelete")
    @Log(title = "邮件", businessType = BusinessType.OTHER)
    @PostMapping( "/markDelete")
    @ResponseBody
    public AjaxResult markDelete(String ids)
    {
        int i = sysOaEmailService.deleteSysOaEmailByIds(ids);
        return AjaxResult.success(i);
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
        String newSequenceNo = sequenceService.getNewSequenceNo(SequenceConstants.OA_EMAIL_SEQUENCE);
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaEmail.setBelongUserId(String.valueOf(sysUser.getUserId()));
        //发送人id
        sysOaEmail.setEmailFrom(String.valueOf(sysUser.getUserId()));
        sysOaEmail.setCreateBy(String.valueOf(sysUser.getUserId()));
        sysOaEmail.setUpdateBy(String.valueOf(sysUser.getUserId()));
        sysOaEmail.setEmailType(0L);
        sysOaEmail.setEmailNo(newSequenceNo);
        sysOaEmail.setSendTime(new Date());
        int i = sysOaEmailService.insertSysOaEmail(sysOaEmail);

        //只有发送状态下才能发送
        if(sysOaEmail.getEmailStatus().intValue()==SysEmailSearchType.COMMON.getCode()){
            SysSendEmailDTO build = SysSendEmailDTO.builder().from(from)
                    .subject(sysOaEmail.getEmailSubject())
                    .content(sysOaEmail.getEmailContent())
                    .receiverSet(Sets.newHashSet(sysOaEmail.getEmailTo()))
                    .sentDate(new Date())
                    .build();
            mailService.sendHtmlMail(build);
        }
        return AjaxResult.success(i);
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
    public String mailDetail(@PathVariable("id") Long id,ModelMap mmap)
    {
        //标记为已读
        SysUser sysUser = ShiroUtils.getSysUser();
        SysOaEmail readSysOaEmail=new SysOaEmail();
        readSysOaEmail.setId(id);
        readSysOaEmail.setIsRead(1L);
        readSysOaEmail.setUpdateBy(String.valueOf(sysUser.getUserId()));
        sysOaEmailService.updateSysOaEmail(readSysOaEmail);

        SysOaEmail sysOaEmail = sysOaEmailService.selectSysOaEmailById(id);
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
        SysOaEmail sysOaEmail=new SysOaEmail();
        sysOaEmail.setEmailTo(String.valueOf(sysUser.getUserId()));
        sysOaEmail.setIsDelete(0L);
        List<SysOaEmail> list = sysOaEmailService.selectSysOaEmailList(sysOaEmail);
        SysOaEmailDataVO.SysOaEmailDataVOBuilder builder = SysOaEmailDataVO.builder();
        if(CollectionUtils.isNotEmpty(list)){


            long inboxTotal = list.stream().filter(t -> t.getEmailTo().equals(String.valueOf(sysUser.getUserId()))).count();
            builder.inboxTotal(inboxTotal);

            long sendMailTotal = list.stream().filter(t -> t.getEmailFrom().equals(String.valueOf(sysUser.getUserId()))).count();
            builder.sendMailTotal(sendMailTotal);

            //是否已读，针对的是收件
            long readTotal = list.stream().filter(t -> (t.getEmailTo().equals(String.valueOf(sysUser.getUserId()))&&t.getIsRead()==0)).count();
            builder.readTotal(readTotal);

            //重要标记：收件或者发件
            long importantTotal = list.stream().filter(t -> (
                    t.getEmailTo().equals(String.valueOf(sysUser.getUserId()))||
                            t.getEmailFrom().equals(String.valueOf(sysUser.getUserId())))&&t.getEmailStatus().intValue()==SysEmailSearchType.IMPORTANT.getCode()).
                    count();
            builder.importantTotal(importantTotal);

            //垃圾标记，针对收件
            long trashTotal = list.stream().filter(t -> (
                    t.getEmailTo().equals(String.valueOf(sysUser.getUserId())))&&t.getEmailStatus().intValue()==SysEmailSearchType.TRASH.getCode()).
                    count();
            builder.trashTotal(trashTotal);

            //草稿标记，针对我发件
            long draftsTotal= list.stream().filter(t -> (
                    t.getEmailFrom().equals(String.valueOf(sysUser.getUserId())))&&t.getEmailStatus().intValue()==SysEmailSearchType.DRAFTS.getCode()).
                    count();
            builder.draftsTotal(draftsTotal);
        }
        return AjaxResult.success(builder.build());
    }

}
