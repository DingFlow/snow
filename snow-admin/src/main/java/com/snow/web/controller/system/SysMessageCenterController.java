package com.snow.web.controller.system;

import cn.hutool.core.collection.CollUtil;
import com.snow.common.annotation.Log;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.core.text.Convert;
import com.snow.common.enums.BusinessType;
import com.snow.common.enums.MessageEventType;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysMessageTransition;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysMessageTransitionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息中心
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
    private ISysMessageTransitionService sysMessageTransitionService;

    /**
     * 跳转消息中心界面
     * @param mmap
     * @return
     */
    @RequiresPermissions("system:messageCenter:view")
    @GetMapping()
    @Deprecated
    public String messageCenter(ModelMap mmap)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        sysMessageTransition.setConsumerId(String.valueOf(sysUser.getUserId()));
        sysMessageTransition.setMessageStatus(0L);
        sysMessageTransition.setOrderBy("update_time desc");
        List<SysMessageTransition> sysMessageTransitions = sysMessageTransitionService.selectSysMessageTransitionList(sysMessageTransition);

        //拜访日志tab页数据
        if(CollUtil.isNotEmpty(sysMessageTransitions)){
            List<SysMessageTransition> visitLogsList = sysMessageTransitions.stream().filter(t -> t.getMessageType().equals(MessageEventType.SEND_VISIT_LOG.getCode())).collect(Collectors.toList());
            long visitLogCount = visitLogsList.stream().filter(t -> t.getMessageReadStatus() == 0).count();
            mmap.put("visitLogCount",visitLogCount);
            mmap.put("visitLogs",visitLogsList);
        }

        if(CollUtil.isNotEmpty(sysMessageTransitions)){
            List<SysMessageTransition> emailList = sysMessageTransitions.stream().filter(t -> t.getMessageType().equals(MessageEventType.SEND_EMAIL.getCode())).collect(Collectors.toList());
            SysMessageTransition.init(emailList);
            long count = emailList.stream().filter(t -> t.getMessageReadStatus() == 0).count();
            mmap.put("emailListCount",count);
            mmap.put("emailList",emailList);
        }

        //待办tab页数据
        if(CollUtil.isNotEmpty(sysMessageTransitions)){
            List<SysMessageTransition> todoTaskList = sysMessageTransitions.stream().filter(t -> t.getMessageType().equals(MessageEventType.INNER_TASK_TODO.getCode())).collect(Collectors.toList());
            long count = todoTaskList.stream().filter(t -> t.getMessageReadStatus() == 0).count();
            mmap.put("todoTaskCount",count);
            mmap.put("todoTaskList",todoTaskList);
        }

        //流程完结tab页数据
        if(CollUtil.isNotEmpty(sysMessageTransitions)){
            List<SysMessageTransition> processEndList = sysMessageTransitions.stream().filter(t -> t.getMessageType().equals(MessageEventType.INNER_PROCESS_END.getCode())).collect(Collectors.toList());
            long count = processEndList.stream().filter(t -> t.getMessageReadStatus() == 0).count();
            mmap.put("processEndCount",count);
            mmap.put("processEndList",processEndList);
        }
        //系统任务
        if(CollUtil.isNotEmpty(sysMessageTransitions)){
            List<SysMessageTransition> sysTaskList = sysMessageTransitions.stream().filter(t -> t.getMessageType().equals(MessageEventType.INNER_SYS_TASK_COMPLETE.getCode())
            ||t.getMessageType().equals(MessageEventType.INNER_SYS_TODO_TASK.getCode())).collect(Collectors.toList());
            long count = sysTaskList.stream().filter(t -> t.getMessageReadStatus() == 0).count();
            mmap.put("sysTaskCount",count);
            mmap.put("sysTaskList",sysTaskList);
        }

        return prefix + "/message_center";
    }

    /**
     * 消息列表
     * @param sysMessageTransition
     * @return
     */
    @RequiresPermissions("system:messageCenter:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysMessageTransition sysMessageTransition) {
        startPage();
        sysMessageTransition.setConsumerId(String.valueOf(ShiroUtils.getUserId()));
        sysMessageTransition.setOrderBy("create_time desc");
        List<SysMessageTransition> sysMessageTransitions = sysMessageTransitionService.selectSysMessageTransitionList(sysMessageTransition);
        return getDataTable(sysMessageTransitions);
    }

    /**
     * 详情
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:messageCenter:detail")
    public String detail(@PathVariable("id") Long id, ModelMap mmap) {
        SysMessageTransition sysMessageTransition = sysMessageTransitionService.selectSysMessageTransitionById(id);
        mmap.put("sysMessageTransition", sysMessageTransition);
        return prefix + "/detail";
    }

    /**
     * 官网消息中心
     * @param sysMessageTransition
     * @param mmap
     * @return
     */
    @GetMapping("/website")
    public String websiteMessageCenter(SysMessageTransition sysMessageTransition,ModelMap mmap) {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysMessageTransition.setConsumerId(String.valueOf(sysUser.getUserId()));
        sysMessageTransition.setMessageShow(1);
        sysMessageTransition.setOrderBy("create_time desc");
        List<SysMessageTransition> list = sysMessageTransitionService.selectSysMessageTransitionList(sysMessageTransition);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        TableDataInfo dataTable = getDataTable(list);
        mmap.put("dataTable",dataTable);
        return "front/message/message_center";
    }


    /**
     * 标记为已读
     * @param id
     * @return
     */
    @RequestMapping( "/remarkRead")
    @ResponseBody
    public AjaxResult remarkRead(Long id)
    {
        SysMessageTransition oldSysMessageTransition = sysMessageTransitionService.selectSysMessageTransitionById(id);
        if(oldSysMessageTransition.getMessageReadStatus()==1){
            return AjaxResult.success();
        }
        SysUser sysUser = ShiroUtils.getSysUser();
        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        sysMessageTransition.setId(id);
        sysMessageTransition.setMessageReadStatus(1L);
        sysMessageTransition.setUpdateBy(String.valueOf(sysUser.getUserId()));
        int i = sysMessageTransitionService.updateSysMessageTransition(sysMessageTransition);
        return AjaxResult.success(i);
    }

    /**
     * 标记已读
     * @param ids
     * @return
     */
    @RequiresPermissions("system:messageCenter:mark")
    @PostMapping( "/mark")
    @ResponseBody
    public AjaxResult mark(String ids)
    {
        List<String> idList = Convert.toStrList(ids);
        idList.forEach(t->{
            SysUser sysUser = ShiroUtils.getSysUser();
            SysMessageTransition sysMessageTransition=new SysMessageTransition();
            sysMessageTransition.setId(Long.parseLong(t));
            sysMessageTransition.setMessageReadStatus(1L);
            sysMessageTransition.setUpdateBy(String.valueOf(sysUser.getUserId()));
            sysMessageTransitionService.updateSysMessageTransition(sysMessageTransition);
        });
        return  AjaxResult.success();
    }

    /**
     * 删除消息
     *
     * @param ids
     * @return
     */
    @RequiresPermissions("system:messageCenter:remove")
    @Log(title = "消息中心", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysMessageTransitionService.deleteSysMessageTransitionByIds(ids));
    }
}
