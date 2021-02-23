package com.snow.system.service.impl;

import java.util.List;

import cn.hutool.core.io.IoUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.ActDeModelMapper;
import com.snow.system.domain.ActDeModel;
import com.snow.system.service.IActDeModelService;
import com.snow.common.core.text.Convert;

/**
 * 设计器modelService业务层处理
 * 
 * @author qimingjin
 * @date 2020-12-01
 */
@Service
public class ActDeModelServiceImpl implements IActDeModelService 
{
    @Autowired
    private ActDeModelMapper actDeModelMapper;



    /**
     * 查询设计器model
     * 
     * @param id 设计器modelID
     * @return 设计器model
     */
    @Override
    public ActDeModel selectActDeModelById(String id)
    {
        return actDeModelMapper.selectActDeModelById(id);
    }

    @Override
    public ActDeModel selectThumbnailById(String id) {
        ActDeModel actDeModel = actDeModelMapper.selectActDeModelById(id);
        byte[] thumbnail = actDeModel.getThumbnail();
        //createBytesDeployment
      //  IOUtils.
        return null;
    }

    /**
     * 查询设计器model列表
     * 
     * @param actDeModel 设计器model
     * @return 设计器model
     */
    @Override
    public List<ActDeModel> selectActDeModelList(ActDeModel actDeModel)
    {
        return actDeModelMapper.selectActDeModelList(actDeModel);
    }

    /**
     * 新增设计器model
     * 
     * @param actDeModel 设计器model
     * @return 结果
     */
    @Override
    public int insertActDeModel(ActDeModel actDeModel)
    {
        return actDeModelMapper.insertActDeModel(actDeModel);
    }

    /**
     * 修改设计器model
     * 
     * @param actDeModel 设计器model
     * @return 结果
     */
    @Override
    public int updateActDeModel(ActDeModel actDeModel)
    {
        return actDeModelMapper.updateActDeModel(actDeModel);
    }

    /**
     * 删除设计器model对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActDeModelByIds(String ids)
    {
        return actDeModelMapper.deleteActDeModelByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除设计器model信息
     * 
     * @param id 设计器modelID
     * @return 结果
     */
    @Override
    public int deleteActDeModelById(String id)
    {
        return actDeModelMapper.deleteActDeModelById(id);
    }
}
