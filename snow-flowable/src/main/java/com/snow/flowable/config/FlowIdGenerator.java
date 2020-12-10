package com.snow.flowable.config;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.dfa.WordTree;
import cn.hutool.poi.word.Word07Writer;
import com.alibaba.fastjson.JSON;
import org.flowable.common.engine.impl.cfg.IdGenerator;

import cn.hutool.core.lang.Console;

import java.awt.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
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
        RSA rsa = new RSA();

//获得私钥
        PrivateKey privateKey = rsa.getPrivateKey();

//获得公钥
        PublicKey publicKey =  rsa.getPublicKey();
        Console.log("privateKey：{}",privateKey.getEncoded());
        Console.log("publicKey：{}",publicKey.getEncoded());

  /*      Word07Writer writer = new Word07Writer();

// 添加段落（标题）
        writer.addText(new Font("方正小标宋简体", Font.PLAIN, 22), "我是第一部分", "我是第二部分");
// 添加段落（正文）
        writer.addText(new Font("宋体", Font.PLAIN, 22), "我是正文第一部分", "我是正文第二部分");
// 写出到文件
        writer.flush(FileUtil.file("d:/wordWrite.docx"));
// 关闭
        writer.close();
*/
   /*     ThreadPoolExecutor executor = ExecutorBuilder.create().
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
        executor.shutdown();*/

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
