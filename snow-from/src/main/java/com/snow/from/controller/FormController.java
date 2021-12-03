package com.snow.from.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.snow.common.constant.CacheConstants;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.enums.FormFieldTypeEnums;
import com.snow.common.utils.CacheUtils;
import com.snow.common.utils.StringUtils;
import com.snow.framework.util.ShiroUtils;
import com.snow.from.domain.SysFormDataRecord;
import com.snow.from.domain.SysFormField;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.domain.request.FormFieldRequest;
import com.snow.from.domain.request.FormRequest;
import com.snow.from.service.impl.SysFormDataRecordServiceImpl;
import com.snow.from.service.impl.SysFormFieldServiceImpl;
import com.snow.from.service.impl.SysFormInstanceServiceImpl;
import com.snow.from.util.FormUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
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
public class FormController{

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
    @GetMapping("formIndex")
    public String fromPreview() {
        return "formIndex";
    }

    @GetMapping("preview.html")
    public String preview() {
        return "preview";
    }

    @GetMapping("handwrittenSignature.html")
    public String handwrittenSignature() {
        return "handwrittenSignature";
    }

    @GetMapping("editorMenu.html")
    public String editorMenu() {
        return "editorMenu";
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
        log.info("@@保存设计的表单前端传入的数据:{}", JSON.toJSONString(formRequest));
        String formData = formRequest.getFormData();
        if(StrUtil.isBlank(formData)){
            return AjaxResult.error("还没有创建组件呢！");
        }
        if(StringUtils.isEmpty(formRequest.getFormId())){
            return AjaxResult.error("表单id必填项");
        }
        if(StringUtils.isEmpty(formRequest.getFormName())){
            return AjaxResult.error("表单名称必填项");
        }
        SysFormInstance sysFormInstanceCode = sysFormInstanceService.selectSysFormInstanceByFormCode(formRequest.getFormId());
        if(StringUtils.isNotNull(sysFormInstanceCode)){
            return AjaxResult.error(StrUtil.format("表单编号:{}已存在",formRequest.getFormId()));
        }
        SysFormInstance sysFormInstanceName = sysFormInstanceService.selectSysFormInstanceByFormName(formRequest.getFormName());
        if(StringUtils.isNotNull(sysFormInstanceName)){
            return AjaxResult.error(StrUtil.format("表单名称:{}已存在",formRequest.getFormName()));
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
    @GetMapping("fromPreview")
    public String fromPreview(@RequestParam Long id, ModelMap mmap) {
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(id);
        mmap.put("formId",id);
        mmap.put("name",sysFormInstance.getFormName());
        return "fromPreview";
    }

    /**
     * 跳转绑定流程页
     * @return 跳转绑定流程页
     */
    @GetMapping("bindProcess")
    public String bindProcess(@RequestParam Long id, ModelMap mmap) {
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(id);
        mmap.put("sysFormInstance",sysFormInstance);
        return "system/instance/bindProcess";
    }
    /**
     * 保存表单填写记录
     * @return
     */
    @PostMapping("/form/saveFormRecord")
    @ResponseBody
    public AjaxResult saveFormRecord(@RequestParam String formId ,
                                     @RequestParam String formData,
                                     @RequestParam String formField){

        //把用户填写的值赋值到表单里面去
        String newFormData = FormUtils.fillFormFieldValue(formData, formField);
        Long userId = ShiroUtils.getUserId();
        SysFormDataRecord sysFormDataRecord=new SysFormDataRecord();
        sysFormDataRecord.setBelongUserId(String.valueOf(userId));
        sysFormDataRecord.setFormData(newFormData);
        sysFormDataRecord.setFormId(formId);
        sysFormDataRecord.setFormField(formField);
        sysFormDataRecord.setCreateBy(String.valueOf(userId));
        //获取最大版本号
        Integer maxVersion = sysFormDataRecordService.getMaxVersionByUsrId(userId);
        //版本号+1组成最新版本号
        sysFormDataRecord.setVersion(Optional.ofNullable(maxVersion).orElse(0)+1);
        sysFormDataRecordService.insertSysFormDataRecord(sysFormDataRecord);
        return AjaxResult.success();
    }

    /**
     * 跳转到详情
     * @param id 记录id
     * @param map 返回前端的数据
     * @return 跳转页面
     */
    @GetMapping("/toFormRecordDetail")
    public String toFormRecordDetail(String id,ModelMap map){
        SysFormDataRecord sysFormDataRecord = sysFormDataRecordService.selectSysFormDataRecordById(Integer.valueOf(id));
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(Long.valueOf(sysFormDataRecord.getFormId()));
        map.put("id",id);
        map.put("name",sysFormInstance.getFormName());
        map.put("createTime",DateUtil.formatDateTime(sysFormDataRecord.getCreateTime()));
        return "formDetail";
    }


    /**
     * 表单详情
     * @param id 表单记录id
     * @return 表单数据
     */
    @PostMapping("/form/getFormRecordDetail")
    @ResponseBody
    public AjaxResult getFormRecordDetail(Integer id){
        SysFormDataRecord sysFormDataRecord = sysFormDataRecordService.selectSysFormDataRecordById(id);
        return AjaxResult.success(sysFormDataRecord.getFormData());
    }
    /**
     * 构建子表数据
     * @param formId 表单id
     * @param formData 表单数据
     */
    private void saveFormField(Long formId,String formData ){
        //解析表单
        JSONArray formDataArray = JSON.parseArray(formData);
        for(int i=0;i<formDataArray.size();i++){
            JSONObject fieldObject=formDataArray.getJSONObject(i);
            //一行多列布局
            if(fieldObject.getString("tag").equals(FormFieldTypeEnums.GRID.getCode())){
                JSONObject gridObject = formDataArray.getJSONObject(i);
                JSONArray columnArray= gridObject.getJSONArray("columns");
                for(int j=0;j<columnArray.size();j++){
                    JSONObject columnObject = columnArray.getJSONObject(j);
                    JSONArray listArray = columnObject.getJSONArray("list");
                    for(int k=0;k<listArray.size();k++){
                        JSONObject listObject=listArray.getJSONObject(k);
                        FormFieldRequest formFieldRequest = listObject.toJavaObject(FormFieldRequest.class);
                        saveSysFormField(formFieldRequest,formId,JSON.toJSONString(listObject));
                    }
                }
            }
            //正常单行布局
            else {
                FormFieldRequest formFieldRequest = fieldObject.toJavaObject(FormFieldRequest.class);
                saveSysFormField(formFieldRequest,formId,JSON.toJSONString(fieldObject));
            }
        }
    }

    /**
     * 保存
     */
    public void saveSysFormField( FormFieldRequest formFieldRequest,Long formId,String jsonString){
        SysFormField sysFormField = BeanUtil.copyProperties(formFieldRequest, SysFormField.class,"id");
        sysFormField.setFromId(formId);
        sysFormField.setFieldKey(formFieldRequest.getId());
        sysFormField.setFieldName(formFieldRequest.getLabel());
        sysFormField.setFieldType(formFieldRequest.getTag());
        sysFormField.setFieldHtml(jsonString);
        sysFormFieldService.insertSysFormField(sysFormField);
    }

    /**
     * 生成二维码
     */
    @GetMapping("/createQRCode")
    public void createQRCode(@RequestParam("id") int id, HttpServletResponse response){
        Object domain = CacheUtils.getSysConfig(CacheConstants.SYS_DOMAIN, "http://localhost");
        QrConfig config = new QrConfig(500, 500);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(3);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(Color.CYAN);
        // 设置背景色（灰色）
        config.setBackColor(Color.GRAY);
        config.setQrVersion(10);
       // config.setImg("https://qimetons.oss-cn-beijing.aliyuncs.com/45a22bcc93644dfe8bcacf690fe133f3.png");
        // 生成二维码
        BufferedImage bufferedImage = QrCodeUtil.generate(StrUtil.format("{}/toFormRecordDetail?id={}",domain,id), config);
        try {
            //以PNG格式向客户端发送
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(bufferedImage, "PNG",os);
            os.flush();
            os.close();
        } catch (IOException e) {
            throw new RuntimeException("生成二维码异常");
        }
    }

}
