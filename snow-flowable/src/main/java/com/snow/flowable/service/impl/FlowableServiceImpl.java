package com.snow.flowable.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.snow.common.core.text.Convert;
import com.snow.common.exception.BusinessException;
import com.snow.flowable.domain.*;
import com.snow.flowable.service.FlowableService;
import com.snow.system.domain.SysRole;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysRoleService;
import com.snow.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.service.impl.TaskQueryProperty;
import org.flowable.ui.modeler.rest.app.AbstractModelBpmnResource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/19 17:27
 */
@Slf4j
@Service
public class FlowableServiceImpl implements FlowableService {
    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private SysUserServiceImpl sysUserService;


    @Override
    public List<DeploymentVO> getDeploymentList(DeploymentQueryDTO deploymentQueryDTO) {
        
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        if(!StringUtils.isEmpty(deploymentQueryDTO.getDeploymentNameLike())){
            deploymentQuery.deploymentKeyLike(deploymentQueryDTO.getDeploymentNameLike());
        }
        if(!StringUtils.isEmpty(deploymentQueryDTO.getDeploymentId())){
            deploymentQuery.deploymentId(deploymentQueryDTO.getDeploymentId());
        }
        if(!StringUtils.isEmpty(deploymentQueryDTO.getDeploymentKeyLike())){
            deploymentQuery.deploymentKeyLike(deploymentQueryDTO.getDeploymentKeyLike());
        }
        if(!StringUtils.isEmpty(deploymentQueryDTO.getProcessDefinitionKeyLike())){
            deploymentQuery.processDefinitionKeyLike(deploymentQueryDTO.getProcessDefinitionKeyLike());
        }
        List<Deployment> deployments = deploymentQuery.orderByDeploymenTime().desc().
                listPage(deploymentQueryDTO.getFirstResult(), deploymentQueryDTO.getMaxResults());
        /*List<DeploymentVO> deploymentVOList = deployments.stream().map(deployment -> {
            DeploymentVO deploymentVO = new DeploymentVO();
            ModelQuery modelQuery = repositoryService.createModelQuery();
            List<Model> modelList = modelQuery.deploymentId(deployment.getId()).list();
            List<ModelVO> modelVoList = modelList.stream().map(model -> {
                ModelVO modelVO = new ModelVO();
                BeanUtils.copyProperties(model, modelVO);
                return modelVO;
            }).collect(Collectors.toList());
            BeanUtils.copyProperties(deployment, deploymentVO);
            deploymentVO.setModelVOList(modelVoList);
            return deploymentVO;
        }).collect(Collectors.toList());*/
        List<DeploymentVO> deploymentVOList = deployments.stream().map(t -> {
            DeploymentVO deploymentVO = new DeploymentVO();
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
            processDefinitionQuery.deploymentId(t.getId());
            if (!StringUtils.isEmpty(deploymentQueryDTO.getStartUserId())) {
                processDefinitionQuery.startableByUser(deploymentQueryDTO.getStartUserId());
            }
            List<ProcessDefinition> processDefinitionList = processDefinitionQuery.active().list();
            List<ProcessDefinitionVO> processDefinitionVOList = processDefinitionList.stream().map(processDefinition -> {
                ProcessDefinitionVO processDefinitionVO = new ProcessDefinitionVO();
                BeanUtils.copyProperties(processDefinition, processDefinitionVO);
                return processDefinitionVO;
            }).collect(Collectors.toList());
            BeanUtils.copyProperties(t,deploymentVO);
            deploymentVO.setProcessDefinitionVO(processDefinitionVOList.get(0));
            deploymentVO.setProcessDefinitionVOList(processDefinitionVOList);
            return deploymentVO;
        }).collect(Collectors.toList());

        return deploymentVOList;
    }

    @Override
    public void deleteDeployment(String ids) {
        List<String> idList = Arrays.asList(Convert.toStrArray(ids));
        idList.parallelStream().forEach(t->
            repositoryService.deleteDeployment(t,true)
        );
    }

    @Override
    public void getDeploymentSource(String id, String resourceName, String type,HttpServletResponse response) {
        try {
        byte[] b = null;
        if (type.equals("xml")) {
            response.setHeader("Content-type", "text/xml;charset=UTF-8");
            InputStream inputStream = repositoryService.getResourceAsStream(id, resourceName);
            b = IoUtil.readInputStream(inputStream, resourceName);
        } else {
            //todo 输出的有乱码，暂时没有解决办法
            response.setHeader("Content-Type", "image/png;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            InputStream inputStream = repositoryService.getResourceAsStream(id, resourceName);
            b = IoUtil.readInputStream(inputStream, resourceName);
        }
            response.getOutputStream().write(b);
        } catch (IOException e) {
            log.error("ApiFlowableModelResource-loadXmlByModelId:" + e);
            e.printStackTrace();
        }

    }
    @Override
    public ProcessInstance startProcessInstanceByKey(StartProcessDTO startProcessDTO) {
        ProcessInstance processInstance=null;
        String startUserId=startProcessDTO.getStartUserId();
        if(!StringUtils.isEmpty(startUserId)){
            identityService.setAuthenticatedUserId(startUserId);
        }
        Map<String, Object> paramMap =CollectionUtils.isEmpty(startProcessDTO.getVariables())?Maps.newHashMap():startProcessDTO.getVariables();
        paramMap.put("startUserId",startUserId);

        if(!CollectionUtils.isEmpty(paramMap)){
            processInstance = runtimeService.startProcessInstanceByKey(startProcessDTO.getProcessDefinitionKey(),startProcessDTO.getBusinessKey(),paramMap);
        }else {
            processInstance = runtimeService.startProcessInstanceByKey(startProcessDTO.getProcessDefinitionKey(),startProcessDTO.getBusinessKey());
        }
        //这个方法最终使用一个ThreadLocal类型的变量进行存储，也就是与当前的线程绑定，所以流程实例启动完毕之后，需要设置为null，防止多线程的时候出问题。
        identityService.setAuthenticatedUserId(null);
        return processInstance;
    }




    @Override
    public Task getTask(String taskId) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        return task;
    }

    @Override
    public List<TaskVO> findTasksByUserId(String userId, TaskBaseDTO taskBaseDTO) {
        //根据用户ID获取角色
        List<SysRole> sysRoles = roleService.selectRolesByUserId(Long.parseLong(userId));

        TaskQuery taskQuery = taskService.createTaskQuery()
                .or()
                .taskCandidateOrAssigned(userId);
        //这个地方查询回去查询系统的用户组表，希望的是查询自己的用户表
        if(!CollectionUtils.isEmpty(sysRoles)) {
            List<String> roleIds = sysRoles.stream().map(t -> {
                return String.valueOf(t.getRoleId());
            }).collect(Collectors.toList());
            taskQuery.taskCandidateGroupIn(roleIds);
        }
        if(!StringUtils.isEmpty(taskBaseDTO.getProcessInstanceId())){
            taskQuery.processInstanceId(taskBaseDTO.getProcessInstanceId());
        }
        if(!StringUtils.isEmpty(taskBaseDTO.getTaskId())){
            taskQuery.taskId(taskBaseDTO.getTaskId());
        }
        if(!StringUtils.isEmpty(taskBaseDTO.getBusinessKey())){
            taskQuery.processInstanceBusinessKey(taskBaseDTO.getBusinessKey());
        }
        if(StringUtils.isEmpty(taskBaseDTO.getDefinitionKey())){
            taskQuery.processDefinitionKey(taskBaseDTO.getDefinitionKey());
        }
        List<Task> taskList = taskQuery.endOr()
                .orderByTaskCreateTime()
                .desc()
                .listPage(taskBaseDTO.getFirstResult(), taskBaseDTO.getMaxResults());
        List<TaskVO> taskVoList = taskList.stream().map(t -> {
            TaskVO taskVO = new TaskVO();
            taskVO.setTaskId(t.getId());
            taskVO.setTaskName(t.getName());
            taskVO.setProcessInstanceId(t.getProcessInstanceId());
            taskVO.setCreateDate(t.getCreateTime());
            taskVO.setFormKey(t.getFormKey());
            taskVO.setParentTaskId(t.getParentTaskId());
            taskVO.setAssignee(t.getAssignee());
            taskVO.setOwner(t.getOwner());
            HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceById(t.getProcessInstanceId());
            taskVO.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
            String startUserId = historicProcessInstance.getStartUserId();
            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(startUserId));
            taskVO.setStartUserId(startUserId);
            taskVO.setStartUserName(sysUser.getUserName());
            taskVO.setBusinessKey(historicProcessInstance.getBusinessKey());
            taskVO.setStartTime(historicProcessInstance.getStartTime());
            return taskVO;
        }).collect(Collectors.toList());
        return taskVoList;
    }



    @Override
    public void completeTask(CompleteTaskDTO completeTaskDTO) {
        Task task = this.getTask(completeTaskDTO.getTaskId());
        if(StringUtils.isEmpty(task)){
            log.info("完成任务时，该任务ID:%不存在",completeTaskDTO.getTaskId());
            throw new BusinessException(String.format("该任务ID:%不存在",completeTaskDTO.getTaskId()));
        }
        if(!StringUtils.isEmpty(completeTaskDTO.getComment())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),completeTaskDTO.getComment());
        }
        List<FileEntry> files = completeTaskDTO.getFiles();
        if(!CollectionUtils.isEmpty(files)){
            files.stream().forEach(t->
                taskService.createAttachment("",task.getId(),task.getProcessInstanceId(),t.getKey(),t.getName(),t.getUrl())
            );
        }
        runtimeService.setVariable(task.getExecutionId(),CompleteTaskDTO.IS_PASS,completeTaskDTO.getIsPass());
        Map<String, Object> paramMap = StringUtils.isEmpty(completeTaskDTO.getParamMap())?Maps.newHashMap():completeTaskDTO.getParamMap();
        if(!CollectionUtils.isEmpty(paramMap)){
            Set<Map.Entry<String, Object>> entries = paramMap.entrySet();
            entries.stream().forEach(t->
                runtimeService.setVariable(task.getExecutionId(),t.getKey(),t.getValue())
            );
        }
        paramMap.put(CompleteTaskDTO.IS_PASS,completeTaskDTO.getIsPass());
        taskService.claim(task.getId(),completeTaskDTO.getUserId());
        taskService.complete(task.getId(),paramMap,true);
    }


    @Override
    public void getProcessDiagram(HttpServletResponse httpServletResponse, String processId) {

        /**
         * 获得当前活动的节点
         */
        String processDefinitionId = "";
        if (this.isFinished(processId)) {// 如果流程已经结束，则得到结束节点
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();

            processDefinitionId=pi.getProcessDefinitionId();
        } else {// 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
            processDefinitionId=pi.getProcessDefinitionId();
        }
        List<String> highLightedActivitis = new ArrayList<String>();

        /**
         * 获得活动的节点
         */
        List<HistoricActivityInstance> highLightedActivitList =  historyService.createHistoricActivityInstanceQuery().processInstanceId(processId).orderByHistoricActivityInstanceStartTime().asc().list();

        for(HistoricActivityInstance tempActivity : highLightedActivitList){
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }

        List<String> flows = new ArrayList<>();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();

        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "bmp", highLightedActivitis, flows, engconf.getActivityFontName(),
                engconf.getLabelFontName(), engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0, true);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            out = httpServletResponse.getOutputStream();
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } catch (IOException e) {
            log.error("操作异常",e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }

    public boolean isFinished(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery().finished()
                .processInstanceId(processInstanceId).count() > 0;
    }

    @Override
    public ProcessInstance getProcessInstanceById(String id){
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(id)
                .singleResult();
    }

    @Override
    public HistoricProcessInstance getHistoricProcessInstanceById(String id){
        return  historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(id)
                .singleResult();
    }

    @Override
    public Task getTaskProcessInstanceById(String id){
        return taskService.createTaskQuery()
                .processInstanceId(id)
                .singleResult();
    }
}
