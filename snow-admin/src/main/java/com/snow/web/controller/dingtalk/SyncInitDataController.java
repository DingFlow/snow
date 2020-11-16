package com.snow.web.controller.dingtalk;

import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.snow.common.utils.StringUtils;
import com.snow.dingtalk.service.impl.DepartmentServiceImpl;
import com.snow.system.domain.SysDept;
import com.snow.system.service.impl.SysDeptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author qimingjin
 * @Title: 同步初始化数据
 * @Description:
 * @date 2020/11/16 9:51
 */
@RestController
@RequestMapping("/syncInitData")
public class SyncInitDataController {

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Autowired
    private SysDeptServiceImpl sysDeptService;

    @GetMapping("/initDepartment")
    public void initDepartment(){
        List<OapiDepartmentListResponse.Department> dingTalkDepartmentList = departmentService.getDingTalkDepartmentList();
        dingTalkDepartmentList.stream().forEach(t->{
            SysDept sysDept=new SysDept();
            SysDept sysDepts = sysDeptService.selectDeptById(t.getId());
            if(StringUtils.isNotNull(sysDepts)){
                sysDept.setDeptId(t.getId());
                sysDept.setDeptName(t.getName());
                sysDept.setOrderNum(String.valueOf(t.getId()));
                sysDept.setParentId(t.getParentid());
                sysDept.setIsSyncDingTalk(false);
                sysDeptService.updateDept(sysDept);
            }else {
                sysDept.setDeptId(t.getId());
                sysDept.setDeptName(t.getName());
                sysDept.setOrderNum(String.valueOf(t.getId()));
                sysDept.setParentId(t.getParentid());
                sysDept.setIsSyncDingTalk(false);
                sysDeptService.insertDept(sysDept);
            }
        });
    }
}
