package com.snow.web.controller.system;

import java.util.List;

import com.dingtalk.api.response.OapiCallBackGetCallBackFailedResultResponse;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.dingtalk.service.impl.CallBackServiceImpl;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.event.SyncEvent;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.snow.common.annotation.Log;
import com.snow.common.enums.BusinessType;
import com.snow.system.domain.DingtalkCallBack;
import com.snow.system.service.IDingtalkCallBackService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

import javax.annotation.Resource;

/**
 * 回调事件Controller
 * 
 * @author qimingjin
 * @date 2020-11-02
 */
@Controller
@RequestMapping("/system/back")
public class DingtalkCallBackController extends BaseController
{
    private String prefix = "system/back";

    @Autowired
    private IDingtalkCallBackService dingtalkCallBackService;

    @Resource
    private ApplicationContext applicationContext;

    @RequiresPermissions("system:back:view")
    @GetMapping()
    public String back()
    {
        return prefix + "/back";
    }

    /**
     * 查询回调事件列表
     */
    @RequiresPermissions("system:back:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DingtalkCallBack dingtalkCallBack)
    {
        startPage();
        List<DingtalkCallBack> list = dingtalkCallBackService.selectDingtalkCallBackList(dingtalkCallBack);
        return getDataTable(list);
    }

    /**
     * 导出回调事件列表
     */
    @RequiresPermissions("system:back:export")
    @Log(title = "回调事件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DingtalkCallBack dingtalkCallBack)
    {
        List<DingtalkCallBack> list = dingtalkCallBackService.selectDingtalkCallBackList(dingtalkCallBack);
        ExcelUtil<DingtalkCallBack> util = new ExcelUtil<DingtalkCallBack>(DingtalkCallBack.class);
        return util.exportExcel(list, "back");
    }

    /**
     * 新增回调事件
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存回调事件
     */
    @RequiresPermissions("system:back:add")
    @Log(title = "回调事件", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DingtalkCallBack dingtalkCallBack)
    {
        dingtalkCallBack.setCreateBy(ShiroUtils.getLoginName());
        dingtalkCallBack.setIsSyncDingTalk(false);
        return toAjax(dingtalkCallBackService.insertDingtalkCallBack(dingtalkCallBack));
    }

    @RequiresPermissions("system:back:register")
    @Log(title = "注册", businessType = BusinessType.INSERT)
    @GetMapping("/register")
    @ResponseBody
    public AjaxResult register(Long id)
    {
        DingtalkCallBack dingtalkCallBack = dingtalkCallBackService.selectDingtalkCallBackById(id);
        // 同步到dingding
        SyncEvent syncEvent = new SyncEvent(dingtalkCallBack, DingTalkListenerType.CALL_BACK_REGISTER);
        applicationContext.publishEvent(syncEvent);
        return AjaxResult.success();
    }

    /**
     * 修改回调事件
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        DingtalkCallBack dingtalkCallBack = dingtalkCallBackService.selectDingtalkCallBackById(id);
        mmap.put("dingtalkCallBack", dingtalkCallBack);
        return prefix + "/edit";
    }

    /**
     * 修改保存回调事件
     */
    @RequiresPermissions("system:back:edit")
    @Log(title = "回调事件", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DingtalkCallBack dingtalkCallBack)
    {
        dingtalkCallBack.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(dingtalkCallBackService.updateDingtalkCallBack(dingtalkCallBack));
    }

    /**
     * 删除回调事件
     */
    @RequiresPermissions("system:back:remove")
    @Log(title = "回调事件", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(dingtalkCallBackService.deleteDingtalkCallBackByIds(ids));
    }


}
