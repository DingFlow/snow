package com.snow.dingtalk.model.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2022/1/25 16:53
 */
@Data
public class AttendanceListRecordResponse implements Serializable {
    private static final long serialVersionUID = 6076359786521545571L;

    private Long id;

    /**
     * 关联的审批ID，
     * 当该字段非空时，表示打卡记录与请假、加班等审批有关。
     */
    private Long approveId;

    /**
     * 计算迟到和早退，基准时间
     */
    private Date baseCheckTime;

    /**
     *考勤类型：
     * OnDuty：上班
     * OffDuty：下班
     */
    private String checkType;

    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 位置结果：
     *
     * Normal：范围内
     * Outside：范围外
     * NotSigned：未打卡
     */
    private String locationResult;

    /**
     * 排班ID
     */
    private Long planId;

    /**
     * 关联的审批实例ID，
     * 当该字段非空时，表示打卡记录与请假、加班等审批有关。
     */
    private String procInstId;


    /**
     * 数据来源：
     * ATM：考勤机打卡（指纹/人脸打卡）
     * BEACON：IBeacon
     * DING_ATM：钉钉考勤机（考勤机蓝牙打卡）
     * USER：用户打卡
     * BOSS：老板改签
     * APPROVE：审批系统
     * SYSTEM：考勤系统
     * AUTO_CHECK：自动打卡
     */
    private String sourceType;

    /**
     * 打卡结果：
     * Normal：正常
     * Early：早退
     * Late：迟到
     * SeriousLate：严重迟到
     * Absenteeism：旷工迟到
     * NotSigned：未打卡
     */
    private String timeResult;

    /**
     * 实际打卡时间, 用户打卡时间的毫秒数。
     */
    private Date userCheckTime;

    /**
     * 打卡人的UserID
     */
    private String userId;

    /**
     * 工作日
     */
    private Date workDate;

    /**
     *
     * 基准定位精度
     */
    private String baseAccuracy;

    /**
     * 基准地址
     */
    private String baseAddress;

    /**
     *
     * 基准纬度。
     */
    private String baseLatitude;

    /**
     * 基准经度。
     */
    private String baseLongitude;

    /**
     * 基准Mac地址。
     */
    private String baseMacAddr;

    /**
     *
     * 基准wifi ssid。
     */
    private String baseSsid;

    /**
     * 关联的业务ID
     */
    private String bizId;


    /**
     * 班次id
     */
    private Long classId;

    /**
     * 打卡设备ID
     */
    private String deviceId;

    /**
     * 打卡设备序列号
     */
    private String deviceSN;

    /**
     * 打卡记录创建时间
     */
    private Date gmtCreate;

    /**
     *
     * 打卡记录修改时间
     */
    private Date gmtModified;

    /**
     *异常信息类型：
     *对应的invalidRecordType异常信息的具体描述。
     */
    private String invalidRecordMsg;

    /**
     * 异常信息类型：
     * Security：安全相关原因
     * Other：其他原因
     */
    private String invalidRecordType;

    /**
     *是否合法。
     *
     * Y：合法
     * 说明 当timeResult和locationResult都为Normal时，为该值。
     * N：不合法
     */
    private String isLegal;

    /**
     * 位置结果：
     *
     * Normal：范围内
     * Outside：范围外
     * NotSigned：未打卡
     */
    private String locationMethod;

    /**
     *
     * 打卡备注。
     */
    private String outsideRemark;

    /**
     * 排班打卡时间
     */
    private Date planCheckTime;


    /**
     *用户打卡定位精度
     */
    private String userAccuracy;

    /**
     * 用户打卡地址
     *
     * 说明 如果是考勤机打卡 userAddress 返回的是考勤机名称
     */
    private String userAddress;

    /**
     * 用户打卡纬度。
     */
    private String userLatitude;

    /**
     * 用户打卡经度
     */
    private String userLongitude;

    /**
     * 用户打卡wifi Mac地址
     */
    private String userMacAddr;

    /**
     * 用户打卡wifi SSID。
     */
    private String userSsid;

}
