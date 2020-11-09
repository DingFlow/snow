package com.snow.system.service.impl;

import java.util.List;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.FinanceAlipayFlowMapper;
import com.snow.system.domain.FinanceAlipayFlow;
import com.snow.system.service.IFinanceAlipayFlowService;
import com.snow.common.core.text.Convert;

/**
 * 财务支付宝流水Service业务层处理
 * 
 * @author snow
 * @date 2020-11-09
 */
@Service
public class FinanceAlipayFlowServiceImpl implements IFinanceAlipayFlowService 
{
    @Autowired
    private FinanceAlipayFlowMapper financeAlipayFlowMapper;

    /**
     * 查询财务支付宝流水
     * 
     * @param id 财务支付宝流水ID
     * @return 财务支付宝流水
     */
    @Override
    public FinanceAlipayFlow selectFinanceAlipayFlowById(Long id)
    {
        return financeAlipayFlowMapper.selectFinanceAlipayFlowById(id);
    }

    /**
     * 查询财务支付宝流水列表
     * 
     * @param financeAlipayFlow 财务支付宝流水
     * @return 财务支付宝流水
     */
    @Override
    public List<FinanceAlipayFlow> selectFinanceAlipayFlowList(FinanceAlipayFlow financeAlipayFlow)
    {
        return financeAlipayFlowMapper.selectFinanceAlipayFlowList(financeAlipayFlow);
    }

    /**
     * 新增财务支付宝流水
     * 
     * @param financeAlipayFlow 财务支付宝流水
     * @return 结果
     */
    @Override
    public int insertFinanceAlipayFlow(FinanceAlipayFlow financeAlipayFlow)
    {
        financeAlipayFlow.setCreateTime(DateUtils.getNowDate());
        return financeAlipayFlowMapper.insertFinanceAlipayFlow(financeAlipayFlow);
    }

    /**
     * 修改财务支付宝流水
     * 
     * @param financeAlipayFlow 财务支付宝流水
     * @return 结果
     */
    @Override
    public int updateFinanceAlipayFlow(FinanceAlipayFlow financeAlipayFlow)
    {
        return financeAlipayFlowMapper.updateFinanceAlipayFlow(financeAlipayFlow);
    }

    /**
     * 删除财务支付宝流水对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFinanceAlipayFlowByIds(String ids)
    {
        return financeAlipayFlowMapper.deleteFinanceAlipayFlowByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除财务支付宝流水信息
     * 
     * @param id 财务支付宝流水ID
     * @return 结果
     */
    @Override
    public int deleteFinanceAlipayFlowById(Long id)
    {
        return financeAlipayFlowMapper.deleteFinanceAlipayFlowById(id);
    }
}
