package com.snow.flowable.listener.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.snow.common.constant.CacheConstants;
import com.snow.common.constant.MessageConstants;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.enums.DingTalkMessageType;
import com.snow.common.utils.CacheUtils;
import com.snow.dingtalk.model.request.WorkrecordAddRequest;
import com.snow.flowable.common.SpringContextUtil;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.listener.AbstractEventListener;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.impl.FlowableUserServiceImpl;
import com.snow.framework.web.domain.common.SysSendMessageRequest;
import com.snow.framework.web.service.MailMessageService;
import com.snow.system.domain.SysUser;
import com.snow.system.event.SyncEvent;
import com.snow.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.event.FlowableProcessStartedEvent;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author qimingjin
 * @Title: ????????????
 * @Description:
 * @date 2020/12/8 14:38
 */
@Slf4j
@Component
@Deprecated
public class SendMessageEventLister extends AbstractEventListener {

    @Resource
    private ApplicationContext applicationContext;


    public SendMessageEventLister() {
        super(
                new HashSet<>(Arrays.asList(
                        FlowableEngineEventType.TASK_CREATED,
                        FlowableEngineEventType.PROCESS_STARTED,
                        FlowableEngineEventType.PROCESS_COMPLETED
                       // FlowableEngineEventType.TASK_OWNER_CHANGED
                )),
                new HashSet<>(Arrays.asList(
                        FlowDefEnum.SNOW_OA_LEAVE,
                        FlowDefEnum.PURCHASE_ORDER_PROCESS,
                        FlowDefEnum.SNOW_OA_RESIGN_PROCESS,
                        FlowDefEnum.SNOW_FN_PAYMENT,
                        FlowDefEnum.SNOW_OA_CUSTOMER_ADMITTANCE
                )));
    }


    /**
     * ????????????
     *
     * @param event
     */
    @Override
    protected void processStarted(FlowableProcessStartedEvent event) {
        log.info("ManagerTaskEventListener----processStarted?????????????????????{}", JSON.toJSONString(event));
        ExecutionEntity execution = (ExecutionEntity) event.getEntity();

        //??????????????????????????????
       // NewsTriggerService newsTriggerService = (NewsTriggerService) SpringContextUtil.getBean(NewsTriggerService.class);

       // boolean dingTalkOnOff = newsTriggerService.getDingTalkOnOff(execution.getProcessDefinitionKey(), FlowableEngineEventType.TASK_CREATED.name());
       // boolean emailOnOff = newsTriggerService.getEmailOnOff(execution.getProcessDefinitionKey(), FlowableEngineEventType.TASK_CREATED.name());
        //????????????
       // if (dingTalkOnOff) {
            this.sendProcessStartedDingTalkMessage(event);
      //  }
        // ????????????
       // if (emailOnOff) {
           this.sendProcessStartedEmailMessage(event);
      //  }
    }

    /**
     * ???????????????
     *
     * @param event
     */
    protected void processCompleted(FlowableEngineEntityEvent event) {
        log.info("ManagerTaskEventListener----processCompleted?????????????????????{}", JSON.toJSONString(event));
        HistoricProcessInstance hisProcessInstance = getHisProcessInstance(event.getProcessInstanceId());
       // boolean dingTalkOnOff = newsTriggerService.getDingTalkOnOff(hisProcessInstance.getProcessDefinitionKey(), FlowableEngineEventType.PROCESS_COMPLETED.name());
       // boolean emailOnOff = newsTriggerService.getEmailOnOff(hisProcessInstance.getProcessDefinitionKey(), FlowableEngineEventType.PROCESS_COMPLETED.name());
        //????????????
      //  if (dingTalkOnOff) {
            this.sendProcessCompletedDingTalkMessage(event);
      //  }
        // ????????????
       // if (emailOnOff) {
            this.sendProcessCompletedEmailMessage(event);
       // }
    }


    /**
     * ????????????(??????)
     *
     * @param event
     */
    @Override
    protected void taskCreated(FlowableEngineEntityEvent event) {
        //???????????????????????????????????????????????????(?????????)
        log.info("ManagerTaskEventListener----taskCreated?????????????????????{}", JSON.toJSONString(event));
        ProcessDefinition processDefinition = getProcessDefinition(event.getProcessDefinitionId());
       // NewsTriggerService newsTriggerService = (NewsTriggerService) SpringContextUtil.getBean(NewsTriggerService.class);
       // boolean dingTalkOnOff = newsTriggerService.getDingTalkOnOff(processDefinition.getKey(), FlowableEngineEventType.TASK_CREATED.name());
      //  boolean emailOnOff = newsTriggerService.getEmailOnOff(processDefinition.getKey(), FlowableEngineEventType.TASK_CREATED.name());
        //????????????
       // if (dingTalkOnOff) {
            this.sendDingTalkMessage(event);
     //   }
        // ????????????
     //   if (emailOnOff) {
            this.sendTaskCreateEmailMessage(event);
      //  }

    }

    /**
     * ??????????????????????????????
     *
     * @param event
     */
    public void sendDingTalkMessage(FlowableEngineEntityEvent event) {
        ThreadPoolExecutor executor = ExecutorBuilder.create().setCorePoolSize(5)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(100))
                .build();
        executor.execute(() -> {
            //????????????ID?????????????????????
            TaskEntity entity = (TaskEntity) event.getEntity();
            Set<SysUser> flowCandidates = getFlowCandidates(entity);
            if (CollectionUtils.isNotEmpty(flowCandidates)) {
                flowCandidates.forEach(t -> {
                    String userId = String.valueOf(t.getUserId());
                    if (!StringUtils.isEmpty(userId)) {
                        WorkrecordAddRequest workrecordAddRequest = initWorkRecordAddRequest(userId, event);
                        SyncEvent<WorkrecordAddRequest> syncEventGroup = new SyncEvent(workrecordAddRequest, DingTalkListenerType.WORK_RECODE_OLD_CREATE);
                        applicationContext.publishEvent(syncEventGroup);
                    }
                });
            } else {
                log.warn("ManagerTaskEventListener----taskCreated?????????????????? userId???groupId is all null");
            }
        });
        executor.shutdown();
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    public void sendProcessStartedEmailMessage(FlowableProcessStartedEvent event) {
        MailMessageService mailService = (MailMessageService)SpringContextUtil.getBean(MailMessageService.class);
        ThreadPoolExecutor executor = ExecutorBuilder.create().setCorePoolSize(5)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(100))
                .build();
        executor.execute(() -> {

            ExecutionEntity execution = (ExecutionEntity) event.getEntity();
            ProcessInstance processInstance = execution.getProcessInstance();
            Map<String, Object> map = new HashMap<>();
            map.put("toUser", getUserInfo(processInstance.getStartUserId()).getUserName());
            map.put("starttime", DateUtil.formatDateTime(processInstance.getStartTime()));
            map.put("processInstance", processInstance.getProcessDefinitionName());
            map.put("url", CacheUtils.getSysConfig(CacheConstants.SYS_DOMAIN,"http://localhost")+"/flow/getMyHistoricProcessInstance");
            map.put("datetime", DateUtil.formatDateTime(new Date()));
            SysSendMessageRequest sysSendMessageDTO = SysSendMessageRequest.builder().templateByCode(MessageConstants.PROCESS_STARTED_CODE)
                    .receiver(getUserInfo(processInstance.getStartUserId()).getEmail())
                    .paramMap(map)
                    .build();
            mailService.sendTemplateSimpleMail(sysSendMessageDTO);
        });
        executor.shutdown();

    }

    /**
     * ??????????????????
     *
     * @param event
     */
    public void sendTaskCreateEmailMessage(FlowableEngineEntityEvent event) {
        FlowableService flowableService = (FlowableService)SpringContextUtil.getBean(FlowableService.class);
        MailMessageService mailService = (MailMessageService)SpringContextUtil.getBean(MailMessageService.class);
        ProcessInstance processInstance = flowableService.getProcessInstanceById(event.getProcessInstanceId());
        ProcessDefinition processDefinition = getProcessDefinition(event.getProcessDefinitionId());
        //????????????ID?????????????????????
        TaskEntity entity = (TaskEntity) event.getEntity();
        Set<SysUser> flowCandidates = getFlowCandidates(entity);
        if (CollectionUtils.isNotEmpty(flowCandidates)) {
            ThreadPoolExecutor executor = ExecutorBuilder.create().setCorePoolSize(5)
                    .setMaxPoolSize(10)
                    .setWorkQueue(new LinkedBlockingQueue<>(100))
                    .build();
            executor.execute(() ->
                    flowCandidates.forEach(t -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("toUser", t.getUserName());
                        map.put("startUser", getUserInfo(processInstance.getStartUserId()).getUserName());
                        map.put("processInstance", processDefinition.getName());
                        map.put("url", CacheUtils.getSysConfig(CacheConstants.SYS_DOMAIN,"http://localhost")+"/flow/findTasksByUserId");
                        map.put("datetime", DateUtil.formatDateTime(new Date()));
                        SysSendMessageRequest sysSendMessageDTO = SysSendMessageRequest.builder().templateByCode(MessageConstants.TASK_CREATED_CODE)
                                .receiver(t.getEmail())
                                .paramMap(map)
                                .build();
                        mailService.sendTemplateSimpleMail(sysSendMessageDTO);
                    })
            );
            executor.shutdown();

        }

    }


    /**
     * ??????????????????
     *
     * @param event
     */
    public void sendProcessStartedDingTalkMessage(FlowableProcessStartedEvent event) {
        ThreadPoolExecutor executor = ExecutorBuilder.create().setCorePoolSize(5)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(100))
                .build();
        executor.execute(() -> {

            ExecutionEntity execution = (ExecutionEntity) event.getEntity();
            ProcessInstance processInstance = execution.getProcessInstance();
            Map<String, Object> map = new HashMap<>();
            map.put("toUser", getUserInfo(processInstance.getStartUserId()).getUserName());
            map.put("starttime", DateUtil.formatDateTime(processInstance.getStartTime()));
            map.put("processInstance", processInstance.getProcessDefinitionName());
            map.put("url", CacheUtils.getSysConfig(CacheConstants.SYS_DOMAIN,"http://localhost")+"/flow/getMyHistoricProcessInstance");
            map.put("datetime", DateUtil.formatDateTime(new Date()));

            SysSendMessageRequest sysSendMessageDTO = SysSendMessageRequest.builder().templateByCode(MessageConstants.PROCESS_STARTED_CODE)
                    .receiverSet(Sets.newHashSet(getUserInfo(processInstance.getStartUserId()).getDingUserId()))
                    .paramMap(map)
                    .dingTalkMessageType(DingTalkMessageType.TEXT)
                    .build();
            SyncEvent<SysSendMessageRequest> syncEventGroup = new SyncEvent(sysSendMessageDTO, DingTalkListenerType.ASYNCSEND_V2);
            applicationContext.publishEvent(syncEventGroup);
        });
        executor.shutdown();

    }

    /**
     * ??????????????????????????????
     *
     * @param event
     */
    public void sendProcessCompletedEmailMessage(FlowableEngineEntityEvent event) {
        MailMessageService mailService = (MailMessageService)SpringContextUtil.getBean(MailMessageService.class);
        ThreadPoolExecutor executor = ExecutorBuilder.create().setCorePoolSize(5)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(100))
                .build();
        executor.execute(() -> {
            HistoricProcessInstance hisProcessInstance = getHisProcessInstance(event.getProcessInstanceId());
            Map<String, Object> map = buildSendProcessCompletedParam(hisProcessInstance);
            SysSendMessageRequest sysSendMessageDTO = SysSendMessageRequest.builder().templateByCode(MessageConstants.PROCESS_COMPLETED_CODE)
                    .receiver(getUserInfo(hisProcessInstance.getStartUserId()).getEmail())
                    .paramMap(map)
                    .build();
            mailService.sendTemplateSimpleMail(sysSendMessageDTO);
        });
        executor.shutdown();

    }

    /**
     * ??????????????????????????????
     *
     * @param event
     */
    public void sendProcessCompletedDingTalkMessage(FlowableEngineEntityEvent event) {
        ThreadPoolExecutor executor = ExecutorBuilder.create().setCorePoolSize(5)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(100))
                .build();
        executor.execute(() -> {
            HistoricProcessInstance hisProcessInstance = getHisProcessInstance(event.getProcessInstanceId());
            Map<String, Object> map = buildSendProcessCompletedParam(hisProcessInstance);

            SysSendMessageRequest sysSendMessageDTO = SysSendMessageRequest.builder().templateByCode(MessageConstants.PROCESS_COMPLETED_CODE)
                    .receiverSet(Sets.newHashSet(getUserInfo(hisProcessInstance.getStartUserId()).getDingUserId()))
                    .paramMap(map)
                    .dingTalkMessageType(DingTalkMessageType.TEXT)
                    .build();
            SyncEvent syncEventGroup = new SyncEvent(sysSendMessageDTO, DingTalkListenerType.ASYNCSEND_V2);
            applicationContext.publishEvent(syncEventGroup);
        });
        executor.shutdown();

    }

    /**
     * ????????????????????????
     *
     * @param hisProcessInstance
     */
    private Map<String, Object> buildSendProcessCompletedParam(HistoricProcessInstance hisProcessInstance) {
        Map<String, Object> map = new HashMap<>();
        map.put("toUser", getUserInfo(hisProcessInstance.getStartUserId()).getUserName());
        map.put("starttime", DateUtil.formatDateTime(hisProcessInstance.getStartTime()));
        map.put("orderNo", hisProcessInstance.getBusinessKey());
        map.put("processInstance", hisProcessInstance.getProcessDefinitionName());
        String spendTime = DateUtil.formatBetween(hisProcessInstance.getStartTime(), new Date(), BetweenFormater.Level.SECOND);
        map.put("time", spendTime);
        map.put("url", CacheUtils.getSysConfig(CacheConstants.SYS_DOMAIN,"http://localhost")+"/flow/getMyHistoricProcessInstance");
        map.put("datetime", DateUtil.formatDateTime(new Date()));
        return map;
    }

    /**
     * ????????????
     *
     * @param userId ????????????????????????
     * @param event
     * @return
     */
    public WorkrecordAddRequest initWorkRecordAddRequest(String userId, FlowableEngineEntityEvent event) {
        FlowableService flowableService = (FlowableService)SpringContextUtil.getBean(FlowableService.class);
        ProcessInstance processInstance = flowableService.getProcessInstanceById(event.getProcessInstanceId());
        TaskEntity entity = (TaskEntity) event.getEntity();
        ProcessDefinition processDefinition = getProcessDefinition(event.getProcessDefinitionId());
        WorkrecordAddRequest workrecordAddRequest = new WorkrecordAddRequest();
        SysUser userInfo = getUserInfo(userId);
        workrecordAddRequest.setUserid(userInfo.getDingUserId());
        // workrecordAddRequest.setBizId(processInstance.getBusinessKey());
        workrecordAddRequest.setUrl(entity.getFormKey());
        workrecordAddRequest.setPcUrl(entity.getFormKey());
        workrecordAddRequest.setSourceName("DingFlow");
        workrecordAddRequest.setPcOpenType(2L);
        workrecordAddRequest.setTitle(processDefinition.getName());
        workrecordAddRequest.setCreateTime(entity.getCreateTime().getTime());
        List<WorkrecordAddRequest.FormItemVo> formItemList = Lists.newArrayList();
        WorkrecordAddRequest.FormItemVo formItemVo = new WorkrecordAddRequest.FormItemVo();
        formItemVo.setTitle("??????");
        formItemVo.setContent(processInstance.getBusinessKey());
        formItemList.add(formItemVo);
        WorkrecordAddRequest.FormItemVo formItemVo3 = new WorkrecordAddRequest.FormItemVo();
        formItemVo3.setTitle("????????????");
        formItemVo3.setContent(entity.getName());
        formItemList.add(formItemVo3);
        WorkrecordAddRequest.FormItemVo formItemVo1 = new WorkrecordAddRequest.FormItemVo();
        formItemVo1.setTitle("????????????");
        formItemVo1.setContent(DateUtil.format(entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        formItemList.add(formItemVo1);
        WorkrecordAddRequest.FormItemVo formItemVo2 = new WorkrecordAddRequest.FormItemVo();
        formItemVo2.setTitle("?????????");
        formItemVo2.setContent(getUserInfo(processInstance.getStartUserId()).getUserName());
        formItemList.add(formItemVo2);
        workrecordAddRequest.setFormItemList(formItemList);
        return workrecordAddRequest;
    }

    /**
     * ????????????????????????
     *
     * @param processDefinitionId
     * @return
     */
    protected ProcessDefinition getProcessDefinition(String processDefinitionId) {
        RepositoryService repositoryService = (RepositoryService)SpringContextUtil.getBean(RepositoryService.class);
        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
    }

    /**
     * ????????????????????????
     *
     * @param processInstanceId
     * @return
     */
    protected HistoricProcessInstance getHisProcessInstance(String processInstanceId) {
        HistoryService historyService = (HistoryService)SpringContextUtil.getBean(HistoryService.class);
        return historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
    }

    /**
     * ??????????????????
     *
     * @param userId
     * @return
     */
    protected SysUser getUserInfo(String userId) {
        SysUserServiceImpl sysUserService = (SysUserServiceImpl)SpringContextUtil.getBean(SysUserServiceImpl.class);
        return Optional.ofNullable(sysUserService.selectUserById(Long.parseLong(userId))).orElse(
                new SysUser(1L, "System", "manager4480")
        );
    }


    /**
     * ????????????ID?????????????????????
     *
     * @param entity
     * @return
     */
    private Set<SysUser> getFlowCandidates(TaskEntity entity) {
        FlowableUserServiceImpl flowableUserService = (FlowableUserServiceImpl)SpringContextUtil.getBean(FlowableUserServiceImpl.class);
        Set<IdentityLink> candidates = entity.getCandidates();
        Set<SysUser> sysUserSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(candidates)) {
            candidates.forEach(t -> {
                String userId = t.getUserId();
                String groupId = t.getGroupId();
                if (StrUtil.isNotEmpty(userId)) {
                    sysUserSet.add(getUserInfo(userId));
                } else if (StrUtil.isNotEmpty(groupId)) {
                    List<SysUser> sysUsers = flowableUserService.getUserByFlowGroupId(Long.parseLong(groupId));
                    if (CollUtil.isNotEmpty(sysUsers)) {
                        sysUserSet.addAll(CollUtil.newHashSet(sysUsers));
                    }
                }
            });
        }
        return sysUserSet;
    }


}
