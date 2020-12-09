package com.snow.flowable.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import org.flowable.common.engine.impl.cfg.IdGenerator;

import cn.hutool.core.lang.Console;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author qimingjin
 * @Title: 修改flowAble id 生成器
 * @Description:
 * @date 2020/12/9 14:01
 */
public class FlowIdGenerator implements IdGenerator {

    @Override
    public synchronized String getNextId() {
        Snowflake snowflake = IdUtil.getSnowflake(2, 1);
        long id = snowflake.nextId();
        return String.valueOf(id);
    }

    //很多时候，我们需要简单模拟N个线程调用某个业务测试其并发状况，于是Hutool提供了一个简单的并发测试类——ConcurrencyTester。
    public static void main(String[] args) {

        ThreadPoolExecutor executor = ExecutorBuilder.create().
                setCorePoolSize(5).
                setMaxPoolSize(10).
                setWorkQueue(new LinkedBlockingQueue<>(100))
                .build();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    Snowflake snowflake = IdUtil.getSnowflake(2, 1);
                    long id = snowflake.nextId();
                    ThreadUtil.sleep(1000);
                    Console.log("{} test finished, delay: {}", Thread.currentThread().getName(), id);
                }
            }
        });
        executor.shutdown();

       /* ConcurrencyTester tester = ThreadUtil.concurrencyTest(100, () -> {
            // 测试的逻辑内容
            Snowflake snowflake = IdUtil.getSnowflake(2, 1);
            long id = snowflake.nextId();
           // ThreadUtil.sleep(1);
            Console.log("{} test finished, delay: {}", Thread.currentThread().getName(), id);
        });

       // 获取总的执行时间，单位毫秒
        Console.log(tester.getInterval());*/

    }
}
