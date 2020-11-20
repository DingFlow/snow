package com.snow.flowable.controller;

import com.snow.flowable.service.FlowableUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/20 10:34
 */
@RestController
@RequestMapping("/flowableUser")
public class FlowableUserController {

    @Autowired
    private FlowableUserService flowableUserService;


    @RequestMapping(value = "/getUserList",method = RequestMethod.GET)
    public void getFlowableUserList() {

        flowableUserService.getFlowableUserList();
    }
}
