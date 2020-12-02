package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.ActDeModel;

/**
 * 设计器modelService接口
 * 
 * @author qimingjin
 * @date 2020-12-01
 */
public interface IActDeModelService 
{
    /**
     * 查询设计器model
     * 
     * @param id 设计器modelID
     * @return 设计器model
     */
    public ActDeModel selectActDeModelById(String id);


    public ActDeModel selectThumbnailById(String id);

    /**
     * 查询设计器model列表
     * 
     * @param actDeModel 设计器model
     * @return 设计器model集合
     */
    public List<ActDeModel> selectActDeModelList(ActDeModel actDeModel);

    /**
     * 新增设计器model
     * 
     * @param actDeModel 设计器model
     * @return 结果
     */
    public int insertActDeModel(ActDeModel actDeModel);

    /**
     * 修改设计器model
     * 
     * @param actDeModel 设计器model
     * @return 结果
     */
    public int updateActDeModel(ActDeModel actDeModel);

    /**
     * 批量删除设计器model
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteActDeModelByIds(String ids);

    /**
     * 删除设计器model信息
     * 
     * @param id 设计器modelID
     * @return 结果
     */
    public int deleteActDeModelById(String id);
}
