package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.FinanceAlipayFlow;

/**
 * 财务支付宝流水Service接口
 * 
 * @author snow
 * @date 2020-11-09
 */
public interface IFinanceAlipayFlowService 
{
    /**
     * 查询财务支付宝流水
     * 
     * @param id 财务支付宝流水ID
     * @return 财务支付宝流水
     */
    public FinanceAlipayFlow selectFinanceAlipayFlowById(Long id);

    /**
     * 查询财务支付宝流水列表
     * 
     * @param financeAlipayFlow 财务支付宝流水
     * @return 财务支付宝流水集合
     */
    public List<FinanceAlipayFlow> selectFinanceAlipayFlowList(FinanceAlipayFlow financeAlipayFlow);

    /**
     * 新增财务支付宝流水
     * 
     * @param financeAlipayFlow 财务支付宝流水
     * @return 结果
     */
    public int insertFinanceAlipayFlow(FinanceAlipayFlow financeAlipayFlow);

    /**
     * 修改财务支付宝流水
     * 
     * @param financeAlipayFlow 财务支付宝流水
     * @return 结果
     */
    public int updateFinanceAlipayFlow(FinanceAlipayFlow financeAlipayFlow);

    /**
     * 批量删除财务支付宝流水
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFinanceAlipayFlowByIds(String ids);

    /**
     * 删除财务支付宝流水信息
     * 
     * @param id 财务支付宝流水ID
     * @return 结果
     */
    public int deleteFinanceAlipayFlowById(Long id);
}
