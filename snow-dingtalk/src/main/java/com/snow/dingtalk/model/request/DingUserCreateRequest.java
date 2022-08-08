package com.snow.dingtalk.model.request;

import com.snow.dingtalk.model.client.SnowDingTalkDefaultClient;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/22 14:59
 */
@Data
public class DingUserCreateRequest implements Serializable {
    private static final long serialVersionUID = 5570831884514763247L;

    /**
     * 员工唯一标识ID（不可修改），企业内必须唯一。
     * 长度为1~64个字符，如果不传，将自动生成一个userid。
     */
    private String userid;

    /**
     * 员工名称，长度最大80个字符。
     */
    @NonNull
    private String name;

    /**
     * 手机号码，企业内必须唯一，不可重复。如果是国际号码，请使用+xx-xxxxxx的格式
     */
    @NonNull
    private String mobile;

    /**
     * 是否号码隐藏
     */
    private Boolean hideMobile=false;


    /**
     * 分机号，长度最大50个字符
     */
    private String telephone;

    /**
     * 员工工号，长度最大为50个字符
     */
    private String jobNumber;

    /**
     * 职位，长度最大为200个字符。
     */
    private String title;


    /**
     * 员工个人邮箱，长度最大50个字符。。企业内必须唯一，不可重复
     */
    private String email;

    /**
     *
     * 员工的企业邮箱，员工的企业邮箱已开通，才能增加此字段， 否则会报错
     */
    private String orgEmail;

    /**
     * 员工的企业邮箱类型。
     * profession: 标准版
     * base：基础版
     */
    private String orgEmailType;

    /**
     * 办公地点，长度最大100个字符。
     */
    private String workPlace;

    /**
     * 备注，长度最大2000个字符。
     */
    private String remark;

    /**
     * 员工在对应的部门中的排序
     */
    private List<DeptOrder> dept_order_list;

    /**
     * 员工在对应的部门中的职位。
     */
    private List<DeptTitle> dept_title_list;

    /**
     * 扩展属性，可以设置多种属性，最大长度2000个字符。
     * 例如：{"爱好":"旅游","年龄":"24"}
     */
    private String extension;

    /**
     *
     * 是否高管模式。true表示是，false表示不是。
     *
     * 开启后，手机号码对所有员工隐藏。普通员工无法对其发DING、发起钉钉免费商务电话。
     * 高管之间不受影响。
     */
    private Boolean seniorMode=false;

    /**
     * 入职时间，Unix时间戳，单位ms
     */
    private Long hiredDate;

    /**
     * 直属主管的userId
     */
    private String managerUserid;

    /**
     * 登录邮箱
     */
    private String loginEmail;

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
    private static class DeptTitle implements Serializable{
        private static final long serialVersionUID = -3593170558925107802L;

        /**
         * 部门id
         */
        private Long deptId;

        /**
         * 员工在部门中的职位
         */
        private Integer title;
    }




}
