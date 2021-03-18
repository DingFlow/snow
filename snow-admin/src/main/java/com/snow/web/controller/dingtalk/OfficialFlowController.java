package com.snow.web.controller.dingtalk;

import com.dingtalk.api.response.OapiProcessListbyuseridResponse;
import com.dingtalk.api.response.OapiProcessTemplateManageGetResponse;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.PageDomain;
import com.snow.common.core.page.PageModel;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.core.page.TableSupport;
import com.snow.dingtalk.model.FlowExecuteTaskRequest;
import com.snow.dingtalk.model.FlowTerminateProcessInstanceRequest;
import com.snow.dingtalk.service.impl.DingOfficialFlowServiceImpl;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-17 14:07
 **/
@Controller
@RequestMapping("/ding/officialFlow")
public class OfficialFlowController extends BaseController {

    private String prefix = "ding/flow";

    @Autowired
    private DingOfficialFlowServiceImpl dingOfficialFlowService;

    /**
     * 跳转钉钉官方流程模板
     * @return
     */
    @RequiresPermissions("ding:officialFlow:getTemplateManageList")
    @GetMapping("/toProcessTemplateManage")
    public String  toProcessTemplateManage(ModelMap mmap){

        return prefix+"/templateManage";
    }

    /**
     * 获取钉钉官方流程模板
     */
    @RequiresPermissions("ding:officialFlow:getTemplateManageList")
    @PostMapping("/getProcessTemplateManageList")
    @ResponseBody
    public TableDataInfo getProcessTemplateManageList()
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        List<OapiProcessTemplateManageGetResponse.ProcessSimpleVO> processTemplateManage = dingOfficialFlowService.getProcessTemplateManage(sysUser.getDingUserId());
        return pageBySubList(processTemplateManage);
    }

    /**
     * 跳转
     * @param mmap
     * @return
     */
    @RequiresPermissions("ding:officialFlow:getProcessListByUserId")
    @GetMapping("/toProcessListByUserId")
    public String  toProcessListByUserId(ModelMap mmap){

        return prefix+"/myDingTask";
    }

    /**
     * 获取我的审批流程
     * @return
     */
    @RequiresPermissions("ding:officialFlow:getProcessListByUserId")
    @PostMapping("/getProcessListByUserId")
    @ResponseBody
    public TableDataInfo getProcessListByUserId(){
        PageDomain pageDomain = TableSupport.buildPageRequest();
        SysUser sysUser = ShiroUtils.getSysUser();
        OapiProcessListbyuseridResponse.HomePageProcessTemplateVo processListByUserId = dingOfficialFlowService.getProcessListByUserId(sysUser.getDingUserId());
        return pageBySubList(processListByUserId.getProcessList());
    }

    /**
     * 执行流程实例
     * @param flowExecuteTaskRequest
     * @return
     */
    @RequiresPermissions("ding:officialFlow:executeProcess")
    @PostMapping("/executeProcessInstance")
    @ResponseBody
    public AjaxResult  executeProcessInstance(FlowExecuteTaskRequest flowExecuteTaskRequest){
        SysUser sysUser = ShiroUtils.getSysUser();
        flowExecuteTaskRequest.setActionerUserid(sysUser.getDingUserId());
        Boolean aBoolean = dingOfficialFlowService.executeProcessInstance(flowExecuteTaskRequest);
        return AjaxResult.success(aBoolean);
    }

    /**
     * 获取流程详情
     * @param processInstanceId
     * @return
     */
    @RequiresPermissions("ding:officialFlow:getProcessDetail")
    @GetMapping("/getProcessInstanceDetail")
    public String  getProcessInstanceDetail(String processInstanceId,ModelMap mmap){

        OapiProcessinstanceGetResponse.ProcessInstanceTopVo processInstanceDetail = dingOfficialFlowService.getProcessInstanceDetail(processInstanceId);

        //获取表单值
        List<OapiProcessinstanceGetResponse.FormComponentValueVo> formComponentValues = processInstanceDetail.getFormComponentValues();
        mmap.put("formComponentValues",formComponentValues);

        mmap.put("processInstanceDetail",processInstanceDetail);
        return prefix+"/checkDetail";
    }

    /**
     * 终止流程
     * @param flowTerminateProcessInstanceRequest
     * @return
     */
    @RequiresPermissions("ding:officialFlow:terminateProcess")
    @PostMapping("/terminateProcessInstance")
    @ResponseBody
    public AjaxResult  terminateProcessInstance(FlowTerminateProcessInstanceRequest flowTerminateProcessInstanceRequest){
        SysUser sysUser = ShiroUtils.getSysUser();
        flowTerminateProcessInstanceRequest.setOperatingUserid(sysUser.getDingUserId());
        Boolean aBoolean = dingOfficialFlowService.terminateProcessInstance(flowTerminateProcessInstanceRequest);
        return AjaxResult.success(aBoolean);
    }

}
