package com.snow.flowable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.snow.common.core.page.PageModel;
import com.snow.common.core.text.Convert;
import com.snow.common.exception.BusinessException;
import com.snow.common.utils.bean.BeanUtils;
import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.common.enums.FlowInstanceEnum;
import com.snow.flowable.common.enums.FlowTypeEnum;
import com.snow.flowable.common.skipTask.TaskSkipService;
import com.snow.flowable.config.ICustomProcessDiagramGenerator;
import com.snow.flowable.domain.*;
import com.snow.flowable.domain.response.ProcessDefinitionResponse;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.flowable.service.FlowableUserService;
import com.snow.system.domain.ActDeModel;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.RepositoryServiceImpl;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.service.ModelServiceImpl;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * ????????????????????????(act_ru_identitylink)
 *
 * ????????????????????????????????????????????????????????????????????????
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
    private RepositoryService repositoryService;


    @Autowired
    private HistoryService historyService;

    @Resource
    private ProcessEngine processEngine;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private ExpressionServiceImpl expressionService;

    @Autowired
    private ModelServiceImpl modelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Resource
    private FlowableUserService flowableUserService;

    @Resource
    private FlowableTaskService flowableTaskService;

    @Resource
    private TaskSkipService taskSkipService;

    @Override
    public void saveModel(ActDeModel actDeModel) {
        // ??????ModelEditorSource
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilNode = objectMapper.createObjectNode();
        stencilNode.put("id","BPMNDiagram");
        editorNode.set("stencil",stencilNode);
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        stencilSetNode.put("url","../editor/stencilsets/bpmn2.0/bpmn2.0.json");
        editorNode.set("stencilset", stencilSetNode);

        org.flowable.ui.modeler.domain.Model model=new org.flowable.ui.modeler.domain.Model();
        model.setName(actDeModel.getName());
        model.setComment(actDeModel.getModelComment());
        model.setDescription(actDeModel.getDescription());
        model.setCreated(new Date());
        model.setLastUpdated(new Date());
        model.setCreatedBy(actDeModel.getCreatedBy());
        model.setKey(actDeModel.getModelKey());
        model.setModelType(actDeModel.getModelType().intValue());
        model.setVersion(actDeModel.getVersion().intValue());
        model.setModelEditorJson(editorNode.toString());
        modelService.saveModel(model);
    }

    @Override
    public void deleteModel(String modelId) {
        List<String> idList = Arrays.asList(Convert.toStrArray(modelId));
        idList.forEach(t->{
            //?????????????????????????????????
            modelService.deleteModel(t);
        });

    }

    @Override
    public void exportModelXml(String modelId, HttpServletResponse response) {
        try {
            Model modelData = modelService.getModel(modelId);
            BpmnModel bpmnModel = modelService.getBpmnModel(modelData);
            // ??????????????????
            if (!CollectionUtils.isEmpty(bpmnModel.getProcesses())) {
                byte[] bpmnXML = modelService.getBpmnXML(modelData);
                ByteArrayInputStream in = new ByteArrayInputStream(bpmnXML);
                String filename = bpmnModel.getMainProcess().getId() + ".bpmn";
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                IOUtils.copy(in, response.getOutputStream());
                response.flushBuffer();
            }else {
                log.warn("??????model???xml????????????,?????????null???modelId={}", modelId);
            }
        } catch (Exception e) {
            log.error("??????model???xml???????????????modelId={}", modelId, e);
        }
    }

    @Override
    public void showModelXml(String modelId, HttpServletResponse response) {
        try {
            Model modelData = modelService.getModel(modelId);
            BpmnModel bpmnModel = modelService.getBpmnModel(modelData);
            // ??????????????????
            if (!CollectionUtils.isEmpty(bpmnModel.getProcesses())) {
                byte[] bpmnXML = modelService.getBpmnXML(modelData);
                response.setHeader("Content-type", "text/xml;charset=UTF-8");
                response.getOutputStream().write(bpmnXML);
            }else {
                log.warn("??????model???xml????????????,?????????null???modelId={}", modelId);
            }
        } catch (Exception e) {
            log.error("??????model???xml???????????????modelId={}", modelId, e);
        }
    }
    @Override
    public PageModel<DeploymentVO> getDeploymentList(DeploymentQueryDTO deploymentQueryDTO) {

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        if(StrUtil.isNotEmpty(deploymentQueryDTO.getDeploymentNameLike())){
            deploymentQuery.deploymentNameLike("%"+deploymentQueryDTO.getDeploymentNameLike()+"%");
        }
        if(StrUtil.isNotEmpty(deploymentQueryDTO.getDeploymentCategory())){
            deploymentQuery.deploymentCategory(deploymentQueryDTO.getDeploymentCategory());
        }
        if(StrUtil.isNotEmpty(deploymentQueryDTO.getDeploymentId())){
            deploymentQuery.deploymentId(deploymentQueryDTO.getDeploymentId());
        }
        if(StrUtil.isNotEmpty(deploymentQueryDTO.getDeploymentKeyLike())){
            deploymentQuery.deploymentKeyLike("%"+deploymentQueryDTO.getDeploymentKeyLike()+"%");
        }
        if(StrUtil.isNotEmpty(deploymentQueryDTO.getProcessDefinitionKeyLike())){
            deploymentQuery.processDefinitionKeyLike("%"+deploymentQueryDTO.getProcessDefinitionKeyLike()+"%");
        }


        long count = deploymentQuery.orderByDeploymenTime().desc().
                count();
        List<Deployment> deployments = deploymentQuery.orderByDeploymenTime().desc().
                listPage(deploymentQueryDTO.getPageNum(), deploymentQueryDTO.getPageSize());

        //??????????????????????????????????????????
        List<DeploymentVO> deploymentVoList = deployments.stream().map(t -> {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(t.getId()).list().get(0);

            DeploymentVO deploymentVO = new DeploymentVO();
            BeanUtils.copyProperties(t, deploymentVO);
            deploymentVO.setEngineVersion(processDefinition.getVersion());
            deploymentVO.setResourceName(processDefinition.getResourceName());
            deploymentVO.setDgrmResourceName(processDefinition.getDiagramResourceName());
            return deploymentVO;
        }).collect(Collectors.toList());

        PageModel<DeploymentVO> pageModel = new PageModel<> ();
        pageModel.setTotalCount((int)count);
        pageModel.setPagedRecords(deploymentVoList);
        return pageModel;
    }

    @Override
    public DeploymentVO getDeploymentDetailById(String id) {
        DeploymentVO deploymentVO=new DeploymentVO();
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(id).singleResult();
        BeanUtil.copyProperties(deployment,deploymentVO);
        //??????????????????????????????????????????
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
        List<ProcessDefVO> processDefVOList=Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(list)){
            list.forEach(t->{
                ProcessDefVO processDefVO=new ProcessDefVO();
                BeanUtils.copyProperties(t,processDefVO);
                processDefVOList.add(processDefVO);
            });
        }
        deploymentVO.setProcessDefVOList(processDefVOList);
        return deploymentVO;
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
            if(StringUtils.isEmpty(resourceName)){
                List<String> deploymentResourceNames = repositoryService.getDeploymentResourceNames(id);
                if(type.equals(FlowConstants.XML)){
                    resourceName = deploymentResourceNames.stream().filter(p -> (p.endsWith(".xml") || p.endsWith(".bpmn"))).findFirst().orElse(null);
                }else {
                    resourceName = deploymentResourceNames.stream().filter(p -> p.endsWith(".png")).findFirst().orElse(null);
                }
            }
            if (type.equals(FlowConstants.XML)) {
                response.setHeader("Content-type", "text/xml;charset=UTF-8");
                InputStream inputStream = repositoryService.getResourceAsStream(id, resourceName);
                b = IoUtil.readInputStream(inputStream, resourceName);
            } else if(type.equals(FlowConstants.PNG)){
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
        if(StrUtil.isNotBlank(startUserId)){
            identityService.setAuthenticatedUserId(startUserId);
        }
        Map<String, Object> paramMap =CollectionUtils.isEmpty(startProcessDTO.getVariables())?Maps.newHashMap():startProcessDTO.getVariables();
        paramMap.put(FlowConstants.START_USER_ID,startUserId);

        if(CollUtil.isNotEmpty(paramMap)){
            processInstance = runtimeService.startProcessInstanceByKey(startProcessDTO.getProcessDefinitionKey(),startProcessDTO.getBusinessKey(),paramMap);
        }else {
            processInstance = runtimeService.startProcessInstanceByKey(startProcessDTO.getProcessDefinitionKey(),startProcessDTO.getBusinessKey());
        }
        //??????????????????????????????ThreadLocal????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????null???????????????????????????????????????
        identityService.setAuthenticatedUserId(null);
        //??????????????????
        taskSkipService.autoSkip(processInstance.getProcessInstanceId());
        return processInstance;
    }

    @Override
    public ProcessInstance startProcessInstanceByAppForm(AppForm appForm) {
        String startUserId=appForm.getStartUserId();
        //??????????????????map
        Map<String, Object> paramMap = BeanUtil.beanToMap(appForm);
        identityService.setAuthenticatedUserId(startUserId);
        //????????????????????????
        paramMap.put(FlowConstants.APP_FORM,appForm);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(appForm.getFlowDef().getCode(),appForm.getBusinessKey(),paramMap);

        //??????????????????????????????ThreadLocal????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????null???????????????????????????????????????
        identityService.setAuthenticatedUserId(null);
        //??????????????????
        taskSkipService.autoSkip(processInstance.getProcessInstanceId());
        return processInstance;
    }


    /**
     * ????????????????????????????????????????????????????????????????????????
     * ????????? com.snow.flowable.service.FlowableTaskService#submitTask(com.snow.flowable.domain.FinishTaskDTO)
     * @param completeTaskDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(CompleteTaskDTO completeTaskDTO) {
        Task task = flowableTaskService.getTask(completeTaskDTO.getTaskId());
        if(StringUtils.isEmpty(task)){
            log.info("???????????????????????????ID:%?????????",completeTaskDTO.getTaskId());
            throw new BusinessException(String.format("?????????ID:%?????????",completeTaskDTO.getTaskId()));
        }
        ////??????????????????????????????????????????userid?????????null
        Authentication.setAuthenticatedUserId(completeTaskDTO.getUserId());
        if(!StringUtils.isEmpty(completeTaskDTO.getComment())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),FlowConstants.OPINION,completeTaskDTO.getComment());
        }else {
            taskService.addComment(task.getId(),task.getProcessInstanceId(),FlowConstants.OPINION,"");
        }

        List<FileEntry> files = completeTaskDTO.getFiles();
        if(!CollectionUtils.isEmpty(files)){
            files.stream().forEach(t->
                taskService.createAttachment("url",task.getId(),task.getProcessInstanceId(),t.getName(),t.getKey(),t.getUrl())
            );
        }

        Map<String, Object> paramMap = StringUtils.isEmpty(completeTaskDTO.getParamMap())?Maps.newHashMap():completeTaskDTO.getParamMap();
        if(!CollectionUtils.isEmpty(paramMap)){
            Set<Map.Entry<String, Object>> entries = paramMap.entrySet();
            entries.stream().forEach(t->
                runtimeService.setVariable(task.getExecutionId(),t.getKey(),t.getValue())
            );
        }
        if(!StringUtils.isEmpty(completeTaskDTO.getIsPass())){
            runtimeService.setVariable(task.getExecutionId(),CompleteTaskDTO.IS_PASS,completeTaskDTO.getIsPass());
            paramMap.put(CompleteTaskDTO.IS_PASS,completeTaskDTO.getIsPass());
        }
        if(!StringUtils.isEmpty(completeTaskDTO.getIsStart())){
            runtimeService.setVariable(task.getExecutionId(),CompleteTaskDTO.IS_START,completeTaskDTO.getIsStart());
            paramMap.put(CompleteTaskDTO.IS_START,completeTaskDTO.getIsStart());
        }
        // owner???????????????????????????????????????
        if (!StringUtils.isEmpty(task.getOwner())) {
            DelegationState delegationState = task.getDelegationState();
            switch (delegationState) {
                //?????????
                case PENDING:
                    // ??????????????????????????????
                    taskService.resolveTask(task.getId(),paramMap);
                    break;
                //?????????????????????
               /* case RESOLVED:
                    System.out.println("????????????????????????");
                    break;*/
                default:
                    //claim the task?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    taskService.claim(task.getId(),completeTaskDTO.getUserId());
                    taskService.complete(task.getId(),paramMap,true);
                    break;
            }
        } else {
            //claim the task?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            taskService.claim(task.getId(),completeTaskDTO.getUserId());
            taskService.complete(task.getId(),paramMap,true);
        }
    }


    @Override
    public void getProcessDiagram(HttpServletResponse httpServletResponse, String processId) {

        /**
         * ???????????????????????????
         */
        String processDefinitionId = "";
        if (this.isFinished(processId)) {// ????????????????????????????????????????????????
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().
                    processInstanceId(processId)
                    .singleResult();

            processDefinitionId=pi.getProcessDefinitionId();
        } else {// ???????????????????????????????????????????????????
            // ??????????????????ID?????????????????????????????????ActivityId??????
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().
                    processInstanceId(processId).
                    singleResult();
            processDefinitionId=pi.getProcessDefinitionId();
        }
        //????????????
        List<String> highLightedActivitis = new ArrayList<String>();
        //?????????
        List<String> flows = new ArrayList<>();
        /**
         * ??????????????????????????????????????????
         */
        List<HistoricActivityInstance> highLightedActivitList =  historyService.createHistoricActivityInstanceQuery().
                processInstanceId(processId).
                orderByHistoricActivityInstanceStartTime().
                asc().
                list();

        for(HistoricActivityInstance tempActivity : highLightedActivitList){
            String activityType = tempActivity.getActivityType();
            if (activityType.equals("sequenceFlow") || activityType.equals("exclusiveGateway")) {
                flows.add(tempActivity.getActivityId());
            } else if (activityType.equals("startEvent")) {
                String activityId = tempActivity.getActivityId();
                highLightedActivitis.add(activityId);
            }
        }
        //?????????????????????
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();
        for (Task task : tasks) {
            highLightedActivitis.add(task.getTaskDefinitionKey());
        }
        //???????????????
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
            log.error("????????????",e);
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
                .includeProcessVariables()
                .singleResult();
    }

    @Override
    public HistoricProcessInstance getHistoricProcessInstanceById(String id){
        return  historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(id)
                //???????????????????????????????????????????????????????????????
                .includeProcessVariables()
                .singleResult();
    }

    @Override
    public ProcessInstanceVO getProcessInstanceVoById(String id) {
        ProcessInstanceVO processInstanceVO=new ProcessInstanceVO();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(id)
                .includeProcessVariables()
                .singleResult();
        BeanUtils.copyProperties(historicProcessInstance,processInstanceVO);
        //????????????
        warpProcessInstanceVo(processInstanceVO);
        return processInstanceVO;
    }


    @Override
    public Task getTaskProcessInstanceById(String id){
        return taskService.createTaskQuery()
                .processInstanceId(id)
                .includeTaskLocalVariables()
                .includeIdentityLinks()
                .includeProcessVariables()
                .singleResult();
    }


    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * @param processInstanceId
     */
    @Override
    public List<TaskVO> getDynamicFlowNodeInfo(String processInstanceId) {
        HistoricProcessInstance processInstance= getHistoricProcessInstanceById(processInstanceId);
        List<TaskVO> hisTaskVOList=Lists.newArrayList();
        HistoricTaskInstanceDTO historicTaskInstanceDTO=new HistoricTaskInstanceDTO();
        historicTaskInstanceDTO.setProcessInstanceId(processInstanceId);
        List<HistoricTaskInstanceVO> historicTaskInstance = flowableTaskService.getHistoricTaskInstanceNoPage(historicTaskInstanceDTO);
        if(!CollectionUtils.isEmpty(historicTaskInstance)){
            hisTaskVOList = historicTaskInstance.stream().map(t -> {
                TaskVO taskVO = new TaskVO();
                taskVO.setTaskName(t.getTaskName());
                taskVO.setProcessInstanceId(t.getProcessInstanceId());
                taskVO.setStartUserId(processInstance.getStartUserId());
                taskVO.setStartUserName(getUserNameById(processInstance.getStartUserId()));
                taskVO.setTaskDefinitionKey(t.getTaskDefinitionKey());
                if (!StringUtils.isEmpty(t.getStartTime())) {
                    taskVO.setCreateDate(t.getStartTime());
                }
                if (!StringUtils.isEmpty(t.getCompleteTime())) {
                    taskVO.setCompleteTime(t.getCompleteTime());
                }

                if (!StringUtils.isEmpty(t.getAssignee())) {
                    taskVO.setAssignee(getUserNameById(t.getAssignee()));
                }else {

                }
                return taskVO;
            }).collect(Collectors.toList());
        }

        List<TaskVO> futureTask = getFutureTask(processInstance);
        for(int i=0;i<hisTaskVOList.size();i++){
            for(int j=0;j<futureTask.size();j++){
                if(hisTaskVOList.get(i).getTaskName().equals(futureTask.get(j).getTaskName())&&CollectionUtils.isEmpty(futureTask.get(j).getHandleUserList())){
                    futureTask.remove(j);
                }
            }
        }
        hisTaskVOList.addAll(futureTask);
        return hisTaskVOList;
    }

    @Override
    public List<ProcessInstanceVO> getHistoricProcessInstanceList(ProcessInstanceDTO processInstanceDTO) {
        HistoricProcessInstanceQuery historicProcessInstanceQuery = buildHistoricProcessInstanceCondition(processInstanceDTO);
        List<HistoricProcessInstance> list = historicProcessInstanceQuery.
                orderByProcessInstanceStartTime().
                desc().
                list();
        List<ProcessInstanceVO> processInstanceVOS = ProcessInstanceVO.warpList(list);
        processInstanceVOS.parallelStream().forEach(t->warpProcessInstanceVo(t));
        return processInstanceVOS;
    }

    @Override
    public PageModel<ProcessInstanceVO> getHistoricProcessInstance(ProcessInstanceDTO processInstanceDTO) {

        HistoricProcessInstanceQuery historicProcessInstanceQuery = buildHistoricProcessInstanceCondition(processInstanceDTO);
        long count = historicProcessInstanceQuery.
                orderByProcessInstanceStartTime().
                desc().
                count();
        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.
                orderByProcessInstanceStartTime().
                desc().
                listPage(processInstanceDTO.getPageNum(), processInstanceDTO.getPageSize());
        List<ProcessInstanceVO> processInstanceVOS = ProcessInstanceVO.warpList(historicProcessInstances);
        processInstanceVOS.forEach(t-> warpProcessInstanceVo(t));
        PageModel<ProcessInstanceVO> pageModel = new PageModel<> ();
        pageModel.setTotalCount((int)count);
        pageModel.setPagedRecords(processInstanceVOS);
        return pageModel;
    }

    /**
     * ??????????????????
     * @param processInstanceDTO ?????????????????????
     */
    private HistoricProcessInstanceQuery buildHistoricProcessInstanceCondition(ProcessInstanceDTO processInstanceDTO){
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        if(!StringUtils.isEmpty(processInstanceDTO.getBusinessKey())){
            historicProcessInstanceQuery.processInstanceBusinessKey(processInstanceDTO.getBusinessKey());
        }
        if(!StringUtils.isEmpty(processInstanceDTO.getProcessDefinitionKey())){
            historicProcessInstanceQuery.processDefinitionKey(processInstanceDTO.getProcessDefinitionKey());
        }
        if(!StringUtils.isEmpty(processInstanceDTO.getStartedBefore())){
            historicProcessInstanceQuery.startedAfter(processInstanceDTO.getStartedBefore());
        }
        if(!StringUtils.isEmpty(processInstanceDTO.getStartedAfter())){
            historicProcessInstanceQuery.startedAfter(processInstanceDTO.getStartedAfter());
        }
        if(!StringUtils.isEmpty(processInstanceDTO.getFinishedBefore())){
            historicProcessInstanceQuery.finishedBefore(processInstanceDTO.getFinishedBefore());
        }
        if(!StringUtils.isEmpty(processInstanceDTO.getFinishedAfter())){
            historicProcessInstanceQuery.finishedAfter(processInstanceDTO.getFinishedAfter());
        }
        if(!StringUtils.isEmpty(processInstanceDTO.getStartedUserId())){
            historicProcessInstanceQuery.startedBy(processInstanceDTO.getStartedUserId());
        }
        if(!StringUtils.isEmpty(processInstanceDTO.getProcessDefinitionName())){
            historicProcessInstanceQuery.processDefinitionName(processInstanceDTO.getProcessDefinitionName());
        }
        historicProcessInstanceQuery.includeProcessVariables();
        return historicProcessInstanceQuery;
    }

    /**
     * ???????????????????????????
     * @param processInstance
     * @return
     */
    public List<TaskVO>  getFutureTask(HistoricProcessInstance processInstance){
        String processInstanceId=processInstance.getId();
        String startUserId=processInstance.getStartUserId();
        List<TaskVO> taskVOList=Lists.newArrayList();

        List<Process> processes = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId()).getProcesses();
        if(!CollectionUtils.isEmpty(processes)){
            processes.stream().forEach(process -> {
                Collection<FlowElement> flowElements = process. getFlowElements();
                for(FlowElement flowElement:flowElements){
                    if(flowElement instanceof UserTask){
                        List<String> handleNameList=Lists.newArrayList();
                        UserTask userTask=(UserTask)flowElement;
                        //?????????
                        String assignee=userTask.getAssignee();
                        List<String> candidateGroups = userTask.getCandidateGroups();
                        List<String> candidateUsers = userTask.getCandidateUsers();
                        if(!CollectionUtils.isEmpty(candidateUsers)){
                            //???????????????????????????
                            MultiInstanceLoopCharacteristics loopCharacteristics = userTask.getLoopCharacteristics();
                            if(loopCharacteristics == null){
                                String expressionValue=null;
                                if(userTask.getId().equals("userTask0")){
                                    expressionValue = processInstance.getStartUserId();
                                    handleNameList.add(expressionValue);
                                }else {
                                    for (String candidateUser:candidateUsers){
                                        if(com.snow.common.utils.StringUtils.isNumeric(candidateUser)){
                                            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(candidateUser));
                                            handleNameList.add(sysUser.getUserName());
                                        }else {
                                            //?????????????????????
                                            //  expressionValue =(String) expressionService.getValue(processInstanceId,candidateUser);
                                            //????????????
                                            handleNameList.add(candidateUser);
                                        }

                                    }
                                }
                                TaskVO.TaskVOBuilder taskVOBuilder = TaskVO.builder().taskName(userTask.getName()).
                                        handleUserList(handleNameList).
                                        taskDefinitionKey(userTask.getId()).
                                        startUserId(processInstance.getStartUserId());

                                if(com.snow.common.utils.StringUtils.isNumeric(startUserId)){
                                    taskVOBuilder.startUserName(getUserNameById(startUserId));
                                }else {
                                    taskVOBuilder.startUserName(startUserId);
                                }
                                TaskVO taskVO = taskVOBuilder.build();
                                taskVOList.add(taskVO);
                            }
                            //todo ?????????????????????
                            else {
                                String inputDataItem = loopCharacteristics.getInputDataItem();
                                List<String> values = (List)expressionService.getValue(processInstanceId, inputDataItem);
                                System.out.println(JSON.toJSON(values));
                            }
                        }
                        if(!CollectionUtils.isEmpty(candidateGroups)){
                            //???????????????????????????
                            MultiInstanceLoopCharacteristics loopCharacteristics = userTask.getLoopCharacteristics();
                            if(loopCharacteristics == null){
                                String expressionValue=null;
                                if(userTask.getId().equals("userTask0")){
                                    expressionValue = processInstance.getStartUserId();
                                    handleNameList.add(expressionValue);
                                }else {
                                    for (String candidateGroup:candidateGroups){
                                        if(com.snow.common.utils.StringUtils.isNumeric(candidateGroup)){
                                            List<SysUser> sysUsers=flowableUserService.getUserByFlowGroupId(Long.parseLong(candidateGroup));
                                            if(!CollectionUtils.isEmpty(sysUsers)){
                                                List<String> collect = sysUsers.stream().map(SysUser::getUserName).collect(Collectors.toList());
                                                handleNameList.addAll(collect);
                                            }
                                        }else {
                                            //?????????????????????
                                            expressionValue =(String) expressionService.getValue(processInstanceId,candidateGroup);
                                            //????????????
                                            handleNameList.add(candidateGroup);
                                        }
                                    }
                                    TaskVO.TaskVOBuilder taskVOBuilder = TaskVO.builder().taskName(userTask.getName()).
                                            handleUserList(handleNameList).
                                            taskDefinitionKey(userTask.getId()).
                                            startUserId(processInstance.getStartUserId());

                                    if(com.snow.common.utils.StringUtils.isNumeric(startUserId)){
                                        taskVOBuilder.startUserName(getUserNameById(startUserId));
                                    }else {
                                        taskVOBuilder.startUserName(startUserId);
                                    }
                                    TaskVO taskVO = taskVOBuilder.build();
                                    taskVOList.add(taskVO);
                                }
                            }
                            //todo ?????????????????????
                            else {
                                String inputDataItem = loopCharacteristics.getInputDataItem();
                                List<String> values = (List)expressionService.getValue(processInstanceId, inputDataItem);
                                System.out.println(JSON.toJSON(values));
                            }
                        }
                        if(!StringUtils.isEmpty(userTask.getAssignee())){
                            MultiInstanceLoopCharacteristics loopCharacteristics = userTask.getLoopCharacteristics();
                            if(loopCharacteristics == null){
                                String expressionValue=null;
                                if(userTask.getName().equals("")){
                                    expressionValue = processInstance.getStartUserId();
                                }else {
                                    //?????????????????????
                                    expressionValue =(String) expressionService.getValue(processInstanceId, userTask.getAssignee());
                                    handleNameList.add(expressionValue);
                                }
                            }else {
                                String inputDataItem = loopCharacteristics.getInputDataItem();
                                List<String> values = (List)expressionService.getValue(processInstanceId, inputDataItem);
                                System.out.println(JSON.toJSON(values));
                            }
                            TaskVO.TaskVOBuilder taskVOBuilder = TaskVO.builder().taskName(userTask.getName()).
                                    handleUserList(handleNameList).
                                    taskDefinitionKey(userTask.getId()).
                                    startUserId(processInstance.getStartUserId());

                            if(com.snow.common.utils.StringUtils.isNumeric(startUserId)){
                                taskVOBuilder.startUserName(getUserNameById(startUserId));
                            }else {
                                taskVOBuilder.startUserName(startUserId);
                            }
                            TaskVO taskVO = taskVOBuilder.build();
                            taskVOList.add(taskVO);
                        }
                    }
                }
            });
        }
        return taskVOList;
    }



    /**
     * ????????????????????????????????????????????????????????????
     */
    @Override
    public void getFlowableProcessImage(String processInstanceId, HttpServletResponse response) {
        log.info("[??????]-?????????????????????");
        try {
            //  ????????????????????????
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();

            if (historicProcessInstance == null) {
                throw new BusinessException("??????????????????ID[" + processInstanceId + "]????????????????????????????????????");
            }
            else {
                // ??????????????????
                ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                        .getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());

                // ??????????????????????????????????????????????????????????????????????????????????????????
                List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().asc().list();

                // ??????????????????ID??????
                List<String> executedActivityIdList = new ArrayList<String>();
                int index = 1;
                for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
                    executedActivityIdList.add(activityInstance.getActivityId());
                    //logger.info("???[" + index + "]??????????????????=" + activityInstance.getActivityId() + " : " +activityInstance.getActivityName());
                    index++;
                }

                BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

                // ?????????????????????
                List<String> flowIds = Lists.newArrayList();
                // ???????????????????????? (getHighLightedFlows??????????????????)
                flowIds = getHighLightedFlows(bpmnModel,processDefinition, historicActivityInstanceList);

                Set<String> currIds = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list()
                        .stream().map(e->e.getActivityId()).collect(Collectors.toSet());

                ICustomProcessDiagramGenerator diagramGenerator = (ICustomProcessDiagramGenerator) processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
                InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,
                        flowIds, "??????", "??????", "??????", null, 1.0,
                        new Color[] { FlowConstants.COLOR_NORMAL, FlowConstants.COLOR_CURRENT }, currIds);

                response.setContentType("image/png");
                OutputStream os = response.getOutputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                log.info("[??????]-?????????????????????");

               os.close();
               imageStream.close();
            }

        } catch (Exception e) {
            log.error("????????????-????????????????????????" + e.getMessage());
            throw new BusinessException("????????????????????????" + e.getMessage());
        }
    }

    @Override
    public FlowGeneralSituationVO getFlowGeneralSituation(String userId) {
        //????????????ID????????????
        Set<Long> sysRoles = flowableUserService.getFlowGroupByUserId(Long.parseLong(userId));

        TaskQuery taskQuery = taskService.createTaskQuery()
                .or()
                .taskCandidateOrAssigned(userId);
        //??????????????????????????????????????????????????????????????????????????????????????????
        if(!CollectionUtils.isEmpty(sysRoles)) {
            List<String> roleIds = sysRoles.stream().map(t ->
                    String.valueOf(t)
            ).collect(Collectors.toList());
            taskQuery.taskCandidateGroupIn(roleIds).endOr();
        }

        List<Task> taskList = taskQuery.list();

        //????????????
        FlowGeneralSituationVO.FlowGeneralSituationVOBuilder flowGeneralSituationVOBuilder = FlowGeneralSituationVO.builder().todoTaskNum(taskQuery.count());

        //???????????????????????????
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().startedBy(userId);
        flowGeneralSituationVOBuilder.myStartProcessInstanceNum(historicProcessInstanceQuery.count());

        //?????????????????????
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId);
        List<HistoricProcessInstance> list = historicProcessInstanceQuery.list();
        flowGeneralSituationVOBuilder.doneTaskNum(historicTaskInstanceQuery.count());


        //????????????????????????????????????
        long count = taskList.stream().filter(t ->
                DateUtil.betweenDay(t.getCreateTime(), new Date(),false) > 3
        ).count();
        flowGeneralSituationVOBuilder.threeTodoTaskNum(count);

        //?????????
        int length = FlowDefEnum.values().length;
        return flowGeneralSituationVOBuilder.processInstanceNum(length).build();

    }

    @Override
    public void suspendOrActiveProcessInstance(String instanceId, Integer suspendState) {
        if(suspendState==FlowInstanceEnum.ACTIVATE.getCode()){
            runtimeService.activateProcessInstanceById(instanceId);
            //TODO ??????????????????
        }else {
            runtimeService.suspendProcessInstanceById(instanceId);
        }

    }

    @Override
    public void cancelProcessInstance(String instanceId, String reason) {
        //????????????????????????????????????????????????
       // historyService.deleteHistoricProcessInstance();
        //???????????????
        runtimeService.deleteProcessInstance(instanceId,reason);

    }

    /**
     * ??????????????????
     * @param bpmnModel
     * @param processDefinitionEntity
     * @param historicActivityInstances
     * @return
     */
    private List<String> getHighLightedFlows(BpmnModel bpmnModel,ProcessDefinitionEntity processDefinitionEntity,List<HistoricActivityInstance> historicActivityInstances) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //24?????????
        List<String> highFlows = new ArrayList<String>();// ????????????????????????flowId
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            // ?????????????????????????????????
            // ?????????????????????????????????
            FlowNode activityImpl=null;
            FlowElement flowElement1 = bpmnModel.getFlowElement(historicActivityInstances.get(i).getActivityId());
            if(flowElement1 instanceof FlowNode){
                 activityImpl = (FlowNode)bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i).getActivityId());
            }
            // ?????????????????????????????????????????????
            List<FlowNode> sameStartTimeNodes = Lists.newArrayList();
            FlowNode sameActivityImpl1 = null;

            HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);// ???????????????
            HistoricActivityInstance activityImp2_ ;

            for(int k = i + 1 ; k <= historicActivityInstances.size() - 1; k++) {
                activityImp2_ = historicActivityInstances.get(k);// ?????????1?????????
                //??????usertask???????????????????????????????????????????????????????????????????????????????????????
                if ( activityImpl_.getActivityType().equals("userTask") && activityImp2_.getActivityType().equals("userTask") &&
                        df.format(activityImpl_.getStartTime()).equals(df.format(activityImp2_.getStartTime()))   )
                {

                }
                else {
                    FlowElement flowElement = bpmnModel.getFlowElement(historicActivityInstances.get(k).getActivityId());
                    if(flowElement instanceof FlowNode){
                        sameActivityImpl1 = (FlowNode)bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(k).getActivityId());//????????????????????????????????????
                        break;
                    }
                }

            }
            sameStartTimeNodes.add(sameActivityImpl1); // ????????????????????????????????????????????????????????????
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// ?????????????????????
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// ?????????????????????

                if (df.format(activityImpl1.getStartTime()).equals(df.format(activityImpl2.getStartTime()))  )
                {// ???????????????????????????????????????????????????????????????
                    FlowElement flowElement = bpmnModel.getFlowElement(activityImpl2.getActivityId());
                    if(flowElement instanceof FlowNode){
                        FlowNode sameActivityImpl2 = (FlowNode)bpmnModel.getMainProcess().getFlowElement(activityImpl2.getActivityId());
                        sameStartTimeNodes.add(sameActivityImpl2);
                    }
                }
                else
                {// ????????????????????????
                    break;
                }
            }
            if(!StringUtils.isEmpty(activityImpl)){
                List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows() ; // ?????????????????????????????????

                for (SequenceFlow pvmTransition : pvmTransitions)
                {// ???????????????????????????
                    FlowElement flowElement = bpmnModel.getFlowElement(pvmTransition.getTargetRef());
                    if(flowElement instanceof FlowNode){
                        FlowNode pvmActivityImpl = (FlowNode)bpmnModel.getMainProcess().getFlowElement( pvmTransition.getTargetRef());// ?????????????????????????????????????????????????????????????????????????????????id?????????????????????
                        if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                            highFlows.add(pvmTransition.getId());
                        }
                    }
                }
            }
        }
        return highFlows;
    }


    private String getUserNameById(String id){
        return sysUserService.selectUserById(Long.parseLong(id)).getUserName();
    }


    /**
     * ??????????????????????????????
     *
     * @param event
     * @return ProcessDefinitionEntity
     */
    public ProcessDefinitionEntity getProcessDefinition(FlowableEngineEvent event) {
        String processDefinitionId = event.getProcessDefinitionId();
        if (processDefinitionId != null) {
            CommandContext commandContext = CommandContextUtil.getCommandContext();
            if (commandContext != null) {
                return CommandContextUtil.getProcessDefinitionEntityManager(commandContext).findById(processDefinitionId);
            }
        }
        return null;
    }

    @Override
    public Set<FlowDefEnum> getAllFlowDefEnumsSet() {
        FlowDefEnum[] values = FlowDefEnum.values();
        return Sets.newHashSet(values);
    }

    @Override
    public List<ProcessDefinitionResponse> getProcessDefByKey(String processDefinitionKey) {
        ProcessDefinitionQuery processDefinitionQuery=repositoryService.createProcessDefinitionQuery();
        if(StrUtil.isNotBlank(processDefinitionKey)){
            processDefinitionQuery.processDefinitionKey(StrUtil.format("%{}%", processDefinitionKey));
        }
        List<ProcessDefinition> list = processDefinitionQuery
                .latestVersion()
                .active()
                .list();
        return cn.hutool.core.convert.Convert.toList(ProcessDefinitionResponse.class,list);
    }

    public Object getHisVariable(String processId, String key) {
        Object variable = null;
        List<HistoricVariableInstance> list = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processId)
                .list();
        for (HistoricVariableInstance historicVariableInstance:list) {
            if (historicVariableInstance.getVariableName().equals(key)) {
                variable = historicVariableInstance.getValue();
            }
        }
        return variable;
    }
    /**
     * ??????ProcessInstanceVO??????
     * @param processInstanceVO
     */
    private void warpProcessInstanceVo(ProcessInstanceVO processInstanceVO){
        Map<String, Object> processVariables = processInstanceVO.getProcessVariables();
        String url= Optional.ofNullable(String.valueOf(processVariables.get(FlowConstants.BUS_VAR_URL))).orElse("");
        //?????????????????????
        if(ObjectUtil.isNotNull(url)&&ObjectUtil.isNotNull(processVariables.get("id"))){
            processInstanceVO.setFromDetailUrl(url+"/"+processVariables.get("id"));
        }
        //??????????????????
        AppForm appForm=(AppForm)processVariables.get(FlowConstants.APP_FORM);
        processInstanceVO.setAppForm(appForm);
        //????????????????????????
        String spendTime =  StringUtils.isEmpty(processInstanceVO.getEndTime())?DateUtil.formatBetween(processInstanceVO.getStartTime(), new Date(), BetweenFormater.Level.SECOND):DateUtil.formatBetween(processInstanceVO.getStartTime(), processInstanceVO.getEndTime(), BetweenFormater.Level.SECOND);
        processInstanceVO.setProcessSpendTime(spendTime);
        //?????????????????????
        SysUser sysUser = sysUserService.selectUserById(Long.parseLong(processInstanceVO.getStartUserId()));
        processInstanceVO.setStartUserName(sysUser.getUserName());
        //??????????????????
        Object processType = processVariables.getOrDefault(FlowConstants.PROCESS_TYPE, FlowTypeEnum.API_PROCESS.getCode());
        processInstanceVO.setProcessType(String.valueOf(processType));
        //?????????????????? ACT_RU_EXECUTION
        List<Execution> list = runtimeService.createExecutionQuery().processInstanceId(processInstanceVO.getId()).list();
        if(CollectionUtils.isEmpty(list)){
            processInstanceVO.setProcessInstanceStatus(FlowInstanceEnum.ACTIVATE.getCode());
        }else {
            Execution execution=list.get(0);
            if(execution.isSuspended()){
                processInstanceVO.setProcessInstanceStatus(FlowInstanceEnum.SUSPEND.getCode());
            }else {
                processInstanceVO.setProcessInstanceStatus(FlowInstanceEnum.ACTIVATE.getCode());
            }
        }
        //??????????????????
        if(ObjectUtil.isEmpty(processInstanceVO.getEndActivityId())&& ObjectUtil.isNotEmpty(processInstanceVO.getDeleteReason())){
            processInstanceVO.setIsFinished(2);
        }else if(ObjectUtil.isNotEmpty(processInstanceVO.getEndActivityId())){
            processInstanceVO.setIsFinished(1);
        }else {
            processInstanceVO.setIsFinished(0);
        }
    }
}
