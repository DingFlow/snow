package com.snow.flowable.common.skipTask;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qimingjin
 * @Title: 任务自动跳过服务
 * @Description: 任务自动跳过服务
 * @date 2021/1/11 10:24
 */
@Slf4j
@Service
public class TaskSkipService {
    @Resource
    private TaskService taskService;

    @Resource
    private TaskSkipRegistry taskSkipRegistry;

    @Transactional
    public void autoSkip(String processInstanceId) {
        log.info("自动推进节点:流程ID={}", processInstanceId);
        while (true) {
            List<Task> taskList = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId).active().list();
            if (taskList.isEmpty()) {
                break;
            }
            // 推进当前活动的task中可自动推进的task
            List<Task> skippedTask = new ArrayList<>();
            taskList.forEach(task -> {
                String procDefKey = task.getProcessDefinitionId().split(":")[0];
                AbstractSkipTask skipTask = taskSkipRegistry.getTask(procDefKey, task.getTaskDefinitionKey());
                if (skipTask != null && skipTask.execute(task)) {
                    skippedTask.add(task);
                }
            });
            if (skippedTask.isEmpty()) {
                // 已经没有需要自动跳过的节点
                break;
            }
        }
    }

}
