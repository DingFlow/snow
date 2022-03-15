package com.snow.dingtalk.model.request;

import com.snow.common.exception.DingTalkApiException;
import com.snow.dingtalk.common.BasePath;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2022/3/15 13:21
 */
public interface SnowDingTalkRequest {

    default String getAccessToken() {
        throw new DingTalkApiException("调用钉钉接口获取token异常");
    }

    default  <T extends TaobaoResponse> T execute(TaobaoRequest<T> request,String url) throws ApiException {
        throw new DingTalkApiException("调用钉钉接口异常");
    }

}
