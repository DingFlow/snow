package com.snow.web.controller.front;

import cn.hutool.core.collection.CollectionUtil;
import com.snow.common.config.Global;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.page.PageModel;
import com.snow.common.enums.MessageReadStatus;
import com.snow.common.utils.ServletUtils;
import com.snow.flowable.domain.FlowGeneralSituationVO;
import com.snow.flowable.domain.HistoricTaskInstanceDTO;
import com.snow.flowable.domain.HistoricTaskInstanceVO;
import com.snow.flowable.service.FlowableService;
import com.snow.framework.shiro.service.SysPasswordService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysMenu;
import com.snow.system.domain.SysMessageTransition;
import com.snow.system.domain.SysNotice;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysConfigService;
import com.snow.system.service.ISysDingRuTaskService;
import com.snow.system.service.ISysMessageTransitionService;
import com.snow.system.service.ISysOperLogService;
import com.snow.system.service.impl.*;
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


    @Autowired
    private FlowableService flowableService;

    @Autowired
    private ISysMessageTransitionService sysMessageTransitionService;


    @Autowired
    private SysNoticeServiceImpl sysNoticeService;

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
     * 跳转注册页
     * @return
     */
    @GetMapping("/register")
    public String register()
    {
        return prefix+"/register";
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
        //获取我的消息数
        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        sysMessageTransition.setConsumerId(String.valueOf(sysUser.getUserId()));
        sysMessageTransition.setMessageReadStatus(MessageReadStatus.NO_READ.getCode());
        List<SysMessageTransition> sysMessageTransitions = sysMessageTransitionService.selectSysMessageTransitionList(sysMessageTransition);
        mmap.put("sysMessageSize", sysMessageTransitions.size());
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
        mmap.put("version", Global.getVersion());
        //流程概况
        FlowGeneralSituationVO flowGeneralSituation = flowableService.getFlowGeneralSituation(String.valueOf(sysUser.getUserId()));
        mmap.put("flowGeneralSituation",flowGeneralSituation);
        SysNotice sysNotice=new SysNotice();
        sysNotice.setStatus("0");
        sysNotice.setNoticeType("1");
        List<SysNotice> sysNotices = sysNoticeService.selectNoticeList(sysNotice);
        mmap.put("sysNotices",sysNotices);
        if(CollectionUtil.isNotEmpty(sysNotices)&&sysNotices.size()>5){
            mmap.put("sysNoticeList",sysNotices.subList(0,5));
        }else {
            mmap.put("sysNoticeList",sysNotices);
        }
        mmap.put("sysNoticeListSize",sysNotices.size());
        return prefix + "/home";
    }


    /**
     * 庆祝共产党成立100周年新闻
     * @return
     */
    @GetMapping("/news/communist_party")
    public String communist_party()
    {
        return prefix+ "/news/communist_party";
    }

}
