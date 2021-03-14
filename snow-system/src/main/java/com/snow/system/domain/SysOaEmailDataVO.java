package com.snow.system.domain;

import lombok.*;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-13 18:55
 **/
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysOaEmailDataVO implements Serializable {

    /**
     * 收件数
     */
    private Long inboxTotal;


    /**
     * 发件数
     */
    private Long sendMailTotal;


    /**
     * 重要数  针对收的和发的邮件
     */
    private Long importantTotal;

    /**
     * 草稿数  草稿是对发的邮件存为草稿
     */
    private Long draftsTotal;

    /**
     * 垃圾箱数 垃圾是标记收的邮件标
     */
    private Long trashTotal;


    /**
     * 已读数 收的邮件
     */
    private Long readTotal;
}
