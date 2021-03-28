package com.snow.quartz.task;

import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.snow.common.utils.StringUtils;
import com.snow.dingtalk.model.UserListRequest;
import com.snow.dingtalk.service.impl.DepartmentServiceImpl;
import com.snow.dingtalk.service.impl.UserServiceImpl;
import com.snow.framework.shiro.service.SysPasswordService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysDept;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysDeptServiceImpl;
import com.snow.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @program: snow
 * @description 定时同步钉钉与系统部门数据
 * @author: 没用的阿吉
 * @create: 2021-03-27 21:48
 **/
@Component("syncDingDeptTask")
@Slf4j
public class SyncDingDeptTask {



}
