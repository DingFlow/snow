package com.snow.system.service.impl;

import java.util.List;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysMessageTemplateMapper;
import com.snow.system.domain.SysMessageTemplate;
import com.snow.system.service.ISysMessageTemplateService;
import com.snow.common.core.text.Convert;

/**
 * 消息模板Service业务层处理
 * 
 * @author qimingjin
 * @date 2021-02-27
 */
@Service
public class SysMessageTemplateServiceImpl implements ISysMessageTemplateService 
{
    @Autowired
    private SysMessageTemplateMapper sysMessageTemplateMapper;

    /**
     * 查询消息模板
     * 
     * @param id 消息模板ID
     * @return 消息模板
     */
    @Override
    public SysMessageTemplate selectSysMessageTemplateById(Integer id)
    {
        return sysMessageTemplateMapper.selectSysMessageTemplateById(id);
    }

    /**
     * 查询消息模板列表
     * 
     * @param sysMessageTemplate 消息模板
     * @return 消息模板
     */
    @Override
    public List<SysMessageTemplate> selectSysMessageTemplateList(SysMessageTemplate sysMessageTemplate)
    {
        return sysMessageTemplateMapper.selectSysMessageTemplateList(sysMessageTemplate);
    }

    /**
     * 新增消息模板
     * 
     * @param sysMessageTemplate 消息模板
     * @return 结果
     */
    @Override
    public int insertSysMessageTemplate(SysMessageTemplate sysMessageTemplate)
    {
        sysMessageTemplate.setCreateTime(DateUtils.getNowDate());
        return sysMessageTemplateMapper.insertSysMessageTemplate(sysMessageTemplate);
    }

    /**
     * 修改消息模板
     * 
     * @param sysMessageTemplate 消息模板
     * @return 结果
     */
    @Override
    public int updateSysMessageTemplate(SysMessageTemplate sysMessageTemplate)
    {
        sysMessageTemplate.setUpdateTime(DateUtils.getNowDate());
        return sysMessageTemplateMapper.updateSysMessageTemplate(sysMessageTemplate);
    }

    /**
     * 删除消息模板对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysMessageTemplateByIds(String ids)
    {
        return sysMessageTemplateMapper.deleteSysMessageTemplateByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除消息模板信息
     * 
     * @param id 消息模板ID
     * @return 结果
     */
    @Override
    public int deleteSysMessageTemplateById(Integer id)
    {
        return sysMessageTemplateMapper.deleteSysMessageTemplateById(id);
    }

    /**
     * 根据code获取模板
     * @param code
     * @return
     */
    @Override
    public SysMessageTemplate getSysMessageTemplateByCode(String code) {
        return sysMessageTemplateMapper.getSysMessageTemplateByCode(code);
    }
}
