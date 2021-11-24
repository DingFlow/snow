package com.snow.from.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.snow.common.enums.FormFieldTypeEnums;
import com.snow.from.domain.field.*;
import com.snow.from.domain.response.BaseFormDataResponse;

import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-11-24 21:37
 **/
public class BaseFieldController{

    public String warpFormField(String formData, String formField){
        //填写的表单值
        JSONObject formFieldObject = JSON.parseObject(formField);
        //原始表单
        JSONArray formDataArray = JSON.parseArray(formData);
        //把填写的表单值装填到原始表单上
        for(int i=0;i<formDataArray.size();i++){
            JSONObject fieldObject=formDataArray.getJSONObject(i);
            //一行多列布局
            if(fieldObject.getString("tag").equals(FormFieldTypeEnums.GRID.getCode())){
                JSONObject jsonObject = formDataArray.getJSONObject(i);
                JSONArray columnsArray= jsonObject.getJSONArray("columns");
                for(int n=0;n<columnsArray.size();n++){
                    JSONObject columnObject = columnsArray.getJSONObject(n);
                    JSONArray listArray = columnObject.getJSONArray("list");
                    for(int j=0;j<listArray.size();j++){
                        //获取组件类型
                        String tag= JSON.parseObject(listArray.getString(j)).getString("tag");
                        belongField(tag,j,columnsArray,formFieldObject);
                    }
                }
            }else {
                //正常单行布局
                belongField(fieldObject.getString("tag"),i,formDataArray,formFieldObject);
            }
        }
        System.out.println(JSONArray.toJSONString(formDataArray));
        return JSONArray.toJSONString(formDataArray);
    }


    private void belongField(String tag,int index,JSONArray columnsArray,JSONObject formFieldObject){
        if(tag.equals(FormFieldTypeEnums.INPUT.getCode())){
            warpDefaultValueField(index,columnsArray,formFieldObject);
        }else if(tag.equals(FormFieldTypeEnums.TEXTAREA.getCode())){
            warpDefaultValueField(index,columnsArray,formFieldObject);
        }else if(tag.equals(FormFieldTypeEnums.NUMBER_INPUT.getCode())){
            warpDefaultValueField(index,columnsArray,formFieldObject);
        }else if(tag.equals(FormFieldTypeEnums.DATE.getCode())){
            warpDateDefaultValueField(index,columnsArray,formFieldObject);
        }else if(tag.equals(FormFieldTypeEnums.DATE_RANGE.getCode())){
            warpDateRangeDefaultValueField(index,columnsArray,formFieldObject);
        }else if(tag.equals(FormFieldTypeEnums.CHECKBOX.getCode())){
            warpCheckedValueField(index,columnsArray,formFieldObject,FormFieldTypeEnums.CHECKBOX);
        }else if(tag.equals(FormFieldTypeEnums.RADIO.getCode())){
            warpCheckedValueField(index,columnsArray,formFieldObject,FormFieldTypeEnums.RADIO);
        }else if(tag.equals(FormFieldTypeEnums.SELECT.getCode())){
            warpCheckedValueField(index,columnsArray,formFieldObject,FormFieldTypeEnums.SELECT);
        }
    }
    /**
     *  组装input
     * @param columnsArray
     * @param formFieldObject
     */
    public void warpDefaultValueField(int index,JSONArray columnsArray,JSONObject formFieldObject ){
        JSONObject jsonObject = columnsArray.getJSONObject(index);
        String value = formFieldObject.getString(jsonObject.getString("id"));
        jsonObject.put("defaultValue",value);
    }
    public void warpDateDefaultValueField(int index,JSONArray columnsArray,JSONObject formFieldObject ){
        JSONObject jsonObject = columnsArray.getJSONObject(index);
        String value = formFieldObject.getString(jsonObject.getString("id"));
        jsonObject.put("dateDefaultValue",value);
    }

    public void warpDateRangeDefaultValueField(int index,JSONArray columnsArray,JSONObject formFieldObject ){
        JSONObject jsonObject = columnsArray.getJSONObject(index);
        String value = formFieldObject.getString(jsonObject.getString("id"));
        jsonObject.put("dateRangeDefaultValue",value);
    }
    public void warpCheckedValueField(int index,JSONArray columnsArray,JSONObject formFieldObject,FormFieldTypeEnums formFieldTypeEnums){
        JSONObject jsonObject = columnsArray.getJSONObject(index);
        String value = formFieldObject.getString(jsonObject.getString("id"));
        if(formFieldTypeEnums.getCode().equals(FormFieldTypeEnums.SELECT.getCode())){
            jsonObject.put("remoteDefaultValue",value);
        }
        JSONArray optionsArray= jsonObject.getJSONArray("options");
        for(int k=0;k<optionsArray.size();k++){
            JSONObject optionObject = optionsArray.getJSONObject(k);
            //值相等则设置为checked
            if(optionObject.get("value").equals(value)){
                optionObject.put("checked",true);
            }else {
                optionObject.put("checked",false);
            }
        }
    }
}
