package com.snow.dingtalk.sync;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.snow.common.annotation.DingTalkLog;
import com.snow.common.constant.Constants;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.enums.DingTalkLogType;
import com.snow.common.enums.SyncLogType;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.service.impl.DepartmentServiceImpl;
import com.snow.system.domain.SysDept;
import com.snow.system.mapper.SysDeptMapper;
import com.snow.system.service.impl.SysDeptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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

    @Resource
    private SysDeptMapper sysDeptMapper=SpringUtils.getBean(SysDeptMapper.class);

    @Override

    public JSONObject SyncSysInfo(DingTalkListenerType dingTalkListenerType,JSONObject jsonObject) {
        Integer code = dingTalkListenerType.getCode();
        if(code==DingTalkListenerType.DEPARTMENT_CREATE.getCode()){
            insertDepartment(jsonObject);
        }else if(code==DingTalkListenerType.DEPARTMENT_UPDATE.getCode()){
            updateDepartment(jsonObject);
        }else if(code==DingTalkListenerType.DEPARTMENT_DELETED.getCode()){
            deleteDepartment(jsonObject);
        }
        return null;
    }

    /**
     * 根据订订新增部门数据
     * @param jsonObject
     */
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_CREATE,syncLogTpye = SyncLogType.SYNC_SYS)
    public void insertDepartment(JSONObject jsonObject){
        try {
            List<Long> deptId = jsonObject.getJSONArray("DeptId").toJavaList(Long.class);
            for (int i=0;i<deptId.size();i++) {
                OapiV2DepartmentGetResponse.DeptGetResponse departmentDetail = departmentService.getDepartmentDetail(deptId.get(i));
                SysDept sysDept=new SysDept();
                sysDept.setDeptId(departmentDetail.getDeptId());
                sysDept.setParentId(departmentDetail.getParentId());
                sysDept.setDeptName(departmentDetail.getName());
                sysDept.setIsSyncDingTalk(false);
                sysDept.setOrderNum(String.valueOf(departmentDetail.getOrder()));
                //获取部门主管列表（本期只取一个）
                List<String> deptManagerUseridList = departmentDetail.getDeptManagerUseridList();
                if(!CollectionUtils.isEmpty(deptManagerUseridList)){
                    sysDept.setLeader(deptManagerUseridList);
                }
                //是否是系统添加
                String sourceIdentifier= departmentDetail.getSourceIdentifier();
                if(StringUtils.isNotEmpty(sourceIdentifier)&&!sourceIdentifier.equals(Constants.Ding_Flow)){
                    sysDeptService.insertDept(sysDept);
                }
            }
        }catch (Exception e){
            throw new SyncDataException(jsonObject.toString(),"创建部门时，钉钉数据同步系统失败");
        }

    }

    /**
     * 根据订订修改部门数据
     */
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_UPDATE,syncLogTpye = SyncLogType.SYNC_SYS)
    public void updateDepartment(JSONObject jsonObject){
        try {
            List<Long> deptId = jsonObject.getJSONArray("DeptId").toJavaList(Long.class);
            for (int i=0;i<deptId.size();i++) {
                OapiV2DepartmentGetResponse.DeptGetResponse departmentDetail = departmentService.getDepartmentDetail(deptId.get(i));
                SysDept sysDept=new SysDept();
                sysDept.setDeptId(departmentDetail.getDeptId());
                sysDept.setParentId(departmentDetail.getParentId());
                sysDept.setDeptName(departmentDetail.getName());
                sysDept.setIsSyncDingTalk(false);
                sysDept.setOrderNum(String.valueOf(departmentDetail.getOrder()));
                //获取部门主管列表（本期只取一个）
                List<String> deptManagerUseridList = departmentDetail.getDeptManagerUseridList();
                if(!CollectionUtils.isEmpty(deptManagerUseridList)){
                    sysDept.setLeader(deptManagerUseridList);
                }
                sysDeptService.updateDept(sysDept);
            }
        }catch (Exception e){
            throw new SyncDataException(jsonObject.toString(),"修改部门时，钉钉数据同步系统失败");
        }
    }

    /**
     * 根据订订删除部门数据
     */
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_DELETED,syncLogTpye = SyncLogType.SYNC_SYS)
    public void deleteDepartment(JSONObject jsonObject){
        try {
            List<Long> deptId = jsonObject.getJSONArray("DeptId").toJavaList(Long.class);
            for (Long id : deptId) {
                sysDeptMapper.deleteDeptById(id);
            }
        }catch (Exception e){
            throw new SyncDataException(jsonObject.toString(),"删除部门时，钉钉数据同步系统失败");
        }
    }
}
