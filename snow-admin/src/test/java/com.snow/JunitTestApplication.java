package com.snow;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/10/27 9:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JunitTestApplication {

    @Before
    public void init() {
        System.out.println("SNOW_TEST begin--------------------------------------------------------");
    }

    @After
    public void after() {
        System.out.println("SNOW_TEST end----------------------------------------------------------");
    }
}
