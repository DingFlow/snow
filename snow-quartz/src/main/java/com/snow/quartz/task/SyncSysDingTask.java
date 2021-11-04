package com.snow.quartz.task;

import cn.hutool.core.collection.CollUtil;
import com.snow.common.enums.DingFlowTaskType;
import com.snow.common.enums.TaskStatus;
import com.snow.system.domain.SysOaTask;
import com.snow.system.domain.SysOaTaskDistribute;
import com.snow.system.service.ISysOaTaskDistributeService;
import com.snow.system.service.ISysOaTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qimingjin
 * @Title:  定时刷新任务状态
 * @Description:
 * @date 2021/11/4 15:46
 */
@Component("syncSysDingTask")
@Slf4j
public class SyncSysDingTask {

    @Autowired
    private ISysOaTaskService sysOaTaskService;

    @Autowired
    private ISysOaTaskDistributeService sysOaTaskDistributeService;

    public void refreshSysTaskStatus(){
        SysOaTask sysOaTask=new SysOaTask();
        sysOaTask.setTaskStatus(TaskStatus.UN_FINISH.getCode());
        List<SysOaTask> sysOaTasks = sysOaTaskService.selectSysOaTaskList(sysOaTask);
        if(CollUtil.isEmpty(sysOaTasks)){
          return;
        }
        sysOaTasks.forEach(t->{
            SysOaTaskDistribute sysOaTaskDistribute=new SysOaTaskDistribute();
            sysOaTaskDistribute.setTaskNo(t.getTaskNo());
            List<SysOaTaskDistribute> sysOaTaskDistributes = sysOaTaskDistributeService.selectSysOaTaskDistributeList(sysOaTaskDistribute);
            if(CollUtil.isEmpty(sysOaTaskDistributes)){
                return;
            }
            //查询完成的条数
            long count = sysOaTaskDistributes.stream().filter(s -> s.getTaskExecuteStatus().equals(DingFlowTaskType.COMPLETED.getCode())).count();
            if(sysOaTaskDistributes.size()==count){
                SysOaTask upSysOaTask=new SysOaTask();
                upSysOaTask.setTaskNo(t.getTaskNo());
                upSysOaTask.setTaskStatus(TaskStatus.FINISH.getCode());
                sysOaTaskService.updateSysOaTask(upSysOaTask);
            }
        });
    }
}
