
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

CREATE TABLE `sys_oa_task` (
                               `task_no` varchar(64)  NOT NULL COMMENT '任务编号',
                               `task_name` varchar(128) NOT NULL COMMENT '任务名称',
                               `task_content` text  COMMENT '任务内容',
                               `priority` int(11) NOT NULL DEFAULT '20' COMMENT '紧急程度（详见数据字典）',
                               `expected_time` datetime DEFAULT NULL COMMENT '预期完成时间',
                               `task_status` varchar(32)  DEFAULT NULL COMMENT '任务状态（详见数据字典）',
                               `task_type` varchar(32)  DEFAULT NULL COMMENT '任务类型（详见数据字典）',
                               `task_source` varchar(128) DEFAULT NULL COMMENT '任务来源',
                               `task_outside_id` varchar(64) DEFAULT NULL COMMENT '任务外部id',
                               `task_url` varchar(128)  DEFAULT NULL COMMENT '任务跳转URL',
                               `revision` int(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
                               `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                               `is_delete` int(11) NOT NULL DEFAULT '0' COMMENT '删除标识',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                               PRIMARY KEY (`task_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统任务表';
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('系统任务', '2145', '1', '/system/task', 'C', '0', 'system:task:view', '#', 'admin', now(), 'ry', now(), '系统任务菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ( '系统任务', @parentId , 1, '/system/task', '', 'C', '0', 'system:task:view', '#', 'admin', NOW(), 'ry', NOW(), '系统任务菜单', 1);
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ( '开始任务', @parentId , 1, '#', 'menuItem', 'F', '0', 'system:task:start', '#', 'admin', NOW(), 'admin', NOW(), '', 1);
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ( '系统任务新增', @parentId , 2, '#', '', 'F', '0', 'system:task:add', '#', 'admin', NOW(), 'ry', NOW(), '', 1);
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ( '系统任务修改', @parentId , 3, '#', '', 'F', '0', 'system:task:edit', '#', 'admin',  NOW(), 'ry',  NOW(), '', 1);
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ('系统任务删除', @parentId , 4, '#', '', 'F', '0', 'system:task:remove', '#', 'admin', NOW(), 'ry',  NOW(), '', 1);
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ('系统任务导出', @parentId , 5, '#', '', 'F', '0', 'system:task:export', '#', 'admin',  NOW(), 'ry',  NOW(), '', 1);
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ('待处理任务', @parentId , 1, '#', 'menuItem', 'F', '0', 'system:task:waitList', '#', 'admin',  NOW(), '', NULL, '', 1);
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ('我创建的任务', @parentId , 2, '#', 'menuItem', 'F', '0', 'system:task:myList', '#', 'admin',  NOW(), '', NULL, '', 1);
INSERT INTO `sys_menu`(`menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ( '我处理的任务', @parentId , 3, '#', 'menuItem', 'F', '0', 'system:task:handleList', '#', 'admin',  NOW(), '', NULL, '', 1);
INSERT INTO `sys_menu`(`menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ('完成任务', @parentId , 4, '#', 'menuItem', 'F', '0', 'system:task:handle', '#', 'admin',  NOW(), '', NULL, '', 1);
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ('任务详情', @parentId , 6, '#', 'menuItem', 'F', '0', 'system:task:detail', '#', 'admin', NOW(), '', NULL, '', 1);

INSERT INTO  `sys_dict_type`(`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('任务类型', 'sys_task_type', '0', 'admin', NOW(), '', NULL, '任务类型');
INSERT INTO `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( 1, '系统任务', '1', 'sys_task_type', NULL, 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '待办任务');

--20211015
INSERT INTO `sys_sequence`(`name`, `current_value`, `increment`, `described`) VALUES ('OA_RW', 1, 1, '系统任务单号');


--20211108

-- 按钮父菜单ID
--联系人菜单
INSERT INTO `sys_menu`(`menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`,  `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ('外部联系人', 2019, 6, '/ding/extContactUser', 'menuItem', 'C', '0', 'system:extContactUser:view',  'admin', '2021-11-04 13:53:50', '', NULL, '', 1);

SELECT @parentId := LAST_INSERT_ID();
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ( '联系人列表', @parentId , 1, '#', 'menuItem', 'F', '0', 'system:extContactUser:list', '#', 'admin', NOW(), '', NULL, '', 1);
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ( '新增联系人', @parentId , 2, '#', 'menuItem', 'F', '0', 'system:extContactUser:add', '#', 'admin', NOW(), '', NULL, '', 1);
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ( '编辑联系人', @parentId , 3, '#', 'menuItem', 'F', '0', 'system:extContactUser:edit', '#', 'admin', NOW(), 'admin', NULL, '', 1);
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ( '移除联系人', @parentId , 4, '#', 'menuItem', 'F', '0', 'system:extContactUser:remove', '#', 'admin', NOW(), '', NULL, '', 1);
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `is_refresh`) VALUES ( '联系人详情', @parentId , 5, '#', 'menuItem', 'F', '0', 'system:extContactUser:detail', '#', 'admin', NOW(), '', NULL, '', 1);

INSERT INTO `sys_message_template`( `template_code`, `template_name`, `template_body`, `template_desc`, `template_type`, `pc_url`, `app_url`, `icon_class`, `template_status`, `is_delete`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ( '1457582416733544448', '站内系统待办任务', '${startUser}于${startTime}发起单号为【${businessKey}】的站内待办任务，现需要您去协助处理。请点击详情前往处理。', '站内待办', 4, '/system/task/taskDistributedDetail?id=${id}', '', 'fa fa-balance-scale', 0, 0, '1', NOW(), '1', NOW());
INSERT INTO `sys_message_template`( `template_code`, `template_name`, `template_body`, `template_desc`, `template_type`, `pc_url`, `app_url`, `icon_class`, `template_status`, `is_delete`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ( '1457591320074919936', '完结系统待办任务', '${startUser}于${startTime}完成单号为【${businessKey}】的站内待办任务，可前往查看具体详情。', '系统待办任务完结', 4, '/system/task/taskDistributedDetail?id=${id}', '', 'fa fa-flag-o', 0, 0, '1', NOW(), '1', NOW());

ALTER TABLE `finance_alipay_flow`
    ADD COLUMN `real_finance_type` int(11) NULL COMMENT '真实财务用途（该条账单所属账务企业真实类型）' AFTER `bill_type`,
    ADD COLUMN `real_income_expenditure_type` int(11) NULL COMMENT '真实收支类型' AFTER `real_finance_type`;


INSERT INTO `sys_dict_type`(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (134, '账单真实收支类型', 'finance_real_sz_type', '0', 'admin', NOW(), '', NULL, '账单真实收支类型');
INSERT INTO `sys_dict_type`(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (135, '财务用途', 'real_finance_type', '0', 'admin', NOW(), '', NULL, '财务用途');


INSERT INTO `sys_config`(`config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( '系统网址', 'sys.website.domain', 'http://dingflow.xyz:9527', 'Y', 'admin',NOW(), '', NULL, '系统网址');