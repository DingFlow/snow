package com.snow.flowable.listener.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.dingtalk.model.WorkrecordAddRequest;
import com.snow.flowable.common.SpringContextUtil;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.listener.AbstractEventListener;
import com.snow.flowable.service.FlowableService;
import com.snow.system.domain.SysUser;
import com.snow.system.event.SyncEvent;
import com.snow.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/12/8 14:38
 */
@Slf4j
@Service
public class SendMessageEventLister extends AbstractEventListener {

    @Resource
    private ApplicationContext applicationContext;

    public SendMessageEventLister() {
        super(
                new HashSet<>(Arrays.asList(
                        FlowableEngineEventType.TASK_CREATED
                )),
                new HashSet<>(Arrays.asList(
                        FlowDefEnum.SNOW_OA_LEAVE
                )));
    }

    @Override
    protected void process(FlowableEngineEvent flowableEngineEvent) {
        DelegateExecution execution = getExecution(flowableEngineEvent);
        log.info("process========>{}",JSON.toJSONString(execution));
    }



    @Override
    protected void taskCreated(FlowableEngineEntityEvent event) {
        //任务创建可发送短信，邮件通知接收人
        log.info("ManagerTaskEventListener----taskCreated任务创建监听：{}",JSON.toJSONString(event));
        sendDingTalkMessage(event);
    }

    /**
     * 发送钉钉工作通知消息
     * @param event
     */
    public void sendDingTalkMessage(FlowableEngineEntityEvent event){
        SysUserMapper sysUserMapper = (SysUserMapper)SpringContextUtil.getBean(SysUserMapper.class);
        ThreadPoolExecutor executor = ExecutorBuilder.create().setCorePoolSize(5)
                .setMaxPoolSize(10)
                .setWorkQueue(new LinkedBlockingQueue<>(100))
                .build();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //根据任务ID获取任务获选人人
                TaskEntity entity = (TaskEntity)event.getEntity();
                Set<IdentityLink> candidates = entity.getCandidates();
                if(!CollectionUtils.isEmpty(candidates)){
                    candidates.forEach(t->{
                        String userId = t.getUserId();
                        String groupId = t.getGroupId();
                        if(!StringUtils.isEmpty(userId)){
                            WorkrecordAddRequest workrecordAddRequest = initWorkRecordAddRequest(userId, event);
                            SyncEvent syncEventGroup = new SyncEvent(workrecordAddRequest, DingTalkListenerType.WORK_RECODE_CREATE);
                            applicationContext.publishEvent(syncEventGroup);
                        }
                        else if(!StringUtils.isEmpty(groupId)) {
                            List<SysUser> sysUsers = sysUserMapper.selectUserListByRoleId(groupId);
                            sysUsers.forEach(sysUser->{
                                WorkrecordAddRequest workrecordAddRequest = initWorkRecordAddRequest(String.valueOf(sysUser.getUserId()), event);
                                SyncEvent syncEventGroup = new SyncEvent(workrecordAddRequest, DingTalkListenerType.WORK_RECODE_CREATE);
                                applicationContext.publishEvent(syncEventGroup);
                            });
                        }else {
                            log.warn("ManagerTaskEventListener----taskCreated任务创建监听 userId和groupId is all null");
                        }

                    });
                }
            }
        });
        executor.shutdown();
    }

    /**
     * 组装参数
     * @param userId
     * @param event
     * @return
     */
    public WorkrecordAddRequest initWorkRecordAddRequest(String userId,FlowableEngineEntityEvent event){
        FlowableService flowableService = (FlowableService)SpringContextUtil.getBean(FlowableService.class);
        ProcessInstance processInstance = flowableService.getProcessInstanceById(event.getProcessInstanceId());
        TaskEntity entity = (TaskEntity)event.getEntity();
        ProcessDefinition processDefinition = getProcessDefinition(event.getProcessDefinitionId());
        WorkrecordAddRequest workrecordAddRequest=new WorkrecordAddRequest();
        workrecordAddRequest.setUserid(userId);
        workrecordAddRequest.setBizId(processInstance.getBusinessKey());
        workrecordAddRequest.setUrl(entity.getFormKey());
        workrecordAddRequest.setPcUrl(entity.getFormKey());
        workrecordAddRequest.setSourceName("DING-FLOW");
        workrecordAddRequest.setPcOpenType(2L);
        workrecordAddRequest.setTitle(entity.getName());
        workrecordAddRequest.setCreateTime(entity.getCreateTime().getTime());
        List<WorkrecordAddRequest.FormItemVo> formItemList=Lists.newArrayList();
        WorkrecordAddRequest.FormItemVo formItemVo = new WorkrecordAddRequest.FormItemVo();
        formItemVo.setTitle("流程名称:");
        formItemVo.setContent(processDefinition.getName());
        formItemList.add(formItemVo);
        WorkrecordAddRequest.FormItemVo formItemVo1 = new WorkrecordAddRequest.FormItemVo();
        formItemVo1.setTitle("创建时间:");
        formItemVo1.setContent(DateUtil.format(entity.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        formItemList.add(formItemVo1);
        WorkrecordAddRequest.FormItemVo formItemVo2 = new WorkrecordAddRequest.FormItemVo();
        formItemVo2.setTitle("发起人:");
        formItemVo2.setContent(processInstance.getStartUserId());
        formItemList.add(formItemVo2);
        workrecordAddRequest.setFormItemList(formItemList);
        return workrecordAddRequest;
    }

    protected ProcessDefinition getProcessDefinition(String processDefinitionId) {
        RepositoryService repositoryService = (RepositoryService)SpringContextUtil.getBean(RepositoryService.class);
        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
    }


}
