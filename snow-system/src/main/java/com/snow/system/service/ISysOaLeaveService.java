package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysOaLeave;

/**
 * 请假单Service接口
 * 
 * @author snow
 * @date 2020-11-22
 */
public interface ISysOaLeaveService 
{
    /**
     * 查询请假单
     * 
     * @param id 请假单ID
     * @return 请假单
     */
    public SysOaLeave selectSysOaLeaveById(Integer id);

    /**
     *
     * @param leaveNo
     * @return
     */
    public SysOaLeave selectSysOaLeaveByLeaveNo(String leaveNo);

    /**
     * 查询请假单列表
     * 
     * @param sysOaLeave 请假单
     * @return 请假单集合
     */
    public List<SysOaLeave> selectSysOaLeaveList(SysOaLeave sysOaLeave);

    /**
     * 新增请假单
     * 
     * @param sysOaLeave 请假单
     * @return 结果
     */
    public int insertSysOaLeave(SysOaLeave sysOaLeave);

    /**
     * 修改请假单
     * 
     * @param sysOaLeave 请假单
     * @return 结果
     */
    public int updateSysOaLeave(SysOaLeave sysOaLeave);

    /**
     * 根据单号修改请假单
     * @param sysOaLeave
     * @return
     */
    public int updateSysOaLeaveByLeaveNo(SysOaLeave sysOaLeave);

    /**
     * 批量删除请假单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOaLeaveByIds(String ids);

    /**
     * 删除请假单信息
     * 
     * @param id 请假单ID
     * @return 结果
     */
    public int deleteSysOaLeaveById(Integer id);
}
