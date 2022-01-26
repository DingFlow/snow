package com.snow.quartz.task;

import cn.hutool.core.date.DateUtil;
import com.snow.system.domain.SysSequence;
import com.snow.system.service.impl.SysSequenceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: snow
 * @description 每日凌晨更新序列值
 * @author: 没用的阿吉
 * @create: 2020-11-24 21:27
 **/
@Component("sequenceTask")
@Slf4j
public class SequenceTask {
    @Autowired
    private SysSequenceServiceImpl sysSequenceService;
    /**
     *每日凌晨更新序列值
     */
    public void updateSequenceValue()
    {
        log.info("开始更新序列值时间:{}",DateUtil.now());
        SysSequence sysSequence=new SysSequence();
        List<SysSequence> sysSequences = sysSequenceService.selectSysSequenceList(sysSequence);
        sysSequences.forEach(t->{
            t.setCurrentValue(1L);
            sysSequenceService.updateSysSequence(t);
        });
    }
}
