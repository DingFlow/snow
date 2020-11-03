package com.snow.web.controller.dingtalk;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/3 17:11
 */
@RestController
@Slf4j
public class DingTalkCallBack {


    /**
     * 钉钉回调
     * @param signature
     * @param timestamp
     * @param nonce
     * @param body
     * @return
     */
    @PostMapping(value = "/dingTalkCallBack")
    public Object dingCallback(
            @RequestParam(value = "signature") String signature,
            @RequestParam(value = "timestamp") Long timestamp,
            @RequestParam(value = "nonce") String nonce,
            @RequestBody(required = false) JSONObject body
    ) {
        String params = "signature:" + signature + " timestamp:" + timestamp + " nonce:" + nonce + " body:" + body;
        try {
            log.info("begin callback:" + params);
            DingTalkEncryptor dingTalkEncryptor = new DingTalkEncryptor(Constant.TOKEN, Constant.ENCODING_AES_KEY, Constant.SUITE_KEY);

            // 从post请求的body中获取回调信息的加密数据进行解密处理
            String encrypt = body.getString("encrypt");
            String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp.toString(), nonce, encrypt);
            JSONObject callBackContent = JSON.parseObject(plainText);

            // 根据回调事件类型做不同的业务处理
            String eventType = callBackContent.getString("EventType");
            if (EVENT_CHECK_CREATE_SUITE_URL.equals(eventType)) {
                log.info("验证新创建的回调URL有效性: " + plainText);
            } else if (EVENT_CHECK_UPADTE_SUITE_URL.equals(eventType)) {
                log.info("验证更新回调URL有效性: " + plainText);
            } else if (EVENT_SUITE_TICKET.equals(eventType)) {
                // suite_ticket用于用签名形式生成accessToken(访问钉钉服务端的凭证)，需要保存到应用的db。
                // 钉钉会定期向本callback url推送suite_ticket新值用以提升安全性。
                // 应用在获取到新的时值时，保存db成功后，返回给钉钉success加密串（如本demo的return）
                log.info("应用suite_ticket数据推送: " + plainText);
            } else if (EVENT_TMP_AUTH_CODE.equals(eventType)) {
                // 本事件应用应该异步进行授权开通企业的初始化，目的是尽最大努力快速返回给钉钉服务端。用以提升企业管理员开通应用体验
                // 即使本接口没有收到数据或者收到事件后处理初始化失败都可以后续再用户试用应用时从前端获取到corpId并拉取授权企业信息，进而初始化开通及企业。
                log.info("企业授权开通应用事件: " + plainText);
            } else {
                // 其他类型事件处理
            }

            // 返回success的加密信息表示回调处理成功
            return dingTalkEncryptor.getEncryptedMap("success", timestamp, nonce);
        } catch (Exception e) {
            //失败的情况，应用的开发者应该通过告警感知，并干预修复
            log.error("process callback fail." + params, e);
            return "fail";
        }
    }
}
