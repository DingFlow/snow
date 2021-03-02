package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysActNewsTriggers;

/**
 * 流程消息配置Service接口
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
public interface ISysActNewsTriggersService 
{
    /**
     * 查询流程消息配置
     * 
     * @param id 流程消息配置ID
     * @return 流程消息配置
     */
    public SysActNewsTriggers selectSysActNewsTriggersById(Long id);

    /**
     * 查询流程消息配置列表
     * 
     * @param sysActNewsTriggers 流程消息配置
     * @return 流程消息配置集合
     */
    public List<SysActNewsTriggers> selectSysActNewsTriggersList(SysActNewsTriggers sysActNewsTriggers);

    /**
     * 新增流程消息配置
     * 
     * @param sysActNewsTriggers 流程消息配置
     * @return 结果
     */
    public int insertSysActNewsTriggers(SysActNewsTriggers sysActNewsTriggers);

    /**
     * 修改流程消息配置
     * 
     * @param sysActNewsTriggers 流程消息配置
     * @return 结果
     */
    public int updateSysActNewsTriggers(SysActNewsTriggers sysActNewsTriggers);

    /**
     * 批量删除流程消息配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysActNewsTriggersByIds(String ids);

    /**
     * 删除流程消息配置信息
     * 
     * @param id 流程消息配置ID
     * @return 结果
     */
    public int deleteSysActNewsTriggersById(Long id);
}
