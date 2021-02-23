package com.snow.web.controller.system;

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
import com.snow.system.domain.SysSequence;
import com.snow.system.service.ISysSequenceService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 系统序列设置Controller
 * 
 * @author snow
 * @date 2020-11-23
 */
@Controller
@RequestMapping("/system/sequence")
public class SysSequenceController extends BaseController
{
    private String prefix = "system/sequence";

    @Autowired
    private ISysSequenceService sysSequenceService;

    @RequiresPermissions("system:sequence:view")
    @GetMapping()
    public String sequence()
    {
        return prefix + "/sequence";
    }

    /**
     * 查询系统序列设置列表
     */
    @RequiresPermissions("system:sequence:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysSequence sysSequence)
    {
        startPage();
        List<SysSequence> list = sysSequenceService.selectSysSequenceList(sysSequence);
        return getDataTable(list);
    }

    /**
     * 导出系统序列设置列表
     */
    @RequiresPermissions("system:sequence:export")
    @Log(title = "系统序列设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysSequence sysSequence)
    {
        List<SysSequence> list = sysSequenceService.selectSysSequenceList(sysSequence);
        ExcelUtil<SysSequence> util = new ExcelUtil<SysSequence>(SysSequence.class);
        return util.exportExcel(list, "sequence");
    }

    /**
     * 新增系统序列设置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存系统序列设置
     */
    @RequiresPermissions("system:sequence:add")
    @Log(title = "系统序列设置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysSequence sysSequence)
    {
        return toAjax(sysSequenceService.insertSysSequence(sysSequence));
    }

    /**
     * 修改系统序列设置
     */
    @GetMapping("/edit/{name}")
    public String edit(@PathVariable("name") String name, ModelMap mmap)
    {
        SysSequence sysSequence = sysSequenceService.selectSysSequenceById(name);
        mmap.put("sysSequence", sysSequence);
        return prefix + "/edit";
    }

    /**
     * 修改保存系统序列设置
     */
    @RequiresPermissions("system:sequence:edit")
    @Log(title = "系统序列设置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysSequence sysSequence)
    {
        return toAjax(sysSequenceService.updateSysSequence(sysSequence));
    }

    /**
     * 删除系统序列设置
     */
    @RequiresPermissions("system:sequence:remove")
    @Log(title = "系统序列设置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysSequenceService.deleteSysSequenceByIds(ids));
    }
}
