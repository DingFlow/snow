package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.SysOaCustomerVisitLog;

/**
 * 客户拜访日志Mapper接口
 * 
 * @author 没用的阿吉
 * @date 2021-04-14
 */
public interface SysOaCustomerVisitLogMapper 
{
    /**
     * 查询客户拜访日志
     * 
     * @param id 客户拜访日志ID
     * @return 客户拜访日志
     */
    public SysOaCustomerVisitLog selectSysOaCustomerVisitLogById(Long id);

    /**
     * 查询客户拜访日志列表
     * 
     * @param sysOaCustomerVisitLog 客户拜访日志
     * @return 客户拜访日志集合
     */
    public List<SysOaCustomerVisitLog> selectSysOaCustomerVisitLogList(SysOaCustomerVisitLog sysOaCustomerVisitLog);

    /**
     * 新增客户拜访日志
     * 
     * @param sysOaCustomerVisitLog 客户拜访日志
     * @return 结果
     */
    public int insertSysOaCustomerVisitLog(SysOaCustomerVisitLog sysOaCustomerVisitLog);

    /**
     * 修改客户拜访日志
     * 
     * @param sysOaCustomerVisitLog 客户拜访日志
     * @return 结果
     */
    public int updateSysOaCustomerVisitLog(SysOaCustomerVisitLog sysOaCustomerVisitLog);

    /**
     * 删除客户拜访日志
     * 
     * @param id 客户拜访日志ID
     * @return 结果
     */
    public int deleteSysOaCustomerVisitLogById(Long id);

    /**
     * 批量删除客户拜访日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOaCustomerVisitLogByIds(String[] ids);
}
