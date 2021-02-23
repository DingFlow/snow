package com.snow.dingtalk.sync;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.snow.common.annotation.SyncLog;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.enums.SyncLogType;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.service.impl.DepartmentServiceImpl;
import com.snow.system.domain.SysDept;
import com.snow.system.service.impl.SysDeptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-15 11:25
 **/
@Component
@Slf4j
public class SyncSysDepartmentService implements ISyncSysInfo {

    @Autowired
    private SysDeptServiceImpl sysDeptService=SpringUtils.getBean(SysDeptServiceImpl.class);

    @Autowired
    private DepartmentServiceImpl departmentService=SpringUtils.getBean(DepartmentServiceImpl.class);

    @Override
    @SyncLog(dingTalkListenerType = DingTalkListenerType.DEPARTMENT_CREATE,syncLogTpye = SyncLogType.SYNC_SYS)
    public JSONObject SyncSysInfo(DingTalkListenerType dingTalkListenerType,JSONObject jsonObject) {
        Integer code = dingTalkListenerType.getCode();
        if(code==DingTalkListenerType.DEPARTMENT_CREATE.getCode()){
            insertDepartment(jsonObject);
        }
        return null;
    }

    public void insertDepartment(JSONObject jsonObject){
        try {
            List<Long> deptId = jsonObject.getJSONArray("DeptId").toJavaList(Long.class);
            for (int i=0;i<deptId.size();i++) {
                OapiV2DepartmentGetResponse.DeptGetResponse departmentDetail = departmentService.getDepartmentDetail(deptId.get(i));
                log.info("调用钉钉返回信息：{}",JSON.toJSONString(departmentDetail));
                SysDept sysDept=new SysDept();
                sysDept.setDeptId(departmentDetail.getDeptId());
                sysDept.setParentId(departmentDetail.getParentId());
                sysDept.setDeptName(departmentDetail.getName());
                sysDept.setIsSyncDingTalk(false);
                sysDept.setOrderNum(String.valueOf(departmentDetail.getOrder()));
                sysDeptService.insertDept(sysDept);
            }
        }catch (Exception e){
            throw new SyncDataException(jsonObject.toString(),"创建部门时，钉钉数据同步系统失败");
        }

    }
}
