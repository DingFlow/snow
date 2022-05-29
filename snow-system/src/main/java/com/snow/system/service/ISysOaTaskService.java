package com.snow.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.system.domain.SysOaTask;

/**
 * 系统任务Service接口
 * 
 * @author 没用的阿吉
 * @date 2021-07-29
 */
public interface ISysOaTaskService extends IService<SysOaTask> {
    /**
     * 查询系统任务
     * 
     * @param taskNo 系统任务ID
     * @return 系统任务
     */
     SysOaTask selectSysOaTaskById(String taskNo);

    /**
     * 查询系统任务列表
     * 
     * @param sysOaTask 系统任务
     * @return 系统任务集合
     */
     List<SysOaTask> selectSysOaTaskList(SysOaTask sysOaTask);

    /**
     * 新增系统任务
     * 
     * @param sysOaTask 系统任务
     * @return 结果
     */
     SysOaTask insertSysOaTask(SysOaTask sysOaTask);


    /**
     * 根据任务编号获取三方id
     * @param taskNo 任务id
     * @return
     */
    String getOutTaskOutsideId(String taskNo);
}
