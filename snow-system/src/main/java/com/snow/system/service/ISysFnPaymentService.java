package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysFnPayment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 支付申请Service接口
 * 
 * @author Agee
 * @date 2022-02-19
 */
public interface ISysFnPaymentService extends IService<SysFnPayment>
{
    /**
     * 查询支付申请
     * 
     * @param id 支付申请ID
     * @return 支付申请
     */
    public SysFnPayment selectSysFnPaymentById(Long id);

    /**
     * 查询支付申请列表
     * 
     * @param sysFnPayment 支付申请
     * @return 支付申请集合
     */
    public List<SysFnPayment> selectSysFnPaymentList(SysFnPayment sysFnPayment);

    /**
     * 新增支付申请
     * 
     * @param sysFnPayment 支付申请
     * @return 结果
     */
    public int insertSysFnPayment(SysFnPayment sysFnPayment);

    /**
     * 修改支付申请
     * 
     * @param sysFnPayment 支付申请
     * @return 结果
     */
    public int updateSysFnPayment(SysFnPayment sysFnPayment);

    /**
     * 批量删除支付申请
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFnPaymentByIds(String ids);

    /**
     * 删除支付申请信息
     * 
     * @param id 支付申请ID
     * @return 结果
     */
    public int deleteSysFnPaymentById(Long id);
}
