package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.SysFnAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 账户Mapper接口
 * 
 * @author Agee
 * @date 2022-02-16
 */
public interface SysFnAccountMapper extends BaseMapper<SysFnAccount>
{
    /**
     * 查询账户
     * 
     * @param id 账户ID
     * @return 账户
     */
    public SysFnAccount selectSysFnAccountById(Long id);

    /**
     * 查询账户列表
     * 
     * @param sysFnAccount 账户
     * @return 账户集合
     */
    public List<SysFnAccount> selectSysFnAccountList(SysFnAccount sysFnAccount);

    /**
     * 新增账户
     * 
     * @param sysFnAccount 账户
     * @return 结果
     */
    public int insertSysFnAccount(SysFnAccount sysFnAccount);

    /**
     * 修改账户
     * 
     * @param sysFnAccount 账户
     * @return 结果
     */
    public int updateSysFnAccount(SysFnAccount sysFnAccount);

    /**
     * 删除账户
     * 
     * @param id 账户ID
     * @return 结果
     */
    public int deleteSysFnAccountById(Long id);

    /**
     * 批量删除账户
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFnAccountByIds(String[] ids);
}
