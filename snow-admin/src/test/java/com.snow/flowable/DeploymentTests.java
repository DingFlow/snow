package com.snow.flowable;

import com.alibaba.fastjson.JSON;
import com.snow.JunitTestApplication;
import com.snow.flowable.domain.DeploymentDTO;
import com.snow.flowable.service.impl.FlowablePublishServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.repository.Deployment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/19 16:02
 */
@Slf4j
public class DeploymentTests extends JunitTestApplication {
    @Autowired
    private FlowablePublishServiceImpl flowAblePublishService;

    @Test
    public void createClassDeployment() throws Exception{
      //  InputStream in = new BufferedInputStream(new FileInputStream("D:\\ChromeCoreDownloads\\snow_leave.bpmn20.xml"));
        DeploymentDTO classDeploymentDTO=new DeploymentDTO();
     //   classDeploymentDTO.setKey("snow_leave");
        classDeploymentDTO.setName("请假流程");
      //  classDeploymentDTO.setCategory("snow");
        classDeploymentDTO.setResourceName("请假流程");
       // classDeploymentDTO.setTenantId("snow");
       // classDeploymentDTO.setClassPathResource("D:\\modeler\\leave.bpmn20.xml");
       // Deployment classDeployment = flowAblePublishService.createInputStreamDeployment(classDeploymentDTO,in);
        flowAblePublishService.deploymentByModelId("054863dc-5629-11eb-802b-040e3c9c6b2f",4);
       // log.info("发布结果:{}",JSON.toJSONString(classDeployment));
    }
}
