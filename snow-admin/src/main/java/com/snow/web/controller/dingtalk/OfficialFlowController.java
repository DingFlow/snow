package com.snow.web.controller.dingtalk;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.dingtalk.api.response.OapiProcessListbyuseridResponse;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.google.common.collect.Lists;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.DingFlowOperationType;
import com.snow.common.enums.DingFlowTaskType;
import com.snow.common.utils.DateUtils;
import com.snow.dingtalk.model.request.FlowExecuteTaskRequest;
import com.snow.dingtalk.model.request.FlowTerminateProcessInstanceRequest;
import com.snow.dingtalk.model.request.StartFlowRequest;
import com.snow.dingtalk.model.response.DingOperationRecordResponse;
import com.snow.dingtalk.model.response.DingTaskResponse;
import com.snow.dingtalk.service.impl.DingOfficialFlowServiceImpl;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysDingProcinst;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysDingProcinstServiceImpl;
import com.snow.system.service.impl.SysDingRuTaskServiceImpl;
import com.snow.system.service.impl.SysUserServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private SysDingRuTaskServiceImpl sysDingRuTaskService;

    @Autowired
    private SysDingProcinstServiceImpl sysDingProcinstService;
    /**
     * 跳转钉钉当前用户可见的审批模板
     * @return
     */
    @RequiresPermissions("ding:officialFlow:getTemplateManageList")
    @GetMapping("/toProcessTemplateManage")
    public String  toProcessTemplateManage(ModelMap mmap){

        return prefix+"/templateManage";
    }

    /**
     * 获取当前用户可见的审批模板
     */
    @RequiresPermissions("ding:officialFlow:getTemplateManageList")
    @PostMapping("/getProcessTemplateManageList")
    @ResponseBody
    public TableDataInfo getProcessTemplateManageList()
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        OapiProcessListbyuseridResponse.HomePageProcessTemplateVo homePageProcessTemplateVo = dingOfficialFlowService.getProcessListByUserId(sysUser.getDingUserId());
        return pageBySubList(homePageProcessTemplateVo.getProcessList());
    }


    /**
     * 执行流程实例
     * @param
     * @return
     */
    @RequiresPermissions("ding:officialFlow:startProcessInstance")
    @GetMapping("/startProcessInstance")
    @ResponseBody
    public AjaxResult  startProcessInstance(){
        SysUser sysUser = ShiroUtils.getSysUser();
        StartFlowRequest startFlowRequest=new StartFlowRequest();
        startFlowRequest.setProcessCode("PROC-DAF427F7-89E2-4496-9481-53FE56551E9F");
        startFlowRequest.setOriginatorUserId(sysUser.getDingUserId());
        startFlowRequest.setDeptId(sysUser.getDeptId());
        List<StartFlowRequest.FormComponentValueVo> list=Lists.newArrayList();
        StartFlowRequest.FormComponentValueVo formComponentValueVo=new StartFlowRequest.FormComponentValueVo();
        formComponentValueVo.setName("商品编码");
        formComponentValueVo.setValue("OA-SP10002");
        list.add(formComponentValueVo);
        StartFlowRequest.FormComponentValueVo formComponentValueVo0=new StartFlowRequest.FormComponentValueVo();
        formComponentValueVo0.setName("商品名称");
        formComponentValueVo0.setValue("测试商品");
        list.add(formComponentValueVo0);
        StartFlowRequest.FormComponentValueVo formComponentValueVo1=new StartFlowRequest.FormComponentValueVo();
        formComponentValueVo1.setName("规格");
        formComponentValueVo1.setValue("OO--XX");
        list.add(formComponentValueVo1);
        StartFlowRequest.FormComponentValueVo formComponentValueVo2=new StartFlowRequest.FormComponentValueVo();
        formComponentValueVo2.setName("审核日期");
        formComponentValueVo2.setValue(DateUtils.getDate());
        list.add(formComponentValueVo2);
        StartFlowRequest.FormComponentValueVo formComponentValueVo3=new StartFlowRequest.FormComponentValueVo();
        formComponentValueVo3.setName("备注说明");
        formComponentValueVo3.setValue("nova 8系列 我由我掌镜。nova 8 Pro前置Vlog视频双镜头，66W超级快充，120Hz环幕屏。");
        list.add(formComponentValueVo3);
        startFlowRequest.setFormComponentValueVoList(list);
        String s = dingOfficialFlowService.startProcessInstance(startFlowRequest);
        return AjaxResult.success(s);
    }

    /**
     * 获取我的审批流程
     * @return
     */
    @RequiresPermissions("ding:officialFlow:getProcessListByUserId")
    @PostMapping("/getProcessListByUserId")
    @ResponseBody
    public TableDataInfo getProcessListByUserId(){
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
        //获取操作记录
        List<OapiProcessinstanceGetResponse.OperationRecordsVo> operationRecords = processInstanceDetail.getOperationRecords();
        List<DingOperationRecordResponse> dingOperationRecordVOList=Lists.newArrayList();
        if(CollUtil.isNotEmpty(operationRecords)){
            operationRecords.forEach(t->{
                DingOperationRecordResponse dingOperationRecordVO=new DingOperationRecordResponse();
                BeanUtil.copyProperties(t,dingOperationRecordVO);
                SysUser sysUser = sysUserService.selectUserByDingUserId(t.getUserid());
                dingOperationRecordVO.setUserName(sysUser.getUserName());
                dingOperationRecordVO.setOperationType(DingFlowOperationType.getType(t.getOperationType()).getCode());
                String operationResult = t.getOperationResult();
                if(operationResult.equals("AGREE")){
                    dingOperationRecordVO.setOperationResult("同意");
                }else if(operationResult.equals("REFUSE")){
                    dingOperationRecordVO.setOperationResult("拒绝");
                }else if(operationResult.equals("NONE")){
                    dingOperationRecordVO.setOperationResult(null);
                }
                dingOperationRecordVOList.add(dingOperationRecordVO);
            });
        }


        //获取任务节点
        List<OapiProcessinstanceGetResponse.TaskTopVo> tasks = processInstanceDetail.getTasks();
        List<DingTaskResponse> dingTaskVOList=Lists.newArrayList();
        if(CollUtil.isNotEmpty(tasks)){
            tasks.stream().filter(t->!t.getTaskStatus().equals(DingFlowTaskType.CANCELED.getCode())).collect(Collectors.toList()).forEach(t->{
                DingTaskResponse dingTaskVO=new DingTaskResponse();
                BeanUtil.copyProperties(t,dingTaskVO);
                dingTaskVO.defaultTaskSpendTime();
                SysUser sysUser = sysUserService.selectUserByDingUserId(t.getUserid());
                dingTaskVO.setUserName(sysUser.getUserName());
                dingTaskVO.setTaskStatus(DingFlowTaskType.getCode(t.getTaskStatus()).getInfo());
                dingTaskVO.defaultTaskSpendTime();
                dingTaskVOList.add(dingTaskVO);
            });
        }
        mmap.put("formComponentValues",formComponentValues);
        mmap.put("operationRecords",dingOperationRecordVOList);
        mmap.put("tasks",dingTaskVOList);
        mmap.put("processInstanceDetail",processInstanceDetail);
        return prefix+"/myStartProcessDetail";
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


    /**
     * 跳转我发起的流程
     * @return
     */
    @RequiresPermissions("ding:flow:getMyStartProcess")
    @GetMapping("/toMyStartProcess")
    public String getMyHistoricProcessInstance()
    {
        return prefix+"/myStartProcess";
    }





    /**
     * 获取我的流程实例
     * @param sysDingProcinst
     * @return
     */
    @RequiresPermissions("ding:flow:getMyStartProcess")
    @PostMapping("/getMyStartProcessList")
    @ResponseBody
    public TableDataInfo getMyStartProcessList(SysDingProcinst sysDingProcinst){
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysDingProcinst.setStartUserId(sysUser.getDingUserId());
        List<SysDingProcinst> list = sysDingProcinstService.selectSysDingProcinstList(sysDingProcinst);
        return getDataTable(list);
    }

}
