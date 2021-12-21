package com.snow.quartz.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.snow.common.utils.StringUtils;
import com.snow.dingtalk.model.request.UserListRequest;
import com.snow.dingtalk.service.impl.DepartmentServiceImpl;
import com.snow.dingtalk.service.impl.UserServiceImpl;
import com.snow.framework.shiro.service.SysPasswordService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysDept;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysConfigService;
import com.snow.system.service.impl.SysDeptServiceImpl;
import com.snow.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @program: snow
 * @description 钉钉与系统数据同步任务
 * @author: 没用的阿吉
 * @create: 2021-03-27 21:48
 **/
@Component("syncDingDataTask")
@Slf4j
public class SyncDingDataTask {
    @Autowired
    private DepartmentServiceImpl departmentService;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysDeptServiceImpl sysDeptService;

    @Autowired
    private ISysConfigService configService;


    /**
     * 同步部门数据
     */
    public void syncDingDept(){
        //获取钉钉部门列表
        List<OapiDepartmentListResponse.Department> dingTalkDepartmentList = departmentService.getDingTalkDepartmentList();
        if(CollUtil.isEmpty(dingTalkDepartmentList)){
            return;
        }
        dingTalkDepartmentList.forEach(t->{
            SysDept sysDept = new SysDept();
            sysDept.setDeptId(t.getId());
            sysDept.setDeptName(t.getName());
            sysDept.setOrderNum(String.valueOf(t.getId()));
            sysDept.setParentId(t.getParentid());
            sysDept.setIsSyncDingTalk(false);
            //判断是更新还是插入
            SysDept sysDepts = sysDeptService.selectDeptById(t.getId());
            if(ObjectUtil.isNotNull(sysDepts)){
                sysDeptService.updateDept(sysDept);
            }else {
                sysDeptService.insertDept(sysDept);
            }
        });
    }

    /**
     * 同步系统用户
     */
    public void syncDingUserInfo(){
        List<OapiDepartmentListResponse.Department> dingTalkDepartmentList = departmentService.getDingTalkDepartmentList();
        Long cursor=0L;
        for (OapiDepartmentListResponse.Department department : dingTalkDepartmentList) {
            UserListRequest userListRequest=new UserListRequest();
            userListRequest.setDeptId(department.getId());
            //目前用户少，直接获取20条
            userListRequest.setSize(20L);
            userListRequest.setCursor(cursor);
            //获取部门下的用户
            OapiV2UserListResponse.PageResult userInfoByDept = userService.getUserInfoByDept(userListRequest);
            List<OapiV2UserListResponse.ListUserResponse> list = userInfoByDept.getList();
            if(CollUtil.isEmpty(list)){
                return;
            }
            list.forEach(t->{
                SysUser sysUser = warpUser(t);
                sysUser.setDeptId(department.getId());
                SysUser oldSysUser = sysUserService.selectUserByDingUserId(t.getUserid());
                if(StringUtils.isNotNull(oldSysUser)){
                    sysUser.setUserId(sysUser.getUserId());
                    sysUserService.updateUser(sysUser);
                }else {
                    sysUser.setLoginName(t.getMobile());
                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    //设置密码
                    sysUser.setSalt(ShiroUtils.randomSalt());
                    sysUser.setPassword(passwordService.encryptPassword(t.getMobile(), password, sysUser.getSalt()));
                    //角色统一为2
                    sysUser.setRoleIds(new Long[]{2L});
                    sysUserService.insertUser(sysUser);
                }
            });
        }
    }

    /**
     * 构建用户参数
     * @param listUserResponse 传入参数
     * @return 返回组装后user对象
     */
    private SysUser warpUser(OapiV2UserListResponse.ListUserResponse listUserResponse){
        SysUser sysUser=new SysUser();
        sysUser.setUserName(listUserResponse.getName());
        sysUser.setEmail(listUserResponse.getEmail());
        sysUser.setDingUserId(listUserResponse.getUserid());
        sysUser.setJobnumber(listUserResponse.getJobNumber());
        sysUser.setPhonenumber(listUserResponse.getMobile());
        sysUser.setWorkPlace(listUserResponse.getWorkPlace());
        sysUser.setAvatar(listUserResponse.getAvatar());
        sysUser.setPosition(listUserResponse.getTitle());
        if(StringUtils.isNotNull(listUserResponse.getHiredDate())){
            sysUser.setHiredDate(new Date(listUserResponse.getHiredDate()));
        }
        sysUser.setIsSyncDingTalk(false);
        return sysUser;
    }



}
