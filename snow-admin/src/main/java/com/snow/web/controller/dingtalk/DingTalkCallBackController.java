package com.snow.web.controller.dingtalk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;
import com.snow.dingtalk.common.EventNameEnum;
import com.snow.system.domain.DingtalkCallBack;
import com.snow.system.service.impl.DingtalkCallBackServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/3 17:11
 */
@RestController
@Slf4j
@RequestMapping("/dingTalk")
public class DingTalkCallBackController {

    @Autowired
    private DingtalkCallBackServiceImpl dingtalkCallBackService;

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
        DingtalkCallBack dingtalkCallBack=new DingtalkCallBack();
        dingtalkCallBack.setFlag(true);
        List<DingtalkCallBack> dingtalkCallBacks = dingtalkCallBackService.selectDingtalkCallBackList(dingtalkCallBack);
        if(!CollectionUtils.isEmpty(dingtalkCallBacks)){
            dingtalkCallBack=dingtalkCallBacks.get(0);
        }else {
            return "fail";
        }
        String params = "signature:" + signature + " timestamp:" + timestamp + " nonce:" + nonce + " body:" + body;
        try {
            log.info("begin callback:" + params);
            DingTalkEncryptor dingTalkEncryptor = new DingTalkEncryptor(dingtalkCallBack.getToken(),dingtalkCallBack.getAesKey(),dingtalkCallBack.getCorpId());

            // 从post请求的body中获取回调信息的加密数据进行解密处理
            String encrypt = body.getString("encrypt");
            String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp.toString(), nonce, encrypt);
            JSONObject callBackContent = JSON.parseObject(plainText);

            // 根据回调事件类型做不同的业务处理
            String eventType = callBackContent.getString("EventType");

            if (EventNameEnum.org_dept_create.equals(eventType)) {
                log.info("部门创建回调数据: " + callBackContent);
            } else if (EventNameEnum.org_dept_modify.equals(eventType)) {
                log.info("验证更新回调URL有效性: " + plainText);
            } else if (EventNameEnum.org_dept_remove.equals(eventType)) {
                // suite_ticket用于用签名形式生成accessToken(访问钉钉服务端的凭证)，需要保存到应用的db。
                // 钉钉会定期向本callback url推送suite_ticket新值用以提升安全性。
                // 应用在获取到新的时值时，保存db成功后，返回给钉钉success加密串（如本demo的return）
                log.info("应用suite_ticket数据推送: " + plainText);
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
