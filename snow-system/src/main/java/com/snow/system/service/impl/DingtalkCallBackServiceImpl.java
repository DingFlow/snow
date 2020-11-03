package com.snow.system.service.impl;

import java.util.List;

import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.utils.DateUtils;
import com.snow.system.domain.DingtalkCallBackEvent;
import com.snow.system.event.SyncEvent;
import com.snow.system.mapper.DingtalkCallBackEventMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.DingtalkCallBackMapper;
import com.snow.system.domain.DingtalkCallBack;
import com.snow.system.service.IDingtalkCallBackService;
import com.snow.common.core.text.Convert;

import javax.annotation.Resource;

/**
 * 回调事件Service业务层处理
 * 
 * @author qimingjin
 * @date 2020-11-02
 */
@Service
public class DingtalkCallBackServiceImpl implements IDingtalkCallBackService 
{
    @Autowired
    private DingtalkCallBackMapper dingtalkCallBackMapper;
    @Autowired
    private DingtalkCallBackEventMapper dingtalkCallBackEventMapper;
    @Autowired
    private SysDictDataServiceImpl sysDictDataServiceImpl;
    @Resource
    private ApplicationContext applicationContext;
    /**
     * 查询回调事件
     * 
     * @param id 回调事件ID
     * @return 回调事件
     */
    @Override
    public DingtalkCallBack selectDingtalkCallBackById(Long id)
    {
        return dingtalkCallBackMapper.selectDingtalkCallBackById(id);
    }

    /**
     * 查询回调事件列表
     * 
     * @param dingtalkCallBack 回调事件
     * @return 回调事件
     */
    @Override
    public List<DingtalkCallBack> selectDingtalkCallBackList(DingtalkCallBack dingtalkCallBack)
    {
        return dingtalkCallBackMapper.selectDingtalkCallBackList(dingtalkCallBack);
    }

    /**
     * 新增回调事件
     * 
     * @param dingtalkCallBack 回调事件
     * @return 结果
     */
    @Override
    public int insertDingtalkCallBack(DingtalkCallBack dingtalkCallBack)
    {
        dingtalkCallBack.setCreateTime(DateUtils.getNowDate());
        DingtalkCallBackEvent dingtalkCallBackEvent=new DingtalkCallBackEvent();
        BeanUtils.copyProperties(dingtalkCallBack,dingtalkCallBackEvent);
        List<String> eventNameList = dingtalkCallBack.getEventNameList();
        int i = dingtalkCallBackMapper.insertDingtalkCallBack(dingtalkCallBack);
        eventNameList.forEach(t->{
            dingtalkCallBackEvent.setEventName(t);
            String addressBook = sysDictDataServiceImpl.selectDictLabel("address_book", t);
            dingtalkCallBackEvent.setEventDesc(addressBook);
            dingtalkCallBackEvent.setId(i);
            dingtalkCallBackEventMapper.insertDingtalkCallBackEvent(dingtalkCallBackEvent);
        });
        // 同步到dingding
        SyncEvent syncEvent = new SyncEvent(dingtalkCallBack, DingTalkListenerType.CALL_BACK_REGISTER.getCode(), dingtalkCallBack);
        applicationContext.publishEvent(syncEvent);
        return 1;
    }

    /**
     * 修改回调事件
     * 
     * @param dingtalkCallBack 回调事件
     * @return 结果
     */
    @Override
    public int updateDingtalkCallBack(DingtalkCallBack dingtalkCallBack)
    {
        dingtalkCallBack.setUpdateTime(DateUtils.getNowDate());
        return dingtalkCallBackMapper.updateDingtalkCallBack(dingtalkCallBack);
    }

    /**
     * 删除回调事件对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDingtalkCallBackByIds(String ids)
    {
        return dingtalkCallBackMapper.deleteDingtalkCallBackByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除回调事件信息
     * 
     * @param id 回调事件ID
     * @return 结果
     */
    @Override
    public int deleteDingtalkCallBackById(Long id)
    {
        return dingtalkCallBackMapper.deleteDingtalkCallBackById(id);
    }
}
