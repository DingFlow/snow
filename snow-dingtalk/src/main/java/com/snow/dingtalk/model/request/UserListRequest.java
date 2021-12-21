package com.snow.dingtalk.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-27 20:26
 **/
@Data
public class UserListRequest implements Serializable {

    private Long deptId;

    private Long cursor;

    private Long size;
}
