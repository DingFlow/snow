package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-20 21:44
 **/
@Data
public class DeploymentQueryDTO extends FlowBaseDTO implements Serializable {

    private String deploymentKeyLike;

    private String deploymentId;

    private String deploymentNameLike;

    private String processDefinitionKeyLike;

    private String startUserId;
}
