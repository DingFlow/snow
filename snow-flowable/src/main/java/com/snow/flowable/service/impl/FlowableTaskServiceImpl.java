package com.snow.flowable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Maps;
import com.snow.common.exception.BusinessException;
import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.domain.CompleteTaskDTO;
import com.snow.flowable.domain.FileEntry;
import com.snow.flowable.domain.FinishTaskDTO;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/13 15:54
 */
@Slf4j
public class FlowableTaskServiceImpl implements FlowableTaskService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FlowableService flowableService;

    @Override
    public <T extends FinishTaskDTO> void submitTask(T finishTaskDTO) {
        Task task = flowableService.getTask(finishTaskDTO.getTaskId());
        if(StringUtils.isEmpty(task)){
            log.info("完成任务时，该任务ID:%不存在",finishTaskDTO.getTaskId());
            throw new BusinessException(String.format("该任务ID:%不存在",finishTaskDTO.getTaskId()));
        }
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
        if(!StringUtils.isEmpty(finishTaskDTO.getIsPass())){
            runtimeService.setVariable(task.getExecutionId(),CompleteTaskDTO.IS_PASS,finishTaskDTO.getIsPass());
            paramMap.put(CompleteTaskDTO.IS_PASS,finishTaskDTO.getIsPass());
        }
        if(!StringUtils.isEmpty(finishTaskDTO.getIsStart())){
            runtimeService.setVariable(task.getExecutionId(),CompleteTaskDTO.IS_START,finishTaskDTO.getIsStart());
            paramMap.put(CompleteTaskDTO.IS_START,finishTaskDTO.getIsStart());
        }

        //claim the task，当任务分配给了某一组人员时，需要该组人员进行抢占。抢到了就将该任务给谁处理，其他人不能处理。认领任务
        taskService.claim(task.getId(),finishTaskDTO.getUserId());
        taskService.complete(task.getId(),paramMap,true);
    }

    @Override
    public void transferTask(String taskId, String curUserId, String targetUserId) {
        try {
            taskService.setOwner(taskId, curUserId);
            taskService.setAssignee(taskId,targetUserId);
        }catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            throw new RuntimeException("转办任务失败，请联系管理员");
        }

    }

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

    @Override
    public void suspendOrActiveApply(String instanceId, String suspendState) {

    }
}
