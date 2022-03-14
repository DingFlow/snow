package com.snow.web.controller.dingtalk;

import com.snow.common.constant.Constants;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.CacheUtils;
import com.snow.common.utils.ServletUtils;
import com.snow.common.utils.StringUtils;
import com.snow.framework.shiro.auth.LoginType;
import com.snow.framework.shiro.auth.UserToken;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysAuthUser;
import com.snow.system.domain.SysUser;
import com.snow.system.mapper.SysUserMapper;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthAlipayRequest;
import me.zhyd.oauth.request.AuthDingTalkRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatEnterpriseQrcodeRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-28 10:12
 **/
@Controller
@RequestMapping("/third/oauth")
public class ThirdOauthController extends BaseController {

    @Resource
    private SysUserMapper userMapper;



    /**
     * 认证授权
     *
     * @param source
     * @throws IOException
     */
    @GetMapping("/toPage/{source}")
    @ResponseBody
    public void renderAuth(@PathVariable("source") String source) throws IOException
    {
        AuthRequest authRequest =null;
                switch (source){
                   case "dingtalk":
                       authRequest= getDingTalkAuthRequest();
                       break;

                   case "weChart":
                       authRequest=getWeChatAuthRequest();
                       break;

                   case "alipay":
                        authRequest = getAlipayAuthRequest();
                        break;
                }

        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        ServletUtils.getResponse().sendRedirect(authorizeUrl);
    }


    /**
     * 微信回调登录
     * @param callback
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/weChartLogin")
    public Object weChartLogin(AuthCallback callback, HttpServletRequest request)
    {
        return thirdLogin("weChart",getWeChatAuthRequest(),callback);
    }

    /**
     * 钉钉回调
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/dingTalkLogin")
    public Object callbackAuth(AuthCallback callback, HttpServletRequest request)
    {
       return thirdLogin("dingtalk",getDingTalkAuthRequest(),callback);
    }


    /**
     * 支付宝登录
     * @param callback
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/alipayLogin")
    public Object alipayLogin(AuthCallback callback, HttpServletRequest request)
    {
        return thirdLogin("alipay",getAlipayAuthRequest(),callback);
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

    /**
     * 构建钉钉AuthRequest
     * @return
     */
    private AuthRequest getDingTalkAuthRequest() {
        Object appId = CacheUtils.getSysConfig("sys_config:ding.login.appid");
        Object appSecret = CacheUtils.getSysConfig("sys_config:ding.login.appSecret");
        Object redirectUri = CacheUtils.getSysConfig("sys_config:ding.login.redirectUri");
        return new AuthDingTalkRequest(AuthConfig.builder()
                .clientId(String.valueOf(appId))
                .clientSecret(String.valueOf(appSecret))
                .redirectUri(String.valueOf(redirectUri))
                .build());
    }

    /**
     * 构建企业微信AuthRequest
     * @return
     */
    private AuthRequest getWeChatAuthRequest() {
        Object agentId = CacheUtils.getSysConfig("sys_config:wechart.login.agentId");
        Object clientId = CacheUtils.getSysConfig("sys_config:wechart.login.clientId");
        Object appSecret = CacheUtils.getSysConfig("sys_config:wechart.login.appSecret");
        Object redirectUri = CacheUtils.getSysConfig("sys_config:wechart.login.redirectUri");
        return new AuthWeChatEnterpriseQrcodeRequest(AuthConfig.builder()
                .clientId(String.valueOf(clientId))
                .clientSecret(String.valueOf(appSecret))
                .redirectUri(String.valueOf(redirectUri))
                .agentId(String.valueOf(agentId))
                .build());
    }

    private AuthRequest getAlipayAuthRequest() {
        Object appId = CacheUtils.getSysConfig("sys_config:alipay.login.appId");
        Object redirectUri = CacheUtils.getSysConfig("sys_config:alipay.login.redirectUri");
        return new AuthAlipayRequest(AuthConfig.builder()
                .clientId(String.valueOf(appId))
                .clientSecret(Constants.ALIPAY_RSA_PRIVATE_KEY)
                .alipayPublicKey(Constants.ALIPAY_PUBLIC_KEY)
                .redirectUri(String.valueOf(redirectUri))
                .build());
    }

    /**
     * 构建登录
     * @param source 来源
     * @param authRequest 请求参数
     * @param callback 请求参数
     * @return
     */
    private Object thirdLogin(String source, AuthRequest authRequest,AuthCallback callback){
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
                authUser.setLoginName(ShiroUtils.getLoginName());
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

}
