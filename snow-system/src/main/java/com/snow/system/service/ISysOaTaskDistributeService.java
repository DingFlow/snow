package com.snow.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.system.domain.SysOaTaskDistribute;

/**
 * 任务分配Service接口
 * 
 * @author 没用的阿吉
 * @date 2021-08-15
 */
public interface ISysOaTaskDistributeService extends IService<SysOaTaskDistribute>
{
    /**
     * 查询任务分配
     * 
     * @param id 任务分配ID
     * @return 任务分配
     */
     SysOaTaskDistribute selectSysOaTaskDistributeById(Long id);

    /**
     * 查询任务分配列表
     * 
     * @param sysOaTaskDistribute 任务分配
     * @return 任务分配集合
     */
     List<SysOaTaskDistribute> selectSysOaTaskDistributeList(SysOaTaskDistribute sysOaTaskDistribute);


    /**
     * 获取我处理的和我待处理任务
     * @param sysOaTaskDistribute 任务分配
     * @return 结果
     */
     List<SysOaTaskDistribute> getSysOaTaskDistribute(SysOaTaskDistribute sysOaTaskDistribute);

    void  warpSysOaTask(List<SysOaTaskDistribute> sysOaTaskDistributes);

}
