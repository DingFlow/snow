package com.snow.common.annotation;

import com.snow.common.enums.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * 
 * @author snow
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DingTalkSyncLog
{

    /**
     * 模块描述
     */

    public DingTalkListenerType dingTalkListenerType();


    /**
     *钉钉同步方式
     */
    public DingTalkSyncType dingTalkSyncType() default DingTalkSyncType.AUTOMATIC;

    /**
     * 请求钉钉的URL
     * @return
     */
    public String dingTalkUrl() default"";

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;
}
