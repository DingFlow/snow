package com.snow.flowable.service;

import com.snow.flowable.domain.AppForm;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/12/7 10:19
 */
public interface AppFormService {
    <A extends AppForm> A  getAppFrom(String classInfoJson,String className);
    <A extends AppForm> A getAppFrom(String classInfoJson,Class<A> className);
}
