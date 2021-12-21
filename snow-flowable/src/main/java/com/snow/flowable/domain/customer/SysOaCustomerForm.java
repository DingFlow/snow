package com.snow.flowable.domain.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.domain.AppForm;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qimingjin
 * @Title: 客户准入申请表单
 * @Description:
 * @date 2020/12/7 11:12
 */
@Data
public class SysOaCustomerForm extends AppForm implements Serializable {


    private static final long serialVersionUID = -2022152179524567406L;

    private Long id;
    /** 客户编号 */

    private String customerNo;

    /** 客户名称 */

    private String customerName;

    /** 客户详细地址 */

    private String customerAddress;

    /** 客户所在省名称 */

    private String customerProvinceName;

    /** 客户所在省 */

    private String customerProvinceCode;

    /** 客户所在市名称 */

    private String customerCityName;

    /** 客户所在市 */

    private String customerCityCode;

    /** 客户所在区名称 */

    private String customerAreaName;

    /** 客户所在区编码 */

    private String customerAreaCode;

    /** 客户状态 */

    private String customerStatus;

    /** 客户来源 */
    private String customerSource;

    /** 客户电话 */
    private String customerPhone;

    /** 客户邮件 */
    private String customerEmail;

    /** 客户所属行业 */
    private String customerIndustry;

    /** 客户负责人 */
    private String customerManager;

    /** 客户主联系人 */
    private String customerLinkeUser;

    @Override
    public FlowDefEnum getFlowDef() {
        return FlowDefEnum.SNOW_OA_CUSTOMER_ADMITTANCE;
    }
}
