package com.snow.framework.web.service;

import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-11 14:50
 **/
@Service("user")
public class UserService {

    @Autowired
    private SysUserServiceImpl sysUserService;

    /**
     * 根据id获取name
     * @param id
     * @return
     */
    public String getName(String id)
    {
        SysUser sysUser = sysUserService.selectUserById(Long.parseLong(id));
        return sysUser.getUserName();
    }
}
