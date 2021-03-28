package com.snow.web.controller.system;

import cn.hutool.core.date.DateUtil;
import com.snow.common.config.Global;
import com.snow.common.constant.ShiroConstants;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.text.Convert;
import com.snow.common.enums.DingFlowTaskType;
import com.snow.common.utils.CookieUtils;
import com.snow.common.utils.DateUtils;
import com.snow.common.utils.ServletUtils;
import com.snow.common.utils.StringUtils;
import com.snow.flowable.domain.FlowGeneralSituationVO;
import com.snow.flowable.domain.ProcessInstanceDTO;
import com.snow.flowable.domain.ProcessInstanceVO;
import com.snow.flowable.service.FlowableService;
import com.snow.framework.shiro.service.SysPasswordService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.*;
import com.snow.system.service.*;
import com.snow.system.service.impl.FinanceAlipayFlowServiceImpl;
import com.snow.system.service.impl.SysDingtalkSyncLogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 首页 业务处理
 * 
 * @author snow
 */
@Controller
public class SysIndexController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysConfigService configService;


    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private FlowableService flowableService;

    @Autowired
    private SysDingtalkSyncLogServiceImpl sysDingtalkSyncLogService;

    @Autowired
    private FinanceAlipayFlowServiceImpl financeAlipayFlowService;

    @Autowired
    private ISysOperLogService operLogService;

    @Autowired
    private ISysDingHiTaskService iSysDingHiTaskService;

    @Autowired
    private ISysDingRuTaskService sysDingRuTaskService;


    @Autowired
    private ISysDingProcinstService iSysDingProcinstService;

    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap)
    {


        // 取身份信息
        SysUser user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单
        List<SysMenu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
        mmap.put("ignoreFooter", configService.selectConfigByKey("sys.index.ignoreFooter"));
        mmap.put("copyrightYear", Global.getCopyrightYear());
        mmap.put("demoEnabled",  Global.isDemoEnabled());
        mmap.put("isDefaultModifyPwd", initPasswordIsModify(user.getPwdUpdateDate()));
        mmap.put("isPasswordExpired", passwordIsExpiration(user.getPwdUpdateDate()));

        // 菜单导航显示风格
        String menuStyle = configService.selectConfigByKey("sys.index.menuStyle");
        // 移动端，默认使左侧导航菜单，否则取默认配置
        String indexStyle = ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent")) ? "index" : menuStyle;

        // 优先Cookie配置导航菜单
        Cookie[] cookies = ServletUtils.getRequest().getCookies();
        for (Cookie cookie : cookies)
        {
            if (StringUtils.isNotEmpty(cookie.getName()) && "nav-style".equalsIgnoreCase(cookie.getName()))
            {
                indexStyle = cookie.getValue();
                break;
            }
        }
        String webIndex = "topnav".equalsIgnoreCase(indexStyle) ? "index-topnav" : "index";
        return webIndex;
    }


    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        mmap.put("version", Global.getVersion());
        //流程概况
        FlowGeneralSituationVO flowGeneralSituation = flowableService.getFlowGeneralSituation(String.valueOf(sysUser.getUserId()));
        mmap.put("flowGeneralSituation",flowGeneralSituation);
        return "main_v1";
    }

    // 锁定屏幕
    @GetMapping("/lockscreen")
    public String lockscreen(ModelMap mmap)
    {
        mmap.put("user", ShiroUtils.getSysUser());
        ServletUtils.getSession().setAttribute(ShiroConstants.LOCK_SCREEN, true);
        return "lock";
    }

    // 解锁屏幕
    @PostMapping("/unlockscreen")
    @ResponseBody
    public AjaxResult unlockscreen(String password)
    {
        SysUser user = ShiroUtils.getSysUser();
        if (StringUtils.isNull(user))
        {
            return AjaxResult.error("服务器超时，请重新登陆");
        }
        if (passwordService.matches(user, password))
        {
            ServletUtils.getSession().removeAttribute(ShiroConstants.LOCK_SCREEN);
            return AjaxResult.success();
        }
        return AjaxResult.error("密码不正确，请重新输入。");
    }

    // 切换主题
    @GetMapping("/system/switchSkin")
    public String switchSkin()
    {
        return "skin";
    }

    // 切换菜单
    @GetMapping("/system/menuStyle/{style}")
    public void menuStyle(@PathVariable String style, HttpServletResponse response)
    {
        CookieUtils.setCookie(response, "nav-style", style);
    }



    // 检查初始密码是否提醒修改
    public boolean initPasswordIsModify(Date pwdUpdateDate)
    {
        Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
        return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
    }

    // 检查密码是否过期
    public boolean passwordIsExpiration(Date pwdUpdateDate)
    {
        Integer passwordValidateDays = Convert.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
        if (passwordValidateDays != null && passwordValidateDays > 0)
        {
            if (StringUtils.isNull(pwdUpdateDate))
            {
                // 如果从未修改过初始密码，直接提醒过期
                return true;
            }
            Date nowDate = DateUtils.getNowDate();
            return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
        }
        return false;
    }

    /**
     * 数据大屏  （后面改成接口的形式获取）
     * @param mmap
     * @return
     */
    @GetMapping("/system/bigScreen")
    public String bigScreen(ModelMap mmap)
    {
        SysUser user = ShiroUtils.getSysUser();
        //流程概况
        FlowGeneralSituationVO flowGeneralSituation = flowableService.getFlowGeneralSituation(String.valueOf(user.getUserId()));
        mmap.put("flowGeneralSituation",flowGeneralSituation);
        //钉钉概况
        SysDingtalkSyncLog sysDingtalkSyncLog=new SysDingtalkSyncLog();
        SysDingtalkSyncSituationVO sysDingtalkSyncSituation = sysDingtalkSyncLogService.getSysDingtalkSyncSituation(sysDingtalkSyncLog);
        mmap.put("DingTalkSituation",sysDingtalkSyncSituation);
        //账单概况
        FinanceBillSituationVO financeAlipayFlowSituation = financeAlipayFlowService.getFinanceAlipayFlowSituation(user.getUserId());
        mmap.put("financeBillSituation",financeAlipayFlowSituation);
        //暂时先存放操作失败的日志
        SysOperLog sysOperLog=new SysOperLog();
        sysOperLog.setStatus(1);
        List<SysOperLog> sysOperLogs = operLogService.selectOperLogList(sysOperLog);
        mmap.put("sysOperLogs",sysOperLogs);
        ProcessInstanceDTO processInstanceDTO=new ProcessInstanceDTO();
        //获取当天发起的流程
        processInstanceDTO.setStartedAfter(DateUtils.parseDate(DateUtils.getDate()+"00:00:00"));
        processInstanceDTO.setStartedBefore(DateUtils.parseDate(DateUtils.getDate()+"23:59:59"));
        List<ProcessInstanceVO> historicProcessInstanceList = flowableService.getHistoricProcessInstanceList(processInstanceDTO);
        mmap.put("historicProcessInstanceList",historicProcessInstanceList);
        return "big_screen";
    }


    public Integer getMyDingTalkRunTask(){
        SysUser user = ShiroUtils.getSysUser();

        SysDingRuTask sysDingRuTask=new SysDingRuTask();
        sysDingRuTask.setAssignee(user.getDingUserId());
        sysDingRuTask.setTaskState(DingFlowTaskType.RUNNING.getCode());
        List<SysDingRuTask> sysDingRuTaskList = sysDingRuTaskService.selectSysDingRuTaskList(sysDingRuTask);

        return sysDingRuTaskList.size();
    }
}
