package com.snow.system.controller;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.constant.SequenceConstants;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysFnAccountBill;
import com.snow.system.service.ISysFnAccountBillService;
import com.snow.system.service.ISysSequenceService;
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
import com.snow.system.domain.SysFnAccount;
import com.snow.system.service.ISysFnAccountService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 账户Controller
 * 
 * @author Agee
 * @date 2022-02-16
 */
@Controller
@RequestMapping("/system/account")
public class SysFnAccountController extends BaseController
{
    private String prefix = "system/account";

    @Autowired
    private ISysFnAccountService sysFnAccountService;

    @Autowired
    private ISysSequenceService sysSequenceService;

    @Autowired
    private ISysFnAccountBillService sysFnAccountBillService;

    @RequiresPermissions("system:account:view")
    @GetMapping()
    public String account()
    {
        return prefix + "/account";
    }

    /**
     * 查询账户列表
     */
    @RequiresPermissions("system:account:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFnAccount sysFnAccount)
    {
        startPage();
        List<SysFnAccount> list = sysFnAccountService.selectSysFnAccountList(sysFnAccount);
        return getDataTable(list);
    }

    /**
     * 导出账户列表
     */
    @RequiresPermissions("system:account:export")
    @Log(title = "账户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFnAccount sysFnAccount)
    {
        List<SysFnAccount> list = sysFnAccountService.selectSysFnAccountList(sysFnAccount);
        ExcelUtil<SysFnAccount> util = new ExcelUtil<SysFnAccount>(SysFnAccount.class);
        return util.exportExcel(list, "account");
    }

    /**
     * 新增账户
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存账户
     */
    @RequiresPermissions("system:account:add")
    @Log(title = "账户", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysFnAccount sysFnAccount)
    {
        Long userId = ShiroUtils.getUserId();
        String accountNo = sysSequenceService.getNewSequenceNo(SequenceConstants.FN_ACCOUNT_NO);
        sysFnAccount.setAccountNo(accountNo);
        sysFnAccount.setCreateBy(String.valueOf(userId));
        sysFnAccount.setUpdateBy(String.valueOf(userId));
        return toAjax(sysFnAccountService.insertSysFnAccount(sysFnAccount));
    }

    /**
     * 修改账户
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysFnAccount sysFnAccount = sysFnAccountService.selectSysFnAccountById(id);
        mmap.put("sysFnAccount", sysFnAccount);
        return prefix + "/edit";
    }

    /**
     * 修改保存账户
     */
    @RequiresPermissions("system:account:edit")
    @Log(title = "账户", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFnAccount sysFnAccount)
    {
        return toAjax(sysFnAccountService.updateSysFnAccount(sysFnAccount));
    }

    /**
     * 删除账户
     */
    @RequiresPermissions("system:account:remove")
    @Log(title = "账户", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysFnAccountService.deleteSysFnAccountByIds(ids));
    }

    /**
     * 账户充值
     * @param sysFnAccount
     * @return
     */
    @RequiresPermissions("system:account:rechargeAccount")
    @Log(title = "账户", businessType = BusinessType.OTHER)
    @PostMapping( "/rechargeAccount")
    @RepeatSubmit
    @ResponseBody
    public AjaxResult rechargeAccount(SysFnAccount sysFnAccount){
        LambdaQueryWrapper<SysFnAccount> lambda = new QueryWrapper<SysFnAccount>().lambda();
        SysFnAccount fnAccount = sysFnAccountService.getOne(lambda.eq(SysFnAccount::getAccountNo, sysFnAccount.getAccountNo()));
        BigDecimal amount = sysFnAccount.getTotalAmount().add(fnAccount.getTotalAmount());
        sysFnAccount.setTotalAmount(amount);
        sysFnAccountService.updateSysFnAccount(sysFnAccount);
        String billNo = sysSequenceService.getNewSequenceNo(SequenceConstants.FN_ACCOUNT_BILL_NO);
        SysFnAccountBill sysFnAccountBill=new SysFnAccountBill();
        sysFnAccountBill.setAccountNo(sysFnAccount.getAccountNo());
        sysFnAccountBill.setBillAmount(sysFnAccount.getTotalAmount());
        sysFnAccountBill.setBillType(1);
        sysFnAccountBill.setRemark("充值");
        sysFnAccountBill.setBillNo(billNo);
        sysFnAccountBillService.insertSysFnAccountBill(sysFnAccountBill);
        return AjaxResult.success();
    }
}
