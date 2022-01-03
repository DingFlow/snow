package com.snow.dingtalk.model.request;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Agee
 * @Title: 自有OA发起实例实体
 * @Description:
 * @date 2021/12/31 13:51
 */

@Data
public class StartFakeProcessInstanceRequest implements Serializable {
    private static final long serialVersionUID = 4626303348055459013L;

    /**
     * 审批实例接收人的userid
     */
    private String originatorUserId;

    /**
     * 审批模板唯一码
     */
    private String processCode;

    /**
     * 实例标题
     */
    private String title;

    /**
     * 实例在审批应用里的跳转url，需要同时适配移动端和pc端。
     */
    private String url;

    /**
     * 备注
     */
    private String remark;

    /**
     * 表单参数列表
     */
    private List<FormComponentValueVo> formComponentValueVoList;

    @Data
    public  static class FormComponentValueVo implements Serializable{

        private static final long serialVersionUID = 8413434955965098858L;

        /**
         * 表单名称。表单每一栏的名称，对应表单组件的label字段
         */
        private String name;

        /**
         *
         * 表单值
         */
        private String value;

        /**
         *
         * 表单额外值
         */
        private String extValue;
    }
}
