package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title: 交易类型枚举
 * @Description:
 * @date 2021/2/26 11:25
 */
public enum FinanceTradeType {
    IMMEDIATELY_ACCOUNT(1, "即时到账交易"),
    ALIPAY_SECURED_TRANSACTION(2, "支付宝担保交易"),
    CONSUMPTION(3, "商户消费"),
    TRANSFER_ACCOUNTS(4, "转账"),
    WX_RED_PACKETS(5, "微信红包"),
    WX_GROUP_RED_PACKETS(6, "微信红包（群红包）"),
    WX_SINGLE_RED_PACKETS(7, "微信红包（单发）"),
    WX_RED_PACKETS_REFUND(8, "微信红包-退款"),
    SCANNER_QR_CODE_PAYMENT(9, "扫二维码付款"),
    QR_CODE_PAYMENT(10, "二维码收款"),
    GROUP_COLLECTION(11, "群收款");
    private final Integer code;
    private final String info;

    FinanceTradeType(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }

    public static Integer getFinanceTradeTypeCode(String info){
        FinanceTradeType[] values = FinanceTradeType.values();
        for (FinanceTradeType type : values) {
            if(type.getInfo().equals(info)){
                return type.code;
            }
        }
        return null;
    }

    public static String getFinanceTradeTypeInfo(Integer code){
        FinanceTradeType[] values = FinanceTradeType.values();
        for (FinanceTradeType type : values) {
            if(type.getCode()==code){
                return type.info;
            }
        }
        return null;
    }
}
