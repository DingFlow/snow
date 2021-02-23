package com.snow.system.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
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
    @ExcelProperty(index = 0,value = { "货物编号"})
    private String goodsNo;
    /** 名称 */
    @ExcelProperty(index = 1,value = { "名称"})
    private String goodsName;

    /** 规格 */
    @ExcelProperty(index = 2,value = {"规格"})
    private String goodsSize;

    /** 数量 */
    @ExcelProperty(index = 3,value = { "数量"})
    private BigDecimal goodsQuantity;

    /** 单价 */
    @ExcelProperty(index = 4,value = { "单价"})
    private BigDecimal goodsPrice;
    /** 备注 */
    @ExcelProperty(index = 5,value = { "备注"})
    private String remark;
    /** 总价 */
    @ExcelIgnore
    private BigDecimal totalPrice;


}
