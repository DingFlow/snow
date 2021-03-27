package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysDingProcinst;

/**
 * 钉钉流程实例Service接口
 * 
 * @author 没用的阿吉
 * @date 2021-03-24
 */
public interface ISysDingProcinstService 
{
    /**
     * 查询钉钉流程实例
     * 
     * @param id 钉钉流程实例ID
     * @return 钉钉流程实例
     */
    public SysDingProcinst selectSysDingProcinstById(Long id);

    /**
     * 查询钉钉流程实例
     * @param procInstId
     * @return
     */
    public SysDingProcinst selectSysDingProcinstByProcInstId(String procInstId);
    /**
     * 查询钉钉流程实例列表
     * 
     * @param sysDingProcinst 钉钉流程实例
     * @return 钉钉流程实例集合
     */
    public List<SysDingProcinst> selectSysDingProcinstList(SysDingProcinst sysDingProcinst);

    /**
     * 新增钉钉流程实例
     * 
     * @param sysDingProcinst 钉钉流程实例
     * @return 结果
     */
    public int insertSysDingProcinst(SysDingProcinst sysDingProcinst);

    /**
     * 修改钉钉流程实例
     * 
     * @param sysDingProcinst 钉钉流程实例
     * @return 结果
     */
    public int updateSysDingProcinst(SysDingProcinst sysDingProcinst);

    /**
     * 批量删除钉钉流程实例
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDingProcinstByIds(String ids);

    /**
     * 删除钉钉流程实例信息
     * 
     * @param id 钉钉流程实例ID
     * @return 结果
     */
    public int deleteSysDingProcinstById(Long id);
}
