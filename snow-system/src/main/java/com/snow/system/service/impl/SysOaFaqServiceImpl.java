package com.snow.system.service.impl;

import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.snow.common.utils.DateUtils;
import com.snow.system.domain.SysOaFaqAnswer;
import com.snow.system.domain.SysOaFaqLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysOaFaqMapper;
import com.snow.system.domain.SysOaFaq;
import com.snow.system.service.ISysOaFaqService;
import com.snow.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * faq问题Service业务层处理
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
@Service
public class SysOaFaqServiceImpl implements ISysOaFaqService 
{
    @Autowired
    private SysOaFaqMapper sysOaFaqMapper;

    @Autowired
    private SysOaFaqAnswerServiceImpl sysOaFaqAnswerService;

    @Autowired
    private SysOaFaqLabelServiceImpl sysOaFaqLabelService;

    /**
     * 查询faq问题
     * 
     * @param faqNo faq问题ID
     * @return faq问题
     */
    @Override
    public SysOaFaq selectSysOaFaqById(String faqNo)
    {
        return sysOaFaqMapper.selectSysOaFaqByFaqNo(faqNo);
    }

    /**
     * 查询faq问题列表
     * 
     * @param sysOaFaq faq问题
     * @return faq问题
     */
    @Override
    public List<SysOaFaq> selectSysOaFaqList(SysOaFaq sysOaFaq)
    {
        List<SysOaFaq> sysOaFaqs = sysOaFaqMapper.selectSysOaFaqList(sysOaFaq);
        if(CollectionUtil.isNotEmpty(sysOaFaqs)){
            sysOaFaqs.forEach(t->{
                List<SysOaFaqAnswer> sysOaFaqAnswerList= sysOaFaqAnswerService.selectSysOaFaqAnswerByFaqNo(t.getFaqNo());
                t.setSysOaFaqAnswerList(sysOaFaqAnswerList);
                List<SysOaFaqLabel> sysOaFaqLabels = sysOaFaqLabelService.selectSysOaFaqLabelByFaqNo(t.getFaqNo());
                t.setSysOaFaqLabelList(sysOaFaqLabels);
            });
        }
        return sysOaFaqs;
    }

    /**
     * 新增faq问题
     * 
     * @param sysOaFaq faq问题
     * @return 结果
     */
    @Override
    public int insertSysOaFaq(SysOaFaq sysOaFaq)
    {
        sysOaFaq.setCreateTime(DateUtils.getNowDate());
        return sysOaFaqMapper.insertSysOaFaq(sysOaFaq);
    }

    /**
     * 修改faq问题
     * 
     * @param sysOaFaq faq问题
     * @return 结果
     */
    @Override
    public int updateSysOaFaq(SysOaFaq sysOaFaq)
    {
        sysOaFaq.setUpdateTime(DateUtils.getNowDate());
        return sysOaFaqMapper.updateSysOaFaq(sysOaFaq);
    }



    /**
     * 删除faq问题信息
     * 
     * @param faqNo faq问题ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSysOaFaqById(String faqNo)
    {
        sysOaFaqAnswerService.deleteSysOaFaqAnswerByFaqNo(faqNo);
        return sysOaFaqMapper.deleteSysOaFaqByFaqNo(faqNo);
    }

    /**
     * 删除faq问题对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSysOaFaqByFaqNos(String ids)
    {
        String[] faqNos= Convert.toStrArray(ids);
        for (String faqNo : faqNos) {
            sysOaFaqAnswerService.deleteSysOaFaqAnswerByFaqNo(faqNo);
        }
        return sysOaFaqMapper.deleteSysOaFaqByFaqNos(Convert.toStrArray(ids));
    }
}
