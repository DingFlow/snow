package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysFile;

/**
 * 文件存储Service接口
 * 
 * @author snow
 * @date 2021-01-11
 */
public interface ISysFileService 
{
    /**
     * 查询文件存储
     * 
     * @param id 文件存储ID
     * @return 文件存储
     */
    public SysFile selectSysFileById(Long id);


    /**
     * 查询文件存储
     *
     * @param key 文件存储key
     * @return 文件存储
     */
    public SysFile selectSysFileByKey(String key);

    /**
     * 查询文件存储列表
     * 
     * @param sysFile 文件存储
     * @return 文件存储集合
     */
    public List<SysFile> selectSysFileList(SysFile sysFile);

    /**
     * 新增文件存储
     * 
     * @param sysFile 文件存储
     * @return 结果
     */
    public int insertSysFile(SysFile sysFile);

    /**
     * 修改文件存储
     * 
     * @param sysFile 文件存储
     * @return 结果
     */
    public int updateSysFile(SysFile sysFile);

    /**
     * 批量删除文件存储
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFileByIds(String ids);

    /**
     * 删除文件存储信息
     * 
     * @param id 文件存储ID
     * @return 结果
     */
    public int deleteSysFileById(Long id);
}
