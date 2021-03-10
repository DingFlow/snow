package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysOaResign;

/**
 * 离职申请单Service接口
 * 
 * @author 没用的阿吉
 * @date 2021-03-10
 */
public interface ISysOaResignService 
{
    /**
     * 查询离职申请单
     * 
     * @param id 离职申请单ID
     * @return 离职申请单
     */
    public SysOaResign selectSysOaResignById(Integer id);

    /**
     * 查询离职申请单列表
     * 
     * @param sysOaResign 离职申请单
     * @return 离职申请单集合
     */
    public List<SysOaResign> selectSysOaResignList(SysOaResign sysOaResign);

    /**
     * 新增离职申请单
     * 
     * @param sysOaResign 离职申请单
     * @return 结果
     */
    public int insertSysOaResign(SysOaResign sysOaResign);

    /**
     * 修改离职申请单
     * 
     * @param sysOaResign 离职申请单
     * @return 结果
     */
    public int updateSysOaResign(SysOaResign sysOaResign);

    /**
     * 根据离职单号修改申请单
     * @param sysOaResign
     * @return
     */
    public int updateSysOaResignByResignNo(SysOaResign sysOaResign);

    /**
     * 批量删除离职申请单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOaResignByIds(String ids);

    /**
     * 删除离职申请单信息
     * 
     * @param id 离职申请单ID
     * @return 结果
     */
    public int deleteSysOaResignById(Integer id);
}
