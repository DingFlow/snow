package com.snow.flowable.domain.purchaseOrder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.annotation.Excel;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.domain.AppForm;
import com.snow.system.domain.PurchaseOrderItem;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-01-09 20:59
 **/
@Data
public class PurchaseOrderForm extends AppForm implements Serializable {

    private static final long serialVersionUID = -7230087438862354631L;
    /** id */
    private Integer id;

    /** 订单号 */

    private String orderNo;

    /** 总数量 */

    private BigDecimal totalQuantity;

    /** 采购总金额 */

    private BigDecimal totalPrice;

    /** 采购标题 */

    private String title;

    /** 供应商名称 */

    private String supplierName;

    /** 订货日期 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date orderTime;

    /** 交货日期 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    /** 审批状态 */

    private Long processStatus;

    /** 采购人 */

    private String belongUser;


    private String remark;

    private List<PurchaseOrderItem> purchaseOrderItemList;

    @Override
    public FlowDefEnum getFlowDef() {
        return FlowDefEnum.PURCHASE_ORDER_PROCESS;
    }
}
