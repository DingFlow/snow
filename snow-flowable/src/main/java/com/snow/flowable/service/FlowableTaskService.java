package com.snow.flowable.service;

import com.snow.common.core.page.PageModel;
import com.snow.flowable.domain.*;
import com.snow.system.domain.SysUser;
import org.flowable.task.api.Task;

import java.util.List;
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
     * 查询历史任务实例(分页)
     * 可查询我经办的
     * @param historicTaskInstanceDTO
     * @return
     */
    PageModel<HistoricTaskInstanceVO> getHistoricTaskInstance(HistoricTaskInstanceDTO historicTaskInstanceDTO);

    /**
     * 获取历史任务
     * @param
     * @return
     */
    List<HistoricTaskInstanceVO> getHistoricTaskInstanceNoPage(HistoricTaskInstanceDTO historicTaskInstanceDTO);


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
     * 根据id查询历史任务
     * @param taskId 任务id
     * @return
     */
    HistoricTaskInstanceVO getHisTask(String taskId);
    /**
     * 完成任务
     * @param finishTaskDTO
     * @param
     */
    void submitTask(FinishTaskDTO finishTaskDTO);

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


}
