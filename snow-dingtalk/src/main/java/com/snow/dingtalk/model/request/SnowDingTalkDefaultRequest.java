package com.snow.dingtalk.model.request;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.snow.common.constant.CacheConstants;
import com.snow.common.constant.Constants;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.CacheUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2022/3/15 13:25
 */
public abstract class SnowDingTalkDefaultRequest implements SnowDingTalkRequest{


    /**
     * 请求钉钉接口
     * @param request 请求对象
     * @param url 请求钉钉接口url路径
     * @param <T> 具体请求参数泛型
     * @return 请求返回的参数对象
     * @throws ApiException
     */
    @Override
    public  <T extends TaobaoResponse> T execute(TaobaoRequest<T> request,String url) throws ApiException{
        DefaultDingTalkClient client = new DefaultDingTalkClient(url);
        return client.execute(request,this.getAccessToken());
    }

    /**
     * 获取token
     * @return 返回token
     */
    @Override
    public String getAccessToken() {
        DefaultDingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_TOKEN_URL);
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(CacheUtils.getSysConfig(CacheConstants.ENTERPRICE_APP_KEY).toString());
        request.setAppsecret(CacheUtils.getSysConfig(CacheConstants.ENTERPRICE_APP_SECRET).toString());
        request.setHttpMethod(Constants.GET);
        try {
            OapiGettokenResponse response = client.execute(request);
            if(response.getErrcode()==0){
                return response.getAccessToken();
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            throw new SyncDataException(JSON.toJSONString(request),e.getMessage());
        }
    }
}
