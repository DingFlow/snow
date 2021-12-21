package com.snow.web.controller.front;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageInfo;
import com.snow.common.annotation.Log;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/front/faq")
public class FrontFaqController extends BaseController {


    private String prefix = "front/faq";

    @Autowired
    private ISysOaFaqService sysOaFaqService;

    @Autowired
    private ISysOaFaqAnswerService sysOaFaqAnswerService;

    @RequiresPermissions("front:faq:pool")
    @GetMapping("/pool")
    public String faq(SysOaFaq sysOaFaq,ModelMap mmap) {
        startPage();
        List<SysOaFaq> list = sysOaFaqService.selectSysOaFaqList(sysOaFaq);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        TableDataInfo dataTable = getDataTable(list);
        mmap.put("dataTable",dataTable);
        return prefix + "/faqpool";
    }

    /**
     * 查询我的faq问题列表
     */
    @RequiresPermissions("front:faq:mypool")
    @GetMapping("/mypool")
    public String list(SysOaFaq sysOaFaq,ModelMap mmap)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysOaFaq.setCreateBy(String.valueOf(sysUser.getUserId()));
        List<SysOaFaq> list = sysOaFaqService.selectSysOaFaqList(sysOaFaq);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        TableDataInfo dataTable = getDataTable(list);
        mmap.put("dataTable",dataTable);
        return prefix + "/myfaqpool";
    }


    /**
     * 答案进行采纳打分
     */
    @PostMapping("/answerFraction")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult answerFraction(SysOaFaqAnswer sysOaFaqAnswer)
    {
        sysOaFaqAnswer.setAdoptTime(new Date());
        sysOaFaqAnswer.setIsAdopt(1L);
        sysOaFaqAnswerService.updateSysOaFaqAnswer(sysOaFaqAnswer);
        //其他都变成未被采纳
        List<SysOaFaqAnswer> sysOaFaqAnswerList = sysOaFaqAnswerService.selectSysOaFaqAnswerByFaqNo(sysOaFaqAnswer.getFaqNo());
        List<SysOaFaqAnswer> otherSysOaFaqAnswerList = sysOaFaqAnswerList.stream().filter(t -> !t.getId().equals(sysOaFaqAnswer.getId())).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(otherSysOaFaqAnswerList)){
            otherSysOaFaqAnswerList.forEach(
                    t->{
                        SysOaFaqAnswer otherSysOaFaqAnswer=new SysOaFaqAnswer();
                        otherSysOaFaqAnswer.setId(t.getId());
                        otherSysOaFaqAnswer.setIsAdopt(2L);
                        sysOaFaqAnswerService.updateSysOaFaqAnswer(otherSysOaFaqAnswer);
                    }
            );
        }
        //更新问题表
        SysOaFaq sysOaFaq=new SysOaFaq();
        sysOaFaq.setFaqNo(sysOaFaqAnswer.getFaqNo());
        //打过分后，问题状态变成已完结
        sysOaFaq.setFaqStatus(3);
        sysOaFaqService.updateSysOaFaq(sysOaFaq);
        return  AjaxResult.success();
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
        return toAjax(sysOaFaqService.insertSysOaFaq(sysOaFaq));
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
        return toAjax(sysOaFaqService.updateSysOaFaq(sysOaFaq));
    }


}
