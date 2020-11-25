package com.snow.flowable.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/25 16:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessInstanceVO  implements Serializable {


    public String processDefinitionId;

    public String processDefinitionName;

    public String processDefinitionKey;


    public Integer processDefinitionVersion;

    public String deploymentId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date startTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date endTime;

    public String endActivityId;

    public String startUserId;

    public String startActivityId;

    public String deleteReason;

    public String superProcessInstanceId;

    public String tenantId;

    public String name;

    public String description;

    public String callbackId;

    public String callbackType;

    private String id;

    public String businessKey;

    public boolean finished;

    public boolean unfinished;

    public boolean deleted;

    public boolean notDeleted;


    public String processDefinitionCategory;




}
