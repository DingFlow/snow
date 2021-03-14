package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/19 15:11
 */
@Data
public class DeploymentDTO implements Serializable {

    private static final long serialVersionUID = -2406490446315149317L;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * key
     */
    private String key;
    /**
     * 分类
     */
    private String category;
    /**
     * 名称
     */
    private String name;
    /**
     * 文件名
     */
    private String resourceName;

    /**
     * 发布类型
     */
    private Integer deploymentType;
}
