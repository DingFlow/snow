package com.snow.common.utils;

import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.request.*;

/**
 * 认证授权工具类
 * 
 * @author ruoyi
 */
public class AuthUtils
{
    @SuppressWarnings("deprecation")
    public static AuthRequest getAuthRequest(String source, String clientId, String clientSecret, String redirectUri,
            AuthStateCache authStateCache)
    {
        AuthRequest authRequest = null;
        switch (source.toLowerCase())
        {
            case "dingtalk":
                authRequest = new AuthDingTalkRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "baidu":
                authRequest = new AuthBaiduRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "weibo":
                authRequest = new AuthWeiboRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "coding":
                authRequest = new AuthCodingRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).codingGroupName("").build(), authStateCache);
                break;
            case "oschina":
                authRequest = new AuthOschinaRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "alipay":
                // 支付宝在创建回调地址时，不允许使用localhost或者127.0.0.1，所以这儿的回调地址使用的局域网内的ip
                authRequest = new AuthAlipayRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .alipayPublicKey("").redirectUri(redirectUri).build(), authStateCache);
                break;
            case "qq":
                authRequest = new AuthQqRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "wechat_open":
                authRequest = new AuthWeChatOpenRequest(AuthConfig.builder().clientId(clientId)
                        .clientSecret(clientSecret).redirectUri(redirectUri).build(), authStateCache);
                break;
            case "csdn":
                authRequest = new AuthCsdnRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "taobao":
                authRequest = new AuthTaobaoRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "douyin":
                authRequest = new AuthDouyinRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "linkedin":
                authRequest = new AuthLinkedinRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "microsoft":
                authRequest = new AuthMicrosoftRequest(AuthConfig.builder().clientId(clientId)
                        .clientSecret(clientSecret).redirectUri(redirectUri).build(), authStateCache);
                break;
            case "mi":
                authRequest = new AuthMiRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "toutiao":
                authRequest = new AuthToutiaoRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "teambition":
                authRequest = new AuthTeambitionRequest(AuthConfig.builder().clientId(clientId)
                        .clientSecret(clientSecret).redirectUri(redirectUri).build(), authStateCache);
                break;
            case "pinterest":
                authRequest = new AuthPinterestRequest(AuthConfig.builder().clientId(clientId)
                        .clientSecret(clientSecret).redirectUri(redirectUri).build(), authStateCache);
                break;
            case "renren":
                authRequest = new AuthRenrenRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "stack_overflow":
                authRequest = new AuthStackOverflowRequest(AuthConfig.builder().clientId(clientId)
                        .clientSecret(clientSecret).redirectUri(redirectUri).stackOverflowKey("").build(),
                        authStateCache);
                break;
            case "huawei":
                authRequest = new AuthHuaweiRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "wechat_enterprise":
                authRequest = new AuthWeChatEnterpriseRequest(AuthConfig.builder().clientId(clientId)
                        .clientSecret(clientSecret).redirectUri(redirectUri).agentId("").build(), authStateCache);
                break;
            case "kujiale":
                authRequest = new AuthKujialeRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "gitlab":
                authRequest = new AuthGitlabRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "meituan":
                authRequest = new AuthMeituanRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "eleme":
                authRequest = new AuthElemeRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build());
                break;
            case "wechat_mp":
                authRequest = new AuthWeChatMpRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            case "aliyun":
                authRequest = new AuthAliyunRequest(AuthConfig.builder().clientId(clientId).clientSecret(clientSecret)
                        .redirectUri(redirectUri).build(), authStateCache);
                break;
            default:
                break;
        }
        if (null == authRequest)
        {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }
}
