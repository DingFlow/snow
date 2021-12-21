package com.snow.system.service.impl;

import com.snow.common.core.domain.Ztree;
import com.snow.common.core.text.Convert;
import com.snow.common.utils.bean.BeanUtils;
import com.snow.system.domain.RegionTreeVO;
import com.snow.system.domain.SysOaRegion;
import com.snow.system.mapper.SysOaRegionMapper;
import com.snow.system.service.ISysOaRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 地区Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-04-09
 */
@Service
public class SysOaRegionServiceImpl implements ISysOaRegionService 
{
    @Autowired
    private SysOaRegionMapper sysOaRegionMapper;

    /**
     * 查询地区
     * 
     * @param code 地区ID
     * @return 地区
     */
    @Override
    public SysOaRegion selectSysOaRegionById(Long code)
    {
        return sysOaRegionMapper.selectSysOaRegionById(code);
    }

    /**
     * 查询地区列表
     * 
     * @param sysOaRegion 地区
     * @return 地区
     */
    @Override
    public List<SysOaRegion> selectSysOaRegionList(SysOaRegion sysOaRegion)
    {
        return sysOaRegionMapper.selectSysOaRegionList(sysOaRegion);
    }

    /**
     * 新增地区
     * 
     * @param sysOaRegion 地区
     * @return 结果
     */
    @Override
    public int insertSysOaRegion(SysOaRegion sysOaRegion)
    {
        return sysOaRegionMapper.insertSysOaRegion(sysOaRegion);
    }

    /**
     * 修改地区
     * 
     * @param sysOaRegion 地区
     * @return 结果
     */
    @Override
    public int updateSysOaRegion(SysOaRegion sysOaRegion)
    {
        return sysOaRegionMapper.updateSysOaRegion(sysOaRegion);
    }

    /**
     * 删除地区对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysOaRegionByIds(String ids)
    {
        return sysOaRegionMapper.deleteSysOaRegionByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除地区信息
     * 
     * @param code 地区ID
     * @return 结果
     */
    @Override
    public int deleteSysOaRegionById(Long code)
    {
        return sysOaRegionMapper.deleteSysOaRegionById(code);
    }

    /**
     * 查询地区树列表
     * 
     * @return 所有地区信息
     */
    @Override
    public List<Ztree> selectSysOaRegionTree()
    {
        List<SysOaRegion> sysOaRegionList = sysOaRegionMapper.selectSysOaRegionList(new SysOaRegion());
        List<Ztree> ztrees = new ArrayList<Ztree>();
        for (SysOaRegion sysOaRegion : sysOaRegionList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(sysOaRegion.getCode());
            ztree.setpId(sysOaRegion.getPCode());
            ztree.setName(sysOaRegion.getName());
            ztree.setTitle(sysOaRegion.getName());
            ztrees.add(ztree);
        }
        return ztrees;
    }

    /**
     * 获取树形结构
     * @return
     */
    public List<RegionTreeVO> getSysOaRegionTree()
    {
        List<SysOaRegion> sysOaRegionList = sysOaRegionMapper.selectSysOaRegionList(new SysOaRegion());
        return initTreeData(BeanUtils.transformList(sysOaRegionList,RegionTreeVO.class));
    }



    private List<RegionTreeVO> initTreeData(List<RegionTreeVO> nodes) {
        ArrayList<RegionTreeVO> rootNode = new ArrayList<>();
        for(RegionTreeVO node:nodes){
            if(node.getPCode().equals(0L)){
                rootNode.add(node);
            }
        }
        for(RegionTreeVO node:rootNode){
            List<RegionTreeVO> child = getChild(node.getCode(), nodes);
            node.setChildren(child);
        }
        return rootNode;
    }


    private List<RegionTreeVO> getChild(Long id, List<RegionTreeVO> allNode) {
        //存放子菜单的集合
        List<RegionTreeVO> listChild = new ArrayList<>();
        for (RegionTreeVO node : allNode) {
            if (node.getPCode().equals(id)) {
                listChild.add(node);
            }
        }
        //递归：
        for (RegionTreeVO node : listChild) {
            node.setChildren(getChild(node.getCode(), allNode));
        }
        if (listChild.size() == 0) {
            return null;
        }
        return listChild;
    }
}
