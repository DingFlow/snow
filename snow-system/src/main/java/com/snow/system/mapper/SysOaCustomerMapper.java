package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.SysOaCustomer;

/**
 * 客户Mapper接口
 * 
 * @author 没用的阿吉
 * @date 2021-04-09
 */
public interface SysOaCustomerMapper 
{
    /**
     * 查询客户
     * 
     * @param id 客户ID
     * @return 客户
     */
    public SysOaCustomer selectSysOaCustomerById(Long id);


    public SysOaCustomer selectSysOaCustomerByCustomerNo(String customerNo);


    public SysOaCustomer selectSysOaCustomerByCustomerName(String customerName);
    /**
     * 查询客户列表
     * 
     * @param sysOaCustomer 客户
     * @return 客户集合
     */
    public List<SysOaCustomer> selectSysOaCustomerList(SysOaCustomer sysOaCustomer);

    /**
     * 新增客户
     * 
     * @param sysOaCustomer 客户
     * @return 结果
     */
    public int insertSysOaCustomer(SysOaCustomer sysOaCustomer);

    /**
     * 修改客户
     * 
     * @param sysOaCustomer 客户
     * @return 结果
     */
    public int updateSysOaCustomer(SysOaCustomer sysOaCustomer);

    public int updateSysOaCustomerByCustomerNo(SysOaCustomer sysOaCustomer);

    /**
     * 删除客户
     * 
     * @param ID 客户ID
     * @return 结果
     */
    public int deleteSysOaCustomerById(Long ID);

    /**
     * 批量删除客户
     * 
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOaCustomerByIds(String[] IDs);
}
