package com.snow.web.controller.system;

import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.snow.framework.excel.FinanceAlipayFlowListener;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.FinanceAlipayFlowImport;
import com.snow.system.domain.SysUser;
import com.snow.system.mapper.FinanceAlipayFlowMapper;
import com.snow.system.mapper.SysUserMapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.snow.common.annotation.Log;
import com.snow.common.enums.BusinessType;
import com.snow.system.domain.FinanceAlipayFlow;
import com.snow.system.service.IFinanceAlipayFlowService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

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


    @Log(title = "财务支付宝流水", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:flow:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, String tradeRealName,String tradeAccount,String billType) throws Exception
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        FinanceAlipayFlowListener financeAlipayFlowListener = new FinanceAlipayFlowListener(financeAlipayFlowService, sysUser, tradeAccount,tradeRealName,Integer.parseInt(billType));
        ExcelReader excelReader = EasyExcel.read(file.getInputStream(), FinanceAlipayFlowImport.class, financeAlipayFlowListener).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
        return AjaxResult.success("导入成功");
    }
}
