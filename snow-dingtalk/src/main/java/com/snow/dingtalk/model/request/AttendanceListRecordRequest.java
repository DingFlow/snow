package com.snow.dingtalk.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
  * @Title: 考勤记录详情
  * @Description:
  * @author qimingjin
  * @date 2022/1/25 15:41  
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceListRecordRequest implements Serializable {
    private static final long serialVersionUID = -6492475769072358528L;

    /**
     * 员工在企业内的userid列表，最多不能超过50个。
     */
    private List<String> userIds;

    /**
     * 查询考勤打卡记录的起始工作日
     */
    private String checkDateFrom;
    /**
     * 查询考勤打卡记录的结束工作日
     */
    private String checkDateTo;

}
