package com.snow.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.common.constant.Constants;
import com.snow.common.core.text.Convert;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.utils.DateUtils;
import com.snow.common.utils.StringUtils;
import com.snow.system.domain.DingtalkCallBack;
import com.snow.system.domain.DingtalkCallBackEvent;
import com.snow.system.event.SyncEvent;
import com.snow.system.mapper.DingtalkCallBackEventMapper;
import com.snow.system.mapper.DingtalkCallBackMapper;
import com.snow.system.service.IDingtalkCallBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 回调事件Service业务层处理
 * 
 * @author qimingjin
 * @date 2020-11-02
 */
@Service
public class DingtalkCallBackServiceImpl extends ServiceImpl<DingtalkCallBackMapper,DingtalkCallBack> implements IDingtalkCallBackService {

    @Resource
    private DingtalkCallBackMapper dingtalkCallBackMapper;

    @Resource
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
    public DingtalkCallBack selectDingtalkCallBackById(Long id) {
        DingtalkCallBack dingtalkCallBack = dingtalkCallBackMapper.selectDingtalkCallBackById(id);
        if(StringUtils.isNotNull(dingtalkCallBack)){
            DingtalkCallBackEvent dingtalkCallBackEvent=new DingtalkCallBackEvent();
            dingtalkCallBackEvent.setCallBackId(id);
            List<DingtalkCallBackEvent> dingtalkCallBackEvents = dingtalkCallBackEventMapper.selectDingtalkCallBackEventList(dingtalkCallBackEvent);
            List<String> eventNameList = dingtalkCallBackEvents.stream()
                    .map(DingtalkCallBackEvent::getEventName)
                    .collect(Collectors.toList());
            dingtalkCallBack.setEventNameList(eventNameList);
        }
        return dingtalkCallBack;
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
    @Transactional(rollbackFor = Exception.class)
    public boolean insertDingtalkCallBack(DingtalkCallBack dingtalkCallBack) {
        dingtalkCallBack.setCreateTime(DateUtils.getNowDate());
        //保存主表
        boolean save = this.save(dingtalkCallBack);
        List<String> eventNameList = dingtalkCallBack.getEventNameList();
        if(CollUtil.isEmpty(eventNameList)){
            return save;
        }
        eventNameList.forEach(t->{
            DingtalkCallBackEvent dingtalkCallBackEvent=new DingtalkCallBackEvent();
            dingtalkCallBackEvent.setEventName(t);
            String addressBook = sysDictDataServiceImpl.selectDictLabel(Constants.ADDRESS_BOOK, t);
            dingtalkCallBackEvent.setEventDesc(addressBook);
            dingtalkCallBackEvent.setCallBackId(dingtalkCallBack.getId());
            dingtalkCallBackEventMapper.insert(dingtalkCallBackEvent);
        });
        return save;
    }

    /**
     * 修改回调事件
     * 
     * @param dingtalkCallBack 回调事件
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDingtalkCallBack(DingtalkCallBack dingtalkCallBack) {
        dingtalkCallBack.setUpdateTime(DateUtils.getNowDate());
        List<String> eventNameList = dingtalkCallBack.getEventNameList();
        int i= dingtalkCallBackEventMapper.deleteDingtalkCallBackEventByCallBanckId(dingtalkCallBack.getId().intValue());
        if(CollUtil.isEmpty(eventNameList)){
            return i;
        }
        eventNameList.forEach(t->{
            DingtalkCallBackEvent dingtalkCallBackEvent=new DingtalkCallBackEvent();
            dingtalkCallBackEvent.setEventName(t);
            String addressBook = sysDictDataServiceImpl.selectDictLabel(Constants.ADDRESS_BOOK, t);
            dingtalkCallBackEvent.setEventDesc(addressBook);
            dingtalkCallBackEvent.setCallBackId(dingtalkCallBack.getId());
            dingtalkCallBackEvent.setCreateBy(dingtalkCallBack.getUpdateBy());
            dingtalkCallBackEventMapper.insert(dingtalkCallBackEvent);
        });
        dingtalkCallBackMapper.updateById(dingtalkCallBack);
        // 同步到dingding
        SyncEvent<DingtalkCallBack> syncEvent = new SyncEvent(dingtalkCallBack, DingTalkListenerType.CALL_BACK_UPDATE);
        applicationContext.publishEvent(syncEvent);
        return i;
    }

    /**
     * 删除回调事件对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDingtalkCallBackByIds(String ids) {
        List<String> idList = Arrays.asList(Convert.toStrArray(ids));
        idList.forEach(t->{
            dingtalkCallBackEventMapper.deleteDingtalkCallBackEventByCallBanckId(Integer.parseInt(t));
        });
        int i = dingtalkCallBackMapper.deleteDingtalkCallBackByIds(Convert.toStrArray(ids));
        // 同步到dingding
        SyncEvent syncEvent = new SyncEvent(idList, DingTalkListenerType.CALL_BACK_DELETE);
        applicationContext.publishEvent(syncEvent);
        return i;
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
