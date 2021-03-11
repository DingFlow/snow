package com.snow.flowable.service;

import com.snow.flowable.domain.AppForm;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/12/7 10:19
 */
public interface AppFormService {

    /**
     * 从流程中获取表单数据
     * @param processInstanceId
     * @param <A>
     * @return
     */
    <A extends AppForm> A getAppFrom(String processInstanceId);

    /**
     * 根据执行id获取表单数据
     * @param executionId
     * @param <A>
     * @return
     */
    <A extends AppForm> A getAppFromByExecutionId(String executionId);


    /**
     * 根据任务id获取表单数据
     * @param taskId
     * @param <A>
     * @return
     */
    <A extends AppForm> A getAppFromByTaskId(String taskId);
}
