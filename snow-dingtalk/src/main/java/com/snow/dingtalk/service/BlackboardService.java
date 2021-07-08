package com.snow.dingtalk.service;

import com.snow.system.domain.SysNotice;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-14 15:16
 **/
public interface BlackboardService {

    /**
     * 更新
     * @param sysNotice
     */
    String createBlackboard(SysNotice sysNotice);

    /**
     * 删除
     * @param blackboardId
     */
    @Deprecated
    void deleteBlackboard(String blackboardId);

    /**
     * 更新
     * @param sysNotice
     */
    @Deprecated
    void updateBlackboard(SysNotice sysNotice);



}
