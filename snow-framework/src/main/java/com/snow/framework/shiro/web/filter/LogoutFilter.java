package com.snow.framework.shiro.web.filter;

import java.io.Serializable;
import java.util.Deque;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.snow.common.constant.UserConstants;
import com.snow.common.enums.UserType;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.snow.common.constant.Constants;
import com.snow.common.constant.ShiroConstants;
import com.snow.common.utils.MessageUtils;
import com.snow.common.utils.StringUtils;
import com.snow.framework.manager.AsyncManager;
import com.snow.framework.manager.factory.AsyncFactory;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysUser;

/**
 * 退出过滤器
 * 
 * @author snow
 */
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter
{
    private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);

    /**
     * 退出后重定向的地址
     */
    private String loginUrl;

    private Cache<String, Deque<Serializable>> cache;

    public String getLoginUrl()
    {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl)
    {
        this.loginUrl = loginUrl;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        try {
            Subject subject = getSubject(request, response);
            String redirectUrl = getRedirectUrl(request, response, subject);

            SysUser user = ShiroUtils.getSysUser();
            if (StringUtils.isNotNull(user)) {
                String loginName = user.getLoginName();
                // 记录用户退出日志
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
                // 清理缓存
                cache.remove(loginName);
                // 退出登录
                subject.logout();
                String userType = user.getUserType();
                if(userType.equals(UserType.SYS_USER_TYPE.getCode())){
                    issueRedirect(request, response, redirectUrl);
                }else if (userType.equals(UserType.FRONT_USER_TYPE.getCode())){
                    issueRedirect(request, response, UserConstants.FRONT_LOGIN_OUT_URL);
                }
            }
        } catch (Exception e) {
            log.error("Encountered session exception during logout.  This can generally safely be ignored.", e);
        }
        return false;
    }

    /**
     * 退出跳转URL
     */
    @Override
    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject)
    {
        String url = getLoginUrl();
        if (StringUtils.isNotEmpty(url))
        {
            return url;
        }
        return super.getRedirectUrl(request, response, subject);
    }

    // 设置Cache的key的前缀
    public void setCacheManager(CacheManager cacheManager)
    {
        // 必须和ehcache缓存配置中的缓存name一致
        this.cache = cacheManager.getCache(ShiroConstants.SYS_USERCACHE);
    }
}
