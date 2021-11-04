package com.snow.dingtalk.model.response;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import com.snow.common.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-26 16:21
 **/
@Data
public class DingTaskResponse implements Serializable {
    private static final long serialVersionUID = -1459137869980975766L;


    private String activityId;

    private Date createTime;

    private Date finishTime;

    private String taskResult;

    private String taskStatus;

    /**
     * 任务id
     */
    private String taskid;

    private String url;

    /**
     * 用户id
     */
    private String userid;


    /**
     * 用户名称
     */
    private String userName;

    /**
     * 任务花费时间
     */
    private String taskSpendTime;


    /**
     * 任务花费时间赋值
     * @return
     */
    public String defaultTaskSpendTime() {

       if(StringUtils.isNotNull(finishTime)){
           setTaskSpendTime(DateUtil.formatBetween(createTime, finishTime, BetweenFormater.Level.SECOND));
       }
        return taskSpendTime;
    }
}
