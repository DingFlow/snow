package com.snow.web.controller.dingtalk;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.snow.common.constant.Constants;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.dingtalk.sync.ISyncSysInfo;
import com.snow.dingtalk.sync.SyncSysInfoFactory;
import com.snow.system.domain.DingtalkCallBack;
import com.snow.system.service.impl.DingtalkCallBackServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/3 17:11
 */
@RestController
@Slf4j
@RequestMapping("/dingTalk")
public class CallBackController {

    @Autowired
    private DingtalkCallBackServiceImpl dingtalkCallBackService;

    @Value("${is.sync.dingtalk:false}")
    private Boolean isSyncDingTalk;

    private static final String EVENT_TYPE="EventType";

    private static final String ENCRYPT="encrypt";

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
        if(CollectionUtils.isEmpty(dingtalkCallBacks)){
            return Constants.CALL_BACK_FAIL_RETURN;
        }
        dingtalkCallBack=dingtalkCallBacks.get(0);
        try {
            log.info("\n @@begin callback------》 signature:{},timestamp:{},nonce:{},body:{}" ,signature,timestamp,nonce,body);
            DingTalkEncryptor dingTalkEncryptor = new DingTalkEncryptor(dingtalkCallBack.getToken(),dingtalkCallBack.getAesKey(),dingtalkCallBack.getCorpId());

            // 从post请求的body中获取回调信息的加密数据进行解密处理
            String encrypt = body.getString(ENCRYPT);
            String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp.toString(), nonce, encrypt);
            JSONObject callBackContent = JSON.parseObject(plainText);
            // 根据回调事件类型做不同的业务处理
            String eventType = callBackContent.getString(EVENT_TYPE);
            DingTalkListenerType type = DingTalkListenerType.getType(eventType);
            if(StringUtils.isEmpty(type)){
                return dingTalkEncryptor.getEncryptedMap(Constants.CALL_BACK_SUCCESS_RETURN, timestamp, nonce);
            }
            //测试回调URL事件，直接返回加密后的success即可
            if(eventType.equals(DingTalkListenerType.CALL_BACK_CHECK_URL.getInfo())){
                return dingTalkEncryptor.getEncryptedMap(Constants.CALL_BACK_SUCCESS_RETURN, timestamp, nonce);
            }
            //调用工厂模式异步处理数据
            if(isSyncDingTalk){
                SyncSysInfoFactory syncSysInfoFactory = new SyncSysInfoFactory();
                ISyncSysInfo iSyncSysInfo = syncSysInfoFactory.getSyncSysInfoService(type);
                iSyncSysInfo.SyncSysInfo(type, callBackContent);
            }
            // 返回success的加密信息表示回调处理成功
            return dingTalkEncryptor.getEncryptedMap(Constants.CALL_BACK_SUCCESS_RETURN, timestamp, nonce);
        } catch (Exception e) {
            log.error("@@dingTalkCallBack fail------》 signature:{},timestamp:{},nonce:{},body:{}" ,signature,timestamp,nonce,body, e);
            return Constants.CALL_BACK_FAIL_RETURN;
        }
    }



    /**
     * 接收钉钉dingFlow机器人消息
     * @return
     */
    @PostMapping(value = "/dingFlowRobot")
    public void dingFlowRobotCallback(@RequestBody(required = false) JSONObject body){
        log.info("dingFlowRobot"+body);

        //todo 校验是否是钉钉群发送过来的消息

    }


    /**
     * 测试给钉钉群发消息
     *
     * @param args
     */
    public static void main(String[] args){

        try {
            //钉钉机器人地址（配置机器人的webhook）
            String dingUrl = "";

            //是否通知所有人
            boolean isAtAll = false;
            //通知具体人的手机号码列表
            List<String> mobileList = Lists.newArrayList();

            //钉钉机器人消息内容
            String content ="TEST"+ "小哥，你好！";
            //组装请求内容
            String reqStr = buildReqStr(content, isAtAll, mobileList);

            //推送消息（http请求）
            String result = HttpUtil.post(dingUrl, reqStr);
            System.out.println("result == " + result);

        }catch (Exception e){
            e.printStackTrace();

        }

    }

    /**
     * 组装请求报文
     * @param content
     * @return
     */
    private static String buildReqStr(String content, boolean isAtAll, List<String> mobileList) {
        //消息内容
        Map<String, String> contentMap = Maps.newHashMap();
        contentMap.put("content", content);

        //通知人
        Map<String, Object> atMap = Maps.newHashMap();
        //1.是否通知所有人
        atMap.put("isAtAll", isAtAll);
        //2.通知具体人的手机号码列表
        atMap.put("atMobiles", mobileList);

        Map<String, Object> reqMap = Maps.newHashMap();
        reqMap.put("msgtype", "text");
        reqMap.put("text", contentMap);
        reqMap.put("at", atMap);

        return JSON.toJSONString(reqMap);
    }

}
