package com.snow.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.system.domain.SysOaEmail;
import com.snow.system.domain.SysOaEmailDTO;
import com.snow.system.domain.SysOaEmailDO;
import com.snow.system.domain.SysOaEmailVO;

/**
 * 邮件Mapper接口
 * 
 * @author 没用的阿吉
 * @date 2021-03-12
 */
public interface SysOaEmailMapper extends BaseMapper<SysOaEmail> {
    /**
     * 查询邮件
     * 
     * @param id 邮件ID
     * @return 邮件
     */
    public SysOaEmailDTO selectSysOaEmailById(Long id);


    public SysOaEmailDTO selectSysOaEmailByEmailNo(String emailNo);

    /**
     * 查询邮件列表
     * 
     * @param sysOaEmail 邮件
     * @return 邮件集合
     */
    public List<SysOaEmailDTO> selectSysOaEmailList(SysOaEmailDTO sysOaEmail);


    /**
     * 查询邮件列表
     * @param sysOaEmailDO
     * @return
     */
    public List<SysOaEmailVO> selectEmailList(SysOaEmailDO sysOaEmailDO);


    /**
     * 新增邮件
     * 
     * @param sysOaEmail 邮件
     * @return 结果
     */
    public int insertSysOaEmail(SysOaEmailDTO sysOaEmail);

    /**
     * 修改邮件
     * 
     * @param sysOaEmail 邮件
     * @return 结果
     */
    public int updateSysOaEmail(SysOaEmailDTO sysOaEmail);

    /**
     *
     * @param sysOaEmail
     * @return
     */
    public int updateSysOaEmailByEmailNo(SysOaEmailDTO sysOaEmail);

    /**
     * 删除邮件
     * 
     * @param id 邮件ID
     * @return 结果
     */
    public int deleteSysOaEmailById(Long id);

    /**
     * 批量删除邮件
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOaEmailByIds(String[] ids);
}
