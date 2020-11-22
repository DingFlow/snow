package com.snow.flowable.service.impl;

import com.alibaba.fastjson.JSON;
import com.snow.common.core.text.Convert;
import com.snow.common.exception.BusinessException;
import com.snow.flowable.domain.*;
import com.snow.flowable.service.FlowableService;
import com.snow.system.domain.SysRole;
import com.snow.system.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.engine.*;
import org.flowable.engine.repository.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.service.impl.TaskQueryProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private ManagementService managementService;

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
            b = IoUtil.readInputStream(inputStream, "image inputStream name");
        } else {
            response.setHeader("Content-Type", "image/png");
            InputStream inputStream = repositoryService.getResourceAsStream(id, resourceName);
            b = IoUtil.readInputStream(inputStream, "image inputStream name");
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
        if(!StringUtils.isEmpty(startProcessDTO.getStartUserId())){
            identityService.setAuthenticatedUserId(startProcessDTO.getStartUserId());
        }
        Map<String, Object> paramMap = startProcessDTO.getVariables();

        if(!CollectionUtils.isEmpty(paramMap)){
            processInstance = runtimeService.startProcessInstanceByKey(startProcessDTO.getProcessDefinitionKey(),startProcessDTO.getBusinessKey(),paramMap);
        }else {
            processInstance = runtimeService.startProcessInstanceByKey(startProcessDTO.getProcessDefinitionKey(),startProcessDTO.getBusinessKey());
        }
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
    public List<Task> findTasksByUserId(String userId, TaskBaseDTO taskBaseDTO) {
        //根据用户ID获取角色
        List<SysRole> sysRoles = roleService.selectRolesByUserId(Long.parseLong(userId));

        TaskQuery taskQuery = taskService.createTaskQuery()
                .or()
                .taskCandidateOrAssigned(userId);
        if(!CollectionUtils.isEmpty(sysRoles)) {
            taskQuery.or()
                    .taskCandidateGroupIn(sysRoles.stream().map(SysRole::getRoleKey).collect(Collectors.toList()));
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
        return taskQuery.endOr()
                .orderBy(TaskQueryProperty.CREATE_TIME)
                .listPage(taskBaseDTO.getFirstResult(),taskBaseDTO.getMaxResults());
    }

    @Override
    public void completeTask(CompleteTaskDTO completeTaskDTO) {
        Task task = this.getTask(completeTaskDTO.getTaskId());
        if(StringUtils.isEmpty(task)){
            log.info("完成任务时，该任务ID:%不存在",completeTaskDTO.getTaskId());
            throw new BusinessException(String.format("该任务ID:%不存在",completeTaskDTO.getTaskId()));
        }
        if(StringUtils.isEmpty(completeTaskDTO.getComment())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),completeTaskDTO.getComment());
        }
        List<FileEntry> files = completeTaskDTO.getFiles();
        if(!CollectionUtils.isEmpty(files)){
            files.stream().forEach(t->
                taskService.createAttachment("",task.getId(),task.getProcessInstanceId(),t.getKey(),t.getName(),t.getUrl())
            );
        }
        runtimeService.setVariable(task.getExecutionId(),CompleteTaskDTO.IS_PASS,completeTaskDTO.getIsPass());
        Map<String, Object> paramMap = completeTaskDTO.getParamMap();
        if(!CollectionUtils.isEmpty(paramMap)){
            Set<Map.Entry<String, Object>> entries = paramMap.entrySet();
            entries.stream().forEach(t->
                runtimeService.setVariable(task.getExecutionId(),t.getKey(),t.getValue())
            );
        }
        paramMap.put(CompleteTaskDTO.IS_PASS,completeTaskDTO.getIsPass());
        taskService.complete(task.getId(),paramMap,true);
    }
}
