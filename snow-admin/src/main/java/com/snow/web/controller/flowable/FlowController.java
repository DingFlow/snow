package com.snow.web.controller.flowable;

import com.google.common.collect.Lists;
import com.snow.common.annotation.Log;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.enums.BusinessType;
import com.snow.common.exception.BusinessException;
import com.snow.common.utils.StringUtils;
import com.snow.flowable.domain.CompleteTaskDTO;
import com.snow.flowable.domain.FileEntry;
import com.snow.flowable.domain.FinishTaskDTO;
import com.snow.flowable.service.impl.FlowableServiceImpl;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysOaLeave;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysOaLeaveService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
public class FlowController {
    private String prefix = "flow";

    @Autowired
    private ISysOaLeaveService sysOaLeaveService;
    @Autowired
    private FlowableServiceImpl flowableService;


    /**
     * 跳转流程编译器
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
            return "system/leave/finishTask";
        }
        return "";
    }

    /**
     * 完成任务
     */
    //@RequiresPermissions("system:leave:startLeaveProcess")
    @Log(title = "完成审批", businessType = BusinessType.INSERT)
    @PostMapping("/finishTask")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult finishTask(FinishTaskDTO finishTaskDTO)

    {
        SysUser sysUser = ShiroUtils.getSysUser();

        List<FileEntry> files=Lists.newArrayList();
        FileEntry fileEntry=new FileEntry();
        fileEntry.setName("请假申请");
        fileEntry.setUrl(finishTaskDTO.getSuggestionFileUrl());
        files.add(fileEntry);
        CompleteTaskDTO completeTaskDTO=new CompleteTaskDTO();
        completeTaskDTO.setTaskId(finishTaskDTO.getTaskId());
        completeTaskDTO.setUserId(String.valueOf(sysUser.getUserId()));
        completeTaskDTO.setFiles(files);
        completeTaskDTO.setComment(finishTaskDTO.getSuggestion());
        Integer checkStatus = finishTaskDTO.getCheckStatus();
        if(checkStatus==0){
            completeTaskDTO.setIsPass(true);
        }else {
            completeTaskDTO.setIsPass(false);
        }
        flowableService.completeTask(completeTaskDTO);
        return AjaxResult.success();
    }
}
