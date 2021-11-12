package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.snow.common.annotation.Sensitive;
import com.snow.common.core.domain.BaseEntity;
import com.snow.common.enums.SensitiveTypeEnum;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 回调事件对象 dingtalk_call_back
 * 
 * @author qimingjin
 * @date 2020-11-02
 */
@Data
public class DingtalkCallBack extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 加解密需要用到的token，ISV(服务提供商)推荐使用注册套件时填写的token，普通企业可以随机填写 */
    @Excel(name = "加解密需要用到的token，ISV(服务提供商)推荐使用注册套件时填写的token，普通企业可以随机填写")
    @Sensitive(type = SensitiveTypeEnum.KEY)
    private String token;

    /** 数据加密密钥。用于回调数据的加密，长度固定为43个字符，从a-z, A-Z, 0-9共62个字符中选取,您可以随机生成，ISV(服务提供商)推荐使用注册套件时填写的EncodingAESKey */
    @Excel(name = "数据加密密钥。用于回调数据的加密，长度固定为43个字符，从a-z, A-Z, 0-9共62个字符中选取,您可以随机生成，ISV(服务提供商)推荐使用注册套件时填写的EncodingAESKey")
    @Sensitive(type = SensitiveTypeEnum.KEY)
    private String aesKey;

    /** 接收事件回调的url，必须是公网可以访问的url地址 */
    @Excel(name = "接收事件回调的url，必须是公网可以访问的url地址")
    private String url;

    /**企业内部应用 */
    private String corpId;

    /** 第三方企业应用 */
    private String suiteKey;

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


}
