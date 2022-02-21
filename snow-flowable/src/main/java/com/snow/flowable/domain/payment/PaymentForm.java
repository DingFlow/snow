package com.snow.flowable.domain.payment;

import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.domain.AppForm;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2022/2/21 15:40
 */
public class PaymentForm extends AppForm implements Serializable {
    private static final long serialVersionUID = -2763605585222075354L;

    @Override
    public FlowDefEnum getFlowDef() {
        return FlowDefEnum.SNOW_FN_PAYMENT;
    }
}
