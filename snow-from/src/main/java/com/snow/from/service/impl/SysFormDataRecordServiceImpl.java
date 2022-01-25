package com.snow.from.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.common.core.text.Convert;
import com.snow.common.utils.DateUtils;
import com.snow.from.domain.SysFormDataRecord;
import com.snow.from.mapper.SysFormDataRecordMapper;
import com.snow.from.service.ISysFormDataRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单数据记录Service业务层处理
 * 
 * @author 阿吉
 * @date 2021-11-21
 */
@Service
public class SysFormDataRecordServiceImpl extends ServiceImpl<SysFormDataRecordMapper,SysFormDataRecord> implements ISysFormDataRecordService {

    @Autowired
    private SysFormDataRecordMapper sysFormDataRecordMapper;

    /**
     * 查询单数据记录
     * 
     * @param id 单数据记录ID
     * @return 单数据记录
     */
    @Override
    public SysFormDataRecord selectSysFormDataRecordById(Integer id) {
        return sysFormDataRecordMapper.selectById(id);
    }

    /**
     * 查询单数据记录
     *
     * @param formNo 单数据记录编号
     * @return 单数据记录
     */
    public SysFormDataRecord selectSysFormDataRecordByFormNo(String formNo){
        return sysFormDataRecordMapper.selectSysFormDataRecordByFormNo(formNo);
    }

    @Override
    public Integer getMaxVersionByUsrId(Long userId) {
        return sysFormDataRecordMapper.getMaxVersionByUsrId(userId);
    }

    /**
     * 查询单数据记录列表
     * 
     * @param sysFormDataRecord 单数据记录
     * @return 单数据记录
     */
    @Override
    public List<SysFormDataRecord> selectSysFormDataRecordList(SysFormDataRecord sysFormDataRecord) {
        LambdaQueryWrapper<SysFormDataRecord> lambda = new QueryWrapper<SysFormDataRecord>().lambda();
        lambda.like(StrUtil.isNotBlank(sysFormDataRecord.getFormNo()),SysFormDataRecord::getFormNo,sysFormDataRecord.getFormNo());
        lambda.like(StrUtil.isNotBlank(sysFormDataRecord.getBelongUserId()),SysFormDataRecord::getBelongUserId,sysFormDataRecord.getBelongUserId());
        lambda.eq(ObjectUtil.isNotEmpty(sysFormDataRecord.getId()),SysFormDataRecord::getId,sysFormDataRecord.getId());
        lambda.eq(ObjectUtil.isNotEmpty(sysFormDataRecord.getFormId()),SysFormDataRecord::getFormId,sysFormDataRecord.getFormId());
        lambda.eq(ObjectUtil.isNotEmpty(sysFormDataRecord.getFormStatus()),SysFormDataRecord::getFormStatus,sysFormDataRecord.getFormStatus());
        lambda.eq(ObjectUtil.isNotEmpty(sysFormDataRecord.getDingProcessInstanceId()),SysFormDataRecord::getDingProcessInstanceId,sysFormDataRecord.getDingProcessInstanceId());
        lambda.in(CollUtil.isNotEmpty(sysFormDataRecord.getFormIdList()),SysFormDataRecord::getFormId,sysFormDataRecord.getFormId());
        lambda.orderByDesc(SysFormDataRecord::getCreateTime);
        return sysFormDataRecordMapper.selectList(lambda);
    }

    /**
     * 新增单数据记录
     * 
     * @param sysFormDataRecord 单数据记录
     * @return 结果
     */
    @Override
    public int insertSysFormDataRecord(SysFormDataRecord sysFormDataRecord)
    {
        sysFormDataRecord.setCreateTime(DateUtils.getNowDate());
        return sysFormDataRecordMapper.insert(sysFormDataRecord);
    }

    /**
     * 修改单数据记录
     * 
     * @param sysFormDataRecord 单数据记录
     * @return 结果
     */
    @Override
    public int updateSysFormDataRecord(SysFormDataRecord sysFormDataRecord) {
        sysFormDataRecord.setUpdateTime(DateUtils.getNowDate());
        return sysFormDataRecordMapper.updateById(sysFormDataRecord);
    }

    @Override
    public int updateSysFormDataRecordByNo(SysFormDataRecord sysFormDataRecord) {
        sysFormDataRecord.setUpdateTime(DateUtils.getNowDate());
        return sysFormDataRecordMapper.updateSysFormDataRecordByNo(sysFormDataRecord);
    }

    /**
     * 删除单数据记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysFormDataRecordByIds(String ids) {
        return sysFormDataRecordMapper.deleteBatchIds(Convert.toStrList(ids));
    }

    /**
     * 删除单数据记录信息
     * 
     * @param id 单数据记录ID
     * @return 结果
     */
    @Override
    public int deleteSysFormDataRecordById(Integer id) {
        return sysFormDataRecordMapper.deleteById(id);
    }
}
