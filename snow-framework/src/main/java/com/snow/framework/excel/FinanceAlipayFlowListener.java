package com.snow.framework.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.snow.common.enums.FinanceTradeType;
import com.snow.common.exception.BusinessException;
import com.snow.common.utils.DateUtils;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.bean.BeanUtils;
import com.snow.system.domain.FinanceAlipayFlow;
import com.snow.system.domain.FinanceAlipayFlowImport;
import com.snow.system.domain.SysUser;
import com.snow.system.service.IFinanceAlipayFlowService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/4 10:43
 */
@Slf4j
public class FinanceAlipayFlowListener extends AnalysisEventListener<FinanceAlipayFlowImport> {


    private IFinanceAlipayFlowService financeAlipayFlowService;

    /**
     * 导入人
     */
    private SysUser sysUser;

    /** 交易主体账户 */
    private String tradeAccount;


    /** 交易真实姓名 */
    private String tradeRealName;

    /**
     * 账单类型
     */
    private Integer billType;

    //创建list集合封装最终的数据
    List<FinanceAlipayFlowImport> list = new ArrayList<>();

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     */
    public FinanceAlipayFlowListener(IFinanceAlipayFlowService financeAlipayFlowService, SysUser sysUser, String tradeAccount, String tradeRealName, Integer billType) {
        this.financeAlipayFlowService = financeAlipayFlowService;
        this.sysUser=sysUser;
        this.tradeAccount=tradeAccount;
        this.tradeRealName=tradeRealName;
        this.billType=billType;
    }

    //一行一行去读取excle内容
    @Override
    public void invoke(FinanceAlipayFlowImport financeAlipayFlowImport, AnalysisContext analysisContext) {
        //没有读取到金额直接返回
        if(StringUtils.isNull(financeAlipayFlowImport.getTradePrice())){
            return;
        }
        list.add(financeAlipayFlowImport);
    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        String firstHead = headMap.get(0);
        Integer rowIndex = context.readRowHolder().getRowIndex();
        if(!firstHead.equals("支付宝交易记录明细查询")&&rowIndex==0){
            throw new BusinessException("非标准化模板请勿导入，请下载标准模板或使用支付宝导出的原账单模板");
        }
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
    }

    /**
     * 保存数据
     */
    public void saveData(){

        List<FinanceAlipayFlow> financeAlipayFlowList = list.stream().map(t -> {
            //数据校验（交易号）
            FinanceAlipayFlow financeAlipayFlow1 = financeAlipayFlowService.selectFinanceAlipayFlowByTradeNo(t.getTradeNo());
            if(StringUtils.isNotNull(financeAlipayFlow1)){
                throw new BusinessException("交易号：【"+t.getTradeNo()+"】已存在请勿重复导入数据");
            }
            FinanceAlipayFlow financeAlipayFlow = new FinanceAlipayFlow();
            BeanUtils.copyProperties(t, financeAlipayFlow);
            String payTime = t.getPayTime();
            String tradeCreateTime = t.getTradeCreateTime();
            String lastModifyTime = t.getLastModifyTime();
            if (StringUtils.isNotEmpty(payTime)) {
                Date date = DateUtils.parseDate(payTime);
                financeAlipayFlow.setPayTime(date);
            }
            if (StringUtils.isNotEmpty(tradeCreateTime)) {
                Date date = DateUtils.parseDate(tradeCreateTime);
                financeAlipayFlow.setTradeCreateTime(date);
            }
            if (StringUtils.isNotEmpty(lastModifyTime)) {
                Date date = DateUtils.parseDate(lastModifyTime);
                financeAlipayFlow.setLastModifyTime(date);
            }
            String capitalStatus = t.getCapitalStatus();
            if (StringUtils.isEmpty(capitalStatus)) {
                financeAlipayFlow.setCapitalStatus(0);
            } else if (capitalStatus.equals("已支出")) {
                financeAlipayFlow.setCapitalStatus(1);
            } else if (capitalStatus.equals("已收入")) {
                financeAlipayFlow.setCapitalStatus(2);
            } else if (capitalStatus.equals("资金转移")) {
                financeAlipayFlow.setCapitalStatus(3);
            } else {
                financeAlipayFlow.setCapitalStatus(10);
            }

            String incomeExpenditureType = t.getIncomeExpenditureType();
            if (StringUtils.isEmpty(incomeExpenditureType)) {
                financeAlipayFlow.setIncomeExpenditureType(0);
                financeAlipayFlow.setRealIncomeExpenditureType(0);
            } else if (incomeExpenditureType.equals("收入")) {
                financeAlipayFlow.setIncomeExpenditureType(2);
                financeAlipayFlow.setRealIncomeExpenditureType(2);
            } else if (incomeExpenditureType.equals("支出")) {
                financeAlipayFlow.setIncomeExpenditureType(1);
                financeAlipayFlow.setRealIncomeExpenditureType(1);
            }
            String tradeStatus = t.getTradeStatus();
            if (StringUtils.isEmpty(tradeStatus)) {
                financeAlipayFlow.setTradeStatus(null);
            } else if (tradeStatus.equals("交易成功")||tradeStatus.equals("已存入零钱")||tradeStatus.equals("支付成功")) {
                financeAlipayFlow.setTradeStatus(1);
            } else if (tradeStatus.equals("交易关闭")) {
                financeAlipayFlow.setTradeStatus(2);
            } else if (tradeStatus.equals("还款成功")) {
                financeAlipayFlow.setTradeStatus(3);
            } else if (tradeStatus.equals("退款成功")||tradeStatus.contains("退款")) {
                financeAlipayFlow.setTradeStatus(4);
            }
            String tradeType = t.getTradeType();
            if (StringUtils.isEmpty(tradeType)) {
                financeAlipayFlow.setTradeType(null);
            }else {
                Integer financeTradeTypeCode = FinanceTradeType.getFinanceTradeTypeCode(tradeType);
                financeAlipayFlow.setTradeType(financeTradeTypeCode);
            }
            financeAlipayFlow.setCreateBy(sysUser.getUserName());
            financeAlipayFlow.setBelongUserId(sysUser.getUserId());
            financeAlipayFlow.setTradeRealName(tradeRealName);
            financeAlipayFlow.setTradeAccount(tradeAccount);
            financeAlipayFlow.setBillType(billType);
            log.info("转化后的对象:financeAlipayFlow{}", com.alibaba.fastjson.JSON.toJSONString(financeAlipayFlow));
            return financeAlipayFlow;
        }).collect(Collectors.toList());
        financeAlipayFlowService.insertBatchFinanceAlipayFlow(financeAlipayFlowList);
    }

}
