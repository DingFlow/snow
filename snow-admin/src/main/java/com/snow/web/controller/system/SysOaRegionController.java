package com.snow.web.controller.system;

import java.util.List;

import com.snow.common.core.page.TableDataInfo;
import com.snow.system.domain.RegionTreeVO;
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
import com.snow.system.domain.SysOaRegion;
import com.snow.system.service.ISysOaRegionService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.utils.StringUtils;
import com.snow.common.core.domain.Ztree;

/**
 * 地区Controller
 * 
 * @author 没用的阿吉
 * @date 2021-04-09
 */
@Controller
@RequestMapping("/system/region")
public class SysOaRegionController extends BaseController
{
    private String prefix = "system/region";

    @Autowired
    private ISysOaRegionService sysOaRegionService;

    @RequiresPermissions("system:region:view")
    @GetMapping()
    public String region()
    {
        return prefix + "/area";
    }

    /**
     * 查询地区树列表
     */
    @RequiresPermissions("system:region:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysOaRegion sysOaRegion)
    {
        startPage();
        if(StringUtils.isEmpty(sysOaRegion.getName())){
            List<SysOaRegion> list = sysOaRegionService.selectSysOaRegionList(sysOaRegion);
            return getDataTable(list);
        }else {
            SysOaRegion sysOaRegionName=new SysOaRegion();
            sysOaRegionName.setName(sysOaRegion.getName());
            List<SysOaRegion> list = sysOaRegionService.selectSysOaRegionList(sysOaRegionName);
            return getDataTable(list);
        }
    }

    /**
     * 导出地区列表
     */
    @RequiresPermissions("system:region:export")
    @Log(title = "地区", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOaRegion sysOaRegion)
    {
        List<SysOaRegion> list = sysOaRegionService.selectSysOaRegionList(sysOaRegion);
        ExcelUtil<SysOaRegion> util = new ExcelUtil<SysOaRegion>(SysOaRegion.class);
        return util.exportExcel(list, "region");
    }

    /**
     * 新增地区
     */
    @GetMapping(value = { "/add/{code}", "/add" })
    public String add(@PathVariable(value = "code", required = false) Long code, ModelMap mmap)
    {
        if (StringUtils.isNotNull(code))
        {
            mmap.put("sysOaRegion", sysOaRegionService.selectSysOaRegionById(code));
        }
        return prefix + "/add";
    }

    /**
     * 新增保存地区
     */
    @RequiresPermissions("system:region:add")
    @Log(title = "地区", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysOaRegion sysOaRegion)
    {
        return toAjax(sysOaRegionService.insertSysOaRegion(sysOaRegion));
    }

    /**
     * 修改地区
     */
    @GetMapping("/edit/{code}")
    public String edit(@PathVariable("code") Long code, ModelMap mmap)
    {
        SysOaRegion sysOaRegion = sysOaRegionService.selectSysOaRegionById(code);
        mmap.put("sysOaRegion", sysOaRegion);
        return prefix + "/edit";
    }

    /**
     * 修改保存地区
     */
    @RequiresPermissions("system:region:edit")
    @Log(title = "地区", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysOaRegion sysOaRegion)
    {
        return toAjax(sysOaRegionService.updateSysOaRegion(sysOaRegion));
    }

    /**
     * 删除
     */
    @RequiresPermissions("system:region:remove")
    @Log(title = "地区", businessType = BusinessType.DELETE)
    @GetMapping("/remove/{code}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("code") Long code)
    {
        return toAjax(sysOaRegionService.deleteSysOaRegionById(code));
    }

    /**
     * 选择地区树
     */
    @GetMapping(value = { "/selectRegionTree/{code}", "/selectRegionTree/" })
    public String selectRegionTree(@PathVariable(value = "code", required = false) Long code, ModelMap mmap)
    {
        if (StringUtils.isNotNull(code))
        {
            mmap.put("sysOaRegion", sysOaRegionService.selectSysOaRegionById(code));
        }
        return prefix + "/tree";
    }

    /**
     * 加载地区树列表
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = sysOaRegionService.selectSysOaRegionTree();
        return ztrees;
    }


    /**
     * 加载地区树列表
     */
    @GetMapping("/getTreeData")
    @ResponseBody
    public List<RegionTreeVO> getTreeData()
    {
        List<RegionTreeVO> sysOaRegionTree = sysOaRegionService.getSysOaRegionTree();
        return sysOaRegionTree;
    }
}
