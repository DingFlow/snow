package com.snow.from.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.snow.common.annotation.Log;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.core.text.Convert;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.StringUtils;
import com.snow.framework.util.ShiroUtils;
import com.snow.from.domain.SysFormDataRecord;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.domain.response.FormDataRecordResponse;
import com.snow.from.domain.response.FormInstanceResponse;
import com.snow.from.service.ISysFormInstanceService;
import com.snow.from.service.impl.SysFormDataRecordServiceImpl;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysUserServiceImpl;
import org.apache.commons.compress.utils.Lists;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private SysFormDataRecordServiceImpl sysFormDataRecordService;

    @Autowired
    private SysUserServiceImpl sysUserService;


    @RequiresPermissions("system:instance:view")
    @GetMapping()
    public String instance() {
        return prefix + "/instance";
    }

    /**
     * 查询单实例列表
     */
    @RequiresPermissions("system:instance:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFormInstance sysFormInstance) {
        startPage();
        if(!ShiroUtils.isAdmin()){
            Long userId = ShiroUtils.getUserId();
            sysFormInstance.setCreateBy(String.valueOf(userId));
        }
        List<SysFormInstance> list = sysFormInstanceService.selectSysFormInstanceList(sysFormInstance);
        return getDataTable(warpSysFormInstanceRecord(list));
    }


    /**
     * 预览
     * @return
     */
    @GetMapping("/fromPreview/{id}")
    public String fromPreview(@PathVariable("id") Long id,ModelMap mmap)
    {
        mmap.put("formId",id);
        return "fromPreview";
    }
    /**
     * 获取表单内容
     * @param formId
     * @return
     */
    @PostMapping("/getFromInfo")
    @ResponseBody
    public AjaxResult getFromInfo(@RequestParam Long formId)
    {
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(formId);
        return AjaxResult.success(sysFormInstance.getFromContentHtml());
    }

    /**
     * 修改单实例
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
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
    public AjaxResult editSave(SysFormInstance sysFormInstance) {
        SysFormInstance sysFormInstanceName = sysFormInstanceService.selectSysFormInstanceByFormName(sysFormInstance.getFormName());
        SysFormInstance sysFormInstanceCode = sysFormInstanceService.selectSysFormInstanceByFormCode(sysFormInstance.getFormName());
        if(StringUtils.isNotNull(sysFormInstanceName)&&!sysFormInstanceName.getFormName().equals(sysFormInstance.getFormName())){
            return AjaxResult.error(StrUtil.format("表单名称{}已存在",sysFormInstance.getFormName()));
        }
        if(StringUtils.isNotNull(sysFormInstanceCode)&&!sysFormInstanceCode.getFormCode().equals(sysFormInstance.getFormCode())){
            return AjaxResult.error(StrUtil.format("表单名称{}已存在",sysFormInstance.getFormCode()));
        }
        return toAjax(sysFormInstanceService.updateSysFormInstance(sysFormInstance));
    }

    /**
     * 绑定流程key
     */
    @RequiresPermissions("system:instance:bindProcess")
    @PostMapping("/bindProcess")
    @ResponseBody
    public AjaxResult bindProcess(SysFormInstance sysFormInstance) {
        return toAjax(sysFormInstanceService.updateSysFormInstance(sysFormInstance));
    }

    /**
     * 跳转表单记录
     * @param id 记录id
     * @param mmap 传给前端的数据
     * @return 跳转的页面
     */
    @RequiresPermissions("system:record:view")
    @GetMapping("/formRecord")
    public String formRecord(@RequestParam Long id,ModelMap mmap)
    {
        mmap.put("id",id);
        return prefix + "/record";
    }

    /**
     * 查询单数据记录列表
     */
    @RequiresPermissions("system:record:list")
    @PostMapping("/recordList")
    @ResponseBody
    public TableDataInfo recordList(SysFormDataRecord sysFormDataRecord)
    {
        startPage();
        List<SysFormDataRecord> list = sysFormDataRecordService.selectSysFormDataRecordList(sysFormDataRecord);
        return getDataTable(warpSysFormDataRecord(list));
    }

    /**
     * 删除表单记录
     * @param ids 前端传入的id
     * @return 返回是否成功
     */
    @RequiresPermissions("system:record:remove")
    @Log(title = "表单记录", businessType = BusinessType.DELETE)
    @PostMapping( "/removeRecord")
    @ResponseBody
    public AjaxResult removeRecord(String ids) {
        return toAjax(sysFormDataRecordService.deleteSysFormDataRecordByIds(ids));
    }

    /**
     * 删除单实例
     */
    @RequiresPermissions("system:instance:remove")
    @Log(title = "单实例", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        SysFormDataRecord sysFormDataRecord = new SysFormDataRecord();
        sysFormDataRecord.setFormIdList(Convert.toStrList(ids));
        List<SysFormDataRecord> sysFormDataRecords = sysFormDataRecordService.selectSysFormDataRecordList(sysFormDataRecord);
        if(CollUtil.isNotEmpty(sysFormDataRecords)){
            return AjaxResult.error("该表单已被填写,暂不能删除");
        }
        return toAjax(sysFormInstanceService.deleteSysFormInstanceByIds(ids));
    }

    /**
     * 组装参数
     * @param list 传入需要组装的参数list
     */
    private List<FormDataRecordResponse> warpSysFormDataRecord(List<SysFormDataRecord> list ){
        List<FormDataRecordResponse> formDataRecordResponseList= Lists.newArrayList();
        if(CollUtil.isEmpty(list)){
            return formDataRecordResponseList;
        }
        list.forEach(t->{
            SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(Long.valueOf(t.getFormId()));
            FormDataRecordResponse formDataRecordResponse =BeanUtil.copyProperties(t,FormDataRecordResponse.class);
            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(t.getBelongUserId()));
            formDataRecordResponse.setFormName(sysFormInstance.getFormName());
            formDataRecordResponse.setBelongUserName(sysUser.getUserName());
            formDataRecordResponse.setProcessFlag(StrUtil.isNotBlank(sysFormInstance.getProcessKey()));
            formDataRecordResponseList.add(formDataRecordResponse);

        });
        return formDataRecordResponseList;
    }

    /**
     * 组装返回流程实例
     * @param list 传入list
     * @return 组装后的数据
     */
    private List<FormInstanceResponse> warpSysFormInstanceRecord(List<SysFormInstance> list ){
        List<FormInstanceResponse> formInstanceResponseList=Lists.newArrayList();
        if(CollUtil.isEmpty(list)){
            return formInstanceResponseList;
        }
        list.forEach(t->{
            FormInstanceResponse formInstanceResponse = BeanUtil.copyProperties(t, FormInstanceResponse.class);
            SysUser sysUser = sysUserService.selectUserById(Long.valueOf(t.getCreateBy()));
            formInstanceResponse.setCreateByName(sysUser.getUserName());
            formInstanceResponseList.add(formInstanceResponse);
        });
        return formInstanceResponseList;
    }


}
