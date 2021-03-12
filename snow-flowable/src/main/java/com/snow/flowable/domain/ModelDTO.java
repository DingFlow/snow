package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-28 14:54
 **/
@Data
public class ModelDTO  extends FlowBaseDTO implements Serializable {


    private static final long serialVersionUID = 4595121192334027366L;

    protected String id;

    protected String category;

    protected String categoryLike;


    protected String name;

    protected String nameLike;

    protected String key;

    protected Integer version;



    protected String deploymentId;

    protected boolean notDeployed;

    protected boolean deployed;


}
