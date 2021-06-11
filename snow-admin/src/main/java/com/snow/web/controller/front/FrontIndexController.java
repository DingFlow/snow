package com.snow.web.controller.front;

import com.snow.common.config.Global;
import com.snow.common.core.controller.BaseController;
import com.snow.common.utils.ServletUtils;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysMenu;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysMenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author qimingjin
 * @Title: 前台请求控制台
 * @Description:
 * @date 2021/6/9 14:48
 */
@Controller
@RequestMapping("/front")
public class FrontIndexController extends BaseController {

    @Autowired
    private SysMenuServiceImpl sysMenuService;

    private String prefix = "/front";


    // 前台首页
    @GetMapping("/index")
    public String index(ModelMap mmap){

        mmap.put("version", Global.getVersion());
        mmap.put("copyrightYear", Global.getCopyrightYear());
        return prefix + "/index";
    }

    /**
     * 登录
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response)
    {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request))
        {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }

        return prefix+"/login";
    }

    /**
     * 前端主页
     * @param mmap
     * @return
     */
    @GetMapping("/main")
    public String main(ModelMap mmap){
        SysUser sysUser = ShiroUtils.getSysUser();
        mmap.put("user", sysUser);
        mmap.put("version", Global.getVersion());
        mmap.put("copyrightYear", Global.getCopyrightYear());

        // 根据用户id取出菜单
        List<SysMenu> menus = sysMenuService.selectMenusByUser(sysUser);
        mmap.put("menus", menus);
        return prefix + "/main";
    }

    /**
     * 前端home主页
     * @param mmap
     * @return
     */
    @GetMapping("/home")
    public String home(ModelMap mmap){
        SysUser sysUser = ShiroUtils.getSysUser();
        mmap.put("user", sysUser);
        mmap.put("version", Global.getVersion());
        mmap.put("copyrightYear", Global.getCopyrightYear());
        return prefix + "/home";
    }
}
