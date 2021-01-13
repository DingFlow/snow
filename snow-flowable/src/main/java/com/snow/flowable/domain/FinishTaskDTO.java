package com.snow.flowable.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-23 22:13
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinishTaskDTO implements Serializable {

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



    public final static String IS_PASS="isPass";

    public final static String IS_START="isStart";

    public final static String FILES="files";

    public final static String COMMENT="comment";
}
