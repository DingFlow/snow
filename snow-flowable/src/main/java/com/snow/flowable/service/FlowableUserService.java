package com.snow.flowable.service;

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

    void getFlowableUserList();

    void getFlowableUserGroupList();
}
