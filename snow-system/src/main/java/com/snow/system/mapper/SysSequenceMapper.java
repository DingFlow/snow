package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.SysSequence;

/**
 * 系统序列设置Mapper接口
 * 
 * @author snow
 * @date 2020-11-23
 */
public interface SysSequenceMapper 
{
    /**
     * 查询系统序列设置
     * 
     * @param name 系统序列设置ID
     * @return 系统序列设置
     */
    public SysSequence selectSysSequenceById(String name);

    /**
     * 查询系统序列设置列表
     * 
     * @param sysSequence 系统序列设置
     * @return 系统序列设置集合
     */
    public List<SysSequence> selectSysSequenceList(SysSequence sysSequence);

    /**
     * 新增系统序列设置
     * 
     * @param sysSequence 系统序列设置
     * @return 结果
     */
    public int insertSysSequence(SysSequence sysSequence);

    /**
     * 修改系统序列设置
     * 
     * @param sysSequence 系统序列设置
     * @return 结果
     */
    public int updateSysSequence(SysSequence sysSequence);

    /**
     * 获取下个序列值
     * @param name
     * @return
     */
    public int getNextSequence(String name);

    /**
     * 更新下个序列值
     * @param name
     * @return
     */
    public int updateNextSequence(String name);
    /**
     * 删除系统序列设置
     * 
     * @param name 系统序列设置ID
     * @return 结果
     */
    public int deleteSysSequenceById(String name);

    /**
     * 批量删除系统序列设置
     * 
     * @param names 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysSequenceByIds(String[] names);
}
