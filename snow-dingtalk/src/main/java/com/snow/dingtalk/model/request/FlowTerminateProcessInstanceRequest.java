package com.snow.dingtalk.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @program: snow
 * @description 终止流程实体
 * @author: 没用的阿吉
 * @create: 2021-03-17 13:34
 **/
@Data
public class FlowTerminateProcessInstanceRequest implements Serializable {
    private static final long serialVersionUID = -7143220394052511824L;

    /**
     *是否通过系统操作：
     *
     * true：由系统直接终止
     *
     * false：由指定的操作者终止
     */
    private Boolean isSystem;

    /**
     * 操作人的userid。
     *
     * 当is_system为false时，该参数必传。
     */
    private String operatingUserid;

    @NotBlank(message = "流程实例id不能为空")
    private String processInstanceId;

    /**
     * 终止说明
     */
    private String remark;
}
