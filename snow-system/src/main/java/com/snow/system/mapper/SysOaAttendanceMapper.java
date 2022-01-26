package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.SysOaAttendance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 考勤Mapper接口
 * 
 * @author Agee
 * @date 2022-01-26
 */
public interface SysOaAttendanceMapper extends BaseMapper<SysOaAttendance>
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
     * 删除考勤
     * 
     * @param id 考勤ID
     * @return 结果
     */
    public int deleteSysOaAttendanceById(Long id);

    /**
     * 批量删除考勤
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOaAttendanceByIds(String[] ids);
}
