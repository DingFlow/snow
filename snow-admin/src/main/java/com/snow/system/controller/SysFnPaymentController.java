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
import com.snow.system.domain.SysFnPayment;
import com.snow.system.service.ISysFnPaymentService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 支付申请Controller
 * 
 * @author Agee
 * @date 2022-02-19
 */
@Controller
@RequestMapping("/system/payment")
public class SysFnPaymentController extends BaseController
{
    private String prefix = "system/payment";

    @Autowired
    private ISysFnPaymentService sysFnPaymentService;

    @RequiresPermissions("system:payment:view")
    @GetMapping()
    public String payment()
    {
        return prefix + "/payment";
    }

    /**
     * 查询支付申请列表
     */
    @RequiresPermissions("system:payment:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFnPayment sysFnPayment)
    {
        startPage();
        List<SysFnPayment> list = sysFnPaymentService.selectSysFnPaymentList(sysFnPayment);
        return getDataTable(list);
    }

    /**
     * 导出支付申请列表
     */
    @RequiresPermissions("system:payment:export")
    @Log(title = "支付申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFnPayment sysFnPayment)
    {
        List<SysFnPayment> list = sysFnPaymentService.selectSysFnPaymentList(sysFnPayment);
        ExcelUtil<SysFnPayment> util = new ExcelUtil<SysFnPayment>(SysFnPayment.class);
        return util.exportExcel(list, "payment");
    }

    /**
     * 新增支付申请
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存支付申请
     */
    @RequiresPermissions("system:payment:add")
    @Log(title = "支付申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysFnPayment sysFnPayment)
    {
        return toAjax(sysFnPaymentService.insertSysFnPayment(sysFnPayment));
    }

    /**
     * 修改支付申请
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysFnPayment sysFnPayment = sysFnPaymentService.selectSysFnPaymentById(id);
        mmap.put("sysFnPayment", sysFnPayment);
        return prefix + "/edit";
    }

    /**
     * 修改保存支付申请
     */
    @RequiresPermissions("system:payment:edit")
    @Log(title = "支付申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFnPayment sysFnPayment)
    {
        return toAjax(sysFnPaymentService.updateSysFnPayment(sysFnPayment));
    }

    /**
     * 删除支付申请
     */
    @RequiresPermissions("system:payment:remove")
    @Log(title = "支付申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysFnPaymentService.deleteSysFnPaymentByIds(ids));
    }
}
