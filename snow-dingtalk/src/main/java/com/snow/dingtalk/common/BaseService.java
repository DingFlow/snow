package com.snow.dingtalk.common;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.snow.common.constant.Constants;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.system.domain.SysOperLog;
import com.snow.system.service.impl.SysConfigServiceImpl;
import com.snow.system.service.impl.SysOperLogServiceImpl;
import com.taobao.api.ApiException;

import java.util.Date;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/16 10:46
 */
public class BaseService {

    public static final String TOKEN="dingtalk_token";

    public static final String TOKENV2="dingtalk_token_V2";

    private SysConfigServiceImpl sysConfigService=SpringUtils.getBean("sysConfigServiceImpl");

    private SysOperLogServiceImpl iSysOperLogService=SpringUtils.getBean("sysOperLogServiceImpl");

    /**
     * 获取token
     * @return
     */
    public String getDingTalkToken(){
        //创建缓存，缓存默认是7100S
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(7100);
        if(StringUtils.isEmpty(timedCache.get(TOKEN))){
            DefaultDingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_TOKEN_URL);
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(sysConfigService.selectConfigByKey(Constants.ENTERPRICE_APP_KEY));
            request.setAppsecret(sysConfigService.selectConfigByKey(Constants.ENTERPRICE_APP_SECRET));
            request.setHttpMethod(Constants.GET);
            try {
                OapiGettokenResponse response = client.execute(request);
                if(response.getErrcode()==0){
                    timedCache.put(TOKEN,response.getAccessToken());
                    syncDingTalkSuccessOperLog(BaseConstantUrl.GET_TOKEN_URL,response.getMessage(),"getDingTalkToken()", com.alibaba.fastjson.JSON.toJSONString(request));
                    return response.getAccessToken();
                }else {
                    //记录获取token失败日志
                    syncDingTalkErrorOperLog(BaseConstantUrl.GET_TOKEN_URL,response.getErrmsg(),"getDingTalkToken()", com.alibaba.fastjson.JSON.toJSONString(request));
                    return null;
                }
            } catch (ApiException e) {
                syncDingTalkErrorOperLog(BaseConstantUrl.GET_TOKEN_URL,e.getMessage(),"getDingTalkToken()",com.alibaba.fastjson.JSON.toJSONString(request));
                e.printStackTrace();
            }
            return null;
        }else {
            return timedCache.get(TOKEN);
        }

    }

    /**
     * 钉钉新版服务费token获取
     * @return token值
     */
    public String getDingTalkTokenV2(){
        //创建缓存，缓存默认是7100S
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(7100);
        if(StringUtils.isEmpty(timedCache.get(TOKENV2))) {
            try {
                GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest()
                        .setAppKey(sysConfigService.selectConfigByKey(Constants.ENTERPRICE_APP_KEY))
                        .setAppSecret(sysConfigService.selectConfigByKey(Constants.ENTERPRICE_APP_SECRET));
                try {
                    GetAccessTokenResponse accessToken = createClient().getAccessToken(getAccessTokenRequest);
                    timedCache.put(TOKENV2,accessToken.getBody().getAccessToken());
                    return accessToken.getBody().getAccessToken();
                } catch (Exception exception) {
                    TeaException err = new TeaException(exception.getMessage(), exception);
                    if (!Common.empty(err.code) && !Common.empty(err.message)) {
                        // err 中含有 code 和 message 属性，可帮助开发定位问题
                        syncDingTalkErrorOperLog(BaseConstantUrl.GET_TOKEN_URL,err.getMessage(),"getDingTalkToken()", com.alibaba.fastjson.JSON.toJSONString(getAccessTokenRequest));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }else {
            return timedCache.get(TOKEN);
        }
    }

    /**
     * 创建createClient
     * @return
     * @throws Exception
     */
    public  Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }

    /**
     * 记录钉钉异常信息
     * @param url
     * @param errorMessage
     * @param method
     */
    public void syncDingTalkErrorOperLog(String url,String errorMessage,String method,String operParam){
        SysOperLog sysOperLog=new SysOperLog();
        sysOperLog.setOperTime(new Date());
        sysOperLog.setErrorMsg(errorMessage);
        sysOperLog.setBusinessType(BusinessType.SYNCHRONIZATION.ordinal());
        sysOperLog.setOperName("系统自动记录");
        sysOperLog.setOperUrl(url);
        sysOperLog.setMethod(method);
        sysOperLog.setOperParam(operParam);
        sysOperLog.setTitle("系统调用钉钉异常");
        sysOperLog.setStatus(1);
        iSysOperLogService.insertOperlog(sysOperLog);
    }

    /**
     * 成功
     * @param url
     * @param successMessage
     * @param method
     * @param operParam
     */
    public void syncDingTalkSuccessOperLog(String url,String successMessage,String method,String operParam){
        SysOperLog sysOperLog=new SysOperLog();
        sysOperLog.setOperTime(new Date());
        sysOperLog.setBusinessType(BusinessType.SYNCHRONIZATION.ordinal());
        sysOperLog.setJsonResult(successMessage);
        sysOperLog.setOperName("系统自动记录");
        sysOperLog.setOperUrl(url);
        sysOperLog.setMethod(method);
        sysOperLog.setOperParam(operParam);
        sysOperLog.setTitle("调用钉钉成功");
        sysOperLog.setStatus(0);
        iSysOperLogService.insertOperlog(sysOperLog);
    }
}
