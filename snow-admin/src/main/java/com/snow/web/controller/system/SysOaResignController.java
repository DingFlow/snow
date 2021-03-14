package com.snow.web.controller.system;

import com.snow.common.annotation.Log;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.constant.SequenceConstants;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.flowable.domain.resign.SysOaResignForm;
import com.snow.flowable.domain.resign.SysOaResignTask;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysOaResign;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysOaResignService;
import com.snow.system.service.ISysSequenceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 离职申请单Controller
 * 
 * @author 没用的阿吉
 * @date 2021-03-10
 */
@Controller
@RequestMapping("/system/resign")
public class SysOaResignController extends BaseController
{
    private String prefix = "system/signature";

    @Autowired
    private ISysOaResignService sysOaResignService;

    @Autowired
    private ISysSequenceService sequenceService;

    @Autowired
    private FlowableService flowableService;

    @Autowired
    private FlowableTaskService flowableTaskService;


    @RequiresPermissions("system:resign:view")
    @GetMapping()
    public String resign()
    {
        return prefix + "/resign";
    }

    /**
     * 查询离职申请单列表
     */
    @RequiresPermissions("system:resign:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysOaResign sysOaResign)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        //管理员权限判断
        if(!ShiroUtils.getSysUser().isAdmin()){
            sysOaResign.setApplyPerson(String.valueOf(sysUser.getUserId()));
        }
        List<SysOaResign> list = sysOaResignService.selectSysOaResignList(sysOaResign);
        return getDataTable(list);
    }

    /**
     * 导出离职申请单列表
     */
    @RequiresPermissions("system:resign:export")
    @Log(title = "离职申请单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOaResign sysOaResign)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        //管理员权限判断
        if(!ShiroUtils.getSysUser().isAdmin()){
            sysOaResign.setApplyPerson(String.valueOf(sysUser.getUserId()));
        }
        List<SysOaResign> list = sysOaResignService.selectSysOaResignList(sysOaResign);
        ExcelUtil<SysOaResign> util = new ExcelUtil<SysOaResign>(SysOaResign.class);
        return util.exportExcel(list, "resign");
    }

    /**
     * 新增离职申请单
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        String newSequenceNo = sequenceService.getNewSequenceNo(SequenceConstants.OA_RESIGN_SEQUENCE);
        mmap.put("resignNo", newSequenceNo);
        return prefix + "/add";
    }

    /**
     * 新增保存离职申请单
     */
    @RequiresPermissions("system:resign:add")
    @Log(title = "离职申请单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysOaResign sysOaResign)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaResign.setCreateBy(String.valueOf(sysUser.getUserId()));
        sysOaResign.setApplyPerson(String.valueOf(sysUser.getUserId()));
        sysOaResign.setUpdateBy(String.valueOf(sysUser.getUserId()));
        return toAjax(sysOaResignService.insertSysOaResign(sysOaResign));
    }

    /**
     * 修改离职申请单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SysOaResign sysOaResign = sysOaResignService.selectSysOaResignById(id);
        mmap.put("sysOaResign", sysOaResign);
        return prefix + "/edit";
    }

    /**
     * 修改离职申请单并发起申请
     */
    @RequiresPermissions("system:resign:edit")
    @Log(title = "离职申请单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult editSave(SysOaResign sysOaResign)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaResign.setUpdateBy(String.valueOf(sysUser.getUserId()));
        int i = sysOaResignService.updateSysOaResign(sysOaResign);
        SysOaResign newSysOaResign = sysOaResignService.selectSysOaResignById(sysOaResign.getId());
        //发起审批
        SysOaResignForm sysOaResignForm=new SysOaResignForm();
        BeanUtils.copyProperties(newSysOaResign,sysOaResignForm);
        sysOaResignForm.setBusinessKey(sysOaResignForm.getResignNo());
        sysOaResignForm.setStartUserId(String.valueOf(sysUser.getUserId()));
        sysOaResignForm.setBusVarUrl("/system/resign/detail");
        ProcessInstance processInstance = flowableService.startProcessInstanceByAppForm(sysOaResignForm);
        //推进任务节点
        flowableTaskService.automaticTask(processInstance.getProcessInstanceId());
        return toAjax(i);
    }

    /**
     * 删除离职申请单
     */
    @RequiresPermissions("system:resign:remove")
    @Log(title = "离职申请单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysOaResignService.deleteSysOaResignByIds(ids));
    }


    /**
     * 详情页
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SysOaResign sysOaResign = sysOaResignService.selectSysOaResignById(id);
        mmap.put("sysOaResign", sysOaResign);
        return prefix + "/detail";
    }


    /**
     * 重新申请
     */
    @PostMapping("/restart")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult restart(SysOaResignTask sysOaResignTask)
    {
        SysOaResign sysOaResign=new SysOaResign();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaResign.setUpdateBy(String.valueOf(sysUser.getUserId()));
        BeanUtils.copyProperties(sysOaResignTask,sysOaResign);
        int i = sysOaResignService.updateSysOaResign(sysOaResign);
        sysOaResignTask.setUserId(String.valueOf(sysUser.getUserId()));
        sysOaResignTask.setIsUpdateBus(true);
        sysOaResignTask.setIsStart(sysOaResignTask.getIsPass());
        flowableTaskService.submitTask(sysOaResignTask);
        return toAjax(i);
    }

    /**
     * 选择用户
     */
    @GetMapping("/selectUser")
    public String selectUser(ModelMap mmap)
    {
        return prefix + "/selectUser";
    }
}
