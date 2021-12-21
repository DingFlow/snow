package com.snow.web.controller.system;

import java.util.List;
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
import com.snow.system.domain.SysDingtalkSyncLog;
import com.snow.system.service.ISysDingtalkSyncLogService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 钉钉同步日志记录Controller
 * 
 * @author snow
 * @date 2020-11-13
 */
@Controller
@RequestMapping("/system/log")
public class SysSyncLogController extends BaseController
{
    private String prefix = "system/log";

    @Autowired
    private ISysDingtalkSyncLogService sysDingtalkSyncLogService;

    @RequiresPermissions("system:log:view")
    @GetMapping()
    public String log()
    {
        return prefix + "/log";
    }

    /**
     * 查询钉钉同步日志记录列表
     */
    @RequiresPermissions("system:log:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysDingtalkSyncLog sysDingtalkSyncLog)
    {
        startPage();
        List<SysDingtalkSyncLog> list = sysDingtalkSyncLogService.selectSysDingtalkSyncLogList(sysDingtalkSyncLog);
        return getDataTable(list);
    }

    /**
     * 导出钉钉同步日志记录列表
     */
    @RequiresPermissions("system:log:export")
    @Log(title = "钉钉同步日志记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysDingtalkSyncLog sysDingtalkSyncLog)
    {
        List<SysDingtalkSyncLog> list = sysDingtalkSyncLogService.selectSysDingtalkSyncLogList(sysDingtalkSyncLog);
        ExcelUtil<SysDingtalkSyncLog> util = new ExcelUtil<SysDingtalkSyncLog>(SysDingtalkSyncLog.class);
        return util.exportExcel(list, "log");
    }
    /**
     * 新增钉钉同步日志记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存钉钉同步日志记录
     */
    @RequiresPermissions("system:log:add")
    @Log(title = "钉钉同步日志记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysDingtalkSyncLog sysDingtalkSyncLog)
    {
        return toAjax(sysDingtalkSyncLogService.insertSysDingtalkSyncLog(sysDingtalkSyncLog));
    }

    /**
     * 修改钉钉同步日志记录
     */
    @GetMapping("/edit/{logId}")
    public String edit(@PathVariable("logId") Long logId, ModelMap mmap)
    {
        SysDingtalkSyncLog sysDingtalkSyncLog = sysDingtalkSyncLogService.selectSysDingtalkSyncLogById(logId);
        mmap.put("sysDingtalkSyncLog", sysDingtalkSyncLog);
        return prefix + "/edit";
    }

    /**
     * 修改保存钉钉同步日志记录
     */
    @RequiresPermissions("system:log:edit")
    @Log(title = "钉钉同步日志记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysDingtalkSyncLog sysDingtalkSyncLog)
    {
        return toAjax(sysDingtalkSyncLogService.updateSysDingtalkSyncLog(sysDingtalkSyncLog));
    }

    /**
     * 删除钉钉同步日志记录
     */
    @RequiresPermissions("system:log:remove")
    @Log(title = "钉钉同步日志记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysDingtalkSyncLogService.deleteSysDingtalkSyncLogByIds(ids));
    }

    @RequiresPermissions("system:log:detail")
    @GetMapping("/detail/{operId}")
    public String detail(@PathVariable("operId") Long operId, ModelMap mmap)
    {
        mmap.put("operLog", sysDingtalkSyncLogService.selectSysDingtalkSyncLogById(operId));
        return prefix + "/detail";
    }
}
