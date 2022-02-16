package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.SysFnAccountBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 账户流水详情Mapper接口
 * 
 * @author Agee
 * @date 2022-02-16
 */
public interface SysFnAccountBillMapper extends BaseMapper<SysFnAccountBill>
{
    /**
     * 查询账户流水详情
     * 
     * @param id 账户流水详情ID
     * @return 账户流水详情
     */
    public SysFnAccountBill selectSysFnAccountBillById(Long id);

    /**
     * 查询账户流水详情列表
     * 
     * @param sysFnAccountBill 账户流水详情
     * @return 账户流水详情集合
     */
    public List<SysFnAccountBill> selectSysFnAccountBillList(SysFnAccountBill sysFnAccountBill);

    /**
     * 新增账户流水详情
     * 
     * @param sysFnAccountBill 账户流水详情
     * @return 结果
     */
    public int insertSysFnAccountBill(SysFnAccountBill sysFnAccountBill);

    /**
     * 修改账户流水详情
     * 
     * @param sysFnAccountBill 账户流水详情
     * @return 结果
     */
    public int updateSysFnAccountBill(SysFnAccountBill sysFnAccountBill);

    /**
     * 删除账户流水详情
     * 
     * @param id 账户流水详情ID
     * @return 结果
     */
    public int deleteSysFnAccountBillById(Long id);

    /**
     * 批量删除账户流水详情
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFnAccountBillByIds(String[] ids);
}
