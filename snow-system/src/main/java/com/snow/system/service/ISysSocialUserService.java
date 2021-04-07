package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysSocialUser;

/**
 * 社会化用户信息Service接口
 * 
 * @author 没用的阿吉
 * @date 2021-03-29
 */
public interface ISysSocialUserService 
{
    /**
     * 查询社会化用户信息
     * 
     * @param id 社会化用户信息ID
     * @return 社会化用户信息
     */
    public SysSocialUser selectSysSocialUserById(Long id);

    /**
     * 查询社会化用户信息列表
     * 
     * @param sysSocialUser 社会化用户信息
     * @return 社会化用户信息集合
     */
    public List<SysSocialUser> selectSysSocialUserList(SysSocialUser sysSocialUser);

    /**
     * 新增社会化用户信息
     * 
     * @param sysSocialUser 社会化用户信息
     * @return 结果
     */
    public int insertSysSocialUser(SysSocialUser sysSocialUser);

    /**
     * 修改社会化用户信息
     * 
     * @param sysSocialUser 社会化用户信息
     * @return 结果
     */
    public int updateSysSocialUser(SysSocialUser sysSocialUser);

    /**
     * 批量删除社会化用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysSocialUserByIds(String ids);

    /**
     * 删除社会化用户信息信息
     * 
     * @param id 社会化用户信息ID
     * @return 结果
     */
    public int deleteSysSocialUserById(Long id);
}
