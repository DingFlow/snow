package com.snow.dingtalk.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-29 17:28
 **/
@Data
public class OcrRecognizeRequest implements Serializable {

    /**
     * 图片URL
     */
    private String imageUrl;
    /**
     * 类型
     */
    private String type;
}
