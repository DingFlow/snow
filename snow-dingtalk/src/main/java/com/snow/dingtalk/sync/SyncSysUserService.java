package com.snow.dingtalk.sync;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.snow.common.annotation.DingTalkLog;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.enums.DingTalkLogType;
import com.snow.common.enums.SyncLogType;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.service.UserService;
import com.snow.framework.shiro.service.SysPasswordService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysConfigService;
import com.snow.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author qimingjin
 * @Title: 监听钉钉用户信息变更逻辑
 * @Description:
 * @date 2021/11/5 11:10
 */
@Component
@Slf4j
public class SyncSysUserService implements ISyncSysInfo {

    private UserService userService= SpringUtils.getBean(UserService.class);

    private ISysUserService sysUserService= SpringUtils.getBean(ISysUserService.class);

    private ISysConfigService configService=SpringUtils.getBean(ISysConfigService.class);

    private SysPasswordService passwordService=SpringUtils.getBean(SysPasswordService.class);

    @Override
    public JSONObject SyncSysInfo(DingTalkListenerType dingTalkListenerType, JSONObject jsonObject) {
        Integer code = dingTalkListenerType.getCode();
        if(code==DingTalkListenerType.USER_CREATE.getCode()){
            insertUser(jsonObject);
        }else if(code==DingTalkListenerType.USER_UPDATE.getCode()){
            updateUser(jsonObject);
        }else if(code==DingTalkListenerType.USER_DELETE.getCode()){
            deleteUser(jsonObject);
        }
        return null;
    }

    @DingTalkLog(dingTalkLogType = DingTalkLogType.USER_CREATE,syncLogTpye = SyncLogType.SYNC_SYS)
    private void insertUser(JSONObject jsonObject){
        List<String> userIdList = jsonObject.getJSONArray("UserId").toJavaList(String.class);
        for(String userId:userIdList){
            OapiV2UserGetResponse.UserGetResponse user = userService.getUserByUserId(userId);
            SysUser sysUser = warpSysUser(user);
            String password = configService.selectConfigByKey("sys.user.initPassword");
            //设置密码
            sysUser.setSalt(ShiroUtils.randomSalt());
            sysUser.setPassword(passwordService.encryptPassword(user.getMobile(), password, sysUser.getSalt()));
            //角色统一为2
            sysUser.setRoleIds(new Long[]{2L});
            sysUserService.insertUser(sysUser);
        }

    }

    @DingTalkLog(dingTalkLogType = DingTalkLogType.USER_UPDATE,syncLogTpye = SyncLogType.SYNC_SYS)
    private void updateUser(JSONObject jsonObject){
        List<String> userIdList = jsonObject.getJSONArray("UserId").toJavaList(String.class);
        for(String userId:userIdList){
            OapiV2UserGetResponse.UserGetResponse user = userService.getUserByUserId(userId);
            SysUser sysUser = warpSysUser(user);
            SysUser oldSysUser = sysUserService.selectUserByDingUserId(userId);
            sysUser.setUserId(oldSysUser.getUserId());
            sysUserService.updateUser(sysUser);
        }
    }

    @DingTalkLog(dingTalkLogType = DingTalkLogType.USER_DELETE,syncLogTpye = SyncLogType.SYNC_SYS)
    private void deleteUser(JSONObject jsonObject){
        List<String> userIdList = jsonObject.getJSONArray("UserId").toJavaList(String.class);
        for(String userId:userIdList){
            SysUser oldSysUser = sysUserService.selectUserByDingUserId(userId);
            sysUserService.deleteUserById(oldSysUser.getUserId());
        }
    }

    private SysUser warpSysUser(OapiV2UserGetResponse.UserGetResponse user){
        SysUser sysUser= new SysUser();
        sysUser.setDingUserId(user.getUserid());
        sysUser.setAvatar(user.getAvatar());
        sysUser.setEmail(user.getEmail());
        sysUser.setUserName(user.getName());
        sysUser.setJobnumber(user.getJobNumber());
        sysUser.setOrgEmail(user.getOrgEmail());
        sysUser.setHiredDate(new Date(user.getHiredDate()));
        sysUser.setTel(user.getTelephone());
        sysUser.setPhonenumber(user.getMobile());
        sysUser.setWorkPlace(user.getWorkPlace());
        sysUser.setRemark(user.getRemark());
        sysUser.setIsSyncDingTalk(false);
        if(CollUtil.isNotEmpty(user.getDeptIdList())){
            sysUser.setDeptId(user.getDeptIdList().get(0));
        }
        return sysUser;
    }
}
