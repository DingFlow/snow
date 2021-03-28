package com.snow.dingtalk.model;

import com.dingtalk.api.request.OapiProcessSaveRequest;
import com.snow.common.enums.FormComponentType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-21 19:40
 **/
@Data
public class SaveFlowRequest implements Serializable {

    private static final long serialVersionUID = -7038720879277547731L;



    private String createInstanceMobileUrl;

    private String createInstancePcUrl;

    private String description;

    private String dirId;

    private Boolean disableFormEdit;

    private Boolean disableStopProcessButton;

    private Boolean fakeMode;

    private List<FormComponentVo> formComponentList;


    private Boolean hidden;

    private String icon;

    private String name;

    private String originDirId;

    private String processCode;

   // private OapiProcessSaveRequest.ProcessConfig processConfig;

    private String templateEditUrl;

    private FormComponentType formComponentType;

    @Data
    public static class FormComponentVo implements Serializable {


        private static final long serialVersionUID = -1887336939225262377L;

        private String componentName;

        private FormComponentPropVo props;
    }

    @Data
    public static class FormComponentPropVo implements Serializable{

        private static final long serialVersionUID = 6244866646550769755L;


        private String actionName;

        private String bizAlias;

        private String bizType;

        private Long choice;

        private String content;

        private Boolean duration;

        private String format;

        private String formula;

        private String id;

        private String label;

        private String link;

        private String notPrint;

        private String notUpper;


        private List<String> options;

        private String placeholder;

        private Boolean required;

        private String unit;
    }
}
