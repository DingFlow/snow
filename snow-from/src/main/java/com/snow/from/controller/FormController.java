package com.snow.from.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.StringUtils;
import com.snow.framework.util.ShiroUtils;
import com.snow.from.domain.SysFormDataRecord;
import com.snow.from.domain.SysFormField;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.domain.request.FormFieldRequest;
import com.snow.from.domain.request.FormRecordRequest;
import com.snow.from.domain.request.FormRequest;
import com.snow.from.service.impl.SysFormDataRecordServiceImpl;
import com.snow.from.service.impl.SysFormFieldServiceImpl;
import com.snow.from.service.impl.SysFormInstanceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/18 14:55
 */
@Controller
@RequestMapping()
@Slf4j
public class FormController {

    @Autowired
    private SysFormInstanceServiceImpl sysFormInstanceService;

    @Autowired
    private SysFormFieldServiceImpl sysFormFieldService;

    @Autowired
    private SysFormDataRecordServiceImpl sysFormDataRecordService;
    /**
     * 跳转form表单首页
     * @return 首页url路径
     */
    @GetMapping("/formIndex")
    public String fromPreview() {
        return "/formIndex";
    }

    @GetMapping("/preview.html")
    public String preview() {
        return "/preview";
    }

    @GetMapping("/handwrittenSignature.html")
    public String handwrittenSignature() {
        return "/handwrittenSignature";
    }

    @GetMapping("/editorMenu.html")
    public String editorMenu() {
        return "/editorMenu";
    }

    /**
     * 保存表单数据
     * @param formRequest 表单参数
     * @return 是否成功
     */
    @PostMapping("/form/saveForm")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult saveForm(FormRequest formRequest){
        log.info("=====>{}", JSON.toJSONString(formRequest));
        String formData = formRequest.getFormData();
        if(StrUtil.isBlank(formData)){
            return AjaxResult.error("还没有创建组件呢！");
        }
        SysFormInstance sysFormInstanceCode = sysFormInstanceService.selectSysFormInstanceByFormCode(formRequest.getFormId());
        if(StringUtils.isNotNull(sysFormInstanceCode)){
            return AjaxResult.error(String.format("表单编号:%已存在",formRequest.getFormId()));
        }
        SysFormInstance sysFormInstanceName = sysFormInstanceService.selectSysFormInstanceByFormName(formRequest.getFormName());
        if(StringUtils.isNotNull(sysFormInstanceName)){
            return AjaxResult.error(String.format("表单名称:%已存在",formRequest.getFormName()));
        }
        //保存主表数据
        SysFormInstance sysFormInstance=new SysFormInstance();
        sysFormInstance.setFormCode(formRequest.getFormId());
        sysFormInstance.setFormName(formRequest.getFormName());
        sysFormInstance.setRev(1L);
        sysFormInstance.setFromContentHtml(formData);
        sysFormInstance.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        sysFormInstance.setUpdateTime(new Date());
        sysFormInstanceService.insertSysFormInstance(sysFormInstance);
        //保存子表数据
        saveFormField(sysFormInstance.getId(),formData);
        return AjaxResult.success();
    }

    /**
     * 预览
     * @return 预览页
     */
    @GetMapping("/fromPreview")
    public String fromPreview(@RequestParam Long id, ModelMap mmap) {
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(id);
        mmap.put("formId",id);
        mmap.put("name",sysFormInstance.getFormName());
        return "/fromPreview";
    }

    /**
     * 保存表单填写记录
     * @return
     */
    @PostMapping("/form/saveFormRecord")
    @ResponseBody
    public AjaxResult saveFormRecord(@RequestParam String formId ,
                                     @RequestParam String formData ){
        Long userId = ShiroUtils.getUserId();
        SysFormDataRecord sysFormDataRecord=new SysFormDataRecord();
        sysFormDataRecord.setBelongUserId(String.valueOf(userId));
        sysFormDataRecord.setFormData(formData);
        sysFormDataRecord.setFormId(formId);
        sysFormDataRecord.setCreateBy(String.valueOf(userId));
        //获取最大版本号
        Integer maxVersion = sysFormDataRecordService.getMaxVersionByUsrId(userId);
        //版本号+1组成最新版本号
        sysFormDataRecord.setVersion(Optional.ofNullable(maxVersion).orElse(0)+1);
        sysFormDataRecordService.insertSysFormDataRecord(sysFormDataRecord);
        return AjaxResult.success();
    }
    /**
     * 构建子表数据
     * @param formId 表单id
     * @param formData 表单数据
     */
    private void saveFormField(Long formId,String formData ){
        List<FormFieldRequest> formFieldRequestList = JSON.parseArray(formData, FormFieldRequest.class);
        for (int i=0;i<formFieldRequestList.size();i++){
            FormFieldRequest formFieldRequest = formFieldRequestList.get(i);
            SysFormField sysFormField = BeanUtil.copyProperties(formFieldRequest, SysFormField.class,"id");
            sysFormField.setFromId(formId);
            sysFormField.setFieldKey(formFieldRequest.getId());
            sysFormField.setFieldName(formFieldRequest.getLabel());
            sysFormField.setFieldType(formFieldRequest.getTag());
            sysFormField.setFieldHtml(JSON.parseArray(formData).getString(i));
            sysFormFieldService.insertSysFormField(sysFormField);
        }
    }
}
