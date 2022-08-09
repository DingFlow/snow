package com.snow.dingtalk.model.client;

import com.snow.common.exception.DingTalkApiException;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;

/**
 * @author qimingjin
 * @Title: 系统钉钉请求对象接口
 * @Description:
 * @date 2022/3/15 13:21
 */
public interface SnowDingTalkClient {

    default  <T extends TaobaoResponse> T execute(TaobaoRequest<T> request) throws ApiException{
        throw new DingTalkApiException("调用钉钉接口异常");
    }

    default  String getAccessToken() {
        throw new DingTalkApiException("调用钉钉接口获取token异常");
    }

    default   <T extends TaobaoResponse> T execute(TaobaoRequest<T> request,String url) throws ApiException {
        throw new DingTalkApiException("调用钉钉接口异常");
    }

}
