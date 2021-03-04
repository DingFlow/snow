package com.snow.framework.web.service;

import com.snow.common.constant.Constants;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysNewsNode;
import com.snow.system.domain.SysNewsTriggers;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysNewsNodeServiceImpl;
import com.snow.system.service.impl.SysNewsTriggersServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/3/3 17:03
 */
@Service
public class NewsTriggerService {

    @Autowired
    private SysNewsNodeServiceImpl sysNewsNodeService;

    @Autowired
    private SysNewsTriggersServiceImpl sysNewsTriggersService;


    /**
     * 获取Email消息通知开关
     * @param nodeKey
     * @return
     */
    public boolean getEmailOnOff(String parentNodeKey,String nodeKey)
    {
        return getNewsOnOff(parentNodeKey,nodeKey,Constants.NEWS_EMAIL_TYPE);
    }


    /**
     * 获取钉钉开关（0--关，1--开）
     * @param nodeKey
     * @return
     */
    public boolean getDingTalkOnOff(String parentNodeKey,String nodeKey)
    {
        return getNewsOnOff(parentNodeKey,nodeKey,Constants.NEWS_DINGDING_TYPE);
    }


    private boolean getNewsOnOff(String parentNodeKey,String nodeKey,Integer newsType){
        SysNewsNode sysNewsNodes=new SysNewsNode();
        sysNewsNodes.setNewsNodeKey(parentNodeKey);
        List<SysNewsNode> sysNewsNodeList = sysNewsNodeService.selectSysNewsNodeList(sysNewsNodes);

        SysNewsNode sysNewsNode = sysNewsNodeService.selectSysNewsNodeByKey(nodeKey,sysNewsNodeList.get(0).getId().longValue());
        if(null == sysNewsNode){
            return false;
        }
        SysNewsTriggers sysNewsTriggers=new SysNewsTriggers();
        sysNewsTriggers.setNewsNodeId(sysNewsNode.getId());
        sysNewsTriggers.setNewsType(newsType);
        SysUser sysUser = ShiroUtils.getSysUser();
        sysNewsTriggers.setUserId(String.valueOf(sysUser.getUserId()));
        List<SysNewsTriggers> sysNewsTriggersList = sysNewsTriggersService.selectSysNewsTriggersList(sysNewsTriggers);
        if(CollectionUtils.isNotEmpty(sysNewsTriggersList)){
            Integer newsOnOff = sysNewsTriggersList.get(0).getNewsOnOff();
            return newsOnOff==1;
        }
        return false;
    }
}
