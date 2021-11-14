package com.snow.dingtalk.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-17 13:09
 **/
@Data
public class FlowExecuteTaskRequest implements Serializable {


    private static final long serialVersionUID = -5629974602089296152L;

    /**
     * 操作人userid，
     */
    private String actionerUserid;

    /**
     * 文件
     */
    private File file;

    /**
     *流程实例id
     */
    private String processInstanceId;

    /**
     * 操作评论，可为空。
     */
    private String remark;

    /**
     * 审批操作，取值。
     *
     * agree：同意
     *
     * refuse：拒绝
     */
    private String result;

    /**
     * 任务节点id
     */
    private Long taskId;


    @Data
    public static class File {


        /**
         * 附件列表。
         */
        private List<Attachment> attachments;

        /**
         * 图片URL地址
         */
        private List<String> photos;

    }

    @Data
    public static class Attachment {


        private String fileId;

        private String fileName;

        private String fileSize;

        private String fileType;

        private String spaceId;

    }
}
