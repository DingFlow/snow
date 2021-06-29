package com.snow.web.controller.system;

import cn.hutool.core.collection.CollectionUtil;
import com.snow.common.annotation.Log;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.enums.MessageEventType;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.flowable.config.FlowIdGenerator;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.*;
import com.snow.system.service.ISysMessageTemplateService;
import com.snow.system.service.ISysMessageTransitionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息模板Controller
 * 
 * @author qimingjin
 * @date 2021-02-27
 */
@Controller
@RequestMapping("/system/messageCenter")
public class SysMessageCenterController extends BaseController
{
    private String prefix = "system/messageCenter";

    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;
    
    @Autowired
    private ISysMessageTransitionService sysMessageTransitionService;

    @RequiresPermissions("system:messageCenter:view")
    @GetMapping()
    public String messageCenter(ModelMap mmap)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        sysMessageTransition.setConsumerId(String.valueOf(sysUser.getUserId()));
        sysMessageTransition.setMessageStatus(0L);
        sysMessageTransition.setOrderBy("update_time desc");
        List<SysMessageTransition> sysMessageTransitions = sysMessageTransitionService.selectSysMessageTransitionList(sysMessageTransition);

        if(CollectionUtil.isNotEmpty(sysMessageTransitions)){
            List<SysMessageTransition> visitLogsList = sysMessageTransitions.stream().filter(t -> t.getMessageType().equals(MessageEventType.SEND_VISIT_LOG.getCode())).collect(Collectors.toList());
            SysMessageTransition.init(visitLogsList);
            long count = visitLogsList.stream().filter(t -> t.getMessageReadStatus() == 0).count();
            mmap.put("visitLogCount",count);
            mmap.put("visitLogs",visitLogsList);
        }

        if(CollectionUtil.isNotEmpty(sysMessageTransitions)){
            List<SysMessageTransition> emailList = sysMessageTransitions.stream().filter(t -> t.getMessageType().equals(MessageEventType.SEND_EMAIL.getCode())).collect(Collectors.toList());
            SysMessageTransition.init(emailList);
            long count = emailList.stream().filter(t -> t.getMessageReadStatus() == 0).count();
            mmap.put("emailListCount",count);
            mmap.put("emailList",emailList);
        }

        return prefix + "/messageCenter";
    }

    @GetMapping("/website")
    public String websiteMessageCenter(SysMessageTransition sysMessageTransition,ModelMap mmap)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysMessageTransition.setConsumerId(String.valueOf(sysUser.getUserId()));
        sysMessageTransition.setMessageShow(1);
        sysMessageTransition.setOrderBy("update_time desc");
        List<SysMessageTransition> list = sysMessageTransitionService.selectSysMessageTransitionList(sysMessageTransition);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        TableDataInfo dataTable = getDataTable(list);
        mmap.put("dataTable",dataTable);
        return "front/message/message_center";
    }
}
