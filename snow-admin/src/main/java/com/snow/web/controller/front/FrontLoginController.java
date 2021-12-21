package com.snow.web.controller.front;

import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.StringUtils;
import com.snow.framework.shiro.auth.LoginType;
import com.snow.framework.shiro.auth.UserToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/7/9 9:05
 */
@Controller
@RequestMapping("/front")
public class FrontLoginController extends BaseController {

    /**
     * 登录
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe){
        UserToken token = new UserToken(username, password, LoginType.OFFICIAL_WEBSITE, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return success();
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }
}
