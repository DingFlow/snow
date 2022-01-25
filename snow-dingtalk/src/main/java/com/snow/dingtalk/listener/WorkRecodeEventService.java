package com.snow.dingtalk.listener;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.dingtalktodo_1_0.models.GetTodoTaskBySourceIdResponseBody;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.model.request.WorkrecordAddRequest;
import com.snow.dingtalk.service.impl.MessageServiceImpl;
import com.snow.dingtalk.service.impl.WorkRecodeServiceImpl;
import com.snow.framework.web.domain.common.SysSendMessageRequest;
import com.snow.system.domain.SysOaTask;
import com.snow.system.domain.SysOaTaskDistribute;
import com.snow.system.event.SyncEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/12/9 15:03
 */
@Component
@Slf4j
public class WorkRecodeEventService implements ISyncDingTalkInfo {

    private WorkRecodeServiceImpl workRecodeService=SpringUtils.getBean(WorkRecodeServiceImpl.class);

    private MessageServiceImpl messageService=SpringUtils.getBean(MessageServiceImpl.class);

    @Override
    public void syncDingTalkInfoEvent(SyncEvent syncEvent) {
        log.info("调用工作通知传入的原始参数:{}",JSON.toJSONString(syncEvent));
        DingTalkListenerType eventType =(DingTalkListenerType) syncEvent.getT();
        Integer code = eventType.getCode();
        //钉钉创建待办
        if(code.equals(DingTalkListenerType.WORK_RECODE_CREATE.getCode())){
            SysOaTask sysOaTask=(SysOaTask)syncEvent.getSource();
            log.info("@@创建钉钉待办系统传入的参数：{}",JSON.toJSONString(sysOaTask));
            workRecodeService.createTodoTask(sysOaTask);
        }
        //更新钉钉待办
        if(code.equals(DingTalkListenerType.WORK_RECORD_UPDATE.getCode())){
            SysOaTask sysOaTask=(SysOaTask)syncEvent.getSource();
            log.info("@@更新钉钉待办系统传入的参数：{}",JSON.toJSONString(sysOaTask));
            workRecodeService.updateTodoTask(sysOaTask);
        }
        //删除钉钉待办
        if(code.equals(DingTalkListenerType.WORK_RECORD_DELETE.getCode())){
            String taskId=(String)syncEvent.getSource();
            log.info("@@删除钉钉待办系统传入的参数：{}",JSON.toJSONString(taskId));
            workRecodeService.deleteTodoTask(taskId);
        }
        //更新任务状态
        if(code.equals(DingTalkListenerType.UPDATE_TODO_TASK_EXECUTOR_STATUS.getCode())){
            SysOaTaskDistribute sysOaTaskDistribute=(SysOaTaskDistribute)syncEvent.getSource();
            GetTodoTaskBySourceIdResponseBody body = workRecodeService.getTodoTaskByBusinessId(String.valueOf(sysOaTaskDistribute.getId()));
            boolean isComplete=false;
            if(ObjectUtil.isNotNull(sysOaTaskDistribute.getTaskCompleteTime())){
                isComplete=true;
            }
            workRecodeService.updateTodoTaskExecutorStatus(body.getId(),isComplete);
        }
        //钉钉发送普通消息
        if(code.equals(DingTalkListenerType.ASYNCSEND_V2.getCode())){
            SysSendMessageRequest sysSendMessageDTO=(SysSendMessageRequest)syncEvent.getSource();
            log.info("@@发送钉钉工作通知消息传入的参数：{}",JSON.toJSONString(sysSendMessageDTO));
            messageService.sendWorkNotice(sysSendMessageDTO);
        }

        if(code.equals(DingTalkListenerType.WORK_RECODE_OLD_CREATE.getCode())){
            WorkrecordAddRequest workrecordAddRequest=(WorkrecordAddRequest)syncEvent.getSource();
            log.info("@@创建钉钉待办(old)系统传入的参数：{}",JSON.toJSONString(workrecordAddRequest));
            workRecodeService.create(workrecordAddRequest);
        }
    }
}
