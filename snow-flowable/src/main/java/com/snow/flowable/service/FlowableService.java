package com.snow.flowable.service;

import com.snow.flowable.domain.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import javax.servlet.http.HttpServletResponse;
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
     * 查询发布列表
     * @param deploymentQueryDTO
     * @return
     */
    List<DeploymentVO> getDeploymentList(DeploymentQueryDTO deploymentQueryDTO);

    /**
     * 删除发布
     * @param ids
     */
    void deleteDeployment(String ids);

    /**
     * 获取流程资源
     * @param id
     * @param resourceName
     * @param type
     * @param response
     */
     void getDeploymentSource(String id, String resourceName, String type,HttpServletResponse response);

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
    List<TaskVO> findTasksByUserId(String userId,TaskBaseDTO taskBaseDTO);

    /**
     * 完成任务
     * @param completeTaskDTO
     */
     void completeTask(CompleteTaskDTO completeTaskDTO);

    /**
     * 获取流程图
     * @param httpServletResponse
     * @param processId
     */
     void getProcessDiagram(HttpServletResponse httpServletResponse, String processId);
}
