package com.snow.flowable.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-21 21:00
 **/
@Data
public class ModelVO implements Serializable {
   private String id;

   private String name;

   private String key;

   private  String category;

   @JSONField(format = "YYYY-MM-DD hh:mm:ss")
   private Date createTime;

   private Date lastUpdateTime;

   private Integer version;

   private String metaInfo;

   private String deploymentId;

   private String tenantId;


}
