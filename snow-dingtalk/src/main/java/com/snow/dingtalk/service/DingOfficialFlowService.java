package com.snow.dingtalk.service;

import com.dingtalk.api.response.OapiProcessListbyuseridResponse;
import com.dingtalk.api.response.OapiProcessTemplateManageGetResponse;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.snow.dingtalk.model.request.*;
import com.snow.dingtalk.model.response.DingCreateTaskResponse;

import java.util.List;

/**
 * @program: snow
 * @description 钉钉官方工作流
 * @author: 没用的阿吉
 * @create: 2021-03-17 10:23
 **/
public interface DingOfficialFlowService {

    /**
     *  创建审批模板
     * @param saveFlowRequest
     * @return
     */
    String saveProcess(SaveProcessRequest saveFlowRequest);

    /**
     * 发起审批实例（官方）
     * @return
     */
    String startProcessInstance(StartFlowRequest startFlowRequest);



    /**
     * 获取当前企业所有可管理的模版
     * @param userId
     */
    List<OapiProcessTemplateManageGetResponse.ProcessSimpleVO> getProcessTemplateManage(String userId);

    /**
     * 获取指定用户可见的审批表单列表
     * @return
     */
    OapiProcessListbyuseridResponse.HomePageProcessTemplateVo getProcessListByUserId( String userId);


    /**
     * 执行审批操作带附件
     * @param flowExecuteTaskRequest
     * @return
     */
    Boolean executeProcessInstance(FlowExecuteTaskRequest flowExecuteTaskRequest);

  // void addProcessInstanceComment();

    /**
     * 获取审批实例详情
     * @param processInstanceId
     * @return
     */
    OapiProcessinstanceGetResponse.ProcessInstanceTopVo getProcessInstanceDetail(String processInstanceId);


    /**
     * 终止流程
     * @param flowTerminateProcessInstanceRequest
     * @return
     */
    Boolean terminateProcessInstance(FlowTerminateProcessInstanceRequest flowTerminateProcessInstanceRequest);


    /**
     * 发起流程实例（自有OA)
     * @return
     */
    String saveFakeProcessInstance(StartFakeProcessInstanceRequest startFakeProcessInstanceRequest);


    List<DingCreateTaskResponse> createProcessTask(SaveTaskRequest saveTaskRequest);


    void updateProcessTask();
}
