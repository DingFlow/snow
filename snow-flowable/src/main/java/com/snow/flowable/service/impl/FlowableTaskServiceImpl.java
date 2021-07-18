package com.snow.flowable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.snow.common.core.page.PageModel;
import com.snow.common.exception.BusinessException;
import com.snow.common.utils.bean.MyBeanUtils;
import com.snow.flowable.common.constants.FlowConstants;
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
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
            return taskVO;
        }).collect(Collectors.toList());
        PageModel<TaskVO> pageModel = new PageModel<> ();
        pageModel.setTotalCount((int)count);
        pageModel.setPagedRecords(taskVoList);
        return pageModel;
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
    public TaskVO getHisTask(String taskId) {
        TaskVO taskVO=new TaskVO();
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .includeIdentityLinks()
                .includeTaskLocalVariables()
                .includeProcessVariables()
                .singleResult();
        BeanUtil.copyProperties(historicTaskInstance,taskVO);
        return taskVO;
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
            files.stream().forEach(t->
                    taskService.createAttachment("url",task.getId(),task.getProcessInstanceId(),t.getName(),t.getKey(),t.getUrl())
            );
        }
        Map<String, Object> paramMap = BeanUtil.beanToMap(finishTaskDTO);
        paramMap.remove(FinishTaskDTO.COMMENT);
        paramMap.remove(FinishTaskDTO.FILES);
        if(!CollectionUtils.isEmpty(paramMap)){
            Set<Map.Entry<String, Object>> entries = paramMap.entrySet();
            entries.stream().forEach(t->
                    runtimeService.setVariable(task.getExecutionId(),t.getKey(),t.getValue())
            );
        }
        //修改业务数据的时候流程业务数据重新赋值
        if(finishTaskDTO.getIsUpdateBus()&&appFrom!=null){
            MyBeanUtils.copyProperties(finishTaskDTO,appFrom);
            runtimeService.setVariable(task.getExecutionId(),FlowConstants.APP_FORM,appFrom);
        }
        // owner不为空说明可能存在委托任务
        if (!StringUtils.isEmpty(task.getOwner())) {
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
