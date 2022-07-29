package com.snow.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.snow.common.annotation.Excel;
import com.snow.common.annotation.Sensitive;
import com.snow.common.core.domain.BaseEntity;
import com.snow.common.enums.SensitiveTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 回调事件对象 dingtalk_call_back
 *
 *  开发者可以使用HTTP的方式注册钉钉的回调事件，用于接收钉钉推送的消息。例如企业授权开通应用事件、通讯录变更事件等，可以让开发者更好的与钉钉集成。
 *
 * @author qimingjin
 * @date 2020-11-02
 */
@Data
public class DingtalkCallBack extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
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

    /** 启用标识*/
    private Integer enableFlag;

    /** 回调名称 */
    @Excel(name = "回调名称")
    private String callBackName;

    /**
     * 事件集合
     */
    @TableField(exist = false)
    private List<String> eventNameList;

    @TableField(exist = false)
    private Boolean flag=false;


}
