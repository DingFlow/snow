package com.snow.system.domain;

import java.util.Date;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 离职申请单对象 sys_oa_resign
 * 
 * @author 没用的阿吉
 * @date 2021-03-10
 */
@Data
public class SysOaResign extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 离职单号 */
    @Excel(name = "离职单号")
    private String resignNo;

    /** 离职标题 */
    @Excel(name = "离职标题")
    private String name;

    /** 离职理由 */
    @Excel(name = "离职理由")
    private String reason;

    /** 离职去向 */
    @Excel(name = "离职去向")
    private String resignPlaceGo;

    /** 交接人 */

    private String transitionPerson;

    /**
     * 交接人名字
     */
    @Excel(name = "交接人")
    private String transitionPersonName;

    /** 离职时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "离职时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date resignTime;

    /** 离职类型（0--自动离职，1-劳动合同期满,2--被辞退，3--其他） */
    @Excel(name = "离职类型", readConverterExp = "0=--自动离职，1-劳动合同期满,2--被辞退，3--其他")
    private Integer resignType;

    /** 流程状态（0--待发起，1-审批中，2--审批通过，3--已驳回，4--作废） */
    @Excel(name = "流程状态", readConverterExp = "0=--待发起，1-审批中，2--审批通过，3--已驳回，4--作废")
    private Integer processStatus;

    /** 离职申请人 */
    private String applyPerson;

    /** 离职申请人 */
    private String applyPersonName;
    /** 删除标识（0--正常，1--删除） */
    private Integer isDelete;

    /** 附件 */
    @Excel(name = "附件")
    private String fileUrl;

}
