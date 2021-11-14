package com.snow.web.controller.system;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.snow.common.annotation.Log;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.framework.excel.FinanceAlipayFlowListener;
import com.snow.framework.excel.FinanceWeChartFlowListener;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.FinanceAlipayFlow;
import com.snow.system.domain.FinanceAlipayFlowImport;
import com.snow.system.domain.FinanceWeChatFlowImport;
import com.snow.system.domain.SysUser;
import com.snow.system.service.IFinanceAlipayFlowService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 财务支付宝流水Controller
 * 
 * @author snow
 * @date 2020-11-09
 */
@Controller
@RequestMapping("/system/flow")
public class FinanceFlowController extends BaseController
{
    private String prefix = "system/flow";

    @Autowired
    private IFinanceAlipayFlowService financeAlipayFlowService;


    @RequiresPermissions("system:flow:view")
    @GetMapping()
    public String flow()
    {
        return prefix + "/flow";
    }

    /**
     * 查询财务支付宝流水列表
     */
    @RequiresPermissions("system:flow:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FinanceAlipayFlow financeAlipayFlow)
    {
        SysUser user = ShiroUtils.getSysUser();
        startPage();
        if(!user.isAdmin()){
            financeAlipayFlow.setBelongUserId(user.getUserId());
        }
        List<FinanceAlipayFlow> list = financeAlipayFlowService.selectFinanceAlipayFlowList(financeAlipayFlow);
        return getDataTable(list);
    }

    /**
     * 导出财务支付宝流水列表
     */
    @RequiresPermissions("system:flow:export")
    @Log(title = "财务支付宝流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FinanceAlipayFlow financeAlipayFlow)
    {
        List<FinanceAlipayFlow> list = financeAlipayFlowService.selectFinanceAlipayFlowList(financeAlipayFlow);
        ExcelUtil<FinanceAlipayFlow> util = new ExcelUtil<FinanceAlipayFlow>(FinanceAlipayFlow.class);
        return util.exportExcel(list, "flow");
    }

    /**
     * 新增财务支付宝流水
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存财务支付宝流水
     */
    @RequiresPermissions("system:flow:add")
    @Log(title = "财务支付宝流水", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FinanceAlipayFlow financeAlipayFlow)
    {
        return toAjax(financeAlipayFlowService.insertFinanceAlipayFlow(financeAlipayFlow));
    }

    /**
     * 修改财务支付宝流水
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinanceAlipayFlow financeAlipayFlow = financeAlipayFlowService.selectFinanceAlipayFlowById(id);
        mmap.put("financeAlipayFlow", financeAlipayFlow);
        return prefix + "/edit";
    }

    /**
     * 修改保存财务支付宝流水
     */
    @RequiresPermissions("system:flow:edit")
    @Log(title = "财务支付宝流水", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FinanceAlipayFlow financeAlipayFlow)
    {
        return toAjax(financeAlipayFlowService.updateFinanceAlipayFlow(financeAlipayFlow));
    }

    /**
     * 删除财务支付宝流水
     */
    @RequiresPermissions("system:flow:remove")
    @Log(title = "财务支付宝流水", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(financeAlipayFlowService.deleteFinanceAlipayFlowByIds(ids));
    }


    @Log(title = "财务账单流水", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:flow:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, String tradeRealName,String tradeAccount,String billType) throws Exception
    {
        SysUser sysUser = ShiroUtils.getSysUser();

        if(billType.equals("1")){
            FinanceAlipayFlowListener financeAlipayFlowListener = new FinanceAlipayFlowListener(financeAlipayFlowService, sysUser, tradeAccount,tradeRealName,Integer.parseInt(billType));
            ExcelReader excelReader = EasyExcel.read(file.getInputStream(), FinanceAlipayFlowImport.class, financeAlipayFlowListener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0)
                    // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
                    .headRowNumber(5).build();
            excelReader.read(readSheet);
            // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
            excelReader.finish();
        }else if(billType.equals("2")){
            FinanceWeChartFlowListener financeWeChartFlowListener = new FinanceWeChartFlowListener(financeAlipayFlowService, sysUser, tradeAccount,tradeRealName,Integer.parseInt(billType));
            ExcelReader excelReader = EasyExcel.read(file.getInputStream(), FinanceWeChatFlowImport.class, financeWeChartFlowListener).build();
            //微信账单是从第十五行开始读的
            ReadSheet readSheet = EasyExcel.readSheet(0)
                    // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
                    .headRowNumber(17).build();
            excelReader.read(readSheet);
            // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
            excelReader.finish();
        }


        return AjaxResult.success("导入成功");
    }

}
