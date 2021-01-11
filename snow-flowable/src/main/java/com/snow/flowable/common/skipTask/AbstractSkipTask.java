package com.snow.flowable.common.skipTask;

import com.snow.flowable.domain.CompleteTaskDTO;
import org.flowable.task.api.Task;
import com.snow.flowable.service.FlowableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qimingjin
 * @Title: 节点跳过处理，在节点create事件处理
 * @Description:
 * @date 2021/1/11 9:57
 */
@Service
@Slf4j
public abstract class AbstractSkipTask {

    @Autowired
    private FlowableService flowableService;


    public boolean execute(Task task) {
        log.info("@@检查节点是否能自动处理。节点={}, taskId={}", task.getName(), task.getId());
        if (canSkip(task)) {
            log.info("自动跳过节点");
            doSkip(task);
            return true;
        }
        log.info("不能自动跳过");
        return false;
    }

    public abstract boolean canSkip(Task task);


    private void doSkip(Task task) {
        log.info("执行自动跳过节点");
        CompleteTaskDTO taskParameter = getTaskParameter(task);
        flowableService.completeTask(taskParameter);
    }

    protected CompleteTaskDTO getTaskParameter(Task task){
        CompleteTaskDTO completeTaskDTO=new CompleteTaskDTO();
        completeTaskDTO.setTaskId(task.getId());
        completeTaskDTO.setIsPass(true);
        completeTaskDTO.setComment("系统自动完成");
        completeTaskDTO.setUserId("1");
        return completeTaskDTO;
    }

    protected abstract String getProcessDefinitionKey();

    protected abstract List<String> getTaskKeys();
}
