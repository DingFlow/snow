package com.snow.flowable.service;

import com.snow.flowable.domain.CompleteTaskDTO;
import com.snow.flowable.domain.StartProcessDTO;
import com.snow.flowable.domain.TaskBaseDTO;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/19 17:27
 */
public interface FlowableService {
    /**
     * 通过processDefinitionKey开始流程
     * @param startProcessDTO
     * @return
     */
    ProcessInstance startProcessInstanceByKey(StartProcessDTO startProcessDTO);



    /**
     * 根据任务ID获取代办
     * @param taskId
     * @return
     */
    Task getTask(String taskId);

    /**
     * 获取代办
     * @param userId
     * @param taskBaseDTO
     * @return
     */
     List<Task> findTasksByUserId(String userId,TaskBaseDTO taskBaseDTO);

     void completeTask(CompleteTaskDTO completeTaskDTO);
}
