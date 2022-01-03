package com.snow.from.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.common.core.text.Convert;
import com.snow.common.utils.DateUtils;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.mapper.SysFormInstanceMapper;
import com.snow.from.service.ISysFormInstanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 单实例Service业务层处理
 * 
 * @author 没用的阿吉
 * @date 2021-03-21
 */
@Service
public class SysFormInstanceServiceImpl extends ServiceImpl<SysFormInstanceMapper,SysFormInstance> implements ISysFormInstanceService {

    @Resource
    private SysFormInstanceMapper sysFormInstanceMapper;

    /**
     * 查询表单实例
     * 
     * @param id 表单实例ID
     * @return 表单实例
     */
    @Override
    public SysFormInstance selectSysFormInstanceById(Long id) {
        return sysFormInstanceMapper.selectById(id);
    }

    @Override
    public SysFormInstance selectSysFormInstanceByFormCode(String fromCode) {
        LambdaQueryWrapper<SysFormInstance> lambda = new QueryWrapper<SysFormInstance>().lambda();
        return sysFormInstanceMapper.selectOne(lambda.eq(SysFormInstance::getFormCode,fromCode));
    }

    @Override
    public SysFormInstance selectSysFormInstanceByFormName(String fromName) {
        LambdaQueryWrapper<SysFormInstance> lambda = new QueryWrapper<SysFormInstance>().lambda();
        return sysFormInstanceMapper.selectOne(lambda.eq(SysFormInstance::getFormCode,fromName));
    }

    /**
     * 查询表单实例列表
     * 
     * @param sysFormInstance 表单实例
     * @return 表单实例
     */
    @Override
    public List<SysFormInstance> selectSysFormInstanceList(SysFormInstance sysFormInstance) {
        LambdaQueryWrapper<SysFormInstance> lambda = new QueryWrapper<SysFormInstance>().lambda();
        lambda.like(StrUtil.isNotBlank(sysFormInstance.getFormCode()),SysFormInstance::getFormCode,sysFormInstance.getFormCode());
        lambda.like(StrUtil.isNotBlank(sysFormInstance.getFormName()),SysFormInstance::getFormName,sysFormInstance.getFormName());
        lambda.orderByDesc(SysFormInstance::getCreateTime);
        return sysFormInstanceMapper.selectList(lambda);
    }

    /**
     * 新增表单实例
     * 
     * @param sysFormInstance 表单实例
     * @return 结果
     */
    @Override
    public int insertSysFormInstance(SysFormInstance sysFormInstance) {
        sysFormInstance.setCreateTime(DateUtils.getNowDate());
        return sysFormInstanceMapper.insert(sysFormInstance);
    }

    /**
     * 修改表单实例
     * 
     * @param sysFormInstance 表单实例
     * @return 结果
     */
    @Override
    public int updateSysFormInstance(SysFormInstance sysFormInstance) {
        sysFormInstance.setUpdateTime(DateUtils.getNowDate());
        return sysFormInstanceMapper.updateById(sysFormInstance);
    }

    /**
     * 删除表单实例对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysFormInstanceByIds(String ids) {
        return sysFormInstanceMapper.deleteBatchIds(Convert.toStrList(ids));
    }

    /**
     * 删除表单实例信息
     * 
     * @param id 表单实例ID
     * @return 结果
     */
    @Override
    public int deleteSysFormInstanceById(Long id) {
        return sysFormInstanceMapper.deleteById(id);
    }
}
