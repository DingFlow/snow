package com.snow.from.controller;

import com.alibaba.fastjson.JSON;
import com.snow.common.core.domain.AjaxResult;
import com.snow.from.domain.request.FormRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        return AjaxResult.success();
    }
}
