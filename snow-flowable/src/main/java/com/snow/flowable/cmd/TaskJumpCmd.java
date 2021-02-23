package com.snow.flowable.cmd;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.FlowableEngineAgenda;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityManager;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/12 13:10
 */
public class TaskJumpCmd implements Command<Void> {

    // 任务ID
    private String taskId;

    // 目标节点ID
    private String targetNodeId;

    public TaskJumpCmd(String taskId, String targetNodeId) {
        this.taskId = taskId;
        this.targetNodeId = targetNodeId;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        // 获取命令执行实体管理器
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();
        // 获取任务对象管理器
        TaskEntityManager taskEntityManager = CommandContextUtil.getTaskServiceConfiguration().getTaskEntityManager();
        // 根据任务ID获取任务对象
        TaskEntity taskEntity = taskEntityManager.findById(taskId);
        // 根据任务对象属性excutionId获取流程实例对象
        ExecutionEntity executionEntity = executionEntityManager.findById(taskEntity.getExecutionId());
        // 根据流程执行实例中的流程模板定义ID属性获取流程对象
        Process process = ProcessDefinitionUtil.getProcess(executionEntity.getProcessDefinitionId());
        // 根据目标节点ID获取对应节点
        FlowElement targetFlowElement = ((Process) process).getFlowElement(targetNodeId);
        // 在上一流程实例对象中改变当前节点
        executionEntity.setCurrentFlowElement(targetFlowElement);
        // 获取流程计划 注: 翻译可能不对
        FlowableEngineAgenda agenda = CommandContextUtil.getAgenda();
        // 更新流程实例对象 注: 翻译可能不对
        agenda.planContinueProcessInCompensation(executionEntity);
        // 根据在任务对象中删除之前任务
        taskEntityManager.delete(taskId);
        return null;
    }
}
