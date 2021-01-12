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
import com.snow.flowable.service.impl.FlowableUserServiceImpl;
import com.snow.system.domain.SysUser;
import com.snow.system.event.SyncEvent;
import com.snow.system.mapper.SysUserMapper;
import com.snow.system.service.impl.SysUserServiceImpl;
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
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author qimingjin
 * @Title: 发送消息
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
                        FlowDefEnum.SNOW_OA_LEAVE,
                        FlowDefEnum.PURCHASE_ORDER_PROCESS
                )));
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
        FlowableUserServiceImpl flowableUserService = (FlowableUserServiceImpl)SpringContextUtil.getBean(FlowableUserServiceImpl.class);
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
                            List<SysUser> sysUsers = flowableUserService.getUserByFlowGroupId(Long.parseLong(groupId));
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
     * @param userId  需要发送通知的人
     * @param event
     * @return
     */
    public WorkrecordAddRequest initWorkRecordAddRequest(String userId,FlowableEngineEntityEvent event){
        FlowableService flowableService = (FlowableService)SpringContextUtil.getBean(FlowableService.class);
        ProcessInstance processInstance = flowableService.getProcessInstanceById(event.getProcessInstanceId());
        TaskEntity entity = (TaskEntity)event.getEntity();
        ProcessDefinition processDefinition = getProcessDefinition(event.getProcessDefinitionId());
        WorkrecordAddRequest workrecordAddRequest=new WorkrecordAddRequest();
        SysUser userInfo = getUserInfo(userId);
        workrecordAddRequest.setUserid(userInfo.getDingUserId());
       // workrecordAddRequest.setBizId(processInstance.getBusinessKey());
        workrecordAddRequest.setUrl(entity.getFormKey());
        workrecordAddRequest.setPcUrl(entity.getFormKey());
        workrecordAddRequest.setSourceName("DING-FLOW");
        workrecordAddRequest.setPcOpenType(2L);
        workrecordAddRequest.setTitle(processDefinition.getName());
        workrecordAddRequest.setCreateTime(entity.getCreateTime().getTime());
        List<WorkrecordAddRequest.FormItemVo> formItemList=Lists.newArrayList();
        WorkrecordAddRequest.FormItemVo formItemVo = new WorkrecordAddRequest.FormItemVo();
        formItemVo.setTitle("单号");
        formItemVo.setContent(processInstance.getBusinessKey());
        formItemList.add(formItemVo);
        WorkrecordAddRequest.FormItemVo formItemVo3 = new WorkrecordAddRequest.FormItemVo();
        formItemVo3.setTitle("审批节点");
        formItemVo3.setContent(entity.getName());
        formItemList.add(formItemVo3);
        WorkrecordAddRequest.FormItemVo formItemVo1 = new WorkrecordAddRequest.FormItemVo();
        formItemVo1.setTitle("创建时间");
        formItemVo1.setContent(DateUtil.format(entity.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        formItemList.add(formItemVo1);
        WorkrecordAddRequest.FormItemVo formItemVo2 = new WorkrecordAddRequest.FormItemVo();
        formItemVo2.setTitle("发起人");
        formItemVo2.setContent(getUserInfo(processInstance.getStartUserId()).getUserName());
        formItemList.add(formItemVo2);
        workrecordAddRequest.setFormItemList(formItemList);
        return workrecordAddRequest;
    }

    /**
     * 获取流程定义信息
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
     * 获取用户信息
     * @param userId
     * @return
     */
    protected SysUser getUserInfo(String userId) {
        SysUserServiceImpl sysUserService = (SysUserServiceImpl)SpringContextUtil.getBean(SysUserServiceImpl.class);
        return Optional.ofNullable(sysUserService.selectUserById(Long.parseLong(userId))).orElse(
                new SysUser(1L,"System","manager4480")
        );
    }


}
