package com.snow.from.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.snow.common.enums.FormFieldTypeEnum;
import com.snow.common.utils.StringUtils;
import com.snow.dingtalk.model.request.SaveProcessRequest;
import com.snow.dingtalk.model.request.StartFakeProcessInstanceRequest;
import com.snow.dingtalk.service.DingOfficialFlowService;
import com.snow.from.domain.SysFormDataRecord;
import com.snow.from.domain.SysFormField;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.service.IDingTalkProcessService;
import com.snow.from.service.ISysFormDataRecordService;
import com.snow.from.service.ISysFormFieldService;
import com.snow.from.service.ISysFormInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author agee
 * @Title:
 * @Description:
 * @date 2022/1/1 10:19
 */
@Slf4j
@Service
public class DingTalkProcessServiceImpl implements IDingTalkProcessService {

    @Resource
    private DingOfficialFlowService dingOfficialFlowService;

    @Resource
    private ISysFormInstanceService formInstanceService;

    @Resource
    private ISysFormFieldService formFieldService;

    @Resource
    private ISysFormDataRecordService formDataRecordService;

    @Override
    public String saveOrUpdateDingTalkProcess(String formCode) {
        SysFormInstance sysFormInstance = formInstanceService.selectSysFormInstanceByFormCode(formCode);
        SysFormField sysFormField=new SysFormField();
        sysFormField.setFromId(sysFormInstance.getId());
        List<SysFormField> sysFormFields = formFieldService.selectSysFormFieldList(sysFormField);
        SaveProcessRequest saveProcessRequest=new SaveProcessRequest();
        saveProcessRequest.setName(sysFormInstance.getFormName());
        saveProcessRequest.setDescription(sysFormInstance.getFormName());
        saveProcessRequest.setFormComponentList(warpFormComponentVoList(sysFormFields));
        if(StringUtils.isNotEmpty(sysFormInstance.getDingProcessCode())){
            saveProcessRequest.setProcessCode(sysFormInstance.getDingProcessCode());
        }
        String processCode= dingOfficialFlowService.saveProcess(saveProcessRequest);
        //更新表单实例表的formCode
        SysFormInstance updateFormInstance=new SysFormInstance();
        updateFormInstance.setId(sysFormInstance.getId());
        updateFormInstance.setDingProcessCode(processCode);
        formInstanceService.updateSysFormInstance(updateFormInstance);
        log.info("@@创建钉钉模板返回的流程code:{}",processCode);
        return processCode;
    }

    @Override
    public String SaveFakeProcessInstance(String fromNo) {
        SysFormDataRecord sysFormDataRecord = formDataRecordService.selectSysFormDataRecordByFormNo(fromNo);
        SysFormInstance sysFormInstance = formInstanceService.selectSysFormInstanceById(Long.parseLong(sysFormDataRecord.getFormId()));
        StartFakeProcessInstanceRequest startFakeProcessInstanceRequest=new StartFakeProcessInstanceRequest();
        startFakeProcessInstanceRequest.setTitle(sysFormInstance.getFormName());
        startFakeProcessInstanceRequest.setProcessCode(sysFormInstance.getDingProcessCode());
        List<StartFakeProcessInstanceRequest.FormComponentValueVo> formComponentValueVoList=Lists.newArrayList();
        //把表单的key value 赋值到请求参数里面
        JSONObject formFieldJSONObject = JSON.parseObject(sysFormDataRecord.getFormField());
        for (Map.Entry<String, Object> entry : formFieldJSONObject.entrySet()) {
            StartFakeProcessInstanceRequest.FormComponentValueVo  formComponentValueVo=new StartFakeProcessInstanceRequest.FormComponentValueVo();
            formComponentValueVo.setName(entry.getKey());
            formComponentValueVo.setValue(String.valueOf(entry.getValue()));
            formComponentValueVoList.add(formComponentValueVo);
        }
        startFakeProcessInstanceRequest.setFormComponentValueVoList(formComponentValueVoList);
        String processInstanceId= dingOfficialFlowService.saveFakeProcessInstance(startFakeProcessInstanceRequest);
        SysFormDataRecord updateFormDataRecord=new SysFormDataRecord();
        updateFormDataRecord.setId(sysFormDataRecord.getId());
        updateFormDataRecord.setDingProcessInstanceId(processInstanceId);
        formDataRecordService.updateSysFormDataRecord(updateFormDataRecord);
        log.info("@@发起自有OA钉钉流程id:{}",processInstanceId);
        return processInstanceId;
    }

    /**
     * 组装 List<SaveProcessRequest.FormComponentVo>
     * @param sysFormFields 系统表单属性集合
     * @return 钉钉表单
     */
   private List<SaveProcessRequest.FormComponentVo> warpFormComponentVoList(List<SysFormField> sysFormFields){
        //返回参数集合
        List<SaveProcessRequest.FormComponentVo> formComponentVoList= Lists.newArrayList();
        //循环组装请求钉钉接口所需参数
        sysFormFields.stream().filter(sysFormField ->
            ObjectUtil.isNull(FormFieldTypeEnum.getCode(sysFormField.getFieldType()))
        ).forEach(t->{
            SaveProcessRequest.FormComponentVo formComponentVo=new SaveProcessRequest.FormComponentVo();
            FormFieldTypeEnum formFieldTypeEnum = FormFieldTypeEnum.getCode(t.getFieldType());
            formComponentVo.setComponentName(formFieldTypeEnum.getDingTalkCode());
            SaveProcessRequest.FormComponentPropVo formComponentPropVo=new SaveProcessRequest.FormComponentPropVo();
            formComponentPropVo.setId(t.getFieldKey());
            formComponentPropVo.setLabel(t.getFieldName());
            formComponentPropVo.setPlaceholder(t.getPlaceholder());
            formComponentPropVo.setRequired(t.isRequired());
            formComponentVo.setProps(formComponentPropVo);
            formComponentVoList.add(formComponentVo);
        });
        return formComponentVoList;
    }

}
