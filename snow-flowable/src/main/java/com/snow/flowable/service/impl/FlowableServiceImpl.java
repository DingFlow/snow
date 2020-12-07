package com.snow.flowable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.snow.common.core.page.PageModel;
import com.snow.common.core.text.Convert;
import com.snow.common.exception.BusinessException;
import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.domain.*;
import com.snow.flowable.enums.FlowFinishedStatusEnum;
import com.snow.flowable.service.FlowableService;
import com.snow.system.domain.ActDeModel;
import com.snow.system.domain.SysRole;
import com.snow.system.domain.SysUser;
import com.snow.system.mapper.SysUserMapper;
import com.snow.system.service.ISysRoleService;
import com.snow.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Attachment;
import org.flowable.engine.task.Comment;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.service.ModelServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * 运行时流程人员表(act_ru_identitylink)
 *
 * 任务参与者数据表。主要存储当前节点参与者的信息。
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

    @Resource
    private ProcessEngine processEngine;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private ExpressionServiceImpl expressionService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private ModelServiceImpl modelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void saveModel(ActDeModel actDeModel) {
        // 构建ModelEditorSource
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
        model.setCreatedBy(actDeModel.getCreatedBy());
        model.setKey(actDeModel.getModelKey());
        model.setModelType(actDeModel.getModelType().intValue());
        model.setVersion(1);
        model.setModelEditorJson(editorNode.toString());
        modelService.saveModel(model);
    }

    @Override
    public void deleteModel(String modelId) {
        List<String> idList = Arrays.asList(Convert.toStrArray(modelId));
        idList.forEach(t->{
            //删除会保留一份历史记录
            modelService.deleteModel(t);
        });

    }

    @Override
    public void exportModelXml(String modelId, HttpServletResponse response) {
        try {
            Model modelData = modelService.getModel(modelId);
            BpmnModel bpmnModel = modelService.getBpmnModel(modelData);
            // 流程非空判断
            if (!CollectionUtils.isEmpty(bpmnModel.getProcesses())) {
                byte[] bpmnXML = modelService.getBpmnXML(modelData);
                ByteArrayInputStream in = new ByteArrayInputStream(bpmnXML);
                String filename = bpmnModel.getMainProcess().getId() + ".bpmn";
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                IOUtils.copy(in, response.getOutputStream());
                response.flushBuffer();
            }else {
                log.warn("导出model的xml文件失败,流程为null：modelId={}", modelId);
            }
        } catch (Exception e) {
            log.error("导出model的xml文件失败：modelId={}", modelId, e);
        }
    }

    @Override
    public void showModelXml(String modelId, HttpServletResponse response) {
        try {
            Model modelData = modelService.getModel(modelId);
            BpmnModel bpmnModel = modelService.getBpmnModel(modelData);
            // 流程非空判断
            if (!CollectionUtils.isEmpty(bpmnModel.getProcesses())) {
                byte[] bpmnXML = modelService.getBpmnXML(modelData);
                response.setHeader("Content-type", "text/xml;charset=UTF-8");
                response.getOutputStream().write(bpmnXML);
            }else {
                log.warn("获取model的xml文件失败,流程为null：modelId={}", modelId);
            }
        } catch (Exception e) {
            log.error("获取model的xml文件失败：modelId={}", modelId, e);
        }
    }
    @Override
    public PageModel<DeploymentVO> getDeploymentList(DeploymentQueryDTO deploymentQueryDTO) {

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        if(!StringUtils.isEmpty(deploymentQueryDTO.getDeploymentNameLike())){
            deploymentQuery.deploymentNameLike(deploymentQueryDTO.getDeploymentNameLike());
        }
        if(!StringUtils.isEmpty(deploymentQueryDTO.getDeploymentCategory())){
            deploymentQuery.deploymentCategory(deploymentQueryDTO.getDeploymentCategory());
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
        long count = deploymentQuery.orderByDeploymenTime().desc().
                count();
        List<Deployment> deployments = deploymentQuery.orderByDeploymenTime().desc().
                listPage(deploymentQueryDTO.getPageNum(), deploymentQueryDTO.getPageSize());
        List<DeploymentVO> deploymentVoList = deployments.stream().map(t -> {
            DeploymentVO deploymentVO = new DeploymentVO();
            BeanUtils.copyProperties(t, deploymentVO);
            return deploymentVO;
        }).collect(Collectors.toList());

        PageModel<DeploymentVO> pageModel = new PageModel<> ();
        pageModel.setTotalCount((int)count);
        pageModel.setPagedRecords(deploymentVoList);
        return pageModel;
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
                if(type.equals("xml")){
                    String xmlType=".xml";
                    String bpmnType=".bpmn";
                    resourceName = deploymentResourceNames.stream().filter(p -> (p.endsWith(xmlType) || p.endsWith(bpmnType))).findFirst().orElse(null);
                }else {
                    String pngType=".png";
                    resourceName = deploymentResourceNames.stream().filter(p -> p.endsWith(pngType)).findFirst().orElse(null);
                }
            }
            if (type.equals("xml")) {
                response.setHeader("Content-type", "text/xml;charset=UTF-8");
                InputStream inputStream = repositoryService.getResourceAsStream(id, resourceName);
                b = IoUtil.readInputStream(inputStream, resourceName);
            } else if(type.equals("png")){
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
        paramMap.put(FlowConstants.START_USER_ID,startUserId);

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
    public ProcessInstance startProcessInstanceByAppForm(AppForm appForm) {
        String startUserId=appForm.getStartUserId();
        //业务参数转成map
        Map<String, Object> paramMap = BeanUtil.beanToMap(appForm);
        identityService.setAuthenticatedUserId(startUserId);
        paramMap.put(FlowConstants.BUS_VAR,appForm.getClassInfoJson());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(appForm.getFlowDef().getCode(),appForm.getBusinessKey(),paramMap);

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
    public PageModel<TaskVO> findTasksByUserId(String userId, TaskBaseDTO taskBaseDTO) {
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
        long count = taskQuery
                .orderByTaskCreateTime()
                .desc()
                .count();
        List<Task> taskList = taskQuery.endOr()
                .orderByTaskCreateTime()
                .desc()
                .listPage(taskBaseDTO.getPageNum(), taskBaseDTO.getPageSize());

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
        PageModel<TaskVO> pageModel = new PageModel<> ();
        pageModel.setTotalCount((int)count);
        pageModel.setPagedRecords(taskVoList);
        return pageModel;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(CompleteTaskDTO completeTaskDTO) {
        Task task = this.getTask(completeTaskDTO.getTaskId());
        if(StringUtils.isEmpty(task)){
            log.info("完成任务时，该任务ID:%不存在",completeTaskDTO.getTaskId());
            throw new BusinessException(String.format("该任务ID:%不存在",completeTaskDTO.getTaskId()));
        }
        ////设置审批人，若不设置则数据表userid字段为null
        Authentication.setAuthenticatedUserId(completeTaskDTO.getUserId());
        if(!StringUtils.isEmpty(completeTaskDTO.getComment())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),FlowConstants.OPINION,completeTaskDTO.getComment());
        }else {
            taskService.addComment(task.getId(),task.getProcessInstanceId(),FlowConstants.OPINION,"");
        }
        List<FileEntry> files = completeTaskDTO.getFiles();
        if(!CollectionUtils.isEmpty(files)){
            files.stream().forEach(t->
                taskService.createAttachment("url",task.getId(),task.getProcessInstanceId(),t.getKey(),t.getName(),t.getUrl())
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

        //claim the task，当任务分配给了某一组人员时，需要该组人员进行抢占。抢到了就将该任务给谁处理，其他人不能处理。认领任务
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
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().
                    processInstanceId(processId)
                    .singleResult();

            processDefinitionId=pi.getProcessDefinitionId();
        } else {// 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().
                    processInstanceId(processId).
                    singleResult();
            processDefinitionId=pi.getProcessDefinitionId();
        }
        //活动节点
        List<String> highLightedActivitis = new ArrayList<String>();
        //线节点
        List<String> flows = new ArrayList<>();
        /**
         * 获得所有的历史活动的节点对象
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
        //现在正处的节点
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();
        for (Task task : tasks) {
            highLightedActivitis.add(task.getTaskDefinitionKey());
        }
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

    @Override
    public List<HistoricTaskInstanceVO>  getHistoricTaskInstanceNoPage(HistoricTaskInstanceDTO historicTaskInstanceDTO){
        String userId= historicTaskInstanceDTO.getUserId();
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();

        if(!StringUtils.isEmpty(historicTaskInstanceDTO.getProcessInstanceId())){
            historicTaskInstanceQuery= historicTaskInstanceQuery.processInstanceId(historicTaskInstanceDTO.getProcessInstanceId());
        }

        if(!StringUtils.isEmpty(historicTaskInstanceDTO.getTaskDefinitionKey())){
           historicTaskInstanceQuery.processInstanceId(historicTaskInstanceDTO.getTaskDefinitionKey());
        }

        if(!StringUtils.isEmpty(userId)){
            historicTaskInstanceQuery.taskAssignee(historicTaskInstanceDTO.getUserId());
            //historicTaskInstanceQuery.taskCandidateUser(userId);
            //historicTaskInstanceQuery.taskInvolvedUser(userId);
        }

        if(!StringUtils.isEmpty(historicTaskInstanceDTO.getBusinessKey())){
            historicTaskInstanceQuery.processInstanceBusinessKey(historicTaskInstanceDTO.getBusinessKey());
        }

        if(!StringUtils.isEmpty(historicTaskInstanceDTO.getProcessStatus())){
            Integer processStatus= historicTaskInstanceDTO.getProcessStatus();
            if(processStatus==FlowFinishedStatusEnum.FLOW_ING.getCode()){
                historicTaskInstanceQuery.unfinished();
            }else if(processStatus==FlowFinishedStatusEnum.FLOW_FINISHED.getCode()){
                historicTaskInstanceQuery.finished();
            }
        }
        historicTaskInstanceQuery.includeTaskLocalVariables().includeProcessVariables();
        List<HistoricTaskInstance> list = historicTaskInstanceQuery.orderByTaskCreateTime().asc().list();
        List<HistoricTaskInstanceVO> historicTaskInstanceVOS = HistoricTaskInstanceVO.warpList(list);
        setHistoricTaskInstanceVos(historicTaskInstanceVOS);
        return historicTaskInstanceVOS;
    }

    private void setHistoricTaskInstanceVos(List<HistoricTaskInstanceVO> list){

        list.parallelStream().forEach(t -> {
            if (!StringUtils.isEmpty(t.getAssignee())) {
                SysUser sysUser = sysUserService.selectUserById(Long.parseLong(t.getAssignee()));
                t .setAssigneeName(sysUser.getUserName());
            }else {
                //下个节点

            }

            Optional.ofNullable( t.getTaskLocalVariables()).ifPresent(u->{
                Object isPass =Optional.ofNullable(u.get(FlowConstants.IS_PASS)).orElse("");
                t.setIsPass(String.valueOf(isPass));
            });

            List<Comment> comment = taskService.getTaskComments(t.getId(), FlowConstants.OPINION);

            List<Attachment> taskAttachments = taskService.getTaskAttachments(t.getId());
            t.setCommentList(comment);
            t.setAttachmentList(taskAttachments);
            //计算任务历时
            if(!StringUtils.isEmpty(t.getEndTime())){
                String spendTime = DateUtil.formatBetween(DateUtil.between(t.getCreateTime(), t.getEndTime(), DateUnit.SECOND));
                t.setSpendTime(spendTime);
            }
            HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceById(t.getProcessInstanceId());
            t.setProcessName(historicProcessInstance.getProcessDefinitionName());
            t.setBusinessKey(historicProcessInstance.getBusinessKey());
        });
    }

    /**
     * 思路就是我们取出节点的表达式，然后用我们流程实例的变量来给他翻译出来即可
     * @param processInstanceId
     */
    @Override
    public List<TaskVO> getDynamicFlowNodeInfo(String processInstanceId) {
        //获取历史变量表
   /*     List<HistoricVariableInstance> historicVariableInstanceList = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();*/
        HistoricProcessInstance processInstance= getHistoricProcessInstanceById(processInstanceId);
        List<TaskVO> hisTaskVOList=Lists.newArrayList();
        HistoricTaskInstanceDTO historicTaskInstanceDTO=new HistoricTaskInstanceDTO();
        historicTaskInstanceDTO.setProcessInstanceId(processInstanceId);
        List<HistoricTaskInstanceVO> historicTaskInstance = getHistoricTaskInstanceNoPage(historicTaskInstanceDTO);
        if(!CollectionUtils.isEmpty(historicTaskInstance)){
            hisTaskVOList = historicTaskInstance.stream().map(t -> {
                TaskVO taskVO = new TaskVO();
                taskVO.setTaskName(t.getName());
                taskVO.setProcessInstanceId(t.getProcessInstanceId());
                taskVO.setStartUserId(processInstance.getStartUserId());
                taskVO.setStartUserName(getUserNameById(processInstance.getStartUserId()));
                taskVO.setTaskDefinitionKey(t.getTaskDefinitionKey());
                if (!StringUtils.isEmpty(t.getCreateTime())) {
                    taskVO.setCreateDate(t.getCreateTime());
                }
                if (!StringUtils.isEmpty(t.getEndTime())) {
                    taskVO.setCompleteTime(t.getEndTime());
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
                if(hisTaskVOList.get(i).getTaskName().equals(futureTask.get(j).getTaskName())&&CollectionUtils.isEmpty(futureTask.get(j).getHandleNameList())){
                    futureTask.remove(j);
                }
            }
        }
        hisTaskVOList.addAll(futureTask);
        return hisTaskVOList;
    }

    @Override
    public PageModel<ProcessInstanceVO> getHistoricProcessInstance(ProcessInstanceDTO processInstanceDTO) {
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
        historicProcessInstanceQuery.includeProcessVariables();
        long count = historicProcessInstanceQuery.
                orderByProcessInstanceStartTime().
                desc().
                count();
        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.
                orderByProcessInstanceStartTime().
                desc().
                listPage(processInstanceDTO.getPageNum(), processInstanceDTO.getPageSize());
        List<ProcessInstanceVO> processInstanceVOS = ProcessInstanceVO.warpList(historicProcessInstances);
        setProcessInstanceVOs(processInstanceVOS);
        PageModel<ProcessInstanceVO> pageModel = new PageModel<> ();
        pageModel.setTotalCount((int)count);
        pageModel.setPagedRecords(processInstanceVOS);
        //List<ProcessInstanceVO> processInstanceVOS = com.snow.common.utils.bean.BeanUtils.transformList(pageInfo.getList(), ProcessInstanceVO.class);
        return pageModel;
    }

    /**
     * 赋值ProcessInstanceVOs
     * @param processInstanceVOS
     */
    public void  setProcessInstanceVOs(List<ProcessInstanceVO> processInstanceVOS){
        processInstanceVOS.parallelStream().forEach(t->{

            Map<String, Object> processVariables = t.getProcessVariables();

            t.setProcessVariables(processVariables);
            //计算流程用时
            if(StringUtils.isEmpty(t.getEndTime())){
                String spendTime = DateUtil.formatBetween(t.getStartTime(), new Date(), BetweenFormater.Level.SECOND);
                t.setProcessSpendTime(spendTime);
            }else {
                String spendTime = DateUtil.formatBetween(t.getStartTime(), t.getEndTime(), BetweenFormater.Level.SECOND);
                t.setProcessSpendTime(spendTime);
            }
            String startUserId = t.getStartUserId();
            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(startUserId));
            t.setStartUserName(sysUser.getUserName());
        });
    }
    @Override
    public PageModel<HistoricTaskInstanceVO> getHistoricTaskInstance(HistoricTaskInstanceDTO historicTaskInstanceDTO) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        if(!StringUtils.isEmpty(historicTaskInstanceDTO.getProcessDefinitionName())){
            historicTaskInstanceQuery.processDefinitionName(historicTaskInstanceDTO.getProcessDefinitionName());
        }
        if(!StringUtils.isEmpty(historicTaskInstanceDTO.getUserId())){
            historicTaskInstanceQuery.taskAssignee(historicTaskInstanceDTO.getUserId());
        }
        if(!StringUtils.isEmpty(historicTaskInstanceDTO.getBusinessKey())){
            historicTaskInstanceQuery.processInstanceBusinessKey(historicTaskInstanceDTO.getBusinessKey());
        }
        if(!StringUtils.isEmpty(historicTaskInstanceDTO.getBusinessKeyLike())){
            historicTaskInstanceQuery.processInstanceBusinessKeyLike(historicTaskInstanceDTO.getBusinessKeyLike());
        }
        long count = historicTaskInstanceQuery.orderByHistoricTaskInstanceStartTime().
                desc().
                count();
        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery.orderByHistoricTaskInstanceStartTime().
                desc().
                listPage(historicTaskInstanceDTO.getPageNum(), historicTaskInstanceDTO.getPageSize());


        List<HistoricTaskInstanceVO> historicTaskInstanceVOS = historicTaskInstances.stream().map(t -> {
            HistoricTaskInstanceVO historicTaskInstanceVO = new HistoricTaskInstanceVO();
            BeanUtils.copyProperties(t, historicTaskInstanceVO);
            if (!StringUtils.isEmpty(t.getAssignee())) {
                SysUser sysUser = sysUserService.selectUserById(Long.parseLong(t.getAssignee()));
                historicTaskInstanceVO.setAssignee(sysUser.getUserName());
            }
            String spendTime = DateUtil.formatBetween(DateUtil.between(t.getCreateTime(), t.getEndTime(), DateUnit.MS));
            historicTaskInstanceVO.setSpendTime(spendTime);
            HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceById(t.getProcessInstanceId());
            historicTaskInstanceVO.setProcessName(historicProcessInstance.getProcessDefinitionName());
            historicTaskInstanceVO.setBusinessKey(historicProcessInstance.getBusinessKey());
            return historicTaskInstanceVO;

        }).collect(Collectors.toList());

        PageModel<HistoricTaskInstanceVO> pageModel = new PageModel<> ();
        pageModel.setTotalCount((int)count);
        pageModel.setPagedRecords(historicTaskInstanceVOS);
        return pageModel;
    }

    
    /**
     * 获取所有的任务节点
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
                        //固定人
                        String assignee=userTask.getAssignee();
                        List<String> candidateGroups = userTask.getCandidateGroups();
                        List<String> candidateUsers = userTask.getCandidateUsers();
                        if(!CollectionUtils.isEmpty(candidateUsers)){
                            //获取的是多实例会签
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
                                            //获取表达式的值
                                            //  expressionValue =(String) expressionService.getValue(processInstanceId,candidateUser);
                                            //先这样写
                                            handleNameList.add(candidateUser);
                                        }

                                    }
                                }
                                TaskVO.TaskVOBuilder taskVOBuilder = TaskVO.builder().taskName(userTask.getName()).
                                        handleNameList(handleNameList).
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
                            //todo 多实例会签出来
                            else {
                                String inputDataItem = loopCharacteristics.getInputDataItem();
                                List<String> values = (List)expressionService.getValue(processInstanceId, inputDataItem);
                                System.out.println(JSON.toJSON(values));
                            }
                        }
                        if(!CollectionUtils.isEmpty(candidateGroups)){
                            //获取的是多实例会签
                            MultiInstanceLoopCharacteristics loopCharacteristics = userTask.getLoopCharacteristics();
                            if(loopCharacteristics == null){
                                String expressionValue=null;
                                if(userTask.getId().equals("userTask0")){
                                    expressionValue = processInstance.getStartUserId();
                                    handleNameList.add(expressionValue);
                                }else {
                                    for (String candidateGroup:candidateGroups){
                                        if(com.snow.common.utils.StringUtils.isNumeric(candidateGroup)){
                                            List<SysUser> sysUsers = sysUserMapper.selectUserListByRoleId(candidateGroup);
                                            if(!CollectionUtils.isEmpty(sysUsers)){
                                                List<String> collect = sysUsers.stream().map(SysUser::getUserName).collect(Collectors.toList());
                                                handleNameList.addAll(collect);
                                            }
                                        }else {
                                            //获取表达式的值
                                            expressionValue =(String) expressionService.getValue(processInstanceId,candidateGroup);
                                            //先这样写
                                            handleNameList.add(candidateGroup);
                                        }
                                    }
                                    TaskVO.TaskVOBuilder taskVOBuilder = TaskVO.builder().taskName(userTask.getName()).
                                            handleNameList(handleNameList).
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
                            //todo 多实例会签出来
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
                                    //获取表达式的值
                                    expressionValue =(String) expressionService.getValue(processInstanceId, userTask.getAssignee());
                                    handleNameList.add(expressionValue);
                                }
                            }else {
                                String inputDataItem = loopCharacteristics.getInputDataItem();
                                List<String> values = (List)expressionService.getValue(processInstanceId, inputDataItem);
                                System.out.println(JSON.toJSON(values));
                            }
                            TaskVO.TaskVOBuilder taskVOBuilder = TaskVO.builder().taskName(userTask.getName()).
                                    handleNameList(handleNameList).
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

   public String getUserNameById(String id){
       return sysUserService.selectUserById(Long.parseLong(id)).getUserName();
   }
}
