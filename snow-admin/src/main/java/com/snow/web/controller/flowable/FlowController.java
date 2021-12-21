package com.snow.web.controller.flowable;

import cn.hutool.core.util.ReflectUtil;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String toFinishTask(String taskId,ModelMap mmap) {
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
    public AjaxResult finishTask(FinishTaskDTO finishTaskDTO) {
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
    public String todoTask() {

        return prefix+"/myTask";
    }

    /**
     * 获取我的待办列表
     */
    @RequiresPermissions("flow:get:todoList")
    @RequestMapping("/findTasksByUserId")
    @ResponseBody
    public TableDataInfo findTasksByUserId(TaskBaseDTO taskBaseDTO) {
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
    public String getMyHistoricProcessInstance() {

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
        return getFlowDataTable(historicProcessInstance);
    }



    /**
     * 跳转我发起的流程详情
     */
    @GetMapping("/myStartProcessDetail")
    @RequiresPermissions("system:flow:myStartProcessDetail")
    public String myStartProcessDetail(String processInstanceId,ModelMap modelMap) {
        ProcessInstanceVO processInstanceVo = flowableService.getProcessInstanceVoById(processInstanceId);
        //已审批的任务
        HistoricTaskInstanceDTO historicTaskInstanceDTO=new HistoricTaskInstanceDTO();
        historicTaskInstanceDTO.setProcessInstanceId(processInstanceId);
        List<HistoricTaskInstanceVO> historicTaskInstanceList= flowableTaskService.getHistoricTaskInstanceNoPage(historicTaskInstanceDTO);
        //获取业务数据
        AppForm appFrom = appFormService.getAppFrom(processInstanceId);
        modelMap.put("historicTaskInstanceList",historicTaskInstanceList);
        modelMap.put("processInstanceId",processInstanceId);
        modelMap.put("processInstance",processInstanceVo);
        modelMap.put("busVarUrl",appFrom.getBusVarUrl());
        modelMap.put("appId",ReflectUtil.getFieldValue(appFrom,"id"));
        return prefix +"/myStartProcessDetail";
    }

    /**
     * 跳转我的已办
     * @return
     */
    @RequiresPermissions("flow:process:getMyTakePartInTask")
    @GetMapping("/toMyTakePartInTask")
    public String getMyTakePartInProcess() {

        return prefix+"/my-taked";
    }

    /**
     * 获取我办结的任务列表
     * @param historicTaskInstanceDTO
     * @return
     */
    @RequiresPermissions("flow:process:getMyTakePartInTask")
    @PostMapping("/getMyTakePartInTask")
    @ResponseBody
    public TableDataInfo getMyTakePartInProcess(HistoricTaskInstanceDTO historicTaskInstanceDTO){
        SysUser sysUser = ShiroUtils.getSysUser();
        historicTaskInstanceDTO.setUserId(String.valueOf(sysUser.getUserId()));
        PageModel<HistoricTaskInstanceVO> historicTaskInstance = flowableTaskService.getHistoricTaskInstance(historicTaskInstanceDTO);
        return getFlowDataTable(historicTaskInstance);
    }

    /**
     * 跳转我的已办详情
     * @param taskId 任务id
     * @param modelMap
     * @return
     */
    @RequiresPermissions("flow:process:myTaskedDetail")
    @GetMapping("/getMyTaskedDetail")
    public String getMyTaskedDetail(String taskId,ModelMap modelMap){
        //获取任务实例
        HistoricTaskInstanceVO hisTask = flowableTaskService.getHisTask(taskId);
        //获取业务数据
        AppForm appFrom = appFormService.getAppFrom(hisTask.getProcessInstanceId());
        //获取流程实例
        ProcessInstanceVO processInstanceVo = flowableService.getProcessInstanceVoById(hisTask.getProcessInstanceId());
        modelMap.put("hisTask",hisTask);
        modelMap.put("appFrom",appFrom);
        modelMap.put("processInstance",processInstanceVo);
        modelMap.put("busVarUrl",appFrom.getBusVarUrl());
        modelMap.put("appId",ReflectUtil.getFieldValue(appFrom,"id"));
        return prefix+"/my-task-detail";
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
    public String selectUser(String taskId,Integer flag,ModelMap mmap) {
        mmap.put("taskId",taskId);
        mmap.put("flag",flag);
        return prefix + "/selectUser";
    }


    @PostMapping("/activeProcessInstance")
    @RequiresPermissions("flow:process:activeProcessInstance")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult activeProcessInstance(String id) {
        flowableService.suspendOrActiveProcessInstance(id,FlowInstanceEnum.ACTIVATE.getCode());
        return AjaxResult.success();
    }

    @PostMapping("/suspendProcessInstance")
    @RequiresPermissions("flow:process:suspendProcessInstance")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult suspendProcessInstance(String id) {
        flowableService.suspendOrActiveProcessInstance(id,FlowInstanceEnum.SUSPEND.getCode());
        return AjaxResult.success();
    }


    /**
     * 跳转任务详情界面
     * @return 跳转的页面
     */
    @GetMapping("/toTaskDetail")
    public String toTaskDetail(String taskId,ModelMap mmap) {
        TaskVO task =  flowableTaskService.getHisTask(taskId);
        //获取业务参数
        AppForm appFrom = appFormService.getAppFrom(task.getProcessInstanceId());
        mmap.put("appFrom", appFrom);
        mmap.put("taskId", taskId);
        mmap.put("processInstanceId", task.getProcessInstanceId());
        return task.getFormKey();
    }

    /**
     * 取消流程
     * @param id 流程实例id
     * @param reason 理由
     * @return
     */
    @PostMapping("/cancelProcessInstance")
    @RequiresPermissions("flow:process:cancelProcessInstance")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult cancelProcessInstanceFlag(String id,String reason) {
        flowableService.cancelProcessInstance(id,reason);
        return AjaxResult.success();
    }

}
