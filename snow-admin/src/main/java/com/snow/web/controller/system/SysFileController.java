package com.snow.web.controller.system;

import java.util.List;
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
import com.snow.system.domain.SysFile;
import com.snow.system.service.ISysFileService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 文件存储Controller
 * 
 * @author snow
 * @date 2021-01-11
 */
@Controller
@RequestMapping("/system/file")
public class SysFileController extends BaseController
{
    private String prefix = "system/file";

    @Autowired
    private ISysFileService sysFileService;

    @RequiresPermissions("system:file:view")
    @GetMapping()
    public String file()
    {
        return prefix + "/file";
    }

    /**
     * 查询文件存储列表
     */
    @RequiresPermissions("system:file:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFile sysFile)
    {
        startPage();
        List<SysFile> list = sysFileService.selectSysFileList(sysFile);
        return getDataTable(list);
    }

    /**
     * 导出文件存储列表
     */
    @RequiresPermissions("system:file:export")
    @Log(title = "文件存储", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFile sysFile)
    {
        List<SysFile> list = sysFileService.selectSysFileList(sysFile);
        ExcelUtil<SysFile> util = new ExcelUtil<SysFile>(SysFile.class);
        return util.exportExcel(list, "file");
    }

    /**
     * 新增文件存储
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存文件存储
     */
    @RequiresPermissions("system:file:add")
    @Log(title = "文件存储", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysFile sysFile)
    {
        return toAjax(sysFileService.insertSysFile(sysFile));
    }

    /**
     * 修改文件存储
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysFile sysFile = sysFileService.selectSysFileById(id);
        mmap.put("sysFile", sysFile);
        return prefix + "/edit";
    }

    /**
     * 修改保存文件存储
     */
    @RequiresPermissions("system:file:edit")
    @Log(title = "文件存储", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFile sysFile)
    {
        return toAjax(sysFileService.updateSysFile(sysFile));
    }

    /**
     * 删除文件存储
     */
    @RequiresPermissions("system:file:remove")
    @Log(title = "文件存储", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysFileService.deleteSysFileByIds(ids));
    }


    /**
     * 根据file key查询文件信息
     * @param fileKey
     * @return
     */
    @PostMapping( "/getFileInfoByKey")
    @ResponseBody
    public AjaxResult getFileInfoByKey(String fileKey)
    {
        SysFile sysFile = sysFileService.selectSysFileByKey(fileKey);
        return AjaxResult.success(sysFile);
    }
}
