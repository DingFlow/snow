package com.snow.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.snow.common.utils.DateUtils;
import com.snow.common.utils.StringUtils;
import com.snow.system.domain.SysNewsNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysNewsTriggersMapper;
import com.snow.system.domain.SysNewsTriggers;
import com.snow.system.service.ISysNewsTriggersService;
import com.snow.common.core.text.Convert;

/**
 * 消息通知配置Service业务层处理
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
@Service
public class SysNewsTriggersServiceImpl implements ISysNewsTriggersService 
{
    @Autowired
    private SysNewsTriggersMapper sysNewsTriggersMapper;

    @Autowired
    private SysNewsNodeServiceImpl sysNewsNodeService;

    /**
     * 查询消息通知配置
     * 
     * @param id 消息通知配置ID
     * @return 消息通知配置
     */
    @Override
    public SysNewsTriggers selectSysNewsTriggersById(Integer id)
    {
        return sysNewsTriggersMapper.selectSysNewsTriggersById(id);
    }

    /**
     * 查询消息通知配置列表
     * 
     * @param sysNewsTriggers 消息通知配置
     * @return 消息通知配置
     */
    @Override
    public List<SysNewsTriggers> selectSysNewsTriggersList(SysNewsTriggers sysNewsTriggers)
    {
        List<SysNewsTriggers> sysNewsTriggersList=new ArrayList<>();
        if(StringUtils.isNotNull(sysNewsTriggers.getNewsNodeId())){
            SysNewsNode sysNewsNode = sysNewsNodeService.selectSysNewsNodeById(sysNewsTriggers.getNewsNodeId().intValue());
            sysNewsTriggersList = sysNewsTriggersMapper.selectSysNewsTriggersList(sysNewsTriggers);
            sysNewsTriggersList.parallelStream().forEach(t->{
                t.setSysNewsNode(sysNewsNode);
            });
        }

        return sysNewsTriggersList;
    }

    /**
     * 新增消息通知配置
     * 
     * @param sysNewsTriggers 消息通知配置
     * @return 结果
     */
    @Override
    public int insertSysNewsTriggers(SysNewsTriggers sysNewsTriggers)
    {
        sysNewsTriggers.setCreateTime(DateUtils.getNowDate());
        return sysNewsTriggersMapper.insertSysNewsTriggers(sysNewsTriggers);
    }

    /**
     * 修改消息通知配置
     * 
     * @param sysNewsTriggers 消息通知配置
     * @return 结果
     */
    @Override
    public int updateSysNewsTriggers(SysNewsTriggers sysNewsTriggers)
    {
        sysNewsTriggers.setUpdateTime(DateUtils.getNowDate());
        return sysNewsTriggersMapper.updateSysNewsTriggers(sysNewsTriggers);
    }

    /**
     * 删除消息通知配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysNewsTriggersByIds(String ids)
    {
        return sysNewsTriggersMapper.deleteSysNewsTriggersByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除消息通知配置信息
     * 
     * @param id 消息通知配置ID
     * @return 结果
     */
    @Override
    public int deleteSysNewsTriggersById(Integer id)
    {
        return sysNewsTriggersMapper.deleteSysNewsTriggersById(id);
    }
}
