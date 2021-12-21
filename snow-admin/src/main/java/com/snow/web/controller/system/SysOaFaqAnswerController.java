package com.snow.web.controller.system;

import java.util.List;

import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysOaFaq;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysOaFaqService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.snow.system.domain.SysOaFaqAnswer;
import com.snow.system.service.ISysOaFaqAnswerService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * faq问题答案Controller
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
@Controller
@RequestMapping("/system/answer")
public class SysOaFaqAnswerController extends BaseController
{
    private String prefix = "system/answer";

    @Autowired
    private ISysOaFaqAnswerService sysOaFaqAnswerService;

    @Autowired
    private ISysOaFaqService sysOaFaqService;

    @RequiresPermissions("system:answer:view")
    @GetMapping()
    public String answer()
    {
        return prefix + "/myanswerlist";
    }

    /**
     * 查询我的faq问题答案列表
     */
    @RequiresPermissions("system:answer:myList")
    @PostMapping("/myList")
    @ResponseBody
    public TableDataInfo myList(SysOaFaqAnswer sysOaFaqAnswer)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaFaqAnswer.setCreateBy(String.valueOf(sysUser.getUserId()));
        List<SysOaFaqAnswer> list = sysOaFaqAnswerService.selectSysOaFaqAnswerList(sysOaFaqAnswer);
        return getDataTable(list);
    }

    /**
     * 导出faq问题答案列表
     */
    @RequiresPermissions("system:answer:export")
    @Log(title = "faq问题答案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOaFaqAnswer sysOaFaqAnswer)
    {
        List<SysOaFaqAnswer> list = sysOaFaqAnswerService.selectSysOaFaqAnswerList(sysOaFaqAnswer);
        ExcelUtil<SysOaFaqAnswer> util = new ExcelUtil<SysOaFaqAnswer>(SysOaFaqAnswer.class);
        return util.exportExcel(list, "answer");
    }

    /**
     * 新增faq问题答案
     */
    @GetMapping("/add")
    public String add(String faqNo, ModelMap mmap)
    {
        mmap.put("faqNo",faqNo);
        return prefix + "/add";
    }

    /**
     * 新增保存faq问题答案
     */
    @RequiresPermissions("system:answer:add")
    @Log(title = "faq问题答案", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSave(SysOaFaqAnswer sysOaFaqAnswer)
    {

        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaFaqAnswer.setCreateBy(String.valueOf(sysUser.getUserId()));
        sysOaFaqAnswer.setUpdateBy(String.valueOf(sysUser.getUserId()));
        //同时更新问题表状态
        SysOaFaq sysOaFaq=new SysOaFaq();
        sysOaFaq.setFaqNo(sysOaFaqAnswer.getFaqNo());
        sysOaFaq.setFaqStatus(1);
        sysOaFaqService.updateSysOaFaq(sysOaFaq);
        return toAjax(sysOaFaqAnswerService.insertSysOaFaqAnswer(sysOaFaqAnswer));
    }

    /**
     * 修改faq问题答案
     */
    @GetMapping("/edit")
    public String edit(Integer id, ModelMap mmap)
    {
        SysOaFaqAnswer sysOaFaqAnswer = sysOaFaqAnswerService.selectSysOaFaqAnswerById(id);
        mmap.put("sysOaFaqAnswer", sysOaFaqAnswer);
        return prefix + "/edit";
    }

    /**
     * 修改保存faq问题答案
     */
    @RequiresPermissions("system:answer:edit")
    @Log(title = "faq问题答案", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysOaFaqAnswer sysOaFaqAnswer)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaFaqAnswer.setUpdateBy(String.valueOf(sysUser.getUserId()));
        return toAjax(sysOaFaqAnswerService.updateSysOaFaqAnswer(sysOaFaqAnswer));
    }

    /**
     * 删除faq问题答案
     */
    @RequiresPermissions("system:answer:remove")
    @Log(title = "faq问题答案", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(Integer id)
    {
        return toAjax(sysOaFaqAnswerService.deleteSysOaFaqAnswerByFaqId(id));
    }

    /**
     * 详情faq问题答案
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:answer:detail")
    public String detail(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SysOaFaqAnswer sysOaFaqAnswer = sysOaFaqAnswerService.selectSysOaFaqAnswerById(id);
        mmap.put("sysOaFaqAnswer", sysOaFaqAnswer);
        return prefix + "/detail";
    }



    /**
     * 修改faq问题答案
     */
    @GetMapping("/myedit")
    public String myedit(Integer id, ModelMap mmap)
    {
        SysOaFaqAnswer sysOaFaqAnswer = sysOaFaqAnswerService.selectSysOaFaqAnswerById(id);
        mmap.put("sysOaFaqAnswer", sysOaFaqAnswer);
        return prefix + "/myedit";
    }

    /**
     * 修改保存faq问题答案
     */
    @RequiresPermissions("system:answer:myedit")
    @Log(title = "faq问题答案", businessType = BusinessType.UPDATE)
    @PostMapping("/myedit")
    @ResponseBody
    public AjaxResult myEditSave(SysOaFaqAnswer sysOaFaqAnswer)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaFaqAnswer.setUpdateBy(String.valueOf(sysUser.getUserId()));
        return toAjax(sysOaFaqAnswerService.updateSysOaFaqAnswer(sysOaFaqAnswer));
    }

}
