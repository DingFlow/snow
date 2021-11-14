package com.snow.dingtalk.model.request;

import lombok.Data;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/21 15:19
 */
@Data
public class WorkrecordGetbyuseridRequest {
    private Long limit;
    private Long offset;
    private Long status;
    private String userid;
}
