package com.snow.system.service.impl;

import java.util.Date;
import java.util.List;

import com.snow.common.enums.DingTalkListenerType;
import com.snow.system.event.SyncEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import com.snow.common.core.text.Convert;
import com.snow.system.domain.SysNotice;
import com.snow.system.mapper.SysNoticeMapper;
import com.snow.system.service.ISysNoticeService;

import javax.annotation.Resource;

/**
 * 公告 服务层实现
 * 
 * @author snow
 * @date 2018-06-25
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService
{
    @Autowired
    private SysNoticeMapper noticeMapper;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId)
    {
        return noticeMapper.selectNoticeById(noticeId);
    }

    @Override
    public SysNotice selectNewNoticeByNoticeType(String noticeType) {
        return noticeMapper.selectNewNoticeByNoticeType(noticeType);
    }

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice)
    {
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(SysNotice notice)
    {
        notice.setCreateTime(new Date());
        int i = noticeMapper.insertNotice(notice);
        //同步钉钉数据
        SyncEvent syncEvent = new SyncEvent(notice, DingTalkListenerType.BLACKBOARD_CREATE);
        applicationContext.publishEvent(syncEvent);
        return i;
    }

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNotice notice)
    {
        SyncEvent syncEvent = new SyncEvent(notice, DingTalkListenerType.BLACKBOARD_UPDATE);
        applicationContext.publishEvent(syncEvent);
        return noticeMapper.updateNotice(notice);
    }

    /**
     * 删除公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(String ids)
    {
        //删除通知钉钉删除
        return noticeMapper.deleteNoticeByIds(Convert.toStrArray(ids));
    }
}
