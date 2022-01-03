package com.snow.web.controller.system;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.snow.common.constant.MessageConstants;
import com.snow.common.core.domain.MessageEventRequest;
import com.snow.common.enums.DingFlowTaskType;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.enums.MessageEventType;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.*;
import com.snow.system.domain.SysOaTaskDistribute;
import com.snow.system.event.SyncEvent;
import com.snow.system.service.ISysOaTaskDistributeService;
import com.snow.system.service.ISysUserService;
import org.apache.commons.compress.utils.Sets;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.snow.common.annotation.Log;
import com.snow.common.enums.BusinessType;
import org.springframework.stereotype.Controller;
import com.snow.system.service.ISysOaTaskService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 系统任务Controller
 * 当任务被分配后，判断是否同步到钉钉系统
 * @author 没用的阿吉
 * @date 2021-07-29
 */
@Controller
@RequestMapping("/system/task")
public class SysOaTaskController extends BaseController
{
    private String prefix = "system/task";

    @Autowired
    private ISysOaTaskService sysOaTaskService;

    @Autowired
    private ISysOaTaskDistributeService sysOaTaskDistributeService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ApplicationContext applicationContext;

    @RequiresPermissions("system:task:view")
    @GetMapping()
    public String task()
    {
        return prefix + "/task";
    }

    /**
     * 查询系统任务列表
     */
    @RequiresPermissions("system:task:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysOaTask sysOaTask)
    {
        startPage();
        List<SysOaTask> list = sysOaTaskService.selectSysOaTaskList(sysOaTask);
        return getDataTable(list);
    }

    /**
     * 任务分配列表
     */
    @RequiresPermissions("system:task:taskDistributeList")
    @PostMapping("/taskDistributeList")
    @ResponseBody
    public TableDataInfo taskDistributeList(SysOaTaskDistribute sysOaTaskDistribute)
    {
        startPage();
        List<SysOaTaskDistribute> list = sysOaTaskDistributeService.selectSysOaTaskDistributeList(sysOaTaskDistribute);
        if(CollUtil.isNotEmpty(list)){
            warpSysOaTask(list);
        }
        return getDataTable(list);
    }
    /**
     * 查询我发起系统任务列表
     */
    @RequiresPermissions("system:task:myList")
    @PostMapping("/myList")
    @ResponseBody
    public TableDataInfo myList(SysOaTask sysOaTask)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaTask.setCreateBy(String.valueOf(sysUser.getUserId()));
        List<SysOaTask> list = sysOaTaskService.selectSysOaTaskList(sysOaTask);
        return getDataTable(list);
    }

    /**
     * 我的待处理任务
     * @param sysOaTaskDistribute
     * @return
     */
    @RequiresPermissions("system:task:waitList")
    @PostMapping("/waitList")
    @ResponseBody
    public TableDataInfo waitList(SysOaTaskDistribute sysOaTaskDistribute)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaTaskDistribute.setTaskExecuteStatusList(Lists.newArrayList(DingFlowTaskType.RUNNING.getCode(),DingFlowTaskType.PROCESSING.getCode()));
        sysOaTaskDistribute.setTaskDistributeId(String.valueOf(sysUser.getUserId()));
        List<SysOaTaskDistribute> sysOaTaskDistributes = sysOaTaskDistributeService.selectSysOaTaskDistributeList(sysOaTaskDistribute);
        if(CollUtil.isNotEmpty(sysOaTaskDistributes)){
            warpSysOaTask(sysOaTaskDistributes);
        }
        return getDataTable(sysOaTaskDistributes);
    }

    /**
     * 我处理的任务
     * @param sysOaTaskDistribute
     * @return
     */
    @RequiresPermissions("system:task:handleList")
    @PostMapping("/handleList")
    @ResponseBody
    public TableDataInfo handleList(SysOaTaskDistribute sysOaTaskDistribute)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaTaskDistribute.setTaskExecuteId(String.valueOf(sysUser.getUserId()));
        sysOaTaskDistribute.setTaskExecuteStatus(DingFlowTaskType.COMPLETED.getCode());
        List<SysOaTaskDistribute> sysOaTaskDistributes = sysOaTaskDistributeService.selectSysOaTaskDistributeList(sysOaTaskDistribute);
        if(CollUtil.isNotEmpty(sysOaTaskDistributes)){
            warpSysOaTask(sysOaTaskDistributes);
        }
        return getDataTable(sysOaTaskDistributes);
    }

    /**
     * 导出系统任务列表
     */
    @RequiresPermissions("system:task:export")
    @Log(title = "系统任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOaTask sysOaTask)
    {
        List<SysOaTask> list = sysOaTaskService.selectSysOaTaskList(sysOaTask);
        ExcelUtil<SysOaTask> util = new ExcelUtil<SysOaTask>(SysOaTask.class);
        return util.exportExcel(list, "task");
    }

    /**
     * 新增系统任务
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存系统任务
     */
    @RequiresPermissions("system:task:add")
    @Log(title = "系统任务", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysOaTask sysOaTask)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaTask.setCreateBy(String.valueOf(sysUser.getUserId()));
        return toAjax(sysOaTaskService.insertSysOaTask(sysOaTask));
    }

    @RequiresPermissions("system:task:start")
    @PostMapping("/start")
    @ResponseBody
    public AjaxResult start(Long id)
    {
        SysOaTaskDistribute sysOaTaskDistribute =new SysOaTaskDistribute();
        sysOaTaskDistribute.setId(id);
        sysOaTaskDistribute.setTaskStartTime(new Date());
        sysOaTaskDistribute.setTaskExecuteStatus(DingFlowTaskType.PROCESSING.getCode());
        return toAjax(sysOaTaskDistributeService.updateSysOaTaskDistribute(sysOaTaskDistribute));
    }
    /**
     * 处理系统任务
     */
    @GetMapping("/handle/{id}")
    public String handle(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysOaTaskDistribute sysOaTaskDistribute = sysOaTaskDistributeService.selectSysOaTaskDistributeById(id);
        warpSysOaTask(Lists.newArrayList(sysOaTaskDistribute));
        mmap.put("sysOaTaskDistribute", sysOaTaskDistribute);
        return prefix + "/handle";
    }


    /**
     * 处理保存系统任务
     */
    @RequiresPermissions("system:task:handle")
    @Log(title = "系统任务", businessType = BusinessType.UPDATE)
    @PostMapping("/handle")
    @ResponseBody
    public AjaxResult handleSave(SysOaTaskDistribute sysOaTaskDistribute)
    {
        sysOaTaskDistribute.setTaskCompleteTime(new Date());
        sysOaTaskDistribute.setTaskExecuteId(String.valueOf(ShiroUtils.getUserId()));
        sysOaTaskDistribute.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        sysOaTaskDistribute.setTaskExecuteStatus(DingFlowTaskType.COMPLETED.getCode());
        sendInnerMessage(sysOaTaskDistribute);
        //事件发送
        SyncEvent<SysOaTaskDistribute> syncEvent = new SyncEvent(sysOaTaskDistribute, DingTalkListenerType.UPDATE_TODO_TASK_EXECUTOR_STATUS);
        applicationContext.publishEvent(syncEvent);
        return toAjax(sysOaTaskDistributeService.updateSysOaTaskDistribute(sysOaTaskDistribute));
    }

    /**
     * 发送站内信
     * @param sysOaTaskDistribute 参数
     */
    private void sendInnerMessage(SysOaTaskDistribute sysOaTaskDistribute){
        MessageEventRequest messageEventDTO=new MessageEventRequest(MessageEventType.INNER_SYS_TASK_COMPLETE.getCode());
        messageEventDTO.setProducerId(sysOaTaskDistribute.getCreateBy());
        messageEventDTO.setConsumerIds(Sets.newHashSet(sysOaTaskDistribute.getTaskDistributeId()));
        messageEventDTO.setMessageEventType(MessageEventType.INNER_SYS_TASK_COMPLETE);
        messageEventDTO.setMessageShow(2);
        Map<String,Object> map= new HashMap<>();
        map.put("startUser",ShiroUtils.getUserName());
        map.put("businessKey", sysOaTaskDistribute.getTaskNo());
        map.put("startTime", DateUtil.formatDateTime(sysOaTaskDistribute.getTaskCompleteTime()));
        map.put("id",sysOaTaskDistribute.getId());
        messageEventDTO.setParamMap(map);
        messageEventDTO.setTemplateCode(MessageConstants.INNER_SYS_TASK_COMPLETE);
        applicationContext.publishEvent(messageEventDTO);
    }
    /**
     * 修改系统任务
     */
    @GetMapping("/edit/{taskNo}")
    public String edit(@PathVariable("taskNo") String taskNo, ModelMap mmap)
    {
        SysOaTask sysOaTask = sysOaTaskService.selectSysOaTaskById(taskNo);
        mmap.put("sysOaTask", sysOaTask);
        return prefix + "/edit";
    }

    /**
     * 修改保存系统任务
     */
    @RequiresPermissions("system:task:edit")
    @Log(title = "系统任务", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysOaTask sysOaTask)
    {

        SysOaTaskDistribute sysOaTaskDistribute=new SysOaTaskDistribute();
        sysOaTaskDistribute.setSysOaTask(sysOaTask);
        SyncEvent<SysOaTaskDistribute> syncEvent = new SyncEvent(sysOaTaskDistribute, DingTalkListenerType.WORK_RECORD_UPDATE);
        applicationContext.publishEvent(syncEvent);
        return toAjax(sysOaTaskService.updateSysOaTask(sysOaTask));
    }

    /**
     * 删除系统任务
     */
    @RequiresPermissions("system:task:remove")
    @Log(title = "系统任务", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysOaTaskService.deleteSysOaTaskByIds(ids));
    }

    /**
     * 任务详情
     * @param taskNo 任务编码
     * @param mmap
     * @return
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:task:detail")
    public String detail(@PathVariable("id") String taskNo, ModelMap mmap)
    {
        SysOaTask sysOaTask = sysOaTaskService.selectSysOaTaskById(taskNo);
        SysOaTaskDistribute sysOaTaskDistribute=new SysOaTaskDistribute();
        sysOaTaskDistribute.setTaskNo(taskNo);
        List<SysOaTaskDistribute> sysOaTaskDistributes = sysOaTaskDistributeService.selectSysOaTaskDistributeList(sysOaTaskDistribute);
        mmap.put("sysOaTask", sysOaTask);
        mmap.put("sysOaTaskDistributes", sysOaTaskDistributes);
        return prefix + "/detail";
    }


    /**
     * 我处理的详情
     * @param taskNo
     * @param mmap
     * @return
     */
    @GetMapping("/taskDistributedDetail/{id}")
    @RequiresPermissions("system:task:taskDistributedDetail")
    public String taskDistributedDetail(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysOaTaskDistribute sysOaTaskDistribute = sysOaTaskDistributeService.selectSysOaTaskDistributeById(id);
        warpSysOaTask(Lists.newArrayList(sysOaTaskDistribute));
        mmap.put("sysOaTask", sysOaTaskDistribute);
        return prefix + "/taskDistributedDetail";
    }
    /**
     * 任务分配详情
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/taskDistribute/detail/{id}")
    @RequiresPermissions("system:taskDistribute:detail")
    public String taskDistributeDetail(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysOaTaskDistribute sysOaTaskDistribute = sysOaTaskDistributeService.selectSysOaTaskDistributeById(id);
        warpSysOaTask(Lists.newArrayList(sysOaTaskDistribute));
        mmap.put("sysOaTask", sysOaTaskDistribute);
        return prefix + "/taskDistributeDetail";
    }

    private void  warpSysOaTask(List<SysOaTaskDistribute> sysOaTaskDistributes){
        sysOaTaskDistributes.forEach(t->{
            SysOaTask sysOaTask = sysOaTaskService.selectSysOaTaskById(t.getTaskNo());
            sysOaTask.setCreateBy(sysUserService.selectUserById(Long.parseLong(sysOaTask.getCreateBy())).getUserName());
            if(ObjectUtil.isNotNull(t.getTaskDistributeId())){
                t.setTaskDistributeId(sysUserService.selectUserById(Long.parseLong(t.getTaskDistributeId())).getUserName());
            }
            if(ObjectUtil.isNotNull(t.getTaskExecuteId())){
                t.setTaskExecuteId(sysUserService.selectUserById(Long.parseLong(t.getTaskExecuteId())).getUserName());
            }
            if(ObjectUtil.isNotNull(t.getTaskStartTime())&&ObjectUtil.isNotNull(t.getTaskCompleteTime())){
                t.setSpendTime(DateUtil.formatBetween(t.getTaskStartTime(),t.getTaskCompleteTime(), BetweenFormater.Level.SECOND));
            }
            t.setSysOaTask(sysOaTask);
            t.setCreateBy(sysUserService.selectUserById(Long.parseLong(t.getCreateBy())).getUserName());
        });
    }
}
