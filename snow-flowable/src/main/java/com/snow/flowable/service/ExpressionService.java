package com.snow.flowable.service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/24 10:08
 */
public interface ExpressionService {
    Object getValue(String processInstanceId, String exp);

   // <T> T getValue(String processInstanceId, String exp, Class<T> clazz);
}
