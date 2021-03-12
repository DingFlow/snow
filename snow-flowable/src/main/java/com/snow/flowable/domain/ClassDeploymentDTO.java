package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/19 15:13
 */
@Data
public class ClassDeploymentDTO extends DeploymentDTO implements Serializable {
    private static final long serialVersionUID = 5615661176888353845L;

    private String classPathResource;
}
