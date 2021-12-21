package com.snow.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import com.snow.common.core.text.Convert;
import com.snow.common.enums.MessageEventType;
import com.snow.common.utils.DateUtils;
import com.snow.system.domain.SysMessageTransition;
import com.snow.system.domain.SysOaEmail;
import com.snow.system.domain.SysOaEmailDO;
import com.snow.system.domain.SysOaEmailVO;
import com.snow.system.mapper.SysOaEmailMapper;
import com.snow.system.service.ISysOaEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 邮件Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-03-12
 */
@Service
public class SysOaEmailServiceImpl implements ISysOaEmailService 
{
    @Autowired
    private SysOaEmailMapper sysOaEmailMapper;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private SysMessageTransitionServiceImpl sysMessageTransitionService;



    /**
     * 查询邮件
     * 
     * @param id 邮件ID
     * @return 邮件
     */
    @Override
    public SysOaEmail selectSysOaEmailById(Long id)
    {
        SysOaEmail sysOaEmail=sysOaEmailMapper.selectSysOaEmailById(id);
        sysOaEmail.setBelongUser(sysUserService.selectUserById(Long.parseLong(sysOaEmail.getBelongUserId())));
        return sysOaEmail;
    }

    @Override
    public SysOaEmail selectSysOaEmailByEmailNo(String emailNo) {
        SysOaEmail sysOaEmail=sysOaEmailMapper.selectSysOaEmailByEmailNo(emailNo);
        sysOaEmail.setBelongUser(sysUserService.selectUserById(Long.parseLong(sysOaEmail.getBelongUserId())));
        return sysOaEmail;
    }


    @Override
    public List<SysOaEmail> getMyNoReadOaEmailList(String userId){
        List<SysOaEmail> sysOaEmailList=new ArrayList<>();
        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        sysMessageTransition.setConsumerId(userId);
        sysMessageTransition.setMessageType(MessageEventType.SEND_EMAIL.getCode());
        sysMessageTransition.setMessageStatus(0L);
        sysMessageTransition.setMessageReadStatus(0L);
        List<SysMessageTransition> sysMessageTransitions = sysMessageTransitionService.selectSysMessageTransitionList(sysMessageTransition);
        if(CollUtil.isNotEmpty(sysMessageTransitions)){
            List<String> emailNoList = sysMessageTransitions.stream().map(SysMessageTransition::getMessageOutsideId).collect(Collectors.toList());
            SysOaEmail sysOaEmail=new SysOaEmail();
            sysOaEmail.setEmailNoList(emailNoList);
            sysOaEmailList = selectSysOaEmailList(sysOaEmail);
        }
        return sysOaEmailList;
    }
    /**
     * 查询邮件列表
     * 
     * @param sysOaEmail 邮件
     * @return 邮件
     */
    @Override
    public List<SysOaEmail> selectSysOaEmailList(SysOaEmail sysOaEmail)
    {

        List<SysOaEmail> sysOaEmailList = sysOaEmailMapper.selectSysOaEmailList(sysOaEmail);
        if(CollUtil.isNotEmpty(sysOaEmailList)){
            sysOaEmailList.forEach(t->{
                t.setSpendTime(DateUtil.formatBetween(t.getSendTime(), new Date(), BetweenFormater.Level.SECOND)+"前");
                SysMessageTransition sysMessageTransition=new SysMessageTransition();
                sysMessageTransition.setMessageType(MessageEventType.SEND_EMAIL.getCode());
                sysMessageTransition.setMessageStatus(0L);
                sysMessageTransition.setMessageOutsideId(t.getEmailNo());
                List<SysMessageTransition> sysMessageTransitions = sysMessageTransitionService.selectSysMessageTransitionList(sysMessageTransition);
                if(CollUtil.isNotEmpty(sysMessageTransitions)){
                    //生产者只有一个，直接get(0)就行了
                    t.setEmailFromUser(sysMessageTransitions.get(0).getProducerUser());
                    //消费者存在多个
                    t.setEmailToUser(sysMessageTransitions.stream().map(SysMessageTransition::getConsumerUser).collect(Collectors.toList()));
                }
            });
        }
        return sysOaEmailList;
    }

    @Override
    public List<SysOaEmailVO> selectEmailList(SysOaEmailDO sysOaEmailDO) {
        List<SysOaEmailVO> sysOaEmailVOS = sysOaEmailMapper.selectEmailList(sysOaEmailDO);
        if(CollUtil.isNotEmpty(sysOaEmailVOS)){
            sysOaEmailVOS.forEach(t->{
                t.setProducerUser(sysUserService.selectUserById(Long.parseLong(t.getProducerId())));
                t.setConsumerUser(sysUserService.selectUserById(Long.parseLong(t.getConsumerId())));

            });
        }
        return sysOaEmailVOS;
    }

    /**
     * 新增邮件
     * 
     * @param sysOaEmail 邮件
     * @return 结果
     */
    @Override
    public int insertSysOaEmail(SysOaEmail sysOaEmail)
    {
        sysOaEmail.setCreateTime(DateUtils.getNowDate());
        return sysOaEmailMapper.insertSysOaEmail(sysOaEmail);
    }

    /**
     * 修改邮件
     * 
     * @param sysOaEmail 邮件
     * @return 结果
     */
    @Override
    public int updateSysOaEmail(SysOaEmail sysOaEmail)
    {
        sysOaEmail.setUpdateTime(DateUtils.getNowDate());
        return sysOaEmailMapper.updateSysOaEmail(sysOaEmail);
    }

    /**
     * 删除邮件对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysOaEmailByIds(String ids)
    {
        return sysOaEmailMapper.deleteSysOaEmailByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除邮件信息
     * 
     * @param id 邮件ID
     * @return 结果
     */
    @Override
    public int deleteSysOaEmailById(Long id)
    {
        return sysOaEmailMapper.deleteSysOaEmailById(id);
    }


    @Override
    public int updateSysOaEmailByEmailNo(SysOaEmail sysOaEmail) {
        sysOaEmail.setUpdateTime(DateUtils.getNowDate());
        return sysOaEmailMapper.updateSysOaEmailByEmailNo(sysOaEmail);
    }
}
