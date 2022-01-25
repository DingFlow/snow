package com.snow.from.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.from.domain.SysFormField;

import java.util.List;

/**
 * 单字段Mapper接口
 * 
 * @author 没用的阿吉
 * @date 2021-03-21
 */
public interface SysFormFieldMapper extends BaseMapper<SysFormField> {
    /**
     * 查询单字段
     * 
     * @param id 单字段ID
     * @return 单字段
     */
    public SysFormField selectSysFormFieldById(Long id);

    /**
     * 查询单字段列表
     * 
     * @param sysFormField 单字段
     * @return 单字段集合
     */
    public List<SysFormField> selectSysFormFieldList(SysFormField sysFormField);

    /**
     * 新增单字段
     * 
     * @param sysFormField 单字段
     * @return 结果
     */
    public int insertSysFormField(SysFormField sysFormField);

    /**
     * 修改单字段
     * 
     * @param sysFormField 单字段
     * @return 结果
     */
    public int updateSysFormField(SysFormField sysFormField);

    /**
     * 删除单字段
     * 
     * @param id 单字段ID
     * @return 结果
     */
    public int deleteSysFormFieldById(Long id);

    /**
     * 批量删除单字段
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFormFieldByIds(String[] ids);
}
