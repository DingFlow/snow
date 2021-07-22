package com.snow.flowable.listener.purchaseOrder;

import com.google.common.collect.Lists;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.common.skipTask.AbstractSkipTask;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

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


    @Override
    public boolean canSkip(Task task) {
        return false;
    }

    @Override
    protected String getProcessDefinitionKey() {
        return FlowDefEnum.PURCHASE_ORDER_PROCESS.getCode();
    }

    @Override
    protected List<String> getTaskKeys() {
        return Lists.newArrayList("userTask3");
    }
}
