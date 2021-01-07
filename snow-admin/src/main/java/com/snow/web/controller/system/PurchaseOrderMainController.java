package com.snow.web.controller.system;

import java.io.IOException;
import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.snow.common.constant.SequenceContants;
import com.snow.framework.excel.FinanceAlipayFlowListener;
import com.snow.framework.excel.PurchaseOrderListener;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.FinanceAlipayFlowImport;
import com.snow.system.domain.PurchaseOrderImport;
import com.snow.system.domain.SysUser;
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
import com.snow.system.domain.PurchaseOrderMain;
import com.snow.system.service.IPurchaseOrderMainService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 采购单主表Controller
 * 
 * @author snow
 * @date 2021-01-07
 */
@Controller
@RequestMapping("/system/purchaseOrder")
public class PurchaseOrderMainController extends BaseController
{
    private String prefix = "system/purchaseOrder";

    @Autowired
    private IPurchaseOrderMainService purchaseOrderMainService;

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
        List<PurchaseOrderMain> list = purchaseOrderMainService.selectPurchaseOrderMainList(purchaseOrderMain);
        return getDataTable(list);
    }

    /**
     * 导出采购单主表列表
     */
    @RequiresPermissions("system:purchaseOrder:export")
    @Log(title = "采购单主表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PurchaseOrderMain purchaseOrderMain)
    {
        List<PurchaseOrderMain> list = purchaseOrderMainService.selectPurchaseOrderMainList(purchaseOrderMain);
        ExcelUtil<PurchaseOrderMain> util = new ExcelUtil<PurchaseOrderMain>(PurchaseOrderMain.class);
        return util.exportExcel(list, "purchaseOrder");
    }

    /**
     * 新增采购单主表
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        String newSequenceNo = sequenceService.getNewSequenceNo(SequenceContants.OA_PURCHASE_SEQUENCE);
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
     * 修改保存采购单主表
     */
    @RequiresPermissions("system:purchaseOrder:edit")
    @Log(title = "采购单主表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(PurchaseOrderMain purchaseOrderMain)
    {
        return toAjax(purchaseOrderMainService.updatePurchaseOrderMain(purchaseOrderMain));
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
}
