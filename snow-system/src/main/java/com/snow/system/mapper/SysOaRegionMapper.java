package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.SysOaRegion;

/**
 * 地区Mapper接口
 * 
 * @author 没用的阿吉
 * @date 2021-04-09
 */
public interface SysOaRegionMapper 
{
    /**
     * 查询地区
     * 
     * @param code 地区ID
     * @return 地区
     */
    public SysOaRegion selectSysOaRegionById(Long code);

    /**
     * 查询地区列表
     * 
     * @param sysOaRegion 地区
     * @return 地区集合
     */
    public List<SysOaRegion> selectSysOaRegionList(SysOaRegion sysOaRegion);

    /**
     * 新增地区
     * 
     * @param sysOaRegion 地区
     * @return 结果
     */
    public int insertSysOaRegion(SysOaRegion sysOaRegion);

    /**
     * 修改地区
     * 
     * @param sysOaRegion 地区
     * @return 结果
     */
    public int updateSysOaRegion(SysOaRegion sysOaRegion);

    /**
     * 删除地区
     * 
     * @param code 地区ID
     * @return 结果
     */
    public int deleteSysOaRegionById(Long code);

    /**
     * 批量删除地区
     * 
     * @param codes 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOaRegionByIds(String[] codes);
}
