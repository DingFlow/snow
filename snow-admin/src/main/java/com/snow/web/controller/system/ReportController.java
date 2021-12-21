package com.snow.web.controller.system;

import com.snow.common.core.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-04-27 20:31
 **/

@Controller
@RequestMapping("/report")
@Slf4j
public class ReportController extends BaseController
{

    private String prefix = "/jmreport";


     /*
     * 报表设计器
     * @return
     **/

    @RequiresPermissions("system:jmreport:design")
    @GetMapping("/design")
    public String reportDesign()
    {

        return redirect(prefix+"/list");
    }

}
