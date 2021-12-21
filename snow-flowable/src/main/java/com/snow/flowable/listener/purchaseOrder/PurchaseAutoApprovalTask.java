package com.snow.flowable.listener.purchaseOrder;

import com.google.common.collect.Lists;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.common.skipTask.AbstractSkipTask;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author qimingjin
 * @Title: 自动跳过节点
 * @Description:
 * @date 2021/7/22 19:13
 */
@Service
@Slf4j
public class PurchaseAutoApprovalTask extends AbstractSkipTask {

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public boolean canSkip(Task task) {

        //获取采购单总金额
        BigDecimal totalPrice = runtimeService.getVariable(task.getExecutionId(), "totalPrice", BigDecimal.class);

        if(totalPrice.compareTo(new BigDecimal(100))<=0){
            log.info("@@采购单总金额小于100元，财务都不想审批了。系统你直接同意了吧！");
            return true;
        }
        return false;
    }

    @Override
    protected String getProcessDefinitionKey() {
        return FlowDefEnum.PURCHASE_ORDER_PROCESS.getCode();
    }

    @Override
    protected List<String> getTaskKeys() {
        return Lists.newArrayList("usertask6");
    }
}
