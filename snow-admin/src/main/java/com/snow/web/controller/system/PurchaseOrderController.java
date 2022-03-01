package com.snow.web.controller.system;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.google.common.collect.Lists;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.constant.SequenceConstants;
import com.snow.common.utils.poi.EasyExcelUtil;
import com.snow.flowable.domain.purchaseOrder.PurchaseCashierTask;
import com.snow.flowable.domain.purchaseOrder.PurchaseOrderForm;
import com.snow.flowable.domain.purchaseOrder.PurchaseOrderMainTask;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.framework.excel.PurchaseOrderListener;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.*;
import com.snow.system.service.ISysSequenceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.snow.common.annotation.Log;
import com.snow.common.enums.BusinessType;
import org.springframework.stereotype.Controller;
import com.snow.system.service.IPurchaseOrderMainService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 采购单主表Controller
 * 
 * @author snow
 * @date 2021-01-07
 */
@Controller
@RequestMapping("/system/purchaseOrder")
public class PurchaseOrderController extends BaseController
{
    private String prefix = "system/purchaseOrder";

    @Autowired
    private IPurchaseOrderMainService purchaseOrderMainService;

    @Autowired
    private FlowableService flowableService;

    @Autowired
    private FlowableTaskService flowableTaskService;

    @Autowired
    private ISysSequenceService sequenceService;
    @RequiresPermissions("system:purchaseOrder:view")
    @GetMapping()
    public String purchaseOrder()
    {
        return prefix + "/purchaseOrder";
    }

    /**
     * 查询采购单主表列表
     */
    @RequiresPermissions("system:purchaseOrder:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PurchaseOrderMain purchaseOrderMain)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        //管理员权限判断
        if(!ShiroUtils.getSysUser().isAdmin()){
            purchaseOrderMain.setBelongUser(String.valueOf(sysUser.getUserId()));
        }
        List<PurchaseOrderMain> list = purchaseOrderMainService.selectPurchaseOrderMainList(purchaseOrderMain);
        return getDataTable(list);
    }

    /**
     * 导出采购单主表列表detail
     */
    @RequiresPermissions("system:purchaseOrder:export")
    @Log(title = "采购单主表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PurchaseOrderMain purchaseOrderMain)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        purchaseOrderMain.setCreateBy(sysUser.getUserName());
        //管理员权限判断
        if(!ShiroUtils.getSysUser().isAdmin()){
            purchaseOrderMain.setCreateBy(String.valueOf(sysUser.getUserId()));
        }
        List<PurchaseOrderMain> list = purchaseOrderMainService.selectPurchaseOrderMainList(purchaseOrderMain);
        ExcelUtil<PurchaseOrderMain> util = new ExcelUtil<PurchaseOrderMain>(PurchaseOrderMain.class);
        return util.exportExcel(list, "purchaseOrder");
    }

    /**
     * 下载标准模板
     */
    @GetMapping("/download")
    @ResponseBody
    public AjaxResult download(HttpServletResponse response)
    {
        PurchaseOrderImport purchaseOrderImport=new PurchaseOrderImport();
        purchaseOrderImport.setGoodsNo("CSJ0001");
        purchaseOrderImport.setGoodsName("示例商品1");
        purchaseOrderImport.setGoodsPrice(new BigDecimal(2.5));
        purchaseOrderImport.setGoodsQuantity(new BigDecimal(2));
        purchaseOrderImport.setGoodsSize("示例规格1");
        purchaseOrderImport.setRemark("示例备注1");
        List<PurchaseOrderImport> list=Lists.newArrayList();
        list.add(purchaseOrderImport);
        try {
            EasyExcelUtil.writeExcel("采购单明细表","明细表",PurchaseOrderImport.class,list,response);
            return AjaxResult.success("下载成功");
        } catch (Exception e) {
            return AjaxResult.error("下载模板异常，请联系管理员");
        }
    }
    /**
     * 新增采购单主表
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        String newSequenceNo = sequenceService.getNewSequenceNo(SequenceConstants.OA_PURCHASE_SEQUENCE);
        mmap.put("orderNo", newSequenceNo);
        return prefix + "/add";
    }

    /**
     * 新增保存采购单主表
     */
    @RequiresPermissions("system:purchaseOrder:add")
    @Log(title = "采购单主表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(PurchaseOrderMain purchaseOrderMain)
    {
        return toAjax(purchaseOrderMainService.insertPurchaseOrderMain(purchaseOrderMain));
    }

    /**
     * 导入采购单子表列表
     */
    @RequiresPermissions("system:purchaseOrder:import")
    @Log(title = "采购单主表", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    @ResponseBody
    public AjaxResult importData(MultipartFile file) 
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        PurchaseOrderListener purchaseOrderListener = new PurchaseOrderListener(purchaseOrderMainService, sysUser);
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(file.getInputStream(), PurchaseOrderImport.class, purchaseOrderListener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.success("导入失败:"+e.getMessage());
        }finally {
            // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
            excelReader.finish();
            
        }
        List<PurchaseOrderImport> list = purchaseOrderListener.list;
        return AjaxResult.success(list);
    }
    /**
     * 修改采购单主表
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        PurchaseOrderMain purchaseOrderMain = purchaseOrderMainService.selectPurchaseOrderMainById(id);
        mmap.put("purchaseOrderMain", purchaseOrderMain);
        return prefix + "/edit";
    }

    /**
     * 修改保存采购单并发起申请
     */
    @RequiresPermissions("system:purchaseOrder:edit")
    @Log(title = "采购单主表", businessType = BusinessType.OTHER)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult editSave(PurchaseOrderMain purchaseOrderMain)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        purchaseOrderMain.setUpdateBy(String.valueOf(sysUser.getUserId()));
        int i = purchaseOrderMainService.updatePurchaseOrderMain(purchaseOrderMain);
        PurchaseOrderMain newPurchaseOrderMain = purchaseOrderMainService.selectPurchaseOrderMainById(purchaseOrderMain.getId());
        //发起审批
        PurchaseOrderForm purchaseOrderForm=new PurchaseOrderForm();
        BeanUtils.copyProperties(newPurchaseOrderMain,purchaseOrderForm);
        purchaseOrderForm.setBusinessKey(purchaseOrderMain.getOrderNo());
        purchaseOrderForm.setStartUserId(String.valueOf(sysUser.getUserId()));
        purchaseOrderForm.setBusVarUrl("/system/purchaseOrder/detail");
        ProcessInstance processInstance = flowableService.startProcessInstanceByAppForm(purchaseOrderForm);
        //推进任务节点
        flowableTaskService.automaticTask(processInstance.getProcessInstanceId());
        return toAjax(i);
    }

    /**
     * 删除采购单主表
     */
    @RequiresPermissions("system:purchaseOrder:remove")
    @Log(title = "采购单主表", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(purchaseOrderMainService.deletePurchaseOrderMainByIds(ids));
    }


    /**
     * 详情页
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, ModelMap mmap)
    {
        PurchaseOrderMain purchaseOrderMain = purchaseOrderMainService.selectPurchaseOrderMainById(id);
        mmap.put("purchaseOrder", purchaseOrderMain);
        return prefix + "/detail";
    }

    @GetMapping("/detailByOrderNo/{orderNo}")
    public String detailByOrderNo(@PathVariable("orderNo") String orderNo, ModelMap mmap)
    {
        PurchaseOrderMain purchaseOrderMain = purchaseOrderMainService.selectPurchaseOrderMainByOrderNo(orderNo);
        mmap.put("purchaseOrder", purchaseOrderMain);
        return prefix + "/detail";
    }


    /**
     * 重新申请
     */
    @PostMapping("/restart")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult restart(PurchaseOrderMainTask purchaseOrderMainTask)
    {
        PurchaseOrderMain purchaseOrderMain=new PurchaseOrderMain();
        SysUser sysUser = ShiroUtils.getSysUser();
        BeanUtils.copyProperties(purchaseOrderMainTask,purchaseOrderMain);
        purchaseOrderMain.setUpdateBy(String.valueOf(sysUser.getUserId()));
        int i = purchaseOrderMainService.updatePurchaseOrderMain(purchaseOrderMain);

        //完成任务
        purchaseOrderMainTask.setUserId(String.valueOf(sysUser.getUserId()));
        purchaseOrderMainTask.setIsUpdateBus(true);
        purchaseOrderMainTask.setIsStart(purchaseOrderMainTask.getIsPass());
        flowableTaskService.submitTask(purchaseOrderMainTask);
        return toAjax(i);
    }



    /**
     * 出纳审核
     */
    @PostMapping("/cashierTask")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult cashierTask(PurchaseCashierTask purchaseCashierTask)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        //完成任务
        purchaseCashierTask.setUserId(String.valueOf(sysUser.getUserId()));
        purchaseCashierTask.setIsUpdateBus(true);
        purchaseCashierTask.setIsStart(purchaseCashierTask.getIsPass());
        flowableTaskService.submitTask(purchaseCashierTask);
        return AjaxResult.success();
    }
}
