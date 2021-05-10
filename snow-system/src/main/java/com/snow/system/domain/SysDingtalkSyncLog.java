package com.snow.system.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.BaseEntity;
import com.snow.common.enums.DingTalkListenerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 钉钉同步日志记录对象 sys_dingtalk_sync_log
 * 
 * @author snow
 * @date 2020-11-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysDingtalkSyncLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志主键 */
    private Long logId;

    /** 模块标题 */
    @Excel(name = "模块标题")
    private String title;

    /** 模块类型（0其它 1用户 2部门 3角色 10回调） */
    @Excel(name = "模块类型", readConverterExp = "0=其它,1=用户,2=部门,3=角色,1=回调")
    private Integer moduleType;

    /** 业务类型（0其它 1新增 2修改 3删除） */
    @Excel(name = "业务类型", readConverterExp = "0=其它,1=新增,2=修改,3=删除")
    private Integer businessType;

    /** 方法名称 */
    @Excel(name = "方法名称")
    private String method;

    /** 请求方式 */
    @Excel(name = "请求方式")
    private String requestMethod;

    /** 操作类别（0其它 1自动同步 2手工同步） */
    @Excel(name = "操作类别", readConverterExp = "0=其它,1=自动同步,2=手工同步")
    private Integer operatorType;

    /** 操作人员 */
    @Excel(name = "操作人员")
    private String operName;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 请求URL */
    @Excel(name = "请求URL")
    private String operUrl;

    /** 主机地址 */
    @Excel(name = "主机地址")
    private String operIp;

    /** 操作地点 */
    @Excel(name = "操作地点")
    private String operLocation;

    /** 请求原参数 */
    @Excel(name = "请求原参数")
    private String operSourceParam;

    /** 请求钉钉参数 */
    @Excel(name = "请求钉钉参数")
    private String operDingtalkParam;

    /** 返回参数 */
    @Excel(name = "返回参数")
    private String jsonResult;

    /** 操作状态（0正常 1异常） */
    @Excel(name = "操作状态", readConverterExp = "0=正常,1=异常")
    private Integer status;

    /** 错误消息 */
    @Excel(name = "错误消息")
    private String errorMsg;

    /** 操作时间 */
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    private DingTalkListenerType dingTalkListenerType;
    /**
     * 日志类型（1同步到钉钉，2同步到系统）
     */
    private Integer logType;

    public SysDingtalkSyncLog(DingTalkListenerType dingTalkListenerType, String method, String operUrl, Integer operatorType, String operSourceParam, String operDingtalkParam, String jsonResult){
        this.dingTalkListenerType=dingTalkListenerType;
        this.method= method;
        this.operUrl=operUrl;
        this.operatorType=operatorType;
        this.operSourceParam= operSourceParam;
        this.operDingtalkParam=operDingtalkParam;
        this.jsonResult= jsonResult;
    }
    public SysDingtalkSyncLog(DingTalkListenerType dingTalkListenerType,String method,String operUrl,Integer operatorType,String operSourceParam,String operDingtalkParam,String jsonResult, String errorMsg){
        this.dingTalkListenerType=dingTalkListenerType;
        this.method= method;
        this.operUrl=operUrl;
        this.operatorType=operatorType;
        this.operSourceParam= operSourceParam;
        this.operDingtalkParam=operDingtalkParam;
        this.jsonResult= jsonResult;
        this.errorMsg= errorMsg;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("title", getTitle())
            .append("moduleType", getModuleType())
            .append("businessType", getBusinessType())
            .append("method", getMethod())
            .append("requestMethod", getRequestMethod())
            .append("operatorType", getOperatorType())
            .append("operName", getOperName())
            .append("deptName", getDeptName())
            .append("operUrl", getOperUrl())
            .append("operIp", getOperIp())
            .append("operLocation", getOperLocation())
            .append("operSourceParam", getOperSourceParam())
            .append("operDingtalkParam", getOperDingtalkParam())
            .append("jsonResult", getJsonResult())
            .append("status", getStatus())
            .append("errorMsg", getErrorMsg())
            .append("operTime", getOperTime())
            .toString();
    }
}
