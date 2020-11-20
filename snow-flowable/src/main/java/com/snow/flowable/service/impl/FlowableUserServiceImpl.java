package com.snow.flowable.service.impl;

import com.snow.flowable.service.FlowableUserService;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/20 10:30
 */
@Service
@Slf4j
public class FlowableUserServiceImpl implements FlowableUserService {

    @Autowired
    private ISysUserService iSysUserService;

    @Override
    public Map<String, Object> loginFlowable() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", "admin");
        map.put("firstName", "My");
        map.put("fullName", "Administrator");
        map.put("id", "admin");
        map.put("lastName", "Administrator");

        Map<String, Object> groupMap = new HashMap<>();
        map.put("id", "ROLE_ADMIN");
        map.put("name", "Superusers");
        map.put("type", "security-role");

        List<Map<String, Object>> groups = new ArrayList<>();
        groups.add(groupMap);

        map.put("groups", groups);

        return map;
    }

    @Override
    public void getFlowableUserList() {
        SysUser sysUser=new SysUser();
        iSysUserService.selectAllocatedList(sysUser)
    }

    @Override
    public void getFlowableUserGroupList() {

    }
}
