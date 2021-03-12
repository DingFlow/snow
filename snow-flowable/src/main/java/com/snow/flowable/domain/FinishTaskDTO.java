package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-23 22:13
 **/
@Data
public class FinishTaskDTO implements Serializable {

    private static final long serialVersionUID = -7366987554013643970L;
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
     * 审批节点的相关文件（只在审批节点展示）
     */
    private List<FileEntry> files;

    /**
     * 是否修改业务参数
     */
    private Boolean isUpdateBus=false;



    public final static String IS_PASS="isPass";

    public final static String IS_START="isStart";

    public final static String FILES="files";

    public final static String COMMENT="comment";
}
