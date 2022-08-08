package com.snow.dingtalk.model.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @date 2022-08-04 13:37
 * @Description:
 */
@Data
public class DingUserResponse implements Serializable {

    private static final long serialVersionUID = 8884016930071611080L;

    private String userid;

    private String unionid;


    private Boolean active;

    private Boolean admin;

    private String avatar;

    private Boolean boss;

    private List<Long> deptIdList;

    private List<DeptOrder> deptOrderList;

    private String email;

    private Boolean exclusiveAccount;

    private String extension;

    private Boolean hideMobile;

    private Long hiredDate;

    private String jobNumber;

    private List<DeptLeader> leaderInDept;

    private String mobile;

    private String name;

    private String orgEmail;

    private Boolean realAuthed;

    private String remark;

    private List<UserRole> roleList;

    private Boolean senior;

    private String stateCode;

    private String telephone;

    private String title;

    private String workPlace;


    @Data
    private static class DeptOrder implements Serializable{
        private static final long serialVersionUID = -3593170558925107802L;

        /**
         * 部门id
         */
        private Long deptId;

        /**
         * 员工在部门中的排序
         */
        private Integer order;
    }

    @Data
    private static class DeptLeader implements Serializable{
        private static final long serialVersionUID = -3593170558925107802L;

        /**
         * 部门id
         */
        private Long dept_id;


        private Boolean leader;
    }

    @Data
    public static class UserRole implements Serializable {
        private static final long serialVersionUID = 8436536014124472980L;

        private Long id;

        private String groupName;

        private String name;
    }
}
