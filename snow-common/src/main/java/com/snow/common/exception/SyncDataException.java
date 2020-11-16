package com.snow.common.exception;

/**
 * @author qimingjin
 * @Title: 同步数据异常
 * @Description:
 * @date 2020/11/13 14:54
 */
public class SyncDataException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private  String requestParam;

    protected final String message;

    public SyncDataException(String requestParam, String message)
    {
        this.requestParam = requestParam;
        this.message = message;
    }
    public SyncDataException(String message)
    {
        this.message = message;
    }

    public SyncDataException(String requestParam, String message, Throwable e)
    {
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
