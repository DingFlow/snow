
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

#2021-07-23
INSERT INTO `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( 3, '取消', '2', 'process_instance_status', NULL, 'warning', 'N', '0', 'admin', NOW(), '', NOW(), '取消');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ('取消流程', 2043, 3, '#', 'menuItem', 'F', '0', 'cancelProcessInstance', '#', 'admin', NOW(), '', NULL, '', 1);


UPDATE `sys_menu` SET `url` = '/flow/toMyTakePartInTask',  `perms` = 'flow:process:getMyTakePartInTask' WHERE `menu_id` = 2044;

INSERT INTO `sys_menu`(`menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES (
'我的已办详情', (select temp.* from( select menu_id from sys_menu where perms='flow:process:getMyTakePartInTask' ) as temp), 2, '#', 'menuItem', 'F', '0', 'flow:process:myTaskedDetail', '#', 'admin', NOW(), 'admin', NOW(), '', 1);

#2021-07-29

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('系统任务', '2145', '1', '/system/task', 'C', '0', 'system:task:view', '#', 'admin', now(), 'ry', now(), '系统任务菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('系统任务查询', @parentId, '1',  '#',  'F', '0', 'system:task:list',         '#', 'admin', now(), 'ry', now(), '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('系统任务新增', @parentId, '2',  '#',  'F', '0', 'system:task:add',          '#', 'admin', now(), 'ry', now(), '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('系统任务修改', @parentId, '3',  '#',  'F', '0', 'system:task:edit',         '#', 'admin', now(), 'ry', now(), '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('系统任务删除', @parentId, '4',  '#',  'F', '0', 'system:task:remove',       '#', 'admin', now(), 'ry', now(), '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('系统任务导出', @parentId, '5',  '#',  'F', '0', 'system:task:export',       '#', 'admin', now(), 'ry', now(), '');

INSERT INTO `snow-dev`.`sys_dict_type`(`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('任务类型', 'sys_task_type', '0', 'admin', NOW(), '', NULL, '任务类型');
INSERT INTO `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( 1, '系统任务', '1', 'sys_task_type', NULL, 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '待办任务');