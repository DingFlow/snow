package com.snow.from.service.impl;

import java.util.List;
import com.snow.common.utils.DateUtils;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.mapper.SysFormInstanceMapper;
import com.snow.from.service.ISysFormInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.common.core.text.Convert;

/**
 * 单实例Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-03-21
 */
@Service
public class SysFormInstanceServiceImpl implements ISysFormInstanceService
{
    @Autowired
    private SysFormInstanceMapper sysFormInstanceMapper;

    /**
     * 查询单实例
     * 
     * @param id 单实例ID
     * @return 单实例
     */
    @Override
    public SysFormInstance selectSysFormInstanceById(Long id)
    {
        return sysFormInstanceMapper.selectSysFormInstanceById(id);
    }

    @Override
    public SysFormInstance selectSysFormInstanceByFormCode(String fromCode) {
        return sysFormInstanceMapper.selectSysFormInstanceByFormCode(fromCode);
    }

    @Override
    public SysFormInstance selectSysFormInstanceByFormName(String fromName) {
        return sysFormInstanceMapper.selectSysFormInstanceByFormName(fromName);
    }

    /**
     * 查询单实例列表
     * 
     * @param sysFormInstance 单实例
     * @return 单实例
     */
    @Override
    public List<SysFormInstance> selectSysFormInstanceList(SysFormInstance sysFormInstance)
    {
        return sysFormInstanceMapper.selectSysFormInstanceList(sysFormInstance);
    }

    /**
     * 新增单实例
     * 
     * @param sysFormInstance 单实例
     * @return 结果
     */
    @Override
    public int insertSysFormInstance(SysFormInstance sysFormInstance)
    {
        sysFormInstance.setCreateTime(DateUtils.getNowDate());
        return sysFormInstanceMapper.insertSysFormInstance(sysFormInstance);
    }

    /**
     * 修改单实例
     * 
     * @param sysFormInstance 单实例
     * @return 结果
     */
    @Override
    public int updateSysFormInstance(SysFormInstance sysFormInstance)
    {
        sysFormInstance.setUpdateTime(DateUtils.getNowDate());
        return sysFormInstanceMapper.updateSysFormInstance(sysFormInstance);
    }

    /**
     * 删除单实例对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysFormInstanceByIds(String ids)
    {
        return sysFormInstanceMapper.deleteSysFormInstanceByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除单实例信息
     * 
     * @param id 单实例ID
     * @return 结果
     */
    @Override
    public int deleteSysFormInstanceById(Long id)
    {
        return sysFormInstanceMapper.deleteSysFormInstanceById(id);
    }
}
