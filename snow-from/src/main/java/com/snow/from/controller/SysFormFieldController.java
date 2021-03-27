package com.snow.from.controller;

import com.snow.common.annotation.Log;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.from.domain.SysFormField;
import com.snow.from.service.ISysFormFieldService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 单字段Controller
 * 
 * @author 没用的阿吉
 * @date 2021-03-21
 */
@Controller
@RequestMapping("/system/field")
public class SysFormFieldController extends BaseController
{
    private String prefix = "system/field";

    @Autowired
    private ISysFormFieldService sysFormFieldService;

    @RequiresPermissions("system:field:view")
    @GetMapping()
    public String field()
    {
        return prefix + "/field";
    }

    /**
     * 查询单字段列表
     */
    @RequiresPermissions("system:field:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFormField sysFormField)
    {
        startPage();
        List<SysFormField> list = sysFormFieldService.selectSysFormFieldList(sysFormField);
        return getDataTable(list);
    }

    /**
     * 导出单字段列表
     */
    @RequiresPermissions("system:field:export")
    @Log(title = "单字段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFormField sysFormField)
    {
        List<SysFormField> list = sysFormFieldService.selectSysFormFieldList(sysFormField);
        ExcelUtil<SysFormField> util = new ExcelUtil<SysFormField>(SysFormField.class);
        return util.exportExcel(list, "field");
    }

    /**
     * 新增单字段
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存单字段
     */
    @RequiresPermissions("system:field:add")
    @Log(title = "单字段", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysFormField sysFormField)
    {
        return toAjax(sysFormFieldService.insertSysFormField(sysFormField));
    }

    /**
     * 修改单字段
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysFormField sysFormField = sysFormFieldService.selectSysFormFieldById(id);
        mmap.put("sysFormField", sysFormField);
        return prefix + "/edit";
    }

    /**
     * 修改保存单字段
     */
    @RequiresPermissions("system:field:edit")
    @Log(title = "单字段", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFormField sysFormField)
    {
        return toAjax(sysFormFieldService.updateSysFormField(sysFormField));
    }

    /**
     * 删除单字段
     */
    @RequiresPermissions("system:field:remove")
    @Log(title = "单字段", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysFormFieldService.deleteSysFormFieldByIds(ids));
    }
}
