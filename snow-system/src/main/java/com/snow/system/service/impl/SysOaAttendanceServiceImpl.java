package com.snow.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.common.core.text.Convert;
import com.snow.common.utils.DateUtils;
import com.snow.system.domain.SysOaAttendance;
import com.snow.system.mapper.SysOaAttendanceMapper;
import com.snow.system.service.ISysOaAttendanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 考勤Service业务层处理
 *
 * @author Agee
 * @date 2022-01-26
 */
@Service
public class SysOaAttendanceServiceImpl extends ServiceImpl<SysOaAttendanceMapper, SysOaAttendance> implements ISysOaAttendanceService {
    @Resource
    private SysOaAttendanceMapper sysOaAttendanceMapper;

    /**
     * 查询考勤
     *
     * @param id 考勤ID
     * @return 考勤
     */
    @Override
    public SysOaAttendance selectSysOaAttendanceById(Long id) {
        return sysOaAttendanceMapper.selectById(id);
    }

    /**
     * 查询考勤列表
     *
     * @param sysOaAttendance 考勤
     * @return 考勤
     */
    @Override
    public List<SysOaAttendance> selectSysOaAttendanceList(SysOaAttendance sysOaAttendance) {
        LambdaQueryWrapper<SysOaAttendance> lambda = new QueryWrapper<SysOaAttendance>().lambda();
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getAttendanceCode()),SysOaAttendance::getAttendanceCode,sysOaAttendance.getAttendanceCode());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getSourceType()),SysOaAttendance::getSourceType,sysOaAttendance.getSourceType());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getProcInstId()),SysOaAttendance::getProcInstId,sysOaAttendance.getProcInstId());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getApproveId()),SysOaAttendance::getApproveId,sysOaAttendance.getApproveId());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getTimeResult()),SysOaAttendance::getTimeResult,sysOaAttendance.getTimeResult());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getLocationResult()),SysOaAttendance::getLocationResult,sysOaAttendance.getLocationResult());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getUserId()),SysOaAttendance::getUserId,sysOaAttendance.getUserId());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getCheckType()),SysOaAttendance::getCheckType,sysOaAttendance.getCheckType());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getUserCheckTime()),SysOaAttendance::getUserCheckTime,sysOaAttendance.getUserCheckTime());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getWorkDate()),SysOaAttendance::getWorkDate,sysOaAttendance.getWorkDate());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getRecordId()),SysOaAttendance::getRecordId,sysOaAttendance.getRecordId());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getPlanId()),SysOaAttendance::getPlanId,sysOaAttendance.getPlanId());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getGroupId()),SysOaAttendance::getGroupId,sysOaAttendance.getGroupId());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getAttendanceId()),SysOaAttendance::getAttendanceId,sysOaAttendance.getAttendanceId());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getBaseCheckTime()),SysOaAttendance::getBaseCheckTime,sysOaAttendance.getBaseCheckTime());
        lambda.eq(ObjectUtil.isNotEmpty(sysOaAttendance.getIsDelete()),SysOaAttendance::getIsDelete,sysOaAttendance.getIsDelete());
        return sysOaAttendanceMapper.selectList(lambda);
    }

    /**
     * 新增考勤
     *
     * @param sysOaAttendance 考勤
     * @return 结果
     */
    @Override
    public int insertSysOaAttendance(SysOaAttendance sysOaAttendance) {
        sysOaAttendance.setCreateTime(DateUtils.getNowDate());
        return sysOaAttendanceMapper.insert(sysOaAttendance);
    }

    /**
     * 修改考勤
     *
     * @param sysOaAttendance 考勤
     * @return 结果
     */
    @Override
    public int updateSysOaAttendance(SysOaAttendance sysOaAttendance) {
        sysOaAttendance.setUpdateTime(DateUtils.getNowDate());
        return sysOaAttendanceMapper.updateById(sysOaAttendance);
    }

    /**
     * 删除考勤对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     @Override
     public int deleteSysOaAttendanceByIds(String ids) {
        return sysOaAttendanceMapper.deleteBatchIds(Convert.toStrList(ids));
     }

    /**
     * 删除考勤信息
     *
     * @param id 考勤ID
     * @return 结果
     */
    @Override
    public int deleteSysOaAttendanceById(Long id) {
        return sysOaAttendanceMapper.deleteById(id);
    }
}
