package com.snow.web.controller.flowable;

import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.PageModel;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.exception.BusinessException;
import com.snow.common.utils.StringUtils;
import com.snow.flowable.domain.*;
import com.snow.flowable.service.impl.FlowableServiceImpl;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysOaLeave;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysOaLeaveService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.history.HistoricProcessInstance;
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
public class FlowController extends BaseController {
    private String prefix = "flow";

    @Autowired
    private ISysOaLeaveService sysOaLeaveService;
    @Autowired
    private FlowableServiceImpl flowableService;


    /**
     * 跳转完成任务界面
     * @return
     */
    //@RequiresPermissions("modeler:flow:view")
    @GetMapping("/toFinishTask")
    public String toFinishTask(String businessKey,String taskId,ModelMap mmap)
    {
        Task task =  flowableService.getTask(taskId);
        HistoricProcessInstance historicProcessInstance= flowableService.getHistoricProcessInstanceById(task.getProcessInstanceId());
        String processDefinitionKey = historicProcessInstance.getProcessDefinitionKey();
        if(processDefinitionKey.equals("snow_oa_leave")){
            SysOaLeave sysOaLeave=new SysOaLeave();
            sysOaLeave.setLeaveNo(businessKey);
            List<SysOaLeave> sysOaLeaves = sysOaLeaveService.selectSysOaLeaveList(sysOaLeave);
            if(StringUtils.isEmpty(sysOaLeaves)){
                throw new BusinessException("跳转请假申请页面异常");
            }
            mmap.put("sysOaLeave", sysOaLeaves.get(0));
            mmap.put("taskId", taskId);
        }
        return task.getFormKey();
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
        return getFlowDataTable(historicProcessInstance);
    }

    @RequiresPermissions("flow:process:getMyTakePartInProcess")
    @GetMapping("/toMyTakePartInProcess")
    public String getMyTakePartInProcess()
    {

        return prefix+"/myTakePartInProcess";
    }
    /**
     * 获取我的流程实例
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
