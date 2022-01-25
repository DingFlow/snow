package com.snow.dingtalk.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @Title: 考勤记录
 * @Description:
 * @date 2022/1/25 15:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceListRequest implements Serializable {
    private static final long serialVersionUID = 1746815839189321601L;
    /**
     * 员工在企业内的userid列表，最多不能超过50个。
     */
    private List<String> userIdList;

    /**
     * 查询考勤打卡记录的起始工作日
     */
    private String workDateFrom;
    /**
     * 查询考勤打卡记录的结束工作日
     */
    private String workDateTo;
    /**
     * 表示获取考勤数据的条数，最大不能超过50条。
     */
    private Long limit;
    /**
     *
     * 表示获取考勤数据的起始点。第一次传0，如果还有多余数据，下次获取传的offset值为之前的offset+limit，0、1、2...依次递增。
     */
    private Long offset;

}
