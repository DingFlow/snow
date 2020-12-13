package com.snow.flowable.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.bean.BeanUtils;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.system.service.impl.SysConfigServiceImpl;
import com.snow.system.service.impl.SysUserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.engine.history.HistoricProcessInstance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    private String id;
    /**
     * 流程定义ID
     */
    private String processDefinitionId;
    /**
     * 流程定义名称
     */
    private String processDefinitionName;
    /**
     * 流程定义key
     */
    private String processDefinitionKey;

    /**
     * 流程版本号
     */
    private Integer processDefinitionVersion;
    /**
     * 发布ID
     */
    private String deploymentId;
    /**
     * 开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String endActivityId;
    /**
     * 流程发起人ID
     */
    private String startUserId;

    /**
     * 流程发起人
     */
    private String startUserName;

    private String startActivityId;

    /**
     * 删除理由
     */
    private String deleteReason;
    /**
     * 流程用时
     */
    private String processSpendTime;
    /**
     * 父流程ID
     */
    private String superProcessInstanceId;

    private String tenantId;


    private String description;

    private String callbackId;

    private String callbackType;

    /**
     * 表单详情URL
     */
    private String fromDetailUrl;
    /**
     * 业务参数
     */
    private String businessKey;
    /**
     * 是否结束(0--进行中，1--结束)
     */
    private Integer isFinished;

    /**
     * 业务流程参数
     */
    @JsonIgnoreProperties
    private AppForm appForm;

    private Map<String, Object> processVariables;

    private String processDefinitionCategory;

    public static List<ProcessInstanceVO> warpList(List<HistoricProcessInstance> historicProcessInstanceList){
       return historicProcessInstanceList.stream().map(t->{
            ProcessInstanceVO processInstanceVO=new ProcessInstanceVO();
            BeanUtils.copyProperties(t,processInstanceVO);
            if(StringUtils.isEmpty(t.getEndActivityId())){
                processInstanceVO.setIsFinished(0);
            }else {
                processInstanceVO.setIsFinished(1);
            }
           Map<String, Object> processVariables = t.getProcessVariables();
           processInstanceVO.setProcessVariables(processVariables);
           return processInstanceVO;
        }).collect(Collectors.toList());
    }

}
