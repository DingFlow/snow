package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysNewsNode;
import com.snow.common.core.domain.Ztree;

/**
 * 消息配置节点Service接口
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
public interface ISysNewsNodeService 
{
    /**
     * 查询消息配置节点
     * 
     * @param id 消息配置节点ID
     * @return 消息配置节点
     */
    public SysNewsNode selectSysNewsNodeById(Integer id);

    /**
     * 查询消息配置节点
     * @param newsNodeKey 配置节点key
     * @return
     */
    public SysNewsNode selectSysNewsNodeByKey(String newsNodeKey,Long parentId);

    /**
     * 查询消息配置节点列表
     * 
     * @param sysNewsNode 消息配置节点
     * @return 消息配置节点集合
     */
    public List<SysNewsNode> selectSysNewsNodeList(SysNewsNode sysNewsNode);

    /**
     * 新增消息配置节点
     * 
     * @param sysNewsNode 消息配置节点
     * @return 结果
     */
    public int insertSysNewsNode(SysNewsNode sysNewsNode);

    /**
     * 修改消息配置节点
     * 
     * @param sysNewsNode 消息配置节点
     * @return 结果
     */
    public int updateSysNewsNode(SysNewsNode sysNewsNode);

    /**
     * 批量删除消息配置节点
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysNewsNodeByIds(String ids);

    /**
     * 删除消息配置节点信息
     * 
     * @param id 消息配置节点ID
     * @return 结果
     */
    public int deleteSysNewsNodeById(Integer id);

    /**
     * 查询消息配置节点树列表
     * 
     * @return 所有消息配置节点信息
     */
    public List<Ztree> selectSysNewsNodeTree();
}
