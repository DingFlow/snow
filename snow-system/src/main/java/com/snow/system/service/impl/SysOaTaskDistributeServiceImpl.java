package com.snow.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.common.enums.DingFlowTaskType;
import com.snow.system.domain.SysOaTask;
import com.snow.system.domain.SysOaTaskDistribute;
import com.snow.system.domain.SysUser;
import com.snow.system.mapper.SysOaTaskDistributeMapper;
import com.snow.system.service.ISysOaTaskDistributeService;
import com.snow.system.service.ISysOaTaskService;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务分配Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-08-15
 */
@Service
public class SysOaTaskDistributeServiceImpl extends ServiceImpl<SysOaTaskDistributeMapper,SysOaTaskDistribute> implements ISysOaTaskDistributeService {


    @Autowired
    private ISysOaTaskService sysOaTaskService;

    @Autowired
    private SysUserServiceImpl sysUserService;
    /**
     * 查询任务分配
     * 
     * @param id 任务分配ID
     * @return 任务分配
     */
    @Override
    public SysOaTaskDistribute selectSysOaTaskDistributeById(Long id)
    {
        return this.getById(id);
    }

    /**
     * 查询任务分配列表
     * 
     * @param sysOaTaskDistribute 任务分配
     * @return 任务分配
     */
    @Override
    public List<SysOaTaskDistribute> selectSysOaTaskDistributeList(SysOaTaskDistribute sysOaTaskDistribute) {
        LambdaQueryWrapper<SysOaTaskDistribute> lambda = new QueryWrapper<SysOaTaskDistribute>().lambda();
        lambda.eq(ObjectUtil.isNotEmpty(sysOaTaskDistribute.getTaskNo()),SysOaTaskDistribute::getTaskNo,sysOaTaskDistribute.getTaskNo());
        lambda.in(CollUtil.isNotEmpty(sysOaTaskDistribute.getTaskExecuteStatusList()),SysOaTaskDistribute::getTaskExecuteStatus,sysOaTaskDistribute.getTaskExecuteStatusList());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaTaskDistribute.getTaskDistributeId()),SysOaTaskDistribute::getTaskDistributeId,sysOaTaskDistribute.getTaskDistributeId());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaTaskDistribute.getTaskExecuteId()),SysOaTaskDistribute::getTaskExecuteId,sysOaTaskDistribute.getTaskExecuteId());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaTaskDistribute.getTaskExecuteStatus()),SysOaTaskDistribute::getTaskExecuteStatus,sysOaTaskDistribute.getTaskExecuteStatus());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaTaskDistribute.getTaskCompleteTime()),SysOaTaskDistribute::getTaskCompleteTime,sysOaTaskDistribute.getTaskCompleteTime());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaTaskDistribute.getTaskStartTime()),SysOaTaskDistribute::getTaskStartTime,sysOaTaskDistribute.getTaskStartTime());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaTaskDistribute.getTaskSuspendTime()),SysOaTaskDistribute::getTaskSuspendTime,sysOaTaskDistribute.getTaskSuspendTime());
        return this.list(lambda);
    }

    @Override
    public List<SysOaTaskDistribute> getSysOaTaskDistribute(SysOaTaskDistribute sysOaTaskDistribute) {
        List<SysOaTaskDistribute> sysOaTaskDistributes = this.selectSysOaTaskDistributeList(sysOaTaskDistribute);
        if(CollUtil.isEmpty(sysOaTaskDistributes)){
            return sysOaTaskDistributes;
        }
        warpSysOaTask(sysOaTaskDistributes);
        return sysOaTaskDistributes;
    }

    public void  warpSysOaTask(List<SysOaTaskDistribute> sysOaTaskDistributes){
        sysOaTaskDistributes.forEach(t->{
            SysOaTask sysOaTask = sysOaTaskService.selectSysOaTaskById(t.getTaskNo());
            sysOaTask.setCreateBy(sysUserService.selectUserById(Long.parseLong(sysOaTask.getCreateBy())).getUserName());
            if(ObjectUtil.isNotNull(t.getTaskDistributeId())){
                t.setTaskDistributeId(sysUserService.selectUserById(Long.parseLong(t.getTaskDistributeId())).getUserName());
            }
            if(ObjectUtil.isNotNull(t.getTaskExecuteId())){
                t.setTaskExecuteId(sysUserService.selectUserById(Long.parseLong(t.getTaskExecuteId())).getUserName());
            }
            if(ObjectUtil.isNotNull(t.getTaskStartTime())&&ObjectUtil.isNotNull(t.getTaskCompleteTime())){
                t.setSpendTime(DateUtil.formatBetween(t.getTaskStartTime(),t.getTaskCompleteTime(), BetweenFormater.Level.SECOND));
            }
            t.setSysOaTask(sysOaTask);
            t.setCreateBy(sysUserService.selectUserById(Long.parseLong(t.getCreateBy())).getUserName());
        });
    }

}
