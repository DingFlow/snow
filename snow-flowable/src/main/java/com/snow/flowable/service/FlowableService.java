package com.snow.flowable.service;

import com.snow.common.core.page.PageModel;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.domain.*;
import com.snow.flowable.domain.response.ProcessDefinitionResponse;
import com.snow.system.domain.ActDeModel;
import com.snow.system.domain.SysUser;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
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
     * 查询发布详情
     * @param id
     * @return
     */
    DeploymentVO getDeploymentDetailById(String id);
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
     * 完成任务
     * @param completeTaskDTO
     */
    @Deprecated
    void completeTask(CompleteTaskDTO completeTaskDTO);


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
     * 获取组装后的流程实例
     * @param id 流程实例id
     * @return 组装后的实例对象
     */
    ProcessInstanceVO getProcessInstanceVoById(String id);

    /**
     * 根据流程实例ID查询任务
     * @param id
     * @return
     */
     Task getTaskProcessInstanceById(String id);


    /**
     * 动态获取流程节点审批信息
     * @param processInstanceId
     */
    List<TaskVO> getDynamicFlowNodeInfo(String processInstanceId);

    /**
     * 获取历史流程
     * @param processInstanceDTO
     * @return
     */
    List<ProcessInstanceVO> getHistoricProcessInstanceList(ProcessInstanceDTO processInstanceDTO);

    /**
     * 查询历史流程实例(分页)
     * 可查询我发起的流程
     * @param processInstanceDTO
     * @return
     */
    PageModel<ProcessInstanceVO> getHistoricProcessInstance(ProcessInstanceDTO processInstanceDTO);



    /**
     * 获取流程图像，已执行节点和流程线高亮显示
     */
    void getFlowableProcessImage(String processInstanceId, HttpServletResponse response);


    /**
     * 获取流程概况（流程大屏显示）
     */
    FlowGeneralSituationVO getFlowGeneralSituation(String userId);



    /**
     * 挂起或激活流程 （0--激活，1--挂起）
     * @param instanceId
     * @param suspendState
     */
    void suspendOrActiveProcessInstance(String instanceId, Integer suspendState);

    /**
     * 取消流程
     * @param instanceId 流程实例id
     * @param reason 理由
     */
    void cancelProcessInstance(String instanceId, String reason);
    /**
     * 获取流程定义实体信息
     *
     * @param event
     * @return ProcessDefinitionEntity
     */
     ProcessDefinitionEntity getProcessDefinition(FlowableEngineEvent event);


    /**
     * 获取所有流程定义枚举set
     * @return
     */
     Set<FlowDefEnum> getAllFlowDefEnumsSet();

    /**
     * 根据流程定义key获取流程
     * @param processDefinitionKey 流程定义
     * @return 流程集合
     */
     List<ProcessDefinitionResponse> getProcessDefByKey(String processDefinitionKey);

    /**
     * 获取流程历史参数
     * @param processId 流程实例id
     * @param key 参数key
     * @return 参数值
     */
     Object getHisVariable(String processId, String key);
}
