package com.snow.flowable.listener.payment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.domain.payment.PaymentCashierTask;
import com.snow.flowable.listener.AbstractTaskListener;
import com.snow.system.domain.SysFnPayment;
import com.snow.system.domain.request.DeductionAccountRequest;
import com.snow.system.service.ISysFnAccountBillService;
import com.snow.system.service.ISysFnPaymentService;
import com.snow.system.service.impl.SysFnAccountServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author qimingjin
 * @Title: 出纳任务监听
 * @Description:
 * @date 2022/2/21 15:30
 */
@Service("paymentCashierTask")
@Slf4j
@RequiredArgsConstructor
public class PaymentCashierTaskListener extends AbstractTaskListener<PaymentCashierTask>{

    private static final long serialVersionUID = 7642578894589504035L;

    private final ISysFnPaymentService sysFnPaymentService;

    private final SysFnAccountServiceImpl sysFnAccountService;

    @Override
    protected void processTask() {
        //扣款
        PaymentCashierTask taskParam = getTaskParam();
        DeductionAccountRequest deductionAccountRequest=new DeductionAccountRequest();
        deductionAccountRequest.setAccountNo(taskParam.getAccountNo());
        deductionAccountRequest.setDeductionAccount(taskParam.getPaymentPrice());
        deductionAccountRequest.setDeductionRemark(taskParam.getPaymentRemark());
        String billNo = sysFnAccountService.deductionAccount(deductionAccountRequest);
        //修改支付单
        SysFnPayment sysFnPayment=new SysFnPayment();
        sysFnPayment.setPaymentStatus(1L);
        sysFnPayment.setBillNo(billNo);
        sysFnPayment.setPaymentPrice(taskParam.getPaymentPrice());
        sysFnPayment.setPaymentTime(new Date());
        LambdaQueryWrapper<SysFnPayment> lambda = new QueryWrapper<SysFnPayment>().lambda();
        lambda.eq(SysFnPayment::getPaymentNo, getBusinessKey());
        sysFnPaymentService.update(sysFnPayment,lambda);
    }
}
