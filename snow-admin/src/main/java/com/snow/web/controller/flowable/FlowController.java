package com.snow.web.controller.flowable;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.PageModel;
import com.snow.common.core.page.TableDataInfo;
import com.snow.flowable.common.enums.FlowInstanceEnum;
import com.snow.flowable.domain.*;
import com.snow.flowable.service.AppFormService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.flowable.service.impl.FlowableServiceImpl;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-23 21:17
 **/
@Controller
@RequestMapping("/flow")
@Slf4j
public class FlowController extends BaseController {
    private String prefix = "flow";

    @Autowired
    private FlowableServiceImpl flowableService;

    @Autowired
    private AppFormService appFormService;

    @Autowired
    private FlowableTaskService flowableTaskService;

    /**
     * 跳转完成任务界面
     * @return
     */
    @GetMapping("/toFinishTask")
    public String toFinishTask(String taskId,ModelMap mmap)
    {
        Task task =  flowableTaskService.getTask(taskId);
        //获取业务参数
        AppForm appFrom = appFormService.getAppFrom(task.getProcessInstanceId());
        mmap.put("appFrom", appFrom);
        mmap.put("taskId", taskId);
        mmap.put("processInstanceId", task.getProcessInstanceId());
        return task.getFormKey();
    }

    /**
     * 完成任务
     * @return
     */
    @PostMapping("/finishTask")
    @RequiresPermissions("system:flow:finishTask")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult finishTask(FinishTaskDTO finishTaskDTO)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        finishTaskDTO.setUserId(String.valueOf(sysUser.getUserId()));
        flowableTaskService.submitTask(finishTaskDTO);
        return AjaxResult.success();
    }

    /**
     * 跳转待办页
     * @return
     */
    @RequiresPermissions("flow:get:todoList")
    @GetMapping("/toDoMyTask")
    public String todoTask()
    {

        return prefix+"/myTask";
    }

    /**
     * 获取我的待办列表
     */
    @RequiresPermissions("flow:get:todoList")
    @RequestMapping("/findTasksByUserId")
    @ResponseBody
    public TableDataInfo findTasksByUserId(TaskBaseDTO taskBaseDTO)
    {
        Long userId = ShiroUtils.getUserId();
        PageModel<TaskVO> taskList = flowableTaskService.findTasksByUserId(String.valueOf(userId), taskBaseDTO);
        return getFlowDataTable(taskList);
    }

    /**
     * 获取所有节点
     * @param processInstanceId
     * @return
     */
    @GetMapping("/getDynamicFlowNodeInfo")
    @ResponseBody
    public AjaxResult getDynamicFlowNodeInfo(String processInstanceId){
        List<TaskVO> dynamicFlowNodeInfo = flowableService.getDynamicFlowNodeInfo(processInstanceId);
        return AjaxResult.success(dynamicFlowNodeInfo);
    }

    /**
     * 跳转我发起的流程
     * @return
     */
    @RequiresPermissions("flow:get:getMyStartProcess")
    @GetMapping("/toMyStartProcess")
    public String getMyHistoricProcessInstance()
    {

        return prefix+"/myStartProcess";
    }

    /**
     * 获取我的流程实例
     * @param processInstanceDTO
     * @return
     */
    @RequiresPermissions("flow:process:getMyStartProcess")
    @PostMapping("/getMyHistoricProcessInstance")
    @ResponseBody
    public TableDataInfo getMyHistoricProcessInstance(ProcessInstanceDTO processInstanceDTO){
        SysUser sysUser = ShiroUtils.getSysUser();
        processInstanceDTO.setStartedUserId(String.valueOf(sysUser.getUserId()));
        PageModel<ProcessInstanceVO> historicProcessInstance = flowableService.getHistoricProcessInstance(processInstanceDTO);
        log.info(JSON.toJSONString(historicProcessInstance.getPagedRecords()));
        return getFlowDataTable(historicProcessInstance);
    }



    /**
     * 跳转我发起的流程详情
     */
    @GetMapping("/myStartProcessDetail")
    @RequiresPermissions("system:flow:myStartProcessDetail")
    public String myStartProcessDetail(String processInstanceId,ModelMap modelMap)
    {
        //已审批的
        HistoricTaskInstanceDTO historicTaskInstanceDTO=new HistoricTaskInstanceDTO();
        historicTaskInstanceDTO.setProcessInstanceId(processInstanceId);
        //historicTaskInstanceDTO.setProcessStatus(1);
        List<HistoricTaskInstanceVO> historicTaskInstanceList= flowableService.getHistoricTaskInstanceNoPage(historicTaskInstanceDTO);
        AppForm appFrom = appFormService.getAppFrom(processInstanceId);
        modelMap.put("historicTaskInstanceList",historicTaskInstanceList);
        modelMap.put("processInstanceId",processInstanceId);
        modelMap.put("busVarUrl",appFrom.getBusVarUrl());
        modelMap.put("appId",ReflectUtil.getFieldValue(appFrom,"id"));
        return prefix +"/myStartProcessDetail";
    }

    /**
     * 跳转我的已办
     * @return
     */
    @RequiresPermissions("flow:process:getMyTakePartInProcess")
    @GetMapping("/toMyTakePartInProcess")
    public String getMyTakePartInProcess()
    {

        return prefix+"/myTakePartInProcess";
    }

    /**
     * 获取我办结的任务列表
     * @param historicTaskInstanceDTO
     * @return
     */
    @RequiresPermissions("flow:process:getMyTakePartInProcess")
    @PostMapping("/getMyTakePartInProcess")
    @ResponseBody
    public TableDataInfo getMyTakePartInProcess(HistoricTaskInstanceDTO historicTaskInstanceDTO){
        SysUser sysUser = ShiroUtils.getSysUser();
        historicTaskInstanceDTO.setUserId(String.valueOf(sysUser.getUserId()));
        PageModel<HistoricTaskInstanceVO> historicTaskInstance = flowableService.getHistoricTaskInstance(historicTaskInstanceDTO);
        return getFlowDataTable(historicTaskInstance);
    }

    /**
     * 转办任务
     * @return
     */
    @PostMapping("/transferTask")
    @RequiresPermissions("flow:process:transferTask")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult transferTask(TransferTaskDTO transferTaskDTO) {
        SysUser sysUser = ShiroUtils.getSysUser();
        flowableTaskService.transferTask(transferTaskDTO.getTaskId(),String.valueOf(sysUser.getUserId()),transferTaskDTO.getTargetUserId());
        return AjaxResult.success();
    }

    /**
     * 委派任务
     * @return
     */
    @PostMapping("/delegateTask")
    @RequiresPermissions("flow:process:delegateTask")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult delegateTask(TransferTaskDTO transferTaskDTO) {
        SysUser sysUser = ShiroUtils.getSysUser();
        flowableTaskService.delegateTask(transferTaskDTO.getTaskId(),String.valueOf(sysUser.getUserId()),transferTaskDTO.getTargetUserId());
        return AjaxResult.success();
    }


    /**
     * 选择用户
     */
    @GetMapping("/selectUser")
    public String selectUser(String taskId,Integer flag,ModelMap mmap)
    {
        mmap.put("taskId",taskId);
        mmap.put("flag",flag);
        return prefix + "/selectUser";
    }


    @PostMapping("/activeProcessInstance")
    @RequiresPermissions("flow:process:activeProcessInstance")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult activeProcessInstance(String id)
    {
        flowableService.suspendOrActiveProcessInstance(id,FlowInstanceEnum.ACTIVATE.getCode());
        return AjaxResult.success();
    }

    @PostMapping("/suspendProcessInstance")
    @RequiresPermissions("flow:process:suspendProcessInstance")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult suspendProcessInstance(String id)
    {
        flowableService.suspendOrActiveProcessInstance(id,FlowInstanceEnum.SUSPEND.getCode());
        return AjaxResult.success();
    }

}
