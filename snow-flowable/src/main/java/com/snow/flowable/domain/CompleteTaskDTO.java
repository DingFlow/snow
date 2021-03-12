package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/20 14:10
 */
@Data
public class CompleteTaskDTO implements Serializable {

    private static final long serialVersionUID = -4443825862466752L;
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
     * 流程级别的相关文件（跟具体业务相关需要回写到业务库）
     */
    private List<FileGroup> flowFiles;
    /**
     * 特殊事项
     */
    private List<String> specialItems;
    /**
     * 参数map
     */
    private Map<String,Object> paramMap;


    public final static String IS_PASS="isPass";

    public final static String IS_START="isStart";
}
