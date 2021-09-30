package com.snow.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import com.snow.common.core.text.Convert;
import com.snow.common.utils.DateUtils;
import com.snow.system.domain.SysMessageTransition;
import com.snow.system.mapper.SysMessageTransitionMapper;
import com.snow.system.service.ISysMessageTransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 消息流转中心Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-03-30
 */
@Service
public class SysMessageTransitionServiceImpl implements ISysMessageTransitionService 
{
    @Autowired
    private SysMessageTransitionMapper sysMessageTransitionMapper;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private SysMessageTemplateServiceImpl sysMessageTemplateService;

    /**
     * 查询消息流转中心
     * 
     * @param id 消息流转中心ID
     * @return 消息流转中心
     */
    @Override
    public SysMessageTransition selectSysMessageTransitionById(Long id)
    {
        return sysMessageTransitionMapper.selectSysMessageTransitionById(id);
    }


    @Override
    public Boolean getIsRead(SysMessageTransition sysMessageTransition)
    {
        SysMessageTransition isReadSysMessageTransition=new SysMessageTransition();
        isReadSysMessageTransition.setConsumerId(sysMessageTransition.getConsumerId());
        isReadSysMessageTransition.setMessageOutsideId(sysMessageTransition.getMessageOutsideId());
        isReadSysMessageTransition.setMessageType(sysMessageTransition.getMessageType());
        isReadSysMessageTransition.setMessageStatus(0L);
        List<SysMessageTransition> sysMessageTransitionList = sysMessageTransitionMapper.selectSysMessageTransitionList(isReadSysMessageTransition);
        if(CollUtil.isNotEmpty(sysMessageTransitionList)){
           return sysMessageTransitionList.get(0).getMessageReadStatus()==1L;
        }
        return false;
    }
    /**
     * 查询消息流转中心列表
     * 
     * @param sysMessageTransition 消息流转中心
     * @return 消息流转中心
     */
    @Override
    public List<SysMessageTransition> selectSysMessageTransitionList(SysMessageTransition sysMessageTransition)
    {
        sysMessageTransition.setMessageStatus(0L);
        List<SysMessageTransition> sysMessageTransitionList= sysMessageTransitionMapper.selectSysMessageTransitionList(sysMessageTransition);
        if(CollUtil.isNotEmpty(sysMessageTransitionList)){
            sysMessageTransitionList.forEach(t->{
                t.setProducerUser(sysUserService.selectUserById(Long.parseLong(t.getProducerId())));
                t.setConsumerUser(sysUserService.selectUserById(Long.parseLong(t.getConsumerId())));
                t.setSpendTime(DateUtil.formatBetween(t.getCreateTime(), new Date(), BetweenFormater.Level.SECOND)+"前");
                Optional.ofNullable(t.getTemplateCode()).ifPresent( m-> t.setSysMessageTemplate(sysMessageTemplateService.getSysMessageTemplateByCode(t.getTemplateCode())));
            });
        }

        return sysMessageTransitionList;
    }

    /**
     * 新增消息流转中心
     * 
     * @param sysMessageTransition 消息流转中心
     * @return 结果
     */
    @Override
    public int insertSysMessageTransition(SysMessageTransition sysMessageTransition)
    {
        sysMessageTransition.setCreateTime(DateUtils.getNowDate());
        sysMessageTransition.setUpdateTime(DateUtils.getNowDate());
        return sysMessageTransitionMapper.insertSysMessageTransition(sysMessageTransition);
    }

    /**
     * 修改消息流转中心
     * 
     * @param sysMessageTransition 消息流转中心
     * @return 结果
     */
    @Override
    public int updateSysMessageTransition(SysMessageTransition sysMessageTransition)
    {
        sysMessageTransition.setUpdateTime(DateUtils.getNowDate());
        return sysMessageTransitionMapper.updateSysMessageTransition(sysMessageTransition);
    }

    @Override
    public int updateByCondition(SysMessageTransition sysMessageTransition) {
        sysMessageTransition.setUpdateTime(DateUtils.getNowDate());
        return sysMessageTransitionMapper.updateByCondition(sysMessageTransition);
    }

    /**
     * 删除消息流转中心对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysMessageTransitionByIds(String ids)
    {
        return sysMessageTransitionMapper.deleteSysMessageTransitionByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除消息流转中心信息
     * 
     * @param id 消息流转中心ID
     * @return 结果
     */
    @Override
    public int deleteSysMessageTransitionById(Long id)
    {
        return sysMessageTransitionMapper.deleteSysMessageTransitionById(id);
    }

    @Override
    public int deleteSysMessageTransitionByOutsideId(List outsideIdList) {
        return sysMessageTransitionMapper.deleteSysMessageTransitionByOutsideId(outsideIdList);
    }
}
