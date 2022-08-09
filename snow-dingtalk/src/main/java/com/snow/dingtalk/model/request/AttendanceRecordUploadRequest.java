package com.snow.dingtalk.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title: 上传考勤记录
 * @Description:
 * @date 2022/1/25 14:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceRecordUploadRequest implements Serializable {

    private static final long serialVersionUID = -583239968246465341L;

    /**
     * 员工的钉钉userid
     */
    private String dingUserId;
    /**
     * 考勤机名称，该参数值是自定义的，比如123456
     */
    private String deviceName;

    /**
     * 考勤机ID，该参数值是自定义的，比如abcde
     */
    private String deviceId;

    /**
     * 打卡备注图片地址，必须是公网可访问的地址。
     */
    private String photoUrl;

    /**
     * 员工打卡的时间，单位毫秒。
     */
    private Long userCheckTime;

}
