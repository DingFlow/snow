package com.snow.from.mapper;

import com.snow.from.domain.SysFormInstance;

import java.util.List;

/**
 * 单实例Mapper接口
 * 
 * @author 没用的阿吉
 * @date 2021-03-21
 */
public interface SysFormInstanceMapper 
{
    /**
     * 查询单实例
     * 
     * @param id 单实例ID
     * @return 单实例
     */
    public SysFormInstance selectSysFormInstanceById(Long id);

    /**
     * 查询单实例
     * @param fromCode
     * @return
     */
    public SysFormInstance selectSysFormInstanceByFormCode(String fromCode);

    /**
     * 查询单实例
     * @param fromName
     * @return
     */
    public SysFormInstance selectSysFormInstanceByFormName(String fromName);

    /**
     * 查询单实例列表
     * 
     * @param sysFormInstance 单实例
     * @return 单实例集合
     */
    public List<SysFormInstance> selectSysFormInstanceList(SysFormInstance sysFormInstance);

    /**
     * 新增单实例
     * 
     * @param sysFormInstance 单实例
     * @return 结果
     */
    public int insertSysFormInstance(SysFormInstance sysFormInstance);

    /**
     * 修改单实例
     * 
     * @param sysFormInstance 单实例
     * @return 结果
     */
    public int updateSysFormInstance(SysFormInstance sysFormInstance);

    /**
     * 删除单实例
     * 
     * @param id 单实例ID
     * @return 结果
     */
    public int deleteSysFormInstanceById(Long id);

    /**
     * 批量删除单实例
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFormInstanceByIds(String[] ids);
}
