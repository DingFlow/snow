package com.snow.flowable;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.snow.JunitTestApplication;
import com.snow.flowable.domain.DeploymentQueryDTO;
import com.snow.flowable.domain.StartProcessDTO;
import com.snow.flowable.domain.TaskVO;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.flowable.service.impl.FlowableUserServiceImpl;
import com.snow.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.IdentityLinkInfo;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/20 16:05
 */
@Slf4j
public class FlowableServiceTests extends JunitTestApplication {
    @Autowired
    private FlowableService flowableService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private FlowableUserServiceImpl flowableUserService;

    @Autowired
    private FlowableTaskService flowableTaskService;

    @Test
    public void startProcessInstanceByKey(){
        StartProcessDTO startProcessDTO=new StartProcessDTO();
        startProcessDTO.setStartUserId("1");
        startProcessDTO.setProcessDefinitionKey("snow_leave");
        startProcessDTO.setBusinessKey("QJ000001");
        Map<String,Object> map=Maps.newHashMap();
        map.put("manager","2");
        startProcessDTO.setVariables(map);
        ProcessInstance processInstance = flowableService.startProcessInstanceByKey(startProcessDTO);
        log.info("发布结果:{}",JSON.toJSONString(processInstance));
    }

    @Test
    public void getDeploymentList(){
        flowableUserService.getUserByFlowGroupId(108L);
        //DeploymentQueryDTO startProcessDTO=new DeploymentQueryDTO();

    //    flowableService.getDeploymentList(startProcessDTO);

    }
    @Test
    public void getDynamicFlowNodeInfo(){
        List<TaskVO> dynamicFlowNodeInfo = flowableService.getDynamicFlowNodeInfo("38505061-2d6b-11eb-b0ec-040e3c9c6b2f");
        log.info(JSON.toJSONString(dynamicFlowNodeInfo));
    }

    @Test
    public void getTaskList(){
        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId("1337010679969882112");
        List<Task> list = taskQuery.list();
        list.forEach(task -> {
            List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
            flowableTaskService.getIdentityLinksForTask(task.getId(),"");
            log.info(JSON.toJSONString(identityLinksForTask));
        });

       
        log.info(JSON.toJSONString(list));
    }


}
