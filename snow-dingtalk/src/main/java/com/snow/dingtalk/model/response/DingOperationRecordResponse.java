package com.snow.dingtalk.model.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-24 15:45
 **/
@Data
public class DingOperationRecordResponse implements Serializable {
    private static final long serialVersionUID = -281729978382670599L;



    /**
     * AGREE：同意
     *
     * REFUSE：拒绝
     *
     * NONE
     */
    private String operationResult;
    /**
     * EXECUTE_TASK_NORMAL：正常执行任务
     *
     * EXECUTE_TASK_AGENT：代理人执行任务
     *
     * APPEND_TASK_BEFORE：前加签任务
     *
     * APPEND_TASK_AFTER：后加签任务
     *
     * REDIRECT_TASK：转交任务
     *
     * START_PROCESS_INSTANCE：发起流程实例
     *
     * TERMINATE_PROCESS_INSTANCE：终止(撤销)流程实例
     *
     * FINISH_PROCESS_INSTANCE：结束流程实例
     *
     * ADD_REMARK：添加评论
     *
     * redirect_process：审批退回
     */
    private String operationType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人钉钉id
     */
    private String userid;

    private String userName;

    /**
     * 操作时间。
     */
    private Date date;
}
