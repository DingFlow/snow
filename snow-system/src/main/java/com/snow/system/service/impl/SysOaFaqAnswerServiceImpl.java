package com.snow.system.service.impl;

import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.snow.common.utils.DateUtils;
import com.snow.system.mapper.SysOaFaqMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysOaFaqAnswerMapper;
import com.snow.system.domain.SysOaFaqAnswer;
import com.snow.system.service.ISysOaFaqAnswerService;
import com.snow.common.core.text.Convert;

/**
 * faq问题答案Service业务层处理
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
@Service
public class SysOaFaqAnswerServiceImpl implements ISysOaFaqAnswerService {
    @Autowired
    private SysOaFaqAnswerMapper sysOaFaqAnswerMapper;

    @Autowired
    private SysOaFaqMapper sysOaFaqMapper;

    @Override
    public SysOaFaqAnswer selectSysOaFaqAnswerById(Integer id) {
        SysOaFaqAnswer sysOaFaqAnswer=sysOaFaqAnswerMapper.selectSysOaFaqAnswerById(id);
        sysOaFaqAnswer.setSysOaFaq(sysOaFaqMapper.selectSysOaFaqByFaqNo(sysOaFaqAnswer.getFaqNo()));
        return sysOaFaqAnswer;
    }

    /**
     * 查询faq问题答案
     * 
     * @param faqNo faq问题答案ID
     * @return faq问题答案
     */
    @Override
    public List<SysOaFaqAnswer> selectSysOaFaqAnswerByFaqNo(String faqNo)
    {
        return sysOaFaqAnswerMapper.selectSysOaFaqAnswerByFaqNo(faqNo);
    }

    /**
     * 查询faq问题答案列表
     * 
     * @param sysOaFaqAnswer faq问题答案
     * @return faq问题答案
     */
    @Override
    public List<SysOaFaqAnswer> selectSysOaFaqAnswerList(SysOaFaqAnswer sysOaFaqAnswer)
    {
        List<SysOaFaqAnswer> sysOaFaqAnswerList= sysOaFaqAnswerMapper.selectSysOaFaqAnswerList(sysOaFaqAnswer);
        if(CollectionUtil.isNotEmpty(sysOaFaqAnswerList)){
            sysOaFaqAnswerList.forEach(t->{
                t.setSysOaFaq(sysOaFaqMapper.selectSysOaFaqByFaqNo(t.getFaqNo()));
            });
        }
        return sysOaFaqAnswerList;
    }

    /**
     * 新增faq问题答案
     * 
     * @param sysOaFaqAnswer faq问题答案
     * @return 结果
     */
    @Override
    public int insertSysOaFaqAnswer(SysOaFaqAnswer sysOaFaqAnswer)
    {
        sysOaFaqAnswer.setCreateTime(DateUtils.getNowDate());
        return sysOaFaqAnswerMapper.insertSysOaFaqAnswer(sysOaFaqAnswer);
    }

    /**
     * 修改faq问题答案
     * 
     * @param sysOaFaqAnswer faq问题答案
     * @return 结果
     */
    @Override
    public int updateSysOaFaqAnswer(SysOaFaqAnswer sysOaFaqAnswer)
    {
        sysOaFaqAnswer.setUpdateTime(DateUtils.getNowDate());
        return sysOaFaqAnswerMapper.updateSysOaFaqAnswer(sysOaFaqAnswer);
    }



    /**
     * 删除faq问题答案信息
     * 
     * @param faqNo faq问题答案ID
     * @return 结果
     */
    @Override
    public int deleteSysOaFaqAnswerByFaqNo(String faqNo)
    {
        return sysOaFaqAnswerMapper.deleteSysOaFaqAnswerByFaqNo(faqNo);
    }

    @Override
    public int deleteSysOaFaqAnswerByFaqId(Integer id) {
        return sysOaFaqAnswerMapper.deleteSysOaFaqAnswerById(id);
    }

}
