package com.snow.from.controller;

import com.snow.common.annotation.Log;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.service.ISysFormFieldService;
import com.snow.from.service.ISysFormInstanceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 单实例Controller
 * 
 * @author 没用的阿吉
 * @date 2021-03-21
 */
@Controller
@RequestMapping("/from/instance")
public class SysFormInstanceController extends BaseController
{
    private String prefix = "system/instance";

    @Autowired
    private ISysFormInstanceService sysFormInstanceService;
    
    @Autowired
    private ISysFormFieldService sysFormFieldService;


    @RequiresPermissions("system:instance:view")
    @GetMapping()
    public String instance()
    {
        return prefix + "/instance";
    }

    /**
     * 查询单实例列表
     */
    @RequiresPermissions("system:instance:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFormInstance sysFormInstance)
    {
        startPage();
        List<SysFormInstance> list = sysFormInstanceService.selectSysFormInstanceList(sysFormInstance);
        return getDataTable(list);
    }

    /**
     * 导出单实例列表
     */
    @RequiresPermissions("system:instance:export")
    @Log(title = "单实例", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFormInstance sysFormInstance)
    {
        List<SysFormInstance> list = sysFormInstanceService.selectSysFormInstanceList(sysFormInstance);
        ExcelUtil<SysFormInstance> util = new ExcelUtil<SysFormInstance>(SysFormInstance.class);
        return util.exportExcel(list, "instance");
    }

    /**
     * 预览
     * @return
     */
    @GetMapping("/fromPreview/{id}")
    public String fromPreview(@PathVariable("id") Long id,ModelMap mmap)
    {
        mmap.put("formId",id);
        return "/fromPreview";
    }
    /**
     * 获取表单内容
     * @param formId
     * @return
     */
    @PostMapping("/getFromInfo")
    @ResponseBody
    public AjaxResult getFromInfo(@RequestParam Long formId)
    {
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(formId);
        return AjaxResult.success(sysFormInstance.getFromContentHtml());
    }
    /**
     * 新增单实例
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存单实例
     */
    @RequiresPermissions("system:instance:add")
    @Log(title = "单实例", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysFormInstance sysFormInstance)
    {
        return toAjax(sysFormInstanceService.insertSysFormInstance(sysFormInstance));
    }

    /**
     * 修改单实例
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(id);
        mmap.put("sysFormInstance", sysFormInstance);
        return prefix + "/edit";
    }

    /**
     * 修改保存单实例
     */
    @RequiresPermissions("system:instance:edit")
    @Log(title = "单实例", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFormInstance sysFormInstance)
    {
        SysFormInstance sysFormInstanceName = sysFormInstanceService.selectSysFormInstanceByFormCode(sysFormInstance.getFormName());
        if(StringUtils.isNotNull(sysFormInstanceName)&&!sysFormInstanceName.getFormName().equals(sysFormInstance.getFormName())){
            return AjaxResult.error(String.format("表单名称:%已存在",sysFormInstance.getFormName()));
        }
        return toAjax(sysFormInstanceService.updateSysFormInstance(sysFormInstance));
    }

    /**
     * 删除单实例
     */
    @RequiresPermissions("system:instance:remove")
    @Log(title = "单实例", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysFormInstanceService.deleteSysFormInstanceByIds(ids));
    }
}
