package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/23 19:39
 */
@Data
public class CarouselField extends BaseField implements Serializable {
    private static final long serialVersionUID = -3039577252797632165L;

    private String width;
    private String height;
    /**
     * 是否全屏
     */
    private boolean full;
    /**
     * 轮播切换动画方式
     */
    private String  anim;
    /**
     * 切换时间 毫秒
     */
    private long interval;

    /**
     * 初始索引
     */
    private int startIndex;
    /**
     * 切换箭头默认显示状态
     */
    private String arrow;
    /**
     * 是否自动切换
     */
    private boolean autoplay;
    /**
     * 帮助文档
     */
    private String document;

    private String datasourceType;

    private String remoteUrl;

    private String remoteMethod;

    private String remoteOptionText;

    private String remoteOptionValue;

    private List<Options> options;
}
