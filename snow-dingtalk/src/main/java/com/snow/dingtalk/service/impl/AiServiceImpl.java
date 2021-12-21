package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiOcrStructuredRecognizeRequest;
import com.dingtalk.api.response.OapiOcrStructuredRecognizeResponse;
import com.snow.common.exception.SyncDataException;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.request.OcrRecognizeRequest;
import com.snow.dingtalk.service.AiService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-29 17:16
 **/
@Service
@Slf4j
public class AiServiceImpl extends BaseService implements AiService {


    @Override
    public OapiOcrStructuredRecognizeResponse.OcrStructuredResult ocrRecognize(OcrRecognizeRequest ocrRecognizeRequest) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/ocr/structured/recognize");
        OapiOcrStructuredRecognizeRequest req = new OapiOcrStructuredRecognizeRequest();
        req.setType(ocrRecognizeRequest.getType());
        req.setImageUrl(ocrRecognizeRequest.getImageUrl());

        try {
            OapiOcrStructuredRecognizeResponse response = client.execute(req, getDingTalkToken());
            if(response.getErrcode()!=0){
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
            return response.getResult();
        } catch (ApiException e) {
            log.error("图片文字识别异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }
}
