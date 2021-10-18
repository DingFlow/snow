package com.snow.web.controller.system;

import java.util.List;

import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysOaCustomerVisitLog;
import com.snow.system.domain.SysOaTaskDistribute;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysOaTaskDistributeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.snow.common.annotation.Log;
import com.snow.common.enums.BusinessType;
import org.springframework.stereotype.Controller;
import com.snow.system.domain.SysOaTask;
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

    @RequiresPermissions("system:task:view")
    @GetMapping()
    public String task()
    {
        return prefix + "/task1";
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
    public TableDataInfo visitLogList(SysOaTaskDistribute sysOaTaskDistribute)
    {
        startPage();
        List<SysOaTaskDistribute> list = sysOaTaskDistributeService.selectSysOaTaskDistributeList(sysOaTaskDistribute);
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
}
