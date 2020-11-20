package com.snow.flowable.service.impl;

import com.snow.common.utils.StringUtils;
import com.snow.flowable.domain.ClassDeploymentDTO;
import com.snow.flowable.domain.DeploymentDTO;
import com.snow.flowable.service.FlowablePublishService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/19 14:59
 */
@Service
@Slf4j
public class FlowablePublishServiceImpl implements FlowablePublishService {
    /**
     * 仓库服务类
     * 仓库指的是流程第定义文档的两个文件：bpmn文件和流程图片
     * DeploymentBuilder:用来定义流程部署的相关参数
     *
     * ProcessDefinitionQuery:用来构造查询流程定义相关参数
     *
     * NativeProcessDefinitionQuery:用来构造本地Sql查询流程定义相关参数
     *
     * DeploymentQuery:用来构造查询部署对象相关参数
     */
    @Autowired
    private RepositoryService repositoryService;


    /**
     * class部署
     * @return
     */
    @Override
    public Deployment createClassDeployment(ClassDeploymentDTO classDeploymentDTO) {
        Deployment deploy = repositoryService.createDeployment()
                .tenantId(classDeploymentDTO.getTenantId())
                .category(classDeploymentDTO.getCategory())
                .name(classDeploymentDTO.getName())
                .key(classDeploymentDTO.getKey())
                .addClasspathResource(classDeploymentDTO.getClassPathResource())
                .deploy();
        return deploy;
    }

    @Override
    public Deployment createStringDeployment(DeploymentDTO deploymentDTO, String filePath) {
        String text= IoUtil.readFileAsString(filePath);
        Deployment deploy = repositoryService.createDeployment()
                .tenantId(deploymentDTO.getTenantId())
                .category(deploymentDTO.getCategory())
                .name(deploymentDTO.getName())
                .key(deploymentDTO.getKey())
                .addString(filePath,text)
                .deploy();
        return deploy;
    }

    @Override
    public Deployment createInputStreamDeployment(DeploymentDTO deploymentDTO, InputStream inputStream) {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        if(StringUtils.isNotNull(deploymentDTO.getTenantId())){
            deploymentBuilder.tenantId(deploymentDTO.getTenantId());
        }
        if(StringUtils.isNotNull(deploymentDTO.getCategory())){
            deploymentBuilder.category(deploymentDTO.getCategory());
        }
        if(StringUtils.isNotNull(deploymentDTO.getKey())){
            deploymentBuilder.key(deploymentDTO.getKey());
        }
        Deployment deploy = deploymentBuilder.name(deploymentDTO.getName())
                .addInputStream(deploymentDTO.getResourceName(), inputStream)
                .deploy();
        return deploy;
    }

    @Override
    public Deployment createZipInputStreamDeployment(DeploymentDTO deploymentDTO, InputStream inputZipStream) {
        ZipInputStream zipInputStream = new ZipInputStream(inputZipStream);
        Deployment deploy = repositoryService.createDeployment()
                .tenantId(deploymentDTO.getTenantId())
                .category(deploymentDTO.getCategory())
                .name(deploymentDTO.getName())
                .key(deploymentDTO.getKey())
                .addZipInputStream(zipInputStream)
                .deploy();
        return deploy;
    }

    @Override
    public Deployment createBytesDeployment(DeploymentDTO deploymentDTO, byte[] bytes) {
        Deployment deploy = repositoryService.createDeployment()
                .tenantId(deploymentDTO.getTenantId())
                .category(deploymentDTO.getCategory())
                .name(deploymentDTO.getName())
                .key(deploymentDTO.getKey())
                .addBytes(deploymentDTO.getResourceName(),bytes)
                .deploy();
        return deploy;
    }


}
