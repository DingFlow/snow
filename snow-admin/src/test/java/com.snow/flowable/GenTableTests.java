package com.snow.flowable;

import com.snow.JunitTestApplication;
import com.snow.generator.service.impl.GenTableServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/12/24 11:21
 */
@Slf4j
public class GenTableTests extends JunitTestApplication {
    @Autowired
    private GenTableServiceImpl genTableServiceImpl;

    @Test
    public void createMenu(){
        genTableServiceImpl.createMenu("sys_menu_c");
    }
}
