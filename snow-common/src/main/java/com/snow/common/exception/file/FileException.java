package com.snow.common.exception.file;

import com.snow.common.exception.base.BaseException;

/**
 * 文件信息异常类
 * 
 * @author snow
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
