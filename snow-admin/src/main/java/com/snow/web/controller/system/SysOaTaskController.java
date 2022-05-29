package com.snow.web.controller.system;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.snow.common.annotation.Log;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.constant.MessageConstants;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.domain.MessageEventRequest;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.enums.DingFlowTaskType;
import com.snow.common.enums.MessageEventType;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.dingtalk.service.WorkRecodeService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysOaTask;
import com.snow.system.domain.SysOaTaskDistribute;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysOaTaskDistributeService;
import com.snow.system.service.ISysOaTaskService;
import org.apache.commons.compress.utils.Sets;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统任务Controller
 * 当任务被分配后，判断是否同步到钉钉系统
 * @author 没用的阿吉
 * @date 2021-07-29
 */
@Controller
@RequestMapping("/system/task")
public class SysOaTaskController extends BaseController {
    private String prefix = "system/task";

    @Autowired
    private ISysOaTaskService sysOaTaskService;

    @Autowired
    private ISysOaTaskDistributeService sysOaTaskDistributeService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WorkRecodeService workRecodeService;

    @RequiresPermissions("system:task:view")
    @GetMapping()
    public String task()
    {
        return prefix + "/task";
    }


    /**
     * 任务分配列表
     */
    @RequiresPermissions("system:task:taskDistributeList")
    @PostMapping("/taskDistributeList")
    @ResponseBody
    public TableDataInfo taskDistributeList(SysOaTaskDistribute sysOaTaskDistribute) {
        startPage();
        List<SysOaTaskDistribute> list = sysOaTaskDistributeService.selectSysOaTaskDistributeList(sysOaTaskDistribute);
        if(CollUtil.isEmpty(list)){
            return getDataTable(list);
        }
        sysOaTaskDistributeService.warpSysOaTask(list);
        return getDataTable(list);
    }
    /**
     * 查询我发起系统任务列表
     */
    @RequiresPermissions("system:task:myList")
    @PostMapping("/myList")
    @ResponseBody
    public TableDataInfo myList(SysOaTask sysOaTask) {
        startPage();
        sysOaTask.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        List<SysOaTask> list = sysOaTaskService.selectSysOaTaskList(sysOaTask);
        return getDataTable(list);
    }

    /**
     * 我的待处理任务
     */
    @RequiresPermissions("system:task:waitList")
    @PostMapping("/waitList")
    @ResponseBody
    public TableDataInfo waitList(SysOaTaskDistribute sysOaTaskDistribute) {
        startPage();
        sysOaTaskDistribute.setTaskDistributeId(String.valueOf(ShiroUtils.getUserId()));
        sysOaTaskDistribute.setTaskExecuteStatusList(Lists.newArrayList(DingFlowTaskType.NEW.getCode(),DingFlowTaskType.RUNNING.getCode()));
        return getDataTable(sysOaTaskDistributeService.getSysOaTaskDistribute(sysOaTaskDistribute));
    }

    /**
     * 我处理的任务
     */
    @RequiresPermissions("system:task:handleList")
    @PostMapping("/handleList")
    @ResponseBody
    public TableDataInfo handleList(SysOaTaskDistribute sysOaTaskDistribute) {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaTaskDistribute.setTaskExecuteId(String.valueOf(sysUser.getUserId()));
        sysOaTaskDistribute.setTaskExecuteStatus(DingFlowTaskType.COMPLETED.getCode());
        List<SysOaTaskDistribute> sysOaTaskDistributes = sysOaTaskDistributeService.getSysOaTaskDistribute(sysOaTaskDistribute);
        return getDataTable(sysOaTaskDistributes);
    }

    /**
     * 导出系统任务列表
     */
    @RequiresPermissions("system:task:export")
    @Log(title = "系统任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOaTask sysOaTask) {
        List<SysOaTask> list = sysOaTaskService.selectSysOaTaskList(sysOaTask);
        ExcelUtil<SysOaTask> util = new ExcelUtil<SysOaTask>(SysOaTask.class);

        return util.exportExcel(list, "task","系统任务");
    }

    /**
     * 新增系统任务
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存系统任务
     */
    @RequiresPermissions("system:task:add")
    @Log(title = "系统任务", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @RepeatSubmit
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSave(SysOaTask sysOaTask) {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaTask.setCreateBy(String.valueOf(sysUser.getUserId()));
        SysOaTask task= sysOaTaskService.insertSysOaTask(sysOaTask);
        //是否同步到钉钉待办,然后把钉钉taskId反写表中
        if(sysOaTask.getSyncDingtalk()==1){
            String taskId = workRecodeService.createTodoTask(task);
            SysOaTask updateTask=new SysOaTask();
            updateTask.setTaskNo(task.getTaskNo());
            updateTask.setTaskOutsideId(taskId);
            sysOaTaskService.updateById(updateTask);
        }
        return AjaxResult.success();
    }

    @RequiresPermissions("system:task:start")
    @PostMapping("/start")
    @ResponseBody
    public AjaxResult start(Long id) {
        SysOaTaskDistribute taskDistribute = sysOaTaskDistributeService.getById(id);
        SysOaTask task = sysOaTaskService.getById(taskDistribute.getTaskNo());
        SysOaTaskDistribute sysOaTaskDistribute =new SysOaTaskDistribute();
        sysOaTaskDistribute.setId(id);
        sysOaTaskDistribute.setTaskStartTime(new Date());
        sysOaTaskDistribute.setTaskExecuteStatus(DingFlowTaskType.RUNNING.getCode());
        if(task.getSyncDingtalk()==1){
            workRecodeService.updateTodoTaskExecutorStatus(ShiroUtils.getUserId(),sysOaTaskService.getOutTaskOutsideId(task.getTaskNo()),false);
        }
        return toAjax(sysOaTaskDistributeService.updateById(sysOaTaskDistribute));
    }
    /**
     * 处理系统任务
     */
    @GetMapping("/handle/{id}")
    public String handle(@PathVariable("id") Long id, ModelMap mmap) {
        SysOaTaskDistribute sysOaTaskDistribute = sysOaTaskDistributeService.selectSysOaTaskDistributeById(id);
        sysOaTaskDistributeService.warpSysOaTask(Lists.newArrayList(sysOaTaskDistribute));
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
    public AjaxResult handleSave(SysOaTaskDistribute sysOaTaskDistribute) {
        SysOaTaskDistribute taskDistribute = sysOaTaskDistributeService.getById(sysOaTaskDistribute.getId());
        SysOaTask task = sysOaTaskService.getById(taskDistribute.getTaskNo());
        sysOaTaskDistribute.setTaskCompleteTime(new Date());
        sysOaTaskDistribute.setTaskExecuteId(String.valueOf(ShiroUtils.getUserId()));
        sysOaTaskDistribute.setTaskExecuteStatus(DingFlowTaskType.COMPLETED.getCode());
        //发送站内信
        sendInnerMessage(sysOaTaskDistribute);
        //更新钉钉任务状态
        if(task.getSyncDingtalk()==1){
            workRecodeService.updateTodoTaskExecutorStatus(ShiroUtils.getUserId(),task.getTaskOutsideId(),true);
        }
        return toAjax(sysOaTaskDistributeService.updateById(sysOaTaskDistribute));
    }


    /**
     * 修改系统任务
     */
    @GetMapping("/edit/{taskNo}")
    public String edit(@PathVariable("taskNo") String taskNo, ModelMap mmap) {
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
    public AjaxResult editSave(SysOaTask sysOaTask) {
        LambdaQueryWrapper<SysOaTaskDistribute> eq = new QueryWrapper<SysOaTaskDistribute>().lambda().ne(SysOaTaskDistribute::getTaskExecuteStatus, DingFlowTaskType.NEW.getCode())
                .eq(SysOaTaskDistribute::getTaskNo, sysOaTask.getTaskNo());
        if(CollUtil.isNotEmpty(sysOaTaskDistributeService.list(eq))){
            return AjaxResult.error("只有待执行任务，才能编辑任务");
        }
        sysOaTask.setUpdateBy(ShiroUtils.getUserId().toString());
        if(sysOaTask.getSyncDingtalk()==1){
            workRecodeService.updateTodoTask(sysOaTask);
        }
        return toAjax(sysOaTaskService.updateById(sysOaTask));
    }



    /**
     * 任务详情
     * @param taskNo 任务编码
     * @param mmap
     * @return
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:task:detail")
    public String detail(@PathVariable("id") String taskNo, ModelMap mmap) {
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
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/taskDistributedDetail/{id}")
    @RequiresPermissions("system:task:taskDistributedDetail")
    public String taskDistributedDetail(@PathVariable("id") Long id, ModelMap mmap) {
        SysOaTaskDistribute sysOaTaskDistribute = sysOaTaskDistributeService.selectSysOaTaskDistributeById(id);
        sysOaTaskDistributeService.warpSysOaTask(Lists.newArrayList(sysOaTaskDistribute));
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
    public String taskDistributeDetail(@PathVariable("id") Long id, ModelMap mmap) {
        SysOaTaskDistribute sysOaTaskDistribute = sysOaTaskDistributeService.selectSysOaTaskDistributeById(id);
        sysOaTaskDistributeService.warpSysOaTask(Lists.newArrayList(sysOaTaskDistribute));
        mmap.put("sysOaTask", sysOaTaskDistribute);
        return prefix + "/taskDistributeDetail";
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

}
