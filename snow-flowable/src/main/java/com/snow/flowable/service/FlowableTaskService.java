package com.snow.flowable.service;

import com.snow.flowable.domain.CompleteTaskDTO;
import com.snow.flowable.domain.FinishTaskDTO;

/**
 * @author qimingjin
 * @Title: flowable 任务相关接口
 * @Description:
 * @date 2021/1/13 15:54
 */
public interface FlowableTaskService {


    /**
     * 完成任务
     * @param finishTaskDTO
     * @param <T>
     */
    <T extends FinishTaskDTO> void submitTask(T finishTaskDTO);

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

   void suspendOrActiveApply(String instanceId, String suspendState);

}
