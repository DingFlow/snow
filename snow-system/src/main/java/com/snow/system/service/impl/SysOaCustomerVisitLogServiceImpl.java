package com.snow.system.service.impl;

import com.snow.common.core.text.Convert;
import com.snow.common.utils.DateUtils;
import com.snow.system.domain.SysOaCustomerVisitLog;
import com.snow.system.mapper.SysOaCustomerVisitLogMapper;
import com.snow.system.service.ISysOaCustomerVisitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户拜访日志Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-04-14
 */
@Service
public class SysOaCustomerVisitLogServiceImpl implements ISysOaCustomerVisitLogService 
{
    @Autowired
    private SysOaCustomerVisitLogMapper sysOaCustomerVisitLogMapper;


    /**
     * 查询客户拜访日志
     * 
     * @param id 客户拜访日志ID
     * @return 客户拜访日志
     */
    @Override
    public SysOaCustomerVisitLog selectSysOaCustomerVisitLogById(Long id)
    {
        return sysOaCustomerVisitLogMapper.selectSysOaCustomerVisitLogById(id);
    }

    /**
     * 查询客户拜访日志列表
     * 
     * @param sysOaCustomerVisitLog 客户拜访日志
     * @return 客户拜访日志
     */
    @Override
    public List<SysOaCustomerVisitLog> selectSysOaCustomerVisitLogList(SysOaCustomerVisitLog sysOaCustomerVisitLog)
    {
        return sysOaCustomerVisitLogMapper.selectSysOaCustomerVisitLogList(sysOaCustomerVisitLog);
    }

    /**
     * 新增客户拜访日志
     * 
     * @param sysOaCustomerVisitLog 客户拜访日志
     * @return 结果
     */
    @Override
    public int insertSysOaCustomerVisitLog(SysOaCustomerVisitLog sysOaCustomerVisitLog)
    {
        sysOaCustomerVisitLog.setCreateTime(DateUtils.getNowDate());

        sysOaCustomerVisitLog.setUpdateTime(DateUtils.getNowDate());

        return sysOaCustomerVisitLogMapper.insertSysOaCustomerVisitLog(sysOaCustomerVisitLog);
    }

    /**
     * 修改客户拜访日志
     * 
     * @param sysOaCustomerVisitLog 客户拜访日志
     * @return 结果
     */
    @Override
    public int updateSysOaCustomerVisitLog(SysOaCustomerVisitLog sysOaCustomerVisitLog)
    {
        sysOaCustomerVisitLog.setUpdateTime(DateUtils.getNowDate());
        return sysOaCustomerVisitLogMapper.updateSysOaCustomerVisitLog(sysOaCustomerVisitLog);
    }

    /**
     * 删除客户拜访日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysOaCustomerVisitLogByIds(String ids)
    {
        return sysOaCustomerVisitLogMapper.deleteSysOaCustomerVisitLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除客户拜访日志信息
     * 
     * @param id 客户拜访日志ID
     * @return 结果
     */
    @Override
    public int deleteSysOaCustomerVisitLogById(Long id)
    {
        return sysOaCustomerVisitLogMapper.deleteSysOaCustomerVisitLogById(id);
    }
}
