package com.snow.flowable.domain.resign;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.domain.AppForm;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-10 21:14
 **/
@Data
public class SysOaResignForm extends AppForm implements Serializable {

    private static final long serialVersionUID = -9152455611987903487L;
    /** id */
    private Integer id;

    /** 离职单号 */
    private String resignNo;

    /** 离职标题 */
    private String name;

    /** 离职理由 */
    private String reason;

    /** 离职去向 */
    private String resignPlaceGo;

    /** 交接人 */
    private String transitionPerson;

    /**
     * 交接人名字
     */
    private String transitionPersonName;

    /** 离职时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date resignTime;

    /** 离职类型（0--自动离职，1-劳动合同期满,2--被辞退，3--其他） */
    private Integer resignType;

    /** 流程状态（0--待发起，1-审批中，2--审批通过，3--已驳回，4--作废） */
    private Integer processStatus;

    /** 离职申请人 */
    private String applyPerson;
    /**
     * 离职人名字
     */
    private String applyPersonName;

    /** 删除标识（0--正常，1--删除） */
    private Integer isDelete;

    /** 附件 */
    private String fileUrl;

    @Override
    public FlowDefEnum getFlowDef() {
        return FlowDefEnum.SNOW_OA_RESIGN_PROCESS;
    }


    /**
     *交接人参数
     */
    public static final String  TRANSITION_PERSON="transitionPerson";
}
