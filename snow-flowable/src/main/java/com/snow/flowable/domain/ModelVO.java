package com.snow.flowable.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
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

   private static final long serialVersionUID = 297278952315623267L;

   private String id;

   private String name;

   private String key;

   private  String category;

   @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
   private Date createTime;

   @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
   private Date lastUpdateTime;

   private Integer version;

   private String metaInfo;

   private String deploymentId;

   private String tenantId;


}
