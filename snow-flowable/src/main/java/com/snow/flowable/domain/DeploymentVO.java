package com.snow.flowable.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.flowable.engine.repository.ProcessDefinition;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-21 20:09
 **/
@Data
public class DeploymentVO implements Serializable {
   private String id;

   private String name;

   @JSONField(format = "YYYY-MM-DD hh:mm:ss")
   private Date deploymentTime;

   private String category;

   private String key;

   private String derivedFrom;

   private String derivedFromRoot;

   private String tenantId;

   private String engineVersion;

   private ProcessDefinitionVO processDefinitionVO;

   private List<ProcessDefinitionVO> processDefinitionVOList;

   private List<ModelVO> modelVOList;

}
