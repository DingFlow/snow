package com.snow.dingtalk.service;

import com.aliyun.dingtalktodo_1_0.models.GetTodoTaskBySourceIdResponseBody;
import com.dingtalk.api.response.OapiWorkrecordGetbyuseridResponse;
import com.snow.dingtalk.model.request.WorkrecordAddRequest;
import com.snow.dingtalk.model.request.WorkrecordGetbyuseridRequest;
import com.snow.system.domain.SysOaTask;
import com.snow.system.domain.SysOaTaskDistribute;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/21 14:28
 */
@Service
public interface WorkRecodeService  {

    /**
     * 创建工作待办
     * @param workrecordAddRequest
     * @return
     */
    @Deprecated
     String create(WorkrecordAddRequest workrecordAddRequest);


    /**
     * 创建待办（新）
     * @param sysOaTask
     * @return
     */
     String createTodoTask(SysOaTask sysOaTask);

    /**
     * 删除待办（新）
     * @param taskId
     * @return
     */
     String deleteTodoTask(String taskId);

    /**
     * 更新待办(新)
     * @param sysOaTask
     * @return
     */
    Boolean updateTodoTask(SysOaTask sysOaTask);

    /**
     * 更新待办状态（新）
     * @param status 是否
     * @return
     */
    Boolean updateTodoTaskExecutorStatus(String taskId,Boolean status);

    /**
     * 根据业务id获取待办详情(新)
     * @param businessId
     * @return
     */
    GetTodoTaskBySourceIdResponseBody getTodoTaskByBusinessId(String businessId);

    /**
     * 根据用户ID获取待办
     * @param workrecordGetbyuseridRequest
     * @return
     */
    @Deprecated
     OapiWorkrecordGetbyuseridResponse.PageResult getWorkRecordByUserId(WorkrecordGetbyuseridRequest workrecordGetbyuseridRequest);

    /**
     * 更新待办
     * @param userId
     * @param recordId
     * @return
     */
    @Deprecated
     Boolean update(String userId,String recordId);

}
