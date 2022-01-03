package com.snow.web.controller.system;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.snow.common.annotation.Log;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.constant.MessageConstants;
import com.snow.common.constant.SequenceConstants;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.enums.MessageEventType;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.flowable.domain.customer.DistributeCustomerTask;
import com.snow.flowable.domain.customer.SysOaCustomerForm;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.framework.util.ShiroUtils;
import com.snow.common.core.domain.MessageEventRequest;
import com.snow.system.domain.SysOaCustomer;
import com.snow.system.domain.SysOaCustomerVisitLog;
import com.snow.system.domain.SysOaRegion;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysOaCustomerService;
import com.snow.system.service.ISysOaCustomerVisitLogService;
import com.snow.system.service.ISysOaRegionService;
import com.snow.system.service.ISysSequenceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private ISysOaCustomerVisitLogService sysOaCustomerVisitLogService;

    @Autowired
    private ApplicationContext applicationContext;

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
     * 查询客户拜访日志列表
     */
    @RequiresPermissions("system:customer:visitLogList")
    @PostMapping("/visitLogList")
    @ResponseBody
    public TableDataInfo visitLogList(SysOaCustomerVisitLog sysOaCustomerVisitLog)
    {
        startPage();
        List<SysOaCustomerVisitLog> list = sysOaCustomerVisitLogService.selectSysOaCustomerVisitLogList(sysOaCustomerVisitLog);
        return getDataTable(list);
    }


    /**
     * 查询客户列表
     */
    @RequiresPermissions("system:customer:myList")
    @PostMapping("/myList")
    @ResponseBody
    public TableDataInfo myList(SysOaCustomer sysOaCustomer)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaCustomer.setCustomerManager(String.valueOf(sysUser.getUserId()));
        sysOaCustomer.setCustomerStatus("ADMITED");
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
        SysOaCustomerVisitLog sysOaCustomerVisitLog=new SysOaCustomerVisitLog();
        sysOaCustomerVisitLog.setCustomerNo(sysOaCustomer.getCustomerNo());
        List<SysOaCustomerVisitLog> sysOaCustomerVisitLogs = sysOaCustomerVisitLogService.selectSysOaCustomerVisitLogList(sysOaCustomerVisitLog);
        mmap.put("sysOaCustomer", sysOaCustomer);
        mmap.put("sysOaCustomerVisitLogs", sysOaCustomerVisitLogs);
        return prefix + "/detail";
    }


    /**
     * 新增客户拜访日志
     */
    @GetMapping("/visitAdd/{id}")
    public String visitAdd(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysOaCustomer sysOaCustomer = sysOaCustomerService.selectSysOaCustomerById(id);
        mmap.put("sysOaCustomer", sysOaCustomer);
        return prefix + "/visitAdd";
    }

    /**
     * 新增保存客户拜访日志
     */
    @RequiresPermissions("system:customer:visitLogAdd")
    @Log(title = "客户拜访日志", businessType = BusinessType.INSERT)
    @PostMapping("/visitAdd")
    @ResponseBody
    public AjaxResult visitAdd(SysOaCustomerVisitLog sysOaCustomerVisitLog)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysUser.setCreateBy(String.valueOf(sysUser.getUserId()));
        int i = sysOaCustomerVisitLogService.insertSysOaCustomerVisitLog(sysOaCustomerVisitLog);
        //加入消息通知
        if(StringUtils.isNotNull(sysOaCustomerVisitLog.getAcceptUser())){
            SysOaCustomer sysOaCustomer = sysOaCustomerService.selectSysOaCustomerByCustomerNo(sysOaCustomerVisitLog.getCustomerNo());
            MessageEventRequest messageEventDTO=new MessageEventRequest(MessageEventType.SEND_VISIT_LOG.getCode());
            messageEventDTO.setProducerId(String.valueOf(sysUser.getUserId()));
            messageEventDTO.setConsumerIds(Sets.newHashSet(sysOaCustomerVisitLog.getAcceptUser()));
            messageEventDTO.setMessageEventType(MessageEventType.SEND_VISIT_LOG);
            messageEventDTO.setMessageOutsideId(String.valueOf(sysOaCustomerVisitLog.getId()));
            messageEventDTO.setMessageShow(2);
            Map<String,Object> map= Maps.newHashMap();
            map.put("userName",sysUser.getUserName());
            map.put("nowTime", DateUtil.now());
            map.put("enterprice",sysOaCustomer.getCustomerName());
            map.put("id",sysOaCustomerVisitLog.getId());
            messageEventDTO.setParamMap(map);
            messageEventDTO.setTemplateCode(MessageConstants.CUSTOMER_VISIT_LOG_CODE);
            applicationContext.publishEvent(messageEventDTO);
        }
        return toAjax(i);
    }

    /**
     * 修改客户拜访日志
     */
    @GetMapping("/visitLogEdit/{id}")
    public String visitLogEdit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysOaCustomerVisitLog sysOaCustomerVisitLog = sysOaCustomerVisitLogService.selectSysOaCustomerVisitLogById(id);
        mmap.put("sysOaCustomerVisitLog", sysOaCustomerVisitLog);
        return prefix + "/visitLogEdit";
    }

    /**
     * 修改保存客户拜访日志
     */
    @RequiresPermissions("system:customer:visitLogEdit")
    @Log(title = "客户拜访日志", businessType = BusinessType.UPDATE)
    @PostMapping("/visitLogEdit")
    @ResponseBody
    public AjaxResult visitLogEditSave(SysOaCustomerVisitLog sysOaCustomerVisitLog)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysUser.setUpdateBy(String.valueOf(sysUser.getUserId()));
        return toAjax(sysOaCustomerVisitLogService.updateSysOaCustomerVisitLog(sysOaCustomerVisitLog));
    }

    /**
     * 删除客户拜访日志
     */
    @RequiresPermissions("system:customer:visitLogRemove")
    @Log(title = "客户拜访日志", businessType = BusinessType.DELETE)
    @PostMapping( "/visitLogRemove")
    @ResponseBody
    public AjaxResult visitLogRemove(String ids)
    {
        return toAjax(sysOaCustomerVisitLogService.deleteSysOaCustomerVisitLogByIds(ids));
    }



    /**
     * 消息详情页
     */
    @GetMapping("/messageDetail/{id}")
    public String messageDetail(@PathVariable("id") Long id)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        SysOaCustomerVisitLog sysOaCustomerVisitLog=sysOaCustomerVisitLogService.selectSysOaCustomerVisitLogById(id);
        SysOaCustomer sysOaCustomer = sysOaCustomerService.selectSysOaCustomerByCustomerNo(sysOaCustomerVisitLog.getCustomerNo());
        //已读监听
        MessageEventRequest messageEventDTO=new MessageEventRequest(MessageEventType.MARK_READED.getCode());
        messageEventDTO.setConsumerIds(Sets.newHashSet(String.valueOf(sysUser.getUserId())));
        messageEventDTO.setMessageOutsideId(String.valueOf(id));
        messageEventDTO.setMessageEventType(MessageEventType.SEND_VISIT_LOG);
        applicationContext.publishEvent(messageEventDTO);
        return redirect("/system/customer/detail/"+ sysOaCustomer.getId()) ;
    }
}
