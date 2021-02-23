package com.snow.system.service.impl;

import java.util.List;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysOaLeaveMapper;
import com.snow.system.domain.SysOaLeave;
import com.snow.system.service.ISysOaLeaveService;
import com.snow.common.core.text.Convert;

/**
 * 请假单Service业务层处理
 * 
 * @author snow
 * @date 2020-11-22
 */
@Service
public class SysOaLeaveServiceImpl implements ISysOaLeaveService 
{
    @Autowired
    private SysOaLeaveMapper sysOaLeaveMapper;

    /**
     * 查询请假单
     * 
     * @param id 请假单ID
     * @return 请假单
     */
    @Override
    public SysOaLeave selectSysOaLeaveById(Integer id)
    {
        return sysOaLeaveMapper.selectSysOaLeaveById(id);
    }

    /**
     *
     * @param leaveNo
     * @return
     */
    @Override
    public SysOaLeave selectSysOaLeaveByLeaveNo(String leaveNo) {
        return sysOaLeaveMapper.selectSysOaLeaveByLeaveNo(leaveNo);
    }

    /**
     * 查询请假单列表
     * 
     * @param sysOaLeave 请假单
     * @return 请假单
     */
    @Override
    public List<SysOaLeave> selectSysOaLeaveList(SysOaLeave sysOaLeave)
    {
        return sysOaLeaveMapper.selectSysOaLeaveList(sysOaLeave);
    }

    /**
     * 新增请假单
     * 
     * @param sysOaLeave 请假单
     * @return 结果
     */
    @Override
    public int insertSysOaLeave(SysOaLeave sysOaLeave)
    {
        sysOaLeave.setCreateTime(DateUtils.getNowDate());
        return sysOaLeaveMapper.insertSysOaLeave(sysOaLeave);
    }

    /**
     * 修改请假单
     * 
     * @param sysOaLeave 请假单
     * @return 结果
     */
    @Override
    public int updateSysOaLeave(SysOaLeave sysOaLeave)
    {
        sysOaLeave.setUpdateTime(DateUtils.getNowDate());
        return sysOaLeaveMapper.updateSysOaLeave(sysOaLeave);
    }

    @Override
    public int updateSysOaLeaveByLeaveNo(SysOaLeave sysOaLeave) {
        sysOaLeave.setUpdateTime(DateUtils.getNowDate());
        return sysOaLeaveMapper.updateSysOaLeaveByLeaveNo(sysOaLeave);
    }

    /**
     * 删除请假单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysOaLeaveByIds(String ids)
    {
        return sysOaLeaveMapper.deleteSysOaLeaveByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除请假单信息
     * 
     * @param id 请假单ID
     * @return 结果
     */
    @Override
    public int deleteSysOaLeaveById(Integer id)
    {
        return sysOaLeaveMapper.deleteSysOaLeaveById(id);
    }
}
