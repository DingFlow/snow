package com.snow.web.controller.front;

import cn.hutool.core.collection.CollectionUtil;
import com.snow.common.config.Global;
import com.snow.common.core.controller.BaseController;
import com.snow.common.enums.MessageReadStatus;
import com.snow.flowable.domain.FlowGeneralSituationVO;
import com.snow.flowable.service.FlowableService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysMenu;
import com.snow.system.domain.SysMessageTransition;
import com.snow.system.domain.SysNotice;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysMessageTransitionService;
import com.snow.system.service.ISysNoticeService;
import com.snow.system.service.impl.SysMenuServiceImpl;
import com.snow.system.service.impl.SysNoticeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private ISysNoticeService sysNoticeService;

    private String prefix = "front";


    // 前台首页
    @GetMapping("/index")
    public String index(ModelMap mmap){
        //获取新闻
        SysNotice sysNotice=new SysNotice();
        sysNotice.setStatus("0");
        sysNotice.setNoticeType("3");
        List<SysNotice> sysNotices = sysNoticeService.selectNoticeList(sysNotice);
        mmap.put("newsList", sysNotices);
        mmap.put("version", Global.getVersion());
        mmap.put("copyrightYear", Global.getCopyrightYear());
        return prefix + "/index";
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

    /**
     * 跳转新闻详情也
     * @param id 新闻id
     * @param mmap
     * @return 跳转的页面
     */
    @GetMapping("/news/{id}")
    public String newsDetail(@PathVariable("id") Long id, ModelMap mmap) {
        SysNotice sysNotice = sysNoticeService.selectNoticeById(id);
        mmap.put("sysNotice",sysNotice);
        return prefix+ "/news/common";
    }

}
