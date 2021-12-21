package com.snow.dingtalk.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-25 20:52
 **/
@Data
public class DingFinishTaskRequest implements Serializable {

    private static final long serialVersionUID = 4115040416030667805L;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 完成任务人
     */
    private String userId;
    /**
     * 是否通过
     */
    private Boolean isPass;

    /**
     * 是否通过
     */
    private Boolean isStart;

    /**
     * 审批意见
     */
    private String comment;



    /**
     * 是否修改业务参数
     */
    private Boolean isUpdateBus=false;



    public final static String IS_PASS="isPass";

    public final static String IS_START="isStart";

    public final static String FILES="files";

    public final static String COMMENT="comment";
}
