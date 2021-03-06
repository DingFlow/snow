package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-06 19:06
 **/
@Data
public class TransferTaskDTO implements Serializable {

    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 目标用户id
     */
    private String targetUserId;
}
