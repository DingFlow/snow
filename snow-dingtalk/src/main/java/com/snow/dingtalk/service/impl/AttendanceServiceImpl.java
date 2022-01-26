package com.snow.dingtalk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceListRecordRequest;
import com.dingtalk.api.request.OapiAttendanceListRequest;
import com.dingtalk.api.request.OapiAttendanceRecordUploadRequest;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import com.dingtalk.api.response.OapiAttendanceRecordUploadResponse;
import com.snow.common.annotation.DingTalkLog;
import com.snow.common.enums.DingTalkLogType;
import com.snow.common.exception.SyncDataException;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.request.AttendanceListRecordRequest;
import com.snow.dingtalk.model.request.AttendanceListRequest;
import com.snow.dingtalk.model.request.AttendanceRecordUploadRequest;
import com.snow.dingtalk.model.response.AttendanceListRecordResponse;
import com.snow.dingtalk.model.response.AttendanceListResponse;
import com.snow.dingtalk.service.AttendanceService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qimingjin
 * @Title: 考勤服务类
 * @Description:
 * @date 2022/1/25 14:15
 */
@Slf4j
@Service
public class AttendanceServiceImpl extends BaseService implements AttendanceService {

    /**
     * 获取考勤记录
     * @param attendanceListRequest 考勤记录请求参数
     * @return 考勤记录
     */
    @Override
    @DingTalkLog(dingTalkLogType=DingTalkLogType.ATTENDANCE_RECORD_LIST,dingTalkUrl =BaseConstantUrl.ATTENDANCE_RECORD_LIST)
    public AttendanceListResponse getAttendanceList(AttendanceListRequest attendanceListRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.ATTENDANCE_RECORD_LIST);
        OapiAttendanceListRequest req = BeanUtil.copyProperties(attendanceListRequest, OapiAttendanceListRequest.class);
        req.setIsI18n(false);
        OapiAttendanceListResponse rsp = null;
        try {
            rsp = client.execute(req, getDingTalkToken());
            if(rsp.getErrcode()!=0){
                log.error("@@获取考勤记录信息返回异常：{}",rsp.getErrmsg());
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
            AttendanceListResponse attendanceListResponse=new AttendanceListResponse();
            attendanceListResponse.setAttendanceList(Convert.toList(AttendanceListResponse.Attendance.class,rsp.getRecordresult()));
            attendanceListResponse.setHasMore(rsp.getHasMore());
            return attendanceListResponse;
        } catch (ApiException e) {
            log.error("@@获取考勤记录信息异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType=DingTalkLogType.ATTENDANCE_RECORD_RECORD,dingTalkUrl =BaseConstantUrl.ATTENDANCE_RECORD_RECORD)
    public List<AttendanceListRecordResponse> getAttendanceRecord(AttendanceListRecordRequest attendanceListRecordRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.ATTENDANCE_RECORD_RECORD);
        OapiAttendanceListRecordRequest req = BeanUtil.copyProperties(attendanceListRecordRequest, OapiAttendanceListRecordRequest.class);
        req.setIsI18n(false);
        OapiAttendanceListRecordResponse  rsp =null;
        try {
            rsp = client.execute(req, getDingTalkToken());
            if(rsp.getErrcode()!=0){
                log.error("@@获取考勤记录信息详情返回异常：{}",rsp.getErrmsg());
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
            return Convert.toList(AttendanceListRecordResponse.class,rsp.getRecordresult());
        } catch (ApiException e) {
            log.error("@@获取考勤记录信息详情异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType=DingTalkLogType.UPLOAD_ATTENDANCE_RECORD,dingTalkUrl =BaseConstantUrl.ATTENDANCE_RECORD_UPLOAD)
    public String uploadAttendanceRecord(AttendanceRecordUploadRequest attendanceRecordUploadRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.ATTENDANCE_RECORD_UPLOAD);
        OapiAttendanceRecordUploadRequest req = BeanUtil.copyProperties(attendanceRecordUploadRequest, OapiAttendanceRecordUploadRequest.class);
        req.setUserid(attendanceRecordUploadRequest.getDingUserId());
        OapiAttendanceRecordUploadResponse rsp = null;
        try {
            rsp = client.execute(req, getDingTalkToken());
            if(rsp.getErrcode()!=0){
                log.error("@@上传考勤记录信息返回异常：{}",rsp.getErrmsg());
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
            return rsp.getRequestId();
        } catch (ApiException e) {
            log.error("@@上传考勤记录信息异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }
}
