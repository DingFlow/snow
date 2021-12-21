package com.snow.dingtalk.model.request;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.util.json.JSONWriter;
import lombok.NonNull;

import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/21 14:37
 */
public class WorkrecordAddRequest {
    /**
     * 外部业务id，建议带上业务方来源字段，防止与其他业务方冲突
     */
    private String bizId;
    /**
     * 待办时间。Unix时间戳，毫秒级
     */
    private Long createTime;
    /**
     * 待办事项表单
     */
    private String formItemList;

    /**
     * pc端跳转url，不传则使用url参数
     */
    private String pcUrl;
    /**
     * 待办的pc打开方式。2表示在pc端打开，4表示在浏览器打开
     */
    private Long pcOpenType;
    /**
     * 	待办来源名称
     */
    private String sourceName;
    /**
     * 待办事项的标题，最多50个字符
     */
    @NonNull
    private String title;
    /**
     * 待办事项的跳转链接。当链接是某个微应用链接时，希望在PC端工作台打开，可通过该方式实现。
     */
    @NonNull
    private String url;
    /**
     * 待办事项对应的用户id
     */
    @NonNull
    private String userid;


    public WorkrecordAddRequest() {
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizId() {
        return this.bizId;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setFormItemList(String formItemList) {
        this.formItemList = formItemList;
    }

    public void setFormItemList(List<WorkrecordAddRequest.FormItemVo> formItemList) {
        this.formItemList = (new JSONWriter(false, false, true)).write(formItemList);
    }

    public String getFormItemList() {
        return this.formItemList;
    }



    public void setPcUrl(String pcUrl) {
        this.pcUrl = pcUrl;
    }

    public String getPcUrl() {
        return this.pcUrl;
    }

    public void setPcOpenType(Long pcOpenType) {
        this.pcOpenType = pcOpenType;
    }

    public Long getPcOpenType() {
        return this.pcOpenType;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return this.userid;
    }

    /**
     * 待办事项表单
     */
    public static class FormItemVo {
        private static final long serialVersionUID = 1767476148969288532L;
        /**
         * 表单标题
         */
        @ApiField("content")
        private String content;
        /**
         * 表单内容
         */
        @ApiField("title")
        private String title;

        public FormItemVo() {
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
