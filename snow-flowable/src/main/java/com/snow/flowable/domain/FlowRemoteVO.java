package com.snow.flowable.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-21 11:21
 **/
@Data
public class FlowRemoteVO<T> implements Serializable {

   private static final long serialVersionUID = 9031360117101499782L;

   private Integer size;

   private long total;

   private Integer start;

   private List<T> data;
}
