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
import com.snow.system.mapper.SysFnPaymentMapper;
import com.snow.system.domain.SysFnPayment;
import com.snow.system.service.ISysFnPaymentService;
import com.snow.common.core.text.Convert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;

/**
 * 支付申请Service业务层处理
 *
 * @author Agee
 * @date 2022-02-19
 */
@Service
public class SysFnPaymentServiceImpl extends ServiceImpl<SysFnPaymentMapper, SysFnPayment> implements ISysFnPaymentService {
    @Resource
    private SysFnPaymentMapper sysFnPaymentMapper;

    @Resource
    private SysSequenceServiceImpl sysSequenceService;
    /**
     * 查询支付申请
     *
     * @param id 支付申请ID
     * @return 支付申请
     */
    @Override
    public SysFnPayment selectSysFnPaymentById(Long id) {
        return sysFnPaymentMapper.selectById(id);
    }

    /**
     * 查询支付申请列表
     *
     * @param sysFnPayment 支付申请
     * @return 支付申请
     */
    @Override
    public List<SysFnPayment> selectSysFnPaymentList(SysFnPayment sysFnPayment) {
        LambdaQueryWrapper<SysFnPayment> lambda = new QueryWrapper<SysFnPayment>().lambda();
        lambda.like(ObjectUtil.isNotEmpty(sysFnPayment.getPaymentNo()),SysFnPayment::getPaymentNo,sysFnPayment.getPaymentNo());
        lambda.eq(ObjectUtil.isNotEmpty(sysFnPayment.getPaymentTitle()),SysFnPayment::getPaymentTitle,sysFnPayment.getPaymentTitle());
        lambda.eq(ObjectUtil.isNotEmpty(sysFnPayment.getRelateNo()),SysFnPayment::getRelateNo,sysFnPayment.getRelateNo());
        lambda.eq(ObjectUtil.isNotEmpty(sysFnPayment.getRelateNoType()),SysFnPayment::getRelateNoType,sysFnPayment.getRelateNoType());
        lambda.eq(ObjectUtil.isNotEmpty(sysFnPayment.getPaymentTime()),SysFnPayment::getPaymentTime,sysFnPayment.getPaymentTime());
        lambda.eq(ObjectUtil.isNotEmpty(sysFnPayment.getPaymentStatus()),SysFnPayment::getPaymentStatus,sysFnPayment.getPaymentStatus());
        lambda.eq(ObjectUtil.isNotEmpty(sysFnPayment.getProcessStatus()),SysFnPayment::getProcessStatus,sysFnPayment.getProcessStatus());
        lambda.eq(ObjectUtil.isNotEmpty(sysFnPayment.getPaymentRemark()),SysFnPayment::getPaymentRemark,sysFnPayment.getPaymentRemark());
        return sysFnPaymentMapper.selectList(lambda);
    }

    /**
     * 新增支付申请
     *
     * @param sysFnPayment 支付申请
     * @return 结果
     */
    @Override
    public int insertSysFnPayment(SysFnPayment sysFnPayment) {
        sysFnPayment.setCreateTime(DateUtils.getNowDate());
        String paymentNo = sysSequenceService.getNewSequenceNo(SequenceConstants.FN_PAYMENT_NO);
        sysFnPayment.setPaymentNo(paymentNo);
        return sysFnPaymentMapper.insert(sysFnPayment);
    }

    /**
     * 修改支付申请
     *
     * @param sysFnPayment 支付申请
     * @return 结果
     */
    @Override
    public int updateSysFnPayment(SysFnPayment sysFnPayment) {
        sysFnPayment.setUpdateTime(DateUtils.getNowDate());

        return sysFnPaymentMapper.updateById(sysFnPayment);
    }

    /**
     * 删除支付申请对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     @Override
     public int deleteSysFnPaymentByIds(String ids) {
        return sysFnPaymentMapper.deleteBatchIds(Convert.toStrList(ids));
     }

    /**
     * 删除支付申请信息
     *
     * @param id 支付申请ID
     * @return 结果
     */
    @Override
    public int deleteSysFnPaymentById(Long id) {
        return sysFnPaymentMapper.deleteById(id);
    }
}
