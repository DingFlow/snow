package com.snow.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.common.constant.MessageConstants;
import com.snow.common.constant.SequenceConstants;
import com.snow.common.core.domain.MessageEventRequest;
import com.snow.common.enums.DingFlowTaskType;
import com.snow.common.enums.MessageEventType;
import com.snow.system.domain.SysOaTask;
import com.snow.system.domain.SysOaTaskDistribute;
import com.snow.system.mapper.SysOaTaskMapper;
import com.snow.system.service.ISysOaTaskDistributeService;
import com.snow.system.service.ISysOaTaskService;
import com.snow.system.service.ISysUserService;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统任务Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-07-29
 */
@Service
public class SysOaTaskServiceImpl extends ServiceImpl<SysOaTaskMapper,SysOaTask> implements ISysOaTaskService {

    @Autowired
    private ISysOaTaskDistributeService sysOaTaskDistributeService;

    @Autowired
    private ISysUserService sysUserService;

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
    public SysOaTask selectSysOaTaskById(String taskNo) {
        return this.getById(taskNo);
    }

    /**
     * 查询系统任务列表
     * 
     * @param sysOaTask 系统任务
     * @return 系统任务
     */
    @Override
    public List<SysOaTask> selectSysOaTaskList(SysOaTask sysOaTask) {
        LambdaQueryWrapper<SysOaTask> lambda = new QueryWrapper<SysOaTask>().lambda();
        lambda.like(ObjectUtil.isNotEmpty(sysOaTask.getTaskNo()),SysOaTask::getTaskNo,sysOaTask.getTaskNo());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaTask.getTaskStatus()),SysOaTask::getTaskStatus,sysOaTask.getTaskStatus());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaTask.getPriority()),SysOaTask::getPriority,sysOaTask.getPriority());
        lambda.like(ObjectUtil.isNotEmpty(sysOaTask.getTaskName()),SysOaTask::getTaskName,sysOaTask.getTaskName());
        return this.list(lambda);
    }

    /**
     * 新增系统任务
     * 
     * @param sysOaTask 系统任务
     * @return 结果
     */
    @Override
    public SysOaTask insertSysOaTask(SysOaTask sysOaTask) {
        String newSequenceNo = sequenceService.getNewSequenceNo(SequenceConstants.OA_TASK_SEQUENCE);
        sysOaTask.setTaskNo(newSequenceNo);
        sysOaTask.setTaskStatus(DingFlowTaskType.NEW.getCode());
        //任务执行人
        List<String> taskDistributeIdList= sysOaTask.getTaskDistributeId();
        this.save(sysOaTask);
        if(CollUtil.isEmpty(taskDistributeIdList)){
            return sysOaTask;
        }
        List<SysOaTaskDistribute> sysOaTaskDistributeList= Lists.newArrayList();
        taskDistributeIdList.forEach(t->{
            SysOaTaskDistribute sysOaTaskDistribute=new SysOaTaskDistribute();
            sysOaTaskDistribute.setTaskDistributeId(t);
            sysOaTaskDistribute.setTaskNo(newSequenceNo);
            sysOaTaskDistribute.setTaskExecuteStatus(DingFlowTaskType.NEW.getCode());
            sysOaTaskDistribute.setCreateBy(sysOaTask.getCreateBy());
            sysOaTaskDistributeService.save(sysOaTaskDistribute);
            //发送站内信
            sendInnerMessage(sysOaTaskDistribute);
            sysOaTaskDistributeList.add(sysOaTaskDistribute);
        });
        sysOaTask.setSysOaTaskDistributeList(sysOaTaskDistributeList);
        return sysOaTask;
    }




    @Override
    public String getOutTaskOutsideId(String taskNo) {
        return this.getOne(new QueryWrapper<SysOaTask>().lambda().eq(SysOaTask::getTaskNo,taskNo)).getTaskOutsideId();
    }

    /**
     * 发送站内信
     * @param sysOaTaskDistribute 参数
     */
    private void sendInnerMessage(SysOaTaskDistribute sysOaTaskDistribute){
        MessageEventRequest messageEventDTO=new MessageEventRequest(MessageEventType.INNER_SYS_TODO_TASK.getCode());
        messageEventDTO.setProducerId(sysOaTaskDistribute.getCreateBy());
        messageEventDTO.setConsumerIds(Sets.newHashSet(sysOaTaskDistribute.getTaskDistributeId()));
        messageEventDTO.setMessageEventType(MessageEventType.INNER_SYS_TODO_TASK);
        messageEventDTO.setMessageShow(2);
        Map<String,Object> map= new HashMap<>();
        map.put("startUser",sysUserService.selectUserById(Long.parseLong(sysOaTaskDistribute.getCreateBy())).getUserName());
        map.put("businessKey", sysOaTaskDistribute.getTaskNo());
        map.put("startTime", DateUtil.formatDateTime(sysOaTaskDistribute.getCreateTime()));
        map.put("id",sysOaTaskDistribute.getId());
        messageEventDTO.setParamMap(map);
        messageEventDTO.setTemplateCode(MessageConstants.INNER_SYS_TODO_TASK);
        applicationContext.publishEvent(messageEventDTO);
    }
}
