package com.snow.system.service.impl;

import java.util.List;
import java.util.Optional;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import com.snow.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysDingProcinstMapper;
import com.snow.system.domain.SysDingProcinst;
import com.snow.system.service.ISysDingProcinstService;
import com.snow.common.core.text.Convert;

/**
 * 钉钉流程实例Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-03-24
 */
@Service
public class SysDingProcinstServiceImpl implements ISysDingProcinstService 
{
    @Autowired
    private SysDingProcinstMapper sysDingProcinstMapper;

    /**
     * 查询钉钉流程实例
     * 
     * @param id 钉钉流程实例ID
     * @return 钉钉流程实例
     */
    @Override
    public SysDingProcinst selectSysDingProcinstById(Long id)
    {
        return sysDingProcinstMapper.selectSysDingProcinstById(id);
    }

    /**
     * 查询钉钉流程实例
     * @param procInstId 钉钉流程实例ID
     * @return
     */
    public SysDingProcinst selectSysDingProcinstByProcInstId(String procInstId){
        SysDingProcinst sysDingProcinst=sysDingProcinstMapper.selectSysDingProcinstByProcInstId(procInstId);
        if(StringUtils.isNotNull(sysDingProcinst)){
            Optional.ofNullable(sysDingProcinst.getFinishTime()).ifPresent(m->DateUtil.formatBetween(sysDingProcinst.getStartTime(),sysDingProcinst.getFinishTime(), BetweenFormater.Level.SECOND));
        }
        return sysDingProcinst;
    }
    /**
     * 查询钉钉流程实例列表
     * 
     * @param sysDingProcinst 钉钉流程实例
     * @return 钉钉流程实例
     */
    @Override
    public List<SysDingProcinst> selectSysDingProcinstList(SysDingProcinst sysDingProcinst)
    {
        List<SysDingProcinst> sysDingProcinstList=sysDingProcinstMapper.selectSysDingProcinstList(sysDingProcinst);

        sysDingProcinstList.forEach(t->

            Optional.ofNullable(t.getFinishTime()).ifPresent(m->t.setProcessSpendTime(DateUtil.formatBetween(t.getStartTime(), t.getFinishTime(), BetweenFormater.Level.SECOND)))
        );

        return sysDingProcinstList;
    }

    /**
     * 新增钉钉流程实例
     * 
     * @param sysDingProcinst 钉钉流程实例
     * @return 结果
     */
    @Override
    public int insertSysDingProcinst(SysDingProcinst sysDingProcinst)
    {
        return sysDingProcinstMapper.insertSysDingProcinst(sysDingProcinst);
    }

    /**
     * 修改钉钉流程实例
     * 
     * @param sysDingProcinst 钉钉流程实例
     * @return 结果
     */
    @Override
    public int updateSysDingProcinst(SysDingProcinst sysDingProcinst)
    {
        return sysDingProcinstMapper.updateSysDingProcinst(sysDingProcinst);
    }

    /**
     * 删除钉钉流程实例对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysDingProcinstByIds(String ids)
    {
        return sysDingProcinstMapper.deleteSysDingProcinstByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除钉钉流程实例信息
     * 
     * @param id 钉钉流程实例ID
     * @return 结果
     */
    @Override
    public int deleteSysDingProcinstById(Long id)
    {
        return sysDingProcinstMapper.deleteSysDingProcinstById(id);
    }
}
