package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 回调事件对象 dingtalk_call_back
 * 
 * @author qimingjin
 * @date 2020-11-02
 */
public class DingtalkCallBack extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 加解密需要用到的token，ISV(服务提供商)推荐使用注册套件时填写的token，普通企业可以随机填写 */
    @Excel(name = "加解密需要用到的token，ISV(服务提供商)推荐使用注册套件时填写的token，普通企业可以随机填写")
    private String token;

    /** 数据加密密钥。用于回调数据的加密，长度固定为43个字符，从a-z, A-Z, 0-9共62个字符中选取,您可以随机生成，ISV(服务提供商)推荐使用注册套件时填写的EncodingAESKey */
    @Excel(name = "数据加密密钥。用于回调数据的加密，长度固定为43个字符，从a-z, A-Z, 0-9共62个字符中选取,您可以随机生成，ISV(服务提供商)推荐使用注册套件时填写的EncodingAESKey")
    private String aesKey;

    /** 接收事件回调的url，必须是公网可以访问的url地址 */
    @Excel(name = "接收事件回调的url，必须是公网可以访问的url地址")
    private String url;

    /** app_key */
    private String appKey;

    /** app_secret */
    private String appSecret;

    /** 删除表示 */
    private Integer delFlag;

    /** 回调名称 */
    @Excel(name = "回调名称")
    private String callBackName;

    /**
     * 部门事件集合
     */
    private List<String> eventNameList;

    private Boolean flag=false;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setToken(String token) 
    {
        this.token = token;
    }

    public String getToken() 
    {
        return token;
    }
    public void setAesKey(String aesKey) 
    {
        this.aesKey = aesKey;
    }

    public String getAesKey() 
    {
        return aesKey;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setAppKey(String appKey) 
    {
        this.appKey = appKey;
    }

    public String getAppKey() 
    {
        return appKey;
    }
    public void setAppSecret(String appSecret) 
    {
        this.appSecret = appSecret;
    }

    public String getAppSecret() 
    {
        return appSecret;
    }
    public void setDelFlag(Integer delFlag) 
    {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag() 
    {
        return delFlag;
    }
    public void setCallBackName(String callBackName) 
    {
        this.callBackName = callBackName;
    }

    public String getCallBackName() 
    {
        return callBackName;
    }

    public List<String> getEventNameList() {
        return eventNameList;
    }

    public void setEventNameList(List<String> eventNameList) {
        this.eventNameList = eventNameList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("token", getToken())
            .append("aesKey", getAesKey())
            .append("url", getUrl())
            .append("appKey", getAppKey())
            .append("appSecret", getAppSecret())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("callBackName", getCallBackName())
            .toString();
    }
}
