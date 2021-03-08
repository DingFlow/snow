package com.snow.flowable.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.flowable.common.engine.impl.cfg.IdGenerator;

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
