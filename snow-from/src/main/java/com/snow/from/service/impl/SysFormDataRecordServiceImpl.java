package com.snow.from.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.snow.common.core.text.Convert;
import com.snow.common.utils.DateUtils;
import com.snow.from.domain.SysFormDataRecord;
import com.snow.from.domain.SysFormInstance;
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
public class SysFormDataRecordServiceImpl implements ISysFormDataRecordService
{
    @Autowired
    private SysFormDataRecordMapper sysFormDataRecordMapper;

    @Autowired
    private SysFormInstanceServiceImpl sysFormInstanceService;

    /**
     * 查询单数据记录
     * 
     * @param id 单数据记录ID
     * @return 单数据记录
     */
    @Override
    public SysFormDataRecord selectSysFormDataRecordById(Integer id)
    {
        return sysFormDataRecordMapper.selectSysFormDataRecordById(id);
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
    public List<SysFormDataRecord> selectSysFormDataRecordList(SysFormDataRecord sysFormDataRecord)
    {
        List<SysFormDataRecord>  sysFormDataRecordList=sysFormDataRecordMapper.selectSysFormDataRecordList(sysFormDataRecord);
        return sysFormDataRecordList;
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
        return sysFormDataRecordMapper.insertSysFormDataRecord(sysFormDataRecord);
    }

    /**
     * 修改单数据记录
     * 
     * @param sysFormDataRecord 单数据记录
     * @return 结果
     */
    @Override
    public int updateSysFormDataRecord(SysFormDataRecord sysFormDataRecord)
    {
        sysFormDataRecord.setUpdateTime(DateUtils.getNowDate());
        return sysFormDataRecordMapper.updateSysFormDataRecord(sysFormDataRecord);
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
    public int deleteSysFormDataRecordByIds(String ids)
    {
        return sysFormDataRecordMapper.deleteSysFormDataRecordByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除单数据记录信息
     * 
     * @param id 单数据记录ID
     * @return 结果
     */
    @Override
    public int deleteSysFormDataRecordById(Integer id)
    {
        return sysFormDataRecordMapper.deleteSysFormDataRecordById(id);
    }
}
