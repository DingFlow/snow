package com.snow.quartz.task;

import cn.hutool.core.util.ObjectUtil;
import com.snow.common.enums.UserType;
import com.snow.flowable.common.enums.FlowDefEnum;
import com.snow.flowable.common.enums.FlowEventTypeEnum;
import com.snow.system.domain.SysNewsNode;
import com.snow.system.domain.SysNewsTriggers;
import com.snow.system.domain.SysUser;
import com.snow.system.mapper.SysUserMapper;
import com.snow.system.service.ISysNewsNodeService;
import com.snow.system.service.ISysNewsTriggersService;
import com.snow.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qimingjin
 * @date 2022-08-26 11:19
 * @Description: 同步流程消息配置
 */
@Component("syncFlowNewsConfigTask")
@Slf4j
@RequiredArgsConstructor
public class SyncFlowNewsConfigTask {

    private final ISysNewsNodeService newsNodeService;

    private final ISysNewsTriggersService triggersService;

    private final SysUserMapper userMapper;;

    public void syncNewsNode(){
        for (FlowDefEnum flowDefEnum: FlowDefEnum.values()) {
            SysNewsNode sysNewsNode = newsNodeService.selectSysNewsNodeByKey(flowDefEnum.getCode(), 1L);
            if(ObjectUtil.isNotEmpty(sysNewsNode)){
                continue;
            }
            SysNewsNode node=new SysNewsNode();
            node.setNewsNodeKey(flowDefEnum.getCode());
            node.setNewsNodeName(flowDefEnum.getInfo());
            node.setParentId(1L);
            newsNodeService.insertSysNewsNode(node);
            //保存子数据
            for (FlowEventTypeEnum flowEventTypeEnum: FlowEventTypeEnum.values()) {
                SysNewsNode sysNode = newsNodeService.selectSysNewsNodeByKey(flowEventTypeEnum.getCode(), node.getId().longValue());
                if(ObjectUtil.isEmpty(sysNode)){
                    SysNewsNode sonNode=new SysNewsNode();
                    sonNode.setNewsNodeKey(flowEventTypeEnum.getCode());
                    sonNode.setNewsNodeName(flowEventTypeEnum.getInfo());
                    sonNode.setParentId(node.getId().longValue());
                    newsNodeService.insertSysNewsNode(sonNode);
                    //初始化所有人的配置
                    SysUser sysUser=new SysUser();
                    sysUser.setUserType(UserType.SYS_USER_TYPE.getCode());
                    List<SysUser> sysUsers = userMapper.selectUserList(sysUser);
                    for (SysUser user: sysUsers) {
                        SysNewsTriggers sysNewsTriggers=new SysNewsTriggers();
                        sysNewsTriggers.setNewsNodeId(sonNode.getId());
                        sysNewsTriggers.setNewsType(1);
                        sysNewsTriggers.setNewsOnOff(0);
                        sysNewsTriggers.setUserId(user.getUserId().toString());
                        triggersService.insertSysNewsTriggers(sysNewsTriggers);
                        sysNewsTriggers.setNewsType(2);
                        triggersService.insertSysNewsTriggers(sysNewsTriggers);
                    }
                }
            }
        }
    }
}
