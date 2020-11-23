package com.snow.flowable.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * ASSIGNEE_（受理人）：task任务的受理人，就是执行TASK的人，这个又分两种情况（有值，NULL）
 *     1），有值的情况：XML流程里面定义的受理人，TASK会直接填入这个人；
 *     2），NULL：XML没有指定受理人或者只指定了候选组；
 * 没有值的时候，可以使用签收功能去指定受理人，就是候选组里面谁签收谁就成了受理人：
 *OWNER_（委托人）：受理人委托其他人操作该TASK的时候，受理人就成了委托人OWNER_，其他人就成了受理人ASSIGNEE_
 * @date 2020/11/23 17:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskVO implements Serializable {

    private String taskId;

    private String taskName;

    private String processDefinitionName;

    private String description;

    private String owner;

    private String parentTaskId;

    private String tenantId;

    private String Assignee;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    private String category;

    private String formKey;

    private String processInstanceId;

    private String startUserId;

    private String startUserName;

    private String businessKey;
    /**
     * 任务发起时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
}
