package com.snow.dingtalk.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-23 13:36
 **/
@Data
public class StartFlowRequest implements Serializable {

    private static final long serialVersionUID = 792885986454419385L;

    /**
     * 发起人所在的部门，如果发起人属于根部门，传-1。
     */
    private Long deptId;


    /**
     * 审批实例发起人的userid。
     */
    private String originatorUserId;
    /**
     * 审批流的唯一码。
     *
     * process_code在审批流程编辑页面的URL中获取。
     */
    private String processCode;

    private List<FormComponentValueVo> formComponentValueVoList;

    @Data
    public static class FormComponentValueVo implements Serializable{


        private static final long serialVersionUID = 7297240924226083327L;

        private String extValue;

        private String name;

        private String value;

    }
}
