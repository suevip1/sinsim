/*
Navicat MySQL Data Transfer

Source Server         : MyDB
Source Server Version : 50547
Source Host           : localhost:3306
Source Database       : sinsim_db

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2018-04-13 13:28:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `abnormal`
-- ----------------------------
DROP TABLE IF EXISTS `abnormal`;
CREATE TABLE `abnormal` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `abnormal_name` varchar(255) NOT NULL COMMENT '异常名称',
  `valid` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '异常是否有效，前端删除某个异常类型时，如果该类型被使用过，valid设置为0，未使用过则删除，默认为1',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of abnormal
-- ----------------------------
INSERT INTO `abnormal` VALUES ('9', '仓库缺料', '1', '2018-04-01 10:29:18', null);
INSERT INTO `abnormal` VALUES ('10', '配件异常', '1', '2018-04-14 06:34:31', null);
INSERT INTO `abnormal` VALUES ('11', '装配异常', '1', '2018-04-14 06:35:29', null);
INSERT INTO `abnormal` VALUES ('12', '设计异常', '1', '2018-04-14 06:35:55', null);
INSERT INTO `abnormal` VALUES ('13', 'BOM异常', '1', '2018-04-14 06:37:03', null);

-- ----------------------------
-- Table structure for `abnormal_image`
-- ----------------------------
DROP TABLE IF EXISTS `abnormal_image`;
CREATE TABLE `abnormal_image` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `abnormal_record_id` int(10) unsigned NOT NULL,
  `image` varchar(1000) NOT NULL COMMENT '异常图片名称（包含路径）,以后这部分数据是最大的，首先pad上传时候时候需要压缩，以后硬盘扩展的话，可以把几几年的图片放置到另外一个硬盘，然后pad端响应升级（根据时间加上图片的路径）',
  `create_time` datetime NOT NULL COMMENT '上传异常图片的时间',
  PRIMARY KEY (`id`),
  KEY `fk_ai_abnormal_record_id` (`abnormal_record_id`),
  CONSTRAINT `fk_ai_abnormal_record_id` FOREIGN KEY (`abnormal_record_id`) REFERENCES `abnormal_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of abnormal_image
-- ----------------------------

-- ----------------------------
-- Table structure for `abnormal_record`
-- ----------------------------
DROP TABLE IF EXISTS `abnormal_record`;
CREATE TABLE `abnormal_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `abnormal_type` int(10) unsigned NOT NULL COMMENT '异常类型',
  `task_record_id` int(10) unsigned NOT NULL COMMENT '作业工序',
  `submit_user` int(10) unsigned NOT NULL COMMENT '提交异常的用户ID',
  `comment` text NOT NULL COMMENT '异常备注',
  `solution` text COMMENT '解决办法',
  `solution_user` int(10) unsigned DEFAULT NULL COMMENT '解决问题的用户对应的ID',
  `create_time` datetime NOT NULL,
  `solve_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ar_abnormal_type` (`abnormal_type`),
  KEY `fk_ar_task_record_id` (`task_record_id`),
  KEY `fk_ar_submit_user` (`submit_user`),
  KEY `fk_ar_solution_user` (`solution_user`),
  CONSTRAINT `fk_ar_abnormal_type` FOREIGN KEY (`abnormal_type`) REFERENCES `abnormal` (`id`),
  CONSTRAINT `fk_ar_solution_user` FOREIGN KEY (`solution_user`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_ar_submit_user` FOREIGN KEY (`submit_user`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_ar_task_record_id` FOREIGN KEY (`task_record_id`) REFERENCES `task_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of abnormal_record
-- ----------------------------

-- ----------------------------
-- Table structure for `contract`
-- ----------------------------
DROP TABLE IF EXISTS `contract`;
CREATE TABLE `contract` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contract_num` varchar(255) NOT NULL COMMENT '合同号',
  `customer_name` varchar(255) NOT NULL COMMENT '客户姓名',
  `sellman` varchar(255) NOT NULL COMMENT '销售人员',
  `contract_ship_date` date NOT NULL COMMENT '合同交货日期',
  `pay_method` varchar(255) DEFAULT NULL COMMENT '支付方式',
  `currency_type` varchar(255) NOT NULL COMMENT '币种',
  `mark` text COMMENT '合同备注信息，有填单员上填入',
  `status` tinyint(4) unsigned NOT NULL COMMENT '合同状态',
  `create_time` datetime NOT NULL COMMENT '填表时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新table的时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract
-- ----------------------------

-- ----------------------------
-- Table structure for `contract_reject_record`
-- ----------------------------
DROP TABLE IF EXISTS `contract_reject_record`;
CREATE TABLE `contract_reject_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contract_id` int(10) unsigned NOT NULL,
  `role_id` int(10) unsigned NOT NULL COMMENT '驳回的角色（审核阶段）',
  `user_id` int(10) unsigned NOT NULL COMMENT '驳回操作的人',
  `reason` text NOT NULL COMMENT '驳回原因',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract_reject_record
-- ----------------------------

-- ----------------------------
-- Table structure for `contract_sign`
-- ----------------------------
DROP TABLE IF EXISTS `contract_sign`;
CREATE TABLE `contract_sign` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contract_id` int(10) unsigned NOT NULL COMMENT '合同ID',
  `sign_content` text NOT NULL COMMENT '签核内容，以json格式的数组形式存放, 所有项完成后更新status为完成.[ \r\n    {"step_number":1, "role_id": 1, "role_name":"销售经理"，“person”：“张三”，”comment“: "同意"，"resolved":1,”update_time“:"2017-11-05 12:08:55"},\r\n    {"step_number":1,"role_id":2, "role_name":"财务部"，“person”：“李四”，”comment“: "同意，但是部分配件需要新设计"，"resolved":0, ”update_time“:"2017-11-06 12:08:55"}\r\n]',
  `current_step` varchar(255) NOT NULL COMMENT '当前进行中的签核环节（来至于role_name）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `fk_cs_contract_id` (`contract_id`),
  CONSTRAINT `fk_cs_contract_id` FOREIGN KEY (`contract_id`) REFERENCES `contract` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES ('2', 'EService-Zhao', '866413031233123');
INSERT INTO `device` VALUES ('4', 'EService-hu', '354112070615948');
INSERT INTO `device` VALUES ('5', 'EService-wu', '990009269149749');
INSERT INTO `device` VALUES ('7', 'sinsim-1', '868619033691989');
INSERT INTO `device` VALUES ('8', 'sinsim-2', '868619034158954');
INSERT INTO `device` VALUES ('9', 'sinsim-3', '866764032351995');
INSERT INTO `device` VALUES ('10', 'sinsim-4', '868619034158947');

-- ----------------------------
-- Table structure for `install_group`
-- ----------------------------
DROP TABLE IF EXISTS `install_group`;
CREATE TABLE `install_group` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) NOT NULL COMMENT '公司部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of install_group
-- ----------------------------
INSERT INTO `install_group` VALUES ('1', '上轴组');
INSERT INTO `install_group` VALUES ('2', '下轴组');
INSERT INTO `install_group` VALUES ('3', '驱动组');
INSERT INTO `install_group` VALUES ('4', '台板组');
INSERT INTO `install_group` VALUES ('5', '电控组');
INSERT INTO `install_group` VALUES ('7', '针杆架组');
INSERT INTO `install_group` VALUES ('8', '调试组');
INSERT INTO `install_group` VALUES ('9', '剪线组');
INSERT INTO `install_group` VALUES ('10', '金片组');
INSERT INTO `install_group` VALUES ('11', '前置工序组');
INSERT INTO `install_group` VALUES ('12', '拉杆安装组');
INSERT INTO `install_group` VALUES ('14', '出厂检验组');
INSERT INTO `install_group` VALUES ('15', '开机测试组');

-- ----------------------------
-- Table structure for `machine`
-- ----------------------------
DROP TABLE IF EXISTS `machine`;
CREATE TABLE `machine` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(10) unsigned NOT NULL COMMENT '对应的order id',
  `machine_strid` varchar(255) NOT NULL COMMENT '系统内部维护用的机器编号(年、月、类型、头数、针数、不大于台数的数字)',
  `nameplate` varchar(255) DEFAULT NULL COMMENT '技术部填入的机器编号（铭牌）',
  `location` varchar(255) DEFAULT NULL COMMENT '机器的位置，一般由生产部管理员上传',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '机器状态（“1”==>"初始化"，“2”==>"开始安装"，“3”==>"安装完成"，“4”==>"无效"）',
  `machine_type` int(10) unsigned NOT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `installed_time` datetime DEFAULT NULL COMMENT '安装完成时间',
  `ship_time` datetime DEFAULT NULL COMMENT '发货时间（如果分批交付，需要用到，否则已订单交付为准）',
  PRIMARY KEY (`id`),
  KEY `idx_m_order_id` (`order_id`) USING BTREE,
  KEY `fk_m_machine_type` (`machine_type`),
  CONSTRAINT `fk_m_machine_type` FOREIGN KEY (`machine_type`) REFERENCES `machine_type` (`id`),
  CONSTRAINT `fk_m_order_id` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine
-- ----------------------------

-- ----------------------------
-- Table structure for `machine_order`
-- ----------------------------
DROP TABLE IF EXISTS `machine_order`;
CREATE TABLE `machine_order` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_num` varchar(255) NOT NULL COMMENT '订单编号',
  `original_order_id` int(10) unsigned DEFAULT NULL COMMENT '在改单/拆单情况发生时，原订单无效，为了做到数据恢复和订单原始记录，需要记录',
  `contract_id` int(10) unsigned NOT NULL COMMENT '合同号对应ID',
  `order_detail_id` int(10) unsigned NOT NULL COMMENT 'Order详细信息，通过它来多表关联',
  `create_user_id` int(10) unsigned NOT NULL COMMENT '创建订单的ID， 只有销售员和销售主管可以创建订单',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '表示订单状态，默认是“1”，表示订单还未签核完成，签核完成则为“2”， 在改单后状态变为“3”， 拆单后订单状态变成“4”，取消后状态为“5”。取消时，需要检查订单中机器的安装状态，如果有机器已经开始安装，则需要先改变机器状态为取消后才能进行删除操作。如果取消时，签核还未开始，处于编辑状态，则可以直接取消，但是只要有后续部分完成签核时候，都需要填写取消原因以及记录取消的人、时间等。在order_cancel_record表中进行维护。因为order表和order_detail表中的内容比较多，所以建议在前端session中保存，这样也方便销售员在下一次填写订单时，只需要改部分内容即可',
  `country` varchar(255) DEFAULT NULL COMMENT '国家',
  `brand` varchar(255) NOT NULL DEFAULT 'SINSIM' COMMENT '商标',
  `machine_num` int(11) unsigned NOT NULL COMMENT '机器台数',
  `machine_type` int(10) unsigned NOT NULL COMMENT '机器类型',
  `needle_num` int(11) unsigned NOT NULL COMMENT '针数',
  `head_num` int(11) unsigned NOT NULL COMMENT '头数',
  `head_distance` int(11) unsigned NOT NULL COMMENT '头距(由销售预填、销售更改)',
  `x_distance` varchar(255) NOT NULL COMMENT 'X-行程',
  `y_distance` varchar(255) NOT NULL COMMENT 'Y-行程',
  `package_method` varchar(255) NOT NULL COMMENT '包装方式',
  `package_mark` text COMMENT '包装备注',
  `equipment` text COMMENT '机器装置，json的字符串，包含装置名称、数量、单价',
  `machine_price` varchar(255) NOT NULL COMMENT '机器价格（不包括装置）',
  `contract_ship_date` date NOT NULL,
  `plan_ship_date` date NOT NULL,
  `mark` text COMMENT '备注信息',
  `sellman` varchar(255) NOT NULL COMMENT '订单中文字输入的销售员，一般以创建订单销售员作为sellman，这边是sinsim的特殊需求',
  `maintain_type` varchar(255) NOT NULL COMMENT '保修方式',
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
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine_order
-- ----------------------------

-- ----------------------------
-- Table structure for `machine_type`
-- ----------------------------
DROP TABLE IF EXISTS `machine_type`;
CREATE TABLE `machine_type` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '机器类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine_type
-- ----------------------------
INSERT INTO `machine_type` VALUES ('1', '单凸轮双驱动');
INSERT INTO `machine_type` VALUES ('2', '高速双凸轮');
INSERT INTO `machine_type` VALUES ('3', '普通平绣');
INSERT INTO `machine_type` VALUES ('4', '纯毛巾');
INSERT INTO `machine_type` VALUES ('5', '纯盘带');
INSERT INTO `machine_type` VALUES ('6', '帽绣');
INSERT INTO `machine_type` VALUES ('7', '平绣+盘带');
INSERT INTO `machine_type` VALUES ('8', '平绣+毛巾');
INSERT INTO `machine_type` VALUES ('9', '单凸轮+盘带');
INSERT INTO `machine_type` VALUES ('10', '单凸轮+毛巾');
INSERT INTO `machine_type` VALUES ('11', '高速双凸轮+盘带');
INSERT INTO `machine_type` VALUES ('12', '高速双凸轮+毛巾');
INSERT INTO `machine_type` VALUES ('13', '盘带+毛巾');

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
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(10) unsigned NOT NULL COMMENT '订单编号',
  `change_reason` text NOT NULL COMMENT '更改原因',
  `user_id` int(10) unsigned NOT NULL COMMENT '修改订单操作的用户ID，只有创建订单的销售员可以修改订单，或者销售经理',
  `change_time` datetime NOT NULL COMMENT '修改订单的时间',
  PRIMARY KEY (`id`),
  KEY `fk_oc_order_id` (`order_id`),
  KEY `fk_oc_user_id` (`user_id`),
  CONSTRAINT `order_change_record_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`),
  CONSTRAINT `order_change_record_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

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
  `electric_language` varchar(255) DEFAULT NULL,
  `electric_motor` varchar(255) DEFAULT NULL COMMENT '电气：主电机',
  `electric_motor_xy` varchar(255) DEFAULT NULL COMMENT '电气：X,Y电机',
  `electric_trim` varchar(255) DEFAULT NULL COMMENT '电气：剪线方式',
  `electric_power` varchar(255) DEFAULT NULL COMMENT '电气： 电源',
  `electric_switch` varchar(255) DEFAULT NULL COMMENT '电气： 按钮开关',
  `electric_oil` varchar(255) DEFAULT NULL COMMENT '电气： 加油系统',
  `axle_split` varchar(255) DEFAULT NULL COMMENT '上下轴：j夹线器',
  `axle_panel` varchar(255) DEFAULT NULL COMMENT '上下轴：面板',
  `axle_needle` varchar(255) DEFAULT NULL COMMENT '上下轴：机针',
  `axle_needle_type` varchar(255) DEFAULT NULL COMMENT '机针类型',
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
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_detail
-- ----------------------------

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
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_oll_order_id` (`order_id`),
  CONSTRAINT `fk_oll_order_id` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_loading_list
-- ----------------------------

-- ----------------------------
-- Table structure for `order_sign`
-- ----------------------------
DROP TABLE IF EXISTS `order_sign`;
CREATE TABLE `order_sign` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(10) unsigned NOT NULL COMMENT '订单ID',
  `sign_content` text NOT NULL COMMENT '签核内容，以json格式的数组形式存放, 所有项完成后更新status为完成\r\n[ \r\n    {"role_id": 1, "role_name":"技术部"，“person”：“张三”，”comment“: "同意"， ”update_time“:"2017-11-05 12:08:55"},\r\n    {"role_id":2, "role_name":"PMC"，“person”：“李四”，”comment“: "同意，但是部分配件需要新设计"， ”update_time“:"2017-11-06 12:08:55"}\r\n]',
  `create_time` datetime NOT NULL COMMENT '签核流程开始时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `fk_os_order_id` (`order_id`),
  CONSTRAINT `fk_os_order_id` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_sign
-- ----------------------------

-- ----------------------------
-- Table structure for `order_split_record`
-- ----------------------------
DROP TABLE IF EXISTS `order_split_record`;
CREATE TABLE `order_split_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(10) unsigned NOT NULL COMMENT '订单编号',
  `split_reason` text NOT NULL COMMENT '取消原因',
  `user_id` int(10) unsigned NOT NULL COMMENT '拆分订单操作的用户ID，只有创建订单的销售员可以拆分改订单，或者销售经理',
  `split_time` datetime NOT NULL COMMENT '拆分订单的时间',
  PRIMARY KEY (`id`),
  KEY `fk_oc_order_id` (`order_id`),
  KEY `fk_oc_user_id` (`user_id`),
  CONSTRAINT `order_split_record_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`),
  CONSTRAINT `order_split_record_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of process
-- ----------------------------
INSERT INTO `process` VALUES ('4', '剪线金片机型', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"开始\", \"key\":-1, \"loc\":\"209.99999999999997 44\"},\n{\"category\":\"End\", \"text\":\"结束\", \"key\":-4, \"loc\":\"210.00000000000003 739.467688700986\"},\n{\"text\":\"下轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-8, \"loc\":\"209.63748168945312 109.19999998807907\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-5, \"loc\":\"209.63748168945312 224.19999998807907\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-7, \"loc\":\"332.6374816894531 219.19999998807907\"},\n{\"text\":\"台板安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-6, \"loc\":\"210.63748168945312 333.19999998807907\"},\n{\"text\":\"上轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-16, \"loc\":\"209.234375 167\"},\n{\"text\":\"电控安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-9, \"loc\":\"351.234375 378\"},\n{\"text\":\"拉杆安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-10, \"loc\":\"104.234375 382\"},\n{\"text\":\"前驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-3, \"loc\":\"210.234375 386\"},\n{\"text\":\"框架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-11, \"loc\":\"210.234375 444\"},\n{\"text\":\"线架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-12, \"loc\":\"52.234375 240\"},\n{\"text\":\"开机测试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-13, \"loc\":\"210.234375 499\"},\n{\"text\":\"剪线\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-14, \"loc\":\"210.234375 276\"},\n{\"text\":\"出厂检验\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-2, \"loc\":\"210.234375 675\"},\n{\"text\":\"调试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-17, \"loc\":\"210.234375 555\"},\n{\"text\":\"装置安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-18, \"loc\":\"210.234375 620\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.99999999999994,88.6046511627907,209.99999999999994,98.6046511627907,209.99999999999994,98.90232557543489,209.63748168945312,98.90232557543489,209.63748168945312,99.19999998807907,209.63748168945312,109.19999998807907 ]},\n{\"from\":-8, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.63748168945312,142.07544859647751,209.63748168945312,152.07544859647751,209.63748168945312,154.53772429823874,209.234375,154.53772429823874,209.234375,157,209.234375,167 ]},\n{\"from\":-16, \"to\":-5, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.234375,199.87544860839844,209.234375,209.87544860839844,209.234375,212.03772429823874,209.63748168945312,212.03772429823874,209.63748168945312,214.19999998807907,209.63748168945312,224.19999998807907 ]},\n{\"from\":-5, \"to\":-14, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.63748168945312,257.0754485964775,209.63748168945312,267.0754485964775,209.93592834472656,267.0754485964775,209.93592834472656,266,210.234375,266,210.234375,276 ]},\n{\"from\":-14, \"to\":-6, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.234375,308.8754486083984,210.234375,318.8754486083984,210.234375,321.03772429823874,210.63748168945312,321.03772429823874,210.63748168945312,323.19999998807907,210.63748168945312,333.19999998807907 ]},\n{\"from\":-6, \"to\":-3, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.63748168945312,366.0754485964775,210.63748168945312,376.0754485964775,210.43592834472656,376.0754485964775,210.43592834472656,376,210.234375,376,210.234375,386 ]},\n{\"from\":-3, \"to\":-11, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.234375,418.8754486083984,210.234375,428.8754486083984,210.234375,431.4377243041992,210.234375,431.4377243041992,210.234375,434,210.234375,444 ]},\n{\"from\":-11, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.234375,476.8754486083984,210.234375,486.8754486083984,210.234375,487.9377243041992,210.234375,487.9377243041992,210.234375,489,210.234375,499 ]},\n{\"from\":-13, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.234375,531.8754486083984,210.234375,541.8754486083984,210.234375,543.4377243041993,210.234375,543.4377243041993,210.234375,545,210.234375,555 ]},\n{\"from\":-8, \"to\":-12, \"fromPort\":\"L\", \"toPort\":\"T\", \"points\":[ 171.13748168945312,125.63772429227829,161.13748168945312,125.63772429227829,52.234375,125.63772429227829,52.234375,177.81886214613914,52.234375,230,52.234375,240 ]},\n{\"from\":-8, \"to\":-7, \"fromPort\":\"R\", \"toPort\":\"T\", \"points\":[ 248.13748168945312,125.63772429227829,258.1374816894531,125.63772429227829,332.6374816894531,125.63772429227829,332.6374816894531,167.41886214017867,332.6374816894531,209.19999998807907,332.6374816894531,219.19999998807907 ]},\n{\"from\":-6, \"to\":-10, \"fromPort\":\"L\", \"toPort\":\"T\", \"points\":[ 172.13748168945312,349.6377242922783,162.13748168945312,349.6377242922783,104.234375,349.6377242922783,104.234375,360.81886214613917,104.234375,372,104.234375,382 ]},\n{\"from\":-6, \"to\":-9, \"fromPort\":\"R\", \"toPort\":\"T\", \"points\":[ 249.13748168945312,349.6377242922783,259.1374816894531,349.6377242922783,351.234375,349.6377242922783,351.234375,358.81886214613917,351.234375,368,351.234375,378 ]},\n{\"from\":-10, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 104.234375,414.8754486083984,104.234375,424.8754486083984,104.234375,515.4377243041993,132.984375,515.4377243041993,161.734375,515.4377243041993,171.734375,515.4377243041993 ]},\n{\"from\":-9, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 351.234375,410.8754486083984,351.234375,420.8754486083984,351.234375,515.4377243041993,304.984375,515.4377243041993,258.734375,515.4377243041993,248.734375,515.4377243041993 ]},\n{\"from\":-12, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 52.234375,272.8754486083984,52.234375,282.8754486083984,52.234375,515.4377243041993,106.984375,515.4377243041993,161.734375,515.4377243041993,171.734375,515.4377243041993 ]},\n{\"from\":-7, \"to\":-6, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 332.6374816894531,252.07544859647751,332.6374816894531,262.0754485964775,332.6374816894531,260,332.6374816894531,260,332.6374816894531,316,210.63748168945312,316,210.63748168945312,323.19999998807907,210.63748168945312,333.19999998807907 ]},\n{\"from\":-2, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.234375,707.8754486083984,210.234375,717.8754486083984,210.234375,723.6715686546922,210.00000000000003,723.6715686546922,210.00000000000003,729.467688700986,210.00000000000003,739.467688700986 ]},\n{\"from\":-17, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[210.234375,587.8754486083984,210.234375,597.8754486083984,210.234375,603.9377243041993,210.234375,603.9377243041993,210.234375,610,210.234375,620]},\n{\"from\":-18, \"to\":-2, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[210.234375,652.8754486083984,210.234375,662.8754486083984,210.234375,663.9377243041993,210.234375,663.9377243041993,210.234375,665,210.234375,675]}\n ]}', '2018-04-01 09:25:19', '2018-04-17 00:36:19');
INSERT INTO `process` VALUES ('5', '普通不剪线机型', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"开始\", \"key\":-1, \"loc\":\"207 39.99999999999999\"},\n{\"category\":\"End\", \"text\":\"结束\", \"key\":-4, \"loc\":\"206.99999999999997 717.2676887129069\"},\n{\"text\":\"下轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-8, \"loc\":\"207.2465303060486 117.03934352855347\"},\n{\"text\":\"上轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-9, \"loc\":\"207.2465303060486 180.0393435285535\"},\n{\"text\":\"拉杆安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-5, \"loc\":\"106.234375 422\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-11, \"loc\":\"207.234375 250\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-14, \"loc\":\"322.234375 264\"},\n{\"text\":\"台板安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-13, \"loc\":\"207.234375 330\"},\n{\"text\":\"前驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-3, \"loc\":\"207.234375 387\"},\n{\"text\":\"框架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-7, \"loc\":\"207.234375 461\"},\n{\"text\":\"电控安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-6, \"loc\":\"323.234375 421\"},\n{\"text\":\"线架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-12, \"loc\":\"55.234375 263\"},\n{\"text\":\"调试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-10, \"loc\":\"207.234375 590\"},\n{\"text\":\"开机测试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-15, \"loc\":\"207.234375 530\"},\n{\"text\":\"出厂检验\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-2, \"loc\":\"207.234375 661\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207,84.6046511627907,207,94.6046511627907,207,100.82199734567209,207.2465303060486,100.82199734567209,207.2465303060486,107.03934352855347,207.2465303060486,117.03934352855347 ]},\n{\"from\":-8, \"to\":-9, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.2465303060486,149.91479213695192,207.2465303060486,159.91479213695192,207.2465303060486,164.9770678327527,207.2465303060486,164.9770678327527,207.2465303060486,170.0393435285535,207.2465303060486,180.0393435285535 ]},\n{\"from\":-9, \"to\":-11, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.2465303060486,212.91479213695195,207.2465303060486,222.91479213695195,207.2465303060486,231.457396068476,207.234375,231.457396068476,207.234375,240,207.234375,250 ]},\n{\"from\":-8, \"to\":-14, \"fromPort\":\"R\", \"toPort\":\"T\", \"points\":[ 245.7465303060486,133.47706783275268,255.7465303060486,133.47706783275268,322.234375,133.47706783275268,322.234375,193.73853391637635,322.234375,254,322.234375,264 ]},\n{\"from\":-8, \"to\":-12, \"fromPort\":\"L\", \"toPort\":\"T\", \"points\":[ 168.7465303060486,133.47706783275268,158.7465303060486,133.47706783275268,55.234375,133.47706783275268,55.234375,193.23853391637635,55.234375,253,55.234375,263 ]},\n{\"from\":-11, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,282.8754486083984,207.234375,292.8754486083984,207.234375,306.4377243041992,207.234375,306.4377243041992,207.234375,320,207.234375,330 ]},\n{\"from\":-13, \"to\":-3, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,362.8754486083984,207.234375,372.8754486083984,207.234375,374.9377243041992,207.234375,374.9377243041992,207.234375,377,207.234375,387 ]},\n{\"from\":-12, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 55.234375,295.8754486083984,55.234375,305.8754486083984,55.234375,546.4377243041993,106.984375,546.4377243041993,158.734375,546.4377243041993,168.734375,546.4377243041993 ]},\n{\"from\":-13, \"to\":-5, \"fromPort\":\"L\", \"toPort\":\"T\", \"points\":[ 168.734375,346.4377243041992,158.734375,346.4377243041992,106.234375,346.4377243041992,106.234375,379.21886215209963,106.234375,412,106.234375,422 ]},\n{\"from\":-13, \"to\":-6, \"fromPort\":\"R\", \"toPort\":\"T\", \"points\":[ 245.734375,346.4377243041992,255.734375,346.4377243041992,323.234375,346.4377243041992,323.234375,378.71886215209963,323.234375,411,323.234375,421 ]},\n{\"from\":-5, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 106.234375,454.8754486083984,106.234375,464.8754486083984,106.234375,546.4377243041993,132.484375,546.4377243041993,158.734375,546.4377243041993,168.734375,546.4377243041993 ]},\n{\"from\":-6, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 323.234375,453.8754486083984,323.234375,463.8754486083984,323.234375,546.4377243041993,289.484375,546.4377243041993,255.734375,546.4377243041993,245.734375,546.4377243041993 ]},\n{\"from\":-3, \"to\":-7, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,419.8754486083984,207.234375,429.8754486083984,207.234375,440.4377243041992,207.234375,440.4377243041992,207.234375,451,207.234375,461 ]},\n{\"from\":-7, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,493.8754486083984,207.234375,503.8754486083984,207.234375,511.9377243041992,207.234375,511.9377243041992,207.234375,520,207.234375,530 ]},\n{\"from\":-15, \"to\":-10, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[207.234375,562.8754486083984,207.234375,572.8754486083984,207.234375,576.4377243041993,207.234375,576.4377243041993,207.234375,580,207.234375,590]},\n{\"from\":-14, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 322.234375,296.8754486083984,322.234375,306.8754486083984,322.234375,313.4377243041992,207.234375,313.4377243041992,207.234375,320,207.234375,330 ]},\n{\"from\":-10, \"to\":-2, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[207.234375,622.8754486083984,207.234375,632.8754486083984,207.234375,641.9377243041993,207.234375,641.9377243041993,207.234375,651,207.234375,661]},\n{\"from\":-2, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[207.234375,693.8754486083984,207.234375,703.8754486083984,207.234375,705.5715686606527,206.99999999999997,705.5715686606527,206.99999999999997,707.2676887129069,206.99999999999997,717.2676887129069]}\n ]}', '2018-04-14 02:44:47', '2018-04-17 00:16:39');
INSERT INTO `process` VALUES ('6', '普通剪线机型', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"开始\", \"key\":-1, \"loc\":\"211.09924853207326 1.3648093947311892\"},\n{\"category\":\"End\", \"text\":\"结束\", \"key\":-4, \"loc\":\"211.67026211658538 717.560456340184\"},\n{\"text\":\"下轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-10, \"loc\":\"211.05664223090022 67.26529369957018\"},\n{\"text\":\"上轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-11, \"loc\":\"211.32160923033257 122.66470535321986\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-6, \"loc\":\"211.5803069047959 191.6342724376983\"},\n{\"text\":\"剪线\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-7, \"loc\":\"211.38207194384694 245.3045345542838\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-9, \"loc\":\"346.25449133038336 220.83054624414626\"},\n{\"text\":\"台板安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-8, \"loc\":\"211.31337875086223 331.26462237564795\"},\n{\"text\":\"框架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-2, \"loc\":\"211.84625448147878 467.0055388397189\"},\n{\"text\":\"调试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-5, \"loc\":\"211.71573213626368 594.2096572641715\"},\n{\"text\":\"电控安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-13, \"loc\":\"320.1239689851683 429.979699557122\"},\n{\"text\":\"拉杆安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-14, \"loc\":\"110.65625 430.66666650772095\"},\n{\"text\":\"线架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-15, \"loc\":\"53.65625 222.66666650772095\"},\n{\"text\":\"出厂检验\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-16, \"loc\":\"211.65625 656.666666507721\"},\n{\"text\":\"前驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-17, \"loc\":\"211.65625000000003 398.6666666865348\"},\n{\"text\":\"开机测试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-18, \"loc\":\"211.65625 535.3333333432674\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-10, \"to\":-11, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.05664223090022,100.14074230796862,211.05664223090022,110.14074230796862,211.05664223090022,111.40272383059424,211.32160923033257,111.40272383059424,211.32160923033257,112.66470535321986,211.32160923033257,122.66470535321986 ]},\n{\"from\":-1, \"to\":-10, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.09924853207326,45.96946055752189,211.09924853207326,55.96946055752189,211.09924853207326,56.61737712854604,211.05664223090022,56.61737712854604,211.05664223090022,57.26529369957018,211.05664223090022,67.26529369957018 ]},\n{\"from\":-11, \"to\":-6, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.32160923033257,155.5401539616183,211.32160923033257,165.5401539616183,211.32160923033257,173.58721319965832,211.5803069047959,173.58721319965832,211.5803069047959,181.6342724376983,211.5803069047959,191.6342724376983 ]},\n{\"from\":-6, \"to\":-7, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.5803069047959,224.50972104609676,211.5803069047959,234.50972104609676,211.5803069047959,234.90712780019027,211.38207194384694,234.90712780019027,211.38207194384694,235.3045345542838,211.38207194384694,245.3045345542838 ]},\n{\"from\":-10, \"to\":-9, \"fromPort\":\"R\", \"toPort\":\"T\", \"points\":[ 249.55664223090022,83.7030180037694,259.55664223090025,83.7030180037694,346.25449133038336,83.7030180037694,346.25449133038336,147.26678212395782,346.25449133038336,210.83054624414626,346.25449133038336,220.83054624414626 ]},\n{\"from\":-7, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.38207194384694,278.1799831626822,211.38207194384694,288.1799831626822,211.38207194384694,304.7223027691651,211.31337875086223,304.7223027691651,211.31337875086223,321.26462237564795,211.31337875086223,331.26462237564795 ]},\n{\"from\":-8, \"to\":-13, \"fromPort\":\"R\", \"toPort\":\"T\", \"points\":[ 249.81337875086223,347.70234667984715,259.8133787508622,347.70234667984715,320.1239689851683,347.70234667984715,320.1239689851683,383.8410231184846,320.1239689851683,419.979699557122,320.1239689851683,429.979699557122 ]},\n{\"from\":-10, \"to\":-15, \"fromPort\":\"L\", \"toPort\":\"T\", \"points\":[ 172.55664223090022,83.7030180037694,162.55664223090022,83.7030180037694,53.65625,83.7030180037694,53.65625,148.18484225574517,53.65625,212.66666650772095,53.65625,222.66666650772095 ]},\n{\"from\":-8, \"to\":-14, \"fromPort\":\"L\", \"toPort\":\"T\", \"points\":[ 172.81337875086223,347.70234667984715,162.81337875086223,347.70234667984715,110.65625,347.70234667984715,110.65625,384.184506593784,110.65625,420.66666650772095,110.65625,430.66666650772095 ]},\n{\"from\":-16, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.65625,689.5421151161194,211.65625,699.5421151161194,211.65625,703.5512857281517,211.67026211658538,703.5512857281517,211.67026211658538,707.560456340184,211.67026211658538,717.560456340184 ]},\n{\"from\":-8, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.31337875086223,364.14007098404636,211.31337875086223,374.14007098404636,211.31337875086223,381.4033688352906,211.65625000000003,381.4033688352906,211.65625000000003,388.6666666865348,211.65625000000003,398.6666666865348 ]},\n{\"from\":-17, \"to\":-2, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.65625000000003,431.54211529493324,211.65625000000003,441.54211529493324,211.65625000000003,449.27382706732607,211.84625448147878,449.27382706732607,211.84625448147878,457.0055388397189,211.84625448147878,467.0055388397189 ]},\n{\"from\":-9, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 346.25449133038336,253.7059948525447,346.25449133038336,263.7059948525447,346.25449133038336,292.4853086140963,211.31337875086223,292.4853086140963,211.31337875086223,321.26462237564795,211.31337875086223,331.26462237564795 ]},\n{\"from\":-2, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.84625448147878,499.8809874481173,211.84625448147878,509.8809874481173,211.84625448147878,517.6071603956924,211.65625,517.6071603956924,211.65625,525.3333333432674,211.65625,535.3333333432674 ]},\n{\"from\":-18, \"to\":-5, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.65625,568.2087819516659,211.65625,578.2087819516659,211.65625,581.2092196079186,211.71573213626368,581.2092196079186,211.71573213626368,584.2096572641715,211.71573213626368,594.2096572641715 ]},\n{\"from\":-5, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.71573213626368,627.0851058725699,211.71573213626368,637.0851058725699,211.71573213626368,641.8758861901454,211.65625,641.8758861901454,211.65625,646.666666507721,211.65625,656.666666507721 ]},\n{\"from\":-13, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 320.1239689851683,462.8551481655204,320.1239689851683,472.8551481655204,320.1239689851683,551.7710576474667,290.14010949258414,551.7710576474667,260.15625,551.7710576474667,250.15625,551.7710576474667 ]},\n{\"from\":-14, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 110.65625,463.54211511611936,110.65625,473.54211511611936,110.65625,551.7710576474667,136.90625,551.7710576474667,163.15625,551.7710576474667,173.15625,551.7710576474667 ]},\n{\"from\":-15, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 53.65625,255.5421151161194,53.65625,265.54211511611936,53.65625,551.7710576474667,108.40625,551.7710576474667,163.15625,551.7710576474667,173.15625,551.7710576474667 ]}\n ]}', '2018-04-14 05:29:05', '2018-04-17 00:35:35');

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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of process_record
-- ----------------------------

-- ----------------------------
-- Table structure for `quality_record_image`
-- ----------------------------
DROP TABLE IF EXISTS `quality_record_image`;
CREATE TABLE `quality_record_image` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `task_quality_record_id` int(10) unsigned NOT NULL,
  `image` varchar(1000) NOT NULL COMMENT '异常图片名称（包含路径）,以后这部分数据是最大的，首先pad上传时候时候需要压缩，以后硬盘扩展的话，可以把几几年的图片放置到另外一个硬盘，然后pad端响应升级（根据时间加上图片的路径）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `fk_task_quality_record_id` (`task_quality_record_id`),
  CONSTRAINT `fk_task_quality_record_id` FOREIGN KEY (`task_quality_record_id`) REFERENCES `task_quality_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of quality_record_image
-- ----------------------------

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
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '超级管理员', '系统后台管理', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[],\"machine\":[\"/home/machine/machine_install_process\",\"/home/machine/machine_config_process\"],\"plan\":[],\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_quality_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":[\"/home/task/process_manage\",\"/home/task/task_content_manage\"],\"system\":[\"/home/system/user_manage\",\"/home/system/install_group_manage\",\"/home/system/role_manage\",\"/home/system/device_manager\"]}');
INSERT INTO `role` VALUES ('2', '生产部管理员', '主要手机上传位置、查看机器安装状态', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":[\"/home/machine/machine_install_process\",\"/home/machine/machine_config_process\"],\"plan\":[],\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":[\"/home/task/process_manage\",\"/home/task/task_content_manage\"],\"system\":null}');
INSERT INTO `role` VALUES ('3', '安装组长', '安装前后扫描机器', '{\"contract\":null,\"order\":null,\"machine\":null,\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('4', '生产部经理', '订单审批', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":[\"/home/machine/machine_config_process\",\"/home/machine/machine_install_process\"],\"plan\":[],\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":[\"/home/task/task_content_manage\",\"/home/task/process_manage\"],\"system\":null}');
INSERT INTO `role` VALUES ('5', '普通员工', '浏览一般网页信息', '{\"contract\":null,\"order\":[],\"machine\":[\"/home/machine/machine_install_process\"],\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('6', '总经理', '订单审核等其他可配置权限', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[],\"machine\":[\"/home/machine/machine_config_process\",\"/home/machine/machine_install_process\"],\"plan\":[],\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":[\"/home/task/task_content_manage\",\"/home/task/process_manage\"],\"system\":null}');
INSERT INTO `role` VALUES ('7', '销售部经理', '订单审批', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":[\"/home/machine/machine_install_process\"],\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('8', '技术部经理', '订单审批', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":[\"/home/machine/machine_install_process\"],\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('9', '销售员', '录入订单', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":null,\"plan\":null,\"abnormal\":null,\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('10', '技术员', '上传装车单，联系单', '{\"contract\":null,\"order\":[],\"machine\":[\"/home/machine/machine_install_process\"],\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('11', '质检员', 'pad上操作', '{\"contract\":null,\"order\":[],\"machine\":[\"/home/machine/machine_install_process\"],\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('12', 'PMC', '生产计划', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":[\"/home/machine/machine_config_process\",\"/home/machine/machine_install_process\"],\"plan\":[],\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":[\"/home/task/task_content_manage\",\"/home/task/process_manage\"],\"system\":null}');
INSERT INTO `role` VALUES ('13', '成本核算员', '成本核算', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[],\"machine\":null,\"plan\":null,\"abnormal\":null,\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('14', '财务经理', '合同合规性检查', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":null,\"plan\":null,\"abnormal\":null,\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('15', '财务会计', '订金确认', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":null,\"plan\":null,\"abnormal\":null,\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('16', '质检组长', '质检组长', '{\"contract\":null,\"order\":null,\"machine\":null,\"plan\":null,\"abnormal\":null,\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('17', '安装工', '安装工', '{\"contract\":null,\"order\":null,\"machine\":null,\"plan\":null,\"abnormal\":null,\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('18', '内贸经理', '订单审批', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":[\"/home/machine/machine_install_process\"],\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\"],\"task\":null,\"system\":null}');

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
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sign_process
-- ----------------------------
INSERT INTO `sign_process` VALUES ('4', '改单签核流程', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\"},{\"number\":4,\"roleId\":14,\"signType\":\"合同签核\"},{\"number\":5,\"roleId\":6,\"signType\":\"合同签核\"}]', '2017-12-12 01:14:40', '2017-12-26 08:30:28');
INSERT INTO `sign_process` VALUES ('3', '正常签核流程', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\"},{\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\"},{\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\"},{\"number\":8,\"roleId\":12,\"signType\":\"需求单签核\"}]', '2017-12-11 23:57:56', '2018-04-01 08:50:12');
INSERT INTO `sign_process` VALUES ('5', '拆单流程', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\"},{\"number\":4,\"roleId\":14,\"signType\":\"合同签核\"},{\"number\":5,\"roleId\":6,\"signType\":\"合同签核\"}]', '2018-01-23 09:59:38', '2018-01-23 10:01:32');

-- ----------------------------
-- Table structure for `task`
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) NOT NULL COMMENT '安装作业项的名称',
  `quality_user_id` int(10) unsigned DEFAULT NULL COMMENT '质检用户的ID',
  `group_id` int(10) unsigned DEFAULT NULL COMMENT '安装小组id',
  `guidance` text COMMENT '作业指导，后续可能会需要（一般是html格式）',
  PRIMARY KEY (`id`),
  KEY `fk_t_group_id` (`group_id`),
  KEY `task_name` (`task_name`),
  KEY `fk_t_quality_user_id` (`quality_user_id`),
  CONSTRAINT `fk_t_group_id` FOREIGN KEY (`group_id`) REFERENCES `install_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('1', '上轴安装', '27', '1', '');
INSERT INTO `task` VALUES ('2', '下轴安装', '27', '2', '');
INSERT INTO `task` VALUES ('3', '驱动安装', '26', '3', '');
INSERT INTO `task` VALUES ('4', '台板安装', '26', '4', '');
INSERT INTO `task` VALUES ('5', '电控', '26', '5', '');
INSERT INTO `task` VALUES ('6', '针杆架安装', '167', '7', '');
INSERT INTO `task` VALUES ('7', '调试', '27', '8', '');
INSERT INTO `task` VALUES ('8', '剪线', '25', '9', '');
INSERT INTO `task` VALUES ('9', '金片安装', '27', '10', '');
INSERT INTO `task` VALUES ('10', '框架安装', '26', '3', '');
INSERT INTO `task` VALUES ('11', '电控安装', null, '5', '');
INSERT INTO `task` VALUES ('12', '线架安装', null, '11', '');
INSERT INTO `task` VALUES ('13', '拉杆安装', '25', '12', '');
INSERT INTO `task` VALUES ('14', '前驱动安装', '26', '3', '');
INSERT INTO `task` VALUES ('15', '出厂检验', '25', '14', '');
INSERT INTO `task` VALUES ('17', '开机测试', null, '15', '');
INSERT INTO `task` VALUES ('19', '装置安装', null, '10', '');

-- ----------------------------
-- Table structure for `task_plan`
-- ----------------------------
DROP TABLE IF EXISTS `task_plan`;
CREATE TABLE `task_plan` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `task_record_id` int(10) unsigned NOT NULL COMMENT '对应taskj记录的id',
  `plan_type` tinyint(4) unsigned NOT NULL COMMENT '计划类型（日计划、弹性计划）',
  `plan_time` datetime DEFAULT NULL COMMENT 'task的计划完成时间',
  `deadline` datetime DEFAULT NULL COMMENT '在彈性模式下，完成的截止时间',
  `user_id` int(10) unsigned NOT NULL COMMENT '添加计划的人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '上一次更改计划的时间',
  PRIMARY KEY (`id`),
  KEY `fk_tp_task_record_id` (`task_record_id`),
  KEY `fk_tp_user_id` (`user_id`),
  CONSTRAINT `fk_tp_task_record_id` FOREIGN KEY (`task_record_id`) REFERENCES `task_record` (`id`),
  CONSTRAINT `fk_tp_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_plan
-- ----------------------------

-- ----------------------------
-- Table structure for `task_quality_record`
-- ----------------------------
DROP TABLE IF EXISTS `task_quality_record`;
CREATE TABLE `task_quality_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `abnormal_type` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '质检异常类型，目前未使用，default值为1',
  `task_record_id` int(10) unsigned NOT NULL COMMENT '对应安装项ID',
  `submit_user` varchar(255) NOT NULL COMMENT '提交质检异常的用户',
  `comment` text COMMENT '质检备注',
  `solution` text COMMENT '解决方法',
  `solution_user` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '添加质检结果的时间',
  `solve_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tqr_task_record_id` (`task_record_id`),
  CONSTRAINT `tqr_task_record_id` FOREIGN KEY (`task_record_id`) REFERENCES `task_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_quality_record
-- ----------------------------

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
  `install_begin_time` datetime DEFAULT NULL,
  `install_end_time` datetime DEFAULT NULL,
  `quality_begin_time` datetime DEFAULT NULL COMMENT 'task开始时间',
  `quality_end_time` datetime DEFAULT NULL COMMENT 'task结束时间',
  PRIMARY KEY (`id`),
  KEY `fk_tr_process_record_id` (`process_record_id`),
  KEY `fk_tr_task_name` (`task_name`),
  CONSTRAINT `fk_tr_process_record_id` FOREIGN KEY (`process_record_id`) REFERENCES `process_record` (`id`),
  CONSTRAINT `fk_tr_task_name` FOREIGN KEY (`task_name`) REFERENCES `task` (`task_name`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_record
-- ----------------------------

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
  CONSTRAINT `fk_user_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', 'admin', '1', 'sinsim', '0', '1');
INSERT INTO `user` VALUES ('7', 'azzz-sz', 'azzz-sz', '3', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('12', 'ptyg', 'ptyg', '5', 'sinsim', '0', '1');
INSERT INTO `user` VALUES ('13', 'xsbjl', 'xsb', '7', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('14', 'jsbjl', 'jsb', '8', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('15', 'zjl', 'zjl', '6', 'sinsim', '0', '1');
INSERT INTO `user` VALUES ('16', '谢侃', '谢侃', '9', 'sinsim', '0', '1');
INSERT INTO `user` VALUES ('17', 'pmc', 'pcm-1', '12', 'sinsim', '0', '1');
INSERT INTO `user` VALUES ('18', 'cbhsy', 'cbhsy-1', '13', 'sinsim', '0', '1');
INSERT INTO `user` VALUES ('19', 'cwjl', 'cwjl1', '14', 'sinsim', '0', '1');
INSERT INTO `user` VALUES ('25', '徐锡康', '徐锡康', '11', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('26', '王叠松', '王叠松', '11', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('27', '刘林', '刘林', '11', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('28', '孟佳飞', '孟佳飞', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('29', '王国娜', '王国娜', '3', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('30', '李霞', '李霞', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('31', '宣小华', '宣小华', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('32', '何承凤', '何承凤', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('33', '陈小英', '陈小英', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('34', '王丹飞', '王丹飞', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('35', '骆钰洁', '骆钰洁', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('36', '陈秀琴', '陈秀琴', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('37', '赵燕红', '赵燕红', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('38', '赵丽霞', '赵丽霞', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('39', '俞红萍', '俞红萍', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('40', '孙兰华', '孙兰华', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('41', '郑国平', '郑国平', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('42', '饶桂枝', '饶桂枝', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('43', '骆利淼', '骆利淼', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('44', '胡尚连', '胡尚连', '17', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('45', '何赵军', '何赵军', '3', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('46', '王飞', '王飞', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('47', '陈炯苗', '陈炯苗', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('48', '斯校军', '斯校军', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('49', '张文龙', '张文龙', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('50', '何海潮', '何海潮', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('51', '章方斌', '章方斌', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('52', '张强', '张强', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('53', '郑锴', '郑锴', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('54', '方泽锋', '方泽锋', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('55', '章钟铭', '章钟铭', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('56', '王艳', '王艳', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('57', '王荣燕', '王艳', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('58', '张叶峰', '张叶峰', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('59', '贺伟', '贺伟', '17', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('60', '陈镇波', '陈镇波', '3', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('61', '陆铮', '陆铮', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('62', '宣浩龙', '宣浩龙', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('63', '韩先成', '韩先成', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('64', '陈铁威', '陈铁威', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('65', '马越柯', '马越柯', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('66', '徐佳龙', '徐佳龙', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('67', '陈益锋', '陈益锋', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('68', '章建达', '章建达', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('69', '徐迪', '徐迪', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('70', '王君', '王君', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('71', '郑茗友', '郑茗友', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('72', '王阿妹', '王阿妹', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('73', '吴鹏飞', '吴鹏飞', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('74', '郑梧', '郑梧', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('75', '向春林', '向春林', '17', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('76', '张斌', '张斌', '3', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('77', '徐银风', '徐银风', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('78', '何洪锋', '何洪锋', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('79', '韩海强', '韩海强', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('80', '阮鑫钢', '阮鑫钢', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('81', '袁伯钿', '袁伯钿', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('82', '杨瑞', '杨瑞', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('83', '卓欢其', '卓欢其', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('84', '郑国涛', '郑国涛', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('85', '魏权峰', '魏权峰', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('86', '方颖丰', '方颖丰', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('87', '陶百伟', '陶百伟', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('88', '楼建锋', '楼建锋', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('89', '郑水锋', '郑水锋', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('90', '章瑜', '章瑜', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('91', '蔡辉辉', '蔡辉辉', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('92', '王烜波', '王烜波', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('93', '袁涛', '袁涛', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('94', '马雄伟', '马雄伟', '17', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('95', '金少军', '金少军', '3', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('96', '张海中', '张海中', '17', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('97', '余斌江', '余斌江', '17', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('98', '毛杭斌', '毛杭斌', '17', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('99', '毛陈波', '毛陈波', '17', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('100', '伍瑞林', '伍瑞林', '17', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('101', '李润', '李润', '17', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('102', '宣汉江', '宣汉江', '17', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('103', '魏叶威', '魏叶威', '17', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('104', '陈天龙', '陈天龙', '17', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('105', '周安', '周安', '17', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('106', '郭镓楠', '郭镓楠', '17', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('107', '何天钢', '何天钢', '17', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('108', '周志祥', '周志祥', '3', 'sinsim', '4', '1');
INSERT INTO `user` VALUES ('109', '宣言梁', '宣言梁', '17', 'sinsim', '4', '1');
INSERT INTO `user` VALUES ('110', '郭海强', '郭海强', '17', 'sinsim', '4', '1');
INSERT INTO `user` VALUES ('111', '龙江', '龙江', '17', 'sinsim', '4', '1');
INSERT INTO `user` VALUES ('112', '吴凡', '吴凡', '17', 'sinsim', '4', '1');
INSERT INTO `user` VALUES ('113', '付中亚', '付中亚', '17', 'sinsim', '4', '1');
INSERT INTO `user` VALUES ('114', '舒孝欢', '舒孝欢', '17', 'sinsim', '4', '1');
INSERT INTO `user` VALUES ('115', '高欢欢', '高欢欢', '17', 'sinsim', '4', '1');
INSERT INTO `user` VALUES ('116', '曾祥平', '曾祥平', '17', 'sinsim', '4', '1');
INSERT INTO `user` VALUES ('117', '方毅', '方毅', '3', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('118', '丁文', '丁文', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('119', '许金龙', '许金龙', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('120', '陈钱栋', '陈钱栋', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('121', '蒋峰', '蒋峰', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('122', '刘伟', '刘伟', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('123', '汤剑', '汤剑', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('124', '周光焱', '周光焱', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('125', '邬润杰', '邬润杰', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('126', '陈可女', '陈可女', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('127', '姚远平', '姚远平', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('128', '杨海军', '杨海军', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('129', '毛锡伟', '毛锡伟', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('130', '李坤鹏', '李坤鹏', '17', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('131', '王新全', '王新全', '11', 'sinsim', '0', '1');
INSERT INTO `user` VALUES ('132', '陈益锋2', '陈益锋2', '17', 'sinsim', '9', '1');
INSERT INTO `user` VALUES ('133', '余铮宇', '余铮宇', '17', 'sinsim', '9', '1');
INSERT INTO `user` VALUES ('134', '宣焕强', '宣焕强', '17', 'sinsim', '9', '1');
INSERT INTO `user` VALUES ('135', '阮少杰', '阮少杰', '17', 'sinsim', '9', '1');
INSERT INTO `user` VALUES ('136', '钱盛', '钱盛', '17', 'sinsim', '9', '1');
INSERT INTO `user` VALUES ('137', '章正国', '章正国', '17', 'sinsim', '9', '1');
INSERT INTO `user` VALUES ('138', '周桂新', '周桂新', '17', 'sinsim', '9', '1');
INSERT INTO `user` VALUES ('139', '侯棋', '侯棋', '17', 'sinsim', '9', '1');
INSERT INTO `user` VALUES ('140', '宣锡阳', '宣锡阳', '17', 'sinsim', '9', '1');
INSERT INTO `user` VALUES ('141', '严玮杰', '严玮杰', '17', 'sinsim', '9', '1');
INSERT INTO `user` VALUES ('142', '王朴卡', '王朴卡', '3', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('143', '胡夏飞', '胡夏飞', '17', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('144', '徐孝栋', '徐孝栋', '17', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('145', '方陈勇', '方陈勇', '17', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('146', '郭忠梁', '郭忠梁', '17', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('147', '陈燕丰', '陈燕丰', '17', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('148', '卓永福', '卓永福', '17', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('149', '吕翔', '吕翔', '17', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('150', '王威', '王威', '17', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('151', '杨忠', '杨忠', '17', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('152', '楼飞翔', '楼飞翔', '17', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('153', '吴明枝', '吴明枝', '17', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('154', '蒋立奇', '蒋立奇', '16', 'sinsim', '0', '1');
INSERT INTO `user` VALUES ('155', 'scbgly', 'scbgly', '2', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('156', 'scbjl', 'scbjl', '4', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('157', 'jsy', 'jsy', '10', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('158', 'cwkj', 'cwkj-1', '15', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('159', 'azzz-xz', 'azzz-xz', '3', 'sinsim', '2', '1');
INSERT INTO `user` VALUES ('160', 'azzz-qd', 'azzz-qd', '3', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('161', 'azzz-tb', 'azzz-tb', '3', 'sinsim', '4', '1');
INSERT INTO `user` VALUES ('162', 'azzz-dk', 'azzz-dk', '3', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('163', 'azzz-zgj', 'azzz-zgj', '3', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('164', 'azzz-ts', 'azzz-ts', '3', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('165', 'azzz-jx', 'azzz-jx', '3', 'sinsim', '9', '1');
INSERT INTO `user` VALUES ('166', 'azzz-jp', 'azzz-jp', '3', 'sinsim', '10', '1');
INSERT INTO `user` VALUES ('167', '郑培军', '郑培军', '11', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('168', '王杰', '王杰', '3', 'sinsim', '15', '1');
INSERT INTO `user` VALUES ('169', '吕春蓓', '吕春蓓', '2', '678089', null, '1');
INSERT INTO `user` VALUES ('170', '杨金魁', '杨金魁', '2', '672821', null, '1');
INSERT INTO `user` VALUES ('171', '斯华锋', '斯华锋', '2', '004856', null, '1');
INSERT INTO `user` VALUES ('172', 'victor', '彭胜利', '1', 'sheng.5566', null, '1');
INSERT INTO `user` VALUES ('173', '斯雯', '斯雯', '9', '678937', null, '1');
INSERT INTO `user` VALUES ('174', '周婷青', '周婷青', '9', '513552', null, '1');
INSERT INTO `user` VALUES ('175', '曹建挺', '曹建挺', '7', '514230', null, '1');
INSERT INTO `user` VALUES ('176', '周颖', '周颖', '9', '655201', null, '1');
INSERT INTO `user` VALUES ('177', '姚娟芝', '姚娟芝', '9', '673101', null, '1');
INSERT INTO `user` VALUES ('178', '陈佳枝', '陈佳枝', '9', '054227', null, '1');
INSERT INTO `user` VALUES ('179', '骆晓军', '骆晓军', '7', '515720', null, '1');
