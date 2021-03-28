package com.snow.system.service.impl;

import java.util.List;
import java.util.Optional;

import com.snow.common.utils.DateUtils;
import com.snow.common.utils.StringUtils;
import com.snow.system.domain.SysDingProcinst;
import com.snow.system.domain.SysUser;
import com.snow.system.mapper.SysDingProcinstMapper;
import com.snow.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysDingRuTaskMapper;
import com.snow.system.domain.SysDingRuTask;
import com.snow.system.service.ISysDingRuTaskService;
import com.snow.common.core.text.Convert;

/**
 * 钉钉运行任务Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-03-24
 */
@Service
public class SysDingRuTaskServiceImpl implements ISysDingRuTaskService 
{
    @Autowired
    private SysDingRuTaskMapper sysDingRuTaskMapper;

    @Autowired
    private SysDingProcinstMapper sysDingProcinstMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询钉钉运行任务
     * 
     * @param id 钉钉运行任务ID
     * @return 钉钉运行任务
     */
    @Override
    public SysDingRuTask selectSysDingRuTaskById(String id)
    {
        SysDingRuTask sysDingRuTask = sysDingRuTaskMapper.selectSysDingRuTaskById(id);
        if(StringUtils.isNotNull(sysDingRuTask)&&StringUtils.isNotNull(sysDingRuTask.getProcInstId())){
            SysDingProcinst sysDingProcinst = sysDingProcinstMapper.selectSysDingProcinstByProcInstId(sysDingRuTask.getProcInstId());
            sysDingRuTask.setSysDingProcinst(sysDingProcinst);
        }
       
        return sysDingRuTask;
    }

    /**
     * 查询钉钉运行任务列表
     * 
     * @param sysDingRuTask 钉钉运行任务
     * @return 钉钉运行任务
     */
    @Override
    public List<SysDingRuTask> selectSysDingRuTaskList(SysDingRuTask sysDingRuTask)
    {
        List<SysDingRuTask> sysDingRuTaskList=sysDingRuTaskMapper.selectSysDingRuTaskList(sysDingRuTask);
        sysDingRuTaskList.parallelStream().forEach(t->{
            if(StringUtils.isNotNull(t.getProcInstId())){
                SysDingProcinst sysDingProcinst = sysDingProcinstMapper.selectSysDingProcinstByProcInstId(t.getProcInstId());
                Optional.ofNullable(sysDingProcinst).ifPresent(s->{
                    SysUser sysUser = sysUserMapper.selectUserByDingUserId(sysDingProcinst.getStartUserId());
                    sysDingProcinst.setStartUserName(sysUser.getUserName());
                });

                t.setSysDingProcinst(sysDingProcinst);
            }

        });
        return sysDingRuTaskList;
    }

    /**
     * 新增钉钉运行任务
     * 
     * @param sysDingRuTask 钉钉运行任务
     * @return 结果
     */
    @Override
    public int insertSysDingRuTask(SysDingRuTask sysDingRuTask)
    {
        return sysDingRuTaskMapper.insertSysDingRuTask(sysDingRuTask);
    }

    /**
     * 修改钉钉运行任务
     * 
     * @param sysDingRuTask 钉钉运行任务
     * @return 结果
     */
    @Override
    public int updateSysDingRuTask(SysDingRuTask sysDingRuTask)
    {
        return sysDingRuTaskMapper.updateSysDingRuTask(sysDingRuTask);
    }

    /**
     * 删除钉钉运行任务对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysDingRuTaskByIds(String ids)
    {
        return sysDingRuTaskMapper.deleteSysDingRuTaskByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除钉钉运行任务信息
     * 
     * @param id 钉钉运行任务ID
     * @return 结果
     */
    @Override
    public int deleteSysDingRuTaskById(String id)
    {
        return sysDingRuTaskMapper.deleteSysDingRuTaskById(id);
    }
}
