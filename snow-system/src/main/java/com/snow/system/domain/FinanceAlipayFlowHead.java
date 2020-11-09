package com.snow.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-09 21:47
 **/
public class FinanceAlipayFlowHead {

    /** 交易主体账户 */
    @ExcelProperty(index = 0)
    private String tradeAccount;


    /** 交易真实姓名 */
    @ExcelProperty(index = 1)
    private String tradeRealName;

    public String getTradeAccount() {
        return tradeAccount;
    }

    public void setTradeAccount(String tradeAccount) {
        this.tradeAccount = tradeAccount;
    }

    public String getTradeRealName() {
        return tradeRealName;
    }

    public void setTradeRealName(String tradeRealName) {
        this.tradeRealName = tradeRealName;
    }
}
