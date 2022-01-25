package com.snow.dingtalk.model.request;

import com.snow.common.enums.FormFieldTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/17 17:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentCreateRequest {
    /**
     *    部门名称，长度限制为1~64个字符，不允许包含字符‘-’‘，’以及‘,’
     */
    private String name;


    /**
     * 父部门id，根部门id为1
     */
    private String  parentid;


    /**
     * 在父部门中的排序值，order值小的排序靠前
     */
    private String  order;


    /**
     *    是否创建一个关联此部门的企业群，默认为false
     */
    private Boolean  createDeptGroup;


    /**
     *
     是否隐藏部门，

     true表示隐藏

     false表示显示
     */
    private Boolean  deptHiding;


    /**
     *  可以查看指定隐藏部门的其他部门列表，如果部门隐藏，则此值生效，
     *  取值为其他的部门id组成的字符串，使用“\|”符号进行分割。总数不能超过200
     */

    private String deptPermits;


    /**
     *     可以查看指定隐藏部门的其他人员列表，如果部门隐藏，则此值生效，
     *     取值为其他的人员userid组成的字符串，使用“\|”符号进行分割。总数不能超过200
     */
    private String   userPermits;


    /**
     *     限制本部门成员查看通讯录，限制开启后，本部门成员只能看到限定范围内的通讯录。true表示限制开启
     */
    private Boolean     outerDept;


    /**
     *  部门标识字段，开发者可用该字段来唯一标识一个部门，并与钉钉外部通讯录里的部门做映射
     */
    private String sourceIdentifier;


    /**
     * 部门自定义字段，格式为文本类型的Json格式
     */
    private String  ext;

    /**
     * @program: snow
     * @description
     * @author: 没用的阿吉
     * @create: 2021-03-21 19:40
     **/
    @Data
    public static class SaveFlowRequest implements Serializable {

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

        private FormFieldTypeEnum formComponentType;

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
}
