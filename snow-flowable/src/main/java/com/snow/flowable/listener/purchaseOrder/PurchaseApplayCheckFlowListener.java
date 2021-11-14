package com.snow.flowable.listener.purchaseOrder;

import com.snow.flowable.domain.purchaseOrder.PurchaseOrderForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/12 19:13
 */
@Service("purApplayCheckFlowListener")
public class PurchaseApplayCheckFlowListener extends AbstractExecutionListener<PurchaseOrderForm> {
    @Override
    protected void process() {

    }
}
