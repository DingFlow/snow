package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.DingtalkCallBackEvent;

/**
 * 事件表Mapper接口
 * 
 * @author snow
 * @date 2020-11-03
 */
public interface DingtalkCallBackEventMapper 
{
    /**
     * 查询事件表
     * 
     * @param id 事件表ID
     * @return 事件表
     */
    public DingtalkCallBackEvent selectDingtalkCallBackEventById(Integer id);

    /**
     * 查询事件表列表
     * 
     * @param dingtalkCallBackEvent 事件表
     * @return 事件表集合
     */
    public List<DingtalkCallBackEvent> selectDingtalkCallBackEventList(DingtalkCallBackEvent dingtalkCallBackEvent);

    /**
     * 新增事件表
     * 
     * @param dingtalkCallBackEvent 事件表
     * @return 结果
     */
    public int insertDingtalkCallBackEvent(DingtalkCallBackEvent dingtalkCallBackEvent);

    /**
     * 修改事件表
     * 
     * @param dingtalkCallBackEvent 事件表
     * @return 结果
     */
    public int updateDingtalkCallBackEvent(DingtalkCallBackEvent dingtalkCallBackEvent);

    /**
     * 删除事件表
     * 
     * @param id 事件表ID
     * @return 结果
     */
    public int deleteDingtalkCallBackEventById(Integer id);

    /**
     *
     * @param id
     * @return
     */
    public int deleteDingtalkCallBackEventByCallBanckId(Integer id);

    /**
     * 批量删除事件表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDingtalkCallBackEventByIds(String[] ids);
}
