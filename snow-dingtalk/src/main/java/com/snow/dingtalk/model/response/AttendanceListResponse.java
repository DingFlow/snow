package com.snow.dingtalk.model.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author qimingjin
 * @Title: 考勤记录返回实体
 * @Description:
 * @date 2022/1/25 15:09
 */
@Data
public class AttendanceListResponse implements Serializable {
    private static final long serialVersionUID = 176270753159525848L;


    /**
     * 返回数据
     */
    private List<Attendance> attendanceList;

    /**
     * 分页是否还有更多数据
     */
    private boolean hasMore;

    @Data
    public static class Attendance implements Serializable {
        private static final long serialVersionUID = 7933532818827152345L;
        /**
         *
         * 唯一标识ID
         */
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
         * 打卡记录ID
         */
        private Long recordId;

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
    }

}
