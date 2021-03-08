package com.snow.system.service.impl;

import java.util.List;
import java.util.ArrayList;
import com.snow.common.core.domain.Ztree;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysNewsNodeMapper;
import com.snow.system.domain.SysNewsNode;
import com.snow.system.service.ISysNewsNodeService;
import com.snow.common.core.text.Convert;

/**
 * 消息配置节点Service业务层处理
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
@Service
public class SysNewsNodeServiceImpl implements ISysNewsNodeService 
{
    @Autowired
    private SysNewsNodeMapper sysNewsNodeMapper;

    /**
     * 查询消息配置节点
     * 
     * @param id 消息配置节点ID
     * @return 消息配置节点
     */
    @Override
    public SysNewsNode selectSysNewsNodeById(Integer id)
    {
        return sysNewsNodeMapper.selectSysNewsNodeById(id);
    }

    @Override
    public SysNewsNode selectSysNewsNodeByKey(String newsNodeKey,Long parentId) {
        return sysNewsNodeMapper.selectSysNewsNodeByKey(newsNodeKey,parentId);
    }

    /**
     * 查询消息配置节点列表
     * 
     * @param sysNewsNode 消息配置节点
     * @return 消息配置节点
     */
    @Override
    public List<SysNewsNode> selectSysNewsNodeList(SysNewsNode sysNewsNode)
    {
        return sysNewsNodeMapper.selectSysNewsNodeList(sysNewsNode);
    }

    /**
     * 新增消息配置节点
     * 
     * @param sysNewsNode 消息配置节点
     * @return 结果
     */
    @Override
    public int insertSysNewsNode(SysNewsNode sysNewsNode)
    {
        sysNewsNode.setCreateTime(DateUtils.getNowDate());
        return sysNewsNodeMapper.insertSysNewsNode(sysNewsNode);
    }

    /**
     * 修改消息配置节点
     * 
     * @param sysNewsNode 消息配置节点
     * @return 结果
     */
    @Override
    public int updateSysNewsNode(SysNewsNode sysNewsNode)
    {
        sysNewsNode.setUpdateTime(DateUtils.getNowDate());
        return sysNewsNodeMapper.updateSysNewsNode(sysNewsNode);
    }

    /**
     * 删除消息配置节点对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysNewsNodeByIds(String ids)
    {
        return sysNewsNodeMapper.deleteSysNewsNodeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除消息配置节点信息
     * 
     * @param id 消息配置节点ID
     * @return 结果
     */
    @Override
    public int deleteSysNewsNodeById(Integer id)
    {
        return sysNewsNodeMapper.deleteSysNewsNodeById(id);
    }

    /**
     * 查询消息配置节点树列表
     * 
     * @return 所有消息配置节点信息
     */
    @Override
    public List<Ztree> selectSysNewsNodeTree()
    {
        List<SysNewsNode> sysNewsNodeList = sysNewsNodeMapper.selectSysNewsNodeList(new SysNewsNode());
        List<Ztree> ztrees = new ArrayList<Ztree>();
        for (SysNewsNode sysNewsNode : sysNewsNodeList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(sysNewsNode.getId().longValue());
            ztree.setpId(sysNewsNode.getParentId());
            ztree.setName(sysNewsNode.getNewsNodeName());
            ztree.setTitle(sysNewsNode.getNewsNodeName());
            ztrees.add(ztree);
        }
        return ztrees;
    }
}
