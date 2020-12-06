package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-12-05 17:05
 **/
@Data
public class AppForm implements Serializable {

    /**
     * 流程ID
     */
    private String processInstanceId;

    /**
     * 业务参数
     */
    private String businessKey;

    /**
     * 流程发起人
     */
    private String startUserId;

}
