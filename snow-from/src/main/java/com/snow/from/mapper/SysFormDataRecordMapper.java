package com.snow.from.mapper;

import java.util.List;

import com.snow.from.domain.SysFormDataRecord;

/**
 * 单数据记录Mapper接口
 * 
 * @author 阿吉
 * @date 2021-11-21
 */
public interface SysFormDataRecordMapper 
{
    /**
     * 查询单数据记录
     * 
     * @param id 单数据记录ID
     * @return 单数据记录
     */
    public SysFormDataRecord selectSysFormDataRecordById(Integer id);

    /**
     * 查询单数据记录
     *
     * @param formNo 单数据记录编号
     * @return 单数据记录
     */
    public SysFormDataRecord selectSysFormDataRecordByFormNo(String formNo);

    /**
     * 获取最大版本号
     * @param userId 用户id
     * @return 最大版本号
     */
    Integer getMaxVersionByUsrId(Long userId);

    /**
     * 查询单数据记录列表
     * 
     * @param sysFormDataRecord 单数据记录
     * @return 单数据记录集合
     */
    public List<SysFormDataRecord> selectSysFormDataRecordList(SysFormDataRecord sysFormDataRecord);

    /**
     * 新增单数据记录
     * 
     * @param sysFormDataRecord 单数据记录
     * @return 结果
     */
    public int insertSysFormDataRecord(SysFormDataRecord sysFormDataRecord);

    /**
     * 修改单数据记录
     * 
     * @param sysFormDataRecord 单数据记录
     * @return 结果
     */
    public int updateSysFormDataRecord(SysFormDataRecord sysFormDataRecord);

    /**
     * 修改单数据记录（单号）
     *
     * @param sysFormDataRecord 单数据记录
     * @return 结果
     */
    public int updateSysFormDataRecordByNo(SysFormDataRecord sysFormDataRecord);

    /**
     * 删除单数据记录
     * 
     * @param id 单数据记录ID
     * @return 结果
     */
    public int deleteSysFormDataRecordById(Integer id);

    /**
     * 批量删除单数据记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFormDataRecordByIds(String[] ids);
}
