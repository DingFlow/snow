package com.snow.system.service.impl;

import java.util.List;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysOaEmailMapper;
import com.snow.system.domain.SysOaEmail;
import com.snow.system.service.ISysOaEmailService;
import com.snow.common.core.text.Convert;

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
        sysOaEmail.setEmailFromUser(sysUserService.selectUserById(Long.parseLong(sysOaEmail.getEmailFrom())));
        sysOaEmail.setEmailToUser(sysUserService.selectUserById(Long.parseLong(sysOaEmail.getEmailTo())));
        return sysOaEmail;
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
        List<SysOaEmail> sysOaEmailList=sysOaEmailMapper.selectSysOaEmailList(sysOaEmail);
        sysOaEmailList.parallelStream().forEach(t->{
            t.setEmailFromUser(sysUserService.selectUserById(Long.parseLong(t.getEmailFrom())));
            t.setEmailToUser(sysUserService.selectUserById(Long.parseLong(t.getEmailTo())));
        });
        return sysOaEmailMapper.selectSysOaEmailList(sysOaEmail);
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

    /**
     * 查询重要
     * @param sysOaEmail
     * @return
     */
    @Override
    public List<SysOaEmail> selectImportantEmailList(SysOaEmail sysOaEmail) {
        List<SysOaEmail> sysOaEmailList=sysOaEmailMapper.selectImportantEmailList(sysOaEmail);
        sysOaEmailList.parallelStream().forEach(t->{
            t.setEmailFromUser(sysUserService.selectUserById(Long.parseLong(t.getEmailFrom())));
            t.setEmailToUser(sysUserService.selectUserById(Long.parseLong(t.getEmailTo())));
        });
        return sysOaEmailList;
    }


    @Override
    public int updateSysOaEmailByEmailNo(SysOaEmail sysOaEmail) {
        sysOaEmail.setUpdateTime(DateUtils.getNowDate());
        return sysOaEmailMapper.updateSysOaEmailByEmailNo(sysOaEmail);
    }
}
