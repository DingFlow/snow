package com.snow.from.service.impl;

import com.snow.common.core.text.Convert;
import com.snow.common.utils.DateUtils;
import com.snow.from.domain.SysFormField;
import com.snow.from.mapper.SysFormFieldMapper;
import com.snow.from.service.ISysFormFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单字段Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-03-21
 */
@Service
public class SysFormFieldServiceImpl implements ISysFormFieldService
{
    @Autowired
    private SysFormFieldMapper sysFormFieldMapper;

    /**
     * 查询单字段
     * 
     * @param id 单字段ID
     * @return 单字段
     */
    @Override
    public SysFormField selectSysFormFieldById(Long id)
    {
        return sysFormFieldMapper.selectSysFormFieldById(id);
    }

    /**
     * 查询单字段列表
     * 
     * @param sysFormField 单字段
     * @return 单字段
     */
    @Override
    public List<SysFormField> selectSysFormFieldList(SysFormField sysFormField)
    {
        return sysFormFieldMapper.selectSysFormFieldList(sysFormField);
    }

    /**
     * 新增单字段
     * 
     * @param sysFormField 单字段
     * @return 结果
     */
    @Override
    public int insertSysFormField(SysFormField sysFormField)
    {
        sysFormField.setCreateTime(DateUtils.getNowDate());
        return sysFormFieldMapper.insertSysFormField(sysFormField);
    }

    /**
     * 修改单字段
     * 
     * @param sysFormField 单字段
     * @return 结果
     */
    @Override
    public int updateSysFormField(SysFormField sysFormField)
    {
        sysFormField.setUpdateTime(DateUtils.getNowDate());
        return sysFormFieldMapper.updateSysFormField(sysFormField);
    }

    /**
     * 删除单字段对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysFormFieldByIds(String ids)
    {
        return sysFormFieldMapper.deleteSysFormFieldByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除单字段信息
     * 
     * @param id 单字段ID
     * @return 结果
     */
    @Override
    public int deleteSysFormFieldById(Long id)
    {
        return sysFormFieldMapper.deleteSysFormFieldById(id);
    }
}
