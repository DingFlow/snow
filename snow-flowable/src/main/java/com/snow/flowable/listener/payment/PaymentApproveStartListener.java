package com.snow.flowable.listener.payment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.domain.payment.PaymentForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import com.snow.system.domain.SysFnPayment;
import com.snow.system.service.ISysFnPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2022/2/21 15:29
 */
@Service("paymentApproveStartListener")
@Slf4j
@RequiredArgsConstructor
public class PaymentApproveStartListener extends AbstractExecutionListener<PaymentForm> {
    private static final long serialVersionUID = -912045241861715507L;

    private final ISysFnPaymentService sysFnPaymentService;

    @Override
    protected void process() {
        String businessKey = getBusinessKey();
        SysFnPayment sysFnPayment=new SysFnPayment();
        sysFnPayment.setProcessStatus((long)ProcessStatus.CHECKING.ordinal());
        LambdaQueryWrapper<SysFnPayment> lambda = new QueryWrapper<SysFnPayment>().lambda();
        lambda.eq(SysFnPayment::getPaymentNo,businessKey);
        sysFnPaymentService.update(sysFnPayment,lambda);
    }
}
