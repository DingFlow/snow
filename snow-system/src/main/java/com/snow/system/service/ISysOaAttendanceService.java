package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysOaAttendance;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 考勤Service接口
 * 
 * @author Agee
 * @date 2022-01-26
 */
public interface ISysOaAttendanceService extends IService<SysOaAttendance>
{
    /**
     * 查询考勤
     * 
     * @param id 考勤ID
     * @return 考勤
     */
    public SysOaAttendance selectSysOaAttendanceById(Long id);

    /**
     * 查询考勤列表
     * 
     * @param sysOaAttendance 考勤
     * @return 考勤集合
     */
    public List<SysOaAttendance> selectSysOaAttendanceList(SysOaAttendance sysOaAttendance);

    /**
     * 新增考勤
     * 
     * @param sysOaAttendance 考勤
     * @return 结果
     */
    public int insertSysOaAttendance(SysOaAttendance sysOaAttendance);

    /**
     * 修改考勤
     * 
     * @param sysOaAttendance 考勤
     * @return 结果
     */
    public int updateSysOaAttendance(SysOaAttendance sysOaAttendance);

    /**
     * 批量删除考勤
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOaAttendanceByIds(String ids);

    /**
     * 删除考勤信息
     * 
     * @param id 考勤ID
     * @return 结果
     */
    public int deleteSysOaAttendanceById(Long id);
}
