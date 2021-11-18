package com.snow.from.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.StringUtils;
import com.snow.framework.util.ShiroUtils;
import com.snow.from.domain.FieldContentDTO;
import com.snow.from.domain.FromInfoDTO;
import com.snow.from.domain.SysFormField;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.domain.request.FormFieldRequest;
import com.snow.from.domain.request.FormRequest;
import com.snow.from.service.impl.SysFormFieldServiceImpl;
import com.snow.from.service.impl.SysFormInstanceServiceImpl;
import com.snow.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

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

    @PostMapping("/form/saveForm")
    @ResponseBody
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
        SysFormInstance sysFormInstance=new SysFormInstance();
        sysFormInstance.setFormCode(formRequest.getFormId());
        sysFormInstance.setFormName(formRequest.getFormName());
        sysFormInstance.setRev(1L);
        sysFormInstance.setFromContentHtml(formData);
        sysFormInstance.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        sysFormInstance.setUpdateTime(new Date());
        sysFormInstanceService.insertSysFormInstance(sysFormInstance);

        List<FormFieldRequest> fieldContentDTOS = JSON.parseArray(formData, FormFieldRequest.class);
        for (int i=0;i<fieldContentDTOS.size();i++){
            SysFormField sysFormField=new SysFormField();
            sysFormField.setFromId(sysFormInstance.getId());
            sysFormField.setFieldKey(fieldContentDTOS.get(i).getId());
            sysFormField.setFieldName(fieldContentDTOS.get(i).getLabel());
            sysFormField.setFieldType(fieldContentDTOS.get(i).getTag());
            sysFormField.setFieldHtml(JSON.parseArray(formData).getString(i));
            sysFormField.setRev(sysFormInstance.getRev());
            sysFormFieldService.insertSysFormField(sysFormField);
        }
        return AjaxResult.success();
    }
}
