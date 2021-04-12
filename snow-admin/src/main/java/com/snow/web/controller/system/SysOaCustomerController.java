package com.snow.web.controller.system;

import com.snow.common.annotation.Log;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.constant.SequenceConstants;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.flowable.domain.customer.DistributeCustomerTask;
import com.snow.flowable.domain.customer.SysOaCustomerForm;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysOaCustomer;
import com.snow.system.domain.SysOaRegion;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysOaCustomerService;
import com.snow.system.service.ISysOaRegionService;
import com.snow.system.service.ISysSequenceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户Controller
 * 
 * @author 没用的阿吉
 * @date 2021-04-09
 */
@Controller
@RequestMapping("/system/customer")
public class SysOaCustomerController extends BaseController
{
    private String prefix = "system/customer";

    @Autowired
    private ISysOaCustomerService sysOaCustomerService;

    @Autowired
    private ISysSequenceService sequenceService;

    @Autowired
    private ISysOaRegionService iSysOaRegionService;

    @Autowired
    private FlowableService flowableService;

    @Autowired
    private FlowableTaskService flowableTaskService;

    @RequiresPermissions("system:customer:view")
    @GetMapping()
    public String customer()
    {
        return prefix + "/customer";
    }

    /**
     * 查询客户列表
     */
    @RequiresPermissions("system:customer:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysOaCustomer sysOaCustomer)
    {
        startPage();
        List<SysOaCustomer> list = sysOaCustomerService.selectSysOaCustomerList(sysOaCustomer);
        return getDataTable(list);
    }

    /**
     * 导出客户列表
     */
    @RequiresPermissions("system:customer:export")
    @Log(title = "客户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOaCustomer sysOaCustomer)
    {
        List<SysOaCustomer> list = sysOaCustomerService.selectSysOaCustomerList(sysOaCustomer);
        ExcelUtil<SysOaCustomer> util = new ExcelUtil<SysOaCustomer>(SysOaCustomer.class);
        return util.exportExcel(list, "customer");
    }

    /**
     * 新增客户
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存客户
     */
    @RequiresPermissions("system:customer:add")
    @Log(title = "客户", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(SysOaCustomer sysOaCustomer)
    {
        SysUser sysUser = ShiroUtils.getSysUser();

        sysOaCustomer.setCreateBy(String.valueOf(sysUser.getUserId()));
        SysOaCustomer sysOaCustomer1 = sysOaCustomerService.selectSysOaCustomerByCustomerName(sysOaCustomer.getCustomerName());

        if(StringUtils.isNotNull(sysOaCustomer1)){
            return AjaxResult.error("客户："+sysOaCustomer.getCustomerName()+"已存在");
        }

        SysOaRegion provinceSysOaRegion = iSysOaRegionService.selectSysOaRegionById(Long.parseLong(sysOaCustomer.getCustomerProvinceCode()));
        sysOaCustomer.setCustomerProvinceName(provinceSysOaRegion.getName());

        SysOaRegion citySysOaRegion = iSysOaRegionService.selectSysOaRegionById(Long.parseLong(sysOaCustomer.getCustomerCityCode()));
        sysOaCustomer.setCustomerCityName(citySysOaRegion.getName());

        SysOaRegion areaSysOaRegion = iSysOaRegionService.selectSysOaRegionById(Long.parseLong(sysOaCustomer.getCustomerAreaCode()));
        sysOaCustomer.setCustomerAreaName(areaSysOaRegion.getName());

        String newSequenceNo = sequenceService.getNewSequenceNo(SequenceConstants.OA_CUSTOMER_SEQUENCE);
        sysOaCustomer.setCustomerNo(newSequenceNo);
        return toAjax(sysOaCustomerService.insertSysOaCustomer(sysOaCustomer));
    }

    /**
     * 修改客户
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysOaCustomer sysOaCustomer = sysOaCustomerService.selectSysOaCustomerById(id);
        mmap.put("sysOaCustomer", sysOaCustomer);
        return prefix + "/edit";
    }

    /**
     * 修改保存客户
     */
    @RequiresPermissions("system:customer:edit")
    @Log(title = "客户", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    @RepeatSubmit
    public AjaxResult editSave(SysOaCustomer sysOaCustomer)
    {
        SysUser sysUser = ShiroUtils.getSysUser();

        sysOaCustomer.setCreateBy(String.valueOf(sysUser.getUserId()));
        SysOaCustomer sysOaCustomer1 = sysOaCustomerService.selectSysOaCustomerById(sysOaCustomer.getId());

        if(StringUtils.isNotNull(sysOaCustomer1)&&!sysOaCustomer.getCustomerName().equals(sysOaCustomer1.getCustomerName())){
            return AjaxResult.error("客户："+sysOaCustomer.getCustomerName()+"已存在");
        }

        SysOaRegion provinceSysOaRegion = iSysOaRegionService.selectSysOaRegionById(Long.parseLong(sysOaCustomer.getCustomerProvinceCode()));
        sysOaCustomer.setCustomerProvinceName(provinceSysOaRegion.getName());

        SysOaRegion citySysOaRegion = iSysOaRegionService.selectSysOaRegionById(Long.parseLong(sysOaCustomer.getCustomerCityCode()));
        sysOaCustomer.setCustomerCityName(citySysOaRegion.getName());

        SysOaRegion areaSysOaRegion = iSysOaRegionService.selectSysOaRegionById(Long.parseLong(sysOaCustomer.getCustomerAreaCode()));
        sysOaCustomer.setCustomerAreaName(areaSysOaRegion.getName());
        int i = sysOaCustomerService.updateSysOaCustomer(sysOaCustomer);
        SysOaCustomer newSysOaResign = sysOaCustomerService.selectSysOaCustomerById(sysOaCustomer.getId());
        //发起审批
        SysOaCustomerForm sysOaCustomerForm=new SysOaCustomerForm();
        BeanUtils.copyProperties(newSysOaResign,sysOaCustomerForm);
        sysOaCustomerForm.setBusinessKey(newSysOaResign.getCustomerNo());
        sysOaCustomerForm.setStartUserId(String.valueOf(sysUser.getUserId()));
        sysOaCustomerForm.setBusVarUrl("/system/customer/detail");
        ProcessInstance processInstance = flowableService.startProcessInstanceByAppForm(sysOaCustomerForm);
        //推进任务节点
        flowableTaskService.automaticTask(processInstance.getProcessInstanceId());
        return toAjax(i);
    }

    /**
     * 删除客户
     */
    @RequiresPermissions("system:customer:remove")
    @Log(title = "客户", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysOaCustomerService.deleteSysOaCustomerByIds(ids));
    }


    /**
     * 分配客户节点
     */
    @PostMapping("/distributeCustomer")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult distributeCustomer(DistributeCustomerTask distributeCustomerTask)
    {
        SysOaCustomer sysOaCustomer=new SysOaCustomer();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaCustomer.setUpdateBy(String.valueOf(sysUser.getUserId()));
        BeanUtils.copyProperties(distributeCustomerTask,sysOaCustomer);
        int i = sysOaCustomerService.updateSysOaCustomerByCustomerNo(sysOaCustomer);
        distributeCustomerTask.setUserId(String.valueOf(sysUser.getUserId()));
        distributeCustomerTask.setIsUpdateBus(true);
        flowableTaskService.submitTask(distributeCustomerTask);
        return toAjax(i);
    }

    /**
     * 详情页
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:customer:detail")
    public String detail(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysOaCustomer sysOaCustomer = sysOaCustomerService.selectSysOaCustomerById(id);
        mmap.put("sysOaCustomer", sysOaCustomer);
        return prefix + "/detail";
    }
}
