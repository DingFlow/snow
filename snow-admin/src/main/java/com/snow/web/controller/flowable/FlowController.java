package com.snow.web.controller.flowable;

import com.alibaba.fastjson.JSON;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.PageModel;
import com.snow.common.core.page.TableDataInfo;
import com.snow.flowable.domain.*;
import com.snow.flowable.service.AppFormService;
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


    /**
     * 跳转完成任务界面
     * @return
     */
    @GetMapping("/toFinishTask")
    public String toFinishTask(String taskId,ModelMap mmap)
    {
        Task task =  flowableService.getTask(taskId);
        //获取业务参数
        AppForm appFrom = appFormService.getAppFrom(task.getProcessInstanceId());
        mmap.put("appFrom", appFrom);
        mmap.put("taskId", taskId);
        return task.getFormKey();
    }

    /**
     * 完成任务
     * @return
     */
    @PostMapping("/finishTask")
    @ResponseBody
    public AjaxResult finishTask(CompleteTaskDTO completeTaskDTO)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        completeTaskDTO.setUserId(String.valueOf(sysUser.getUserId()));
        flowableService.completeTask(completeTaskDTO);
        return AjaxResult.success();
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
        AppForm appFrom = appFormService.getAppFromBySerializable(processInstanceId);
        modelMap.put("historicTaskInstanceList",historicTaskInstanceList);
        modelMap.put("processInstanceId",processInstanceId);
        modelMap.put("busVarUrl",appFrom.getBusVarUrl());
        return prefix +"/myStartProcessDetail";
    }

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
}
