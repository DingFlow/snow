package com.snow.system.service.impl;

import java.util.List;
import java.util.ArrayList;
import com.snow.common.core.domain.Ztree;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.FlowGroupDOMapper;
import com.snow.system.domain.FlowGroupDO;
import com.snow.system.service.IFlowGroupDOService;
import com.snow.common.core.text.Convert;

/**
 * 流程组信息Service业务层处理
 * 
 * @author snow
 * @date 2020-12-19
 */
@Service
public class FlowGroupDOServiceImpl implements IFlowGroupDOService 
{
    @Autowired
    private FlowGroupDOMapper flowGroupDOMapper;

    /**
     * 查询流程组信息
     * 
     * @param roleId 流程组信息ID
     * @return 流程组信息
     */
    @Override
    public FlowGroupDO selectFlowGroupDOById(Long roleId)
    {
        return flowGroupDOMapper.selectFlowGroupDOById(roleId);
    }

    @Override
    public List<FlowGroupDO> selectFlowGroupDOByUserId(Long userId) {
        return flowGroupDOMapper.selectFlowGroupDOByUserId(userId);
    }

    /**
     * 查询流程组信息列表
     * 
     * @param flowGroupDO 流程组信息
     * @return 流程组信息
     */
    @Override
    public List<FlowGroupDO> selectFlowGroupDOList(FlowGroupDO flowGroupDO)
    {
        return flowGroupDOMapper.selectFlowGroupDOList(flowGroupDO);
    }

    /**
     * 新增流程组信息
     * 
     * @param flowGroupDO 流程组信息
     * @return 结果
     */
    @Override
    public int insertFlowGroupDO(FlowGroupDO flowGroupDO)
    {
        flowGroupDO.setCreateTime(DateUtils.getNowDate());
        return flowGroupDOMapper.insertFlowGroupDO(flowGroupDO);
    }

    /**
     * 修改流程组信息
     * 
     * @param flowGroupDO 流程组信息
     * @return 结果
     */
    @Override
    public int updateFlowGroupDO(FlowGroupDO flowGroupDO)
    {
        flowGroupDO.setUpdateTime(DateUtils.getNowDate());
        return flowGroupDOMapper.updateFlowGroupDO(flowGroupDO);
    }

    /**
     * 删除流程组信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFlowGroupDOByIds(String ids)
    {
        return flowGroupDOMapper.deleteFlowGroupDOByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除流程组信息信息
     * 
     * @param roleId 流程组信息ID
     * @return 结果
     */
    @Override
    public int deleteFlowGroupDOById(Long roleId)
    {
        return flowGroupDOMapper.deleteFlowGroupDOById(roleId);
    }

    /**
     * 查询流程组信息树列表
     * 
     * @return 所有流程组信息信息
     */
    @Override
    public List<Ztree> selectFlowGroupDOTree()
    {
        List<FlowGroupDO> flowGroupDOList = flowGroupDOMapper.selectFlowGroupDOList(new FlowGroupDO());
        List<Ztree> ztrees = new ArrayList<Ztree>();
        for (FlowGroupDO flowGroupDO : flowGroupDOList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(flowGroupDO.getRoleId());
            ztree.setpId(flowGroupDO.getParentId());
            ztree.setName(flowGroupDO.getRoleName());
            ztree.setTitle(flowGroupDO.getRoleName());
            ztrees.add(ztree);
        }
        return ztrees;
    }
}
