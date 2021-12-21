package com.snow.web.controller.dingtalk;

import cn.hutool.core.util.ObjectUtil;
import com.dingtalk.api.response.OapiExtcontactListlabelgroupsResponse;
import com.snow.common.annotation.Log;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.dingtalk.model.request.ExtContactUserRequest;
import com.snow.dingtalk.service.ExtContactUserService;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/4 11:01
 */
@Controller
@RequestMapping("/ding/extContactUser")
public class ExtContactUserController extends BaseController {
    private String prefix = "ding/extContactUser";

    @Autowired
    private ExtContactUserService extContactUserService;

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("system:extContactUser:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/extContactUser";
    }

    @RequiresPermissions("system:extContactUser:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo getList() {
        startPage();
        List<ExtContactUserRequest> extContactUserList = extContactUserService.getExtContactUserList(null,null);
        return pageBySubList(extContactUserList);
    }

    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        List<OapiExtcontactListlabelgroupsResponse.OpenLabelGroup> extContactUserLabelGroupsList = extContactUserService.getExtContactUserLabelGroupsList();
        mmap.put("label",extContactUserLabelGroupsList);
        return prefix + "/add";
    }

    @RequiresPermissions("system:extContactUser:add")
    @Log(title = "外部联系人", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ExtContactUserRequest extContactUserRequest)
    {
        String extContactUser = extContactUserService.createExtContactUser(extContactUserRequest);
        return AjaxResult.success(extContactUser);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String userid, ModelMap mmap)
    {
        ExtContactUserRequest extContactUserDetail = extContactUserService.getExtContactUserDetail(userid);
        SysUser sysUser = userService.selectUserByDingUserId(userid);
        if(ObjectUtil.isNotNull(sysUser)){
            extContactUserDetail.setFollowerUserId(sysUser.getUserName());
        }
        mmap.put("extContactUser", extContactUserDetail);
        return prefix + "/edit";
    }


    @RequiresPermissions("system:extContactUser:edit")
    @Log(title = "外部联系人", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ExtContactUserRequest extContactUserRequest)
    {
        return toAjax(extContactUserService.updateExtContactUser(extContactUserRequest));
    }


    @RequiresPermissions("system:extContactUser:remove")
    @Log(title = "外部联系人", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(extContactUserService.deleteExtContactUser(ids));
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:extContactUser:detail")
    public String detail(@PathVariable("id") String userId, ModelMap mmap)
    {
        ExtContactUserRequest extContactUserRequest = extContactUserService.getExtContactUserDetail(userId);
        SysUser sysUser = userService.selectUserByDingUserId(userId);
        if(ObjectUtil.isNotNull(sysUser)){
            extContactUserRequest.setFollowerUserId(sysUser.getUserName());
        }
        mmap.put("extContactUser", extContactUserRequest);
        return prefix + "/detail";
    }
}
