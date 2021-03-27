package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysDingRuTask;

/**
 * 钉钉运行任务Service接口
 * 
 * @author 没用的阿吉
 * @date 2021-03-24
 */
public interface ISysDingRuTaskService 
{
    /**
     * 查询钉钉运行任务
     * 
     * @param id 钉钉运行任务ID
     * @return 钉钉运行任务
     */
    public SysDingRuTask selectSysDingRuTaskById(String id);

    /**
     * 查询钉钉运行任务列表
     * 
     * @param sysDingRuTask 钉钉运行任务
     * @return 钉钉运行任务集合
     */
    public List<SysDingRuTask> selectSysDingRuTaskList(SysDingRuTask sysDingRuTask);

    /**
     * 新增钉钉运行任务
     * 
     * @param sysDingRuTask 钉钉运行任务
     * @return 结果
     */
    public int insertSysDingRuTask(SysDingRuTask sysDingRuTask);

    /**
     * 修改钉钉运行任务
     * 
     * @param sysDingRuTask 钉钉运行任务
     * @return 结果
     */
    public int updateSysDingRuTask(SysDingRuTask sysDingRuTask);

    /**
     * 批量删除钉钉运行任务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDingRuTaskByIds(String ids);

    /**
     * 删除钉钉运行任务信息
     * 
     * @param id 钉钉运行任务ID
     * @return 结果
     */
    public int deleteSysDingRuTaskById(String id);
}
