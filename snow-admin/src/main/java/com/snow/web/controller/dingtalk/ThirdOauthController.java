package com.snow.web.controller.dingtalk;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import com.dingtalk.api.response.OapiUserGetbyunionidResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.AuthUtils;
import com.snow.common.utils.ServletUtils;
import com.snow.common.utils.StringUtils;
import com.snow.dingtalk.service.UserService;
import com.snow.framework.shiro.auth.LoginType;
import com.snow.framework.shiro.auth.UserToken;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysAuthUser;
import com.snow.system.domain.SysSocialUser;
import com.snow.system.domain.SysUser;
import com.snow.system.mapper.SysUserMapper;
import com.snow.system.service.ISysConfigService;
import com.snow.system.service.impl.SysSocialUserServiceImpl;
import com.snow.system.service.impl.SysUserServiceImpl;
import me.zhyd.oauth.cache.AuthDefaultStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDingTalkRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-28 10:12
 **/
@Controller
@RequestMapping("/third/oauth")
public class ThirdOauthController extends BaseController {

    @Autowired
    private ISysConfigService iSysConfigService;

    @Resource
    private SysUserMapper userMapper;



    /**
     * 认证授权
     *
     * @param source
     * @throws IOException
     */
    @GetMapping("/toDingPage/{source}")
    @ResponseBody
    public void renderAuth(@PathVariable("source") String source) throws IOException
    {
        String appId= iSysConfigService.selectConfigByKey("ding.login.appid");
        String appSecret= iSysConfigService.selectConfigByKey("ding.login.appSecret");
        String url="http://workflow.vaiwan.com/third/oauth/dingTalkLogin";

        AuthRequest authRequest = new AuthDingTalkRequest(AuthConfig.builder()
                .clientId(appId)
                .clientSecret(appSecret)
                .redirectUri(url)
                .build());
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        ServletUtils.getResponse().sendRedirect(authorizeUrl);
    }

    /**
     * 回调结果
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/dingTalkLogin")
    public Object callbackAuth(@PathVariable("source") String source, AuthCallback callback, HttpServletRequest request)
    {

        if (StringUtils.isEmpty(source))
        {
            return new ModelAndView("error/unauth");
        }

        String appId= iSysConfigService.selectConfigByKey("ding.login.appid");
        String appSecret= iSysConfigService.selectConfigByKey("ding.login.appSecret");
        String url="http://workflow.vaiwan.com/third/oauth/dingTalkLogin";
        AuthRequest authRequest = new AuthDingTalkRequest(AuthConfig.builder()
                .clientId(appId)
                .clientSecret(appSecret)
                .redirectUri(url)
                .build());
        AuthResponse<AuthUser> response = authRequest.login(callback);
        if (response.ok())
        {
            if (SecurityUtils.getSubject() != null && SecurityUtils.getSubject().getPrincipal() != null)
            {
                SysUser user = userMapper.selectAuthUserByUuid(source + response.getData().getUuid());
                if (StringUtils.isNotNull(user))
                {
                    return redirect("/index");
                }
                // 若已经登录则直接绑定系统账号
                SysAuthUser authUser = new SysAuthUser();
                authUser.setAvatar(response.getData().getAvatar());
                authUser.setUuid(source + response.getData().getUuid());
                authUser.setUserId(ShiroUtils.getUserId());
                authUser.setUserName(response.getData().getNickname());
                authUser.setLoginName(response.getData().getUsername());
                authUser.setEmail(response.getData().getEmail());
                authUser.setSource(source);
                userMapper.insertAuthUser(authUser);
                return redirect("/index");
            }
            SysUser user = userMapper.selectAuthUserByUuid(source + response.getData().getUuid());
            if (StringUtils.isNotNull(user))
            {
                Subject subject = SecurityUtils.getSubject();
                UserToken token = new UserToken(user.getLoginName(), LoginType.NOPASSWD);
                subject.login(token);
                return redirect("/index");
            }
            else
            {
                return new ModelAndView("error/bind");
            }
        }
        return new ModelAndView("error/404");
    }

    /**
     * 检查是否授权
     */
    @PostMapping("/checkAuthUser")
    @ResponseBody
    public AjaxResult checkAuthUser(SysAuthUser authUser)
    {
        Long userId = ShiroUtils.getUserId();
        String source = authUser.getSource();
        if (userMapper.checkAuthUser(userId, source) > 0)
        {
            return error(source + "平台账号已经绑定");
        }
        return AjaxResult.success();
    }

    /**
     * 取消授权
     */
    @PostMapping("/unlock")
    @ResponseBody
    public AjaxResult unlockAuth(SysAuthUser authUser)
    {
        return toAjax(userMapper.deleteAuthUser(authUser.getAuthId()));
    }

}
