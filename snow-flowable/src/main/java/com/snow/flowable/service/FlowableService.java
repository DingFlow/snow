package com.snow.flowable.service;

import com.snow.common.core.page.PageModel;
import com.snow.flowable.domain.*;
import com.snow.system.domain.ActDeModel;
import com.snow.system.domain.SysUser;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Model;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/19 17:27
 */
public interface FlowableService {

    /**
     *  保存model
     * @param actDeModel
     */
    void saveModel(ActDeModel actDeModel);

    /**
     * 删除model
     * @param modelId
     */
    void deleteModel(String modelId);

    /**
     * 导出XML
     * @param modelId
     * @param response
     */
    void exportModelXml(String modelId, HttpServletResponse response);

    /**
     * 展示XML
     * @param modelId
     * @param response
     */
    void showModelXml(String modelId, HttpServletResponse response);
    /**
     * 查询发布列表(分页)
     * @param deploymentQueryDTO
     * @return
     */
    PageModel<DeploymentVO> getDeploymentList(DeploymentQueryDTO deploymentQueryDTO);

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
     * APP开启流程
     * @param appForm
     * @return
     */
    ProcessInstance startProcessInstanceByAppForm(AppForm appForm);


    /**
     * 自动完成任务
     * @param processInstanceId
     */
    void automaticTask(String processInstanceId);

    /**
     * 根据任务ID获取代办
     * @param taskId
     * @return
     */
    Task getTask(String taskId);

    /**
     * 获取代办 分页获取
     * @param userId
     * @param taskBaseDTO
     * @return
     */
    PageModel<TaskVO> findTasksByUserId(String userId,TaskBaseDTO taskBaseDTO);

    /**
     * 根据任务ID获取关联待办人待办组的人
     * @param taskId
     * @return
     */
    Set<SysUser> getIdentityLinksForTask(String taskId,String type);

    /**
     * 获取历史的
     * @param taskId
     * @return
     */
    Set<SysUser>  getHistoricIdentityLinksForTask(String taskId);

    /**
     * 完成任务
     * @param completeTaskDTO
     */
     void completeTask(CompleteTaskDTO completeTaskDTO);

    /**
     * 完成任务
     * @param completeTaskDTO
     * @param <T>
     */
     <T extends CompleteTaskDTO> void submitTask(T completeTaskDTO);

    /**
     * 获取流程图
     * @param httpServletResponse
     * @param processId
     */
     @Deprecated
     void getProcessDiagram(HttpServletResponse httpServletResponse, String processId);

    /**
     * 获取流程实例
     * @param id
     * @return
     */
     ProcessInstance getProcessInstanceById(String id);

    /**
     * 获取历史流程实例
     * @param id
     * @return
     */
     HistoricProcessInstance getHistoricProcessInstanceById(String id);

    /**
     * 根据流程实例ID查询任务
     * @param id
     * @return
     */
     Task getTaskProcessInstanceById(String id);

    /**
     * 获取历史任务
     * @param
     * @return
     */
    List<HistoricTaskInstanceVO> getHistoricTaskInstanceNoPage(HistoricTaskInstanceDTO historicTaskInstanceDTO);

    /**
     * 动态获取流程节点审批信息
     * @param processInstanceId
     */
    List<TaskVO> getDynamicFlowNodeInfo(String processInstanceId);

    /**
     * 查询历史流程实例(分页)
     * 可查询我发起的流程
     * @param processInstanceDTO
     * @return
     */
    PageModel<ProcessInstanceVO> getHistoricProcessInstance(ProcessInstanceDTO processInstanceDTO);

    /**
     * 查询历史任务实例(分页)
     * 可查询我经办的
     * @param historicTaskInstanceDTO
     * @return
     */
    PageModel<HistoricTaskInstanceVO> getHistoricTaskInstance(HistoricTaskInstanceDTO historicTaskInstanceDTO);

    /**
     * 获取流程图像，已执行节点和流程线高亮显示
     */
    void getFlowableProcessImage(String processInstanceId, HttpServletResponse response);
}
