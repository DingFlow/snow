package com.snow.system.controller;

import com.snow.common.annotation.Log;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysFnAccount;
import com.snow.system.domain.request.RechargeAccountRequest;
import com.snow.system.service.ISysFnAccountService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @RequiresPermissions("system:account:view")
    @GetMapping()
    public String account(ModelMap mmap)
    {
        SysFnAccount sysFnAccount=new SysFnAccount();
        sysFnAccount.setIsDelete(0);
        List<SysFnAccount> sysFnAccounts = sysFnAccountService.selectSysFnAccountList(sysFnAccount);
        mmap.put("sysFnAccounts", sysFnAccounts);
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
     * 获取账户详情
     * @param accountNo 账户号
     * @return 账户详情
     */
    @RequiresPermissions("system:account:detail")
    @PostMapping("/getSysFnAccountByNo")
    @ResponseBody
    public AjaxResult getSysFnAccountByNo(String accountNo)
    {
        return AjaxResult.success(sysFnAccountService.getSysFnAccountByNo(accountNo));
    }

    /**
     * 跳转充值页面
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/rechargeAccount/{id}")
    public String toRechargeAccount(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysFnAccount sysFnAccount = sysFnAccountService.selectSysFnAccountById(id);
        mmap.put("sysFnAccount", sysFnAccount);
        return prefix + "/rechargeAccount";
    }
    /**
     * 账户充值
     * @param rechargeAccountRequest
     * @return
     */
    @RequiresPermissions("system:account:rechargeAccount")
    @Log(title = "账户", businessType = BusinessType.OTHER)
    @PostMapping( "/rechargeAccount")
    @RepeatSubmit
    @ResponseBody
    public AjaxResult rechargeAccount(RechargeAccountRequest rechargeAccountRequest){
        rechargeAccountRequest.setRechargeRemark("账户充值");
        return AjaxResult.success(sysFnAccountService.rechargeAccount(rechargeAccountRequest));
    }
}
