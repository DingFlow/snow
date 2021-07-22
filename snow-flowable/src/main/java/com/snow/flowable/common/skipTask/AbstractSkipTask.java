package com.snow.flowable.common.skipTask;

import com.snow.flowable.domain.FinishTaskDTO;
import com.snow.flowable.service.FlowableTaskService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.api.Task;
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
    private FlowableTaskService flowableTaskService;


    public boolean execute(Task task) {
        log.info("@@检查节点是否能自动处理。节点={}, taskId={}", task.getName(), task.getId());
        if (canSkip(task)) {
            doSkip(task);
            return true;
        }
        log.info("不能自动跳过");
        return false;
    }

    public abstract boolean canSkip(Task task);


    private void doSkip(Task task) {
        log.info("@@执行自动跳过节点");
        FinishTaskDTO taskParameter = getTaskParameter(task);
        flowableTaskService.submitTask(taskParameter);
    }

    /**
     * 自动完成任务时，需要传入的参数
     * @param task 任务对象
     * @return 封装的提交完成任务参数
     */
    protected FinishTaskDTO getTaskParameter(Task task){
        FinishTaskDTO completeTaskDTO=new FinishTaskDTO();
        completeTaskDTO.setTaskId(task.getId());
        completeTaskDTO.setIsPass(true);
        completeTaskDTO.setComment("系统自动完成");
        completeTaskDTO.setUserId("1");
        return completeTaskDTO;
    }

    protected abstract String getProcessDefinitionKey();

    /**
     * 需要跳过的任务节点
     * @return 返回跳过的任务节点list
     */
    protected abstract List<String> getTaskKeys();
}
