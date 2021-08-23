package com.snow.system.service.impl;

import java.util.List;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysOaTaskDistributeMapper;
import com.snow.system.domain.SysOaTaskDistribute;
import com.snow.system.service.ISysOaTaskDistributeService;
import com.snow.common.core.text.Convert;

/**
 * 任务分配Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-08-15
 */
@Service
public class SysOaTaskDistributeServiceImpl implements ISysOaTaskDistributeService 
{
    @Autowired
    private SysOaTaskDistributeMapper sysOaTaskDistributeMapper;

    /**
     * 查询任务分配
     * 
     * @param id 任务分配ID
     * @return 任务分配
     */
    @Override
    public SysOaTaskDistribute selectSysOaTaskDistributeById(Long id)
    {
        return sysOaTaskDistributeMapper.selectSysOaTaskDistributeById(id);
    }

    /**
     * 查询任务分配列表
     * 
     * @param sysOaTaskDistribute 任务分配
     * @return 任务分配
     */
    @Override
    public List<SysOaTaskDistribute> selectSysOaTaskDistributeList(SysOaTaskDistribute sysOaTaskDistribute)
    {
        return sysOaTaskDistributeMapper.selectSysOaTaskDistributeList(sysOaTaskDistribute);
    }

    /**
     * 新增任务分配
     * 
     * @param sysOaTaskDistribute 任务分配
     * @return 结果
     */
    @Override
    public int insertSysOaTaskDistribute(SysOaTaskDistribute sysOaTaskDistribute)
    {
        sysOaTaskDistribute.setCreateTime(DateUtils.getNowDate());
        return sysOaTaskDistributeMapper.insertSysOaTaskDistribute(sysOaTaskDistribute);
    }

    /**
     * 修改任务分配
     * 
     * @param sysOaTaskDistribute 任务分配
     * @return 结果
     */
    @Override
    public int updateSysOaTaskDistribute(SysOaTaskDistribute sysOaTaskDistribute)
    {
        sysOaTaskDistribute.setUpdateTime(DateUtils.getNowDate());
        return sysOaTaskDistributeMapper.updateSysOaTaskDistribute(sysOaTaskDistribute);
    }

    /**
     * 删除任务分配对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysOaTaskDistributeByIds(String ids)
    {
        return sysOaTaskDistributeMapper.deleteSysOaTaskDistributeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除任务分配信息
     * 
     * @param id 任务分配ID
     * @return 结果
     */
    @Override
    public int deleteSysOaTaskDistributeById(Long id)
    {
        return sysOaTaskDistributeMapper.deleteSysOaTaskDistributeById(id);
    }
}
