package com.snow.system.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import com.snow.common.exception.BusinessException;
import com.snow.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysSequenceMapper;
import com.snow.system.domain.SysSequence;
import com.snow.system.service.ISysSequenceService;
import com.snow.common.core.text.Convert;

/**
 * 系统序列设置Service业务层处理
 * 
 * @author snow
 * @date 2020-11-23
 */
@Service
public class SysSequenceServiceImpl implements ISysSequenceService 
{
    @Autowired
    private SysSequenceMapper sysSequenceMapper;

    /**
     * 查询系统序列设置
     * 
     * @param name 系统序列设置ID
     * @return 系统序列设置
     */
    @Override
    public SysSequence selectSysSequenceById(String name)
    {
        return sysSequenceMapper.selectSysSequenceById(name);
    }

    /**
     * 查询系统序列设置列表
     * 
     * @param sysSequence 系统序列设置
     * @return 系统序列设置
     */
    @Override
    public List<SysSequence> selectSysSequenceList(SysSequence sysSequence)
    {
        return sysSequenceMapper.selectSysSequenceList(sysSequence);
    }

    /**
     * 新增系统序列设置
     * 
     * @param sysSequence 系统序列设置
     * @return 结果
     */
    @Override
    public int insertSysSequence(SysSequence sysSequence)
    {
        return sysSequenceMapper.insertSysSequence(sysSequence);
    }

    /**
     * 修改系统序列设置
     * 
     * @param sysSequence 系统序列设置
     * @return 结果
     */
    @Override
    public int updateSysSequence(SysSequence sysSequence)
    {
        return sysSequenceMapper.updateSysSequence(sysSequence);
    }

    /**
     * 删除系统序列设置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysSequenceByIds(String ids)
    {
        return sysSequenceMapper.deleteSysSequenceByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除系统序列设置信息
     * 
     * @param name 系统序列设置ID
     * @return 结果
     */
    @Override
    public int deleteSysSequenceById(String name)
    {
        return sysSequenceMapper.deleteSysSequenceById(name);
    }

    /**
     * 获取最新的序列化
     * @param name
     * @return
     */
    @Override
    public String getNewSequenceNo(String name) {
        SysSequence sysSequence = selectSysSequenceById(name);
        if(StringUtils.isNull(sysSequence)){
            throw new BusinessException("该序列名称不存在");
        }
        sysSequenceMapper.updateNextSequence(name);
        int nextSequence = sysSequenceMapper.getNextSequence(name);
        String date = DateUtil.format(new Date(), "yyyyMMdd");
        StringBuilder sequenceNo=new StringBuilder(name);
        sequenceNo.append(date);
        sequenceNo.append(String.format("%04d",nextSequence));
        return sequenceNo.toString();
    }
}
