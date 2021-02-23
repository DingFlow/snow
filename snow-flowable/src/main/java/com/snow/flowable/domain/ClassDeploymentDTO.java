package com.snow.flowable.domain;

import lombok.Data;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/19 15:13
 */
@Data
public class ClassDeploymentDTO extends DeploymentDTO {
    private String classPathResource;
}
