package com.snow.dingtalk.common;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.snow.common.constant.Constants;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.StringUtils;
import com.snow.system.domain.SysOperLog;
import com.snow.system.service.ISysConfigService;
import com.snow.system.service.ISysOperLogService;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/16 10:46
 */
public class BaseService {

    public static final String TOKEN="token";
    @Autowired
    private ISysConfigService isysConfigService;
    @Autowired
    private ISysOperLogService iSysOperLogService;

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
            request.setAppkey(isysConfigService.selectConfigByKey(Constants.ENTERPRICE_APP_KEY));
            request.setAppsecret(isysConfigService.selectConfigByKey(Constants.ENTERPRICE_APP_SECRET));
            request.setHttpMethod(Constants.GET);
            try {
                OapiGettokenResponse response = client.execute(request);
                if(response.getErrcode()==0){
                    timedCache.put( TOKEN,response.getAccessToken());
                    return response.getAccessToken();
                }else {
                    //记录获取token失败日志
                    syncDingTalkErrorOperLog(BaseConstantUrl.GET_TOKEN_URL,response.getErrmsg(),"getDingTalkToken()");
                    return null;
                }
            } catch (ApiException e) {
                syncDingTalkErrorOperLog(BaseConstantUrl.GET_TOKEN_URL,e.getMessage(),"getDingTalkToken()");
                e.printStackTrace();
            }
            return null;
        }else {
            return timedCache.get(TOKEN);
        }

    }

    /**
     * 记录钉钉异常信息
     * @param url
     * @param errorMessage
     * @param method
     */
    public void syncDingTalkErrorOperLog(String url,String errorMessage,String method){
        SysOperLog sysOperLog=new SysOperLog();
        sysOperLog.setOperTime(new Date());
        sysOperLog.setErrorMsg(errorMessage);
        sysOperLog.setBusinessType(BusinessType.SYNCHRONIZATION.ordinal());
        sysOperLog.setOperName("系统自动记录");
        sysOperLog.setOperUrl(url);
        sysOperLog.setMethod(method);
        sysOperLog.setTitle("系统调用钉钉异常");
        iSysOperLogService.insertOperlog(sysOperLog);
    }

}
