package com.snow.web.controller.system;

import java.util.List;

import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
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
import com.snow.system.domain.SysNewsTriggers;
import com.snow.system.service.ISysNewsTriggersService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 消息通知配置Controller
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
@Controller
@RequestMapping("/system/triggers")
public class SysNewsTriggersController extends BaseController
{
    private String prefix = "system/triggers";

    @Autowired
    private ISysNewsTriggersService sysNewsTriggersService;

    @RequiresPermissions("system:triggers:view")
    @GetMapping()
    public String triggers()
    {
        return prefix + "/triggers";
    }

    /**
     * 查询消息通知配置列表
     */
    @RequiresPermissions("system:triggers:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysNewsTriggers sysNewsTriggers)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysNewsTriggers.setUserId(String.valueOf(sysUser.getUserId()));
        List<SysNewsTriggers> list = sysNewsTriggersService.selectSysNewsTriggersList(sysNewsTriggers);
        return getDataTable(list);
    }

    /**
     * 导出消息通知配置列表
     */
    @RequiresPermissions("system:triggers:export")
    @Log(title = "消息通知配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysNewsTriggers sysNewsTriggers)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysNewsTriggers.setUserId(String.valueOf(sysUser.getUserId()));

        List<SysNewsTriggers> list = sysNewsTriggersService.selectSysNewsTriggersList(sysNewsTriggers);
        ExcelUtil<SysNewsTriggers> util = new ExcelUtil<SysNewsTriggers>(SysNewsTriggers.class);
        return util.exportExcel(list, "triggers");
    }

    /**
     * 新增消息通知配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存消息通知配置
     */
    @RequiresPermissions("system:triggers:add")
    @Log(title = "消息通知配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysNewsTriggers sysNewsTriggers)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysNewsTriggers.setUserId(String.valueOf(sysUser.getUserId()));
        sysNewsTriggers.setNewsOnOff(null);
        List<SysNewsTriggers> sysNewsTriggersList = sysNewsTriggersService.selectSysNewsTriggersList(sysNewsTriggers);
        if(CollectionUtils.isNotEmpty(sysNewsTriggersList)){
            return AjaxResult.error("该配置已存在，请勿重复添加");
        }
        sysNewsTriggers.setNewsOnOff(sysNewsTriggers.getNewsOnOff());
        sysNewsTriggers.setCreateBy(String.valueOf(sysUser.getUserId()));
        return toAjax(sysNewsTriggersService.insertSysNewsTriggers(sysNewsTriggers));
    }

    /**
     * 修改消息通知配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SysNewsTriggers sysNewsTriggers = sysNewsTriggersService.selectSysNewsTriggersById(id);
        mmap.put("sysNewsTriggers", sysNewsTriggers);
        return prefix + "/edit";
    }


    /**
     * 删除消息通知配置
     */
    @RequiresPermissions("system:triggers:remove")
    @Log(title = "消息通知配置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysNewsTriggersService.deleteSysNewsTriggersByIds(ids));
    }


    /**
     * 开启关闭通知
     */
    @RequiresPermissions("system:triggers:changeStatus")
    @Log(title = "开启关闭通知", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysNewsTriggers sysNewsTriggers)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        sysNewsTriggers.setUpdateBy(String.valueOf(sysUser.getUserId()));
        return toAjax(sysNewsTriggersService.updateSysNewsTriggers(sysNewsTriggers));
    }
}
