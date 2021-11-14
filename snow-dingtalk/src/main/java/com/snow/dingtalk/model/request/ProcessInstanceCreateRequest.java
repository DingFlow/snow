package com.snow.dingtalk.model.request;


import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.internal.util.json.JSONWriter;
import lombok.NonNull;

import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/18 15:26
 */
public class ProcessInstanceCreateRequest {
    /**
     *
     * 审批人userid列表，最大列表长度20。
     *
     * 多个审批人用逗号分隔，按传入的顺序依次审批
     */
    private String approvers;
    /**
     * 审批人列表。
     *
     * 支持会签/或签，优先级高于approvers变量
     */
    private String approversV2;
    /**
     *抄送人userid列表，最大列表长度：20。多个抄送人用逗号分隔。
     *
     * 该参数需要与cc_position参数一起传，抄送人才会生效；
     *
     * 该参数需要与approvers或approvers_v2参数一起传，抄送人才会生效；
     */
    private String ccList;
    /**
     * 抄送时间，分为（START, FINISH, START_FINISH）,默认是START
     */
    private String ccPosition;
    /**
     * 发起人所在的部门。
     *
     * 如果发起人属于根部门，传-1
     */
    @NonNull
    private Long deptId;

    private String formComponentValues;
    /**
     * 审批实例发起人的userid
     */
    private String originatorUserId;
    /**
     * 审批流的唯一码，process_code就在审批流编辑的页面URL中
     */
    private String processCode;


    public ProcessInstanceCreateRequest() {
    }

    public void setApprovers(String approvers) {
        this.approvers = approvers;
    }

    public String getApprovers() {
        return this.approvers;
    }

    public void setApproversV2(String approversV2) {
        this.approversV2 = approversV2;
    }

    public void setApproversV2(List<ProcessInstanceCreateRequest.ProcessInstanceApproverVo> approversV2) {
        this.approversV2 = (new JSONWriter(false, false, true)).write(approversV2);
    }

    public String getApproversV2() {
        return this.approversV2;
    }

    public void setCcList(String ccList) {
        this.ccList = ccList;
    }

    public String getCcList() {
        return this.ccList;
    }

    public void setCcPosition(String ccPosition) {
        this.ccPosition = ccPosition;
    }

    public String getCcPosition() {
        return this.ccPosition;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public void setFormComponentValues(String formComponentValues) {
        this.formComponentValues = formComponentValues;
    }

    public void setFormComponentValues(List<ProcessInstanceCreateRequest.FormComponentValueVo> formComponentValues) {
        this.formComponentValues = (new JSONWriter(false, false, true)).write(formComponentValues);
    }

    public String getFormComponentValues() {
        return this.formComponentValues;
    }

    public void setOriginatorUserId(String originatorUserId) {
        this.originatorUserId = originatorUserId;
    }

    public String getOriginatorUserId() {
        return this.originatorUserId;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getProcessCode() {
        return this.processCode;
    }





    public static class ProcessInstanceApproverVo  {
        private static final long serialVersionUID = 7264365528946378186L;
        @ApiField("task_action_type")
        private String taskActionType;
        @ApiListField("user_ids")
        @ApiField("string")
        private List<String> userIds;

        public ProcessInstanceApproverVo() {
        }

        public String getTaskActionType() {
            return this.taskActionType;
        }

        public void setTaskActionType(String taskActionType) {
            this.taskActionType = taskActionType;
        }

        public List<String> getUserIds() {
            return this.userIds;
        }

        public void setUserIds(List<String> userIds) {
            this.userIds = userIds;
        }
    }

    public static class FormComponentValueVo  {
        private static final long serialVersionUID = 4315945541956941229L;
        @ApiField("ext_value")
        private String extValue;
        @ApiField("name")
        private String name;
        @ApiField("value")
        private String value;

        public FormComponentValueVo() {
        }

        public String getExtValue() {
            return this.extValue;
        }

        public void setExtValue(String extValue) {
            this.extValue = extValue;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
