package com.snow.common.annotation;

import com.snow.common.enums.DingTalkLogType;
import com.snow.common.enums.DingTalkSyncType;
import com.snow.common.enums.SyncLogType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * 
 * @author snow
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DingTalkLog
{

    /**
     * 日志类型
     */

    public DingTalkLogType dingTalkLogType();


    /**
     *钉钉同步方式
     */
    @Deprecated
    public DingTalkSyncType dingTalkSyncType() default DingTalkSyncType.AUTOMATIC;

    /**
     * 请求钉钉的URL
     * @return
     */
    public String dingTalkUrl() default"";

    /**
     * 同步日志类型
     * @return
     */
    @Deprecated
    public SyncLogType syncLogTpye() default SyncLogType.SYNC_DINGTALK;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;
}
