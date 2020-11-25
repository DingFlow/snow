package com.snow.flowable.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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


    private String taskId;

    private String suggestion;

    private String suggestionFileUrl;
    /**
     * 0通过，1--驳回
     */
    private Integer checkStatus;
    /**
     * 业务参数
     */
    private String businessKey;
}
