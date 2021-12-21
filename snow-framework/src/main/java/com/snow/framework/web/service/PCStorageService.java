package com.snow.framework.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @program: snow
 * @description 设计模式-生产者消费者模式
 * @author: 没用的阿吉
 * @create: 2021-03-30 10:43
 **/
@Slf4j
@Service
public class PCStorageService {

    private LinkedBlockingQueue<Object> list = new LinkedBlockingQueue<>(20);


    public void put(Object obj) {
        try {
            list.put(obj);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("进入缓冲区");
    }


    public Object get() {
        Object obj = null;
        try {
            obj = list.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("离开缓冲区");
        return obj;
    }
}
