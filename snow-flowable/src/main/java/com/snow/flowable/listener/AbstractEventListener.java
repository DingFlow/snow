package com.snow.flowable.listener;


import com.snow.flowable.common.enums.FlowDefEnum;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.*;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.*;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.variable.api.event.FlowableVariableEvent;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * @author qimingjin
 * @Title: 全局事件监听
 * @Description: 每一个类型对应于org.flowable.engine.common.api.delegate.event.FlowableEventType下的一个枚举值。
 * ENGINE_CREATED：该监听器所附加的process engine已被创建且准备好接受API调用；org.flowable…​FlowableEvent。
 * ENGINE_CLOSED：该监听器所附加的process engine已被关闭且API调用将不可用；org.flowable…​FlowableEvent。
 * ENTITY_CREATED：一个新entity被创建，这个新entity包含在事件中；org.flowable…​FlowableEntityEvent。
 * ENTITY_INITIALIZED：一个新entity被创建并被完全初始化，如果该entity有子entity创建则该事件直到所有子entity完成创建后才会发出；org.flowable…​FlowableEntityEvent。
 * ENTITY_UPDATED：一个已存在的entity被升级，被升级的entity包含在事件中；org.flowable…​FlowableEntityEvent。
 * ENTITY_DELETED：一个已存在的entity被删除，被删除的entity包含在事件中；org.flowable…​FlowableEntityEvent。
 * ENTITY_SUSPENDED：一个已存在的entity被暂停，被暂停的entity包含在事件中，将被ProcessDefinitions、ProcessInstances、Tasks分派；org.flowable…​FlowableEntityEvent。
 * ENTITY_ACTIVATED：一个已存在的entity被激活，被激活的entity包含在事件中，将被ProcessDefinitions、ProcessInstances、Tasks分派；org.flowable…​FlowableEntityEvent。
 * JOB_EXECUTION_SUCCESS：一项作业已执行成功，该事件包含被执行的作业；org.flowable…​FlowableEntityEvent。
 * JOB_EXECUTION_FAILURE：一项作业已执行失败，该事件包含被执行的作业及异常；org.flowable…​FlowableEntityEvent和org.flowable…​FlowableExceptionEvent。
 * JOB_RETRIES_DECREMENTED：由于作业失败，作业重试次数已减少，该事件包含已更新的作业；org.flowable…​FlowableEntityEvent。
 * TIMER_SCHEDULED：一项记时作业被创建并计划在未来某个时间点执行；org.flowable…​FlowableEntityEvent。
 * TIMER_FIRED：计时器被触发，该事件包含已被执行的作业；org.flowable…​FlowableEntityEvent。
 * JOB_CANCELED：作业被取消，该事件包含已被取消的作业，在新的流程定义部署中，可以通过API调用取消作业，完成任务并取消关联的边界计时器；org.flowable…​FlowableEntityEvent。
 * ACTIVITY_STARTED：一项活动开始执行；org.flowable…​FlowableActivityEvent。
 * ACTIVITY_COMPLETED：一项活动执行成功；org.flowable…​FlowableActivityEvent。
 * ACTIVITY_CANCELLED：一项活动将被取消，可能有三个原因（MessageEventSubscriptionEntity, SignalEventSubscriptionEntity, TimerEntity）；org.flowable…​FlowableActivityCancelledEvent。
 * ACTIVITY_SIGNALED：一项活动收到信号；org.flowable…​FlowableSignalEvent。
 * ACTIVITY_MESSAGE_RECEIVED：一项活动收到了消息；在活动收到消息之前进行调度，收到后，将根据类型（边界事件或事件子进程启动事件）为此活动调度ACTIVITY_SIGNAL或ACTIVITY_STARTED；org.flowable…​FlowableMessageEvent。
 * ACTIVITY_MESSAGE_WAITING：一项活动创建了一个消息事件订阅且正在等待接收；org.flowable…​FlowableMessageEvent。
 * ACTIVITY_MESSAGE_CANCELLED：已经为其创建了消息事件订阅的活动被取消，因此接收该消息将不再触发该特定消息；org.flowable…​FlowableMessageEvent
 * ACTIVITY_ERROR_RECEIVED：一项活动收到错误事件；在活动处理实际错误之前进行调度；事件的activityId包含对错误处理活动的引用。如果错误成功传递，则此事件将跟随ACTIVITY_SIGNALLED或ACTIVITY_COMPLETE用于所涉及的活动；org.flowable…​FlowableErrorEvent
 * UNCAUGHT_BPMN_ERROR：抛出未处理的BPMN错误，该process没有针对该特定错误的任何处理程序，此事件的activityId将为空；org.flowable…​FlowableErrorEvent。
 * ACTIVITY_COMPENSATE：一项活动即将得到补偿，该事件包含将要执行以进行补偿的活动的ID；org.flowable…​FlowableActivityEvent。
 * MULTI_INSTANCE_ACTIVITY_STARTED：一个多实例活动将被执行；org.flowable…​FlowableMultiInstanceActivityEvent。
 * MULTI_INSTANCE_ACTIVITY_COMPLETED：一个多实例活动已成功执行完毕；org.flowable…​FlowableMultiInstanceActivityEvent。
 * MULTI_INSTANCE_ACTIVITY_CANCELLED：一个多实例活动将被取消，可能有三个原因（MessageEventSubscriptionEntity, SignalEventSubscriptionEntity, TimerEntity）；org.flowable…​FlowableMultiInstanceActivityCancelledEvent。
 * VARIABLE_CREATED：创建变量，该事件包含变量名称，以及相关执行和任务；org.flowable…​FlowableVariableEvent。
 * VARIABLE_UPDATED：现有变量已更新，该事件包含变量名称，更新值以及相关执行和任务（如果有）；org.flowable…​FlowableVariableEvent。
 * VARIABLE_DELETED：现有变量已删除，该事件包含变量名称，最后的值以及相关执行和任务（如果有）；org.flowable…​FlowableVariableEvent。
 * TASK_ASSIGNED：一个任务被分配给用户，该事件包含任务；org.flowable…​FlowableEntityEvent。
 * TASK_CREATED：一个任务被创建，这是在ENTITY_CREATE事件之后调度的，如果任务是process的一部分，则在执行任务监听器之前将触发此事件；org.flowable…​FlowableEntityEvent。
 * TASK_COMPLETED：一个任务已完成，这是在ENTITY_DELETE事件之前调度的。如果该任务是process的一部分，则在process继续之前将触发此事件，之后将跟随触发ACTIVITY_COMPLETE事件（该事件指向表示已完成任务的活动）；org.flowable…​FlowableEntityEvent。
 * PROCESS_CREATED：一个流程实例已经被创建，所有的基础属性被设置，但是还没有变量；org.flowable…​FlowableEntityEvent。
 * PROCESS_STARTED：一个流程实例已启动，在启动先前创建的流程实例时调度。在关联事件ENTITY_INITIALIZED之后和设置变量之后调度事件PROCESS_STARTED；org.flowable…​FlowableEntityEvent。
 * PROCESS_COMPLETED：一个流程已完成，表示流程实例已停止所有的执行，在最后一个活动ACTIVITY_COMPLETED事件之后调度，当流程达到流程实例没有任何转换的状态时，流程就完成了；org.flowable…​FlowableEntityEvent。
 * PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT：一个流程已经结束并达到最后的事件；org.flowable…​FlowableProcessTerminatedEvent。
 * PROCESS_CANCELLED：一个流程已被取消。在从运行时删除流程实例之前调度。例如，可以通过API调用RuntimeService.deleteProcessInstance，通过调用活动上的中断边界事件等方法来取消流程实例；org.flowable…​FlowableCancelledEvent。
 * MEMBERSHIP_CREATED：用户已添加到组中，该事件包含所涉及的用户和组的ID；org.flowable…​FlowableMembershipEvent。
 * MEMBERSHIP_DELETED：用户已从组中删除，该事件包含所涉及的用户和组的ID；org.flowable…​FlowableMembershipEvent。
 * MEMBERSHIPS_DELETED：所有成员都将从一个组中删除，在删除成员之前抛出该事件，因此仍可访问它们。出于性能原因，如果立即删除所有成员，则不会抛出任何单独的MEMBERSHIP_DELETED事件；org.flowable…​FlowableMembershipEvent。
 * 所有ENTITY_开头的事件都与引擎内entity相关，下面的列表显示了为哪些实体分派了哪些实体事件的概述：
 * <p>
 * ENTITY_CREATED, ENTITY_INITIALIZED, ENTITY_DELETED: Attachment, Comment, Deployment, Execution, Group, IdentityLink, Job, Model, ProcessDefinition, ProcessInstance, Task, User.
 * ENTITY_UPDATED: Attachment, Deployment, Execution, Group, IdentityLink, Job, Model, ProcessDefinition, ProcessInstance, Task, User.
 * ENTITY_SUSPENDED, ENTITY_ACTIVATED: ProcessDefinition, ProcessInstance/Execution, Task.
 * @date 2020/12/7 18:13
 */
@Slf4j
@Service
public abstract class AbstractEventListener extends AbstractFlowableEventListener {
    /**
     * 需要监听的类型集合
     */
    protected Set<FlowableEngineEventType> types;

    /**
     * 监听的流程集合
     */
    protected Set<FlowDefEnum> flowDefEnums;


    public AbstractEventListener() {
    }

    public AbstractEventListener(Set<FlowableEngineEventType> types, Set<FlowDefEnum> flowDefEnums) {
        this.types = types;
        this.flowDefEnums = flowDefEnums;
    }

    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        if (flowableEvent instanceof FlowableEngineEvent) {
            FlowableEngineEvent flowableEngineEvent = (FlowableEngineEvent) flowableEvent;
            initProcessDefinition(flowableEngineEvent);
        }
    }

    /**
     * 初始化监听哪些流程
     *
     * @param flowableEngineEvent
     */
    public void initProcessDefinition(FlowableEngineEvent flowableEngineEvent) {
        ProcessDefinitionEntity processDefinition = getProcessDefinition(flowableEngineEvent);
        if (flowDefEnums != null && processDefinition != null) {
            String key = Optional.ofNullable(processDefinition.getKey()).orElse("");
            for (FlowDefEnum flowDefEnum : flowDefEnums) {
                //在流程中存在的才监听
                if (flowDefEnum.getCode().equals(key)) {
                    initEngineEventType(flowableEngineEvent);
                }
            }
        }

    }

    /**
     * 初始化事件类型
     *
     * @param flowableEngineEvent
     */
    public void initEngineEventType(FlowableEngineEvent flowableEngineEvent) {
        FlowableEngineEventType engineEventType = (FlowableEngineEventType) flowableEngineEvent.getType();
        if (types == null || types.contains(engineEventType)) {
            switch (engineEventType) {
                case ENTITY_CREATED:
                    entityCreated((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case ENTITY_INITIALIZED:
                    entityInitialized((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case ENTITY_UPDATED:
                    entityUpdated((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case ENTITY_DELETED:
                    entityDeleted((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case ENTITY_SUSPENDED:
                    entitySuspended((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case ENTITY_ACTIVATED:
                    entityActivated((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case TIMER_SCHEDULED:
                    timerScheduled((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case TIMER_FIRED:
                    timerFired((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case JOB_CANCELED:
                    jobCancelled((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case JOB_EXECUTION_SUCCESS:
                    jobExecutionSuccess((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case JOB_EXECUTION_FAILURE:
                    jobExecutionFailure((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case JOB_RETRIES_DECREMENTED:
                    jobRetriesDecremented((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case JOB_RESCHEDULED:
                    jobRescheduled((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case CUSTOM:
                    custom(flowableEngineEvent);
                    break;
                case ENGINE_CREATED:
                    engineCreated((FlowableProcessEngineEvent) flowableEngineEvent);
                    break;
                case ENGINE_CLOSED:
                    engineClosed((FlowableProcessEngineEvent) flowableEngineEvent);
                    break;
                case ACTIVITY_STARTED:
                    activityStarted((FlowableActivityEvent) flowableEngineEvent);
                    break;
                case ACTIVITY_COMPLETED:
                    activityCompleted((FlowableActivityEvent) flowableEngineEvent);
                    break;
                case ACTIVITY_CANCELLED:
                    activityCancelled((FlowableActivityCancelledEvent) flowableEngineEvent);
                    break;
                case MULTI_INSTANCE_ACTIVITY_STARTED:
                    multiInstanceActivityStarted((FlowableMultiInstanceActivityEvent) flowableEngineEvent);
                    break;
                case MULTI_INSTANCE_ACTIVITY_COMPLETED:
                    multiInstanceActivityCompleted((FlowableMultiInstanceActivityCompletedEvent) flowableEngineEvent);
                    break;
                case MULTI_INSTANCE_ACTIVITY_COMPLETED_WITH_CONDITION:
                    multiInstanceActivityCompletedWithCondition((FlowableMultiInstanceActivityCompletedEvent) flowableEngineEvent);
                    break;
                case MULTI_INSTANCE_ACTIVITY_CANCELLED:
                    multiInstanceActivityCancelled((FlowableMultiInstanceActivityCancelledEvent) flowableEngineEvent);
                    break;
                case ACTIVITY_SIGNAL_WAITING:
                    activitySignalWaiting((FlowableSignalEvent) flowableEngineEvent);
                    break;
                case ACTIVITY_SIGNALED:
                    activitySignaled((FlowableSignalEvent) flowableEngineEvent);
                    break;
                case ACTIVITY_COMPENSATE:
                    activityCompensate((FlowableActivityEvent) flowableEngineEvent);
                    break;
                case ACTIVITY_MESSAGE_WAITING:
                    activityMessageWaiting((FlowableMessageEvent) flowableEngineEvent);
                    break;
                case ACTIVITY_MESSAGE_RECEIVED:
                    activityMessageReceived((FlowableMessageEvent) flowableEngineEvent);
                    break;
                case ACTIVITY_MESSAGE_CANCELLED:
                    activityMessageCancelled((FlowableMessageEvent) flowableEngineEvent);
                    break;
                case ACTIVITY_ERROR_RECEIVED:
                    activityErrorReceived((FlowableErrorEvent) flowableEngineEvent);
                    break;
                case HISTORIC_ACTIVITY_INSTANCE_CREATED:
                    historicActivityInstanceCreated((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case HISTORIC_ACTIVITY_INSTANCE_ENDED:
                    historicActivityInstanceEnded((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case SEQUENCEFLOW_TAKEN:
                    sequenceFlowTaken((FlowableSequenceFlowTakenEvent) flowableEngineEvent);
                    break;
                case VARIABLE_CREATED:
                    variableCreated((FlowableVariableEvent) flowableEngineEvent);
                    break;
                case VARIABLE_UPDATED:
                    variableUpdatedEvent((FlowableVariableEvent) flowableEngineEvent);
                    break;
                case VARIABLE_DELETED:
                    variableDeletedEvent((FlowableVariableEvent) flowableEngineEvent);
                    break;
                case TASK_CREATED:
                    taskCreated((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case TASK_ASSIGNED:
                    taskAssigned((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case TASK_COMPLETED:
                    taskCompleted((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case PROCESS_CREATED:
                    processCreated((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case PROCESS_STARTED:
                    processStarted((FlowableProcessStartedEvent) flowableEngineEvent);
                    break;
                case PROCESS_COMPLETED:
                    processCompleted((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT:
                    processCompletedWithTerminateEnd((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case PROCESS_COMPLETED_WITH_ERROR_END_EVENT:
                    processCompletedWithErrorEnd((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case PROCESS_CANCELLED:
                    processCancelled((FlowableCancelledEvent) flowableEngineEvent);
                    break;
                case HISTORIC_PROCESS_INSTANCE_CREATED:
                    historicProcessInstanceCreated((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
                case HISTORIC_PROCESS_INSTANCE_ENDED:
                    historicProcessInstanceEnded((FlowableEngineEntityEvent) flowableEngineEvent);
                    break;
            }
        }
    }


    @Override
    public boolean isFailOnException() {
        return true;
    }

    protected void entityCreated(FlowableEngineEntityEvent event) {
    }

    protected void entityInitialized(FlowableEngineEntityEvent event) {
    }

    protected void entityUpdated(FlowableEngineEntityEvent event) {
    }

    protected void entityDeleted(FlowableEngineEntityEvent event) {
    }

    protected void entitySuspended(FlowableEngineEntityEvent event) {
    }

    protected void entityActivated(FlowableEngineEntityEvent event) {
    }

    protected void timerScheduled(FlowableEngineEntityEvent event) {
    }

    protected void timerFired(FlowableEngineEntityEvent event) {
    }

    protected void jobCancelled(FlowableEngineEntityEvent event) {
    }

    protected void jobExecutionSuccess(FlowableEngineEntityEvent event) {
    }

    protected void jobExecutionFailure(FlowableEngineEntityEvent event) {
    }

    protected void jobRetriesDecremented(FlowableEngineEntityEvent event) {
    }

    protected void jobRescheduled(FlowableEngineEntityEvent event) {
    }

    protected void custom(FlowableEngineEvent event) {
    }

    protected void engineCreated(FlowableProcessEngineEvent event) {
    }

    protected void engineClosed(FlowableProcessEngineEvent flowableEngineEvent) {
    }

    protected void activityStarted(FlowableActivityEvent event) {
    }

    protected void activityCompleted(FlowableActivityEvent event) {
    }

    protected void activityCancelled(FlowableActivityCancelledEvent event) {
    }

    protected void multiInstanceActivityStarted(FlowableMultiInstanceActivityEvent event) {
    }

    protected void multiInstanceActivityCompleted(FlowableMultiInstanceActivityCompletedEvent event) {
    }

    protected void multiInstanceActivityCompletedWithCondition(FlowableMultiInstanceActivityCompletedEvent event) {
    }

    protected void multiInstanceActivityCancelled(FlowableMultiInstanceActivityCancelledEvent event) {
    }

    protected void activitySignalWaiting(FlowableSignalEvent event) {
    }

    protected void activitySignaled(FlowableSignalEvent event) {
    }

    protected void activityCompensate(FlowableActivityEvent event) {
    }

    protected void activityMessageWaiting(FlowableMessageEvent event) {
    }

    protected void activityMessageReceived(FlowableMessageEvent event) {
    }

    protected void activityMessageCancelled(FlowableMessageEvent event) {
    }

    protected void activityErrorReceived(FlowableErrorEvent event) {
    }

    protected void historicActivityInstanceCreated(FlowableEngineEntityEvent event) {
    }

    protected void historicActivityInstanceEnded(FlowableEngineEntityEvent event) {
    }

    protected void sequenceFlowTaken(FlowableSequenceFlowTakenEvent event) {
    }

    protected void variableCreated(FlowableVariableEvent event) {
    }

    protected void variableUpdatedEvent(FlowableVariableEvent event) {
    }

    protected void variableDeletedEvent(FlowableVariableEvent event) {
    }

    protected void taskCreated(FlowableEngineEntityEvent event) {
    }

    protected void taskAssigned(FlowableEngineEntityEvent event) {
    }

    protected void taskCompleted(FlowableEngineEntityEvent event) {
    }

    protected void processCreated(FlowableEngineEntityEvent event) {
    }

    protected void processStarted(FlowableProcessStartedEvent event) {
    }

    protected void processCompleted(FlowableEngineEntityEvent event) {
    }

    protected void processCompletedWithTerminateEnd(FlowableEngineEntityEvent event) {
    }

    protected void processCompletedWithErrorEnd(FlowableEngineEntityEvent event) {
    }

    protected void processCancelled(FlowableCancelledEvent event) {
    }

    protected void historicProcessInstanceCreated(FlowableEngineEntityEvent event) {
    }

    protected void historicProcessInstanceEnded(FlowableEngineEntityEvent event) {
    }

    /**
     * 获取流程执行信息
     *
     * @param event
     * @return
     */
    protected DelegateExecution getExecution(FlowableEngineEvent event) {
        String executionId = event.getExecutionId();

        if (executionId != null) {
            CommandContext commandContext = CommandContextUtil.getCommandContext();
            if (commandContext != null) {
                return CommandContextUtil.getExecutionEntityManager(commandContext).findById(executionId);
            }
        }
        return null;
    }

    /**
     * 获取流程定义信息
     *
     * @param event
     * @return
     */
    protected ProcessDefinitionEntity getProcessDefinition(FlowableEngineEvent event) {
        String processDefinitionId = event.getProcessDefinitionId();
        if (processDefinitionId != null) {
            CommandContext commandContext = CommandContextUtil.getCommandContext();
            if (commandContext != null) {
                return CommandContextUtil.getProcessDefinitionEntityManager(commandContext).findById(processDefinitionId);
            }
        }
        return null;
    }

    /**
     * 获取流程执行信息
     *
     * @param executionId
     * @return
     */
    protected DelegateExecution getExecutionById(String executionId) {
        if (executionId != null) {
            CommandContext commandContext = CommandContextUtil.getCommandContext();
            if (commandContext != null) {
                return CommandContextUtil.getExecutionEntityManager(commandContext).findById(executionId);
            }
        }
        return null;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}
