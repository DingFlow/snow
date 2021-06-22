package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysOaFaqAnswer;

/**
 * faq问题答案Service接口
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
public interface ISysOaFaqAnswerService 
{
    public SysOaFaqAnswer selectSysOaFaqAnswerById(Integer id);
    /**
     * 查询faq问题答案
     * 
     * @param faqNo faq问题答案ID
     * @return faq问题答案
     */
    public List<SysOaFaqAnswer> selectSysOaFaqAnswerByFaqNo(String faqNo);

    /**
     * 查询faq问题答案列表
     * 
     * @param sysOaFaqAnswer faq问题答案
     * @return faq问题答案集合
     */
    public List<SysOaFaqAnswer> selectSysOaFaqAnswerList(SysOaFaqAnswer sysOaFaqAnswer);

    /**
     * 新增faq问题答案
     * 
     * @param sysOaFaqAnswer faq问题答案
     * @return 结果
     */
    public int insertSysOaFaqAnswer(SysOaFaqAnswer sysOaFaqAnswer);

    /**
     * 修改faq问题答案
     * 
     * @param sysOaFaqAnswer faq问题答案
     * @return 结果
     */
    public int updateSysOaFaqAnswer(SysOaFaqAnswer sysOaFaqAnswer);



    /**
     * 删除faq问题答案信息
     *
     * @param faqNo faq问题答案faqNo
     * @return 结果
     */
    public int deleteSysOaFaqAnswerByFaqNo(String faqNo);

    /**
     * 删除faq问题答案信息
     *
     * @param id faq问题答案ID
     * @return 结果
     */
    public int deleteSysOaFaqAnswerByFaqId(Integer id);

}
