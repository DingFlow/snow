package com.snow.flowable.service;

import com.snow.common.core.page.PageModel;
import com.snow.flowable.domain.*;
import com.snow.system.domain.SysUser;
import org.flowable.task.api.Task;

import java.util.Set;

/**
 * @author qimingjin
 * @Title: flowable 任务相关接口
 * @Description:
 * @date 2021/1/13 15:54
 */
public interface FlowableTaskService {


    /**
     * 获取代办 分页获取
     * @param userId
     * @param taskBaseDTO
     * @return
     */
    PageModel<TaskVO> findTasksByUserId(String userId, TaskBaseDTO taskBaseDTO);

    /**
     * 根据任务ID获取关联待办人待办组的人
     * @param taskId
     * @return
     */
    Set<SysUser> getIdentityLinksForTask(String taskId, String type);


    /**
     * 获取历史的
     * @param taskId
     * @return
     */
    Set<SysUser>  getHistoricIdentityLinksForTask(String taskId);
    /**
     * 根据ID获取任务
     * @param taskId
     * @return
     */
    Task getTask(String taskId);
    /**
     * 完成任务
     * @param finishTaskDTO
     * @param <T>
     */
    <T extends FinishTaskDTO> void submitTask(T finishTaskDTO);

    /**
     * 自动完成任务
     * @param processInstanceId
     */
    void automaticTask(String processInstanceId);
    /**
     * 转办任务
     * @param taskId 任务ID
     * @param curUserId 当前人ID
     * @param targetUserId 目标人ID
     */
   void transferTask(String taskId,String curUserId,String targetUserId);

    /**
     * 委派任务
     * @param taskId 任务ID
     * @param curUserId 当前人ID
     * @param targetUserId 目标人ID
     */
   void delegateTask(String taskId,String curUserId,String targetUserId);

    /**
     * 挂起或激活流程
     * @param instanceId
     * @param suspendState
     */
   void suspendOrActiveApply(String instanceId, String suspendState);

    /**
     * 获取流程概况（流程大屏显示）
     */
    FlowGeneralSituationVO getFlowGeneralSituation(String userId);
}
