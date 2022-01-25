package com.snow.dingtalk.model.request;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2022/1/4 17:28
 */
@Data
public class SaveTaskRequest implements Serializable {

    /**
     * 审批实例ID
     */
    private String processInstanceId;

    /**
     * 待办事项列表
     */
    private List<TaskTopVo> tasks;

    @Data
    public static class TaskTopVo implements Serializable {
        private static final long serialVersionUID = 7216484491466141389L;

        /**
         * 待办事项执行人的userid
         */
        private String userId;

        /**
         * 待办事项跳转URL
         */
        private String url;

    }
}
