package com.snow.dingtalk.common;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.snow.common.utils.StringUtils;
import com.taobao.api.ApiException;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/16 10:46
 */
@Service
public class BaseService {
    public final String TOKEN="token";
    /**
     * 获取token
     * @return
     */
    public String getDingTalkToken(){
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(7100);
        if(StringUtils.isEmpty(timedCache.get(TOKEN))){
            DefaultDingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_TOKEN_URL);
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey("dingjidrzgnmznpofira");
            request.setAppsecret("IuKf2e8Z0stvf8LPMfHy_Im6sTi3G1mh0Jn0k4xuEnOhOyiLfKogwYympDVcRGK4");
            request.setHttpMethod("GET");
            try {
                OapiGettokenResponse response = client.execute(request);
                if(response.getErrcode()==0){
                    timedCache.put( TOKEN,response.getAccessToken());
                    return response.getAccessToken();
                }else {
                    //记录获取token失败日志
                    return null;
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
            return null;
        }else {
            return timedCache.get(TOKEN);
        }

    }

}
