package com.snow.web.controller.flowable;

import java.util.List;

import com.snow.common.constant.UserConstants;
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
import com.snow.system.domain.FlowGroupDO;
import com.snow.system.service.IFlowGroupDOService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.utils.StringUtils;
import com.snow.common.core.domain.Ztree;

/**
 * 流程组信息Controller
 * 
 * @author snow
 * @date 2020-12-19
 */
@Controller
@RequestMapping("/flow/role")
public class FlowGroupController extends BaseController
{
    private String prefix = "flow/role";

    @Autowired
    private IFlowGroupDOService flowGroupDOService;

    @RequiresPermissions("system:group:view")
    @GetMapping()
    public String role()
    {
        return prefix + "/role";
    }

    /**
     * 查询流程组信息树列表
     */
    @RequiresPermissions("system:group:list")
    @PostMapping("/list")
    @ResponseBody
    public List<FlowGroupDO> list(FlowGroupDO flowGroupDO)
    {
        flowGroupDO.setRoleType(UserConstants.FLOW_ROLE_TYPE);
        List<FlowGroupDO> list = flowGroupDOService.selectFlowGroupDOList(flowGroupDO);
        return list;
    }

    /**
     * 导出流程组信息列表
     */
    @RequiresPermissions("system:group:export")
    @Log(title = "流程组信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FlowGroupDO flowGroupDO)
    {
        flowGroupDO.setRoleType(UserConstants.FLOW_ROLE_TYPE);
        List<FlowGroupDO> list = flowGroupDOService.selectFlowGroupDOList(flowGroupDO);
        ExcelUtil<FlowGroupDO> util = new ExcelUtil<FlowGroupDO>(FlowGroupDO.class);
        return util.exportExcel(list, "role");
    }

    /**
     * 新增流程组信息
     */
    @GetMapping(value = { "/add/{roleId}", "/add/" })
    public String add(@PathVariable(value = "roleId", required = false) Long roleId, ModelMap mmap)
    {
        if (StringUtils.isNotNull(roleId))
        {
            mmap.put("flowGroupDO", flowGroupDOService.selectFlowGroupDOById(roleId));
        }
        return prefix + "/add";
    }

    /**
     * 新增保存流程组信息
     */
    @RequiresPermissions("system:group:add")
    @Log(title = "流程组信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FlowGroupDO flowGroupDO)
    {
        flowGroupDO.setRoleType(UserConstants.FLOW_ROLE_TYPE);
        return toAjax(flowGroupDOService.insertFlowGroupDO(flowGroupDO));
    }

    /**
     * 修改流程组信息
     */
    @GetMapping("/edit/{roleId}")
    public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        FlowGroupDO flowGroupDO = flowGroupDOService.selectFlowGroupDOById(roleId);
        mmap.put("flowGroupDO", flowGroupDO);
        return prefix + "/edit";
    }

    /**
     * 修改保存流程组信息
     */
    @RequiresPermissions("system:group:edit")
    @Log(title = "流程组信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FlowGroupDO flowGroupDO)
    {
        return toAjax(flowGroupDOService.updateFlowGroupDO(flowGroupDO));
    }

    /**
     * 删除
     */
    @RequiresPermissions("system:group:remove")
    @Log(title = "流程组信息", businessType = BusinessType.DELETE)
    @GetMapping("/remove/{roleId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("roleId") Long roleId)
    {
        return toAjax(flowGroupDOService.deleteFlowGroupDOById(roleId));
    }

    /**
     * 选择流程组信息树
     */
    @GetMapping(value = { "/selectRoleTree/{roleId}", "/selectRoleTree/" })
    public String selectRoleTree(@PathVariable(value = "roleId", required = false) Long roleId, ModelMap mmap)
    {
        if (StringUtils.isNotNull(roleId))
        {
            mmap.put("flowGroupDO", flowGroupDOService.selectFlowGroupDOById(roleId));
        }
        return prefix + "/tree";
    }

    /**
     * 加载流程组信息树列表
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = flowGroupDOService.selectFlowGroupDOTree();
        return ztrees;
    }
}
