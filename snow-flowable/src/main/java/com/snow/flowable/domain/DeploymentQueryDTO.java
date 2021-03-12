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

    private static final long serialVersionUID = -2575657191082354869L;
    /**
     * 发布key  模糊
     */
    private String deploymentKeyLike;

    /**
     * 发布id
     */
    private String deploymentId;

    /**
     * 发布名称 模糊
     */
    private String deploymentNameLike;

    /**
     * 流程定义key 模糊
     */
    private String processDefinitionKeyLike;

    /**
     * 发布分类
     */
    private String deploymentCategory;
}
