package com.snow.web.controller.dingtalk;

import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import com.dingtalk.api.response.OapiUserGetbyunionidResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.StringUtils;
import com.snow.dingtalk.service.UserService;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysConfigService;
import com.snow.system.service.impl.SysUserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    @Autowired
    private UserService userService;

    @Autowired
    private SysUserServiceImpl sysUserService;

    /**
     * 跳转钉钉授权页面
     */
    @GetMapping("/toDingPage")
    public String toDingPage()
    {

        String appId= iSysConfigService.selectConfigByKey("ding.login.appid");
        StringBuilder url=new StringBuilder("https://oapi.dingtalk.com/connect/qrconnect?appid=");
        url.append(appId).append("&response_type=code&scope=snsapi_login&state=STATE&redirect_uri=");
        url.append("http://workflow.vaiwan.com/third/oauth/dingTalkLogin");

        return redirect(url.toString());

    }

    @RequestMapping("/dingTalkLogin")
    @ResponseBody
    public AjaxResult dingTalkLogin(String code)
    {
        OapiSnsGetuserinfoBycodeResponse.UserInfo userInfoByCode = userService.getUserInfoByCode(code);
        OapiUserGetbyunionidResponse.UserGetByUnionIdResponse userByUnionId = userService.getUserByUnionId(userInfoByCode.getUnionid());
        SysUser sysUser = sysUserService.selectUserByDingUserId(userByUnionId.getUserid());
        if(StringUtils.isNotNull(sysUser)){
           //todo 登录系统
        }else {
            return AjaxResult.error("非企业内用户不允许扫码登录");
        }

        return AjaxResult.success();
    }


}
