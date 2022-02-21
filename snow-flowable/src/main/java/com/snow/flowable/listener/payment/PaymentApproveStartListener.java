package com.snow.flowable.listener.payment;

import com.snow.flowable.domain.payment.PaymentForm;
import com.snow.flowable.listener.AbstractExecutionListener;
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

    @Override
    protected void process() {

    }
}
