package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 社会化用户信息对象 sys_social_user
 * 
 * @author 没用的阿吉
 * @date 2021-03-29
 */
public class SysSocialUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户授权令牌 */
    @Excel(name = "用户授权令牌")
    private String accessToken;

    /** 第三方用户的授权令牌的有效期 */
    @Excel(name = "第三方用户的授权令牌的有效期")
    private Long expireIn;

    /** 刷新令牌 */
    @Excel(name = "刷新令牌")
    private String refreshToken;

    /** 第三方用户的 open id */
    @Excel(name = "第三方用户的 open id")
    private String openId;

    /** 第三方用户的 union id */
    @Excel(name = "第三方用户的 union id")
    private String unionId;

    /** 第三方用户授予的权限 */
    @Excel(name = "第三方用户授予的权限")
    private String scope;

    /** 用户的授权code */
    @Excel(name = "用户的授权code")
    private String code;

    /** 来源 */
    @Excel(name = "来源")
    private String source;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setAccessToken(String accessToken) 
    {
        this.accessToken = accessToken;
    }

    public String getAccessToken() 
    {
        return accessToken;
    }
    public void setExpireIn(Long expireIn) 
    {
        this.expireIn = expireIn;
    }

    public Long getExpireIn() 
    {
        return expireIn;
    }
    public void setRefreshToken(String refreshToken) 
    {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() 
    {
        return refreshToken;
    }
    public void setOpenId(String openId) 
    {
        this.openId = openId;
    }

    public String getOpenId() 
    {
        return openId;
    }
    public void setUnionId(String unionId) 
    {
        this.unionId = unionId;
    }

    public String getUnionId() 
    {
        return unionId;
    }
    public void setScope(String scope) 
    {
        this.scope = scope;
    }

    public String getScope() 
    {
        return scope;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setSource(String source) 
    {
        this.source = source;
    }

    public String getSource() 
    {
        return source;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("accessToken", getAccessToken())
            .append("expireIn", getExpireIn())
            .append("refreshToken", getRefreshToken())
            .append("openId", getOpenId())
            .append("unionId", getUnionId())
            .append("scope", getScope())
            .append("code", getCode())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("source", getSource())
            .toString();
    }
}
