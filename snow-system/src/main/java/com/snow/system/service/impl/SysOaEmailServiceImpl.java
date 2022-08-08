package com.snow.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.common.core.text.Convert;
import com.snow.common.enums.MessageEventType;
import com.snow.common.enums.MessageReadStatus;
import com.snow.common.utils.DateUtils;
import com.snow.common.utils.bean.BeanUtils;
import com.snow.system.domain.*;
import com.snow.system.mapper.SysOaEmailMapper;
import com.snow.system.service.ISysOaEmailService;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 邮件Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-03-12
 */
@Service
public class SysOaEmailServiceImpl extends ServiceImpl<SysOaEmailMapper, SysOaEmail> implements ISysOaEmailService {
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
    public SysOaEmailDTO selectSysOaEmailById(Long id) {
        SysOaEmail sysOaEmail=this.getById(id);
        SysOaEmailDTO sysOaEmailDTO= BeanUtil.copyProperties(sysOaEmail,SysOaEmailDTO.class);
        sysOaEmailDTO.setBelongUser(sysUserService.selectUserById(Long.parseLong(sysOaEmail.getBelongUserId())));
        return sysOaEmailDTO;
    }

    @Override
    public SysOaEmailDTO selectSysOaEmailByEmailNo(String emailNo) {
        LambdaQueryWrapper<SysOaEmail> eq = new LambdaQueryWrapper<SysOaEmail>()
                .eq(SysOaEmail::getEmailSubject, emailNo);
        SysOaEmail sysOaEmail = this.getOne(eq);
        SysOaEmailDTO sysOaEmailDTO= BeanUtil.copyProperties(sysOaEmail,SysOaEmailDTO.class);
        sysOaEmailDTO.setBelongUser(sysUserService.selectUserById(Long.parseLong(sysOaEmail.getBelongUserId())));
        return sysOaEmailDTO;
    }


    @Override
    public List<SysOaEmailDTO> getMyNoReadOaEmailList(String userId){

        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        sysMessageTransition.setConsumerId(userId);
        sysMessageTransition.setMessageType(MessageEventType.SEND_EMAIL.getCode());
        sysMessageTransition.setMessageStatus(0L);
        sysMessageTransition.setMessageReadStatus(MessageReadStatus.NO_READ.getCode());
        List<SysMessageTransition> sysMessageTransitions = sysMessageTransitionService.selectSysMessageTransitionList(sysMessageTransition);
        if(CollUtil.isEmpty(sysMessageTransitions)){
            return Lists.newArrayList();
        }
        List<String> emailNoList = sysMessageTransitions.stream().map(SysMessageTransition::getMessageOutsideId).collect(Collectors.toList());
        SysOaEmailDTO sysOaEmail=new SysOaEmailDTO();
        sysOaEmail.setEmailNoList(emailNoList);
        return this.selectSysOaEmailList(sysOaEmail);

    }
    /**
     * 查询邮件列表
     * 
     * @param sysOaEmail 邮件
     * @return 邮件
     */
    @Override
    public List<SysOaEmailDTO> selectSysOaEmailList(SysOaEmailDTO sysOaEmail) {

        LambdaQueryWrapper<SysOaEmail> eq = new LambdaQueryWrapper<SysOaEmail>()
                .in(ObjectUtil.isNotEmpty(sysOaEmail.getEmailNoList()),SysOaEmail::getEmailNo,sysOaEmail.getEmailNoList())
                .like(StrUtil.isNotBlank(sysOaEmail.getEmailSubject()), SysOaEmail::getEmailSubject, sysOaEmail.getEmailSubject())
                .eq(ObjectUtil.isNotEmpty(sysOaEmail.getEmailStatus()),SysOaEmail::getEmailStatus,sysOaEmail.getEmailStatus());

        List<SysOaEmail> list = this.list(eq);
        if(CollUtil.isEmpty(list)){
            return BeanUtils.transformList(list,SysOaEmailDTO.class);
        }
        List<SysOaEmailDTO> sysOaEmailDTOList= Lists.newArrayList();
        list.forEach(t->{
            List<String> emailToUser = t.getEmailToUser();
            if(CollUtil.isEmpty(emailToUser)){
                return;
            }
            List<SysUser> sysUserList = sysUserService.selectUserByIds(cn.hutool.core.convert.Convert.toLongArray(emailToUser));
            List<String> emailList = sysUserList.stream().map(SysUser::getEmail).collect(Collectors.toList());
            t.setEmailToUser(emailList);
            SysOaEmailDTO sysOaEmailDTO = BeanUtil.copyProperties(t, SysOaEmailDTO.class);
            sysOaEmailDTO.setEmailToUser(sysUserList);
            sysOaEmailDTO.setEmailTo(CollUtil.join(emailList,","));
            sysOaEmailDTOList.add(sysOaEmailDTO);
        });
        return sysOaEmailDTOList;
    }

    @Override
    public List<SysOaEmailVO> selectEmailList(SysOaEmailDO sysOaEmailDO) {
        List<SysOaEmailVO> sysOaEmailVOS = sysOaEmailMapper.selectEmailList(sysOaEmailDO);
        if(CollUtil.isEmpty(sysOaEmailVOS)){
            return sysOaEmailVOS;
        }
        sysOaEmailVOS.forEach(t->{
            t.setProducerUser(sysUserService.selectUserById(Long.parseLong(t.getProducerId())));
            t.setConsumerUser(sysUserService.selectUserById(Long.parseLong(t.getConsumerId())));
        });
        return sysOaEmailVOS;
    }



    /**
     * 修改邮件
     * 
     * @param sysOaEmail 邮件
     * @return 结果
     */
    @Override
    public int updateSysOaEmail(SysOaEmailDTO sysOaEmail)
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
    public int updateSysOaEmailByEmailNo(SysOaEmailDTO sysOaEmail) {
        sysOaEmail.setUpdateTime(DateUtils.getNowDate());
        return sysOaEmailMapper.updateSysOaEmailByEmailNo(sysOaEmail);
    }
}
