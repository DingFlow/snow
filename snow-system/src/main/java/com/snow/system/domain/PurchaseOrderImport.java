package com.snow.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.snow.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/7 15:55
 */
@Data
public class PurchaseOrderImport {

    /** 货物编号 */
    @ExcelProperty(index = 0)
    private String goodsNo;
    /** 名称 */
    @ExcelProperty(index = 1)
    private String goodsName;

    /** 规格 */
    @ExcelProperty(index = 2)
    private String goodsSize;

    /** 数量 */
    @ExcelProperty(index = 3)
    private BigDecimal goodsQuantity;

    /** 单价 */
    @ExcelProperty(index = 4)
    private BigDecimal goodsPrice;
    /** 备注 */
    @ExcelProperty(index = 5)
    private String remark;
    /** 总价 */

    private BigDecimal totalPrice;


}
