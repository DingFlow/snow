package com.snow.flowable.common.skipTask;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/11 10:11
 */
@Service
@Slf4j
public class TaskSkipRegistry {

    private Table<String, String, AbstractSkipTask> table = HashBasedTable.create();

    private final ApplicationContext applicationContext;

    @Autowired
    public TaskSkipRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public AbstractSkipTask getTask(String processDefKey, String taskDefKey) {
        return table.get(processDefKey, taskDefKey);
    }
    
    @PostConstruct
    public void init(){
        Map<String,AbstractSkipTask> tasks = applicationContext.getBeansOfType(AbstractSkipTask.class);
        tasks.forEach((k,skipTask)->skipTask.getTaskKeys().forEach(tkey ->{
            AbstractSkipTask prev= table.put(skipTask.getProcessDefinitionKey(), tkey, skipTask);
            if (prev != null) {
                throw new RuntimeException("重复注册");
            }
        }));
    }
}
