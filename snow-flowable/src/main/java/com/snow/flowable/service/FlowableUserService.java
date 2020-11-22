package com.snow.flowable.service;

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

    List<RemoteUser> getFlowUserList(String name);

    List<RemoteGroup> getFlowUserGroupList(String filter);
}
