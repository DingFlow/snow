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
import com.snow.system.domain.SysFnAccountBill;
import com.snow.system.service.ISysFnAccountBillService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 账户流水详情Controller
 * 
 * @author Agee
 * @date 2022-02-16
 */
@Controller
@RequestMapping("/system/bill")
public class SysFnAccountBillController extends BaseController
{
    private String prefix = "system/bill";

    @Autowired
    private ISysFnAccountBillService sysFnAccountBillService;

    @RequiresPermissions("system:bill:view")
    @GetMapping()
    public String bill()
    {
        return prefix + "/bill";
    }

    /**
     * 查询账户流水详情列表
     */
    @RequiresPermissions("system:bill:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFnAccountBill sysFnAccountBill)
    {
        startPage();
        List<SysFnAccountBill> list = sysFnAccountBillService.selectSysFnAccountBillList(sysFnAccountBill);
        return getDataTable(list);
    }

    /**
     * 导出账户流水详情列表
     */
    @RequiresPermissions("system:bill:export")
    @Log(title = "账户流水详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFnAccountBill sysFnAccountBill)
    {
        List<SysFnAccountBill> list = sysFnAccountBillService.selectSysFnAccountBillList(sysFnAccountBill);
        ExcelUtil<SysFnAccountBill> util = new ExcelUtil<SysFnAccountBill>(SysFnAccountBill.class);
        return util.exportExcel(list, "bill");
    }

    /**
     * 新增账户流水详情
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存账户流水详情
     */
    @RequiresPermissions("system:bill:add")
    @Log(title = "账户流水详情", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysFnAccountBill sysFnAccountBill)
    {
        return toAjax(sysFnAccountBillService.insertSysFnAccountBill(sysFnAccountBill));
    }

    /**
     * 修改账户流水详情
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysFnAccountBill sysFnAccountBill = sysFnAccountBillService.selectSysFnAccountBillById(id);
        mmap.put("sysFnAccountBill", sysFnAccountBill);
        return prefix + "/edit";
    }

    /**
     * 修改保存账户流水详情
     */
    @RequiresPermissions("system:bill:edit")
    @Log(title = "账户流水详情", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFnAccountBill sysFnAccountBill)
    {
        return toAjax(sysFnAccountBillService.updateSysFnAccountBill(sysFnAccountBill));
    }

    /**
     * 删除账户流水详情
     */
    @RequiresPermissions("system:bill:remove")
    @Log(title = "账户流水详情", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysFnAccountBillService.deleteSysFnAccountBillByIds(ids));
    }


}
