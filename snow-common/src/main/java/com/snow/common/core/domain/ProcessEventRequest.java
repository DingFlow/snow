package com.snow.common.core.domain;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title: 流程监听
 * @Description:
 * @date 2021/12/6 15:42
 */
@Data
public class ProcessEventRequest extends ApplicationEvent implements Serializable {
    private static final long serialVersionUID = 4565185183548903691L;

    /**
     * 业务参数
     */
    private String businessKey;

    /** 流程状态 */
    private String processStatus;


    public ProcessEventRequest(Object source) {
        super(source);
    }
}
