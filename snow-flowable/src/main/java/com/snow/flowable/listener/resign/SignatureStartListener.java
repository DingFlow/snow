package com.snow.flowable.listener.resign;

import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.domain.resign.SysOaResignForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import com.snow.system.domain.SysOaResign;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysDeptServiceImpl;
import com.snow.system.service.impl.SysOaResignServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: snow
 * @description 离职申请开始监听器
 * @author: 没用的阿吉
 * @create: 2021-03-10 21:59
 **/
@Service
@Slf4j
public class SignatureStartListener extends AbstractExecutionListener<SysOaResignForm> {

    @Autowired
    private SysOaResignServiceImpl sysOaResignService;

    @Autowired
    private SysDeptServiceImpl sysDeptService;

    @Override
    protected void process() {
        //获取交接人
        Object variable = getVariable(SysOaResignForm.TRANSITION_PERSON);
        //开始的节点是获取不到表单的，因为这个时候流程id是生成了但是由于底层事务的关系，还没有落库。
        String businessKey= getBusinessKey();
        SysOaResign sysOaResign=new SysOaResign();
        sysOaResign.setProcessStatus(ProcessStatus.CHECKING.ordinal());
        sysOaResign.setResignNo(businessKey);
        //设置交际人参数
        setVariable(SysOaResignForm.TRANSITION_PERSON,variable);
        sysOaResignService.updateSysOaResignByResignNo(sysOaResign);

        //设置部门主管
        List<SysUser> deptLeaderList = sysDeptService.selectLeadsByUserId(getStartUserInfo().getUserId());
        if(CollectionUtils.isEmpty(deptLeaderList)){
            //管理员id
            setVariable("deptLeader",1);
        }else {
            setVariable("deptLeader",deptLeaderList.stream().map(s->String.valueOf(s.getUserId())).collect(Collectors.toList()));
        }
    }
}
