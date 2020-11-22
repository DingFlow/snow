package com.snow.flowable.service.impl;

import com.google.common.collect.Lists;
import com.snow.common.utils.StringUtils;
import com.snow.flowable.service.FlowableUserService;
import com.snow.system.domain.SysRole;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysRoleService;
import com.snow.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.ui.common.model.RemoteGroup;
import org.flowable.ui.common.model.RemoteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private ISysRoleService sysRoleService;

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
    public List<RemoteUser> getFlowUserList(String name) {
        SysUser sysUser=new SysUser();
        sysUser.setUserName(name);
        List<SysUser> sysUsers = iSysUserService.selectUserList(sysUser);
        List<RemoteUser> result = new ArrayList();

        sysUsers.parallelStream().forEach(t->{
            RemoteUser remoteUser=new RemoteUser();
            remoteUser.setId(String.valueOf(t.getUserId()));
            remoteUser.setEmail(t.getEmail());
            remoteUser.setDisplayName(t.getUserName());
            remoteUser.setFullName(t.getUserName());
            remoteUser.setFirstName(t.getUserName());
            remoteUser.setLastName(t.getUserName());

            List<SysRole> roles = t.getRoles();
            if(!StringUtils.isEmpty(roles)) {
                List<RemoteGroup> remoteGroupList = roles.stream().map(role -> {
                    RemoteGroup remoteGroup = new RemoteGroup();
                    remoteGroup.setId(String.valueOf(role.getRoleId()));
                    remoteGroup.setName(role.getRoleName());
                    remoteGroup.setType(role.getRoleKey());
                    return remoteGroup;
                }).collect(Collectors.toList());
                remoteUser.setGroups(remoteGroupList);
            }

            result.add(remoteUser);
        });
        return result;
    }

    @Override
    public List<RemoteGroup> getFlowUserGroupList(String filter) {
        SysRole sysRole=new SysRole();
        sysRole.setRoleName(filter);
        List<SysRole> sysRoles = sysRoleService.selectRoleList(sysRole);
        List<RemoteGroup> remoteGroupList=Lists.newArrayList();
        if(!StringUtils.isEmpty(sysRoles)) {
            remoteGroupList = sysRoles.stream().map(role -> {
                RemoteGroup remoteGroup = new RemoteGroup();
                remoteGroup.setName(role.getRoleName());
                remoteGroup.setId(String.valueOf(role.getRoleId()));
                remoteGroup.setType(role.getRoleKey());
                return remoteGroup;
            }).collect(Collectors.toList());
        }
        return remoteGroupList;
    }

}
