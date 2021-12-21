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
import com.snow.system.domain.SysOaFaqLabel;
import com.snow.system.service.ISysOaFaqLabelService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * faq问题标签Controller
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
@Controller
@RequestMapping("/system/label")
public class SysOaFaqLabelController extends BaseController
{
    private String prefix = "system/label";

    @Autowired
    private ISysOaFaqLabelService sysOaFaqLabelService;

    @RequiresPermissions("system:label:view")
    @GetMapping()
    public String label()
    {
        return prefix + "/label";
    }

    /**
     * 查询faq问题标签列表
     */
    @RequiresPermissions("system:label:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysOaFaqLabel sysOaFaqLabel)
    {
        startPage();
        List<SysOaFaqLabel> list = sysOaFaqLabelService.selectSysOaFaqLabelList(sysOaFaqLabel);
        return getDataTable(list);
    }

    /**
     * 导出faq问题标签列表
     */
    @RequiresPermissions("system:label:export")
    @Log(title = "faq问题标签", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOaFaqLabel sysOaFaqLabel)
    {
        List<SysOaFaqLabel> list = sysOaFaqLabelService.selectSysOaFaqLabelList(sysOaFaqLabel);
        ExcelUtil<SysOaFaqLabel> util = new ExcelUtil<SysOaFaqLabel>(SysOaFaqLabel.class);
        return util.exportExcel(list, "label");
    }

    /**
     * 新增faq问题标签
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存faq问题标签
     */
    @RequiresPermissions("system:label:add")
    @Log(title = "faq问题标签", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysOaFaqLabel sysOaFaqLabel)
    {
        return toAjax(sysOaFaqLabelService.insertSysOaFaqLabel(sysOaFaqLabel));
    }

    /**
     * 修改faq问题标签
     */
    @GetMapping("/edit/{faqNo}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SysOaFaqLabel sysOaFaqLabel = sysOaFaqLabelService.selectSysOaFaqLabelById(id);
        mmap.put("sysOaFaqLabel", sysOaFaqLabel);
        return prefix + "/edit";
    }

    /**
     * 修改保存faq问题标签
     */
    @RequiresPermissions("system:label:edit")
    @Log(title = "faq问题标签", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysOaFaqLabel sysOaFaqLabel)
    {
        return toAjax(sysOaFaqLabelService.updateSysOaFaqLabel(sysOaFaqLabel));
    }

}
