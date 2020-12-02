package com.snow.flowable.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.flowable.common.engine.api.repository.EngineResource;
import org.flowable.engine.repository.ProcessDefinition;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-21 20:09
 **/
@Data
public class DeploymentVO implements Serializable {
   private String id;
   /**
    * 发布名称
    */
   private String name;
   /**
    * 发布时间
    */
   @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
   private Date deploymentTime;
   /**
    * 分类
    */
   private String category;

   /**
    * 租户Id
    */
   protected String tenantId;
   /**
    * 父节点
    */
   protected String parentDeploymentId;


}
