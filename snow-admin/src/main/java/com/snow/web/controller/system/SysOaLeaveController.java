package com.snow.web.controller.system;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.snow.common.constant.SequenceContants;
import com.snow.common.enums.ProcessStatus;
import com.snow.common.utils.StringUtils;
import com.snow.flowable.common.FlowConstants;
import com.snow.flowable.domain.CompleteTaskDTO;
import com.snow.flowable.domain.FileEntry;
import com.snow.flowable.domain.FinishTaskDTO;
import com.snow.flowable.domain.StartProcessDTO;
import com.snow.flowable.service.impl.FlowableServiceImpl;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysSequenceService;
import com.snow.system.service.impl.SysOaLeaveServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.snow.common.annotation.Log;
import com.snow.common.enums.BusinessType;
import com.snow.system.domain.SysOaLeave;
import com.snow.system.service.ISysOaLeaveService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 请假单Controller
 * 
 * @author snow
 * @date 2020-11-22
 */
@Controller
@RequestMapping("/system/leave")
public class SysOaLeaveController extends BaseController
{
    private String prefix = "system/leave";

    @Autowired
    private ISysOaLeaveService sysOaLeaveService;

    @Autowired
    private FlowableServiceImpl flowableService;
    @Autowired
    private ISysSequenceService sequenceService;


    @RequiresPermissions("system:leave:view")
    @GetMapping()
    public String leave()
    {
        return prefix + "/leave";
    }

    /**
     * 查询请假单列表
     */
    @RequiresPermissions("system:leave:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysOaLeave sysOaLeave)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaLeave.setCreateBy(sysUser.getUserName());
        List<SysOaLeave> list = sysOaLeaveService.selectSysOaLeaveList(sysOaLeave);
        return getDataTable(list);
    }

    /**
     * 导出请假单列表
     */
    @RequiresPermissions("system:leave:export")
    @Log(title = "请假单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOaLeave sysOaLeave)
    {
        List<SysOaLeave> list = sysOaLeaveService.selectSysOaLeaveList(sysOaLeave);
        ExcelUtil<SysOaLeave> util = new ExcelUtil<SysOaLeave>(SysOaLeave.class);
        return util.exportExcel(list, "leave");
    }

    /**
     * 新增请假单
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存请假单
     */
    @RequiresPermissions("system:leave:add")
    @Log(title = "请假单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSave(SysOaLeave sysOaLeave)

    {
        SysUser sysUser = ShiroUtils.getSysUser();
        String leaveNo = sequenceService.getNewSequenceNo(SequenceContants.OA_LEAVE_SEQUENCE);
        sysOaLeave.setCreateBy(sysUser.getUserName());
        sysOaLeave.setLeaveNo(leaveNo);
        int i = sysOaLeaveService.insertSysOaLeave(sysOaLeave);
        return toAjax(i);
    }



    /**
     * 修改请假单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SysOaLeave sysOaLeave = sysOaLeaveService.selectSysOaLeaveById(id);
        mmap.put("sysOaLeave", sysOaLeave);
        return prefix + "/edit";
    }

    /**
     * 修改保存并发起请假单
     */
    @RequiresPermissions("system:leave:edit")
    @Log(title = "请假单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysOaLeave sysOaLeave)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        SysOaLeave oldSysOaLeave = sysOaLeaveService.selectSysOaLeaveById(sysOaLeave.getId());
        //发起审批
        StartProcessDTO startProcessDTO=new StartProcessDTO();
        startProcessDTO.setBusinessKey(oldSysOaLeave.getLeaveNo());
        startProcessDTO.setProcessDefinitionKey(FlowConstants.SNOW_OA_LEAVE);
        startProcessDTO.setStartUserId(String.valueOf(sysUser.getUserId()));
        ProcessInstance processInstance = flowableService.startProcessInstanceByKey(startProcessDTO);
        CompleteTaskDTO completeTaskDTO=new CompleteTaskDTO();
        completeTaskDTO.setUserId(String.valueOf(sysUser.getUserId()));
        Task task= flowableService.getTaskProcessInstanceById(processInstance.getProcessInstanceId());
        completeTaskDTO.setTaskId(task.getId());
        flowableService.completeTask(completeTaskDTO);
        sysOaLeave.setProcessStatus(ProcessStatus.CHECKING.ordinal());
        sysOaLeave.setCreateBy(sysUser.getUserName());
        sysOaLeave.setApplyPerson(sysUser.getUserName());
        BeanUtils.copyProperties(sysOaLeave,oldSysOaLeave);
        sysOaLeave.setProcessInstanceId(processInstance.getProcessInstanceId());
        return toAjax(sysOaLeaveService.updateSysOaLeave(sysOaLeave));
    }

    /**
     * 删除请假单
     */
    @RequiresPermissions("system:leave:remove")
    @Log(title = "请假单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysOaLeaveService.deleteSysOaLeaveByIds(ids));
    }

    @Log(title = "主管完成审批", businessType = BusinessType.OTHER)
    @PostMapping("/managerFinishTask")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult managerFinishTask(FinishTaskDTO finishTaskDTO)

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
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("hr",2);
        completeTaskDTO.setParamMap(paramMap);
        if(checkStatus==0){
            completeTaskDTO.setIsPass(true);
        }else {
            completeTaskDTO.setIsPass(false);
        }
        flowableService.completeTask(completeTaskDTO);
        return AjaxResult.success();
    }

    /**
     * hr完成审批
     */
    //@RequiresPermissions("system:leave:startLeaveProcess")
    @Log(title = "hr完成审批", businessType = BusinessType.OTHER)
    @PostMapping("/hrFinishTask")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult hrFinishTask(FinishTaskDTO finishTaskDTO)
    {
        SysOaLeave sysOaLeave=new SysOaLeave();
        sysOaLeave.setLeaveNo(finishTaskDTO.getBusinessKey());
        List<SysOaLeave> sysOaLeaves = sysOaLeaveService.selectSysOaLeaveList(sysOaLeave);
        if(StringUtils.isEmpty(sysOaLeaves)){
            return AjaxResult.error("该业务参数不存在");
        }
        SysUser sysUser = ShiroUtils.getSysUser();
        CompleteTaskDTO completeTaskDTO=new CompleteTaskDTO();
        completeTaskDTO.setUserId(String.valueOf(sysUser.getUserId()));
        completeTaskDTO.setTaskId(finishTaskDTO.getTaskId());
        Integer checkStatus = finishTaskDTO.getCheckStatus();
        if(checkStatus==0){
            completeTaskDTO.setIsPass(true);
            sysOaLeave.setProcessStatus(ProcessStatus.PASS.ordinal());
        }else {
            completeTaskDTO.setIsPass(false);
            sysOaLeave.setProcessStatus(ProcessStatus.REJECT.ordinal());
        }
        completeTaskDTO.setComment(finishTaskDTO.getSuggestion());
        flowableService.completeTask(completeTaskDTO);
        sysOaLeave.setUpdateBy(sysUser.getUserName());
        int i = sysOaLeaveService.updateSysOaLeaveByLeaveNo(sysOaLeave);
        return toAjax(i);
    }
}
