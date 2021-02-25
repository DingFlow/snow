package com.snow.system.domain;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author qimingjin
 * @Title:  钉钉同步数据概况返回实体
 * @Description:
 * @date 2021/2/25 19:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SysDingtalkSyncSituationVO implements Serializable {

    /**
     * 成功数
     */
    private long successNums;

    /**
     * 失败数
     */
    private long failureNums;

    /**
     * 成功比率
     */
    private BigDecimal successRatio;

    /**
     * 失败比率
     */
    private BigDecimal failureRatio;
}
