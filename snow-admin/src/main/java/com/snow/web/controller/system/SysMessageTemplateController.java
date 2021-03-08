package com.snow.web.controller.system;

import java.util.List;

import com.snow.common.annotation.RepeatSubmit;
import com.snow.flowable.config.FlowIdGenerator;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysUser;
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
import com.snow.system.domain.SysMessageTemplate;
import com.snow.system.service.ISysMessageTemplateService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 消息模板Controller
 * 
 * @author qimingjin
 * @date 2021-02-27
 */
@Controller
@RequestMapping("/system/template")
public class SysMessageTemplateController extends BaseController
{
    private String prefix = "system/template";

    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;

    @RequiresPermissions("system:template:view")
    @GetMapping()
    public String template()
    {
        return prefix + "/template";
    }

    /**
     * 查询消息模板列表
     */
    @RequiresPermissions("system:template:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysMessageTemplate sysMessageTemplate)
    {
        startPage();
        List<SysMessageTemplate> list = sysMessageTemplateService.selectSysMessageTemplateList(sysMessageTemplate);
        return getDataTable(list);
    }

    /**
     * 导出消息模板列表
     */
    @RequiresPermissions("system:template:export")
    @Log(title = "消息模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysMessageTemplate sysMessageTemplate)
    {
        List<SysMessageTemplate> list = sysMessageTemplateService.selectSysMessageTemplateList(sysMessageTemplate);
        ExcelUtil<SysMessageTemplate> util = new ExcelUtil<SysMessageTemplate>(SysMessageTemplate.class);
        return util.exportExcel(list, "template");
    }

    /**
     * 新增消息模板
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        FlowIdGenerator flowIdGenerator = new FlowIdGenerator();
        mmap.put("templateCode", flowIdGenerator.getNextId());
        return prefix + "/add";
    }

    /**
     * 新增保存消息模板
     */
    @RequiresPermissions("system:template:add")
    @Log(title = "消息模板", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(SysMessageTemplate sysMessageTemplate)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysMessageTemplate.setCreateBy(String.valueOf(sysUser.getUserId()));
        return toAjax(sysMessageTemplateService.insertSysMessageTemplate(sysMessageTemplate));
    }

    /**
     * 修改消息模板
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SysMessageTemplate sysMessageTemplate = sysMessageTemplateService.selectSysMessageTemplateById(id);
        mmap.put("sysMessageTemplate", sysMessageTemplate);
        return prefix + "/edit";
    }

    /**
     * 修改保存消息模板
     */
    @RequiresPermissions("system:template:edit")
    @Log(title = "消息模板", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysMessageTemplate sysMessageTemplate)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysMessageTemplate.setUpdateBy(String.valueOf(sysUser.getUserId()));
        return toAjax(sysMessageTemplateService.updateSysMessageTemplate(sysMessageTemplate));
    }

    /**
     * 删除消息模板
     */
    @RequiresPermissions("system:template:remove")
    @Log(title = "消息模板", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysMessageTemplateService.deleteSysMessageTemplateByIds(ids));
    }
}
