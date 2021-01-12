package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.FlowGroupDO;
import com.snow.common.core.domain.Ztree;

/**
 * 流程组信息Service接口
 * 
 * @author snow
 * @date 2020-12-19
 */
public interface IFlowGroupDOService 
{
    /**
     * 查询流程组信息
     * 
     * @param roleId 流程组信息ID
     * @return 流程组信息
     */
    public FlowGroupDO selectFlowGroupDOById(Long roleId);

    /**
     * 根据用户查询流程组
     * @param userId
     * @return
     */
    public List<FlowGroupDO> selectFlowGroupDOByUserId(Long userId);

    /**
     * 查询流程组信息列表
     * 
     * @param flowGroupDO 流程组信息
     * @return 流程组信息集合
     */
    public List<FlowGroupDO> selectFlowGroupDOList(FlowGroupDO flowGroupDO);

    /**
     * 新增流程组信息
     * 
     * @param flowGroupDO 流程组信息
     * @return 结果
     */
    public int insertFlowGroupDO(FlowGroupDO flowGroupDO);

    /**
     * 修改流程组信息
     * 
     * @param flowGroupDO 流程组信息
     * @return 结果
     */
    public int updateFlowGroupDO(FlowGroupDO flowGroupDO);

    /**
     * 批量删除流程组信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFlowGroupDOByIds(String ids);

    /**
     * 删除流程组信息信息
     * 
     * @param roleId 流程组信息ID
     * @return 结果
     */
    public int deleteFlowGroupDOById(Long roleId);

    /**
     * 查询流程组信息树列表
     * 
     * @return 所有流程组信息信息
     */
    public List<Ztree> selectFlowGroupDOTree();
}
