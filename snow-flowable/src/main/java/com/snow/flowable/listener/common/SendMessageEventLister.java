package com.snow.flowable.listener.common;

import com.alibaba.fastjson.JSON;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.listener.AbstractEventListener;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/12/8 14:38
 */
@Slf4j
@Service
public class SendMessageEventLister extends AbstractEventListener {

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
    protected void process() {

    }

    @Override
    protected void taskCreated(FlowableEngineEntityEvent event) {
        TaskEntity entity = (TaskEntity)event.getEntity();
        ProcessDefinitionEntity processDefinition = getProcessDefinition(event);
        //根据任务ID获取任务获选人人
        Set<IdentityLink> candidates = entity.getCandidates();
        if(!CollectionUtils.isEmpty(candidates)){
            candidates.forEach(t->{
                String userId = t.getUserId();
                String groupId = t.getGroupId();
                if(!StringUtils.isEmpty(userId)){
                    sendMessage();
                } else if(!StringUtils.isEmpty(groupId)) {
                    sendMessage();
                }
                });
        }
        //任务创建可发送短信，邮件通知接收人
        log.info("ManagerTaskEventListener----taskCreated任务创建监听：{}",JSON.toJSONString(event));
    }

    public void sendMessage(){

    }

}
