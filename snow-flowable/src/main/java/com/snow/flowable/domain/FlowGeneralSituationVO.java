package com.snow.flowable.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: snow
 * @description 流程概况
 * @author: 没用的阿吉
 * @create: 2021-02-23 20:10
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlowGeneralSituationVO implements Serializable {

    private static final long serialVersionUID = 5709897875619881275L;

    /**
     * 待办数
     */
    private long todoTaskNum;

    /**
     * 已办数
     */
    private long doneTaskNum;

    /**
     * 我发起的流程数
     */
    private long myStartProcessInstanceNum;

    /**
     * 三天未办数
     */
    private long threeTodoTaskNum;


    /**
     * 流程数
     */
    private long processInstanceNum;

}
