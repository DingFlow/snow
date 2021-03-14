package com.snow.flowable.domain.leave;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.domain.AppForm;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qimingjin
 * @Title: 请假申请表单
 * @Description:
 * @date 2020/12/7 11:12
 */
@Data
public class SysOaLeaveForm extends AppForm implements Serializable {

    private static final long serialVersionUID = -208387201380193517L;
    /** id */
    private Integer id;

    /** 请假名称 */
    private String name;

    /** 请假理由 */
    private String reason;

    /** 开始时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 流程状态（0--待审批，1-审批中，2--审批通过，3--已驳回） */
    private Integer processStatus;

    /** 流程实例ID */

    private String processInstanceId;

    /** 申请人 */

    private String applyPerson;

    /** 附件 */

    private String fileUrl;
    /** 请假单号 */

    private String leaveNo;

    @Override
    public FlowDefEnum getFlowDef() {
        return FlowDefEnum.SNOW_OA_LEAVE;
    }
}
