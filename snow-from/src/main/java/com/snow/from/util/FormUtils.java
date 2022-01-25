package com.snow.from.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.snow.common.enums.FormFieldTypeEnum;
import com.snow.common.exception.BusinessException;
import com.snow.from.domain.field.InputField;

import java.util.Optional;

/**
 * @author qimingjin
 * @Title: DingFlow form表单工具类
 * @Description:
 * @date 2021/11/25 15:25
 */
public class FormUtils {

    //选中标识
    private static final String CHECKED="checked";
    /**
     * 填充表单值
     * @param formData 原始表单数据json
     * @param formField 用户填写的数据
     * @return 返回组装后的json数据
     */
    public  static String fillFormFieldValue(String formData, String formField){
        //填写的表单值
        JSONObject formFieldObject = JSON.parseObject(formField);
        //原始表单
        JSONArray formDataArray = JSON.parseArray(formData);
        //把填写的表单值装填到原始表单上
        for(int i=0;i<formDataArray.size();i++){
            JSONObject fieldObject=formDataArray.getJSONObject(i);
            //一行多列布局
            if(fieldObject.getString("tag").equals(FormFieldTypeEnum.GRID.getCode())){
                JSONObject gridObject = formDataArray.getJSONObject(i);
                JSONArray columnArray= gridObject.getJSONArray("columns");
                for(int j=0;j<columnArray.size();j++){
                    JSONObject columnObject = columnArray.getJSONObject(j);
                    JSONArray listArray = columnObject.getJSONArray("list");
                    for(int k=0;k<listArray.size();k++){
                        //获取组件类型
                        String tag= JSON.parseObject(listArray.getString(k)).getString("tag");
                        JSONObject listObject=listArray.getJSONObject(k);
                        //根据组件类型填充值
                        fillValueField(tag,listObject,formFieldObject);
                    }
                }
            }else {
                //正常单行布局
                fillValueField(fieldObject.getString("tag"),fieldObject,formFieldObject);
            }
        }
        return JSON.toJSONString(formDataArray);
    }


    private static void fillValueField(String tag,JSONObject listObject,JSONObject formFieldObject){
        if(isDefaultValue(tag)){
            warpDefaultValueField(listObject,formFieldObject);
        } else if(tag.equals(FormFieldTypeEnum.SWITCH.getCode())){
            warpSwitchValueField(listObject,formFieldObject);
        } else if(tag.equals(FormFieldTypeEnum.SIGN.getCode())){
            warpDateField(listObject,formFieldObject);
        } else if(tag.equals(FormFieldTypeEnum.DATE.getCode())){
            warpDateDefaultValueField(listObject,formFieldObject);
        }else if(tag.equals(FormFieldTypeEnum.DATE_RANGE.getCode())){
            warpDateRangeDefaultValueField(listObject,formFieldObject);
        }else if(tag.equals(FormFieldTypeEnum.CHECKBOX.getCode())){
            warpCheckBoxValueField(listObject,formFieldObject);
        }else if(tag.equals(FormFieldTypeEnum.RADIO.getCode())){
            warpCheckedValueField(listObject,formFieldObject, FormFieldTypeEnum.RADIO);
        }else if(tag.equals(FormFieldTypeEnum.SELECT.getCode())){
            warpCheckedValueField(listObject,formFieldObject, FormFieldTypeEnum.SELECT);
        }else {
            throw new BusinessException(StrUtil.format("暂不支持的组件:{}",tag));
        }
    }

    /**
     * 判断是否是默认值形式
     * @param tag 表单字段类型
     * @return 是否
     */
    private static boolean isDefaultValue(String tag){
        return tag.equals(FormFieldTypeEnum.INPUT.getCode()) || tag.equals(FormFieldTypeEnum.TEXTAREA.getCode()) || tag.equals(FormFieldTypeEnum.PASSWORD.getCode())
                || tag.equals(FormFieldTypeEnum.NUMBER_INPUT.getCode()) || tag.equals(FormFieldTypeEnum.RATE.getCode()) || tag.equals(FormFieldTypeEnum.CRON.getCode())
                || tag.equals(FormFieldTypeEnum.FILE.getCode()) ||tag.equals(FormFieldTypeEnum.IMAGE.getCode()) ||tag.equals(FormFieldTypeEnum.COLOR_PICKER.getCode())
                ||tag.equals(FormFieldTypeEnum.ICON_PICKER.getCode()) ||tag.equals(FormFieldTypeEnum.SLIDER.getCode());
    }

    //输入框
    public static void warpDefaultValueField(JSONObject fieldObject,JSONObject formFieldObject ){
        String value = formFieldObject.getString(fieldObject.getString("id"));
        fieldObject.put("defaultValue",value);
    }

    //构建输入框对象
    public static InputField warpInputField(JSONObject fieldObject, JSONObject formFieldObject ){
        String value = formFieldObject.getString(fieldObject.getString("id"));
        InputField inputField = fieldObject.toJavaObject(InputField.class);
        inputField.setDefaultValue(value);
        return inputField;
    }

    //开关
    public static void warpSwitchValueField(JSONObject fieldObject,JSONObject formFieldObject ){
        String value = formFieldObject.getString(fieldObject.getString("id"));
        fieldObject.put("switchValue",value);
    }

    //日期
    public static void warpDateDefaultValueField(JSONObject fieldObject,JSONObject formFieldObject ){
        String value = formFieldObject.getString(StrUtil.format("date{}",fieldObject.getString("id")));
        fieldObject.put("dateDefaultValue",value);
    }

    //日期范围
    public static void warpDateRangeDefaultValueField(JSONObject fieldObject,JSONObject formFieldObject ){
        String startValue = formFieldObject.getString(StrUtil.format("start{}",fieldObject.getString("id")));
        String endValue = formFieldObject.getString(StrUtil.format("end{}",fieldObject.getString("id")));
        fieldObject.put("dateRangeDefaultValue",StrUtil.format("{} - {}",startValue,endValue));
    }

    //签名sign
    public static void warpDateField(JSONObject fieldObject,JSONObject formFieldObject ){
        String value = formFieldObject.getString(fieldObject.getString("id"));
        fieldObject.put("data",value);
    }

    //下拉和单选
    public static void warpCheckedValueField(JSONObject fieldObject, JSONObject formFieldObject, FormFieldTypeEnum formFieldTypeEnums){
        String value = formFieldObject.getString(fieldObject.getString("id"));
        if(formFieldTypeEnums.getCode().equals(FormFieldTypeEnum.SELECT.getCode())){
            fieldObject.put("remoteDefaultValue",value);
        }
        JSONArray optionsArray= fieldObject.getJSONArray("options");
        for(int k=0;k<optionsArray.size();k++){
            JSONObject optionObject = optionsArray.getJSONObject(k);
            //值相等则设置为checked
            if(optionObject.get("value").equals(value)){
                optionObject.put(CHECKED,true);
            }else {
                optionObject.put(CHECKED,false);
            }
        }
    }

    //复选框赋值
    public static void warpCheckBoxValueField(JSONObject fieldObject,JSONObject formFieldObject){
        JSONArray optionsArray= fieldObject.getJSONArray("options");
        for(int k=0;k<optionsArray.size();k++){
            JSONObject optionObject = optionsArray.getJSONObject(k);
            String key=StrUtil.format("{}[{}]",fieldObject.getString("id"),optionObject.get("value"));
            String value = Optional.ofNullable(formFieldObject.getString(key)).orElse("off");
            //如果为on 则checked选中
            if(value.equals("on")){
                optionObject.put(CHECKED,true);
            }else {
                optionObject.put(CHECKED,false);
            }
        }
    }
}
