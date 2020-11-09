package com.snow.dingtalk.service.impl;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserCreateRequest;
import com.dingtalk.api.response.OapiV2UserCreateResponse;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.service.UserService;
import com.snow.system.domain.SysUser;
import com.taobao.api.ApiException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/9 10:51
 */
@Service
public class UserServiceImpl  extends BaseService implements UserService {

    @Override
    public String create(SysUser sysUser) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.USER_CREATE);
        OapiV2UserCreateRequest req = new OapiV2UserCreateRequest();
        req.setUserid(String.valueOf(sysUser.getUserId()));
        req.setName(sysUser.getUserName());
        req.setMobile(sysUser.getPhonenumber());
        req.setHideMobile(false);
        //req.setTelephone(sysUser.getPhonenumber());
        req.setJobNumber(sysUser.getJobnumber());
        req.setTitle("技术总监");
        req.setEmail("test@xxx.com");
        req.setOrgEmail("test@xxx.com");
        req.setWorkPlace("未来park");
        req.setRemark("备注备注");
        req.setDeptIdList("\"2,3,4\"");
        List<OapiV2UserCreateRequest.DeptOrder> list2 = new ArrayList<>();
        OapiV2UserCreateRequest.DeptOrder obj3 = new OapiV2UserCreateRequest.DeptOrder();
        list2.add(obj3);
        obj3.setDeptId(2L);
        obj3.setOrder(1L);
        req.setDeptOrderList(list2);
        List<OapiV2UserCreateRequest.DeptTitle> list5 = new ArrayList<>();
        OapiV2UserCreateRequest.DeptTitle obj6 = new OapiV2UserCreateRequest.DeptTitle();
        list5.add(obj6);
        obj6.setDeptId(2L);
        obj6.setTitle("资深产品经理");
        req.setDeptTitleList(list5);
        req.setExtension("{\"爱好\":\"旅游\",\"年龄\":\"24\"}");
        req.setSeniorMode(false);
        req.setHiredDate(1597573616828L);
        OapiV2UserCreateResponse rsp = null;
        try {
            rsp = client.execute(req, getDingTalkToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
        return null;
    }
}
