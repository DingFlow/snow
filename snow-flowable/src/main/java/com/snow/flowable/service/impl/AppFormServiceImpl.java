package com.snow.flowable.service.impl;

import com.alibaba.fastjson.JSON;

import com.snow.flowable.domain.AppForm;
import com.snow.flowable.service.AppFormService;
import com.snow.system.domain.SysOaLeave;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/12/7 10:20
 */
@Service
@Slf4j
public class AppFormServiceImpl implements AppFormService {

    @Override
    public <A extends AppForm> A getAppFrom(String classInfoJson,String className) {
        return JSON.parseObject(classInfoJson, getFormClass(className));
    }

    @Override
    public <A extends AppForm> A getAppFrom(String classInfoJson,Class<A> className) {
        return JSON.parseObject(classInfoJson, className);
    }

    private <A> Class<A> getFormClass(String className) {
        try {
            return (Class<A>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception{
        Class<SysOaLeave> sysOaLeaveClass = SysOaLeave.class;
        Class<?> sysOaLeaveForm = Class.forName("com.snow.flowable.domain.leave.SysOaLeaveForm");
        System.out.println(sysOaLeaveForm);
    }
}
