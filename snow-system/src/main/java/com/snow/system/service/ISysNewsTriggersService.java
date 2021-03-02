package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysNewsTriggers;

/**
 * 消息通知配置Service接口
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
public interface ISysNewsTriggersService 
{
    /**
     * 查询消息通知配置
     * 
     * @param id 消息通知配置ID
     * @return 消息通知配置
     */
    public SysNewsTriggers selectSysNewsTriggersById(Integer id);

    /**
     * 查询消息通知配置列表
     * 
     * @param sysNewsTriggers 消息通知配置
     * @return 消息通知配置集合
     */
    public List<SysNewsTriggers> selectSysNewsTriggersList(SysNewsTriggers sysNewsTriggers);

    /**
     * 新增消息通知配置
     * 
     * @param sysNewsTriggers 消息通知配置
     * @return 结果
     */
    public int insertSysNewsTriggers(SysNewsTriggers sysNewsTriggers);

    /**
     * 修改消息通知配置
     * 
     * @param sysNewsTriggers 消息通知配置
     * @return 结果
     */
    public int updateSysNewsTriggers(SysNewsTriggers sysNewsTriggers);

    /**
     * 批量删除消息通知配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysNewsTriggersByIds(String ids);

    /**
     * 删除消息通知配置信息
     * 
     * @param id 消息通知配置ID
     * @return 结果
     */
    public int deleteSysNewsTriggersById(Integer id);
}
