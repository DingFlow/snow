package com.snow.from.controller;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.snow.common.annotation.Log;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.framework.util.ShiroUtils;
import com.snow.from.domain.FieldContentDTO;
import com.snow.from.domain.FromInfoDTO;
import com.snow.from.domain.SysFormField;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.service.ISysFormFieldService;
import com.snow.from.service.ISysFormInstanceService;
import com.snow.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单实例Controller
 * 
 * @author 没用的阿吉
 * @date 2021-03-21
 */
@Controller
@RequestMapping("/from/instance")
public class SysFormInstanceController extends BaseController
{
    private String prefix = "system/instance";

    @Autowired
    private ISysFormInstanceService sysFormInstanceService;
    
    @Autowired
    private ISysFormFieldService sysFormFieldService;


    @RequiresPermissions("system:instance:view")
    @GetMapping()
    public String instance()
    {
        return prefix + "/instance";
    }

    /**
     * 查询单实例列表
     */
    @RequiresPermissions("system:instance:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFormInstance sysFormInstance)
    {
        startPage();
        List<SysFormInstance> list = sysFormInstanceService.selectSysFormInstanceList(sysFormInstance);
        return getDataTable(list);
    }

    /**
     * 导出单实例列表
     */
    @RequiresPermissions("system:instance:export")
    @Log(title = "单实例", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFormInstance sysFormInstance)
    {
        List<SysFormInstance> list = sysFormInstanceService.selectSysFormInstanceList(sysFormInstance);
        ExcelUtil<SysFormInstance> util = new ExcelUtil<SysFormInstance>(SysFormInstance.class);
        return util.exportExcel(list, "instance");
    }

    /**
     *
     * 保存表单数据
     * @param fromInfoDTO
     * @return
     */
    @PostMapping("/saveFromInfo")
    @ResponseBody
    @Transactional
    public AjaxResult saveFromInfo(@RequestBody FromInfoDTO fromInfoDTO)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        String contentHtml = fromInfoDTO.getContentHtml();
        Object ortumJson = JSON.parseObject(contentHtml).get(FromInfoDTO.ORTUM_JSON);

        List<FieldContentDTO> fieldContentDTOS = JSON.parseArray(JSON.toJSONString(ortumJson), FieldContentDTO.class);
        if(fieldContentDTOS.size()==0){
            return AjaxResult.error("还没有创建组件呢！");
        }
        SysFormInstance sysFormInstanceCode = sysFormInstanceService.selectSysFormInstanceByFormCode(fromInfoDTO.getFormCode());
        if(StringUtils.isNotNull(sysFormInstanceCode)){
            return AjaxResult.error(String.format("表单编号:%已存在",fromInfoDTO.getFormCode()));
        }
        SysFormInstance sysFormInstanceName = sysFormInstanceService.selectSysFormInstanceByFormCode(fromInfoDTO.getColumnName());
        if(StringUtils.isNotNull(sysFormInstanceName)){
            return AjaxResult.error(String.format("表单名称:%已存在",fromInfoDTO.getFormName()));
        }
        SysFormInstance sysFormInstance=new SysFormInstance();
        sysFormInstance.setFormCode(fromInfoDTO.getFormCode());
        sysFormInstance.setFormName(fromInfoDTO.getFormName());
        sysFormInstance.setRev(Long.parseLong(fromInfoDTO.getVersion()));
        sysFormInstance.setFromContentHtml(contentHtml);
        sysFormInstance.setCreateBy(sysUser.getUserName());
        sysFormInstance.setUpdateTime(new Date());
        sysFormInstanceService.insertSysFormInstance(sysFormInstance);

        fieldContentDTOS.stream().forEach(t->{
            SysFormField sysFormField=new SysFormField();
            sysFormField.setFromId(sysFormInstance.getId());
            sysFormField.setFieldKey(t.getName());
            sysFormField.setFieldName(t.getTitle());
            sysFormField.setFieldType(t.getComponentKey());
            sysFormField.setFieldHtml(t.getHtml());
            sysFormField.setRev(sysFormInstance.getRev());
            //组件属性
            sysFormField.setRemark(t.getComponentProperties());
            sysFormFieldService.insertSysFormField(sysFormField);
        });
        return AjaxResult.success(fromInfoDTO);
    }

    /**
     * 预览
     * @return
     */
    @GetMapping("/fromPreview/{id}")
    public String fromPreview(@PathVariable("id") Long id,ModelMap mmap)
    {
        mmap.put("formId",id);
        return prefix+"/fromPreview";
    }
    /**
     * 获取表单内容
     * @param formId
     * @return
     */
    @GetMapping("/getFromInfo/{formId}")
    @ResponseBody
    public AjaxResult getFromInfo(@PathVariable("formId")Long formId)
    {
        FromInfoDTO fromInfoDTO=new FromInfoDTO();
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(formId);
        fromInfoDTO.setFormCode(sysFormInstance.getFormCode());
        fromInfoDTO.setFormName(sysFormInstance.getFormName());
        fromInfoDTO.setVersion(String.valueOf(sysFormInstance.getRev()));
        fromInfoDTO.setId(String.valueOf(sysFormInstance.getId()));
        SysFormField sysFormField=new SysFormField();
        sysFormField.setFromId(formId);
        List<SysFormField> sysFormFieldList = sysFormFieldService.selectSysFormFieldList(sysFormField);
        if(CollectionUtil.isNotEmpty(sysFormFieldList)){
            String columnID = sysFormFieldList.stream().map(SysFormField::getFieldKey).collect(Collectors.joining(","));
            String columnName = sysFormFieldList.stream().map(SysFormField::getFieldName).collect(Collectors.joining(","));
            fromInfoDTO.setColumnID(columnID);
            fromInfoDTO.setColumnName(columnName);
            fromInfoDTO.setContentHtml(sysFormInstance.getFromContentHtml());
            fromInfoDTO.setEditor(sysFormInstance.getCreateBy());
            fromInfoDTO.setEditTime(sysFormInstance.getUpdateTime());
        }
        return AjaxResult.success(fromInfoDTO);
    }
    /**
     * 新增单实例
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存单实例
     */
    @RequiresPermissions("system:instance:add")
    @Log(title = "单实例", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysFormInstance sysFormInstance)
    {
        return toAjax(sysFormInstanceService.insertSysFormInstance(sysFormInstance));
    }

    /**
     * 修改单实例
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(id);
        mmap.put("sysFormInstance", sysFormInstance);
        return prefix + "/edit";
    }

    /**
     * 修改保存单实例
     */
    @RequiresPermissions("system:instance:edit")
    @Log(title = "单实例", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFormInstance sysFormInstance)
    {
        return toAjax(sysFormInstanceService.updateSysFormInstance(sysFormInstance));
    }

    /**
     * 删除单实例
     */
    @RequiresPermissions("system:instance:remove")
    @Log(title = "单实例", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysFormInstanceService.deleteSysFormInstanceByIds(ids));
    }
}
