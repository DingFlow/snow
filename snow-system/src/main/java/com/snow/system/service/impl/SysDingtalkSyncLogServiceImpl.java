package com.snow.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysDingtalkSyncLogMapper;
import com.snow.system.domain.SysDingtalkSyncLog;
import com.snow.system.service.ISysDingtalkSyncLogService;
import com.snow.common.core.text.Convert;

/**
 * 钉钉同步日志记录Service业务层处理
 * 
 * @author snow
 * @date 2020-11-13
 */
@Service
public class SysDingtalkSyncLogServiceImpl implements ISysDingtalkSyncLogService 
{
    @Autowired
    private SysDingtalkSyncLogMapper sysDingtalkSyncLogMapper;

    /**
     * 查询钉钉同步日志记录
     * 
     * @param logId 钉钉同步日志记录ID
     * @return 钉钉同步日志记录
     */
    @Override
    public SysDingtalkSyncLog selectSysDingtalkSyncLogById(Long logId)
    {
        return sysDingtalkSyncLogMapper.selectSysDingtalkSyncLogById(logId);
    }

    /**
     * 查询钉钉同步日志记录列表
     * 
     * @param sysDingtalkSyncLog 钉钉同步日志记录
     * @return 钉钉同步日志记录
     */
    @Override
    public List<SysDingtalkSyncLog> selectSysDingtalkSyncLogList(SysDingtalkSyncLog sysDingtalkSyncLog)
    {
        return sysDingtalkSyncLogMapper.selectSysDingtalkSyncLogList(sysDingtalkSyncLog);
    }

    /**
     * 新增钉钉同步日志记录
     * 
     * @param sysDingtalkSyncLog 钉钉同步日志记录
     * @return 结果
     */
    @Override
    public int insertSysDingtalkSyncLog(SysDingtalkSyncLog sysDingtalkSyncLog)
    {

        sysDingtalkSyncLog.setOperTime(new Date());
        return sysDingtalkSyncLogMapper.insertSysDingtalkSyncLog(sysDingtalkSyncLog);
    }

    /**
     * 修改钉钉同步日志记录
     * 
     * @param sysDingtalkSyncLog 钉钉同步日志记录
     * @return 结果
     */
    @Override
    public int updateSysDingtalkSyncLog(SysDingtalkSyncLog sysDingtalkSyncLog)
    {
        return sysDingtalkSyncLogMapper.updateSysDingtalkSyncLog(sysDingtalkSyncLog);
    }

    /**
     * 删除钉钉同步日志记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysDingtalkSyncLogByIds(String ids)
    {
        return sysDingtalkSyncLogMapper.deleteSysDingtalkSyncLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除钉钉同步日志记录信息
     * 
     * @param logId 钉钉同步日志记录ID
     * @return 结果
     */
    @Override
    public int deleteSysDingtalkSyncLogById(Long logId)
    {
        return sysDingtalkSyncLogMapper.deleteSysDingtalkSyncLogById(logId);
    }
}
