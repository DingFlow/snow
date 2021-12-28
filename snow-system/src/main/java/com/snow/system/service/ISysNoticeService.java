package com.snow.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.system.domain.SysNotice;

/**
 * 公告 服务层
 * 
 * @author snow
 */
public interface ISysNoticeService extends IService<SysNotice>
{
    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    public SysNotice selectNoticeById(Long noticeId);

    /**
     * 根据公告类型查询最新的公告
     * @param noticeType
     * @return
     */
    public SysNotice selectNewNoticeByNoticeType(String noticeType);

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    public List<SysNotice> selectNoticeList(SysNotice notice);

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    public int insertNotice(SysNotice notice);

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    public int updateNotice(SysNotice notice);

    /**
     * 删除公告信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteNoticeByIds(String ids);
}
