package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysOaTaskDistribute;

/**
 * 任务分配Service接口
 * 
 * @author 没用的阿吉
 * @date 2021-08-15
 */
public interface ISysOaTaskDistributeService 
{
    /**
     * 查询任务分配
     * 
     * @param id 任务分配ID
     * @return 任务分配
     */
    public SysOaTaskDistribute selectSysOaTaskDistributeById(Long id);

    /**
     * 查询任务分配列表
     * 
     * @param sysOaTaskDistribute 任务分配
     * @return 任务分配集合
     */
    public List<SysOaTaskDistribute> selectSysOaTaskDistributeList(SysOaTaskDistribute sysOaTaskDistribute);

    /**
     * 新增任务分配
     * 
     * @param sysOaTaskDistribute 任务分配
     * @return 结果
     */
    public int insertSysOaTaskDistribute(SysOaTaskDistribute sysOaTaskDistribute);

    /**
     * 修改任务分配
     * 
     * @param sysOaTaskDistribute 任务分配
     * @return 结果
     */
    public int updateSysOaTaskDistribute(SysOaTaskDistribute sysOaTaskDistribute);

    /**
     * 批量删除任务分配
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOaTaskDistributeByIds(String ids);

    /**
     * 删除任务分配信息
     * 
     * @param id 任务分配ID
     * @return 结果
     */
    public int deleteSysOaTaskDistributeById(Long id);
}
