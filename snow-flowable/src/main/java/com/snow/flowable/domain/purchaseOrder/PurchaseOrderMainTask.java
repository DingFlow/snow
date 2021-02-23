package com.snow.flowable.domain.purchaseOrder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import com.snow.flowable.domain.FileEntry;
import com.snow.system.domain.PurchaseOrderItem;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/13 13:47
 */
@Data
public class PurchaseOrderMainTask  extends BaseEntity implements Serializable {
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


    private Long isDelete;

    private List<PurchaseOrderItem> purchaseOrderItemList;

    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 完成任务人
     */
    private String userId;


    /**
     * 是否通过
     */
    private Boolean isStart;

    /**
     * 审批意见
     */
    private String comment;
    /**
     * 审批节点的相关文件（只在审批节点展示）
     */

    private List<FileEntry> files;

}
