package com.snow.flowable.listener.purchaseOrder;

import cn.hutool.core.bean.BeanUtil;
import com.snow.flowable.domain.purchaseOrder.PurchaseCashierTask;
import com.snow.flowable.domain.purchaseOrder.PurchaseOrderForm;
import com.snow.flowable.listener.AbstractTaskListener;
import com.snow.flowable.service.AppFormService;
import com.snow.system.domain.SysFnPayment;
import com.snow.system.service.impl.SysFnPaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Service;

/**
 * 出纳制单节点
 */
@Service("purchaseCashierTask")
@Slf4j
@RequiredArgsConstructor
public class PurchaseCashierTaskListener extends AbstractTaskListener<PurchaseCashierTask> {
    private static final long serialVersionUID = -2370764741837973836L;

    private final SysFnPaymentServiceImpl sysFnPaymentService;

    private final AppFormService appFormService;

    @Override
    protected void processTask() {
        DelegateTask delegateTask = getDelegateTask();
        PurchaseOrderForm appForms=appFormService.getAppFromByExecutionId(delegateTask.getExecutionId());
        PurchaseCashierTask purchaseCashierTask = new PurchaseCashierTask();
        purchaseCashierTask= BeanUtil.fillBeanWithMap(getTaskLocalParams(),purchaseCashierTask, true);
        //如果成功则生成支付单
        if(getApprovalResult()){
            String assignee = delegateTask.getAssignee();
            SysFnPayment sysFnPayment=new SysFnPayment();
            sysFnPayment.setOrderPrice(appForms.getTotalPrice());
            sysFnPayment.setPaymentTitle(purchaseCashierTask.getPaymentTitle());
            sysFnPayment.setPaymentPrice(purchaseCashierTask.getPaymentPrice());
            sysFnPayment.setPaymentRemark(purchaseCashierTask.getPaymentRemark());
            sysFnPayment.setRelateNo(appForms.getOrderNo());
            sysFnPayment.setRelateNoType(1L);
            sysFnPayment.setPaymentUser(assignee);
            sysFnPaymentService.insertSysFnPayment(sysFnPayment);
        }
    }
}
