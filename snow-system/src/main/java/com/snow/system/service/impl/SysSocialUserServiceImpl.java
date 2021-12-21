package com.snow.system.service.impl;

import java.util.List;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysSocialUserMapper;
import com.snow.system.domain.SysSocialUser;
import com.snow.system.service.ISysSocialUserService;
import com.snow.common.core.text.Convert;

/**
 * 社会化用户信息Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-03-29
 */
@Service
public class SysSocialUserServiceImpl implements ISysSocialUserService 
{
    @Autowired
    private SysSocialUserMapper sysSocialUserMapper;

    /**
     * 查询社会化用户信息
     * 
     * @param id 社会化用户信息ID
     * @return 社会化用户信息
     */
    @Override
    public SysSocialUser selectSysSocialUserById(Long id)
    {
        return sysSocialUserMapper.selectSysSocialUserById(id);
    }

    /**
     * 查询社会化用户信息列表
     * 
     * @param sysSocialUser 社会化用户信息
     * @return 社会化用户信息
     */
    @Override
    public List<SysSocialUser> selectSysSocialUserList(SysSocialUser sysSocialUser)
    {
        return sysSocialUserMapper.selectSysSocialUserList(sysSocialUser);
    }

    /**
     * 新增社会化用户信息
     * 
     * @param sysSocialUser 社会化用户信息
     * @return 结果
     */
    @Override
    public int insertSysSocialUser(SysSocialUser sysSocialUser)
    {
        sysSocialUser.setCreateTime(DateUtils.getNowDate());
        return sysSocialUserMapper.insertSysSocialUser(sysSocialUser);
    }

    /**
     * 修改社会化用户信息
     * 
     * @param sysSocialUser 社会化用户信息
     * @return 结果
     */
    @Override
    public int updateSysSocialUser(SysSocialUser sysSocialUser)
    {
        sysSocialUser.setUpdateTime(DateUtils.getNowDate());
        return sysSocialUserMapper.updateSysSocialUser(sysSocialUser);
    }

    /**
     * 删除社会化用户信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysSocialUserByIds(String ids)
    {
        return sysSocialUserMapper.deleteSysSocialUserByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除社会化用户信息信息
     * 
     * @param id 社会化用户信息ID
     * @return 结果
     */
    @Override
    public int deleteSysSocialUserById(Long id)
    {
        return sysSocialUserMapper.deleteSysSocialUserById(id);
    }
}
