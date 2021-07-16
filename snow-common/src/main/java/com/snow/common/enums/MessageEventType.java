package com.snow.common.enums;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-30 13:49
 **/
public enum  MessageEventType {
    TASK_TODO("TASK_TODO", "任务待办"),

    INNER_TASK_TODO("INNER_TASK_TODO", "站内信任务待办"),

    TASK_FINISH("TASK_FINISH", "任务完成"),

    SEND_EMAIL("SEND_EMAIL", "发送邮件"),

    MARK_READED("MARK_READED", "标记已读"),

    SEND_VISIT_LOG("SEND_VISIT_LOG", "发送拜访日志"),

    REGISTER_ACCOUNT_SUCCESS("REGISTER_ACCOUNT_SUCCESS", "成功注册账户"),
    ;



    private final String code;
    private final String info;

    MessageEventType(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }

    public static MessageEventType getType(String info) {
        for (MessageEventType messageEventType:MessageEventType.values()){
            if(messageEventType.getInfo().equals(info)){
                return messageEventType;
            }
        }
        return null;
    }

    public static MessageEventType getCode(String code) {
        for (MessageEventType messageEventType:MessageEventType.values()){
            if(messageEventType.getCode().equals(code)){
                return messageEventType;
            }
        }
        return null;
    }
}
