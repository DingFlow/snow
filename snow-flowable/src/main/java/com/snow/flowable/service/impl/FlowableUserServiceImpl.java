package com.snow.flowable.service.impl;

import com.google.common.collect.Lists;
import com.snow.common.constant.UserConstants;
import com.snow.common.utils.StringUtils;
import com.snow.flowable.service.FlowableUserService;
import com.snow.system.domain.FlowGroupDO;
import com.snow.system.domain.SysRole;
import com.snow.system.domain.SysUser;
import com.snow.system.service.IFlowGroupDOService;
import com.snow.system.service.ISysRoleService;
import com.snow.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.ui.common.model.RemoteGroup;
import org.flowable.ui.common.model.RemoteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
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

    //存放所有子几点
    private static Set<Long> childFlowGroup= new HashSet<>();
    
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private IFlowGroupDOService flowGroupDOService;

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
           // remoteUser.setFirstName(t.getUserName());
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
        sysRole.setRoleType(UserConstants.FLOW_ROLE_TYPE);
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

    @Override
    public List<SysUser> getUserByFlowGroupId(Long groupId) {
        FlowGroupDO flowGroupDO = flowGroupDOService.selectFlowGroupDOById(groupId);
        //先获取所有的子组Id
        Set<Long> allSonSysRoleList = getAllSonSysRoleList(groupId);
        allSonSysRoleList.add(flowGroupDO.getRoleId());
        //根据角色获取所有用户
        List<SysUser> userList = iSysUserService.selectUserListByRoleIds( new ArrayList<>(allSonSysRoleList));
        return userList;
    }

    @Override
    public List<SysRole> getFlowGroupByUserId(String userId) {
        return null;
    }

 

    /**
     * 获取某个父节点下面的所有子节点
     * @param parentId
     * @return
     */
    public  Set<Long> getAllSonSysRoleList(Long parentId){
        FlowGroupDO sysRole=new FlowGroupDO();
        sysRole.setParentId(parentId);
        sysRole.setRoleType(UserConstants.FLOW_ROLE_TYPE);
        List<FlowGroupDO> sysRoleList = flowGroupDOService.selectFlowGroupDOList(sysRole);
        if(!CollectionUtils.isEmpty(sysRoleList)){
            Set<Long> collect = sysRoleList.stream().map(FlowGroupDO::getRoleId).collect(Collectors.toSet());
            childFlowGroup.addAll(collect);
            for(FlowGroupDO flowGroupDO: sysRoleList){
                // 不为空则递归
                getAllSonSysRoleList(flowGroupDO.getRoleId());

            }
        }
        return childFlowGroup;
    }


}
