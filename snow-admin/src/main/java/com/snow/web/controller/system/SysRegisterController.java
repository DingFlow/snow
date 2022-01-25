package com.snow.web.controller.system;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.snow.common.constant.MessageConstants;
import com.snow.common.constant.UserConstants;
import com.snow.common.enums.MessageEventType;
import com.snow.common.core.domain.MessageEventRequest;
import com.snow.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.framework.shiro.service.SysRegisterService;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysConfigService;

import java.util.Map;

/**
 * 注册验证
 * 
 * @author snow
 */
@Controller
public class SysRegisterController extends BaseController
{
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/register")
    public String register()
    {
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public AjaxResult ajaxRegister(SysUser user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

    /**
     * 官网注册用户
     * @param user
     * @return
     */
    @PostMapping("/front/register")
    @ResponseBody
    public AjaxResult afrontRegister(SysUser user)
    {

        String msg = registerService.register(user);

        SysUser sysUser = userService.selectUserByLoginName(user.getLoginName());
        //分配角色
        userService.insertUserAuth(sysUser.getUserId(), new Long[]{UserConstants.FRONT_ROLE_TYPE_ID});
        //注册成功后发送站内信
        MessageEventRequest messageEventDTO=new MessageEventRequest(MessageEventType.REGISTER_ACCOUNT_SUCCESS.getCode());
        messageEventDTO.setConsumerIds(Sets.newHashSet(String.valueOf(sysUser.getUserId())));
        messageEventDTO.setMessageOutsideId(String.valueOf(sysUser.getUserId()));
        messageEventDTO.setMessageEventType(MessageEventType.REGISTER_ACCOUNT_SUCCESS);
        messageEventDTO.setMessageShow(1);
        Map<String,Object> map= Maps.newHashMap();
        map.put("userName",sysUser.getUserName());
        map.put("nowTime", DateUtil.now());
        map.put("loginName",sysUser.getLoginName());
        map.put("dateTime",DateUtil.now());
        messageEventDTO.setParamMap(map);
        messageEventDTO.setTemplateCode(MessageConstants.REGISTER_ACCOUNT_SUCCESS_CODE);
        applicationContext.publishEvent(messageEventDTO);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
