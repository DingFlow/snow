package com.snow.system.controller;

import java.util.List;
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
import com.snow.system.domain.SysOaAttendance;
import com.snow.system.service.ISysOaAttendanceService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 考勤Controller
 * 
 * @author Agee
 * @date 2022-01-26
 */
@Controller
@RequestMapping("/system/attendance")
public class SysOaAttendanceController extends BaseController
{
    private String prefix = "system/attendance";

    @Autowired
    private ISysOaAttendanceService sysOaAttendanceService;

    @RequiresPermissions("system:attendance:view")
    @GetMapping()
    public String attendance()
    {
        return prefix + "/attendance";
    }

    /**
     * 查询考勤列表
     */
    @RequiresPermissions("system:attendance:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysOaAttendance sysOaAttendance)
    {
        startPage();
        List<SysOaAttendance> list = sysOaAttendanceService.selectSysOaAttendanceList(sysOaAttendance);
        return getDataTable(list);
    }

    /**
     * 导出考勤列表
     */
    @RequiresPermissions("system:attendance:export")
    @Log(title = "考勤", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOaAttendance sysOaAttendance)
    {
        List<SysOaAttendance> list = sysOaAttendanceService.selectSysOaAttendanceList(sysOaAttendance);
        ExcelUtil<SysOaAttendance> util = new ExcelUtil<SysOaAttendance>(SysOaAttendance.class);
        return util.exportExcel(list, "attendance");
    }

    /**
     * 新增考勤
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存考勤
     */
    @RequiresPermissions("system:attendance:add")
    @Log(title = "考勤", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysOaAttendance sysOaAttendance)
    {
        return toAjax(sysOaAttendanceService.insertSysOaAttendance(sysOaAttendance));
    }

    /**
     * 修改考勤
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysOaAttendance sysOaAttendance = sysOaAttendanceService.selectSysOaAttendanceById(id);
        mmap.put("sysOaAttendance", sysOaAttendance);
        return prefix + "/edit";
    }

    /**
     * 修改保存考勤
     */
    @RequiresPermissions("system:attendance:edit")
    @Log(title = "考勤", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysOaAttendance sysOaAttendance)
    {
        return toAjax(sysOaAttendanceService.updateSysOaAttendance(sysOaAttendance));
    }

    /**
     * 删除考勤
     */
    @RequiresPermissions("system:attendance:remove")
    @Log(title = "考勤", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysOaAttendanceService.deleteSysOaAttendanceByIds(ids));
    }
}
