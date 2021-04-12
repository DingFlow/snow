package com.snow.system.service.impl;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import com.snow.common.core.text.Convert;
import com.snow.common.utils.StringUtils;
import com.snow.system.domain.SysDingHiTask;
import com.snow.system.domain.SysDingProcinst;
import com.snow.system.domain.SysUser;
import com.snow.system.mapper.SysDingHiTaskMapper;
import com.snow.system.mapper.SysUserMapper;
import com.snow.system.service.ISysDingHiTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 历史任务Service业务层处理
 *
 * @author 没用的阿吉
 * @date 2021-03-24
 */
@Service
public class SysDingHiTaskServiceImpl implements ISysDingHiTaskService
{
    @Resource
    private SysDingHiTaskMapper sysDingHiTaskMapper;

    @Autowired
    private SysDingProcinstServiceImpl sysDingProcinstService;


    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 查询历史任务
     *
     * @param id 历史任务ID
     * @return 历史任务
     */
    @Override
    public SysDingHiTask selectSysDingHiTaskById(String id)
    {
        return sysDingHiTaskMapper.selectSysDingHiTaskById(id);
    }

    /**
     * 查询历史任务列表
     *
     * @param sysDingHiTask 历史任务
     * @return 历史任务
     */
    @Override
    public List<SysDingHiTask> selectSysDingHiTaskList(SysDingHiTask sysDingHiTask)
    {
        List<SysDingHiTask> sysDingHiTaskList =sysDingHiTaskMapper.selectSysDingHiTaskList(sysDingHiTask);
        sysDingHiTaskList.parallelStream().forEach(t->{
            if(StringUtils.isNotNull(t.getProcInstId())){
                SysDingProcinst sysDingProcinst = sysDingProcinstService.selectSysDingProcinstByProcInstId(t.getProcInstId());
                Optional.ofNullable(sysDingProcinst).ifPresent(s->{
                    SysUser sysUser = sysUserMapper.selectUserByDingUserId(sysDingProcinst.getStartUserId());
                    sysDingProcinst.setStartUserName(sysUser.getUserName());
                });

                t.setSysDingProcinst(sysDingProcinst);
            }

            Optional.ofNullable(t.getFinishTime()).ifPresent(s->t.setTaskSpendTime( DateUtil.formatBetween(t.getCreateTime(), t.getFinishTime(), BetweenFormater.Level.SECOND)));


        });
        return sysDingHiTaskList;
    }

    /**
     * 新增历史任务
     *
     * @param sysDingHiTask 历史任务
     * @return 结果
     */
    @Override
    public int insertSysDingHiTask(SysDingHiTask sysDingHiTask)
    {
        return sysDingHiTaskMapper.insertSysDingHiTask(sysDingHiTask);
    }

    /**
     * 修改历史任务
     *
     * @param sysDingHiTask 历史任务
     * @return 结果
     */
    @Override
    public int updateSysDingHiTask(SysDingHiTask sysDingHiTask)
    {
        return sysDingHiTaskMapper.updateSysDingHiTask(sysDingHiTask);
    }

    /**
     * 删除历史任务对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysDingHiTaskByIds(String ids)
    {
        return sysDingHiTaskMapper.deleteSysDingHiTaskByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除历史任务信息
     *
     * @param id 历史任务ID
     * @return 结果
     */
    @Override
    public int deleteSysDingHiTaskById(String id)
    {
        return sysDingHiTaskMapper.deleteSysDingHiTaskById(id);
    }
}
