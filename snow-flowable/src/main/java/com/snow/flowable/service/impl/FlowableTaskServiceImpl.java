package com.snow.flowable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.snow.common.core.page.PageModel;
import com.snow.common.enums.WorkRecordStatus;
import com.snow.common.exception.BusinessException;
import com.snow.common.utils.bean.BeanUtils;
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
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        //????????????ID????????????
        Set<Long> sysRoles = flowableUserService.getFlowGroupByUserId(Long.parseLong(userId));

        TaskQuery taskQuery = taskService.createTaskQuery()
                .or()
                .taskCandidateOrAssigned(userId);
        //??????????????????????????????????????????????????????????????????????????????????????????
        if(CollUtil.isNotEmpty(sysRoles)) {
            List<String> roleIds = sysRoles.stream().map(String::valueOf).collect(Collectors.toList());
            taskQuery.taskCandidateGroupIn(roleIds).endOr();
        }

        if(StrUtil.isNotEmpty(taskBaseDTO.getProcessInstanceId())){
            taskQuery.processInstanceId(taskBaseDTO.getProcessInstanceId());
        }
        if(StrUtil.isNotEmpty(taskBaseDTO.getTaskId())){
            taskQuery.taskId(taskBaseDTO.getTaskId());
        }

        if(StrUtil.isNotEmpty(taskBaseDTO.getTaskName())){
            taskQuery.taskNameLike("%"+taskBaseDTO.getTaskName()+"%");
        }
        if(StrUtil.isNotEmpty(taskBaseDTO.getBusinessKey())){
            taskQuery.processInstanceBusinessKeyLike("%"+taskBaseDTO.getBusinessKey()+"%");
        }
        if(StrUtil.isNotEmpty(taskBaseDTO.getDefinitionKey())){
            taskQuery.processDefinitionKey(taskBaseDTO.getDefinitionKey());
        }
        if(StrUtil.isNotEmpty(taskBaseDTO.getProcessDefinitionName())){
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
            //??????????????????
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
    public  void submitTask(FinishTaskDTO finishTaskDTO) {
        Task task = this.getTask(finishTaskDTO.getTaskId());
        if(StringUtils.isEmpty(task)){
            log.debug("@@???????????????????????????ID:{}?????????",finishTaskDTO.getTaskId());
            throw new BusinessException(StrUtil.format("?????????ID:{}?????????",finishTaskDTO.getTaskId()));
        }
        AppForm appFrom = appFormService.getAppFrom(task.getProcessInstanceId());
        //??????????????????????????????????????????userid?????????null
        Authentication.setAuthenticatedUserId(finishTaskDTO.getUserId());
        //????????????
        taskService.addComment(task.getId(),task.getProcessInstanceId(),FlowConstants.OPINION,StrUtil.isNotEmpty(finishTaskDTO.getComment())?finishTaskDTO.getComment():"");
        //????????????
        List<FileEntry> files = finishTaskDTO.getFiles();
        if(CollUtil.isNotEmpty(files)){
            files.forEach(t->{
                if(ObjectUtil.isNull(t.getKey())) return;
                taskService.createAttachment(FlowConstants.URL,task.getId(),task.getProcessInstanceId(),t.getName(),t.getKey(),t.getUrl());
            });
        }
        //?????????????????????
        Map<String, Object> paramMap = BeanUtil.beanToMap(finishTaskDTO);
        paramMap.remove(FinishTaskDTO.COMMENT);
        paramMap.remove(FinishTaskDTO.FILES);
        if(CollUtil.isNotEmpty(paramMap)){
            Set<Map.Entry<String, Object>> entries = paramMap.entrySet();
            entries.forEach(t-> runtimeService.setVariable(task.getExecutionId(),t.getKey(),t.getValue()));
        }
        //?????????????????????????????????????????????????????????
        if(finishTaskDTO.getIsUpdateBus()&&appFrom!=null){
            BeanUtil.copyProperties(finishTaskDTO,appFrom);
            runtimeService.setVariable(task.getExecutionId(),FlowConstants.APP_FORM,appFrom);
        }
        // owner???????????????????????????????????????
        if (ObjectUtil.isNotNull(task.getOwner())) {
            switch (task.getDelegationState()) {
                //?????????
                case PENDING:
                    // ??????????????????????????????
                    taskService.resolveTask(task.getId(),paramMap);
                    break;
                default:
                    taskService.claim(task.getId(),finishTaskDTO.getUserId());
                    taskService.complete(task.getId(),paramMap,true);
                    break;
            }
        } else {
            //claim the task?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            taskService.claim(task.getId(),finishTaskDTO.getUserId());
            taskService.complete(task.getId(),paramMap,true);
        }
        //???????????????????????????
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
        this.submitTask(completeTaskDTO);
    }

    @Override
    public void transferTask(String taskId, String curUserId, String targetUserId) {
        try {
            //todo ????????????
            taskService.setOwner(taskId, curUserId);
            taskService.setAssignee(taskId,targetUserId);
        }catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            throw new RuntimeException("???????????????????????????????????????");
        }

    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * @param taskId ??????ID
     * @param curUserId ?????????ID
     * @param targetUserId ?????????ID
     */
    @Override
    public void delegateTask(String taskId, String curUserId, String targetUserId) {
        try {
            taskService.setOwner(taskId, curUserId);
            taskService.delegateTask(taskId,targetUserId);

        }catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            throw new RuntimeException("???????????????????????????????????????");
        }
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
            //?????????????????????
            if(ObjectUtil.isNotEmpty(url)&&ObjectUtil.isNotEmpty(processVariables.get("id"))){
                historicTaskInstanceVO.setFromDetailUrl(url+"/"+processVariables.get("id"));
            }

            historicTaskInstanceVO.setCompleteTime(t.getEndTime());
            //????????????????????????????????????????????????????????????????????????
            if(ObjectUtil.isNotNull(t.getEndTime())){
                String spendTime= DateUtil.formatBetween(t.getCreateTime(), t.getEndTime(), BetweenFormater.Level.SECOND);
                historicTaskInstanceVO.setHandleTaskTime(spendTime);
            }
            historicTaskInstanceVO.setTaskId(t.getId());
            historicTaskInstanceVO.setTaskName(t.getName());
            historicTaskInstanceVO.setStartTime(t.getCreateTime());
            //????????????
            Optional.ofNullable(historicTaskInstanceVO.getTaskLocalVariables()).ifPresent(u->{
                Object isPass =Optional.ofNullable(u.get(FlowConstants.IS_PASS)).orElse("");
                Object isStart =Optional.ofNullable(u.get(FlowConstants.IS_START)).orElse("");
                //??????????????????
                historicTaskInstanceVO.setIsPass(String.valueOf(isPass));
                historicTaskInstanceVO.setIsStart(String.valueOf(isStart));
            });
            //??????????????????
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
     * ??????????????????????????????
     * @param historicTaskInstanceDTO ?????????????????????
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
     * ????????????????????????
     * @param historicTaskInstanceVO
     */
   private void warpTaskVoList(HistoricTaskInstanceVO historicTaskInstanceVO){
       //???????????????
       Set<SysUser> identityLinksForTask = getHistoricIdentityLinksForTask(historicTaskInstanceVO.getTaskId());
       Optional.ofNullable(identityLinksForTask).ifPresent(m->{
           List<String> userNameList = identityLinksForTask.stream().map(SysUser::getUserName).collect(Collectors.toList());
           historicTaskInstanceVO.setHandleUserList(userNameList);
       });
       //?????????
       if (ObjectUtil.isNotEmpty(historicTaskInstanceVO.getAssignee())&&com.snow.common.utils.StringUtils.isNumeric(historicTaskInstanceVO.getAssignee())) {
           SysUser sysUser = sysUserService.selectUserById(Long.parseLong(historicTaskInstanceVO.getAssignee()));
           historicTaskInstanceVO.setAssigneeName(sysUser.getUserName());
       }
       //????????????
       Optional.ofNullable(historicTaskInstanceVO.getTaskLocalVariables()).ifPresent(u->{
           Object isPass =Optional.ofNullable(u.get(FlowConstants.IS_PASS)).orElse("");
           Object isStart =Optional.ofNullable(u.get(FlowConstants.IS_START)).orElse("");
           //??????????????????
           historicTaskInstanceVO.setIsPass(String.valueOf(isPass));
           historicTaskInstanceVO.setIsStart(String.valueOf(isStart));
       });
       //????????????
       List<Comment> comment = taskService.getTaskComments(historicTaskInstanceVO.getTaskId(), FlowConstants.OPINION);
       //????????????
       List<Attachment> taskAttachments = taskService.getTaskAttachments(historicTaskInstanceVO.getTaskId());
       historicTaskInstanceVO.setCommentList(comment);
       historicTaskInstanceVO.setAttachmentList(taskAttachments);
       //??????????????????
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
        if(CollUtil.isEmpty(identityLinksForTask)){
            return userList;
        }
        identityLinksForTask.stream().filter(identityLink -> StrUtil.isNotEmpty(identityLink.getGroupId()) &&identityLink.getType().equals(type))
                .forEach(identityLink -> {
                    String groupId = identityLink.getGroupId();
                    List<SysUser> sysUsers=flowableUserService.getUserByFlowGroupId(Long.parseLong(groupId));
                    userList.addAll(sysUsers);
                });
        identityLinksForTask.stream().filter(identityLink -> StrUtil.isNotEmpty(identityLink.getUserId()) &&identityLink.getType().equals(type))
                .forEach(identityLink -> {
                    String userId = identityLink.getUserId();
                    SysUser sysUsers = sysUserService.selectUserById(Long.parseLong(userId));
                    userList.add(sysUsers);
                });
        return userList;
    }

    /**
     * starter???USER_ID???PROC_INST_ID_???????????????????????????
     * candidate???USER_ID_ ??? GROUP_ID_ ???????????????????????????TASK_ID_??????????????????????????????????????????????????????
     * participant??? USER_ID???PROC_INST_ID_??????????????????????????????????????????
     * @param taskId
     * @return
     */
    @Override
    public Set<SysUser> getHistoricIdentityLinksForTask(String taskId){
        Set<SysUser> userList=new HashSet<>();
        List<HistoricIdentityLink> historicIdentityLinksForTask = historyService.getHistoricIdentityLinksForTask(taskId);
        if(CollUtil.isEmpty(historicIdentityLinksForTask)){
            return userList;
        }
        historicIdentityLinksForTask.stream().filter(identityLink -> StrUtil.isNotEmpty(identityLink.getGroupId()) &&identityLink.getType().equals("candidate"))
                .forEach(identityLink -> {
                    String groupId = identityLink.getGroupId();
                    List<SysUser> sysUsers=flowableUserService.getUserByFlowGroupId(Long.parseLong(groupId));
                    userList.addAll(sysUsers);
                });
        historicIdentityLinksForTask.stream().filter(identityLink -> StrUtil.isNotEmpty(identityLink.getUserId()) &&identityLink.getType().equals("candidate"))
                .forEach(identityLink -> {
                    String userId = identityLink.getUserId();
                    SysUser sysUsers = sysUserService.selectUserById(Long.parseLong(userId));
                    userList.add(sysUsers);
                });
        return userList;
    }

    @Override
    public Task getTask(String taskId) {
        return taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
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
        //??????createTime???????????????time getCreateTime
        historicTaskInstanceVO.setStartTime(historicTaskInstance.getTime());
        historicTaskInstanceVO.setCompleteTime(historicTaskInstance.getEndTime());
        historicTaskInstanceVO.setTaskLocalVariables(historicTaskInstance.getTaskLocalVariables());
        historicTaskInstanceVO.setProcessVariables(historicTaskInstance.getProcessVariables());
        warpTaskVoList(historicTaskInstanceVO);
        return historicTaskInstanceVO;
    }




}
