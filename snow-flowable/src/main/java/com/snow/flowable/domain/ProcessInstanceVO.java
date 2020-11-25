package com.snow.flowable.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    public String processDefinitionId;
    /**
     * 流程定义名称
     */
    public String processDefinitionName;
    /**
     * 流程定义key
     */
    public String processDefinitionKey;

    /**
     * 流程版本号
     */
    public Integer processDefinitionVersion;
    /**
     * 发布ID
     */
    public String deploymentId;
    /**
     * 开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date endTime;

    public String endActivityId;

    public String startUserId;

    public String startActivityId;
    /**
     * 删除理由
     */
    public String deleteReason;
    /**
     * 父流程ID
     */
    public String superProcessInstanceId;

    public String tenantId;


    public String description;

    public String callbackId;

    public String callbackType;


    /**
     * 业务参数
     */
    public String businessKey;
    /**
     * 是否结束(0--进行中，1--结束)
     */
    public Integer isFinished;



    public String processDefinitionCategory;

    public static List<ProcessInstanceVO> warpList(List<HistoricProcessInstance> historicProcessInstanceList){
       return historicProcessInstanceList.stream().map(t->{
            ProcessInstanceVO processInstanceVO=new ProcessInstanceVO();
            BeanUtils.copyProperties(t,processInstanceVO);
            if(StringUtils.isEmpty(t.getEndActivityId())){
                processInstanceVO.setIsFinished(0);
            }else {
                processInstanceVO.setIsFinished(1);
            }
           return processInstanceVO;
        }).collect(Collectors.toList());
    }

}
