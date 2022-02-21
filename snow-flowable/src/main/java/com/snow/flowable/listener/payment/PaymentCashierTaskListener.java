package com.snow.flowable.listener.payment;

import com.snow.flowable.domain.payment.PaymentCashierTask;
import com.snow.flowable.listener.AbstractTaskListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Override
    protected void processTask() {
        PaymentCashierTask taskParam = getTaskParam();
    }
}
