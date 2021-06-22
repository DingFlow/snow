package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.SysOaFaqLabel;

/**
 * faq问题标签Mapper接口
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
public interface SysOaFaqLabelMapper 
{
    public SysOaFaqLabel selectSysOaFaqLabelById(Integer id);
    /**
     * 查询faq问题标签
     * 
     * @param faqNo faq问题标签ID
     * @return faq问题标签
     */
    public List<SysOaFaqLabel> selectSysOaFaqLabelByFaqNo(String faqNo);

    /**
     * 查询faq问题标签列表
     * 
     * @param sysOaFaqLabel faq问题标签
     * @return faq问题标签集合
     */
    public List<SysOaFaqLabel> selectSysOaFaqLabelList(SysOaFaqLabel sysOaFaqLabel);

    /**
     * 新增faq问题标签
     * 
     * @param sysOaFaqLabel faq问题标签
     * @return 结果
     */
    public int insertSysOaFaqLabel(SysOaFaqLabel sysOaFaqLabel);

    /**
     * 修改faq问题标签
     * 
     * @param sysOaFaqLabel faq问题标签
     * @return 结果
     */
    public int updateSysOaFaqLabel(SysOaFaqLabel sysOaFaqLabel);

    /**
     * 删除faq问题标签
     * 
     * @param faqNo faq问题标签ID
     * @return 结果
     */
    public int deleteSysOaFaqLabelByFaqNo(String faqNo);

}
