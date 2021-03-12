package com.snow.flowable.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snow.common.utils.StringUtils;
import com.snow.flowable.domain.ClassDeploymentDTO;
import com.snow.flowable.domain.DeploymentDTO;
import com.snow.flowable.service.FlowablePublishService;
import com.snow.system.domain.ActDeModel;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.dmn.api.DmnDeployment;
import org.flowable.dmn.api.DmnRepositoryService;
import org.flowable.dmn.model.DmnDefinition;
import org.flowable.editor.dmn.converter.DmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.service.ModelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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


    @Autowired
    private ModelServiceImpl modelService;

    @Autowired
    private ObjectMapper objectMapper;



    @Autowired
    private DmnRepositoryService dmnRepositoryService;

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
                .category(deploymentDTO.getCategory())
                .name(deploymentDTO.getName())
                .key(deploymentDTO.getKey())
                .addBytes(deploymentDTO.getResourceName(),bytes)
                .deploy();
        return deploy;
    }

    @Override
    public Deployment createBytesDeploymentByModelId(String id) {
        Model model = modelService.getModel(id);
        if(StringUtils.isNull(model)){
            return null;
        }
        byte[] bpmnXML = modelService.getBpmnXML(model);
        InputStream inputStream = new ByteArrayInputStream(bpmnXML);
        DeploymentDTO deployModel = new DeploymentDTO();
        deployModel.setCategory("system_flow");
        deployModel.setName(model.getName());
        deployModel.setKey(model.getKey());
        //这个地方必须加.bpmn或者.bpmn20.xml后缀，不然数据不会生成act_re_procdef这个表的数据
        deployModel.setResourceName(model.getName()+".bpmn20.xml");
        Deployment deploy = createInputStreamDeployment(deployModel,inputStream);
        return deploy;
    }

    @Override
    public String deploymentByModelId(String id, int deploymentType) {
        Model model = modelService.getModel(id);
        if(StringUtils.isNull(model)){
            return null;
        }
        try {
            if(deploymentType==ActDeModel.MODEL_TYPE_DECISION_TABLE){
                JsonNode editorJsonNode = objectMapper.readTree(model.getModelEditorJson());
                DmnJsonConverter dmnJsonConverter=new DmnJsonConverter();
                DmnDefinition dmnDefinition = dmnJsonConverter.convertToDmn(editorJsonNode, model.getId(), model.getVersion(), null);
                DmnDeployment deploy = dmnRepositoryService.createDeployment()
                    .name(model.getName())
                    .addDmnModel(model.getName()+".dmn",dmnDefinition)
                    .category("system_dmn")
                    .deploy();
                return deploy.getId();
            }else if(deploymentType==ActDeModel.MODEL_TYPE_BPMN){
                Deployment deployment = createBytesDeploymentByModelId(id);
                return deployment.getId();
            }
            return null;
        } catch (IOException e) {
            log.error("deploymentByModelId is fail",e.getMessage());
            throw new RuntimeException("发布流程失败");
        }
    }

}
