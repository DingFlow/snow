package com.snow.flowable.common.enums;

/**
 * @author qimingjin
 * @Title: 流程定义枚举
 * @Description:
 * @date 2020/12/7 15:54
 */
public enum  FlowDefEnum {

    SNOW_OA_LEAVE("snow_oa_leave", "请假申请流程"),

    PURCHASE_ORDER_PROCESS("purchase_order_process", "采购审批流程"),

    SNOW_OA_RESIGN_PROCESS("snow_oa_resign", "snow系统离职审批流程"),

    SNOW_OA_CUSTOMER_ADMITTANCE("snow_oa_customer_admittance","客户准入流程"),

    SNOW_FN_PAYMENT("snow_fn_payment","付款审批流程");

    private final String code;
    private final String info;

    FlowDefEnum(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }

    public static FlowDefEnum getByCode(String code) {
        for (FlowDefEnum value : FlowDefEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
