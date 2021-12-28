package com.snow.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.common.core.text.Convert;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.system.domain.SysNotice;
import com.snow.system.event.SyncEvent;
import com.snow.system.mapper.SysNoticeMapper;
import com.snow.system.service.ISysNoticeService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告 服务层实现
 * 
 * @author snow
 * @date 2018-06-25
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

    @Resource
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
    public SysNotice selectNoticeById(Long noticeId) {
        return noticeMapper.selectById(noticeId);
    }

    @Override
    public SysNotice selectNewNoticeByNoticeType(String noticeType) {
        LambdaQueryWrapper<SysNotice> lambda = new QueryWrapper<SysNotice>().lambda();
        lambda.eq(SysNotice::getStatus, "0");
        lambda.eq(StrUtil.isNotBlank(noticeType),SysNotice::getNoticeType,noticeType);
        lambda.orderByDesc(SysNotice::getCreateTime);
        lambda.last("limit 1");
        return getOne(lambda);
    }

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice) {
        LambdaQueryWrapper<SysNotice> lambda = new QueryWrapper<SysNotice>().lambda();
        lambda.eq(SysNotice::getStatus, "0");
        lambda.eq(StrUtil.isNotBlank(notice.getNoticeType()),SysNotice::getNoticeType,notice.getNoticeType());
        lambda.like(StrUtil.isNotBlank(notice.getNoticeTitle()),SysNotice::getNoticeTitle,notice.getNoticeTitle());
        lambda.orderByDesc(SysNotice::getCreateTime);
        return noticeMapper.selectList(lambda);
    }

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(SysNotice notice) {
        int save = noticeMapper.insert(notice);
        //同步钉钉数据
        SyncEvent syncEvent = new SyncEvent(notice, DingTalkListenerType.BLACKBOARD_CREATE);
        applicationContext.publishEvent(syncEvent);
        return save;
    }

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNotice notice) {
        return noticeMapper.updateById(notice);
    }

    /**
     * 删除公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(String ids) {
        //删除通知钉钉删除
        return noticeMapper.deleteBatchIds(Convert.toStrList(ids));
    }
}
