package com.snow.system.service;

import com.snow.system.domain.FinanceAlipayFlow;
import com.snow.system.domain.FinanceBillSituationVO;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

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

    public FinanceAlipayFlow selectFinanceAlipayFlowByTradeNo(String tradeNo);

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
     * 批量新增财务支付宝流水
     * @param financeAlipayFlowList
     * @return
     */
    public int insertBatchFinanceAlipayFlow(List<FinanceAlipayFlow> financeAlipayFlowList);


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

    /**
     * 获取账单消费概况
     * @param userId
     * @return
     */
    public FinanceBillSituationVO getFinanceAlipayFlowSituation(Long userId);

    public LinkedHashMap<String,BigDecimal> getFinanceAlipayFlowByYearAndMonth(FinanceAlipayFlow financeAlipayFlow);
}
