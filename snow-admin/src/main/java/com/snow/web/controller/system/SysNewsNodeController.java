package com.snow.web.controller.system;

import java.util.List;

import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysUser;
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
import com.snow.system.domain.SysNewsNode;
import com.snow.system.service.ISysNewsNodeService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.utils.StringUtils;
import com.snow.common.core.domain.Ztree;

/**
 * 消息配置节点Controller
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
@Controller
@RequestMapping("/system/node")
public class SysNewsNodeController extends BaseController
{
    private String prefix = "system/node";

    private String trggers_prefix = "system/triggers";

    @Autowired
    private ISysNewsNodeService sysNewsNodeService;

    @RequiresPermissions("system:node:view")
    @GetMapping()
    public String node()
    {
        return prefix + "/node";
    }

    /**
     * 查询消息配置节点树列表
     */
    @RequiresPermissions("system:node:list")
    @PostMapping("/list")
    @ResponseBody
    public List<SysNewsNode> list(SysNewsNode sysNewsNode)
    {
        List<SysNewsNode> list = sysNewsNodeService.selectSysNewsNodeList(sysNewsNode);
        return list;
    }

    /**
     * 导出消息配置节点列表
     */
    @RequiresPermissions("system:node:export")
    @Log(title = "消息配置节点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysNewsNode sysNewsNode)
    {
        List<SysNewsNode> list = sysNewsNodeService.selectSysNewsNodeList(sysNewsNode);
        ExcelUtil<SysNewsNode> util = new ExcelUtil<SysNewsNode>(SysNewsNode.class);
        return util.exportExcel(list, "node");
    }

    /**
     * 新增消息配置节点
     */
    @GetMapping(value = {"/add/{id}","/add/"})
    public String add(@PathVariable(value = "id", required = false) Long id, ModelMap mmap)
    {
        if (StringUtils.isNotNull(id))
        {
            mmap.put("sysNewsNode", sysNewsNodeService.selectSysNewsNodeById(id.intValue()));
        }
        return prefix + "/add";
    }

    /**
     * 新增保存消息配置节点
     */
    @RequiresPermissions("system:node:add")
    @Log(title = "消息配置节点", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysNewsNode sysNewsNode)
    {
        SysNewsNode sysNewsNodeKey = sysNewsNodeService.selectSysNewsNodeByKey(sysNewsNode.getNewsNodeKey(),sysNewsNode.getParentId());
        if (sysNewsNodeKey!=null) {
            return AjaxResult.error("该父节下配置节点key已存在");
        }
        SysUser sysUser = ShiroUtils.getSysUser();
        sysNewsNode.setCreateBy(String.valueOf(sysUser.getUserId()));
        return toAjax(sysNewsNodeService.insertSysNewsNode(sysNewsNode));
    }

    /**
     * 修改消息配置节点
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SysNewsNode sysNewsNode = sysNewsNodeService.selectSysNewsNodeById(id);
        mmap.put("sysNewsNode", sysNewsNode);
        return prefix + "/edit";
    }

    /**
     * 修改保存消息配置节点
     */
    @RequiresPermissions("system:node:edit")
    @Log(title = "消息配置节点", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysNewsNode sysNewsNode)
    {
        String newsNodeKey = sysNewsNode.getNewsNodeKey();
        SysNewsNode sysNewsNodeKey = sysNewsNodeService.selectSysNewsNodeByKey(newsNodeKey,sysNewsNode.getParentId());
        if (sysNewsNodeKey!=null&&!sysNewsNodeKey.getNewsNodeKey().equals(newsNodeKey)) {
            return AjaxResult.error("该配置节点key已存在");
        }
        SysUser sysUser = ShiroUtils.getSysUser();
        sysNewsNode.setUpdateBy(String.valueOf(sysUser.getUserId()));
        return toAjax(sysNewsNodeService.updateSysNewsNode(sysNewsNode));
    }

    /**
     * 删除
     */
    @RequiresPermissions("system:node:remove")
    @Log(title = "消息配置节点", businessType = BusinessType.DELETE)
    @GetMapping("/remove/{id}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("id") Integer id)
    {
        return toAjax(sysNewsNodeService.deleteSysNewsNodeById(id));
    }

    /**
     * 选择消息配置节点树
     */
    @GetMapping(value = { "/selectNodeTree/{id}", "/selectNodeTree/" })
    public String selectNodeTree(@PathVariable(value = "id", required = false) Long id, ModelMap mmap)
    {
        if (StringUtils.isNotNull(id))
        {
            mmap.put("sysNewsNode", sysNewsNodeService.selectSysNewsNodeById(id.intValue()));
        }
        return prefix + "/tree";
    }

    /**
     * 加载消息配置节点树列表
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = sysNewsNodeService.selectSysNewsNodeTree();
        return ztrees;
    }


    /**
     * 选择消息触发节点树
     */
    @GetMapping(value = { "/selectTriggerTree/{id}", "/selectTriggerTree/" })
    public String selectTriggerTree(@PathVariable(value = "id", required = false) Long id, ModelMap mmap)
    {
        if (StringUtils.isNotNull(id))
        {
            mmap.put("sysNewsNode", sysNewsNodeService.selectSysNewsNodeById(id.intValue()));
        }
        return trggers_prefix + "/tree";
    }
}
