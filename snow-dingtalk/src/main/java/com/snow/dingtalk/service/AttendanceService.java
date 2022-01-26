package com.snow.dingtalk.service;

import com.snow.dingtalk.model.request.AttendanceListRecordRequest;
import com.snow.dingtalk.model.request.AttendanceListRequest;
import com.snow.dingtalk.model.request.AttendanceRecordUploadRequest;
import com.snow.dingtalk.model.response.AttendanceListRecordResponse;
import com.snow.dingtalk.model.response.AttendanceListResponse;

import java.util.List;

/**
 * @author qimingjin
 * @Title: 考勤相关
 * @Description:
 * @date 2022/1/25 13:44
 */
public interface AttendanceService {

   /**
    * 获取考勤记录
    * @param attendanceListRequest 考勤记录请求参数
    * @return 考勤记录
    */
   AttendanceListResponse getAttendanceList(AttendanceListRequest attendanceListRequest);

   /**
    * 获取打卡记录详情
    * 比如，企业给一个员工设定的排班是上午9点和下午6点各打一次卡，但是员工在这期间打了多次，该接口会把所有的打卡记录都返回
    * @param attendanceListRecordRequest 考勤记录详情请求参数
    * @return 考勤记录详情
    */
   List<AttendanceListRecordResponse>  getAttendanceRecord(AttendanceListRecordRequest attendanceListRecordRequest);

   /**
    * 上传考勤记录
    * @param attendanceRecordUploadRequest 上传考勤记录参数
    * @return 请求id
    */
   String uploadAttendanceRecord(AttendanceRecordUploadRequest attendanceRecordUploadRequest);
}
