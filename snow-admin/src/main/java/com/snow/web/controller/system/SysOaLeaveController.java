package com.snow.web.controller.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.snow.common.constant.SequenceContants;
import com.snow.common.enums.WorkRecordStatus;
import com.snow.common.utils.StringUtils;
import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.domain.*;
import com.snow.flowable.domain.leave.LeaveFinishTaskDTO;
import com.snow.flowable.domain.leave.LeaveRestartTaskDTO;
import com.snow.flowable.domain.leave.SysOaLeaveForm;
import com.snow.flowable.service.AppFormService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.flowable.service.impl.FlowableServiceImpl;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysSequenceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private FlowableTaskService flowableTaskService;


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
        //管理员权限判断
        if(!ShiroUtils.getSysUser().isAdmin()){
            sysOaLeave.setCreateBy(String.valueOf(sysUser.getUserId()));
        }
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
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaLeave.setCreateBy(sysUser.getUserName());
        //管理员权限判断
        if(!ShiroUtils.getSysUser().isAdmin()){
            sysOaLeave.setCreateBy(String.valueOf(sysUser.getUserId()));
        }
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
     * 1.填写请假单 可修改
     */
    @RequiresPermissions("system:leave:add")
    @Log(title = "请假单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSave(SysOaLeave sysOaLeave)

    {

        SysUser sysUser = ShiroUtils.getSysUser();
        Date endTime = sysOaLeave.getEndTime();
        Date startTime = sysOaLeave.getStartTime();
        int compare = DateUtil.compare(startTime, endTime);
        if(compare==1||compare==0){
            return AjaxResult.error("请假结束时间必须大于开始时间");
        }
        //生成请假单
        String leaveNo = sequenceService.getNewSequenceNo(SequenceContants.OA_LEAVE_SEQUENCE);
        sysOaLeave.setCreateBy(String.valueOf(sysUser.getUserId()));
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
        String spendTime = DateUtil.formatBetween(sysOaLeave.getStartTime(), sysOaLeave.getEndTime(), BetweenFormater.Level.SECOND);
        sysOaLeave.setLeaveTime(spendTime);
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

        sysOaLeave.setApplyPerson(sysUser.getUserName());
        int i = sysOaLeaveService.updateSysOaLeave(sysOaLeave);
        SysOaLeave newSysOaLeave = sysOaLeaveService.selectSysOaLeaveById(sysOaLeave.getId());
        //发起审批
        SysOaLeaveForm sysOaLeaveForm=new SysOaLeaveForm();
        BeanUtils.copyProperties(sysOaLeave,sysOaLeaveForm);
        sysOaLeaveForm.setBusinessKey(newSysOaLeave.getLeaveNo());
        sysOaLeaveForm.setStartUserId(String.valueOf(sysUser.getUserId()));
        sysOaLeaveForm.setBusVarUrl("/system/leave/detail");
        ProcessInstance processInstance = flowableService.startProcessInstanceByAppForm(sysOaLeaveForm);
        //提交
        CompleteTaskDTO completeTaskDTO=new CompleteTaskDTO();
        completeTaskDTO.setUserId(String.valueOf(sysUser.getUserId()));
        Task task= flowableService.getTaskProcessInstanceById(processInstance.getProcessInstanceId());
        completeTaskDTO.setTaskId(task.getId());
        completeTaskDTO.setIsPass(true);
        flowableService.completeTask(completeTaskDTO);
        return toAjax(i);
    }

    /**
     * 请假单详情
     */
    @RequiresPermissions("system:leave:detail")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SysOaLeave sysOaLeave = sysOaLeaveService.selectSysOaLeaveById(id);
        HistoricTaskInstanceDTO historicTaskInstanceDTO=new HistoricTaskInstanceDTO();
        historicTaskInstanceDTO.setBusinessKey(sysOaLeave.getLeaveNo());
        historicTaskInstanceDTO.setProcessInstanceId(sysOaLeave.getProcessInstanceId());
        historicTaskInstanceDTO.setProcessStatus(WorkRecordStatus.FINISHED);
        List<HistoricTaskInstanceVO> historicTaskInstanceList= flowableService.getHistoricTaskInstanceNoPage(historicTaskInstanceDTO);
        String spendTime = DateUtil.formatBetween(sysOaLeave.getStartTime(), sysOaLeave.getEndTime(), BetweenFormater.Level.SECOND);
        sysOaLeave.setLeaveTime(spendTime);
        mmap.put("sysOaLeave", sysOaLeave);
        mmap.put("historicTaskInstanceList", historicTaskInstanceList);
        return prefix + "/detail";
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
    public AjaxResult managerFinishTask(LeaveFinishTaskDTO finishTaskDTO)

    {
        SysUser sysUser = ShiroUtils.getSysUser();
        finishTaskDTO.setUserId(String.valueOf(sysUser.getUserId()));
        finishTaskDTO.setHr("2");
        flowableTaskService.submitTask(finishTaskDTO);
        return AjaxResult.success();
    }

    /**
     * hr完成审批
     */
    @Log(title = "hr完成审批", businessType = BusinessType.OTHER)
    @PostMapping("/hrFinishTask")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult hrFinishTask(LeaveFinishTaskDTO finishTaskDTO)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        finishTaskDTO.setUserId(String.valueOf(sysUser.getUserId()));
        flowableTaskService.submitTask(finishTaskDTO);
        return AjaxResult.success();
    }

    /**
     * 重新发起申请
     */
    @Log(title = "重新发起申请", businessType = BusinessType.OTHER)
    @PostMapping("/reStartTask")
    @ResponseBody
    @Transactional
    public AjaxResult reStartTask(LeaveRestartTaskDTO finishTaskDTO)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        SysOaLeave sysOaLeave=new SysOaLeave();
        BeanUtils.copyProperties(finishTaskDTO,sysOaLeave);
        sysOaLeave.setUpdateBy(sysUser.getUserName());
        int i = sysOaLeaveService.updateSysOaLeave(sysOaLeave);
        finishTaskDTO.setUserId(String.valueOf(sysUser.getUserId()));
        flowableTaskService.submitTask(finishTaskDTO);
        return AjaxResult.success();
    }
}
