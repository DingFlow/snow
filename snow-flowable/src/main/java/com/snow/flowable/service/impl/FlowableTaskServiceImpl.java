package com.snow.flowable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.snow.common.core.page.PageModel;
import com.snow.common.enums.WorkRecordStatus;
import com.snow.common.exception.BusinessException;
import com.snow.common.utils.bean.BeanUtils;
import com.snow.common.utils.bean.MyBeanUtils;
import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.common.enums.FlowTypeEnum;
import com.snow.flowable.common.skipTask.TaskSkipService;
import com.snow.flowable.domain.*;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.flowable.service.FlowableUserService;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Attachment;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/13 15:54
 */
@Slf4j
@Service
public class FlowableTaskServiceImpl implements FlowableTaskService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FlowableService flowableService;

    @Resource
    private FlowableUserService flowableUserService;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private AppFormServiceImpl appFormService;

    @Autowired
    private TaskSkipService taskSkipService;

    @Override
    public PageModel<TaskVO> findTasksByUserId(String userId, TaskBaseDTO taskBaseDTO) {
        //根据用户ID获取角色
        Set<Long> sysRoles = flowableUserService.getFlowGroupByUserId(Long.parseLong(userId));

        TaskQuery taskQuery = taskService.createTaskQuery()
                .or()
                .taskCandidateOrAssigned(userId);
        //这个地方查询会去查询系统的用户组表，希望的是查询自己的用户表
        if(!CollectionUtils.isEmpty(sysRoles)) {
            List<String> roleIds = sysRoles.stream().map(t ->
                    String.valueOf(t)
            ).collect(Collectors.toList());
            taskQuery.taskCandidateGroupIn(roleIds).endOr();
        }

        if(!StringUtils.isEmpty(taskBaseDTO.getProcessInstanceId())){
            taskQuery.processInstanceId(taskBaseDTO.getProcessInstanceId());
        }
        if(!StringUtils.isEmpty(taskBaseDTO.getTaskId())){
            taskQuery.taskId(taskBaseDTO.getTaskId());
        }

        if(!StringUtils.isEmpty(taskBaseDTO.getTaskName())){
            taskQuery.taskNameLike("%"+taskBaseDTO.getTaskName()+"%");
        }
        if(!StringUtils.isEmpty(taskBaseDTO.getBusinessKey())){
            taskQuery.processInstanceBusinessKeyLike("%"+taskBaseDTO.getBusinessKey()+"%");
        }
        if(!StringUtils.isEmpty(taskBaseDTO.getDefinitionKey())){
            taskQuery.processDefinitionKey(taskBaseDTO.getDefinitionKey());
        }
        if(!StringUtils.isEmpty(taskBaseDTO.getProcessDefinitionName())){
            taskQuery.processDefinitionNameLike("%"+taskBaseDTO.getProcessDefinitionName()+"%");
        }

        long count = taskQuery
                .orderByTaskCreateTime()
                .desc()
                .count();
        List<Task> taskList = taskQuery
                .orderByTaskCreateTime()
                .desc()
                .listPage(taskBaseDTO.getPageNum(), taskBaseDTO.getPageSize());

        List<TaskVO> taskVoList = taskList.stream().map(t -> {
            TaskVO taskVO = new TaskVO();
            taskVO.setTaskId(t.getId());
            taskVO.setTaskName(t.getName());
            taskVO.setProcessInstanceId(t.getProcessInstanceId());
            taskVO.setCreateDate(t.getCreateTime());
            taskVO.setFormKey(t.getFormKey());
            taskVO.setParentTaskId(t.getParentTaskId());
            taskVO.setAssignee(t.getAssignee());
            taskVO.setOwner(t.getOwner());
            ProcessInstance historicProcessInstance = flowableService.getProcessInstanceById(t.getProcessInstanceId());
            taskVO.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
            String startUserId = historicProcessInstance.getStartUserId();
            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(startUserId));
            taskVO.setStartUserId(startUserId);
            taskVO.setStartUserName(sysUser.getUserName());
            taskVO.setBusinessKey(historicProcessInstance.getBusinessKey());
            taskVO.setStartTime(historicProcessInstance.getStartTime());
            //设置流程类型
            Object hisVariable = flowableService.getHisVariable(t.getProcessInstanceId(), FlowConstants.PROCESS_TYPE);
            Object processType = Optional.ofNullable(hisVariable).orElse(FlowTypeEnum.API_PROCESS.getCode());
            taskVO.setProcessType(String.valueOf(processType));
            return taskVO;
        }).collect(Collectors.toList());
        PageModel<TaskVO> pageModel = new PageModel<> ();
        pageModel.setTotalCount((int)count);
        pageModel.setPagedRecords(taskVoList);
        return pageModel;
    }

    @Override
    public PageModel<HistoricTaskInstanceVO> getHistoricTaskInstance(HistoricTaskInstanceDTO historicTaskInstanceDTO) {

        HistoricTaskInstanceQuery historicTaskInstanceQuery = buildHistoricTaskInstanceCondition(historicTaskInstanceDTO);
        long count = historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime().
                desc().
                count();
        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime().
                desc().
                listPage(historicTaskInstanceDTO.getPageNum(), historicTaskInstanceDTO.getPageSize());


        List<HistoricTaskInstanceVO> historicTaskInstanceVOS = historicTaskInstances.stream().map(t -> {
            HistoricTaskInstanceVO historicTaskInstanceVO = new HistoricTaskInstanceVO();
            BeanUtils.copyProperties(t, historicTaskInstanceVO);
            Map<String, Object> processVariables = t.getProcessVariables();
            String url= Optional.ofNullable(String.valueOf(processVariables.get(FlowConstants.BUS_VAR_URL))).orElse("");
            //设置返回详情页
            if(ObjectUtil.isNotEmpty(url)&&ObjectUtil.isNotEmpty(processVariables.get("id"))){
                historicTaskInstanceVO.setFromDetailUrl(url+"/"+processVariables.get("id"));
            }

            historicTaskInstanceVO.setCompleteTime(t.getEndTime());
            //转办的时候会出现到历史记录里，但是任务还没完结？？？
            if(ObjectUtil.isNotNull(t.getEndTime())){
                String spendTime= DateUtil.formatBetween(t.getCreateTime(), t.getEndTime(), BetweenFormater.Level.SECOND);
                historicTaskInstanceVO.setHandleTaskTime(spendTime);
            }
            historicTaskInstanceVO.setTaskId(t.getId());
            historicTaskInstanceVO.setTaskName(t.getName());
            historicTaskInstanceVO.setStartTime(t.getCreateTime());
            //审批结果
            Optional.ofNullable(historicTaskInstanceVO.getTaskLocalVariables()).ifPresent(u->{
                Object isPass =Optional.ofNullable(u.get(FlowConstants.IS_PASS)).orElse("");
                Object isStart =Optional.ofNullable(u.get(FlowConstants.IS_START)).orElse("");
                //处理审核条件
                historicTaskInstanceVO.setIsPass(String.valueOf(isPass));
                historicTaskInstanceVO.setIsStart(String.valueOf(isStart));
            });
            //设置流程类型
            Object hisVariable = flowableService.getHisVariable(t.getProcessInstanceId(), FlowConstants.PROCESS_TYPE);
            Object processType = Optional.ofNullable(hisVariable).orElse(FlowTypeEnum.API_PROCESS.getCode());
            historicTaskInstanceVO.setProcessType(String.valueOf(processType));
            ProcessInstanceVO processInstanceVo = flowableService.getProcessInstanceVoById(t.getProcessInstanceId());
            historicTaskInstanceVO.setProcessName(processInstanceVo.getProcessDefinitionName());
            historicTaskInstanceVO.setBusinessKey(processInstanceVo.getBusinessKey());
            historicTaskInstanceVO.setStartUserId(processInstanceVo.getStartUserId());
            historicTaskInstanceVO.setStartUserName(processInstanceVo.getStartUserName());
            return historicTaskInstanceVO;

        }).collect(Collectors.toList());

        PageModel<HistoricTaskInstanceVO> pageModel = new PageModel<> ();
        pageModel.setTotalCount((int)count);
        pageModel.setPagedRecords(historicTaskInstanceVOS);
        return pageModel;
    }


    @Override
    public List<HistoricTaskInstanceVO>  getHistoricTaskInstanceNoPage(HistoricTaskInstanceDTO historicTaskInstanceDTO){
        HistoricTaskInstanceQuery historicTaskInstanceQuery = buildHistoricTaskInstanceCondition(historicTaskInstanceDTO);
        List<HistoricTaskInstance> list = historicTaskInstanceQuery.orderByTaskCreateTime().asc().list();
        List<HistoricTaskInstanceVO> historicTaskInstanceVOS = HistoricTaskInstanceVO.warpList(list);
        historicTaskInstanceVOS.parallelStream().forEach(t -> warpTaskVoList(t));
        return historicTaskInstanceVOS;
    }

    /**
     * 构建历史任务查询条件
     * @param historicTaskInstanceDTO 传入的查询参数
     */
    private HistoricTaskInstanceQuery buildHistoricTaskInstanceCondition(HistoricTaskInstanceDTO historicTaskInstanceDTO){
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();

        if(ObjectUtil.isNotEmpty(historicTaskInstanceDTO.getProcessInstanceId())){
            historicTaskInstanceQuery.processInstanceId(historicTaskInstanceDTO.getProcessInstanceId());
        }
        if(ObjectUtil.isNotEmpty(historicTaskInstanceDTO.getProcessDefinitionName())){
            historicTaskInstanceQuery.processDefinitionName(historicTaskInstanceDTO.getProcessDefinitionName());
        }
        if(ObjectUtil.isNotEmpty(historicTaskInstanceDTO.getUserId())){
            historicTaskInstanceQuery.taskAssignee(historicTaskInstanceDTO.getUserId());
        }
        if(ObjectUtil.isNotEmpty(historicTaskInstanceDTO.getBusinessKeyLike())){
            historicTaskInstanceQuery.processInstanceBusinessKeyLike("%"+historicTaskInstanceDTO.getBusinessKeyLike()+"%");
        }
        if(ObjectUtil.isNotEmpty(historicTaskInstanceDTO.getBusinessKey())){
            historicTaskInstanceQuery.processInstanceBusinessKey(historicTaskInstanceDTO.getBusinessKey());
        }
        Optional.ofNullable(historicTaskInstanceDTO.getTaskStatus()).ifPresent(m->{
            if(m.equals(WorkRecordStatus.NO_FINISHED)){
                historicTaskInstanceQuery.unfinished();
            }
            if(m.equals(WorkRecordStatus.FINISHED)){
                historicTaskInstanceQuery.finished();
            }
        });

        Optional.ofNullable(historicTaskInstanceDTO.getProcessStatus()).ifPresent(m->{
            if(m.equals(WorkRecordStatus.NO_FINISHED)){
                historicTaskInstanceQuery.processUnfinished();
            }
            if(m.equals(WorkRecordStatus.FINISHED)){
                historicTaskInstanceQuery.processFinished();
            }
        });
        historicTaskInstanceQuery.includeIdentityLinks()
                .includeProcessVariables()
                .includeTaskLocalVariables();
        return historicTaskInstanceQuery;
    }

    /**
     * 组装任务返回参数
     * @param historicTaskInstanceVO
     */
   private void warpTaskVoList(HistoricTaskInstanceVO historicTaskInstanceVO){
       //保存待办人
       Set<SysUser> identityLinksForTask = getHistoricIdentityLinksForTask(historicTaskInstanceVO.getTaskId());
       Optional.ofNullable(identityLinksForTask).ifPresent(m->{
           List<String> userNameList = identityLinksForTask.stream().map(SysUser::getUserName).collect(Collectors.toList());
           historicTaskInstanceVO.setHandleUserList(userNameList);
       });
       //办理人
       if (ObjectUtil.isNotEmpty(historicTaskInstanceVO.getAssignee())&&com.snow.common.utils.StringUtils.isNumeric(historicTaskInstanceVO.getAssignee())) {
           SysUser sysUser = sysUserService.selectUserById(Long.parseLong(historicTaskInstanceVO.getAssignee()));
           historicTaskInstanceVO.setAssigneeName(sysUser.getUserName());
       }
       //审批结果
       Optional.ofNullable(historicTaskInstanceVO.getTaskLocalVariables()).ifPresent(u->{
           Object isPass =Optional.ofNullable(u.get(FlowConstants.IS_PASS)).orElse("");
           Object isStart =Optional.ofNullable(u.get(FlowConstants.IS_START)).orElse("");
           //处理审核条件
           historicTaskInstanceVO.setIsPass(String.valueOf(isPass));
           historicTaskInstanceVO.setIsStart(String.valueOf(isStart));
       });
       //获取评论
       List<Comment> comment = taskService.getTaskComments(historicTaskInstanceVO.getTaskId(), FlowConstants.OPINION);
       //获取附件
       List<Attachment> taskAttachments = taskService.getTaskAttachments(historicTaskInstanceVO.getTaskId());
       historicTaskInstanceVO.setCommentList(comment);
       historicTaskInstanceVO.setAttachmentList(taskAttachments);
       //计算任务历时
       if(!StringUtils.isEmpty(historicTaskInstanceVO.getCompleteTime())){
           String spendTime = DateUtil.formatBetween(DateUtil.between(historicTaskInstanceVO.getStartTime(), historicTaskInstanceVO.getCompleteTime(), DateUnit.SECOND));
           historicTaskInstanceVO.setHandleTaskTime(spendTime);
       }
       HistoricProcessInstance historicProcessInstance = flowableService.getHistoricProcessInstanceById(historicTaskInstanceVO.getProcessInstanceId());
       historicTaskInstanceVO.setProcessName(historicProcessInstance.getProcessDefinitionName());
       historicTaskInstanceVO.setBusinessKey(historicProcessInstance.getBusinessKey());
    }


    @Override
    public Set<SysUser> getIdentityLinksForTask(String taskId, String type) {
        Set<SysUser> userList=new HashSet<>();
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        if(!CollectionUtils.isEmpty(identityLinksForTask)){
            identityLinksForTask.stream().filter(identityLink ->
                    !StringUtils.isEmpty(identityLink.getGroupId())
                            &&identityLink.getType().equals(type)
            )
                    .forEach(identityLink -> {
                        String groupId = identityLink.getGroupId();
                        List<SysUser> sysUsers=flowableUserService.getUserByFlowGroupId(Long.parseLong(groupId));
                        userList.addAll(sysUsers);
                    });
            identityLinksForTask.stream().filter(identityLink ->
                    !StringUtils.isEmpty(identityLink.getUserId())
                            &&identityLink.getType().equals(type)
            )
                    .forEach(identityLink -> {
                        String userId = identityLink.getUserId();
                        SysUser sysUsers = sysUserService.selectUserById(Long.parseLong(userId));
                        userList.add(sysUsers);
                    });
        }
        return userList;
    }

    /**
     * starter，USER_ID与PROC_INST_ID_，记录流程的发起者
     * candidate，USER_ID_ 或 GROUP_ID_ 其中一个必须有值、TASK_ID_有值，记录当前任务的指派人与指派组。
     * participant， USER_ID与PROC_INST_ID_有值，记录流程任务的参与者。
     * @param taskId
     * @return
     */
    @Override
    public Set<SysUser> getHistoricIdentityLinksForTask(String taskId){
        Set<SysUser> userList=new HashSet<>();
        List<HistoricIdentityLink> historicIdentityLinksForTask = historyService.getHistoricIdentityLinksForTask(taskId);
        if(!CollectionUtils.isEmpty(historicIdentityLinksForTask)){
            historicIdentityLinksForTask.stream().filter(identityLink -> !StringUtils.isEmpty(identityLink.getGroupId())
                    &&identityLink.getType().equals("candidate"))
                    .forEach(identityLink -> {
                        String groupId = identityLink.getGroupId();
                        List<SysUser> sysUsers=flowableUserService.getUserByFlowGroupId(Long.parseLong(groupId));
                        userList.addAll(sysUsers);
                    });
            historicIdentityLinksForTask.stream().filter(identityLink -> !StringUtils.isEmpty(identityLink.getUserId())
                    &&identityLink.getType().equals("candidate"))
                    .forEach(identityLink -> {
                        String userId = identityLink.getUserId();
                        SysUser sysUsers = sysUserService.selectUserById(Long.parseLong(userId));
                        userList.add(sysUsers);
                    });
        }
        return userList;
    }

    @Override
    public Task getTask(String taskId) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        return task;
    }

    @Override
    public HistoricTaskInstanceVO getHisTask(String taskId) {
        HistoricTaskInstanceVO historicTaskInstanceVO=new HistoricTaskInstanceVO();
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .includeIdentityLinks()
                .includeTaskLocalVariables()
                .includeProcessVariables()
                .singleResult();
        BeanUtil.copyProperties(historicTaskInstance,historicTaskInstanceVO);
        historicTaskInstanceVO.setTaskId(historicTaskInstance.getId());
        historicTaskInstanceVO.setTaskName(historicTaskInstance.getName());
        //老的createTime底层换成了time getCreateTime
        historicTaskInstanceVO.setStartTime(historicTaskInstance.getTime());
        historicTaskInstanceVO.setCompleteTime(historicTaskInstance.getEndTime());
        historicTaskInstanceVO.setTaskLocalVariables(historicTaskInstance.getTaskLocalVariables());
        historicTaskInstanceVO.setProcessVariables(historicTaskInstance.getProcessVariables());
        warpTaskVoList(historicTaskInstanceVO);
        return historicTaskInstanceVO;
    }


    @Override
    public  void submitTask(FinishTaskDTO finishTaskDTO) {
        Task task = this.getTask(finishTaskDTO.getTaskId());
        if(StringUtils.isEmpty(task)){
            log.info("完成任务时，该任务ID:%不存在",finishTaskDTO.getTaskId());
            throw new BusinessException(String.format("该任务ID:%不存在",finishTaskDTO.getTaskId()));
        }
        AppForm appFrom = appFormService.getAppFrom(task.getProcessInstanceId());
        ////设置审批人，若不设置则数据表userid字段为null
        Authentication.setAuthenticatedUserId(finishTaskDTO.getUserId());
        if(!StringUtils.isEmpty(finishTaskDTO.getComment())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),FlowConstants.OPINION,finishTaskDTO.getComment());
        }else {
            taskService.addComment(task.getId(),task.getProcessInstanceId(),FlowConstants.OPINION,"");
        }

        List<FileEntry> files = finishTaskDTO.getFiles();
        if(!CollectionUtils.isEmpty(files)){
            files.forEach(t->{
                if(ObjectUtil.isNull(t.getKey())) return;
                taskService.createAttachment("url",task.getId(),task.getProcessInstanceId(),t.getName(),t.getKey(),t.getUrl());
            });
        }
        Map<String, Object> paramMap = BeanUtil.beanToMap(finishTaskDTO);
        paramMap.remove(FinishTaskDTO.COMMENT);
        paramMap.remove(FinishTaskDTO.FILES);
        if(!CollectionUtils.isEmpty(paramMap)){
            Set<Map.Entry<String, Object>> entries = paramMap.entrySet();
            entries.forEach(t-> runtimeService.setVariable(task.getExecutionId(),t.getKey(),t.getValue()));
        }
        //修改业务数据的时候流程业务数据重新赋值
        if(finishTaskDTO.getIsUpdateBus()&&appFrom!=null){
            MyBeanUtils.copyProperties(finishTaskDTO,appFrom);
            runtimeService.setVariable(task.getExecutionId(),FlowConstants.APP_FORM,appFrom);
        }
        // owner不为空说明可能存在委托任务
        if (ObjectUtil.isNotNull(task.getOwner())) {
            DelegationState delegationState = task.getDelegationState();
            switch (delegationState) {
                //委派中
                case PENDING:
                    // 被委派人处理完成任务
                    taskService.resolveTask(task.getId(),paramMap);
                    break;
                default:
                    taskService.claim(task.getId(),finishTaskDTO.getUserId());
                    taskService.complete(task.getId(),paramMap,true);
                    break;
            }
        } else {
            //claim the task，当任务分配给了某一组人员时，需要该组人员进行抢占。抢到了就将该任务给谁处理，其他人不能处理。认领任务
            taskService.claim(task.getId(),finishTaskDTO.getUserId());
            taskService.complete(task.getId(),paramMap,true);
        }
        //推进自动推动的节点
        taskSkipService.autoSkip(task.getProcessInstanceId());
    }

    @Override
    public void automaticTask(String processInstanceId){
        FinishTaskDTO completeTaskDTO=new FinishTaskDTO();
        Task task=flowableService.getTaskProcessInstanceById(processInstanceId);
        completeTaskDTO.setTaskId(task.getId());
        completeTaskDTO.setIsStart(true);
        completeTaskDTO.setIsPass(true);
        completeTaskDTO.setUserId(task.getAssignee());
        submitTask(completeTaskDTO);
    }

    @Override
    public void transferTask(String taskId, String curUserId, String targetUserId) {
        try {
            //todo 转办记录
            taskService.setOwner(taskId, curUserId);
            taskService.setAssignee(taskId,targetUserId);
        }catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            throw new RuntimeException("转办任务失败，请联系管理员");
        }

    }

    /**
     * 委派：是将任务节点分给其他人处理，等其他人处理好之后，委派任务会自动回到委派人的任务中
     * @param taskId 任务ID
     * @param curUserId 当前人ID
     * @param targetUserId 目标人ID
     */
    @Override
    public void delegateTask(String taskId, String curUserId, String targetUserId) {
        try {
            taskService.setOwner(taskId, curUserId);
            taskService.delegateTask(taskId,targetUserId);

        }catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            throw new RuntimeException("转办任务失败，请联系管理员");
        }
    }


}
