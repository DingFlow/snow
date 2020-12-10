package com.snow.flowable.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.utils.bean.BeanUtils;
import lombok.Data;
import org.flowable.engine.task.Attachment;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.IdentityLinkInfo;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/26 16:41
 */
@Data
public class HistoricTaskInstanceVO implements Serializable {
    /**
     * 历史任务id
     */
    private String id;

    /**
     * 流程执行ID
     */
    private String executionId;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     *流程名称
     */
    private String processName;

    /**
     *业务参数
     */
    private String businessKey;

    /**
     * 任务定义ID
     */
    private String taskDefinitionId;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 完成时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 用时
     */
    private String spendTime;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 用户ID
     */
    private String assignee;


    /**
     * 用户名称
     */
    private String assigneeName;

    /**
     * 任务描述
     */
    private String description;

    private String category;

    private String isPass;

    /**
     * 评论
     */
    private List<Comment> commentList;

    /**
     *附件
     */
    private  List<Attachment> attachmentList;

    /**
     * 任务变量
     */
    private Map<String, Object> taskLocalVariables;


    private String formKey;

    /**
     * 流程变量
     */
    private Map<String, Object> processVariables;


/*    private String scopeId;
    private String subScopeId;
    private String scopeType;
    private String scopeDefinitionId;
    private String propagatedStageInstanceId;*/



    private String deleteReason;

    private String localizedName;
    private String parentTaskId;

    private String localizedDescription;
    private String owner;


    private String taskDefinitionKey;



    private int priority;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueDate;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date claimTime;


    public static List<HistoricTaskInstanceVO> warpList(List<HistoricTaskInstance> historicTaskInstanceList){
        return historicTaskInstanceList.stream().map(t->{
            HistoricTaskInstanceVO historicTaskInstanceVO=new HistoricTaskInstanceVO();
            BeanUtils.copyProperties(t,historicTaskInstanceVO);
            historicTaskInstanceVO.setTaskLocalVariables(t.getTaskLocalVariables());

            historicTaskInstanceVO.setProcessVariables(t.getProcessVariables());
            return historicTaskInstanceVO;
        }).collect(Collectors.toList());
    }
}
