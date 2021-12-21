package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysMessageTransition;

/**
 * 消息流转中心Service接口
 * 
 * @author 没用的阿吉
 * @date 2021-03-30
 */
public interface ISysMessageTransitionService 
{
    /**
     * 查询消息流转中心
     * 
     * @param id 消息流转中心ID
     * @return 消息流转中心
     */
    public SysMessageTransition selectSysMessageTransitionById(Long id);


    public Boolean getIsRead(SysMessageTransition sysMessageTransition);

    /**
     * 查询消息流转中心列表
     * 
     * @param sysMessageTransition 消息流转中心
     * @return 消息流转中心集合
     */
    public List<SysMessageTransition> selectSysMessageTransitionList(SysMessageTransition sysMessageTransition);

    /**
     * 新增消息流转中心
     * 
     * @param sysMessageTransition 消息流转中心
     * @return 结果
     */
    public int insertSysMessageTransition(SysMessageTransition sysMessageTransition);

    /**
     * 修改消息流转中心
     * 
     * @param sysMessageTransition 消息流转中心
     * @return 结果
     */
    public int updateSysMessageTransition(SysMessageTransition sysMessageTransition);


    /**
     * 修改消息流转中心
     *
     * @param sysMessageTransition 消息流转中心
     * @return 结果
     */
    public int updateByCondition(SysMessageTransition sysMessageTransition);

    /**
     * 批量删除消息流转中心
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysMessageTransitionByIds(String ids);

    /**
     * 删除消息流转中心信息
     * 
     * @param id 消息流转中心ID
     * @return 结果
     */
    public int deleteSysMessageTransitionById(Long id);


    public int deleteSysMessageTransitionByOutsideId(List outsideIdList);


}
