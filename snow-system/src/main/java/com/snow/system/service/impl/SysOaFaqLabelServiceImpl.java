package com.snow.system.service.impl;

import java.util.List;
import com.snow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysOaFaqLabelMapper;
import com.snow.system.domain.SysOaFaqLabel;
import com.snow.system.service.ISysOaFaqLabelService;
import com.snow.common.core.text.Convert;

/**
 * faq问题标签Service业务层处理
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
@Service
public class SysOaFaqLabelServiceImpl implements ISysOaFaqLabelService 
{
    @Autowired
    private SysOaFaqLabelMapper sysOaFaqLabelMapper;

    @Override
    public SysOaFaqLabel selectSysOaFaqLabelById(Integer id) {
        return sysOaFaqLabelMapper.selectSysOaFaqLabelById(id);
    }

    /**
     * 查询faq问题标签
     * 
     * @param faqNo faq问题标签ID
     * @return faq问题标签
     */
    @Override
    public List<SysOaFaqLabel> selectSysOaFaqLabelByFaqNo(String faqNo)
    {
        return sysOaFaqLabelMapper.selectSysOaFaqLabelByFaqNo(faqNo);
    }

    /**
     * 查询faq问题标签列表
     * 
     * @param sysOaFaqLabel faq问题标签
     * @return faq问题标签
     */
    @Override
    public List<SysOaFaqLabel> selectSysOaFaqLabelList(SysOaFaqLabel sysOaFaqLabel)
    {
        return sysOaFaqLabelMapper.selectSysOaFaqLabelList(sysOaFaqLabel);
    }

    /**
     * 新增faq问题标签
     * 
     * @param sysOaFaqLabel faq问题标签
     * @return 结果
     */
    @Override
    public int insertSysOaFaqLabel(SysOaFaqLabel sysOaFaqLabel)
    {
        sysOaFaqLabel.setCreateTime(DateUtils.getNowDate());
        return sysOaFaqLabelMapper.insertSysOaFaqLabel(sysOaFaqLabel);
    }

    /**
     * 修改faq问题标签
     * 
     * @param sysOaFaqLabel faq问题标签
     * @return 结果
     */
    @Override
    public int updateSysOaFaqLabel(SysOaFaqLabel sysOaFaqLabel)
    {
        sysOaFaqLabel.setUpdateTime(DateUtils.getNowDate());
        return sysOaFaqLabelMapper.updateSysOaFaqLabel(sysOaFaqLabel);
    }



    /**
     * 删除faq问题标签信息
     * 
     * @param faqNo faq问题标签ID
     * @return 结果
     */
    @Override
    public int deleteSysOaFaqLabelById(String faqNo)
    {
        return sysOaFaqLabelMapper.deleteSysOaFaqLabelByFaqNo(faqNo);
    }
}
