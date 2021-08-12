package com.snow.system.service.impl;

import java.util.List;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysOaTaskMapper;
import com.snow.system.domain.SysOaTask;
import com.snow.system.service.ISysOaTaskService;
import com.snow.common.core.text.Convert;

/**
 * 系统任务Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-07-29
 */
@Service
public class SysOaTaskServiceImpl implements ISysOaTaskService 
{
    @Autowired
    private SysOaTaskMapper sysOaTaskMapper;

    /**
     * 查询系统任务
     * 
     * @param taskNo 系统任务ID
     * @return 系统任务
     */
    @Override
    public SysOaTask selectSysOaTaskById(String taskNo)
    {
        return sysOaTaskMapper.selectSysOaTaskById(taskNo);
    }

    /**
     * 查询系统任务列表
     * 
     * @param sysOaTask 系统任务
     * @return 系统任务
     */
    @Override
    public List<SysOaTask> selectSysOaTaskList(SysOaTask sysOaTask)
    {
        return sysOaTaskMapper.selectSysOaTaskList(sysOaTask);
    }

    /**
     * 新增系统任务
     * 
     * @param sysOaTask 系统任务
     * @return 结果
     */
    @Override
    public int insertSysOaTask(SysOaTask sysOaTask)
    {
        sysOaTask.setCreateTime(DateUtils.getNowDate());
        return sysOaTaskMapper.insertSysOaTask(sysOaTask);
    }

    /**
     * 修改系统任务
     * 
     * @param sysOaTask 系统任务
     * @return 结果
     */
    @Override
    public int updateSysOaTask(SysOaTask sysOaTask)
    {
        sysOaTask.setUpdateTime(DateUtils.getNowDate());
        return sysOaTaskMapper.updateSysOaTask(sysOaTask);
    }

    /**
     * 删除系统任务对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysOaTaskByIds(String ids)
    {
        return sysOaTaskMapper.deleteSysOaTaskByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除系统任务信息
     * 
     * @param taskNo 系统任务ID
     * @return 结果
     */
    @Override
    public int deleteSysOaTaskById(String taskNo)
    {
        return sysOaTaskMapper.deleteSysOaTaskById(taskNo);
    }
}
