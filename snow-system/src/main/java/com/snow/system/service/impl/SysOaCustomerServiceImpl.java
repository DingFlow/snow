package com.snow.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.snow.common.core.text.Convert;
import com.snow.common.utils.DateUtils;
import com.snow.common.utils.StringUtils;
import com.snow.system.domain.SysOaCustomer;
import com.snow.system.mapper.SysOaCustomerMapper;
import com.snow.system.service.ISysOaCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-04-09
 */
@Service
public class SysOaCustomerServiceImpl implements ISysOaCustomerService 
{
    @Autowired
    private SysOaCustomerMapper sysOaCustomerMapper;

    @Autowired
    private SysUserServiceImpl sysUserServicel;

    /**
     * 查询客户
     * 
     * @param id 客户ID
     * @return 客户
     */
    @Override
    public SysOaCustomer selectSysOaCustomerById(Long id)
    {
        return sysOaCustomerMapper.selectSysOaCustomerById(id);
    }

    @Override
    public SysOaCustomer selectSysOaCustomerByCustomerNo(String customerNo) {
        return sysOaCustomerMapper.selectSysOaCustomerByCustomerNo(customerNo);
    }

    @Override
    public SysOaCustomer selectSysOaCustomerByCustomerName(String customerName) {
        return sysOaCustomerMapper.selectSysOaCustomerByCustomerName(customerName);
    }

    /**
     * 查询客户列表
     * 
     * @param sysOaCustomer 客户
     * @return 客户
     */
    @Override
    public List<SysOaCustomer> selectSysOaCustomerList(SysOaCustomer sysOaCustomer)
    {
        List<SysOaCustomer> sysOaCustomerList= sysOaCustomerMapper.selectSysOaCustomerList(sysOaCustomer);
        if(CollUtil.isNotEmpty(sysOaCustomerList)){
            sysOaCustomerList.forEach(t->{
                if(StringUtils.isNotNull(t.getCustomerManager())){
                    t.setCustomerManagerName(sysUserServicel.selectUserById(Long.parseLong(t.getCustomerManager())).getUserName());
                }
            });
        }
        return sysOaCustomerList;
    }

    /**
     * 新增客户
     * 
     * @param sysOaCustomer 客户
     * @return 结果
     */
    @Override
    public int insertSysOaCustomer(SysOaCustomer sysOaCustomer)
    {
        sysOaCustomer.setCreateTime(DateUtils.getNowDate());
        sysOaCustomer.setUpdateTime(DateUtils.getNowDate());
        return sysOaCustomerMapper.insertSysOaCustomer(sysOaCustomer);
    }

    /**
     * 修改客户
     * 
     * @param sysOaCustomer 客户
     * @return 结果
     */
    @Override
    public int updateSysOaCustomer(SysOaCustomer sysOaCustomer)
    {
        sysOaCustomer.setUpdateTime(DateUtils.getNowDate());
        return sysOaCustomerMapper.updateSysOaCustomer(sysOaCustomer);
    }

    @Override
    public int updateSysOaCustomerByCustomerNo(SysOaCustomer sysOaCustomer) {
        sysOaCustomer.setUpdateTime(DateUtils.getNowDate());
        return sysOaCustomerMapper.updateSysOaCustomerByCustomerNo(sysOaCustomer);
    }

    /**
     * 删除客户对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysOaCustomerByIds(String ids)
    {
        return sysOaCustomerMapper.deleteSysOaCustomerByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除客户信息
     * 
     * @param ID 客户ID
     * @return 结果
     */
    @Override
    public int deleteSysOaCustomerById(Long ID)
    {
        return sysOaCustomerMapper.deleteSysOaCustomerById(ID);
    }
}
