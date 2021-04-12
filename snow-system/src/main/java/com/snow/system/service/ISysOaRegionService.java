package com.snow.system.service;

import java.util.List;

import com.snow.system.domain.RegionTreeVO;
import com.snow.system.domain.SysOaRegion;
import com.snow.common.core.domain.Ztree;

/**
 * 地区Service接口
 * 
 * @author 没用的阿吉
 * @date 2021-04-09
 */
public interface ISysOaRegionService 
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
     * 批量删除地区
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOaRegionByIds(String ids);

    /**
     * 删除地区信息
     * 
     * @param code 地区ID
     * @return 结果
     */
    public int deleteSysOaRegionById(Long code);

    /**
     * 查询地区树列表
     * 
     * @return 所有地区信息
     */
    public List<Ztree> selectSysOaRegionTree();


    /**
     * 获取省市区树形结构
     * @return
     */
    public List<RegionTreeVO> getSysOaRegionTree();
}
