package com.snow.flowable.service;

import com.snow.system.domain.SysUser;
import org.flowable.ui.common.model.RemoteGroup;
import org.flowable.ui.common.model.RemoteUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author qimingjin
 * @Title:  流程用户相关操作
 * @Description:
 * @date 2020/11/20 10:28
 */
public interface FlowableUserService {
    /**
     * 模拟用户登录
     * @return
     */
    Map<String, Object> loginFlowable();

    /**
     * 根据用户名称查询用户
     * @param name 用户名称
     * @return 流程用户
     */
    List<RemoteUser> getFlowUserList(String name);

    /**
     * 获取流程角色组
     * @param filter 用户组
     * @return 流程用户组
     */
    List<RemoteGroup> getFlowUserGroupList(String filter);


    /**
     * 根据流程组查询用户
     * @param groupId
     * @return
     */
    List<SysUser> getUserByFlowGroupId(Long groupId);


    /**
     * 查询用户拥有的流程角色
     * @param userId 用户id
     * @return 角色集合
     */
    Set<Long> getFlowGroupByUserId(Long userId);


    /**
     * 获取当前节点的待办人
     * @param assignee 任务分配人
     * @param taskId 任务ID
     * @return 待处理人员集合
     */
     List<SysUser> getCandidateUsers(String assignee,String taskId);

    /**
     *
     * @param filter
     * @return
     */
    List<RemoteGroup> getLinkFlowUserGroupList(String filter);

}
