package com.snow.common.exception;

/**
 * @author qimingjin
 * @Title: 钉钉api异常
 * @Description:
 * @date 2022/1/25 14:35
 */
public class DingTalkApiException extends RuntimeException{
    private static final long serialVersionUID = 608263975019811552L;

    private  String requestParam;

    protected final String message;

    public DingTalkApiException(String requestParam, String message) {
        this.requestParam = requestParam;
        this.message = message;
    }
    public DingTalkApiException(String message)
    {
        this.message = message;
    }

    public DingTalkApiException(String requestParam, String message, Throwable e) {
        super(message, e);
        this.message = message;
        this.requestParam = requestParam;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public String getRequestParam() {
        return requestParam;
    }
}
