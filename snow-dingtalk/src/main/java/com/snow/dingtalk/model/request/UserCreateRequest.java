package com.snow.dingtalk.model.request;

import lombok.NonNull;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/22 14:59
 */
public class UserCreateRequest {
    /**
     * 数组类型，数组里面值为整型，成员所属部门id列表
     */
    @NonNull
    private String department;
    /**
     * 邮箱。长度为0~64个字符。企业内必须唯一，不可重复
     */
    private String email;
    /**
     *
     * 扩展属性，可以设置多种属性（手机上最多显示10个扩展属性，具体显示哪些属性，请到OA管理后台->设置->通讯录信息设置和OA管理后台->设置->手机端显示信息设置）。
     *
     * 该字段的值支持链接类型填写，同时链接支持变量通配符自动替换，目前支持通配符有：userid，corpid。示例： [工位地址](http://www.dingtalk.com?userid=#userid#&corpid=#corpid#)
     */
    private String extattr;
    /**
     * 入职时间，Unix时间戳，单位ms
     */
    private Long hiredDate;
    /**
     * 是否号码隐藏。true表示隐藏，false表示不隐藏。
     *
     * 隐藏手机号后，手机号在个人资料页隐藏，但仍可对其发DING、发起钉钉免费商务电话。
     */
    private Boolean isHide;
    /**
     *
     * 是否高管模式。true表示是，false表示不是。
     *
     * 开启后，手机号码对所有员工隐藏。普通员工无法对其发DING、发起钉钉免费商务电话。
     * 高管之间不受影响。
     */
    private Boolean isSenior;
    /**
     * 员工工号。对应显示到OA后台和客户端个人资料的工号栏目。
     *
     * 长度为0~64个字符
     */
    private String jobnumber;
    /**
     * 手机号码，企业内必须唯一，不可重复。如果是国际号码，请使用+xx-xxxxxx的格式
     */
    @NonNull
    private String mobile;
    /**
     * 成员名称。
     *
     * 长度为1~64个字符
     */
    @NonNull
    private String name;
    /**
     *
     * 在对应的部门中的排序，
     *
     * Map结构的json字符串，key是部门的id, value是人员在这个部门的排序值
     */
    private String orderInDepts;
    /**
     *
     * 员工的企业邮箱，员工的企业邮箱已开通，才能增加此字段， 否则会报错
     */
    private String orgEmail;
    /**
     * 职位信息。
     *
     * 长度为0~64个字符
     */
    private String position;
    /**
     *
     * 设置用户在每个部门下的职位。
     *
     * Map结构的json字符串，
     *
     * Map的Key是deptId，表示部门id，
     *
     * Map的Value是职位，表示在这个部门下的职位
     */
    private String positionInDepts;
    /**
     * 备注，长度为0~1000个字符
     */
    private String remark;
    /**
     * 分机号，长度为0~50个字符，企业内必须唯一，不可重复
     */
    private String tel;
    /**
     * 员工在当前企业内的唯一标识，也称staffId。可由企业在创建时指定，并代表一定含义比如工号，创建后不可修改，企业内必须唯一。
     *
     * 长度为1~64个字符，如果不传，服务器将自动生成一个userid。
     */
    private String userid;
    /**
     *办公地点，长度为0~50个字符
     */
    private String workPlace;


    public UserCreateRequest() {
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setExtattr(String extattr) {
        this.extattr = extattr;
    }

    public void setExtattrString(String extattr) {
        this.extattr = extattr;
    }

    public String getExtattr() {
        return this.extattr;
    }

    public void setHiredDate(Long hiredDate) {
        this.hiredDate = hiredDate;
    }

    public Long getHiredDate() {
        return this.hiredDate;
    }

    public void setIsHide(Boolean isHide) {
        this.isHide = isHide;
    }

    public Boolean getIsHide() {
        return this.isHide;
    }

    public void setIsSenior(Boolean isSenior) {
        this.isSenior = isSenior;
    }

    public Boolean getIsSenior() {
        return this.isSenior;
    }

    public void setJobnumber(String jobnumber) {
        this.jobnumber = jobnumber;
    }

    public String getJobnumber() {
        return this.jobnumber;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setOrderInDepts(String orderInDepts) {
        this.orderInDepts = orderInDepts;
    }

    public void setOrderInDeptsString(String orderInDepts) {
        this.orderInDepts = orderInDepts;
    }

    public String getOrderInDepts() {
        return this.orderInDepts;
    }

    public void setOrgEmail(String orgEmail) {
        this.orgEmail = orgEmail;
    }

    public String getOrgEmail() {
        return this.orgEmail;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPositionInDepts(String positionInDepts) {
        this.positionInDepts = positionInDepts;
    }

    public void setPositionInDeptsString(String positionInDepts) {
        this.positionInDepts = positionInDepts;
    }

    public String getPositionInDepts() {
        return this.positionInDepts;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return this.tel;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getWorkPlace() {
        return this.workPlace;
    }
}
