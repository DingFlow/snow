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
}
