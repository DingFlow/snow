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
public class TaskBaseDTO extends FlowBaseDTO implements Serializable {

    private String processInstanceId;

    private String definitionKey;

    private String taskId;

    private String businessKey;


}
