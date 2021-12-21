package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysOaTask;

/**
 * 系统任务Service接口
 * 
 * @author 没用的阿吉
 * @date 2021-07-29
 */
public interface ISysOaTaskService 
{
    /**
     * 查询系统任务
     * 
     * @param taskNo 系统任务ID
     * @return 系统任务
     */
    public SysOaTask selectSysOaTaskById(String taskNo);

    /**
     * 查询系统任务列表
     * 
     * @param sysOaTask 系统任务
     * @return 系统任务集合
     */
    public List<SysOaTask> selectSysOaTaskList(SysOaTask sysOaTask);

    /**
     * 新增系统任务
     * 
     * @param sysOaTask 系统任务
     * @return 结果
     */
    public int insertSysOaTask(SysOaTask sysOaTask);

    /**
     * 修改系统任务
     * 
     * @param sysOaTask 系统任务
     * @return 结果
     */
    public int updateSysOaTask(SysOaTask sysOaTask);

    /**
     * 批量删除系统任务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOaTaskByIds(String ids);

    /**
     * 删除系统任务信息
     * 
     * @param taskNo 系统任务ID
     * @return 结果
     */
    public int deleteSysOaTaskById(String taskNo);
}
