package com.snow.dingtalk.service;

import com.dingtalk.api.response.OapiV2UserCreateResponse;
import com.snow.system.domain.SysUser;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/22 14:29
 */

public interface UserService  {


    /**
     * 创建用户
     * @param
     * @return
     */
    OapiV2UserCreateResponse.UserCreateResponse createUser(SysUser sysUser);

    /**
     * 删除用户
     * @param ids
     */
    void deleteUser(String ids);

}
