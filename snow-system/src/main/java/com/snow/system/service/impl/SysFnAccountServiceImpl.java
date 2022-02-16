package com.snow.system.service.impl;

import java.util.List;
import java.util.ArrayList;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.snow.common.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
}
