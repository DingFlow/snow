package com.snow.flowable.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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
   private static final long serialVersionUID = 4408144744139924888L;

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
    * 发布key
    */
   private String key;
   /**
    * 分类
    */
   private String category;

   /**
    * 租户Id
    */
   private String tenantId;
   /**
    * 父节点
    */
   private String parentDeploymentId;
   /**
    * 版本
    */
   private int engineVersion;

   /**
    * 是否是最新版本
    */
   private Boolean isNew;

   /**
    * xml
    */
   private String resourceName;

   /**
    * 图片
    */
   private String dgrmResourceName;


   /**
    * 流程定义
    */
   private List<ProcessDefVO> processDefVOList;
}
