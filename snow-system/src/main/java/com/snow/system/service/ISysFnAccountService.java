package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysFnAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.system.domain.request.DeductionAccountRequest;
import com.snow.system.domain.request.RechargeAccountRequest;
import com.snow.system.domain.response.SysFnAccountResponse;

/**
 * 账户Service接口
 * 
 * @author Agee
 * @date 2022-02-16
 */
public interface ISysFnAccountService extends IService<SysFnAccount>
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
     * 批量删除账户
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFnAccountByIds(String ids);

    /**
     * 删除账户信息
     * 
     * @param id 账户ID
     * @return 结果
     */
    public int deleteSysFnAccountById(Long id);

    /**
     * 获取账户信息通过账户
     * @param accountNo 账户号
     * @return 账户信息
     */
    public SysFnAccountResponse getSysFnAccountByNo(String accountNo);

    /**
     *  充值账户
     * @param rechargeAccountRequest
     * @return 流水号
     */
    public String rechargeAccount(RechargeAccountRequest rechargeAccountRequest);

    /**
     * 扣款账户
     * @param deductionAccountRequest
     * @return 流水号
     */
    public String deductionAccount(DeductionAccountRequest deductionAccountRequest);
}
