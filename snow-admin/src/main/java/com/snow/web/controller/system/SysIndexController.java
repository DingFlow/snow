package com.snow.web.controller.system;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.snow.common.config.Global;
import com.snow.common.constant.ShiroConstants;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.text.Convert;
import com.snow.common.enums.MessageReadStatus;
import com.snow.common.enums.NoticeType;
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
import com.snow.system.service.ISysConfigService;
import com.snow.system.service.ISysMenuService;
import com.snow.system.service.ISysMessageTransitionService;
import com.snow.system.service.ISysOperLogService;
import com.snow.system.service.impl.FinanceAlipayFlowServiceImpl;
import com.snow.system.service.impl.SysDingtalkSyncLogServiceImpl;
import com.snow.system.service.impl.SysNoticeServiceImpl;
import com.snow.system.service.impl.SysOaEmailServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * ?????? ????????????
 * 
 * @author snow
 */
@Controller
public class SysIndexController extends BaseController {
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
    private ISysMessageTransitionService sysMessageTransitionService;

    @Autowired
    private SysOaEmailServiceImpl sysOaEmailService;

    @Autowired
    private SysNoticeServiceImpl sysNoticeService;

    @Value("${is.notice}")
    private Boolean isNotice;


    // ????????????
    @GetMapping("/index")
    public String index(ModelMap mmap) {
        // ???????????????
        SysUser user = ShiroUtils.getSysUser();
        // ????????????id????????????
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

        mmap.put("isNewNotice",isNotice);
        List<SysOaEmailDTO> myNoReadOaEmailList = sysOaEmailService.getMyNoReadOaEmailList(ShiroUtils.getUserIdAsString());
        mmap.put("emailListSize",myNoReadOaEmailList.size());
        //???????????????????????????????????????
        if(CollectionUtils.isNotEmpty(myNoReadOaEmailList)&&myNoReadOaEmailList.size()>3){
            myNoReadOaEmailList=myNoReadOaEmailList.subList(0,3);
        }

        mmap.put("emailList",myNoReadOaEmailList);

        SysMessageTransition sysMessageTransition=new SysMessageTransition();
        sysMessageTransition.setConsumerId(String.valueOf(user.getUserId()));
        sysMessageTransition.setMessageStatus(0L);
        sysMessageTransition.setMessageReadStatus(MessageReadStatus.NO_READ.getCode());
        sysMessageTransition.setOrderBy("update_time desc");
        List<SysMessageTransition> sysMessageTransitions = sysMessageTransitionService.selectSysMessageTransitionList(sysMessageTransition);
        //???????????????????????????????????????
        if(CollUtil.isNotEmpty(sysMessageTransitions)&&sysMessageTransitions.size()>5){
            sysMessageTransitions=sysMessageTransitions.subList(0,5);

        }
        SysMessageTransition.init(sysMessageTransitions);
        mmap.put("sysMessageList",sysMessageTransitions);
        mmap.put("sysMessageSize",sysMessageTransitions.size());
        // ????????????????????????
        String menuStyle = configService.selectConfigByKey("sys.index.menuStyle");
        // ???????????????????????????????????????????????????????????????
        String indexStyle = ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent")) ? "index" : menuStyle;

        // ??????Cookie??????????????????
        Cookie[] cookies = ServletUtils.getRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (StringUtils.isNotEmpty(cookie.getName()) && "nav-style".equalsIgnoreCase(cookie.getName())) {
                indexStyle = cookie.getValue();
                break;
            }
        }
        String webIndex = "topnav".equalsIgnoreCase(indexStyle) ? "index-topnav" : "index";
        return webIndex;
    }


    // ????????????
    @GetMapping("/system/main")
    public String main(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        //????????????
        FlowGeneralSituationVO flowGeneralSituation = flowableService.getFlowGeneralSituation(ShiroUtils.getUserIdAsString());
        mmap.put("flowGeneralSituation",flowGeneralSituation);
        SysNotice sysNotice=new SysNotice();
        sysNotice.setStatus("0");
        sysNotice.setNoticeType(NoticeType.NOTICE_TYPE.getCode());
        List<SysNotice> sysNotices = sysNoticeService.selectNoticeList(sysNotice);
        mmap.put("sysNotices",sysNotices);
        return "main";
    }

    // ????????????
    @GetMapping("/lockscreen")
    public String lockscreen(ModelMap mmap) {
        mmap.put("user", ShiroUtils.getSysUser());
        ServletUtils.getSession().setAttribute(ShiroConstants.LOCK_SCREEN, true);
        return "lock";
    }

    // ????????????
    @PostMapping("/unlockscreen")
    @ResponseBody
    public AjaxResult unlockscreen(String password) {
        SysUser user = ShiroUtils.getSysUser();
        if (ObjectUtil.isNull(user)) {
            return AjaxResult.error("?????????????????????????????????");
        }
        if (passwordService.matches(user, password)) {
            ServletUtils.getSession().removeAttribute(ShiroConstants.LOCK_SCREEN);
            return AjaxResult.success();
        }
        return AjaxResult.error("????????????????????????????????????");
    }

    // ????????????
    @GetMapping("/system/switchSkin")
    public String switchSkin()
    {
        return "skin";
    }

    // ????????????
    @GetMapping("/system/menuStyle/{style}")
    public void menuStyle(@PathVariable String style, HttpServletResponse response) {
        CookieUtils.setCookie(response, "nav-style", style);
    }



    // ????????????????????????????????????
    public boolean initPasswordIsModify(Date pwdUpdateDate) {
        Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
        return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
    }

    // ????????????????????????
    public boolean passwordIsExpiration(Date pwdUpdateDate) {
        Integer passwordValidateDays = Convert.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
        if (passwordValidateDays != null && passwordValidateDays > 0) {
            if (StringUtils.isNull(pwdUpdateDate)) {
                // ??????????????????????????????????????????????????????
                return true;
            }
            Date nowDate = DateUtils.getNowDate();
            return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
        }
        return false;
    }

    /**
     * ????????????  ???????????????????????????????????????
     * @param mmap
     * @return
     */
    @GetMapping("/system/bigScreen")
    public String bigScreen(ModelMap mmap) {
        SysUser user = ShiroUtils.getSysUser();
        //????????????
        FlowGeneralSituationVO flowGeneralSituation = flowableService.getFlowGeneralSituation(String.valueOf(user.getUserId()));
        mmap.put("flowGeneralSituation",flowGeneralSituation);
        //????????????
        SysDingtalkSyncLog sysDingtalkSyncLog=new SysDingtalkSyncLog();
        SysDingtalkSyncSituationVO sysDingtalkSyncSituation = sysDingtalkSyncLogService.getSysDingtalkSyncSituation(sysDingtalkSyncLog);
        mmap.put("DingTalkSituation",sysDingtalkSyncSituation);
        //????????????
        FinanceBillSituationVO financeAlipayFlowSituation = financeAlipayFlowService.getFinanceAlipayFlowSituation(user.getUserId());
        mmap.put("financeBillSituation",financeAlipayFlowSituation);
        //????????????????????????????????????
        SysOperLog sysOperLog=new SysOperLog();
        sysOperLog.setStatus(1);
        List<SysOperLog> sysOperLogs = operLogService.selectOperLogList(sysOperLog);
        mmap.put("sysOperLogs",sysOperLogs);
        ProcessInstanceDTO processInstanceDTO=new ProcessInstanceDTO();
        //???????????????????????????
        processInstanceDTO.setStartedAfter(DateUtils.parseDate(DateUtils.getDate()+"00:00:00"));
        processInstanceDTO.setStartedBefore(DateUtils.parseDate(DateUtils.getDate()+"23:59:59"));
        processInstanceDTO.setStartedUserId(user.getUserId().toString());
        List<ProcessInstanceVO> historicProcessInstanceList = flowableService.getHistoricProcessInstanceList(processInstanceDTO);
        mmap.put("historicProcessInstanceList",historicProcessInstanceList);
        return "big_screen";
    }
}
