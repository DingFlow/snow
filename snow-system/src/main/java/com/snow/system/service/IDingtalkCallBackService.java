package com.snow.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.system.domain.DingtalkCallBack;

/**
 * 回调事件Service接口
 * 
 * @author qimingjin
 * @date 2020-11-02
 */
public interface IDingtalkCallBackService extends IService<DingtalkCallBack> {
    /**
     * 查询回调事件
     * 
     * @param id 回调事件ID
     * @return 回调事件
     */
     DingtalkCallBack selectDingtalkCallBackById(Long id);

    /**
     * 查询回调事件列表
     * 
     * @param dingtalkCallBack 回调事件
     * @return 回调事件集合
     */
     List<DingtalkCallBack> selectDingtalkCallBackList(DingtalkCallBack dingtalkCallBack);

    /**
     * 新增回调事件
     * 
     * @param dingtalkCallBack 回调事件
     * @return 结果
     */
     boolean insertDingtalkCallBack(DingtalkCallBack dingtalkCallBack);

    /**
     * 修改回调事件
     * 
     * @param dingtalkCallBack 回调事件
     * @return 结果
     */
     int updateDingtalkCallBack(DingtalkCallBack dingtalkCallBack);

    /**
     * 批量删除回调事件
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     int deleteDingtalkCallBackByIds(String ids);

    /**
     * 删除回调事件信息
     * 
     * @param id 回调事件ID
     * @return 结果
     */
     int deleteDingtalkCallBackById(Long id);
}
