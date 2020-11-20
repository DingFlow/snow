package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/20 13:43
 */
@Data
public class TaskBaseDTO implements Serializable {

    private String processInstanceId;

    private String definitionKey;

    private String taskId;

    private String businessKey;
    /**
     * 初始页
     */
    private int firstResult=0;
    /**
     * 每页数
     */
    private int maxResults=10;

}
