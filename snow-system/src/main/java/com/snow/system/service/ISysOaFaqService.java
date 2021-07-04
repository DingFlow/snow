package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysOaFaq;

/**
 * faq问题Service接口
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
public interface ISysOaFaqService 
{
    /**
     * 查询faq问题
     * 
     * @param faqNo faq问题ID
     * @return faq问题
     */
    public SysOaFaq selectSysOaFaqById(String faqNo);

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
     * 删除faq问题信息
     * 
     * @param faqNo faq问题ID
     * @return 结果
     */
    public int deleteSysOaFaqById(String faqNo);


    /**
     * 删除批量
     * @param ids
     * @return
     */
    public int deleteSysOaFaqByFaqNos(String ids);
}
