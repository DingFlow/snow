package com.snow.web.controller.dingtalk;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.DingFlowTaskType;
import com.snow.dingtalk.model.request.DingFinishTaskRequest;
import com.snow.dingtalk.model.response.DingTaskResponse;
import com.snow.dingtalk.model.request.FlowExecuteTaskRequest;
import com.snow.dingtalk.service.impl.DingOfficialFlowServiceImpl;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysDingHiTask;
import com.snow.system.domain.SysDingRuTask;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysDingHiTaskService;
import com.snow.system.service.ISysDingRuTaskService;
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
 * 钉钉任务Controller
 * 
 * @author 没用的阿吉
 * @date 2021-03-24
 */

@Controller
@RequestMapping("/ding/task")
public class DingTaskController extends BaseController
{
    private String prefix = "ding/task";

    @Autowired
    private ISysDingRuTaskService sysDingRuTaskService;

    @Autowired
    private DingOfficialFlowServiceImpl dingOfficialFlowService;

    @Autowired
    private ISysDingHiTaskService sysDingHiTaskService;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @RequiresPermissions("ding:task:view")
    @GetMapping()
    public String task()
    {
        return prefix + "/task";
    }


    /**
     * 查询钉钉运行任务列表
     */

    @RequiresPermissions("ding:task:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysDingRuTask sysDingRuTask)
    {
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysDingRuTask.setAssignee(sysUser.getDingUserId());
        //待办为处理中的
        sysDingRuTask.setTaskState(DingFlowTaskType.RUNNING.getCode());
        List<SysDingRuTask> list = sysDingRuTaskService.selectSysDingRuTaskList(sysDingRuTask);
        return getDataTable(list);
    }




    /**
     * 跳转完成任务界面
     * @return
     */
    @GetMapping("/toFinishTask")
    public String toFinishTask(String taskId,ModelMap mmap)
    {
        SysDingRuTask sysDingRuTask = sysDingRuTaskService.selectSysDingRuTaskById(taskId);

        OapiProcessinstanceGetResponse.ProcessInstanceTopVo processInstanceDetail = dingOfficialFlowService.getProcessInstanceDetail(sysDingRuTask.getProcInstId());

        //获取表单值
        List<OapiProcessinstanceGetResponse.FormComponentValueVo> formComponentValues = processInstanceDetail.getFormComponentValues();

        mmap.put("formComponentValues",formComponentValues);
        mmap.put("taskId",taskId);
        mmap.put("processInstanceId",sysDingRuTask.getProcInstId());
        mmap.put("processInstanceDetail",processInstanceDetail);
        return prefix+"/checkDetail";
    }

    /**
     * 完成任务
     * @return
     */
    @PostMapping("/finishTask")
    @RequiresPermissions("ding:task:finishTask")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult finishTask(DingFinishTaskRequest finishTaskDTO)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        finishTaskDTO.setUserId(sysUser.getDingUserId());
        FlowExecuteTaskRequest flowExecuteTaskRequest=new FlowExecuteTaskRequest();
        flowExecuteTaskRequest.setTaskId(Long.parseLong(finishTaskDTO.getTaskId()));
        flowExecuteTaskRequest.setActionerUserid(sysUser.getDingUserId());
        flowExecuteTaskRequest.setRemark(finishTaskDTO.getComment());
        flowExecuteTaskRequest.setResult(finishTaskDTO.getIsPass()?"agree":"refuse");
        flowExecuteTaskRequest.setProcessInstanceId(finishTaskDTO.getProcessInstanceId());
        Boolean aBoolean = dingOfficialFlowService.executeProcessInstance(flowExecuteTaskRequest);
        return AjaxResult.success(aBoolean);
    }

    /**
     * 跳转我的已办
     * @return
     */
    @RequiresPermissions("ding:task:myTasked")
    @GetMapping("/toMyTasked")
    public String toMyTasked()
    {

        return prefix+"/myTasked";
    }

    /**
     * 获取我办结的任务列表
     * @param sysDingHiTask
     * @return
     */
    @RequiresPermissions("ding:task:myTasked")
    @PostMapping("/getMyTaskedList")
    @ResponseBody
    public TableDataInfo getMyTaskedList(SysDingHiTask sysDingHiTask){
        startPage();
        SysUser sysUser = ShiroUtils.getSysUser();
        sysDingHiTask.setAssignee(sysUser.getDingUserId());
        sysDingHiTask.setTaskState(DingFlowTaskType.COMPLETED.getCode());
        List<SysDingHiTask> list = sysDingHiTaskService.selectSysDingHiTaskList(sysDingHiTask);
        return getDataTable(list);
    }


    /**
     * 获取我的已办详情
     * @param taskId
     * @return
     */
    @RequiresPermissions("ding:task:getTaskedDetail")
    @GetMapping("/getTaskedDetail")
    public String  getTaskedDetail(String taskId,ModelMap mmap){

        SysDingHiTask sysDingHiTask = sysDingHiTaskService.selectSysDingHiTaskById(taskId);
        OapiProcessinstanceGetResponse.ProcessInstanceTopVo processInstanceDetail = dingOfficialFlowService.getProcessInstanceDetail(sysDingHiTask.getProcInstId());

        //获取表单值
        List<OapiProcessinstanceGetResponse.FormComponentValueVo> formComponentValues = processInstanceDetail.getFormComponentValues();

        //获取任务节点
        List<OapiProcessinstanceGetResponse.TaskTopVo> tasks = processInstanceDetail.getTasks();
        DingTaskResponse dingTaskVO=new DingTaskResponse();
        if(CollectionUtil.isNotEmpty(tasks)){
            tasks.stream().filter(taskTopVo -> taskTopVo.getTaskid().equals(sysDingHiTask.getTaskId())).collect(Collectors.toList()).forEach(t->{
                BeanUtil.copyProperties(t,dingTaskVO);
                dingTaskVO.defaultTaskSpendTime();
                SysUser sysUser = sysUserService.selectUserByDingUserId(t.getUserid());
                dingTaskVO.setUserName(sysUser.getUserName());
                dingTaskVO.setTaskStatus(DingFlowTaskType.getCode(t.getTaskStatus()).getInfo());
            });
        }
        SysUser sysUser = sysUserService.selectUserByDingUserId(processInstanceDetail.getOriginatorUserid());
        mmap.put("formComponentValues",formComponentValues);
        mmap.put("tasks",dingTaskVO);
        mmap.put("applyUserName",sysUser.getUserName());
        mmap.put("processInstanceDetail",processInstanceDetail);
        return prefix+"/myTaskedDetail";
    }

}

