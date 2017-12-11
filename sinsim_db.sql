/*
Navicat MySQL Data Transfer

Source Server         : sinsim
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : sinsim_db

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2017-12-12 01:19:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `abnormal`
-- ----------------------------
DROP TABLE IF EXISTS `abnormal`;
CREATE TABLE `abnormal` (
  `id` tinyint(4) unsigned NOT NULL AUTO_INCREMENT,
  `abnormal_name` varchar(255) NOT NULL COMMENT '异常名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of abnormal
-- ----------------------------
INSERT INTO `abnormal` VALUES ('1', 'abnormalName111');
INSERT INTO `abnormal` VALUES ('2', 'abn_222');
INSERT INTO `abnormal` VALUES ('3', 'abn333-by-update');
INSERT INTO `abnormal` VALUES ('4', 'abnABC');
INSERT INTO `abnormal` VALUES ('5', 'abn333-by-add');

-- ----------------------------
-- Table structure for `abnormal_image`
-- ----------------------------
DROP TABLE IF EXISTS `abnormal_image`;
CREATE TABLE `abnormal_image` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `abnormal_record_id` int(10) unsigned NOT NULL,
  `image` varchar(255) NOT NULL COMMENT '异常图片名称（包含路径）,以后这部分数据是最大的，首先pad上传时候时候需要压缩，以后硬盘扩展的话，可以把几几年的图片放置到另外一个硬盘，然后pad端响应升级（根据时间加上图片的路径）',
  `create_time` datetime NOT NULL COMMENT '上传异常图片的时间',
  PRIMARY KEY (`id`),
  KEY `fk_ai_abnormal_record_id` (`abnormal_record_id`),
  CONSTRAINT `fk_ai_abnormal_record_id` FOREIGN KEY (`abnormal_record_id`) REFERENCES `abnormal_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of abnormal_image
-- ----------------------------
INSERT INTO `abnormal_image` VALUES ('1', '1', 'img1111', '2017-12-05 13:34:56');
INSERT INTO `abnormal_image` VALUES ('2', '2', 'img22-by-update', '2017-12-05 13:35:42');
INSERT INTO `abnormal_image` VALUES ('3', '2', 'img333', '2017-12-05 13:35:42');
INSERT INTO `abnormal_image` VALUES ('4', '2', 'img444-by-add', '2017-12-05 13:35:42');
INSERT INTO `abnormal_image` VALUES ('5', '3', 'img555', '2017-12-05 14:45:26');
INSERT INTO `abnormal_image` VALUES ('6', '4', 'img666', '2017-12-05 14:45:42');
INSERT INTO `abnormal_image` VALUES ('7', '5', 'img7777', '2017-12-10 10:38:58');
INSERT INTO `abnormal_image` VALUES ('8', '6', 'img888', '2017-12-10 11:05:48');

-- ----------------------------
-- Table structure for `abnormal_record`
-- ----------------------------
DROP TABLE IF EXISTS `abnormal_record`;
CREATE TABLE `abnormal_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `abnormal_type` tinyint(4) unsigned NOT NULL COMMENT '异常类型',
  `task_record_id` int(10) unsigned NOT NULL COMMENT '作业工序',
  `submit_user` int(10) unsigned NOT NULL COMMENT '提交异常的用户ID',
  `comment` text NOT NULL COMMENT '异常备注',
  `solution` text NOT NULL COMMENT '解决办法',
  `solution_user` int(10) unsigned NOT NULL COMMENT '解决问题的用户对应的ID',
  PRIMARY KEY (`id`),
  KEY `fk_ar_abnormal_type` (`abnormal_type`),
  KEY `fk_ar_task_record_id` (`task_record_id`),
  KEY `fk_ar_submit_user` (`submit_user`),
  KEY `fk_ar_solution_user` (`solution_user`),
  CONSTRAINT `fk_ar_abnormal_type` FOREIGN KEY (`abnormal_type`) REFERENCES `abnormal` (`id`),
  CONSTRAINT `fk_ar_solution_user` FOREIGN KEY (`solution_user`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_ar_submit_user` FOREIGN KEY (`submit_user`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_ar_task_record_id` FOREIGN KEY (`task_record_id`) REFERENCES `task_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of abnormal_record
-- ----------------------------
INSERT INTO `abnormal_record` VALUES ('1', '1', '3', '4', 'cmtAaa', 'solutionAAA', '6');
INSERT INTO `abnormal_record` VALUES ('2', '2', '4', '5', 'cmt111-by-update', 'solu3333-by-update', '7');
INSERT INTO `abnormal_record` VALUES ('3', '4', '7', '2', 'cmt333', 'slutionCCC', '3');
INSERT INTO `abnormal_record` VALUES ('4', '2', '4', '5', 'cmt111-by-Add', 'solu3333-by_add', '7');
INSERT INTO `abnormal_record` VALUES ('5', '5', '1', '5', 'cmt5555', 'solustion555', '5');
INSERT INTO `abnormal_record` VALUES ('6', '1', '1', '11', 'cmt6666', 'solution666', '12');

-- ----------------------------
-- Table structure for `contract`
-- ----------------------------
DROP TABLE IF EXISTS `contract`;
CREATE TABLE `contract` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contract_num` varchar(255) NOT NULL COMMENT '合同号',
  `customer_name` varchar(255) NOT NULL COMMENT '客户姓名',
  `sellman` varchar(255) NOT NULL COMMENT '销售人员',
  `create_time` datetime NOT NULL COMMENT '填表时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract
-- ----------------------------
INSERT INTO `contract` VALUES ('2', 'ss123456', '', '', '0000-00-00 00:00:00');

-- ----------------------------
-- Table structure for `contract_sign`
-- ----------------------------
DROP TABLE IF EXISTS `contract_sign`;
CREATE TABLE `contract_sign` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contract_id` int(10) unsigned NOT NULL COMMENT '合同ID',
  `sign_content` text NOT NULL COMMENT '签核内容，以json格式的数组形式存放, 所有项完成后更新status为完成.[ \r\n    {"step_number":1, "role_id": 1, "role_name":"销售经理"，“person”：“张三”，”comment“: "同意"，"resolved":1,”update_time“:"2017-11-05 12:08:55"},\r\n    {"step_number":1,"role_id":2, "role_name":"财务部"，“person”：“李四”，”comment“: "同意，但是部分配件需要新设计"，"resolved":0, ”update_time“:"2017-11-06 12:08:55"}\r\n]',
  `current_step` varchar(255) NOT NULL COMMENT '当前进行中的签核环节（来至于role_name）',
  `status` tinyint(4) NOT NULL COMMENT '签核状态：“1”==>签核中， “2”==>签核完成， “3”==>驳回，“4”==>改单，“5”==>拆单，该条记录在驳回后停止修改，会新创建签核记录',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `fk_cs_contract_id` (`contract_id`),
  CONSTRAINT `fk_cs_contract_id` FOREIGN KEY (`contract_id`) REFERENCES `contract` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract_sign
-- ----------------------------

-- ----------------------------
-- Table structure for `device`
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '设备名称',
  `meid` varchar(255) NOT NULL COMMENT 'MEID地址',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES ('1', '上轴', '12:34:56:78:90');
INSERT INTO `device` VALUES ('2', '下轴', '11:11:22:33:44');

-- ----------------------------
-- Table structure for `install_group`
-- ----------------------------
DROP TABLE IF EXISTS `install_group`;
CREATE TABLE `install_group` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) NOT NULL COMMENT '公司部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of install_group
-- ----------------------------
INSERT INTO `install_group` VALUES ('1', '上轴安装组');
INSERT INTO `install_group` VALUES ('2', '下轴安装组');
INSERT INTO `install_group` VALUES ('3', '驱动安装组');
INSERT INTO `install_group` VALUES ('4', '台板安装组');
INSERT INTO `install_group` VALUES ('5', '电控组');

-- ----------------------------
-- Table structure for `machine`
-- ----------------------------
DROP TABLE IF EXISTS `machine`;
CREATE TABLE `machine` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(10) unsigned NOT NULL COMMENT '对应的order id',
  `machine_id` varchar(255) NOT NULL COMMENT '系统内部维护用的机器编号(年、月、类型、头数、针数、不大于台数的数字)',
  `nameplate` varchar(255) DEFAULT NULL COMMENT '技术部填入的机器编号（铭牌）',
  `location` varchar(255) DEFAULT NULL COMMENT '机器的位置，一般由生产部管理员上传',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '机器状态（“1”==>"初始化"，“2”==>"开始安装"，“3”==>"安装完成"，“4”==>"无效"）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `installed_time` datetime DEFAULT NULL COMMENT '安装完成时间',
  `ship_time` datetime DEFAULT NULL COMMENT '发货时间（如果分批交付，需要用到，否则已订单交付为准）',
  PRIMARY KEY (`id`),
  KEY `idx_m_order_id` (`order_id`) USING BTREE,
  CONSTRAINT `fk_m_order_id` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine
-- ----------------------------
INSERT INTO `machine` VALUES ('1', '1', 'machien_machhineID111', 'nnn', 'loca111', '1', '2017-11-30 10:54:59', '2017-11-30 10:55:03', '2017-12-01 10:55:09', '2017-12-23 10:55:14');
INSERT INTO `machine` VALUES ('2', '2', 'machien_machhineID222', 'nnnnn', 'loca222', '1', '2017-11-30 10:56:01', '2017-11-30 10:56:05', '2017-12-07 10:56:10', '2017-12-22 10:56:16');
INSERT INTO `machine` VALUES ('3', '4', 'machien_machhineID2333', 'nnnme', 'loca333', '2', '2017-11-30 11:11:16', '2017-11-30 11:11:19', '2017-12-01 11:11:29', '2017-12-07 11:11:36');

-- ----------------------------
-- Table structure for `machine_order`
-- ----------------------------
DROP TABLE IF EXISTS `machine_order`;
CREATE TABLE `machine_order` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `original_order_id` int(10) unsigned DEFAULT NULL COMMENT '在改单/拆单情况发生时，原订单无效，为了做到数据恢复和订单原始记录，需要记录',
  `contract_id` int(10) unsigned NOT NULL COMMENT '合同号对应ID',
  `order_detail_id` int(10) unsigned NOT NULL COMMENT 'Order详细信息，通过它来多表关联',
  `create_user_id` int(10) unsigned NOT NULL COMMENT '创建订单的ID， 只有销售员和销售主管可以创建订单',
  `country` varchar(255) DEFAULT NULL COMMENT '国家',
  `brand` varchar(255) NOT NULL DEFAULT 'SINSIM' COMMENT '商标',
  `machine_num` tinyint(4) unsigned NOT NULL COMMENT '机器台数',
  `machine_type` int(10) unsigned NOT NULL COMMENT '机器类型',
  `needle_num` int(11) unsigned NOT NULL COMMENT '针数',
  `head_num` int(11) unsigned NOT NULL COMMENT '头数',
  `head_distance` int(11) unsigned NOT NULL COMMENT '头距(由销售预填、销售更改)',
  `x_distance` varchar(255) NOT NULL COMMENT 'X-行程',
  `y_distance` varchar(255) NOT NULL COMMENT 'Y-行程',
  `package_method` varchar(255) NOT NULL COMMENT '包装方式',
  `machine_price` varchar(255) NOT NULL COMMENT '机器价格（不包括装置）',
  `contract_ship_date` date NOT NULL,
  `plan_ship_date` date NOT NULL,
  `mark` text NOT NULL COMMENT '备注信息',
  `sellman` varchar(255) DEFAULT NULL COMMENT '订单中文字输入的销售员，一般以创建订单销售员作为sellman，这边是sinsim的特殊需求',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '表示订单状态，默认是“1”，表示订单还未签核完成，签核完成则为“2”， 在改单后状态变为“3”， 拆单后订单状态变成“4”，取消后状态为“5”。取消时，需要检查订单中机器的安装状态，如果有机器已经开始安装，则需要先改变机器状态为取消后才能进行删除操作。如果取消时，签核还未开始，处于编辑状态，则可以直接取消，但是只要有后续部分完成签核时候，都需要填写取消原因以及记录取消的人、时间等。在order_cancel_record表中进行维护。因为order表和order_detail表中的内容比较多，所以建议在前端session中保存，这样也方便销售员在下一次填写订单时，只需要改部分内容即可',
  `create_time` datetime NOT NULL COMMENT '订单创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '订单信息更新时间',
  `end_time` datetime DEFAULT NULL COMMENT '订单结束时间',
  PRIMARY KEY (`id`),
  KEY `fk_o_machine_type` (`machine_type`),
  KEY `fk_o_order_detail_id` (`order_detail_id`),
  KEY `fk_o_contract_id` (`contract_id`),
  CONSTRAINT `fk_o_contract_id` FOREIGN KEY (`contract_id`) REFERENCES `contract` (`id`),
  CONSTRAINT `fk_o_machine_type` FOREIGN KEY (`machine_type`) REFERENCES `machine_type` (`id`),
  CONSTRAINT `fk_o_order_detail_id` FOREIGN KEY (`order_detail_id`) REFERENCES `order_detail` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine_order
-- ----------------------------
INSERT INTO `machine_order` VALUES ('1', '1', '2', '1', '1', 'cn', 'SINSIM', '22', '3', '22', '33', '44', '22', '22', '22', '5566', '2017-10-27', '2017-11-30', 'mm', 'alice', '1', '2017-11-21 11:45:23', '2017-11-27 13:36:44', '2017-12-01 13:36:48');
INSERT INTO `machine_order` VALUES ('2', '12', '2', '2', '2', 'fr', 'SINSIM', '55', '2', '22', '22', '224', '11', '22', '33', '333', '2017-11-28', '2017-12-09', 'eee', 'bob', '1', '2017-11-24 11:45:23', '2017-11-29 11:45:28', '2017-11-30 11:45:33');
INSERT INTO `machine_order` VALUES ('3', '1', '2', '2', '66', 'fr', 'SINSIM', '22', '2', '22', '22', '33', '13', '22', '22', '223', '2017-11-28', '2017-12-08', 'fff', 'bob', '22', '2017-11-24 11:45:23', '2017-11-28 11:51:39', '2017-12-01 13:36:48');
INSERT INTO `machine_order` VALUES ('4', '4', '2', '1', '3', 'contr1', 'SINSIM', '4', '1', '1', '1', '2', '3', '3', '3', '4', '2017-11-28', '2017-12-07', 'm', 'Dim', '1', '2017-11-29 14:24:37', '2017-11-29 14:24:42', '2017-12-10 14:24:48');
INSERT INTO `machine_order` VALUES ('11', '1', '2', '28', '66', 'fr', 'SINSIM-add-by-interface', '22', '2', '22', '22', '33', '13', '22', '22', '223', '2017-11-28', '2017-12-08', 'fff', 'bob', '22', '2017-11-24 11:45:23', '2017-11-28 11:51:39', '2017-12-01 13:36:48');
INSERT INTO `machine_order` VALUES ('12', '1', '2', '29', '66', 'fr', 'SINSIM-add-by-interface', '22', '2', '22', '22', '33', '13', '22', '22', '223', '2017-11-28', '2017-12-08', 'fff', 'bob', '22', '2017-11-24 11:45:23', '2017-11-28 11:51:39', '2017-12-01 13:36:48');
INSERT INTO `machine_order` VALUES ('13', '1', '2', '30', '66', 'fr', 'SINSIM-add-by-interface', '22', '1', '22', '22', '33', '13', '22', '22', '223', '2017-11-28', '2017-12-08', 'fff', 'bob', '22', '2017-11-24 11:45:23', '2017-11-28 11:51:39', '2017-12-01 13:36:48');
INSERT INTO `machine_order` VALUES ('14', '1', '2', '31', '66', 'fr', 'SINSIM-add-by-interface', '22', '2', '22', '22', '33', '13', '22', '22', '223', '2017-11-28', '2017-12-08', 'fff', 'bob', '22', '2017-11-24 11:45:23', '2017-11-28 11:51:39', '2017-12-01 13:36:48');
INSERT INTO `machine_order` VALUES ('15', '1', '2', '32', '66', 'fr', 'SINSIM-add-by-interface', '22', '2', '22', '22', '33', '13', '22', '22', '223', '2017-11-28', '2017-12-08', 'fff', 'bob', '22', '2017-11-24 11:45:23', '2017-11-28 11:51:39', '2017-12-01 13:36:48');
INSERT INTO `machine_order` VALUES ('16', '1', '2', '33', '66', 'fr', 'SINSIM-add-by-interface', '22', '2', '22', '22', '33', '13', '22', '22', '223', '2017-11-28', '2017-12-08', 'fff', 'bob', '22', '2017-11-24 11:45:23', '2017-11-28 11:51:39', '2017-12-01 13:36:48');
INSERT INTO `machine_order` VALUES ('18', '1', '2', '35', '66', 'fr', 'SINSIM-add-by-interface', '22', '2', '22', '22', '33', '13', '22', '22', '223', '2017-11-28', '2017-12-08', 'fff', 'bob', '22', '2017-11-24 11:45:23', '2017-11-28 11:51:39', '2017-12-01 13:36:48');
INSERT INTO `machine_order` VALUES ('20', '1', '2', '27', '66', 'fr', 'SINSIM-add-by-interface', '22', '2', '22', '22', '33', '13', '22', '22', '223', '2017-11-28', '2017-12-08', 'fff', 'bob', '22', '2017-11-24 11:45:23', '2017-11-28 11:51:39', '2017-12-01 13:36:48');

-- ----------------------------
-- Table structure for `machine_type`
-- ----------------------------
DROP TABLE IF EXISTS `machine_type`;
CREATE TABLE `machine_type` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '机器类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine_type
-- ----------------------------
INSERT INTO `machine_type` VALUES ('1', ' 平绣11');
INSERT INTO `machine_type` VALUES ('2', '毛巾秀22');
INSERT INTO `machine_type` VALUES ('3', '特种秀');
INSERT INTO `machine_type` VALUES ('4', '11');

-- ----------------------------
-- Table structure for `order_cancel_record`
-- ----------------------------
DROP TABLE IF EXISTS `order_cancel_record`;
CREATE TABLE `order_cancel_record` (
  `id` int(10) unsigned NOT NULL,
  `order_id` int(10) unsigned NOT NULL COMMENT '订单编号',
  `cancel_reason` text NOT NULL COMMENT '取消原因',
  `user_id` int(10) unsigned NOT NULL COMMENT '取消用户的ID，只有创建订单的销售员可以取消改订单，或者销售经理',
  `cancel_time` datetime NOT NULL COMMENT '取消时间',
  PRIMARY KEY (`id`),
  KEY `fk_oc_order_id` (`order_id`),
  KEY `fk_oc_user_id` (`user_id`),
  CONSTRAINT `fk_oc_order_id` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`),
  CONSTRAINT `fk_oc_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_cancel_record
-- ----------------------------

-- ----------------------------
-- Table structure for `order_change_record`
-- ----------------------------
DROP TABLE IF EXISTS `order_change_record`;
CREATE TABLE `order_change_record` (
  `id` int(10) unsigned NOT NULL,
  `order_id` int(10) unsigned NOT NULL COMMENT '订单编号',
  `change_reason` text NOT NULL COMMENT '更改原因',
  `user_id` int(10) unsigned NOT NULL COMMENT '修改订单操作的用户ID，只有创建订单的销售员可以修改订单，或者销售经理',
  `change_time` datetime NOT NULL COMMENT '修改订单的时间',
  PRIMARY KEY (`id`),
  KEY `fk_oc_order_id` (`order_id`),
  KEY `fk_oc_user_id` (`user_id`),
  CONSTRAINT `order_change_record_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`),
  CONSTRAINT `order_change_record_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_change_record
-- ----------------------------

-- ----------------------------
-- Table structure for `order_detail`
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `special_towel_color` varchar(255) DEFAULT NULL COMMENT '特种：毛巾（色数）',
  `special_towel_daxle` varchar(255) DEFAULT NULL COMMENT '特种： D轴',
  `special_towel_haxle` varchar(255) DEFAULT NULL COMMENT '特种： H轴',
  `special_towel_motor` varchar(255) DEFAULT NULL COMMENT '特种：主电机',
  `special_taping_head` varchar(255) DEFAULT NULL COMMENT '特种：特种：盘带头',
  `special_towel_needle` varchar(255) DEFAULT NULL COMMENT '特种：毛巾机针',
  `electric_pc` varchar(255) DEFAULT NULL COMMENT '电气： 电脑',
  `electric_motor` varchar(255) DEFAULT NULL COMMENT '电气：主电机',
  `electric_motor_xy` varchar(255) DEFAULT NULL COMMENT '电气：X,Y电机',
  `electric_trim` varchar(255) DEFAULT NULL COMMENT '电气：剪线方式',
  `electric_power` varchar(255) DEFAULT NULL COMMENT '电气： 电源',
  `electric_switch` varchar(255) DEFAULT NULL COMMENT '电气： 按钮开关',
  `electric_oil` varchar(255) DEFAULT NULL COMMENT '电气： 加油系统',
  `axle_split` varchar(255) DEFAULT NULL COMMENT '上下轴：j夹线器',
  `axle_panel` varchar(255) DEFAULT NULL COMMENT '上下轴：面板',
  `axle_needle` varchar(255) DEFAULT NULL COMMENT '上下轴：机针',
  `axle_rail` varchar(255) DEFAULT NULL COMMENT '上下轴：机头中导轨',
  `axle_down_check` varchar(255) DEFAULT NULL COMMENT '上下轴：底检方式',
  `axle_hook` varchar(255) DEFAULT NULL COMMENT '上下轴：旋梭',
  `axle_jump` varchar(255) DEFAULT NULL COMMENT '上下轴：跳跃方式',
  `axle_upper_thread` varchar(255) DEFAULT NULL COMMENT '上下轴：面线夹持',
  `axle_addition` varchar(255) DEFAULT NULL COMMENT '上下轴：附加装置（该部分由销售预填，技术进行确认或更改）',
  `framework_color` varchar(255) DEFAULT NULL COMMENT '机架台板：机架颜色 ',
  `framework_platen` varchar(255) DEFAULT NULL COMMENT '机架台板：台板',
  `framework_platen_color` varchar(255) DEFAULT NULL COMMENT '机架台板：台板颜色',
  `framework_ring` varchar(255) DEFAULT NULL COMMENT '机架台板：吊环',
  `framework_bracket` varchar(255) DEFAULT NULL COMMENT '机架台板：电脑托架',
  `framework_stop` varchar(255) DEFAULT NULL COMMENT '机架台板：急停装置',
  `framework_light` varchar(255) DEFAULT NULL COMMENT '机架台板：日光灯',
  `driver_type` varchar(255) DEFAULT NULL COMMENT '驱动：类型',
  `driver_method` varchar(255) DEFAULT NULL COMMENT '驱动：方式',
  `driver_reel_hole` varchar(255) DEFAULT NULL COMMENT '驱动：绷架孔',
  `driver_horizon_num` tinyint(4) DEFAULT NULL COMMENT '驱动：横档数量',
  `driver_vertical_num` tinyint(4) DEFAULT NULL COMMENT '驱动：直档数量',
  `driver_reel` varchar(255) DEFAULT NULL COMMENT '驱动：绷架',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_detail
-- ----------------------------
INSERT INTO `order_detail` VALUES ('1', 'rred', '11', 'dd', 'ttt', 'head', 'nnbb', 'opc', 'ff', 'ff', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhh', 'rrr', 'ccc', 'hhhh', 'jj', 'yy', 'yyr', 'fttrttr', 'fr', 'rfrf', 'aaa', 'bbbb', 'st', 'aa', 'aa', 'dd', 'hol', '11', '22', 'fffffff');
INSERT INTO `order_detail` VALUES ('2', 'blue', '33', 'aa', 'ff', '11', 'bbbb', 'vv', 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'rr', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('27', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'add-by-interface--GetID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('28', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'add-by-interface--GetID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('29', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', '222add-by-interface--GetID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('30', '2222blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', '222add-by-interface--GetID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('31', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'yizhixing-add-by-interface--returnID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('32', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'yizhixing-add-by-interface--returnID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('33', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'yizhixing-add-by-interface--returnID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('34', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'yizhixing-add-by-interface--returnID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('35', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'yizhixing-add-by-interface--returnID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('36', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'yizhixing-add-by-interface--returnID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('37', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'yizhixing-add-by-interface--returnID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');

-- ----------------------------
-- Table structure for `order_loading_list`
-- ----------------------------
DROP TABLE IF EXISTS `order_loading_list`;
CREATE TABLE `order_loading_list` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(10) unsigned NOT NULL COMMENT '装车单、联系单对应的订单id，多张图片对应多个记录',
  `file_name` varchar(255) NOT NULL COMMENT '装车单、联系单对应的Excel文件名（包含路径）,多个的话对应多条记录',
  `type` tinyint(4) NOT NULL COMMENT '"1"==>装车单，"2"==>联系单',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `fk_oll_order_id` (`order_id`),
  CONSTRAINT `fk_oll_order_id` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_loading_list
-- ----------------------------
INSERT INTO `order_loading_list` VALUES ('1', '1', 'file_name1111', '1', '2017-12-10 10:00:52');
INSERT INTO `order_loading_list` VALUES ('2', '2', 'fielName2222', '2', '2017-12-10 10:01:13');
INSERT INTO `order_loading_list` VALUES ('3', '4', 'file_name1111-by-add', '1', '2017-12-10 10:00:52');
INSERT INTO `order_loading_list` VALUES ('4', '18', 'file_name1111-by-add', '1', '2017-12-10 10:00:52');
INSERT INTO `order_loading_list` VALUES ('5', '20', 'file_name1111-by-add', '1', '2017-12-10 10:00:52');
INSERT INTO `order_loading_list` VALUES ('6', '3', 'file_name1111-by-add', '1', '2017-12-10 10:00:52');

-- ----------------------------
-- Table structure for `order_sign`
-- ----------------------------
DROP TABLE IF EXISTS `order_sign`;
CREATE TABLE `order_sign` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(10) unsigned NOT NULL COMMENT '订单ID',
  `sign_content` text NOT NULL COMMENT '签核内容，以json格式的数组形式存放, 所有项完成后更新status为完成\r\n[ \r\n    {"role_id": 1, "role_name":"技术部"，“person”：“张三”，”comment“: "同意"， ”update_time“:"2017-11-05 12:08:55"},\r\n    {"role_id":2, "role_name":"PMC"，“person”：“李四”，”comment“: "同意，但是部分配件需要新设计"， ”update_time“:"2017-11-06 12:08:55"}\r\n]',
  `status` tinyint(4) unsigned NOT NULL COMMENT '需求单签核状态：“1”==>签核中， “2”==>签核完成， “3”==>驳回，该条记录在驳回后停止修改，会新创建签核记录',
  `create_time` datetime NOT NULL COMMENT '签核流程开始时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `fk_os_order_id` (`order_id`),
  CONSTRAINT `fk_os_order_id` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_sign
-- ----------------------------

-- ----------------------------
-- Table structure for `order_split_record`
-- ----------------------------
DROP TABLE IF EXISTS `order_split_record`;
CREATE TABLE `order_split_record` (
  `id` int(10) unsigned NOT NULL,
  `order_id` int(10) unsigned NOT NULL COMMENT '订单编号',
  `split_reason` text NOT NULL COMMENT '取消原因',
  `user_id` int(10) unsigned NOT NULL COMMENT '拆分订单操作的用户ID，只有创建订单的销售员可以拆分改订单，或者销售经理',
  `split_time` datetime NOT NULL COMMENT '拆分订单的时间',
  PRIMARY KEY (`id`),
  KEY `fk_oc_order_id` (`order_id`),
  KEY `fk_oc_user_id` (`user_id`),
  CONSTRAINT `order_split_record_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`),
  CONSTRAINT `order_split_record_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_split_record
-- ----------------------------

-- ----------------------------
-- Table structure for `process`
-- ----------------------------
DROP TABLE IF EXISTS `process`;
CREATE TABLE `process` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '流程名字（平绣、特种绣等）',
  `task_list` text NOT NULL COMMENT '作业内容的json对象，该对象中包括link数据和node数据。其是创建流程的模板，在创建记录时，需要解析node array的内容，创建task记录列表',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of process
-- ----------------------------
INSERT INTO `process` VALUES ('1', 'processAAA', 'tsk123', '2017-11-30 10:58:17', '2017-11-30 10:58:20');
INSERT INTO `process` VALUES ('2', 'processBBB', 'tsk22', '2017-11-30 10:58:43', '2017-11-30 10:58:46');
INSERT INTO `process` VALUES ('3', 'pro33', 'taskList333', '2017-11-30 11:10:08', '2017-11-30 22:10:12');

-- ----------------------------
-- Table structure for `process_record`
-- ----------------------------
DROP TABLE IF EXISTS `process_record`;
CREATE TABLE `process_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `machine_id` int(10) unsigned NOT NULL,
  `process_id` int(10) unsigned NOT NULL COMMENT '对应的模板（process）的ID',
  `link_data` text NOT NULL COMMENT '安装流程的link数据,格式参考linkDataArray',
  `node_data` text NOT NULL COMMENT '安装流程的node数据，格式参考nodeDataArray',
  `create_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_mp_machine_id` (`machine_id`),
  KEY `fk_pr_process_id` (`process_id`),
  CONSTRAINT `fk_pr_machine_id` FOREIGN KEY (`machine_id`) REFERENCES `machine` (`id`),
  CONSTRAINT `fk_pr_process_id` FOREIGN KEY (`process_id`) REFERENCES `process` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of process_record
-- ----------------------------
INSERT INTO `process_record` VALUES ('1', '1', '1', 'linkData111', 'node_data111', '2017-11-30 11:00:05', '2017-11-30 11:00:44');
INSERT INTO `process_record` VALUES ('2', '2', '2', 'llll', 'nnn', '2017-11-30 11:00:39', '2017-11-30 11:00:50');
INSERT INTO `process_record` VALUES ('3', '1', '1', 'dddd', 'aaaa', '2017-11-30 11:00:49', '2017-12-01 11:01:16');
INSERT INTO `process_record` VALUES ('4', '3', '3', 'linkData444', 'nodeData44', '2017-12-02 13:24:47', '2018-01-20 13:24:53');

-- ----------------------------
-- Table structure for `quality_record_image`
-- ----------------------------
DROP TABLE IF EXISTS `quality_record_image`;
CREATE TABLE `quality_record_image` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `task_quality_record_id` int(10) unsigned NOT NULL,
  `image` varchar(255) NOT NULL COMMENT '异常图片名称（包含路径）,以后这部分数据是最大的，首先pad上传时候时候需要压缩，以后硬盘扩展的话，可以把几几年的图片放置到另外一个硬盘，然后pad端响应升级（根据时间加上图片的路径）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `fk_task_quality_record_id` (`task_quality_record_id`),
  CONSTRAINT `fk_task_quality_record_id` FOREIGN KEY (`task_quality_record_id`) REFERENCES `task_quality_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of quality_record_image
-- ----------------------------
INSERT INTO `quality_record_image` VALUES ('1', '1', 'img_url123', '2017-12-05 13:25:31');
INSERT INTO `quality_record_image` VALUES ('2', '2', 'image_url222', '2017-12-05 13:25:49');
INSERT INTO `quality_record_image` VALUES ('3', '3', 'NoImge', '2017-12-05 13:26:11');
INSERT INTO `quality_record_image` VALUES ('4', '1', 'imngURL444', '2017-12-10 14:06:25');

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  `role_des` text COMMENT '角色说明',
  `role_scope` text COMMENT '角色权限列表',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '超级管理员', '系统后台管理', '');
INSERT INTO `role` VALUES ('2', '生产部管理员', '主要Pad上操作，上传位置、pad上查看流程等', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[\"/home/order/order_sign\",\"/home/order/order_manage\"],\"plan\":[],\"abnormal\":[],\"task\":[\"/home/task/process_manage\"],\"system\":[]}');
INSERT INTO `role` VALUES ('3', '安装组长', '安装前后扫描机器', null);
INSERT INTO `role` VALUES ('4', '生产部经理', '订单审批', null);
INSERT INTO `role` VALUES ('5', '普通员工', '浏览一般网页信息', null);
INSERT INTO `role` VALUES ('6', '总经理', '订单审核等其他可配置权限', null);
INSERT INTO `role` VALUES ('7', '销售部经理', '订单审批', null);
INSERT INTO `role` VALUES ('8', '技术部经理', '订单审批', null);
INSERT INTO `role` VALUES ('9', '销售员', '录入订单', null);
INSERT INTO `role` VALUES ('10', '技术员', '上传装车单，联系单', null);
INSERT INTO `role` VALUES ('11', '质检员', 'pad上操作', null);

-- ----------------------------
-- Table structure for `sign_process`
-- ----------------------------
DROP TABLE IF EXISTS `sign_process`;
CREATE TABLE `sign_process` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `process_name` varchar(255) NOT NULL COMMENT '签核流程的名称',
  `process_content` text NOT NULL COMMENT '签核流程内容，json格式，每一个step为序号和对应角色\r\n[\r\n    {"step":1, "role_id":1}.\r\n    {"step":2, "role_id":3}.\r\n]',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sign_process
-- ----------------------------
INSERT INTO `sign_process` VALUES ('4', '拆单签核流程', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\"},{\"number\":4,\"roleId\":6,\"signType\":\"合同签核\"}]', '2017-12-12 01:14:40', null);
INSERT INTO `sign_process` VALUES ('3', '正常签核流程', '[{\"number\":1,\"roleId\":1,\"signType\":\"合同签核\"},{\"number\":2,\"roleId\":6,\"signType\":\"合同签核\"}]', '2017-12-11 23:57:56', '2017-12-12 01:18:19');

-- ----------------------------
-- Table structure for `task`
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) NOT NULL COMMENT '安装作业项的名称',
  `group_id` int(10) unsigned NOT NULL COMMENT '安装小组id',
  `guidance` text COMMENT '作业指导，后续可能会需要（一般是html格式）',
  PRIMARY KEY (`id`),
  KEY `fk_t_group_id` (`group_id`),
  KEY `task_name` (`task_name`),
  CONSTRAINT `fk_t_group_id` FOREIGN KEY (`group_id`) REFERENCES `install_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('1', 'tsk111', '1', 'guidance111');
INSERT INTO `task` VALUES ('2', 'tskName222Have', '2', 'guidance2222');
INSERT INTO `task` VALUES ('3', 'tskName333Have', '2', 'guidance3333');
INSERT INTO `task` VALUES ('4', 'tsk44', '4', 'guidance444');
INSERT INTO `task` VALUES ('5', 'tskAbc', '3', 'guidanceAAA');

-- ----------------------------
-- Table structure for `task_plan`
-- ----------------------------
DROP TABLE IF EXISTS `task_plan`;
CREATE TABLE `task_plan` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `task_record_id` int(10) unsigned NOT NULL COMMENT '对应taskj记录的id',
  `plan_time` datetime NOT NULL COMMENT 'task的计划完成时间',
  `user_id` int(10) unsigned NOT NULL COMMENT '添加计划的人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '上一次更改计划的时间',
  PRIMARY KEY (`id`),
  KEY `fk_tp_task_record_id` (`task_record_id`),
  KEY `fk_tp_user_id` (`user_id`),
  CONSTRAINT `fk_tp_task_record_id` FOREIGN KEY (`task_record_id`) REFERENCES `task_record` (`id`),
  CONSTRAINT `fk_tp_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=255556 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_plan
-- ----------------------------
INSERT INTO `task_plan` VALUES ('1', '1', '2017-12-02 11:06:05', '1', '2017-12-02 11:06:13', '2017-12-02 11:06:29');
INSERT INTO `task_plan` VALUES ('22222', '3', '2017-12-02 11:09:04', '3', '2017-12-02 11:11:11', '2017-12-02 11:09:19');
INSERT INTO `task_plan` VALUES ('36666', '3', '2017-11-30 13:15:54', '1', '2017-12-02 22:22:22', '2017-12-10 13:16:06');
INSERT INTO `task_plan` VALUES ('177777', '1', '2017-11-30 22:14:45', '2', '2017-11-30 13:14:54', '2017-11-30 13:14:58');
INSERT INTO `task_plan` VALUES ('255555', '2', '2017-12-07 13:15:18', '2', '2017-11-30 13:15:30', '2017-12-09 13:15:35');

-- ----------------------------
-- Table structure for `task_quality_record`
-- ----------------------------
DROP TABLE IF EXISTS `task_quality_record`;
CREATE TABLE `task_quality_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `task_record_id` int(10) unsigned NOT NULL COMMENT '对应安装项ID',
  `name` varchar(255) NOT NULL COMMENT '质检员名字',
  `status` tinyint(4) NOT NULL COMMENT '质检结果: "1"==>通过； “0”==>不通过',
  `comment` text COMMENT '质检备注',
  `create_time` datetime NOT NULL COMMENT '添加质检结果的时间',
  PRIMARY KEY (`id`),
  KEY `tqr_task_record_id` (`task_record_id`),
  CONSTRAINT `tqr_task_record_id` FOREIGN KEY (`task_record_id`) REFERENCES `task_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_quality_record
-- ----------------------------
INSERT INTO `task_quality_record` VALUES ('1', '1', 'QAer111', '1', 'passOK', '2017-12-05 13:23:02');
INSERT INTO `task_quality_record` VALUES ('2', '7', 'CheckerBob', '0', 'NoPass', '2017-12-05 13:23:57');
INSERT INTO `task_quality_record` VALUES ('3', '3', 'QAer333', '1', 'Passed', '2017-12-05 13:24:42');
INSERT INTO `task_quality_record` VALUES ('4', '1', 'QA444', '2', 'cmtOKK444', '2017-12-10 14:05:44');

-- ----------------------------
-- Table structure for `task_record`
-- ----------------------------
DROP TABLE IF EXISTS `task_record`;
CREATE TABLE `task_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) NOT NULL,
  `process_record_id` int(10) unsigned NOT NULL,
  `node_key` tinyint(4) NOT NULL COMMENT '对应流程中node节点的key值',
  `leader` varchar(255) DEFAULT NULL COMMENT '扫描组长（名字）',
  `worker_list` text COMMENT '组长扫描结束之前，需要填入的工人名字,保存格式为string数组',
  `status` tinyint(4) unsigned NOT NULL COMMENT 'task状态，“1”==>未开始， “2”==>进行中，“3”==>完成， “4”==>异常',
  `begin_time` datetime DEFAULT NULL COMMENT 'task开始时间',
  `end_time` datetime DEFAULT NULL COMMENT 'task结束时间',
  PRIMARY KEY (`id`),
  KEY `fk_tr_process_record_id` (`process_record_id`),
  KEY `fk_tr_task_name` (`task_name`),
  CONSTRAINT `fk_tr_process_record_id` FOREIGN KEY (`process_record_id`) REFERENCES `process_record` (`id`),
  CONSTRAINT `fk_tr_task_name` FOREIGN KEY (`task_name`) REFERENCES `task` (`task_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_record
-- ----------------------------
INSERT INTO `task_record` VALUES ('1', 'tskAbc', '1', '111', 'bob111', 'worder1,w2,w3,wworder4', '1', '2017-11-30 11:04:40', '2017-12-08 11:04:43');
INSERT INTO `task_record` VALUES ('2', 'tskName222Have', '4', '32', 'zhang222-by-update', 'worder1,r,2,w3,worder22-by-update', '2', '2017-11-30 11:05:43', '2017-11-30 11:05:48');
INSERT INTO `task_record` VALUES ('3', 'tsk111', '1', '44', 'leader33', 'worker11,woker33_of_tsk3', '3', '2017-11-30 11:08:54', '2017-11-30 22:08:58');
INSERT INTO `task_record` VALUES ('4', 'tsk111', '2', '127', 'lead33', 'wk111', '2', '2017-12-05 11:42:47', '2017-12-05 11:42:50');
INSERT INTO `task_record` VALUES ('5', 'tsk44', '2', '127', 'lead444', 'wk11,wk33', '1', '2017-12-05 11:45:39', '2017-12-05 11:45:44');
INSERT INTO `task_record` VALUES ('7', 'tskName333Have', '3', '33', 'lead555', 'wokerAA,wokerB', '2', '2017-12-05 11:40:42', '2017-12-29 11:40:48');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL COMMENT '账号',
  `name` varchar(255) NOT NULL COMMENT '用户姓名',
  `role_id` int(10) unsigned NOT NULL COMMENT '角色ID',
  `password` varchar(255) DEFAULT 'sinsim' COMMENT '密码',
  `group_id` int(10) unsigned DEFAULT NULL COMMENT '所在安装组group ID，可以为空(其他部门人员)',
  `valid` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '员工是否在职，“1”==>在职, “0”==>离职',
  PRIMARY KEY (`id`),
  KEY `idx_user_role_id` (`role_id`) USING BTREE,
  KEY `fk_user_group_id` (`group_id`),
  CONSTRAINT `fk_user_group_id` FOREIGN KEY (`group_id`) REFERENCES `install_group` (`id`),
  CONSTRAINT `fk_user_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '胡通', '1', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('2', 'sinsim22', 'user李四-by_update', '2', 'sinsim-by-update', '1', '1');
INSERT INTO `user` VALUES ('3', 'sinsim33', 'user王五', '4', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('4', 'sinsim555', 'user李四555', '2', 'sinsim555', '1', '1');
INSERT INTO `user` VALUES ('5', 'sinsim555', 'user李四555', '2', 'sinsim555', '1', '1');
INSERT INTO `user` VALUES ('6', 'QAer1', 'user李四555', '11', 'pppwd', null, '1');
INSERT INTO `user` VALUES ('7', 'sss', 'saaa_user', '3', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('9', 'sinsimAAA', 'user999', '1', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('10', 'account22', 'user10', '2', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('11', 'hutong', '胡通', '3', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('12', 'test', '张三', '5', 'sinsim', null, '1');
