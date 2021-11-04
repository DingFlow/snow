package com.snow.dingtalk.model.request;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @Title: 部门联系人
 * @Description:
 * @date 2021/11/4 10:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExtContactUserRequest implements Serializable {
    private static final long serialVersionUID = -8403556837654203135L;

    /**
     * 用户id
     */
    private String userid;
    /**
     * 地址
     */
    private String address;

    /**
     * 外部联系人的企业名称
     */
    private String companyName;

    /**
     * 负责人的钉钉userId
     */
    private String followerUserId;

    /**
     * 标签列表
     */
    private List<Long> labelIds;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 姓名
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 共享给的部门ID
     */
    private List<Long> shareDeptIds;

    /**
     * 共享给的员工userid列表
     */
    private List<String> shareUserIds;


    /**
     * 职位
     */
    private String title;

    /**
     * 邮箱
     */
    private String email;
}
