package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysDingHiTask;

/**
 * 历史任务Service接口
 * 
 * @author 没用的阿吉
 * @date 2021-03-24
 */
public interface ISysDingHiTaskService 
{
    /**
     * 查询历史任务
     * 
     * @param id 历史任务ID
     * @return 历史任务
     */
    public SysDingHiTask selectSysDingHiTaskById(String id);

    /**
     * 查询历史任务列表
     * 
     * @param sysDingHiTask 历史任务
     * @return 历史任务集合
     */
    public List<SysDingHiTask> selectSysDingHiTaskList(SysDingHiTask sysDingHiTask);

    /**
     * 新增历史任务
     * 
     * @param sysDingHiTask 历史任务
     * @return 结果
     */
    public int insertSysDingHiTask(SysDingHiTask sysDingHiTask);

    /**
     * 修改历史任务
     * 
     * @param sysDingHiTask 历史任务
     * @return 结果
     */
    public int updateSysDingHiTask(SysDingHiTask sysDingHiTask);

    /**
     * 批量删除历史任务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDingHiTaskByIds(String ids);

    /**
     * 删除历史任务信息
     * 
     * @param id 历史任务ID
     * @return 结果
     */
    public int deleteSysDingHiTaskById(String id);
}
