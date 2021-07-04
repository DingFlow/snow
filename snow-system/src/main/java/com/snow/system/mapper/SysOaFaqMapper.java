package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.SysOaFaq;

/**
 * faq问题Mapper接口
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
public interface SysOaFaqMapper 
{
    /**
     * 查询faq问题
     * 
     * @param faqNo faq问题ID
     * @return faq问题
     */
    public SysOaFaq selectSysOaFaqByFaqNo(String faqNo);

    /**
     * 查询faq问题列表
     * 
     * @param sysOaFaq faq问题
     * @return faq问题集合
     */
    public List<SysOaFaq> selectSysOaFaqList(SysOaFaq sysOaFaq);

    /**
     * 新增faq问题
     * 
     * @param sysOaFaq faq问题
     * @return 结果
     */
    public int insertSysOaFaq(SysOaFaq sysOaFaq);

    /**
     * 修改faq问题
     * 
     * @param sysOaFaq faq问题
     * @return 结果
     */
    public int updateSysOaFaq(SysOaFaq sysOaFaq);

    /**
     * 删除faq问题
     * 
     * @param faqNo faq问题ID
     * @return 结果
     */
    public int deleteSysOaFaqByFaqNo(String faqNo);

    /**
     * 批量删除faq问题
     *
     * @param faqNos 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOaFaqByFaqNos(String[] faqNos);

}
