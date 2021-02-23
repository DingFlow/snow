package com.snow.flowable.listener.purchaseOrder;



import com.snow.flowable.domain.purchaseOrder.PurchaseOrderForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-01-10 20:05
 **/
@Service("purManagerFlowListener")
@Slf4j
public class PurchaseManagerFlowListener extends AbstractExecutionListener<PurchaseOrderForm> {


    @Override
    protected void process() {
        PurchaseOrderForm appForms = getAppForms();
        //设置参数(应该在任务节点设置参数)
        setVariable("totalPrice",appForms.getTotalPrice());
        setVariable("price",1000);

    }
}
