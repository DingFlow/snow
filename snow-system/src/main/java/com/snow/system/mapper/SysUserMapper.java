package com.snow.system.mapper;

import com.snow.system.domain.SysAuthUser;
import com.snow.system.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 数据层
 * 
 * @author snow
 */
public interface SysUserMapper
{
    /**
     * 根据条件分页查询用户列表
     * 
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUserList(SysUser sysUser);

    /**
     * 根据条件分页查询未已配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectAllocatedList(SysUser user);

    /**
     *
     * @param roleId
     * @return
     */
    public List<SysUser> selectUserListByRoleId(String roleId);

    /**
     *
     * @param roleIds
     * @return
     */
    public List<SysUser>  selectUserListByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByLoginName(String userName);

    /**
     * 通过手机号码查询用户
     * 
     * @param phoneNumber 手机号码
     * @return 用户对象信息
     */
    public SysUser selectUserByPhoneNumber(String phoneNumber);

    /**
     * 通过邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象信息
     */
    public SysUser selectUserByEmail(String email);

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(Long userId);

    /**
     * 通过用户dingUserId查询用户
     * @param dingUserId
     * @return
     */
    public SysUser selectUserByDingUserId(String dingUserId);

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserByIds(Long[] ids);

    /**
     * 修改用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(SysUser user);

    /**
     * 新增用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);

    /**
     * 校验用户名称是否唯一
     * 
     * @param loginName 登录名称
     * @return 结果
     */
    public int checkLoginNameUnique(String loginName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public SysUser checkEmailUnique(String email);


    /**
     * 根据uuid查询用户信息
     *
     * @param uuid 唯一信息
     * @return 结果
     */
    public SysUser selectAuthUserByUuid(String uuid);

    /**
     * 新增第三方授权信息
     *
     * @param authUser 用户信息
     * @return 结果
     */
    public int insertAuthUser(SysAuthUser authUser);

    /**
     * 根据用户编号查询授权列表
     *
     * @param userId 登录账户
     * @return 授权列表
     */
    public List<SysAuthUser> selectAuthUserListByUserId(Long userId);

    /**
     * 校验source平台是否绑定
     *
     * @param userId 用户编号
     * @param source 绑定平台
     * @return 结果
     */
    public int checkAuthUser(@Param("userId") Long userId, @Param("source") String source);

    /**
     * 根据编号删除第三方授权信息
     *
     * @param authId 授权编号
     * @return 结果
     */
    public int deleteAuthUser(Long authId);
}
