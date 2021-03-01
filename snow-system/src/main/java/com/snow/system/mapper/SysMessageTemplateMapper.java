package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.SysMessageTemplate;

/**
 * 消息模板Mapper接口
 * 
 * @author qimingjin
 * @date 2021-02-27
 */
public interface SysMessageTemplateMapper 
{
    /**
     * 查询消息模板
     * 
     * @param id 消息模板ID
     * @return 消息模板
     */
    public SysMessageTemplate selectSysMessageTemplateById(Integer id);

    /**
     * 查询消息模板列表
     * 
     * @param sysMessageTemplate 消息模板
     * @return 消息模板集合
     */
    public List<SysMessageTemplate> selectSysMessageTemplateList(SysMessageTemplate sysMessageTemplate);

    /**
     * 新增消息模板
     * 
     * @param sysMessageTemplate 消息模板
     * @return 结果
     */
    public int insertSysMessageTemplate(SysMessageTemplate sysMessageTemplate);

    /**
     * 修改消息模板
     * 
     * @param sysMessageTemplate 消息模板
     * @return 结果
     */
    public int updateSysMessageTemplate(SysMessageTemplate sysMessageTemplate);

    /**
     * 删除消息模板
     * 
     * @param id 消息模板ID
     * @return 结果
     */
    public int deleteSysMessageTemplateById(Integer id);

    /**
     * 批量删除消息模板
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysMessageTemplateByIds(String[] ids);

    /**
     * 通过code获取
     * @param code
     * @return
     */
    public SysMessageTemplate getSysMessageTemplateByCode(String code);
}
