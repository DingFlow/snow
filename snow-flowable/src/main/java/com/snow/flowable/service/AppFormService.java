package com.snow.flowable.service;

import com.snow.flowable.domain.AppForm;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/12/7 10:19
 */
public interface AppFormService {

    /**
     * 从流程中获取表单数据
     * @param processInstanceId
     * @param <A>
     * @return
     */
    <A extends AppForm> A getAppFrom(String processInstanceId);
}
