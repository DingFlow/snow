package com.snow.flowable.listener.purchaseOrder;

import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.domain.purchaseOrder.PurchaseOrderForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import com.snow.system.domain.PurchaseOrderMain;
import com.snow.system.mapper.PurchaseOrderMainMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-01-09 21:15
 **/
@Service
@Slf4j
public class PurchaseOrderStartListener extends AbstractExecutionListener<PurchaseOrderForm> {

    @Autowired
    private PurchaseOrderMainMapper purchaseOrderMainMapper;

    @Override
    protected void process() {
        String businessKey= getBusinessKey();
        PurchaseOrderMain purchaseOrderMain=new PurchaseOrderMain();
        purchaseOrderMain.setProcessStatus((long)ProcessStatus.CHECKING.ordinal());
        purchaseOrderMain.setOrderNo(businessKey);
        purchaseOrderMainMapper.updatePurchaseOrderMainByOrderNo(purchaseOrderMain);

    }
}
