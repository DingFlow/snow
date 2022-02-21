package com.snow.flowable.listener.payment;

import com.snow.flowable.domain.payment.PaymentForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2022/2/21 15:30
 */
@Slf4j
@Service("paymentApproveRejectListener")
public class PaymentApproveRejectListener extends AbstractExecutionListener<PaymentForm> {
    private static final long serialVersionUID = -1783957935516480728L;

    @Override
    protected void process() {

    }
}
