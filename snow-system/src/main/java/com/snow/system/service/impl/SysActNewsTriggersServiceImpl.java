package com.snow.system.service.impl;

import java.util.List;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysActNewsTriggersMapper;
import com.snow.system.domain.SysActNewsTriggers;
import com.snow.system.service.ISysActNewsTriggersService;
import com.snow.common.core.text.Convert;

/**
 * 流程消息配置Service业务层处理
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
@Service
public class SysActNewsTriggersServiceImpl implements ISysActNewsTriggersService 
{
    @Autowired
    private SysActNewsTriggersMapper sysActNewsTriggersMapper;

    /**
     * 查询流程消息配置
     * 
     * @param id 流程消息配置ID
     * @return 流程消息配置
     */
    @Override
    public SysActNewsTriggers selectSysActNewsTriggersById(Long id)
    {
        return sysActNewsTriggersMapper.selectSysActNewsTriggersById(id);
    }

    /**
     * 查询流程消息配置列表
     * 
     * @param sysActNewsTriggers 流程消息配置
     * @return 流程消息配置
     */
    @Override
    public List<SysActNewsTriggers> selectSysActNewsTriggersList(SysActNewsTriggers sysActNewsTriggers)
    {
        return sysActNewsTriggersMapper.selectSysActNewsTriggersList(sysActNewsTriggers);
    }

    /**
     * 新增流程消息配置
     * 
     * @param sysActNewsTriggers 流程消息配置
     * @return 结果
     */
    @Override
    public int insertSysActNewsTriggers(SysActNewsTriggers sysActNewsTriggers)
    {
        sysActNewsTriggers.setCreateTime(DateUtils.getNowDate());
        return sysActNewsTriggersMapper.insertSysActNewsTriggers(sysActNewsTriggers);
    }

    /**
     * 修改流程消息配置
     * 
     * @param sysActNewsTriggers 流程消息配置
     * @return 结果
     */
    @Override
    public int updateSysActNewsTriggers(SysActNewsTriggers sysActNewsTriggers)
    {
        sysActNewsTriggers.setUpdateTime(DateUtils.getNowDate());
        return sysActNewsTriggersMapper.updateSysActNewsTriggers(sysActNewsTriggers);
    }

    /**
     * 删除流程消息配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysActNewsTriggersByIds(String ids)
    {
        return sysActNewsTriggersMapper.deleteSysActNewsTriggersByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除流程消息配置信息
     * 
     * @param id 流程消息配置ID
     * @return 结果
     */
    @Override
    public int deleteSysActNewsTriggersById(Long id)
    {
        return sysActNewsTriggersMapper.deleteSysActNewsTriggersById(id);
    }
}
