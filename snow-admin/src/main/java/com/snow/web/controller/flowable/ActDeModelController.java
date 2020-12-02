package com.snow.web.controller.flowable;

import java.util.List;

import com.snow.flowable.service.impl.FlowablePublishServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.repository.Deployment;
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
import com.snow.system.domain.ActDeModel;
import com.snow.system.service.IActDeModelService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 设计器modelController
 * 
 * @author qimingjin
 * @date 2020-12-01
 */
@Controller
@RequestMapping("/system/model")
public class ActDeModelController extends BaseController
{
    private String prefix = "system/model";

    @Autowired
    private IActDeModelService actDeModelService;

    @Autowired
    private FlowablePublishServiceImpl flowablePublishServiceImpl;

    @RequiresPermissions("system:model:view")
    @GetMapping()
    public String model()
    {
        return prefix + "/model";
    }

    /**
     * 查询设计器model列表
     */
    @RequiresPermissions("system:model:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActDeModel actDeModel)
    {
        startPage();
        List<ActDeModel> list = actDeModelService.selectActDeModelList(actDeModel);
        return getDataTable(list);
    }

    /**
     * 导出设计器model列表
     */
    @RequiresPermissions("system:model:export")
    @Log(title = "设计器model", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ActDeModel actDeModel)
    {
        List<ActDeModel> list = actDeModelService.selectActDeModelList(actDeModel);
        ExcelUtil<ActDeModel> util = new ExcelUtil<ActDeModel>(ActDeModel.class);
        return util.exportExcel(list, "model");
    }

    /**
     * 新增设计器model
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存设计器model
     */
    @RequiresPermissions("system:model:add")
    @Log(title = "设计器model", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ActDeModel actDeModel)
    {
        return toAjax(actDeModelService.insertActDeModel(actDeModel));
    }

    /**
     * 修改设计器model
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        ActDeModel actDeModel = actDeModelService.selectActDeModelById(id);
        mmap.put("actDeModel", actDeModel);
        return redirect("/modeler/index.html#/editor/"+id);
    }

    /**
     * 修改保存设计器model
     */
    @RequiresPermissions("system:model:edit")
    @Log(title = "设计器model", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ActDeModel actDeModel)
    {
        return toAjax(actDeModelService.updateActDeModel(actDeModel));
    }

    /**
     * 删除设计器model
     */
    @RequiresPermissions("system:model:remove")
    @Log(title = "设计器model", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(actDeModelService.deleteActDeModelByIds(ids));
    }


    @Log(title = "发布流程", businessType = BusinessType.DEPLOYMENT)
    @RequiresPermissions("system:model:deployment")
    @PostMapping("/deployment")
    @ResponseBody
    public AjaxResult deployment(String id)
    {
        Deployment Deployment = flowablePublishServiceImpl.createBytesDeploymentByModelId(id);
        if(Deployment==null){
            return AjaxResult.error("modelId不存在");
        }
        return AjaxResult.success("发布成功");
    }
}
