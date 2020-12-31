package com.snow.flowable.service;

import com.snow.system.domain.SysRole;
import com.snow.system.domain.SysUser;
import org.flowable.ui.common.model.RemoteGroup;
import org.flowable.ui.common.model.RemoteUser;

import java.util.List;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
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
     * 获取用户组
     * @param name
     * @return
     */
    List<RemoteUser> getFlowUserList(String name);

    /**
     * 获取流程角色组
     * @param filter
     * @return
     */
    List<RemoteGroup> getFlowUserGroupList(String filter);


    /**
     * 根据流程组查询角色
     * @param groupId
     * @return
     */
    List<SysUser> getUserByFlowGroupId(Long groupId);


    /**
     * 根据流程组查询角色
     * @param userId
     * @return
     */
    List<SysRole> getFlowGroupByUserId(Long userId);
}
