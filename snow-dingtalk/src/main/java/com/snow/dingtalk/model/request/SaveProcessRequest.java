package com.snow.dingtalk.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @Title: 审批模板信息实体
 * @Description:
 * @date 2021/12/31 13:51
 */
@Data
public class SaveProcessRequest implements Serializable {
    private static final long serialVersionUID = -5950588668001683721L;

    /**
     * 审批流的唯一码。process_code在审批流编辑页面的URL中获取。
     * 如果该参数没有赋值，表示新建一个模板。
     * 如果赋值，表示更新所传值对应的审批模板。
     */
    private String processCode;

    /**
     * 审批模板名称
     */
    private String name;

    /**
     * 审批模板描述
     */
    private String description;

    /**
     * 表单属性
     */
    private List<SaveProcessRequest.FormComponentVo> formComponentList;

    @Data
    public static class FormComponentVo implements Serializable {

        private static final long serialVersionUID = -4081272321655736210L;

        /**
         * 表单名称。每种表单组件的component_name是固定的。表单组件的props里的id，
         * 必须在模板里唯一，可以有两段字符串组成，第一段为表单的component_name；
         * 第二段为8位随机字符串。
         */
        private String componentName;

        /**
         * 表单属性
         */
        private SaveProcessRequest.FormComponentPropVo props;
    }

    @Data
    public static class FormComponentPropVo implements Serializable{

        private static final long serialVersionUID = -6941423974749631049L;

        /**
         * 表单Id
         */
        private String id;

        /**
         * 表单名称
         */
        private String label;

        /**
         * 是否必填：
         * true：是
         * false：否
         */
        private Boolean required;

        private String placeholder;

    }
}
