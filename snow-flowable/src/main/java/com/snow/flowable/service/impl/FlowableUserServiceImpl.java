package com.snow.flowable.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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

    /**
     * 根据角色查询人及其父角色的人
     * @param groupId
     * @return
     */
    @Override
    public List<SysUser> getUserByFlowGroupId(Long groupId) {
        FlowGroupDO flowGroupDO = flowGroupDOService.selectFlowGroupDOById(groupId);
        //先获取所有的子组Id
        Set<Long> allSonSysRoleList = getAllParentSysRoleList(groupId);
        allSonSysRoleList.add(flowGroupDO.getRoleId());
        //根据角色获取所有用户
        List<SysUser> userList = iSysUserService.selectUserListByRoleIds( new ArrayList<>(allSonSysRoleList));
        return userList;
    }

    /**
     * 根据人查询角色及其子角色
     * @param userId
     * @return
     */
    @Override
    public Set<Long> getFlowGroupByUserId(Long userId) {
        Set<Long> totalFlowGroupDO=Sets.newHashSet();
        List<FlowGroupDO> flowGroupDOS = flowGroupDOService.selectFlowGroupDOByUserId(userId);
        if(!CollectionUtils.isEmpty(flowGroupDOS)){
            flowGroupDOS.forEach(t->{
                Set<Long> allSonSysRoleList = getAllSonSysRoleList(t.getRoleId());
                totalFlowGroupDO.addAll(allSonSysRoleList);
            });
        }
        return totalFlowGroupDO;
    }

 

    /**
     * 获取某个节点下面的所有子节点
     * @param roleId
     * @return
     */
    public  Set<Long> getAllSonSysRoleList(Long roleId){
        //存放所有子节点
        Set<Long> childFlowGroup= new HashSet<>();
        FlowGroupDO sysRole=new FlowGroupDO();
        sysRole.setParentId(roleId);
        sysRole.setRoleType(UserConstants.FLOW_ROLE_TYPE);
        List<FlowGroupDO> sysRoleList = flowGroupDOService.selectFlowGroupDOList(sysRole);
        if(!CollectionUtils.isEmpty(sysRoleList)){
            Set<Long> collect = sysRoleList.stream().map(FlowGroupDO::getRoleId).collect(Collectors.toSet());
            childFlowGroup.addAll(collect);
            for(FlowGroupDO flowGroupDO: sysRoleList){
                // 不为空则递归
                childFlowGroup.addAll(getAllSonSysRoleList(flowGroupDO.getRoleId()));
            }
        }
        return childFlowGroup;
    }

    /**
     * 获取某个子节点上的所有父节点
     * @param roleId
     * @return
     */
    public  Set<Long> getAllParentSysRoleList(Long roleId){
        //存放所有的父节点
        Set<Long> parentFlowGroup= new HashSet<>();
        SysRole sysRole = sysRoleService.selectRoleById(roleId);
        if(StringUtils.isNotNull(sysRole)){
            FlowGroupDO flowGroupDO=new FlowGroupDO();
            flowGroupDO.setRoleId(sysRole.getParentId());
            flowGroupDO.setRoleType(UserConstants.FLOW_ROLE_TYPE);
            List<FlowGroupDO> sysRoleList = flowGroupDOService.selectFlowGroupDOList(flowGroupDO);
            Set<Long> collect = sysRoleList.stream().map(FlowGroupDO::getRoleId).collect(Collectors.toSet());
            parentFlowGroup.addAll(collect);
            for(FlowGroupDO roleGroupDO: sysRoleList){
                // 不为空则递归
                parentFlowGroup.addAll(getAllParentSysRoleList(roleGroupDO.getRoleId()));
            }
        }
        return parentFlowGroup;
    }

}
