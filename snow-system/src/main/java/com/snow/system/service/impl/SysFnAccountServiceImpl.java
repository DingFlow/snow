package com.snow.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.snow.common.constant.SequenceConstants;
import com.snow.common.exception.BusinessException;
import com.snow.common.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.system.domain.SysFnAccountBill;
import com.snow.system.domain.request.DeductionAccountRequest;
import com.snow.system.domain.request.RechargeAccountRequest;
import com.snow.system.domain.response.SysFnAccountResponse;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysFnAccountMapper;
import com.snow.system.domain.SysFnAccount;
import com.snow.system.service.ISysFnAccountService;
import com.snow.common.core.text.Convert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;

/**
 * 账户Service业务层处理
 *
 * @author Agee
 * @date 2022-02-16
 */
@Service
public class SysFnAccountServiceImpl extends ServiceImpl<SysFnAccountMapper, SysFnAccount> implements ISysFnAccountService {
    @Resource
    private SysFnAccountMapper sysFnAccountMapper;

    @Resource
    private SysSequenceServiceImpl sysSequenceService;

    @Resource
    private SysFnAccountBillServiceImpl sysFnAccountBillService;

    /**
     * 查询账户
     *
     * @param id 账户ID
     * @return 账户
     */
    @Override
    public SysFnAccount selectSysFnAccountById(Long id) {
        return sysFnAccountMapper.selectById(id);
    }

    /**
     * 查询账户列表
     *
     * @param sysFnAccount 账户
     * @return 账户
     */
    @Override
    public List<SysFnAccount> selectSysFnAccountList(SysFnAccount sysFnAccount) {
        LambdaQueryWrapper<SysFnAccount> lambda = new QueryWrapper<SysFnAccount>().lambda();
        lambda.like(ObjectUtil.isNotEmpty(sysFnAccount.getAccountNo()),SysFnAccount::getAccountNo,sysFnAccount.getAccountNo());
        lambda.like(ObjectUtil.isNotEmpty(sysFnAccount.getAccountName()),SysFnAccount::getAccountName,sysFnAccount.getAccountName());
        return sysFnAccountMapper.selectList(lambda);
    }

    /**
     * 新增账户
     *
     * @param sysFnAccount 账户
     * @return 结果
     */
    @Override
    public int insertSysFnAccount(SysFnAccount sysFnAccount) {
        String accountNo = sysSequenceService.getNewSequenceNo(SequenceConstants.FN_ACCOUNT_NO);
        sysFnAccount.setAccountNo(accountNo);
        sysFnAccount.setCreateTime(DateUtils.getNowDate());
        return sysFnAccountMapper.insert(sysFnAccount);
    }

    /**
     * 修改账户
     *
     * @param sysFnAccount 账户
     * @return 结果
     */
    @Override
    public int updateSysFnAccount(SysFnAccount sysFnAccount) {
        sysFnAccount.setUpdateTime(DateUtils.getNowDate());
        return sysFnAccountMapper.updateById(sysFnAccount);
    }

    /**
     * 删除账户对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     @Override
     public int deleteSysFnAccountByIds(String ids) {
        return sysFnAccountMapper.deleteBatchIds(Convert.toStrList(ids));
     }

    /**
     * 删除账户信息
     *
     * @param id 账户ID
     * @return 结果
     */
    @Override
    public int deleteSysFnAccountById(Long id) {
        return sysFnAccountMapper.deleteById(id);
    }

    @Override
    public SysFnAccountResponse getSysFnAccountByNo(String accountNo) {
        SysFnAccount sysFnAccount = getOne(new QueryWrapper<SysFnAccount>().lambda().eq(SysFnAccount::getAccountNo, accountNo));
        SysFnAccountResponse sysFnAccountResponse = BeanUtil.copyProperties(sysFnAccount, SysFnAccountResponse.class);
        //计算可用金额
        BigDecimal usableAmount = sysFnAccountResponse.getTotalAmount().subtract(sysFnAccountResponse.getFreezeAmount());
        sysFnAccountResponse.setUsableAmount(usableAmount);
        return sysFnAccountResponse;
    }

    @Override
    public String rechargeAccount(RechargeAccountRequest rechargeAccountRequest) {
        LambdaQueryWrapper<SysFnAccount> lambda = new QueryWrapper<SysFnAccount>().lambda();
        SysFnAccount fnAccount = getOne(lambda.eq(SysFnAccount::getAccountNo, rechargeAccountRequest.getAccountNo()));
        BigDecimal rechargeAmount = Optional.ofNullable(rechargeAccountRequest.getRechargeAmount()).orElse(BigDecimal.ZERO);
        BigDecimal amount = rechargeAmount.add(fnAccount.getTotalAmount());
        fnAccount.setTotalAmount(amount);
        updateSysFnAccount(fnAccount);
        SysFnAccountBill sysFnAccountBill=new SysFnAccountBill();
        sysFnAccountBill.setAccountNo(rechargeAccountRequest.getAccountNo());
        sysFnAccountBill.setBillAmount(rechargeAccountRequest.getRechargeAmount());
        sysFnAccountBill.setBillType(2);
        sysFnAccountBill.setBillRemark(rechargeAccountRequest.getRechargeRemark());
        sysFnAccountBillService.insertSysFnAccountBill(sysFnAccountBill);
        return sysFnAccountBill.getBillNo();
    }

    @Override
    public String deductionAccount(DeductionAccountRequest deductionAccountRequest){
        LambdaQueryWrapper<SysFnAccount> lambda = new QueryWrapper<SysFnAccount>().lambda();
        SysFnAccount fnAccount = getOne(lambda.eq(SysFnAccount::getAccountNo, deductionAccountRequest.getAccountNo()));
        BigDecimal deductionAccount = Optional.ofNullable(deductionAccountRequest.getDeductionAccount()).orElse(BigDecimal.ZERO);
        BigDecimal amount =(fnAccount.getTotalAmount()).subtract(deductionAccount);
        if(amount.compareTo(BigDecimal.ZERO)==-1){
            throw new BusinessException("账户金额不足,请先充值!");
        }
        fnAccount.setTotalAmount(amount);
        updateSysFnAccount(fnAccount);
        SysFnAccountBill sysFnAccountBill=new SysFnAccountBill();
        sysFnAccountBill.setAccountNo(deductionAccountRequest.getAccountNo());
        sysFnAccountBill.setBillAmount(deductionAccountRequest.getDeductionAccount());
        sysFnAccountBill.setBillType(1);
        sysFnAccountBill.setBillRemark(deductionAccountRequest.getDeductionRemark());
        sysFnAccountBillService.insertSysFnAccountBill(sysFnAccountBill);
        return sysFnAccountBill.getBillNo();
    }
}
