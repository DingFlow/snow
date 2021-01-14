package com.snow.flowable.service;

import com.snow.flowable.domain.ClassDeploymentDTO;
import com.snow.flowable.domain.DeploymentDTO;
import org.flowable.engine.repository.Deployment;

import java.io.InputStream;

/**
 * @author qimingjin
 * @Title: 发布接口
 * @Description:
 * @date 2020/11/19 14:57
 */
public interface FlowablePublishService {
    /**
     *class部署
     * @return
     */
    Deployment createClassDeployment(ClassDeploymentDTO classDeploymentDTO);

    /**
     * 文本部署
     * @param filePath
     * @return
     */
    Deployment createStringDeployment(DeploymentDTO deploymentDTO,String filePath);

    /**
     * 流部署
     * @param deploymentDTO
     * @param inputStream
     * @return
     */
    Deployment createInputStreamDeployment(DeploymentDTO deploymentDTO,InputStream inputStream);

    /**
     * 压缩流部署
     * @param deploymentDTO
     * @param inputZipStream
     * @return
     */
    Deployment createZipInputStreamDeployment(DeploymentDTO deploymentDTO,InputStream inputZipStream);

    /**
     * 字节形式部署
     * @param deploymentDTO
     * @param bytes
     * @return
     */
    Deployment createBytesDeployment(DeploymentDTO deploymentDTO,byte[] bytes );

    /**
     * 根据modelId发布
     * @param id
     * @return
     */
    Deployment createBytesDeploymentByModelId(String id);


    /**
     * 根据模型ID发布
     * @param id
     * @param deploymentType
     * @return
     */
    String deploymentByModelId(String id,int deploymentType);
}
