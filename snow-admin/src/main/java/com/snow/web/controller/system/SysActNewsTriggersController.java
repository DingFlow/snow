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
import com.snow.system.domain.SysActNewsTriggers;
import com.snow.system.service.ISysActNewsTriggersService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 流程消息配置Controller
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
@Controller
@RequestMapping("/system/triggers11")
public class SysActNewsTriggersController extends BaseController
{
    private String prefix = "system/triggers";

    @Autowired
    private ISysActNewsTriggersService sysActNewsTriggersService;

    @RequiresPermissions("system:triggers:view")
    @GetMapping()
    public String triggers()
    {
        return prefix + "/triggers";
    }

    /**
     * 查询流程消息配置列表
     */
    @RequiresPermissions("system:triggers:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysActNewsTriggers sysActNewsTriggers)
    {
        startPage();
        List<SysActNewsTriggers> list = sysActNewsTriggersService.selectSysActNewsTriggersList(sysActNewsTriggers);
        return getDataTable(list);
    }

    /**
     * 导出流程消息配置列表
     */
    @RequiresPermissions("system:triggers:export")
    @Log(title = "流程消息配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysActNewsTriggers sysActNewsTriggers)
    {
        List<SysActNewsTriggers> list = sysActNewsTriggersService.selectSysActNewsTriggersList(sysActNewsTriggers);
        ExcelUtil<SysActNewsTriggers> util = new ExcelUtil<SysActNewsTriggers>(SysActNewsTriggers.class);
        return util.exportExcel(list, "triggers");
    }

    /**
     * 新增流程消息配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存流程消息配置
     */
    @RequiresPermissions("system:triggers:add")
    @Log(title = "流程消息配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysActNewsTriggers sysActNewsTriggers)
    {
        return toAjax(sysActNewsTriggersService.insertSysActNewsTriggers(sysActNewsTriggers));
    }

    /**
     * 修改流程消息配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysActNewsTriggers sysActNewsTriggers = sysActNewsTriggersService.selectSysActNewsTriggersById(id);
        mmap.put("sysActNewsTriggers", sysActNewsTriggers);
        return prefix + "/edit";
    }

    /**
     * 修改保存流程消息配置
     */
    @RequiresPermissions("system:triggers:edit")
    @Log(title = "流程消息配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysActNewsTriggers sysActNewsTriggers)
    {
        return toAjax(sysActNewsTriggersService.updateSysActNewsTriggers(sysActNewsTriggers));
    }

    /**
     * 删除流程消息配置
     */
    @RequiresPermissions("system:triggers:remove")
    @Log(title = "流程消息配置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysActNewsTriggersService.deleteSysActNewsTriggersByIds(ids));
    }
}
