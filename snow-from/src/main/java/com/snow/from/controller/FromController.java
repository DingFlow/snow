package com.snow.from.controller;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.alibaba.fastjson.JSON;
import com.snow.common.core.domain.AjaxResult;

import com.snow.from.model.FromInfoDTO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-19 15:19
 **/
@Controller
@RequestMapping("/from/interface")
public class FromController {


    @GetMapping()
    public String fromIndex()
    {
        return "/fromIndex";
    }

    /**
     * 预览
     * @return
     */
    @GetMapping("/fromPreview")
    public String fromPreview()
    {
        return "/fromPreview";
    }

    @GetMapping("/canvas")
    public String canvas()
    {
        return "/canvas";
    }


    @GetMapping("/drog")
    public String drog()
    {
        return "/drog";
    }


    @GetMapping("/contextMenuAdd")
    public String contextMenuAdd()
    {
        return "/contextMenuAdd";
    }


    @GetMapping("/codemirror")
    public String codemirror()
    {
        return "/common/codemirror";
    }

    @GetMapping("/attributesArr")
    public String attributesArr()
    {
        return "/common/attributesArr";
    }


    /**
     * 编写CSS
     * @return
     */
    @GetMapping("/codeMirrorCss")
    public String codeMirrorCss()
    {
        return "/common/codeMirrorCss";
    }


    @GetMapping("/codeMirrorHtml")
    public String codeMirrorHtml()
    {
        return "/common/codeMirrorHtml";
    }


    /**
     * 更多设置
     * @return
     */
    @GetMapping("/moreSettings")
    public String moreSettings()
    {
        return "/common/moreSettings";
    }

    @GetMapping("/checkbox_settings")
    public String checkbox_settings()
    {
        return "/common/checkbox_settings";
    }

    @GetMapping("/grid_settings")
    public String grid_settings()
    {
        return "/common/grid_settings";
    }

    @GetMapping("/radio_settings")
    public String radio_settings()
    {
        return "/common/radio_settings";
    }

    @GetMapping("/select_settings")
    public String select_settings()
    {
        return "/common/select_settings";
    }




    /**
     *
     * 保存表单数据
     * @param fromInfoDTO
     * @return
     */
    @PostMapping("/saveFromInfo")
    @ResponseBody
    public AjaxResult saveFromInfo(@RequestBody FromInfoDTO fromInfoDTO)
    {

        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(7100);
        timedCache.put("fromInfoData",JSON.toJSONString(fromInfoDTO));
        return AjaxResult.success(fromInfoDTO);
    }


    @GetMapping("/getFromInfo")
    @ResponseBody
    public AjaxResult getFromInfo()
    {
        String f="{\"ortumJson\":[{\"frame\":\"Bootstrap\",\"children\":[],\"componentKey\":\"inputDom\",\"name\":\"input_1616296539862130a\",\"title\":\"名称\",\"html\":\"<div class=\\\"form-group ortum_item ortum_bootstrap_input row\\\" data-frame=\\\"Bootstrap\\\" data-componentkey=\\\"inputDom\\\" ortum_uuid=\\\"3a50665e-ed1f-8dc6-b804-aa6e4fe8a0c3\\\"> <label class=\\\"col-form-label col-2 ortum_display_NONE\\\">名称</label><input type=\\\"text\\\" title=\\\"名称\\\" name=\\\"input_1616296539862130a\\\" class=\\\"form-control col\\\" placeholder=\\\"请输入\\\" autocomplete=\\\"off\\\"></div>\",\"componentProperties\":\"{\\\"data\\\":\\\"{\\\\\\\"id\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"name\\\\\\\":\\\\\\\"input_1616296539862130a\\\\\\\",\\\\\\\"defaultVal\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"verification\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"authority\\\\\\\":\\\\\\\"edit\\\\\\\",\\\\\\\"placeholder\\\\\\\":\\\\\\\"请输入\\\\\\\",\\\\\\\"cssClass\\\\\\\":\\\\\\\"form-control col\\\\\\\",\\\\\\\"hideLabel\\\\\\\":true,\\\\\\\"labelName\\\\\\\":\\\\\\\"名称\\\\\\\",\\\\\\\"title\\\\\\\":\\\\\\\"名称\\\\\\\",\\\\\\\"labelPosition\\\\\\\":\\\\\\\"rowLeft\\\\\\\",\\\\\\\"labelCSS\\\\\\\":\\\\\\\"col-form-label col-2 ortum_display_NONE\\\\\\\",\\\\\\\"onBefore\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"onAfter\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"onClick\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"onBlur\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"onInput\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"uuid\\\\\\\":\\\\\\\"3a50665e-ed1f-8dc6-b804-aa6e4fe8a0c3\\\\\\\",\\\\\\\"attributesArr\\\\\\\":\\\\\\\"[\\\\\\\\\\\\\\\"{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"label\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"autocomplete\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"off\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}\\\\\\\\\\\\\\\"]\\\\\\\"}\\\",\\\"inputChange\\\":\\\"[\\\\\\\"id\\\\\\\",\\\\\\\"name\\\\\\\",\\\\\\\"defaultVal\\\\\\\",\\\\\\\"verification\\\\\\\",\\\\\\\"placeholder\\\\\\\",\\\\\\\"cssClass\\\\\\\",\\\\\\\"labelName\\\\\\\",\\\\\\\"labelCSS\\\\\\\",\\\\\\\"title\\\\\\\"]\\\",\\\"clickChange\\\":\\\"[\\\\\\\"authority\\\\\\\",\\\\\\\"hideLabel\\\\\\\",\\\\\\\"labelPosition\\\\\\\"]\\\",\\\"purview\\\":\\\"{\\\\\\\"id\\\\\\\":3,\\\\\\\"name\\\\\\\":3,\\\\\\\"defaultVal\\\\\\\":3,\\\\\\\"verification\\\\\\\":3,\\\\\\\"authority\\\\\\\":3,\\\\\\\"placeholder\\\\\\\":3,\\\\\\\"cssClass\\\\\\\":3,\\\\\\\"hideLabel\\\\\\\":3,\\\\\\\"labelName\\\\\\\":3,\\\\\\\"labelPosition\\\\\\\":3,\\\\\\\"labelCSS\\\\\\\":3,\\\\\\\"title\\\\\\\":3}\\\",\\\"dataShowType\\\":\\\"{\\\\\\\"hideLabel\\\\\\\":\\\\\\\"switch\\\\\\\",\\\\\\\"authority\\\\\\\":\\\\\\\"checkbox\\\\\\\",\\\\\\\"labelPosition\\\\\\\":\\\\\\\"checkbox\\\\\\\"}\\\"}\",\"ortumChildren\":null,\"script\":\"\"}],\"ortumSet\":{}}";

        return AjaxResult.success(f);
    }
}
