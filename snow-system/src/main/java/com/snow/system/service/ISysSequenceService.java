package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysSequence;

/**
 * 系统序列设置Service接口
 * 
 * @author snow
 * @date 2020-11-23
 */
public interface ISysSequenceService 
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
     * 批量删除系统序列设置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysSequenceByIds(String ids);

    /**
     * 删除系统序列设置信息
     * 
     * @param name 系统序列设置ID
     * @return 结果
     */
    public int deleteSysSequenceById(String name);

    /**
     * 获取新的序列号
     * @param name
     * @return
     */
    String getNewSequenceNo(String name);
}
