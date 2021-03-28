package com.snow.quartz.task;

import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.snow.common.utils.StringUtils;
import com.snow.dingtalk.model.UserListRequest;
import com.snow.dingtalk.service.impl.DepartmentServiceImpl;
import com.snow.dingtalk.service.impl.UserServiceImpl;
import com.snow.framework.shiro.service.SysPasswordService;
import com.snow.framework.util.ShiroUtils;
import com.snow.framework.web.service.ConfigService;
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
        dingTalkDepartmentList.forEach(t->{
            SysDept sysDept=new SysDept();
            SysDept sysDepts = sysDeptService.selectDeptById(t.getId());
            if(StringUtils.isNotNull(sysDepts)){
                sysDept.setDeptId(t.getId());
                sysDept.setDeptName(t.getName());
                sysDept.setOrderNum(String.valueOf(t.getId()));
                sysDept.setParentId(t.getParentid());
                sysDept.setIsSyncDingTalk(false);
                sysDeptService.updateDept(sysDept);
            }else {
                sysDept.setDeptId(t.getId());
                sysDept.setDeptName(t.getName());
                sysDept.setOrderNum(String.valueOf(t.getId()));
                sysDept.setParentId(t.getParentid());
                sysDept.setIsSyncDingTalk(false);
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
            OapiV2UserListResponse.PageResult userInfoByDept = userService.getUserInfoByDept(userListRequest);
            List<OapiV2UserListResponse.ListUserResponse> list = userInfoByDept.getList();
            list.forEach(t->{
                SysUser sysUser = sysUserService.selectUserByDingUserId(t.getUserid());
                if(StringUtils.isNotNull(sysUser)){
                    SysUser insertUser=new SysUser();
                    insertUser.setUserId(sysUser.getUserId());
                    insertUser.setUserName(t.getName());
                    insertUser.setDeptId(department.getId());
                    insertUser.setEmail(t.getEmail());
                    insertUser.setDingUserId(t.getUserid());
                    if(StringUtils.isNotNull(t.getHiredDate())){
                        insertUser.setHiredDate(new Date(t.getHiredDate()));
                    }
                    insertUser.setJobnumber(t.getJobNumber());
                    insertUser.setPhonenumber(t.getMobile());
                    insertUser.setWorkPlace(t.getWorkPlace());
                    insertUser.setAvatar(t.getAvatar());
                    insertUser.setPosition(t.getTitle());
                    insertUser.setIsSyncDingTalk(false);
                    sysUserService.updateUser(insertUser);
                    //更新用户数据
                }else {
                    SysUser insertUser=new SysUser();
                    insertUser.setUserName(t.getName());
                    insertUser.setDeptId(department.getId());
                    insertUser.setEmail(t.getEmail());
                    insertUser.setDingUserId(t.getUserid());
                    if(StringUtils.isNotNull(t.getHiredDate())){
                        insertUser.setHiredDate(new Date(t.getHiredDate()));
                    }
                    insertUser.setJobnumber(t.getJobNumber());
                    insertUser.setLoginName(t.getMobile());
                    insertUser.setPhonenumber(t.getMobile());
                    insertUser.setWorkPlace(t.getWorkPlace());
                    insertUser.setAvatar(t.getAvatar());
                    insertUser.setPosition(t.getTitle());
                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    //设置密码
                    insertUser.setSalt(ShiroUtils.randomSalt());
                    insertUser.setPassword(passwordService.encryptPassword(t.getMobile(), password, insertUser.getSalt()));

                    //角色统一为2
                    insertUser.setRoleIds(new Long[]{2L});
                    insertUser.setIsSyncDingTalk(false);
                    sysUserService.insertUser(insertUser);
                }

            });
        }
    }



}
