package com.snow.dingtalk.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2022/1/4 17:54
 */
@Data
public class DingCreateTaskResponse implements Serializable {

    /**
     * 待办事项ID
     */
    private String taskId;

    /**
     * 待办事项执行人的userid
     */
    private String userid;


}
