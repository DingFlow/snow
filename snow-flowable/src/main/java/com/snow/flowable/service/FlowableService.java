package com.snow.flowable.service;

import com.snow.common.core.page.PageModel;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.domain.*;
import com.snow.flowable.domain.response.ProcessDefinitionResponse;
import com.snow.system.domain.ActDeModel;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @author qimingjin
 * @Title: 流程相关操作
 * @Description:
 * @date 2020/11/19 17:27
 */
public interface FlowableService {


    // =================== 模型相关操作 ===================
    /**
     *  保存model
     * @param actDeModel 保存模型所需实体
     */
    void saveModel(ActDeModel actDeModel);

    /**
     * 删除model
     * @param modelId 模型id
     */
    void deleteModel(String modelId);

    /**
     * 导出XML
     * @param modelId 模型id
     * @param response  响应对象
     */
    void exportModelXml(String modelId, HttpServletResponse response);

    /**
     * 展示XML
     * @param modelId 模型id
     * @param response 响应对象
     */
    void showModelXml(String modelId, HttpServletResponse response);


    // =================== 流程部署相关操作 ===================
    /**
     * 查询发布列表(分页)
     * @param deploymentQueryDTO 查询实体对象
     * @return 分页结果
     */
    PageModel<DeploymentVO> getDeploymentList(DeploymentQueryDTO deploymentQueryDTO);

    /**
     * 查询发布详情
     * @param id 发布id
     * @return 详情
     */
    DeploymentVO getDeploymentDetailById(String id);

    /**
     * 删除发布
     * @param ids id字符串，多个逗号隔开
     */
    void deleteDeployment(String ids);

    /**
     * 获取流程资源
     * @param id  发布id
     * @param resourceName 资源名称（可选参数）
     * @param type 资源类型  支持（xml|png）
     * @param response 响应对象
     */
     void getDeploymentSource(String id, String resourceName, String type,HttpServletResponse response);


    // =================== 流程实例相关操作 ===================

    /**
     * 通过processDefinitionKey发起流程
     * @param startProcessDTO 发起流程实体对象
     * @return 流程实体对象
     */
    ProcessInstance startProcessInstanceByKey(StartProcessDTO startProcessDTO);

    /**
     * DingFlow 特有form形式发起流程
     * @param appForm form对象
     * @return 流程实例对象
     */
    ProcessInstance startProcessInstanceByAppForm(AppForm appForm);

    /**
     * 获取运行中流程实例
     * @param id 流程实例id
     * @return 流程实例对象
     */
    ProcessInstance getProcessInstanceById(String id);

    /**
     * 获取历史流程实例
     * @param id 流程实例id
     * @return 流程实例对象
     */
    HistoricProcessInstance getHistoricProcessInstanceById(String id);

    /**
     * 获取组装后的流程实例
     * @param id 流程实例id
     * @return 组装后的实例对象
     */
    ProcessInstanceVO getProcessInstanceVoById(String id);

    /**
     * 根据流程实例id获取流程图
     *    该方法已废弃，不建议使用
     * @param httpServletResponse 响应对象
     * @param processId 流程实例id
     */
     @Deprecated
     void getProcessDiagram(HttpServletResponse httpServletResponse, String processId);


    /**
     * 根据流程实例ID查询任务
     * @param id 流程实例id
     * @return 任务对象
     */
     Task getTaskProcessInstanceById(String id);


    /**
     * 动态获取流程节点审批信息
     * @param processInstanceId 流程实例id
     */
    List<TaskVO> getDynamicFlowNodeInfo(String processInstanceId);

    /**
     * 获取历史流程
     * @param processInstanceDTO 查询对象
     * @return 流程实例集合
     */
    List<ProcessInstanceVO> getHistoricProcessInstanceList(ProcessInstanceDTO processInstanceDTO);

    /**
     * 查询历史流程实例(分页)
     * 可查询我发起的流程
     * @param processInstanceDTO 查询对象
     * @return 分页数据
     */
    PageModel<ProcessInstanceVO> getHistoricProcessInstance(ProcessInstanceDTO processInstanceDTO);


    /**
     * 获取流程图像，已执行节点和流程线高亮显示
     * @param processInstanceId 流程实例id
     * @param response 响应流程图
     */
    void getFlowableProcessImage(String processInstanceId, HttpServletResponse response);


    /**
     * 获取流程概况（流程大屏显示）
     *  @param userId 用户id
     */
    FlowGeneralSituationVO getFlowGeneralSituation(String userId);

    /**
     * 挂起或激活流程 （0--激活，1--挂起）
     * @param instanceId  流程实例id
     * @param suspendState 挂起状态
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
     * @return 所有流程枚举
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


    // =================== 任务相关操作 ===================

    // --- 任务相关操作已移到com.snow.flowable.service.FlowableTaskService 类 下面的方法已废弃，强烈建议不要使用

    /**
     * 完成任务
     * @param completeTaskDTO 完成任务所需参数
     */
    @Deprecated
    void completeTask(CompleteTaskDTO completeTaskDTO);
}
