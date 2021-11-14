package com.snow.flowable.domain;

import com.snow.flowable.common.enums.FlowDefEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-12-05 17:05
 **/
@Data
public abstract class AppForm implements Serializable {

    private static final long serialVersionUID = -4544643731565791031L;
    /**
     * 流程发起人
     */
    @NotBlank(message = "流程发起人不能为空")
    private String startUserId;


    /**
     * 业务参数
     */
    @NotBlank(message = "业务参数不能为空")
    private String businessKey;


    /**
     * 业务数据详情页URL
     */
    private String busVarUrl;

    /**
     * 流程申请单需实现此方法,返回申请单对应的流程定义.
     * 一个流程对应一个申请单.(暂时)
     *
     * @return 流程定义
     */
    public abstract FlowDefEnum getFlowDef();

}
