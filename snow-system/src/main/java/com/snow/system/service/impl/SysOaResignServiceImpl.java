package com.snow.system.service.impl;

import java.util.List;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysOaResignMapper;
import com.snow.system.domain.SysOaResign;
import com.snow.system.service.ISysOaResignService;
import com.snow.common.core.text.Convert;

/**
 * 离职申请单Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-03-10
 */
@Service
public class SysOaResignServiceImpl implements ISysOaResignService 
{
    @Autowired
    private SysOaResignMapper sysOaResignMapper;

    @Autowired
    private SysUserServiceImpl sysUserService;

    /**
     * 查询离职申请单
     * 
     * @param id 离职申请单ID
     * @return 离职申请单
     */
    @Override
    public SysOaResign selectSysOaResignById(Integer id)
    {
        SysOaResign sysOaResign=sysOaResignMapper.selectSysOaResignById(id);
        sysOaResign.setApplyPersonName(sysUserService.selectUserById(Long.parseLong(sysOaResign.getApplyPerson())).getUserName());
        sysOaResign.setTransitionPersonName(sysUserService.selectUserById(Long.parseLong(sysOaResign.getTransitionPerson())).getUserName());
        return sysOaResign;
    }

    /**
     * 查询离职申请单列表
     * 
     * @param sysOaResign 离职申请单
     * @return 离职申请单
     */
    @Override
    public List<SysOaResign> selectSysOaResignList(SysOaResign sysOaResign)
    {
        List<SysOaResign> list=sysOaResignMapper.selectSysOaResignList(sysOaResign);

        list.parallelStream().forEach(t->{
            t.setApplyPersonName(sysUserService.selectUserById(Long.parseLong(t.getApplyPerson())).getUserName());
            t.setTransitionPersonName(sysUserService.selectUserById(Long.parseLong(t.getTransitionPerson())).getUserName());
        });
        return list;
    }

    /**
     * 新增离职申请单
     * 
     * @param sysOaResign 离职申请单
     * @return 结果
     */
    @Override
    public int insertSysOaResign(SysOaResign sysOaResign)
    {
        sysOaResign.setCreateTime(DateUtils.getNowDate());
        return sysOaResignMapper.insertSysOaResign(sysOaResign);
    }

    /**
     * 修改离职申请单
     * 
     * @param sysOaResign 离职申请单
     * @return 结果
     */
    @Override
    public int updateSysOaResign(SysOaResign sysOaResign)
    {
        sysOaResign.setUpdateTime(DateUtils.getNowDate());
        return sysOaResignMapper.updateSysOaResign(sysOaResign);
    }

    /**
     * 根据离职单号修改申请单
     * @param sysOaResign
     * @return
     */
    @Override
    public int updateSysOaResignByResignNo(SysOaResign sysOaResign) {
        sysOaResign.setUpdateTime(DateUtils.getNowDate());
        return sysOaResignMapper.updateSysOaResignByResignNo(sysOaResign);
    }

    /**
     * 删除离职申请单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysOaResignByIds(String ids)
    {
        return sysOaResignMapper.deleteSysOaResignByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除离职申请单信息
     * 
     * @param id 离职申请单ID
     * @return 结果
     */
    @Override
    public int deleteSysOaResignById(Integer id)
    {
        return sysOaResignMapper.deleteSysOaResignById(id);
    }
}
