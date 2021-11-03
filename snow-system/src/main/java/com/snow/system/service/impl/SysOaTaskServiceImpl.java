package com.snow.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.snow.common.constant.SequenceConstants;
import com.snow.common.core.text.Convert;
import com.snow.common.enums.DingFlowTaskType;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.exception.BusinessException;
import com.snow.common.utils.DateUtils;
import com.snow.system.domain.SysOaTask;
import com.snow.system.domain.SysOaTaskDistribute;
import com.snow.system.event.SyncEvent;
import com.snow.system.mapper.SysOaTaskDistributeMapper;
import com.snow.system.mapper.SysOaTaskMapper;
import com.snow.system.service.ISysOaTaskDistributeService;
import com.snow.system.service.ISysOaTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 系统任务Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-07-29
 */
@Service
public class SysOaTaskServiceImpl implements ISysOaTaskService 
{
    @Autowired
    private SysOaTaskMapper sysOaTaskMapper;

    @Autowired
    private ISysOaTaskDistributeService sysOaTaskDistributeService;

    @Autowired
    private SysOaTaskDistributeMapper sysOaTaskDistributeMapper;

    @Autowired
    private SysSequenceServiceImpl sequenceService;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 查询系统任务
     * 
     * @param taskNo 系统任务ID
     * @return 系统任务
     */
    @Override
    public SysOaTask selectSysOaTaskById(String taskNo)
    {
        return sysOaTaskMapper.selectSysOaTaskById(taskNo);
    }

    /**
     * 查询系统任务列表
     * 
     * @param sysOaTask 系统任务
     * @return 系统任务
     */
    @Override
    public List<SysOaTask> selectSysOaTaskList(SysOaTask sysOaTask)
    {
        return sysOaTaskMapper.selectSysOaTaskList(sysOaTask);
    }

    /**
     * 新增系统任务
     * 
     * @param sysOaTask 系统任务
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSysOaTask(SysOaTask sysOaTask)
    {
        sysOaTask.setCreateTime(DateUtils.getNowDate());
        String newSequenceNo = sequenceService.getNewSequenceNo(SequenceConstants.OA_TASK_SEQUENCE);
        sysOaTask.setTaskNo(newSequenceNo);
        List<String> taskDistributeIdList= sysOaTask.getTaskDistributeId();
        if(CollUtil.isEmpty(taskDistributeIdList)){
            //待完成
            sysOaTask.setTaskStatus(DingFlowTaskType.NEW.getCode());
        }else {
            //已分配
            sysOaTask.setTaskStatus(DingFlowTaskType.RUNNING.getCode());
            taskDistributeIdList.forEach(t->{
                SysOaTaskDistribute sysOaTaskDistribute=new SysOaTaskDistribute();
                //任务分配人
                sysOaTaskDistribute.setTaskDistributeId(sysOaTask.getCreateBy());
                sysOaTaskDistribute.setTaskNo(newSequenceNo);
                sysOaTaskDistribute.setTaskExecuteStatus(DingFlowTaskType.RUNNING.getCode());
                sysOaTaskDistribute.setCreateBy(sysOaTask.getCreateBy());
                sysOaTaskDistributeService.insertSysOaTaskDistribute(sysOaTaskDistribute);
            });
        }
        //事件发送
        SyncEvent<SysOaTask> syncEvent = new SyncEvent(sysOaTask, DingTalkListenerType.WORK_RECODE_CREATE);
        applicationContext.publishEvent(syncEvent);
        return sysOaTaskMapper.insertSysOaTask(sysOaTask);
    }

    /**
     * 修改系统任务
     * 
     * @param sysOaTask 系统任务
     * @return 结果
     */
    @Override
    public int updateSysOaTask(SysOaTask sysOaTask)
    {
        sysOaTask.setUpdateTime(DateUtils.getNowDate());
        return sysOaTaskMapper.updateSysOaTask(sysOaTask);
    }

    /**
     * 删除系统任务对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysOaTaskByIds(String ids)
    {
        List<String> list = Arrays.asList(Convert.toStrArray(ids));
        list.forEach(t->{
            List<SysOaTaskDistribute> sysOaTaskDistributes = sysOaTaskDistributeMapper.selectSysOaTaskDistributeByTaskNo(t);
            if(CollUtil.isNotEmpty(sysOaTaskDistributes)){
                throw new BusinessException("任务编号："+t+"已分配，不允许删除操作");
            }
        });
        return sysOaTaskMapper.deleteSysOaTaskByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除系统任务信息
     * 
     * @param taskNo 系统任务ID
     * @return 结果
     */
    @Override
    public int deleteSysOaTaskById(String taskNo)
    {
        List<SysOaTaskDistribute> sysOaTaskDistributes = sysOaTaskDistributeMapper.selectSysOaTaskDistributeByTaskNo(taskNo);
        if(CollUtil.isNotEmpty(sysOaTaskDistributes)){
            throw new BusinessException("任务编号："+taskNo+"已分配，不允许删除操作");
        }
        //发送事件
        SyncEvent<String> syncEvent = new SyncEvent(taskNo, DingTalkListenerType.WORK_RECODE_CREATE);
        applicationContext.publishEvent(syncEvent);
        return sysOaTaskMapper.deleteSysOaTaskById(taskNo);
    }
}
