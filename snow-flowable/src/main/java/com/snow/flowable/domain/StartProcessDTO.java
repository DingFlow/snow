package com.snow.flowable.domain;

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

   private String startUserId;

   private String processDefinitionKey;

   private String businessKey;

   private Map<String, Object> variables;
}
