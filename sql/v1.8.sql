
#2021-07-08
#系统配置
INSERT INTO `sys_config`(`config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( '微信公众号AppSecret', 'wx.gzh.appSecret', '92d005b63242ef3ae1e286f78cd8ef34', 'Y', 'admin', NOW(), '', NULL, '微信公众号AppSecret');
INSERT INTO `sys_config`(`config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( '微信公众号AppId', 'wx.gzh.appId', 'wxf847d7ba2539a307', 'Y', 'admin', NOW(), '', NULL, '微信公众号AppId');

#SQL
ALTER TABLE `sys_notice`
    CHANGE COLUMN `blackboard_id` `notice_url` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'banner图url' FIRST,
    MODIFY COLUMN `notice_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告类型（1公告 2通知 3banner）' AFTER `notice_title`;

INSERT INTO `sys_dict_type`( `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( '用户类型', 'sys_user_type', '0', 'admin', NOW(), 'ry', NOW(), '用户类型');

INSERT INTO `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '系统用户', '00', 'sys_user_type', 'info', '', 'Y', '0', 'admin',  NOW(), 'ry',  NOW(), '系统用户');
INSERT INTO `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '官网用户', '01', 'sys_user_type', 'primary', '', 'N', '0', 'admin',  NOW(), 'ry',  NOW(), '官网用户');

#2021-07-16

INSERT INTO `sys_message_template`( `template_code`, `template_name`, `template_body`, `template_desc`, `template_type`, `pc_url`, `app_url`, `icon_class`, `template_status`, `is_delete`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ( '1411568525448978432', '拜访日志', '${userName}于${nowTime}向您推送了一条【${enterprice}】的拜访日志，请您及时前往客户管理查看。', '拜访日志', 4, '/system/customer/messageDetail/${id}', '', 'fa  fa-modx', 0, 0, '1',  NOW(), '1', NOW());
INSERT INTO `sys_message_template`( `template_code`, `template_name`, `template_body`, `template_desc`, `template_type`, `pc_url`, `app_url`, `icon_class`, `template_status`, `is_delete`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ( '1415927384573616128', '任务待办', '${startUser}于${startTime}发起的【${processInstance}】审批流程到达【${taskName}】审批节点，现需要您去协助处理。请点击详情前往处理。', '任务待办', 4, '/flow/toTaskDetail?taskId=${taskId}', '', 'fa fa-bullhorn', 0, 0, '1', '2021-07-17 10:09:37', '1', '2021-07-17 19:54:55');
INSERT INTO `sys_message_template`( `template_code`, `template_name`, `template_body`, `template_desc`, `template_type`, `pc_url`, `app_url`, `icon_class`, `template_status`, `is_delete`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ( '1416370192572882944', '流程完结', '您于${startTime}发起的业务单号为【${businessKey}】的【${processInstance}】审批流程现已完结，流程总用时:【${spendTime}】。请点击详情前往查看。', '流程完结', 4, '/flow/myStartProcessDetail?processInstanceId=${processInstanceId}', '', 'fa fa-hourglass-start', 0, 0, '1', '2021-07-17 20:18:23', '1', '2021-07-17 23:07:39');