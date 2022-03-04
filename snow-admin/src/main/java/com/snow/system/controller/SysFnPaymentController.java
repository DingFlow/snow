package com.snow.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.snow.common.annotation.Log;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.flowable.domain.payment.PaymentCashierTask;
import com.snow.flowable.domain.payment.PaymentForm;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysFnPayment;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysFnPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付申请Controller
 * 
 * @author Agee
 * @date 2022-02-19
 */
@Controller
@RequestMapping("/system/payment")
@Slf4j
public class SysFnPaymentController extends BaseController
{
    private String prefix = "system/payment";

    @Autowired
    private ISysFnPaymentService sysFnPaymentService;

    @Autowired
    private FlowableService flowableService;

    @Autowired
    private FlowableTaskService flowableTaskService;

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
     * 详情
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:payment:detail")
    public String detail(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysFnPayment sysFnPayment = sysFnPaymentService.selectSysFnPaymentById(id);
        mmap.put("sysFnPayment", sysFnPayment);
        return prefix + "/detail";
    }
    /**
     * 修改保存支付申请
     */
    @RequiresPermissions("system:payment:edit")
    @Log(title = "支付申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFnPayment sysFnPayment) {
        //发起审批
        PaymentForm paymentForm = BeanUtil.copyProperties(sysFnPayment, PaymentForm.class);
        paymentForm.setBusinessKey(paymentForm.getPaymentNo());
        paymentForm.setStartUserId(String.valueOf(ShiroUtils.getUserId()));
        paymentForm.setBusVarUrl("/system/payment/detail");
        SysFnPayment newSysFnPayment = sysFnPaymentService.selectSysFnPaymentById(sysFnPayment.getId());
        paymentForm.setCreateTime(newSysFnPayment.getCreateTime());
        ProcessInstance processInstance = flowableService.startProcessInstanceByAppForm(paymentForm);
        log.info("@@发起业务单号为:{}付款申请流程:{}",sysFnPayment.getPaymentNo(),processInstance.getProcessInstanceId());
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


    /**
     * 出纳审核
     */
    @PostMapping("/cashierTask")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult cashierTask(PaymentCashierTask paymentCashierTask) {
        SysUser sysUser = ShiroUtils.getSysUser();
        //完成任务
        paymentCashierTask.setUserId(String.valueOf(sysUser.getUserId()));
        paymentCashierTask.setIsUpdateBus(true);
        paymentCashierTask.setIsStart(paymentCashierTask.getIsPass());
        flowableTaskService.submitTask(paymentCashierTask);
        return AjaxResult.success();
    }
}
