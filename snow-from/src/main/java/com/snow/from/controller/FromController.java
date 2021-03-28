package com.snow.from.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @RequiresPermissions("system:instance:add")
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

}
