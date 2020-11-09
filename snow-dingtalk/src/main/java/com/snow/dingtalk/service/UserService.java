package com.snow.dingtalk.service;

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
    String create(SysUser sysUser);

}
