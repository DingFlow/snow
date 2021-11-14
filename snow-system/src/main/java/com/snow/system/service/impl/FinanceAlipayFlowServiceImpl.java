package com.snow.system.service.impl;

import com.snow.common.core.text.Convert;
import com.snow.common.enums.FinanceTradeType;
import com.snow.common.utils.DateUtils;
import com.snow.system.domain.FinanceAlipayFlow;
import com.snow.system.domain.FinanceBillSituationVO;
import com.snow.system.domain.SysUser;
import com.snow.system.mapper.FinanceAlipayFlowMapper;
import com.snow.system.mapper.SysUserMapper;
import com.snow.system.service.IFinanceAlipayFlowService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

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
    @Autowired
    private SysUserMapper sysUserMapper;

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
    public FinanceAlipayFlow selectFinanceAlipayFlowByTradeNo(String tradeNo){
        return financeAlipayFlowMapper.selectFinanceAlipayFlowByTradeNo(tradeNo);
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
        List<FinanceAlipayFlow> financeAlipayFlowList = financeAlipayFlowMapper.selectFinanceAlipayFlowList(financeAlipayFlow);
        financeAlipayFlowList.parallelStream().forEach(t->{
            Long belongUserId = t.getBelongUserId();
            SysUser sysUser = sysUserMapper.selectUserById(belongUserId);
            t.setBelongUserName(sysUser.getUserName());
        });
        return financeAlipayFlowList;
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

    @Override
    public int insertBatchFinanceAlipayFlow(List<FinanceAlipayFlow> financeAlipayFlowList) {
        return financeAlipayFlowMapper.insertBatchFinanceAlipayFlow(financeAlipayFlowList);
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

    /**
     * 获取账单消费概况
     * @param userId
     * @return
     */
    @Override
    public FinanceBillSituationVO getFinanceAlipayFlowSituation(Long userId) {
        FinanceAlipayFlow financeAlipayFlow=new FinanceAlipayFlow();
        financeAlipayFlow.setBelongUserId(userId);
        FinanceBillSituationVO.FinanceBillSituationVOBuilder builder = FinanceBillSituationVO.builder();
        List<FinanceAlipayFlow> financeAlipayFlowList = financeAlipayFlowMapper.selectFinanceAlipayFlowList(financeAlipayFlow);
        if(CollectionUtils.isNotEmpty(financeAlipayFlowList)) {
            BigDecimal immediatelyAccountTotal = financeAlipayFlowList.stream().filter(t -> t.getTradeType() == FinanceTradeType.IMMEDIATELY_ACCOUNT.getCode()).map(FinanceAlipayFlow::getTradePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            builder.immediatelyAccountTotal(immediatelyAccountTotal);

            BigDecimal alipaySecuredTransactionTotal = financeAlipayFlowList.stream().filter(t -> t.getTradeType() == FinanceTradeType.ALIPAY_SECURED_TRANSACTION.getCode()).map(FinanceAlipayFlow::getTradePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            builder.alipaySecuredTransactionTotal(alipaySecuredTransactionTotal);

            BigDecimal consumptionTotal = financeAlipayFlowList.stream().filter(t -> t.getTradeType() == FinanceTradeType.CONSUMPTION.getCode()).map(FinanceAlipayFlow::getTradePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            builder.consumptionTotal(consumptionTotal);

            BigDecimal transferAccountsTotal = financeAlipayFlowList.stream().filter(t -> t.getTradeType() == FinanceTradeType.TRANSFER_ACCOUNTS.getCode()).map(FinanceAlipayFlow::getTradePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            builder.transferAccountsTotal(transferAccountsTotal);

            BigDecimal wxRedPacketsTotal = financeAlipayFlowList.stream().filter(t ->
                    t.getTradeType() == FinanceTradeType.WX_RED_PACKETS.getCode() || t.getTradeType() == FinanceTradeType.WX_GROUP_RED_PACKETS.getCode() || t.getTradeType() == FinanceTradeType.WX_SINGLE_RED_PACKETS.getCode()
            ).map(FinanceAlipayFlow::getTradePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            builder.wxRedPacketsTotal(wxRedPacketsTotal);

            BigDecimal redPacketsRefundTotal = financeAlipayFlowList.stream().filter(t -> t.getTradeType() == FinanceTradeType.WX_RED_PACKETS_REFUND.getCode()).map(FinanceAlipayFlow::getTradePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            builder.redPacketsRefundTotal(redPacketsRefundTotal);

            BigDecimal scannerQrCodePaymentTotal = financeAlipayFlowList.stream().filter(t -> t.getTradeType() == FinanceTradeType.SCANNER_QR_CODE_PAYMENT.getCode()).map(FinanceAlipayFlow::getTradePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            builder.scannerQrCodePaymentTotal(scannerQrCodePaymentTotal);

            BigDecimal qrCodePaymentTotal = financeAlipayFlowList.stream().filter(t -> t.getTradeType() == FinanceTradeType.QR_CODE_PAYMENT.getCode()).map(FinanceAlipayFlow::getTradePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            builder.qrCodePaymentTotal(qrCodePaymentTotal);
            //支出
            BigDecimal expenditureTotal = financeAlipayFlowList.stream().filter(t -> t.getIncomeExpenditureType() == 1).map(FinanceAlipayFlow::getTradePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            builder.expenditureTotal(expenditureTotal);
            //收入
            BigDecimal inComeTotal = financeAlipayFlowList.stream().filter(t -> t.getIncomeExpenditureType() == 2).map(FinanceAlipayFlow::getTradePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            builder.inComeTotal(inComeTotal);
        }else {
            builder.immediatelyAccountTotal(BigDecimal.ZERO)
                    .alipaySecuredTransactionTotal(BigDecimal.ZERO)
                    .alipaySecuredTransactionTotal(BigDecimal.ZERO)
                    .consumptionTotal(BigDecimal.ZERO)
                    .expenditureTotal(BigDecimal.ZERO)
                    .inComeTotal(BigDecimal.ZERO)
                    .qrCodePaymentTotal(BigDecimal.ZERO)
                    .redPacketsRefundTotal(BigDecimal.ZERO)
                    .scannerQrCodePaymentTotal(BigDecimal.ZERO)
                    .redPacketsRefundTotal(BigDecimal.ZERO)
                    .transferAccountsTotal(BigDecimal.ZERO)
                    .wxRedPacketsTotal(BigDecimal.ZERO);
        }
        return builder.build();
    }

    @Override
    public LinkedHashMap<String,BigDecimal> getFinanceAlipayFlowByYearAndMonth(FinanceAlipayFlow financeAlipayFlow) {

        return financeAlipayFlowMapper.getFinanceAlipayFlowByYearAndMonth(financeAlipayFlow);
    }
}
