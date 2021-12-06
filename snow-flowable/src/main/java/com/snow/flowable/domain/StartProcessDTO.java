package com.snow.flowable.domain;

import com.snow.flowable.common.enums.FlowTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/20 16:16
 */
@Data
public class StartProcessDTO implements Serializable {

   private static final long serialVersionUID = -7725494125601536828L;

   /**
    * 流程发起人id
    */
   private String startUserId;

   /**
    * 流程定于key
    */
   private String processDefinitionKey;


   /**
    * 业务参数
    */
   private String businessKey;

   /**
    * 自定义参数
    */
   private Map<String, Object> variables;
}
