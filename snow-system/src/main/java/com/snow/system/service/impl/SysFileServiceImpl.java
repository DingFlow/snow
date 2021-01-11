package com.snow.system.service.impl;

import java.util.List;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysFileMapper;
import com.snow.system.domain.SysFile;
import com.snow.system.service.ISysFileService;
import com.snow.common.core.text.Convert;

/**
 * 文件存储Service业务层处理
 * 
 * @author snow
 * @date 2021-01-11
 */
@Service
public class SysFileServiceImpl implements ISysFileService 
{
    @Autowired
    private SysFileMapper sysFileMapper;

    /**
     * 查询文件存储
     * 
     * @param id 文件存储ID
     * @return 文件存储
     */
    @Override
    public SysFile selectSysFileById(Long id)
    {
        return sysFileMapper.selectSysFileById(id);
    }

    @Override
    public SysFile selectSysFileByKey(String key) {
        return sysFileMapper.selectSysFileByKey(key);
    }

    /**
     * 查询文件存储列表
     * 
     * @param sysFile 文件存储
     * @return 文件存储
     */
    @Override
    public List<SysFile> selectSysFileList(SysFile sysFile)
    {
        return sysFileMapper.selectSysFileList(sysFile);
    }

    /**
     * 新增文件存储
     * 
     * @param sysFile 文件存储
     * @return 结果
     */
    @Override
    public int insertSysFile(SysFile sysFile)
    {
        sysFile.setCreateTime(DateUtils.getNowDate());
        return sysFileMapper.insertSysFile(sysFile);
    }

    /**
     * 修改文件存储
     * 
     * @param sysFile 文件存储
     * @return 结果
     */
    @Override
    public int updateSysFile(SysFile sysFile)
    {
        sysFile.setUpdateTime(DateUtils.getNowDate());
        return sysFileMapper.updateSysFile(sysFile);
    }

    /**
     * 删除文件存储对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysFileByIds(String ids)
    {
        return sysFileMapper.deleteSysFileByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除文件存储信息
     * 
     * @param id 文件存储ID
     * @return 结果
     */
    @Override
    public int deleteSysFileById(Long id)
    {
        return sysFileMapper.deleteSysFileById(id);
    }
}
