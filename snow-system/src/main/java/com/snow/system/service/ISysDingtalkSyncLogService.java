package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysDingtalkSyncLog;
import com.snow.system.domain.SysDingtalkSyncSituationVO;

/**
 * 钉钉同步日志记录Service接口
 * 
 * @author snow
 * @date 2020-11-13
 */
public interface ISysDingtalkSyncLogService 
{
    /**
     * 查询钉钉同步日志记录
     * 
     * @param logId 钉钉同步日志记录ID
     * @return 钉钉同步日志记录
     */
    public SysDingtalkSyncLog selectSysDingtalkSyncLogById(Long logId);

    /**
     * 查询钉钉同步日志记录列表
     * 
     * @param sysDingtalkSyncLog 钉钉同步日志记录
     * @return 钉钉同步日志记录集合
     */
    public List<SysDingtalkSyncLog> selectSysDingtalkSyncLogList(SysDingtalkSyncLog sysDingtalkSyncLog);

    /**
     * 新增钉钉同步日志记录
     * 
     * @param sysDingtalkSyncLog 钉钉同步日志记录
     * @return 结果
     */
    public int insertSysDingtalkSyncLog(SysDingtalkSyncLog sysDingtalkSyncLog);

    /**
     * 修改钉钉同步日志记录
     * 
     * @param sysDingtalkSyncLog 钉钉同步日志记录
     * @return 结果
     */
    public int updateSysDingtalkSyncLog(SysDingtalkSyncLog sysDingtalkSyncLog);

    /**
     * 批量删除钉钉同步日志记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDingtalkSyncLogByIds(String ids);

    /**
     * 删除钉钉同步日志记录信息
     * 
     * @param logId 钉钉同步日志记录ID
     * @return 结果
     */
    public int deleteSysDingtalkSyncLogById(Long logId);

    /**
     * 获取钉钉同步数据概况
     */
    public SysDingtalkSyncSituationVO getSysDingtalkSyncSituation(SysDingtalkSyncLog sysDingtalkSyncLog);
}
