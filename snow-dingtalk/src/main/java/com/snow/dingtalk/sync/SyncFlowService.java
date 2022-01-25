package com.snow.dingtalk.sync;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.snow.common.enums.DingFlowTaskType;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.service.impl.DingOfficialFlowServiceImpl;
import com.snow.system.domain.SysDingHiTask;
import com.snow.system.domain.SysDingProcinst;
import com.snow.system.domain.SysDingRuTask;
import com.snow.system.service.impl.SysDingHiTaskServiceImpl;
import com.snow.system.service.impl.SysDingProcinstServiceImpl;
import com.snow.system.service.impl.SysDingRuTaskServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-24 22:09
 **/
@Component
@Slf4j
public class SyncFlowService implements ISyncSysInfo  {

    @Autowired
    private SysDingProcinstServiceImpl sysDingProcinstService=SpringUtils.getBean(SysDingProcinstServiceImpl.class);

    @Autowired
    private DingOfficialFlowServiceImpl dingOfficialFlowService=SpringUtils.getBean(DingOfficialFlowServiceImpl.class);

    @Autowired
    private SysDingRuTaskServiceImpl sysDingRuTaskService=SpringUtils.getBean(SysDingRuTaskServiceImpl.class);


    @Autowired
    private SysDingHiTaskServiceImpl sysDingHiTaskService=SpringUtils.getBean(SysDingHiTaskServiceImpl.class);

    @Override
    public JSONObject SyncSysInfo(DingTalkListenerType dingTalkListenerType, JSONObject jsonObject) {

        Integer code = dingTalkListenerType.getCode();

        if(code.equals(DingTalkListenerType.BPMS_INSTANCE_CHANGE.getCode())){
            handleSysInstance(jsonObject);
        }else if(code.equals(DingTalkListenerType.BPMS_TASK_CHANGE.getCode())){
            handleSysTask(jsonObject);
        }
        return null;
    }


    private void handleSysInstance(JSONObject jsonObject){

        SysDingProcinst sysDingProcinst= jsonObject.toJavaObject(SysDingProcinst.class);
        sysDingProcinst.setProcInstId(String.valueOf(jsonObject.get("processInstanceId")));
        sysDingProcinst.setStartUserId(String.valueOf(jsonObject.get("staffId")));
        sysDingProcinst.setDingUrl(String.valueOf(jsonObject.get("url")));
        sysDingProcinst.setProcCode(String.valueOf(jsonObject.get("processCode")));
        Optional.ofNullable(jsonObject.get("result")).ifPresent(t->sysDingProcinst.setProcResult(String.valueOf(jsonObject.get("result"))));
        Optional.ofNullable(jsonObject.get("createTime")).ifPresent(t->sysDingProcinst.setStartTime(new Date((long)jsonObject.get("createTime"))));
        Optional.ofNullable(jsonObject.get("finishTime")).ifPresent(t->sysDingProcinst.setFinishTime(new Date((long)jsonObject.get("finishTime"))));
        String type = sysDingProcinst.getType();
        switch (type){
            //实例开始
            case "start":
                startDingSysInstance(sysDingProcinst);
                break;

            case "finish":
                finishDingSysInstance(sysDingProcinst);
                break;
            //审批终止（发起人撤销审批单）
            case "terminate":
                break;
                default:


        }

    }

    /**
     * 开始流程时的逻辑处理
     * @param sysDingProcinst
     */
    private void startDingSysInstance(SysDingProcinst sysDingProcinst){
        String processInstanceId=sysDingProcinst.getProcInstId();
        //添加流程实例记录
        SysDingProcinst querySysDingProcinst = sysDingProcinstService.selectSysDingProcinstByProcInstId(sysDingProcinst.getProcInstId());
        if(StringUtils.isNull(querySysDingProcinst)){

            //查询流程详情，保存下个节点待办
            OapiProcessinstanceGetResponse.ProcessInstanceTopVo processInstanceDetail = dingOfficialFlowService.getProcessInstanceDetail(processInstanceId);
            sysDingProcinst.setType(processInstanceDetail.getStatus());
            sysDingProcinst.setBusinessKey(processInstanceDetail.getBusinessId());
            //保存流程实例
            sysDingProcinstService.insertSysDingProcinst(sysDingProcinst);

        }

    }

    /**
     * 结束流程时的逻辑处理
     * @param sysDingProcinst
     */
    private void finishDingSysInstance(SysDingProcinst sysDingProcinst){
        String processInstanceId=sysDingProcinst.getProcInstId();
        //查询流程详情，保存下个节点待办
        OapiProcessinstanceGetResponse.ProcessInstanceTopVo processInstanceDetail = dingOfficialFlowService.getProcessInstanceDetail(processInstanceId);
        //更新审批实例
        SysDingProcinst querySysDingProcinst = sysDingProcinstService.selectSysDingProcinstByProcInstId(processInstanceId);
        if(StringUtils.isNotNull(querySysDingProcinst)){
            sysDingProcinst.setId(querySysDingProcinst.getId());
            sysDingProcinst.setType(processInstanceDetail.getStatus());
            sysDingProcinstService.updateSysDingProcinst(sysDingProcinst);
        }

    }


    private void handleSysTask(JSONObject jsonObject){
        SysDingRuTask sysDingRuTask= jsonObject.toJavaObject(SysDingRuTask.class);
        sysDingRuTask.setProcInstId(String.valueOf(jsonObject.get("processInstanceId")));
        sysDingRuTask.setAssignee(String.valueOf(jsonObject.get("staffId")));

        sysDingRuTask.setProcCode(String.valueOf(jsonObject.get("processCode")));

        String type= String.valueOf(jsonObject.get("type"));
        switch (type){
            //实例开始
            case "start":
                startDingSysTask(sysDingRuTask);
                break;

            case "finish":
                finishDingSysTask(sysDingRuTask);
                break;
            //当前节点有多个审批人并且是或签，其中一个人执行了审批，其他审批人会推送cancel类型事件
            case "cancel":
                finishDingSysTask(sysDingRuTask);
                break;
            default:


        }
    }

    /**
     * 任务开始运行表和历史表添加数据
     * @param sysDingRuTask
     */
    private void startDingSysTask(SysDingRuTask sysDingRuTask){
        String procInstId = sysDingRuTask.getProcInstId();
        //查询流程详情，保存下个节点待办
        OapiProcessinstanceGetResponse.ProcessInstanceTopVo processInstanceDetail = dingOfficialFlowService.getProcessInstanceDetail(procInstId);
        //获取任务节点(写入钉钉待办表)
        List<OapiProcessinstanceGetResponse.TaskTopVo> tasks = processInstanceDetail.getTasks();
        if(CollUtil.isNotEmpty(tasks)){
            tasks.forEach(t->{
                //第二步，运行中的任务保存到本地表
                if(t.getTaskStatus().equals(DingFlowTaskType.RUNNING.getCode())){
                    SysDingRuTask querySysDingRuTask = sysDingRuTaskService.selectSysDingRuTaskById(t.getTaskid());
                    if(StringUtils.isNull(querySysDingRuTask)){
                        saveDingRunTask(t,procInstId,sysDingRuTask.getProcCode());
                    }
                    SysDingHiTask sysDingHiTask = sysDingHiTaskService.selectSysDingHiTaskById(t.getTaskid());
                    if(StringUtils.isNull(sysDingHiTask)){
                        saveDingHiTask(t,procInstId,sysDingRuTask.getProcCode());
                    }
                }

            });
        }
    }

    /**
     * 任务完成删除运行中的表数据
     * @param sysDingRuTask
     */
    private void finishDingSysTask(SysDingRuTask sysDingRuTask){
        String procInstId = sysDingRuTask.getProcInstId();
        //查询流程详情，保存下个节点待办
        OapiProcessinstanceGetResponse.ProcessInstanceTopVo processInstanceDetail = dingOfficialFlowService.getProcessInstanceDetail(procInstId);
        //获取任务节点(写入钉钉待办表)
        List<OapiProcessinstanceGetResponse.TaskTopVo> tasks = processInstanceDetail.getTasks();
        if(CollUtil.isNotEmpty(tasks)){
            tasks.forEach(t->{
                //第二步，删除运行中的任务，更新历史任务表
                if(t.getTaskStatus().equals(DingFlowTaskType.COMPLETED.getCode())){
                    SysDingRuTask querySysDingRuTask = sysDingRuTaskService.selectSysDingRuTaskById(t.getTaskid());
                    if(StringUtils.isNotNull(querySysDingRuTask)){
                        sysDingRuTaskService.deleteSysDingRuTaskById(querySysDingRuTask.getId());
                        SysDingHiTask sysDingHiTask=new SysDingHiTask();
                        sysDingHiTask.setId(t.getTaskid());
                        sysDingHiTask.setTaskResult(t.getTaskResult());
                        sysDingHiTask.setFinishTime(t.getFinishTime());
                        sysDingHiTask.setAssignee(t.getUserid());
                        sysDingHiTask.setTaskState(t.getTaskStatus());
                        sysDingHiTask.setDescription(processInstanceDetail.getTitle());
                        sysDingHiTaskService.updateSysDingHiTask(sysDingHiTask);
                    }
                }
                //当前节点有多个审批人并且是或签，其中一个人执行了审批，其他审批人会推送cancel类型事件，修改运行表任务状态
                else if(t.getTaskStatus().equals(DingFlowTaskType.CANCELED.getCode())){

                    SysDingRuTask sysDingRuTask1=new SysDingRuTask();
                    sysDingRuTask1.setId(t.getTaskid());
                    sysDingRuTask1.setTaskState(t.getTaskStatus());
                    sysDingRuTaskService.updateSysDingRuTask(sysDingRuTask1);
                }

            });
        }
    }


    private void saveDingRunTask(OapiProcessinstanceGetResponse.TaskTopVo taskTopVo,String procInstId,String procCode){

        try {
            SysDingRuTask sysDingRuTask=new SysDingRuTask();
            sysDingRuTask.setId(taskTopVo.getTaskid());
            sysDingRuTask.setCreateTime(taskTopVo.getCreateTime());
            sysDingRuTask.setActivityId(taskTopVo.getActivityId());
            sysDingRuTask.setProcCode(procCode);
            sysDingRuTask.setFinishTime(taskTopVo.getFinishTime());
            sysDingRuTask.setAssignee(taskTopVo.getUserid());
            sysDingRuTask.setTaskState(taskTopVo.getTaskStatus());
            sysDingRuTask.setFormKey(taskTopVo.getUrl());
            sysDingRuTask.setId(taskTopVo.getTaskid());
            sysDingRuTask.setProcInstId(procInstId);
            sysDingRuTaskService.insertSysDingRuTask(sysDingRuTask);
        }catch (Exception e){
            log.error("**************保存运行任务时候出现异常：{}",e.getMessage());
        }

    }

    private void saveDingHiTask(OapiProcessinstanceGetResponse.TaskTopVo taskTopVo,String procInstId,String procCode){
       try {
           SysDingHiTask sysDingHiTask=new SysDingHiTask();
           sysDingHiTask.setId(taskTopVo.getTaskid());
           sysDingHiTask.setTaskId(taskTopVo.getTaskid());
           sysDingHiTask.setTaskResult(taskTopVo.getTaskResult());
           sysDingHiTask.setCreateTime(taskTopVo.getCreateTime());
           sysDingHiTask.setActivityId(taskTopVo.getActivityId());
           sysDingHiTask.setProcCode(procCode);
           sysDingHiTask.setFinishTime(taskTopVo.getFinishTime());
           sysDingHiTask.setAssignee(taskTopVo.getUserid());
           sysDingHiTask.setTaskState(taskTopVo.getTaskStatus());
           sysDingHiTask.setFormKey(taskTopVo.getUrl());
           sysDingHiTask.setId(taskTopVo.getTaskid());
           sysDingHiTask.setProcInstId(procInstId);

           sysDingHiTaskService.insertSysDingHiTask(sysDingHiTask);
       }catch (Exception e){
           log.error("**************保存历史任务时候出现异常：{}",e.getMessage());
       }
    }

}
