package com.snow.web.controller.flowable;

import com.google.common.collect.Lists;
import com.snow.common.annotation.Log;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.flowable.service.impl.FlowablePublishServiceImpl;
import com.snow.flowable.service.impl.FlowableServiceImpl;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.ActDeModel;
import com.snow.system.service.IActDeModelService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @Autowired
    private FlowableServiceImpl flowableService;

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
        actDeModel.setModelTypeList(Lists.newArrayList(ActDeModel.MODEL_TYPE_BPMN,ActDeModel.MODEL_TYPE_DECISION_TABLE));
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
        Long userId = ShiroUtils.getUserId();
        ActDeModel actDeModelName=new ActDeModel();
        actDeModelName.setName(actDeModel.getName());
        List<ActDeModel> actDeModels = actDeModelService.selectActDeModelList(actDeModelName);
        if(!CollectionUtils.isEmpty(actDeModels)){
            return AjaxResult.error("该模型名称已存在");
        }
        ActDeModel actDeModelKey=new ActDeModel();
        actDeModelKey.setModelKey(actDeModel.getModelKey());
        List<ActDeModel> actDeModelKeyList = actDeModelService.selectActDeModelList(actDeModelKey);
        if(!CollectionUtils.isEmpty(actDeModelKeyList)){
            return AjaxResult.error("该模型key已存在");
        }
        actDeModel.setCreatedBy(String.valueOf(userId));
        flowableService.saveModel(actDeModel);
        return toAjax(1);
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
        flowableService.deleteModel(ids);
        return toAjax(1);
    }


    @Log(title = "发布流程", businessType = BusinessType.DEPLOYMENT)
    @RequiresPermissions("system:model:deployment")
    @PostMapping("/deployment")
    @ResponseBody
    public AjaxResult deployment(String id,Integer type)
    {
        String deploymentId = flowablePublishServiceImpl.deploymentByModelId(id, type);
        if(deploymentId==null){
            return AjaxResult.error("modelId不存在");
        }
        return AjaxResult.success("发布成功");
    }


    /**
     * 导出model的xml文件
     */
    @Log(title = "导出流程XML", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:model:exportXml")
    @GetMapping(value = "/exportXml/{modelId}")
    public void exportXml(@PathVariable("modelId") String modelId, HttpServletResponse response) {
        flowableService.exportModelXml(modelId,response);
    }

    /**
     * 展示model的xml文件
     */
    @RequiresPermissions("system:model:showXml")
    @GetMapping(value = "/showXml/{modelId}")
    public void showXml(@PathVariable("modelId") String modelId, HttpServletResponse response) {
        flowableService.showModelXml(modelId,response);
    }
}
