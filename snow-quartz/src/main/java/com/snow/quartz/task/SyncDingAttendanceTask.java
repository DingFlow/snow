package com.snow.quartz.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.common.constant.SequenceConstants;
import com.snow.common.utils.DateUtils;
import com.snow.dingtalk.model.request.AttendanceListRequest;
import com.snow.dingtalk.model.response.AttendanceListResponse;
import com.snow.dingtalk.service.AttendanceService;
import com.snow.system.domain.SysOaAttendance;
import com.snow.system.service.ISysOaAttendanceService;
import com.snow.system.service.ISysSequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @Title: 同步钉钉考勤信息任务
 * @Description:
 * @date 2022/1/26 13:35
 */
@RequiredArgsConstructor
@Component("syncDingAttendanceTask")
@Slf4j
public class SyncDingAttendanceTask {

    private final AttendanceService attendanceService;

    private final ISysOaAttendanceService sysOaAttendanceService;
    
    private final ISysSequenceService sequenceService;

    /**
     * 定时任务同步数据，每十分钟同步一次
     * @param dataFrom 开始时间
     * @param dataTo 结束时间
     */
    public void syncDingAttendanceData(String dataFrom,String dataTo){
        AttendanceListRequest attendanceListRequest = AttendanceListRequest.builder().build();
        if(StrUtil.isNotBlank(dataFrom)){
            attendanceListRequest.setWorkDateFrom(dataFrom);
        }else {
            attendanceListRequest.setWorkDateFrom(DateUtils.getDate()+" 00:00:00");
        }
        if(StrUtil.isNotBlank(dataTo)){
            attendanceListRequest.setWorkDateTo(dataTo);
        }else {
            attendanceListRequest.setWorkDateTo(DateUtils.getDate()+" 59:59:59");
        }
        long offset=0L;
        long limit=10L;
        attendanceListRequest.setOffset(offset);
        attendanceListRequest.setLimit(limit);
        attendanceListRequest.setUserIdList(CollUtil.newArrayList("manager4480"));
        AttendanceListResponse attendanceListResponse = attendanceService.getAttendanceList(attendanceListRequest);
        saveSysOaAttendance(attendanceListResponse);
        boolean isHasMore=attendanceListResponse.isHasMore();
        while (isHasMore){
            offset= offset+limit;
            attendanceListRequest.setOffset(offset);
            AttendanceListResponse attendanceResponse = attendanceService.getAttendanceList(attendanceListRequest);
            saveSysOaAttendance(attendanceResponse);
            isHasMore=attendanceListResponse.isHasMore();
        }

    }

    /**
     * 保存数据到本地数据库
     * @param attendanceListResponse 钉钉返回参数
     */
    private void saveSysOaAttendance(AttendanceListResponse attendanceListResponse){
        List<AttendanceListResponse.Attendance> attendanceList = attendanceListResponse.getAttendanceList();
        if(CollUtil.isNotEmpty(attendanceList)){
            List<SysOaAttendance> sysOaAttendanceList = attendanceList.stream().map(attendance -> {
                String newSequenceNo = sequenceService.getNewSequenceNo(SequenceConstants.OA_ATTENDANCE_SEQUENCE);
                SysOaAttendance sysOaAttendance = BeanUtil.copyProperties(attendance, SysOaAttendance.class);
                sysOaAttendance.setAttendanceCode(newSequenceNo);
                sysOaAttendance.setAttendanceId(attendance.getId());
                //判断是否已同步过
                LambdaQueryWrapper<SysOaAttendance> sysOaAttendanceLambdaQueryWrapper = new QueryWrapper<SysOaAttendance>().lambda().eq(SysOaAttendance::getAttendanceId,attendance.getId());
                SysOaAttendance oaAttendance= sysOaAttendanceService.getOne(sysOaAttendanceLambdaQueryWrapper);
                if(ObjectUtil.isNotNull(oaAttendance)){
                    return null;
                }
                return sysOaAttendance;
            }).collect(Collectors.toList());
            sysOaAttendanceService.saveBatch(sysOaAttendanceList);
        }
    }

}
