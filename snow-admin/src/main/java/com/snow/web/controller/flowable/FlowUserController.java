package com.snow.web.controller.flowable;

import com.snow.common.core.controller.BaseController;
import com.snow.common.core.page.TableDataInfo;
import com.snow.flowable.domain.FlowRemoteVO;
import com.snow.flowable.service.FlowableUserService;
import org.flowable.ui.common.model.RemoteGroup;
import org.flowable.ui.common.model.RemoteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Agee
 * @Title:  流程编译器用户和用户组
 * @Description:
 * @date 2020/11/20 10:34
 */
@RestController
@RequestMapping("/app")
public class FlowUserController extends BaseController {

    @Autowired
    private FlowableUserService flowableUserService;

    /**
     * 编译器登录
     *   集成flowable官方编译器，指定特定用户
     * @return 用户信息
     */
    @RequestMapping(value="/rest/account")
    public String getAccount() {
        return "{\"id\":\"admin\",\"firstName\":\"Test\",\"lastName\":\"Administrator\",\"email\":\"admin@modeler.org\",\"fullName\":\"Test Administrator\",\"groups\":[],\"privileges\":[\"access-idm\",\"access-task\",\"access-modeler\",\"access-admin\"]}\n" +
                "";
    }

    /**
     * 获取用户列表
     *   流程编译器分配用户时查询
     * @param filter
     */
    @GetMapping(value = "/rest/getUserList")
    public FlowRemoteVO<RemoteUser> getFlowUserList(@RequestParam(value = "filter",required = false) String filter) {
        startPage();
        List<RemoteUser> flowUserList = flowableUserService.getFlowUserList(filter);
        TableDataInfo dataTable = getDataTable(flowUserList);
        FlowRemoteVO flowRemoteVO = new FlowRemoteVO<>();
        flowRemoteVO.setData(dataTable.getRows());
        flowRemoteVO.setSize(dataTable.getPageSize());
        flowRemoteVO.setStart(dataTable.getPageIndex());
        flowRemoteVO.setTotal(dataTable.getTotal());
        return flowRemoteVO;
    }

    /**
     * 获取用户组列表
     * @param filter
     */
    @GetMapping(value = "/rest/getUserGroupList")
    public FlowRemoteVO getUserGroupList(@RequestParam(value = "filter",required = false) String filter) {

        List<RemoteGroup> flowUserGroupList = flowableUserService.getLinkFlowUserGroupList(filter);

        FlowRemoteVO flowRemoteVO = new FlowRemoteVO();
        flowRemoteVO.setData(flowUserGroupList);
       // flowRemoteVO.setSize(dataTable.getPageSize());
      //  flowRemoteVO.setStart(dataTable.getPageIndex());
        flowRemoteVO.setTotal(flowUserGroupList.size());
        return flowRemoteVO;
    }
}
