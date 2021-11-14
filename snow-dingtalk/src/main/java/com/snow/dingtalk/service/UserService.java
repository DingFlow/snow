package com.snow.dingtalk.service;

import com.dingtalk.api.response.*;
import com.snow.dingtalk.model.request.UserListRequest;
import com.snow.system.domain.SysUser;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/22 14:29
 */

public interface UserService  {


    /**
     * 通过临时授权码获取授权用户的个人信息
     * @param authCode
     * @return
     */
    OapiSnsGetuserinfoBycodeResponse.UserInfo getUserInfoByCode(String authCode);
    /**
     * 创建用户
     * @param
     * @return
     */
    OapiV2UserCreateResponse.UserCreateResponse createUser(SysUser sysUser);


    /**
     * 编辑用户
     * @param sysUser
     * @return
     */
    String updateUserV2(SysUser sysUser);

    /**
     * 删除用户
     * @param ids
     */
    void deleteUser(String ids);

    /**
     * 根据userid获取用户详情
     * @param userId
     * @return
     */
    OapiV2UserGetResponse.UserGetResponse  getUserByUserId(String userId);


    /**
     * 获取部门用户详情
     * @param userListRequest
     * @return
     */
    OapiV2UserListResponse.PageResult getUserInfoByDept(UserListRequest userListRequest);

    /**
     * 根据unionid获取userid
     * @param unionId
     * @return
     */
    OapiUserGetbyunionidResponse.UserGetByUnionIdResponse  getUserByUnionId(String unionId);

    /**
     * 根据系统用户id获取用户UnionId
     * @param sysUserId 系统用户id
     * @return UnionId
     */
    String getUnionIdBySysUserId(Long sysUserId);
}
