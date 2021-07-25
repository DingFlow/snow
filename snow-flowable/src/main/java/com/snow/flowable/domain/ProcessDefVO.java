package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProcessDefVO implements Serializable {
    private static final long serialVersionUID = -1084950842088533110L;

    /**
     * 流程定义id
     */
    private String id;

    /**
     * 流程定义名称
     */
    private String name;

    /**
     * 流程描述
     */
    private String description;

    /**
     * 流程定义key
     */
    private String key;

    /**
     * 当前版本
     */
    private int version;

    /**
     * 分类
     */
    private String category;

    /**
     * 流程发布id
     */
    private String deploymentId;


    /**
     * xml
     */
    private String resourceName;


    private Integer historyLevel;

    private String diagramResourceName;



    private boolean hasStartFormKey;

}
