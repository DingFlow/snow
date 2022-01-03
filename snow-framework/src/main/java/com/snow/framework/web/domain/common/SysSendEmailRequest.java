package com.snow.framework.web.domain.common;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-12 19:25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SysSendEmailRequest implements Serializable {
    private static final long serialVersionUID = -3505555356014745742L;

    private String templateByCode;
    /**
     * 发送人
     */
    private String from;

    /**
     * 主题
     */
    private String subject;
    /**
     * 内容
     */
    private String content;

    /**
     * 接收人set集合
     */
    private Set receiverSet;
    /**
     * 抄送人
     */
    private Set CCSet;

    /**
     * 文件path
     */
    private String filePath;

    /**
     * 发送时间
     */
    private Date sentDate;


    /**
     * 参数List
     */
    private Map<String,String> paramMap;

}
