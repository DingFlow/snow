package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-21 22:40
 **/
@Data
public class ProcessDefinitionVO implements Serializable {

    private static final long serialVersionUID = -1943425916705266957L;

    private String id;

    private String category;

    private String name;

    private String key;

    private String description;

    private int version;

    private String resourceName;

    private String deploymentId;

    private String diagramResourceName;

    private boolean hasStartFormKey;

    private boolean hasGraphicalNotation;

    private boolean isSuspended;

    private String tenantId;

    private String derivedFrom;

    private String derivedFromRoot;

    private int derivedVersion;

    private String engineVersion;
}
