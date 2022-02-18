package com.snow.system.service.impl;

import java.util.List;
import java.util.ArrayList;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.snow.common.constant.SequenceConstants;
import com.snow.common.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysFnAccountBillMapper;
import com.snow.system.domain.SysFnAccountBill;
import com.snow.system.service.ISysFnAccountBillService;
import com.snow.common.core.text.Convert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;

/**
 * 账户流水详情Service业务层处理
 *
 * @author Agee
 * @date 2022-02-16
 */
@Service
public class SysFnAccountBillServiceImpl extends ServiceImpl<SysFnAccountBillMapper, SysFnAccountBill> implements ISysFnAccountBillService {
    @Resource
    private SysFnAccountBillMapper sysFnAccountBillMapper;

    @Resource
    private SysSequenceServiceImpl sysSequenceService;


    /**
     * 查询账户流水详情
     *
     * @param id 账户流水详情ID
     * @return 账户流水详情
     */
    @Override
    public SysFnAccountBill selectSysFnAccountBillById(Long id) {
        return sysFnAccountBillMapper.selectById(id);
    }

    /**
     * 查询账户流水详情列表
     *
     * @param sysFnAccountBill 账户流水详情
     * @return 账户流水详情
     */
    @Override
    public List<SysFnAccountBill> selectSysFnAccountBillList(SysFnAccountBill sysFnAccountBill) {
        LambdaQueryWrapper<SysFnAccountBill> lambda = new QueryWrapper<SysFnAccountBill>().lambda();
        lambda.like(ObjectUtil.isNotEmpty(sysFnAccountBill.getBillNo()),SysFnAccountBill::getBillNo,sysFnAccountBill.getBillNo());
        lambda.like(ObjectUtil.isNotEmpty(sysFnAccountBill.getAccountNo()),SysFnAccountBill::getAccountNo,sysFnAccountBill.getAccountNo());
        lambda.eq(ObjectUtil.isNotEmpty(sysFnAccountBill.getBillType()),SysFnAccountBill::getBillType,sysFnAccountBill.getBillType());
        return sysFnAccountBillMapper.selectList(lambda);
    }

    /**
     * 新增账户流水详情
     *
     * @param sysFnAccountBill 账户流水详情
     * @return 结果
     */
    @Override
    public int insertSysFnAccountBill(SysFnAccountBill sysFnAccountBill) {
        String billNo = sysSequenceService.getNewSequenceNo(SequenceConstants.FN_ACCOUNT_BILL_NO);
        sysFnAccountBill.setBillNo(billNo);
        sysFnAccountBill.setCreateTime(DateUtils.getNowDate());
        return sysFnAccountBillMapper.insert(sysFnAccountBill);
    }

    /**
     * 修改账户流水详情
     *
     * @param sysFnAccountBill 账户流水详情
     * @return 结果
     */
    @Override
    public int updateSysFnAccountBill(SysFnAccountBill sysFnAccountBill) {
        sysFnAccountBill.setUpdateTime(DateUtils.getNowDate());
        return sysFnAccountBillMapper.updateById(sysFnAccountBill);
    }

    /**
     * 删除账户流水详情对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     @Override
     public int deleteSysFnAccountBillByIds(String ids) {
        return sysFnAccountBillMapper.deleteBatchIds(Convert.toStrList(ids));
     }

    /**
     * 删除账户流水详情信息
     *
     * @param id 账户流水详情ID
     * @return 结果
     */
    @Override
    public int deleteSysFnAccountBillById(Long id) {
        return sysFnAccountBillMapper.deleteById(id);
    }
}
