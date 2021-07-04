package com.snow.web.controller.system;

import com.snow.common.annotation.Log;
import com.snow.common.constant.SequenceConstants;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysOaFaq;
import com.snow.system.domain.SysOaFaqAnswer;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysOaFaqAnswerService;
import com.snow.system.service.ISysOaFaqService;
import com.snow.system.service.ISysSequenceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * faq问题Controller
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
@Controller
@RequestMapping("/system/faq")
public class SysOaFaqController extends BaseController
{
    private String prefix = "system/faq";

    @Autowired
    private ISysSequenceService sequenceService;


    @Autowired
    private ISysOaFaqAnswerService sysOaFaqAnswerService;

    @Autowired
    private ISysOaFaqService sysOaFaqService;

    @RequiresPermissions("system:faq:view")
    @GetMapping()
    public String faq()
    {
        return prefix + "/faq";
    }

    /**
     * 查询faq问题列表
     */
    @RequiresPermissions("system:faq:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysOaFaq sysOaFaq)
    {
        startPage();
        List<SysOaFaq> list = sysOaFaqService.selectSysOaFaqList(sysOaFaq);
        return getDataTable(list);
    }

    /**
     * 导出faq问题列表
     */
    @RequiresPermissions("system:faq:export")
    @Log(title = "faq问题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOaFaq sysOaFaq)
    {
        List<SysOaFaq> list = sysOaFaqService.selectSysOaFaqList(sysOaFaq);
        ExcelUtil<SysOaFaq> util = new ExcelUtil<SysOaFaq>(SysOaFaq.class);
        return util.exportExcel(list, "faq");
    }

    /**
     * 新增faq问题
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存faq问题
     */
    @RequiresPermissions("system:faq:add")
    @Log(title = "faq问题", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysOaFaq sysOaFaq)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaFaq.setCreateBy(String.valueOf(sysUser.getUserId()));
        sysOaFaq.setUpdateBy(String.valueOf(sysUser.getUserId()));
        String newSequenceNo = sequenceService.getNewSequenceNo(SequenceConstants.OA_FAQ_SEQUENCE);
        sysOaFaq.setFaqNo(newSequenceNo);
        return toAjax(sysOaFaqService.insertSysOaFaq(sysOaFaq));
    }

    /**
     * 修改faq问题
     */
    @GetMapping("/edit/{faqNo}")
    public String edit(@PathVariable("faqNo") String faqNo, ModelMap mmap)
    {
        SysOaFaq sysOaFaq = sysOaFaqService.selectSysOaFaqById(faqNo);
        mmap.put("sysOaFaq", sysOaFaq);
        return prefix + "/edit";
    }

    /**
     * 修改保存faq问题
     */
    @RequiresPermissions("system:faq:edit")
    @Log(title = "faq问题", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysOaFaq sysOaFaq)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaFaq.setUpdateBy(String.valueOf(sysUser.getUserId()));
        return toAjax(sysOaFaqService.updateSysOaFaq(sysOaFaq));
    }


    @RequiresPermissions("system:answer:sysOaFaqAnswerList")
    @PostMapping("/sysOaFaqAnswerList")
    @ResponseBody
    public TableDataInfo sysOaFaqAnswerList(SysOaFaqAnswer sysOaFaqAnswer)
    {
        startPage();
        List<SysOaFaqAnswer> sysOaFaqAnswerList = sysOaFaqAnswerService.selectSysOaFaqAnswerList(sysOaFaqAnswer);
        return getDataTable(sysOaFaqAnswerList);
    }


    /**
     * 删除客户
     */
    @RequiresPermissions("system:answer:remove")
    @Log(title = "faq问题", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysOaFaqService.deleteSysOaFaqByFaqNos(ids));
    }
}
