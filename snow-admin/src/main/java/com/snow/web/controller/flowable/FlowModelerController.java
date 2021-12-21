package com.snow.web.controller.flowable;

import com.snow.common.annotation.Log;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.PageModel;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.flowable.domain.*;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.flowable.service.impl.FlowablePublishServiceImpl;
import com.snow.flowable.service.impl.FlowableServiceImpl;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.FinanceAlipayFlow;
import com.snow.system.service.IFinanceAlipayFlowService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.repository.Model;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * Controller
 * 
 * @author snow
 * @date 2020-11-09
 */
@Controller
@RequestMapping("/modeler")
public class FlowModelerController extends BaseController
{
    private String prefix = "flow";


    @Autowired
    private FlowableServiceImpl flowableService;
    @Autowired
    private FlowablePublishServiceImpl flowablePublishService;

    /**
     * 跳转流程编译器
     * @return
     */
    @RequiresPermissions("modeler:flow:view")
    @GetMapping("/index")
    public String index()
    {

        return redirect("/modeler/index.html");
    }

    @RequiresPermissions("modeler:model:view")
    @GetMapping("/model")
    public String modelView()
    {
        return prefix + "/model";
    }



    @RequiresPermissions("modeler:deployment:view")
    @GetMapping()
    public String deploymentView()
    {
        return prefix + "/deployment";
    }

    @RequiresPermissions("modeler:deployment:detail")
    @GetMapping("/getDeploymentDetail/{id}")
    public String getDeploymentDetail(@PathVariable("id")String id , ModelMap modelMap) {
        DeploymentVO deploymentDetailById = flowableService.getDeploymentDetailById(id);
        modelMap.put("deploymentVO",deploymentDetailById);
        return prefix + "/deployment-detail";
    }

    /**
     * 查询发布实例列表
     */
    @RequiresPermissions("modeler:deployment:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DeploymentQueryDTO deploymentQuery)
    {
        PageModel<DeploymentVO> deploymentList = flowableService.getDeploymentList(deploymentQuery);
        return getFlowDataTable(deploymentList);
    }



    /**
     * 获取XML
     */
    @GetMapping("/getXml")
    @RequiresPermissions("modeler:deployment:getXml")
    public void getXml(String id,String resourceName,HttpServletResponse response)
    {

        flowableService.getDeploymentSource(id,resourceName,"xml",response);
    }

    /**
     * 获取流程图
     */
    @GetMapping("/getFlowPicture")
    @RequiresPermissions("modeler:deployment:getFlowPicture")
    public void getFlowPicture(String id,String resourceName,HttpServletResponse response)
    {

        flowableService.getDeploymentSource(id,resourceName,"png",response);
    }


    /**
     * 获取流程图
     */
    @RequiresPermissions("modeler:flow:getProcessDiagram")
    @GetMapping("/getProcessDiagram")
    public void getProcessDiagram(String processInstanceId,HttpServletResponse response,ModelMap modelMap)
    {
        flowableService.getFlowableProcessImage(processInstanceId, response);
    }
    /**
     * 删除发布
     */
    @RequiresPermissions("modeler:deployment:remove")
    @Log(title = "删除发布", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        flowableService.deleteDeployment(ids);
        return toAjax(1);
    }


    @Log(title = "发布流程", businessType = BusinessType.IMPORT)
    @RequiresPermissions("modeler:deployment:deployment")
    @PostMapping("/importData")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult importData(MultipartFile file, String name,String key,String category) throws Exception
    {
        InputStream inputStream = file.getInputStream();
        String originalFilename = file.getOriginalFilename();
        DeploymentDTO deploymentQueryDTO=new DeploymentDTO();
        deploymentQueryDTO.setName(name);
        deploymentQueryDTO.setKey(key);
        deploymentQueryDTO.setCategory(category);
        deploymentQueryDTO.setResourceName(originalFilename);
        flowablePublishService.createInputStreamDeployment(deploymentQueryDTO,inputStream);
        return AjaxResult.success("发布成功");
    }
}
