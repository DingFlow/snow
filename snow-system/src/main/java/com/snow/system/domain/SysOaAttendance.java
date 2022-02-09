package com.snow.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 考勤对象 sys_oa_attendance
 * 
 * @author Agee
 * @date 2022-01-26
 */
@Data
public class SysOaAttendance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 考勤编码 */
    @Excel(name = "考勤编码")
    private String attendanceCode;

    /** 考勤来源：详见字典表sys_oa_attendance_source_type(ATM：考勤机打卡（指纹/人脸打卡）BEACON：IBeaconDING_ATM：钉钉考勤机（考勤机蓝牙打卡）USER：用户打卡BOSS：老板改签APPROVE：审批系统SYSTEM：考勤系统AUTO_CHECK：自动打卡) */
    @Excel(name = "考勤来源：详见字典表sys_oa_attendance_source_type(ATM：考勤机打卡", readConverterExp = "指=纹/人脸打卡")
    private String sourceType;

    /** 关联的审批实例ID，当该字段非空时，表示打卡记录与请假、加班等审批有关 */
    @Excel(name = "关联的审批实例ID，当该字段非空时，表示打卡记录与请假、加班等审批有关")
    private String procInstId;

    /** 关联的审批实例ID，当该字段非空时，表示打卡记录与请假、加班等审批有关 */
    @Excel(name = "关联的审批实例ID，当该字段非空时，表示打卡记录与请假、加班等审批有关")
    private String approveId;

    /** 打卡结果：详见字典表sys_oa_attendance_time_result(Normal：正常Early：早退Late：迟到SeriousLate：严重迟到Absenteeism：旷工迟到NotSigned：未打卡) */
    @Excel(name = "打卡结果：详见字典表sys_oa_attendance_time_result(Normal：正常Early：早退Late：迟到SeriousLate：严重迟到Absenteeism：旷工迟到NotSigned：未打卡)")
    private String timeResult;

    /** 位置结果：详见字典表sys_oa_attendance_location_result （Normal：范围内Outside：范围外NotSigned：未打卡） */
    @Excel(name = "位置结果：详见字典表sys_oa_attendance_location_result ", readConverterExp = "N=ormal：范围内Outside：范围外NotSigned：未打卡")
    private String locationResult;

    /** 打卡人的UserID */
    @Excel(name = "打卡人的UserID")
    private String userId;

    /** 考勤类型：详见字典表sys_oa_attendance_check_type(OnDuty：上班OffDuty：下班 */
    @Excel(name = "考勤类型：详见字典表sys_oa_attendance_check_type(OnDuty：上班OffDuty：下班")
    private String checkType;

    /** 实际打卡时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际打卡时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date userCheckTime;

    /** 工作日 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Excel(name = "工作日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date workDate;

    /** 打卡记录ID */
    @Excel(name = "打卡记录ID")
    private Long recordId;

    /** 排班ID */
    @Excel(name = "排班ID")
    private Long planId;

    /** 考勤组id */
    @Excel(name = "考勤组id")
    private Long groupId;

    /** 考勤组 */
    @Excel(name = "考勤组id")
    private Long attendanceId;

    /** 计算迟到和早退，基准时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "计算迟到和早退，基准时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date baseCheckTime;

    /** 删除标识 */
    @Excel(name = "删除标识")
    private Long isDelete;



}
