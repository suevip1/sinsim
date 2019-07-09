/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50553
Source Host           : localhost:3306
Source Database       : sinsim_db

Target Server Type    : MYSQL
Target Server Version : 50553
File Encoding         : 65001

Date: 2018-06-11 00:06:27
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `market_group_name` varchar(255) NOT NULL COMMENT '销售组信息',
  `currency_type` varchar(255) NOT NULL COMMENT '币种',
  `mark` text COMMENT '合同备注信息，有填单员上填入',
  `status` tinyint(4) unsigned NOT NULL COMMENT '合同状态',
  `create_time` datetime NOT NULL COMMENT '填表时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新table的时间',
  `record_user` varchar(255) DEFAULT NULL COMMENT '录单人员',
  `is_valid` varchar(4) NOT NULL DEFAULT '1' COMMENT '指示合同是否有效，用于删除标记，可以理解为作废单据',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES ('16', 'sinsim-9', '868619032663401');
INSERT INTO `device` VALUES ('15', 'sinsim-8', '866764032335774');
INSERT INTO `device` VALUES ('14', 'sinsim-7', '866764032375473');
INSERT INTO `device` VALUES ('7', 'sinsim-1', '868619033691989');
INSERT INTO `device` VALUES ('13', 'sinsim-2', '866764032345773');
INSERT INTO `device` VALUES ('9', 'sinsim-3', '866764032351995');
INSERT INTO `device` VALUES ('10', 'sinsim-4', '868619034158947');
INSERT INTO `device` VALUES ('11', 'sinsim-5', '868619033674340');
INSERT INTO `device` VALUES ('12', 'sinsim-6', '868619031505686');
INSERT INTO `device` VALUES ('17', 'sinsim-10', '868619031416504');
INSERT INTO `device` VALUES ('18', 'sinsim-11', '868619034253045');
INSERT INTO `device` VALUES ('19', 'sinsim-12', '868619034158848');
INSERT INTO `device` VALUES ('20', 'sinsim-13', '868619034190205');
INSERT INTO `device` VALUES ('21', 'sinsim-14', '868619033852243');
INSERT INTO `device` VALUES ('22', 'sinsim-15', '868208033128422');
-- ----------------------------
-- Records of domestic_trade_zone
-- ----------------------------
INSERT INTO `domestic_trade_zone` VALUES ('1', '绍兴(柯桥、杨汛桥)', '186');
INSERT INTO `domestic_trade_zone` VALUES ('2', '云南贵州', '186');
INSERT INTO `domestic_trade_zone` VALUES ('3', '萧山', '184');
INSERT INTO `domestic_trade_zone` VALUES ('4', '嘉兴、余杭', '184');
INSERT INTO `domestic_trade_zone` VALUES ('5', '湖北', '184');
INSERT INTO `domestic_trade_zone` VALUES ('6', '江苏', '187');
INSERT INTO `domestic_trade_zone` VALUES ('7', '江苏以北(山东、河南、辽宁)', '187');
INSERT INTO `domestic_trade_zone` VALUES ('8', '孙情区域', '187');
INSERT INTO `domestic_trade_zone` VALUES ('9', '何绍华区域', '187');
INSERT INTO `domestic_trade_zone` VALUES ('10', '宁波慈溪金华其其他地区', '186');
-- ----------------------------
-- Table structure for `install_group`
-- ----------------------------
DROP TABLE IF EXISTS `install_group`;
CREATE TABLE `install_group` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) NOT NULL COMMENT '公司部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of install_group
-- ----------------------------
INSERT INTO `install_group` VALUES ('1', '上轴组');
INSERT INTO `install_group` VALUES ('2', '下轴组');
INSERT INTO `install_group` VALUES ('3', '驱动组');
INSERT INTO `install_group` VALUES ('4', '台板组');
INSERT INTO `install_group` VALUES ('5', '电控组');
INSERT INTO `install_group` VALUES ('7', '针杆架组');
INSERT INTO `install_group` VALUES ('8', '测试调试组');
INSERT INTO `install_group` VALUES ('9', '剪线组');
INSERT INTO `install_group` VALUES ('10', '装置组');
INSERT INTO `install_group` VALUES ('11', '前置工序组');
INSERT INTO `install_group` VALUES ('14', '出厂检验组');
INSERT INTO `install_group` VALUES ('16', '毛巾安装组');
INSERT INTO `install_group` VALUES ('17', '毛巾调试');
INSERT INTO `install_group` VALUES ('18', '线架组');

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
  `is_urgent` tinyint(1) DEFAULT NULL COMMENT '是否加急；1表示加急,0表示取消加急，默认为null',
  PRIMARY KEY (`id`),
  KEY `idx_m_order_id` (`order_id`) USING BTREE,
  KEY `fk_m_machine_type` (`machine_type`),
  CONSTRAINT `fk_m_machine_type` FOREIGN KEY (`machine_type`) REFERENCES `machine_type` (`id`),
  CONSTRAINT `fk_m_order_id` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `needle_num` varchar(255) NOT NULL COMMENT '针数',
  `head_num` varchar(255) NOT NULL COMMENT '头数',
  `head_distance` varchar(255) NOT NULL COMMENT '头距(由销售预填、销售更改)',
  `x_distance` varchar(255) NOT NULL COMMENT 'X-行程',
  `y_distance` varchar(255) NOT NULL COMMENT 'Y-行程',
  `package_method` varchar(255) NOT NULL COMMENT '包装方式',
  `wrap_machine` varchar(255) DEFAULT NULL COMMENT '绕线机配置',
  `wrap_machine_change` varchar(255) DEFAULT NULL COMMENT '绕线机置换',
  `package_mark` text COMMENT '包装备注',
  `equipment` text COMMENT '机器装置，json的字符串，包含装置名称、数量、单价',
  `machine_price` varchar(255) NOT NULL COMMENT '机器价格（不包括装置）',
  `Intermediary_price` varchar(255) NOT NULL DEFAULT '0' COMMENT '居间费用',
  `discounts` varchar(255) NOT NULL DEFAULT '0' COMMENT '价格优惠',
  `order_total_discounts` varchar(255) NOT NULL DEFAULT '0' COMMENT '需求单总优惠金额，内贸需要',
  `contract_ship_date` date NOT NULL,
  `plan_ship_date` date NOT NULL,
  `mark` text COMMENT '备注信息',
  `sellman` varchar(255) NOT NULL COMMENT '订单中文字输入的销售员，一般以创建订单销售员作为sellman，这边是sinsim的特殊需求',
  `valid` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `maintain_type` varchar(255) NOT NULL COMMENT '保修方式',
  `maintain_person` varchar(255) DEFAULT '',
  `create_time` datetime NOT NULL COMMENT '订单创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '订单信息更新时间',
  `end_time` datetime DEFAULT NULL COMMENT '订单结束时间',
  `all_urgent` tinyint(4) DEFAULT NULL COMMENT '该订单的机器全部加急；1表示加急,0表示取消加急(曾经加急后来取消了)，默认为null',
  `domestic_trade_zone` int(10) unsigned DEFAULT NULL COMMENT '内贸部哪个区,外贸时为空',
  PRIMARY KEY (`id`),
  KEY `fk_o_machine_type` (`machine_type`),
  KEY `fk_o_order_detail_id` (`order_detail_id`),
  KEY `fk_o_contract_id` (`contract_id`),
  KEY `fk_o_dtz` (`domestic_trade_zone`),
  CONSTRAINT `fk_o_dtz` FOREIGN KEY (`domestic_trade_zone`) REFERENCES `domestic_trade_zone` (`id`),
  CONSTRAINT `fk_o_contract_id` FOREIGN KEY (`contract_id`) REFERENCES `contract` (`id`),
  CONSTRAINT `fk_o_machine_type` FOREIGN KEY (`machine_type`) REFERENCES `machine_type` (`id`),
  CONSTRAINT `fk_o_order_detail_id` FOREIGN KEY (`order_detail_id`) REFERENCES `order_detail` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `finished` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine_type
-- ----------------------------

-- ----------------------------
-- Table structure for `market_group`
-- ----------------------------
DROP TABLE IF EXISTS `market_group`;
CREATE TABLE `market_group` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) NOT NULL COMMENT '销售部各组名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of market_group
-- ----------------------------
INSERT INTO `market_group` VALUES ('1', '外贸一部');
INSERT INTO `market_group` VALUES ('2', '外贸二部');
INSERT INTO `market_group` VALUES ('3', '内贸部');

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
  `electric_language` varchar(255) DEFAULT NULL,
  `electric_motor` varchar(255) DEFAULT NULL COMMENT '电气：主电机',
  `electric_motor_xy` varchar(255) DEFAULT NULL COMMENT '电气：X,Y电机',
  `electric_trim` varchar(255) DEFAULT NULL COMMENT '电气：剪线方式',
  `electric_power` varchar(255) DEFAULT NULL COMMENT '电气： 电源',
  `electric_switch` varchar(255) DEFAULT NULL COMMENT '电气： 按钮开关',
  `color_change_mode` varchar(255) DEFAULT NULL COMMENT '换色方式',
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
  `axle_addition` longtext COMMENT '上下轴：附加装置（该部分由销售预填，技术进行确认或更改）',
  `framework_color` varchar(255) DEFAULT NULL COMMENT '机架台板：机架颜色 ',
  `framework_platen` varchar(255) DEFAULT NULL COMMENT '机架台板：台板',
  `framework_platen_color` varchar(255) DEFAULT NULL COMMENT '机架台板：台板颜色',
  `framework_ring` varchar(255) DEFAULT NULL COMMENT '机架台板：吊环',
  `framework_bracket` varchar(255) DEFAULT NULL COMMENT '机架台板：电脑托架',
  `framework_stop` varchar(255) DEFAULT NULL COMMENT '机架台板：急停装置',
  `framework_pole_height` varchar(255) DEFAULT NULL COMMENT '线架立柱高度',
  `framework_light` varchar(255) DEFAULT NULL COMMENT '机架台板：日光灯',
  `driver_type` varchar(255) DEFAULT NULL COMMENT '驱动：类型',
  `driver_method` varchar(255) DEFAULT NULL COMMENT '驱动：方式',
  `driver_reel_hole` varchar(255) DEFAULT NULL COMMENT '驱动：绷架孔',
  `driver_horizon_num` tinyint(4) DEFAULT NULL COMMENT '驱动：横档数量',
  `driver_vertical_num` tinyint(4) DEFAULT NULL COMMENT '驱动：直档数量',
  `driver_reel` varchar(255) DEFAULT NULL COMMENT '驱动：绷架',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `current_step` varchar(255) DEFAULT NULL COMMENT '需求单的当前签核步骤',
  `sign_content` text NOT NULL COMMENT '签核内容，以json格式的数组形式存放, 所有项完成后更新status为完成\r\n[ \r\n    {"role_id": 1, "role_name":"技术部"，“person”：“张三”，”comment“: "同意"， ”update_time“:"2017-11-05 12:08:55"},\r\n    {"role_id":2, "role_name":"PMC"，“person”：“李四”，”comment“: "同意，但是部分配件需要新设计"， ”update_time“:"2017-11-06 12:08:55"}\r\n]',
  `create_time` datetime NOT NULL COMMENT '签核流程开始时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
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
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(10) unsigned NOT NULL COMMENT '订单编号',
  `original_order_id` int(10) unsigned NOT NULL,
  `split_reason` text NOT NULL COMMENT '取消原因',
  `user_id` int(10) unsigned NOT NULL COMMENT '拆分订单操作的用户ID，只有创建订单的销售员可以拆分改订单，或者销售经理',
  `split_time` datetime NOT NULL COMMENT '拆分订单的时间',
  PRIMARY KEY (`id`),
  KEY `fk_oc_order_id` (`order_id`),
  KEY `fk_oc_user_id` (`user_id`),
  CONSTRAINT `order_split_record_ibfk_3` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`),
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of process
-- ----------------------------
INSERT INTO `process` VALUES ('4', '装置剪线机型', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"开始\", \"key\":-1, \"loc\":\"209.99999999999997 44\"},\n{\"category\":\"End\", \"text\":\"结束\", \"key\":-4, \"loc\":\"210.00000000000003 739.467688700986\"},\n{\"text\":\"下轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-8, \"loc\":\"209.63748168945312 110.19999998807907\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-5, \"loc\":\"209.63748168945312 224.19999998807907\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-7, \"loc\":\"332.6374816894531 219.19999998807907\"},\n{\"text\":\"台板安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-6, \"loc\":\"210.6374816894531 349.199999988079\"},\n{\"text\":\"上轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-16, \"loc\":\"209.234375 167\"},\n{\"text\":\"电控安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-9, \"loc\":\"305.234375 443\"},\n{\"text\":\"线架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-12, \"loc\":\"52.234375 240\"},\n{\"text\":\"出厂检验\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-2, \"loc\":\"210.234375 675\"},\n{\"text\":\"装置安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-18, \"loc\":\"210.234375 620\"},\n{\"text\":\"剪线安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-19, \"loc\":\"209.63748168945312 275.19999998807907\"},\n{\"text\":\"测试调试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-15, \"loc\":\"210.078125 528\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-11, \"loc\":\"120.234375 443\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-13, \"loc\":\"210.234375 437\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.99999999999994,88.6046511627907,209.99999999999994,98.6046511627907,209.99999999999994,99.40232557543489,209.63748168945312,99.40232557543489,209.63748168945312,100.19999998807907,209.63748168945312,110.19999998807907 ]},\n{\"from\":-8, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.63748168945312,143.07544859647751,209.63748168945312,153.07544859647751,209.63748168945312,155.03772429823874,209.234375,155.03772429823874,209.234375,157,209.234375,167 ]},\n{\"from\":-16, \"to\":-5, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.234375,199.87544860839844,209.234375,209.87544860839844,209.234375,212.03772429823874,209.63748168945312,212.03772429823874,209.63748168945312,214.19999998807907,209.63748168945312,224.19999998807907 ]},\n{\"from\":-8, \"to\":-12, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.63748168945312,143.07544859647751,209.63748168945312,153.07544859647751,209.63748168945312,156,52.234375,156,52.234375,230,52.234375,240 ]},\n{\"from\":-7, \"to\":-6, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 332.6374816894531,252.07544859647751,332.6374816894531,262.0754485964775,332.6374816894531,260,332.6374816894531,260,332.6374816894531,316,210.63748168945312,316,210.63748168945312,339.19999998807907,210.63748168945312,349.19999998807907 ]},\n{\"from\":-2, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.234375,707.8754486083984,210.234375,717.8754486083984,210.234375,723.6715686546922,210.00000000000003,723.6715686546922,210.00000000000003,729.467688700986,210.00000000000003,739.467688700986 ]},\n{\"from\":-18, \"to\":-2, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.234375,652.8754486083984,210.234375,662.8754486083984,210.234375,663.9377243041993,210.234375,663.9377243041993,210.234375,665,210.234375,675 ]},\n{\"from\":-6, \"to\":-9, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.63748168945312,382.0754485964775,210.63748168945312,392.0754485964775,210.63748168945312,412.53772429823874,305.234375,412.53772429823874,305.234375,433,305.234375,443 ]},\n{\"from\":-8, \"to\":-7, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.63748168945312,143.07544859647751,209.63748168945312,153.07544859647751,209.63748168945312,156,332.6374816894531,156,332.6374816894531,209.19999998807907,332.6374816894531,219.19999998807907 ]},\n{\"from\":-5, \"to\":-19, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.63748168945312,257.0754485964775,209.63748168945312,267.0754485964775,209.63748168945312,267.0754485964775,209.63748168945312,265.19999998807907,209.63748168945312,265.19999998807907,209.63748168945312,275.19999998807907 ]},\n{\"from\":-19, \"to\":-6, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.63748168945312,308.0754485964775,209.63748168945312,318.0754485964775,209.63748168945312,328.6377242922783,210.63748168945312,328.6377242922783,210.63748168945312,339.19999998807907,210.63748168945312,349.19999998807907 ]},\n{\"from\":-15, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.078125,560.8754486083984,210.078125,570.8754486083984,210.078125,590.4377243041993,210.234375,590.4377243041993,210.234375,610,210.234375,620 ]},\n{\"from\":-9, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 305.234375,475.8754486083984,305.234375,485.8754486083984,305.234375,544.4377243041993,281.5662384033203,544.4377243041993,257.8981018066406,544.4377243041993,247.89810180664062,544.4377243041993 ]},\n{\"from\":-12, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 52.234375,272.8754486083984,52.234375,282.8754486083984,52.234375,544.4377243041993,107.24626159667969,544.4377243041993,162.25814819335938,544.4377243041993,172.25814819335938,544.4377243041993 ]},\n{\"from\":-6, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.63748168945312,382.0754485964775,210.63748168945312,392.0754485964775,210.63748168945312,409.53772429823874,210.234375,409.53772429823874,210.234375,427,210.234375,437 ]},\n{\"from\":-13, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.234375,469.8754486083984,210.234375,479.8754486083984,210.234375,498.9377243041992,210.078125,498.9377243041992,210.078125,518,210.078125,528 ]},\n{\"from\":-6, \"to\":-11, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 210.63748168945312,382.0754485964775,210.63748168945312,392.0754485964775,210.63748168945312,412.53772429823874,120.234375,412.53772429823874,120.234375,433,120.234375,443 ]},\n{\"from\":-11, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 120.234375,475.8754486083984,120.234375,485.8754486083984,120.234375,544.4377243041993,140.90625,544.4377243041993,161.578125,544.4377243041993,171.578125,544.4377243041993 ]}\n ]}', '2018-04-01 09:25:19', '2018-06-21 17:03:39');
INSERT INTO `process` VALUES ('5', '不剪线机型', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"开始\", \"key\":-1, \"loc\":\"207 39.99999999999999\"},\n{\"category\":\"End\", \"text\":\"结束\", \"key\":-4, \"loc\":\"207.99999999999994 662.2676887129069\"},\n{\"text\":\"下轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-8, \"loc\":\"207.2465303060486 117.03934352855347\"},\n{\"text\":\"上轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-9, \"loc\":\"207.2465303060486 180.0393435285535\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-11, \"loc\":\"207.234375 250\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-14, \"loc\":\"322.234375 264\"},\n{\"text\":\"台板安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-13, \"loc\":\"207.234375 329\"},\n{\"text\":\"电控安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-6, \"loc\":\"323.234375 421\"},\n{\"text\":\"线架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-12, \"loc\":\"58.234375 265\"},\n{\"text\":\"出厂检验\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-2, \"loc\":\"208.23437499999997 587.0000000000001\"},\n{\"text\":\"测试调试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-15, \"loc\":\"208.078125 512\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-16, \"loc\":\"208.234375 420\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-17, \"loc\":\"117.234375 420\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207,84.6046511627907,207,94.6046511627907,207,100.82199734567209,207.2465303060486,100.82199734567209,207.2465303060486,107.03934352855347,207.2465303060486,117.03934352855347 ]},\n{\"from\":-8, \"to\":-9, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.2465303060486,149.91479213695192,207.2465303060486,159.91479213695192,207.2465303060486,164.9770678327527,207.2465303060486,164.9770678327527,207.2465303060486,170.0393435285535,207.2465303060486,180.0393435285535 ]},\n{\"from\":-9, \"to\":-11, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.2465303060486,212.91479213695195,207.2465303060486,222.91479213695195,207.2465303060486,231.457396068476,207.234375,231.457396068476,207.234375,240,207.234375,250 ]},\n{\"from\":-11, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,282.8754486083984,207.234375,292.8754486083984,207.234375,305.9377243041992,207.234375,305.9377243041992,207.234375,319,207.234375,329 ]},\n{\"from\":-14, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 322.234375,296.8754486083984,322.234375,306.8754486083984,322.234375,312.9377243041992,207.234375,312.9377243041992,207.234375,319,207.234375,329 ]},\n{\"from\":-2, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 208.23437499999997,619.8754486083985,208.23437499999997,629.8754486083985,208.23437499999997,641.0715686606527,207.99999999999994,641.0715686606527,207.99999999999994,652.2676887129069,207.99999999999994,662.2676887129069 ]},\n{\"from\":-8, \"to\":-14, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.2465303060486,149.91479213695192,207.2465303060486,159.91479213695192,207.2465303060486,159.91479213695192,322.234375,159.91479213695192,322.234375,254,322.234375,264 ]},\n{\"from\":-8, \"to\":-12, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.2465303060486,149.91479213695192,207.2465303060486,159.91479213695192,207.2465303060486,159.91479213695192,58.234375,159.91479213695192,58.234375,255,58.234375,265 ]},\n{\"from\":-13, \"to\":-6, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,361.8754486083984,207.234375,371.8754486083984,207.234375,391.4377243041992,323.234375,391.4377243041992,323.234375,411,323.234375,421 ]},\n{\"from\":-15, \"to\":-2, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 208.078125,544.8754486083984,208.078125,554.8754486083984,208.078125,565.9377243041993,208.23437499999997,565.9377243041993,208.23437499999997,577.0000000000001,208.23437499999997,587.0000000000001 ]},\n{\"from\":-6, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 323.234375,453.8754486083984,323.234375,463.8754486083984,323.234375,528.4377243041993,289.90625,528.4377243041993,256.578125,528.4377243041993,246.578125,528.4377243041993 ]},\n{\"from\":-12, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 58.234375,297.8754486083984,58.234375,307.8754486083984,58.234375,528.4377243041993,108.90625,528.4377243041993,159.578125,528.4377243041993,169.578125,528.4377243041993 ]},\n{\"from\":-17, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 117.234375,452.8754486083984,117.234375,462.8754486083984,117.234375,528.4377243041993,138.40625,528.4377243041993,159.578125,528.4377243041993,169.578125,528.4377243041993 ]},\n{\"from\":-13, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,361.8754486083984,207.234375,371.8754486083984,207.234375,390.9377243041992,208.234375,390.9377243041992,208.234375,410,208.234375,420 ]},\n{\"from\":-16, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 208.234375,452.8754486083984,208.234375,462.8754486083984,208.234375,482.4377243041992,208.078125,482.4377243041992,208.078125,502,208.078125,512 ]},\n{\"from\":-13, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[207.234375,361.8754486083984,207.234375,371.8754486083984,207.234375,390.9377243041992,117.234375,390.9377243041992,117.234375,410,117.234375,420]}\n ]}', '2018-04-14 02:44:47', '2018-06-21 17:03:30');
INSERT INTO `process` VALUES ('6', '剪线机型', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"开始\", \"key\":-1, \"loc\":\"211.09924853207326 1.3648093947311892\"},\n{\"category\":\"End\", \"text\":\"结束\", \"key\":-4, \"loc\":\"211.67026211658538 652.5604563401841\"},\n{\"text\":\"下轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-10, \"loc\":\"211.05664223090022 67.26529369957018\"},\n{\"text\":\"上轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-11, \"loc\":\"211.32160923033257 122.66470535321986\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-6, \"loc\":\"211.5803069047959 191.6342724376983\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-9, \"loc\":\"346.25449133038336 220.83054624414626\"},\n{\"text\":\"台板安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-8, \"loc\":\"211.31337875086223 331.26462237564795\"},\n{\"text\":\"电控安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-13, \"loc\":\"312.1239689851683 430.97969955712193\"},\n{\"text\":\"线架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-15, \"loc\":\"46.65625 221.66666650772098\"},\n{\"text\":\"出厂检验\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-16, \"loc\":\"211.65625 591.666666507721\"},\n{\"text\":\"剪线安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-19, \"loc\":\"211.63748168945312 247.19999998807907\"},\n{\"text\":\"测试调试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-18, \"loc\":\"211.078125 513\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-14, \"loc\":\"117.234375 429\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-17, \"loc\":\"211.234375 434\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-10, \"to\":-11, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.05664223090022,100.14074230796862,211.05664223090022,110.14074230796862,211.05664223090022,111.40272383059424,211.32160923033257,111.40272383059424,211.32160923033257,112.66470535321986,211.32160923033257,122.66470535321986 ]},\n{\"from\":-1, \"to\":-10, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.09924853207326,45.96946055752189,211.09924853207326,55.96946055752189,211.09924853207326,56.61737712854604,211.05664223090022,56.61737712854604,211.05664223090022,57.26529369957018,211.05664223090022,67.26529369957018 ]},\n{\"from\":-11, \"to\":-6, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.32160923033257,155.5401539616183,211.32160923033257,165.5401539616183,211.32160923033257,173.58721319965832,211.5803069047959,173.58721319965832,211.5803069047959,181.6342724376983,211.5803069047959,191.6342724376983 ]},\n{\"from\":-16, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.65625,624.5421151161194,211.65625,634.5421151161194,211.65625,638.5512857281517,211.67026211658538,638.5512857281517,211.67026211658538,642.560456340184,211.67026211658538,652.560456340184 ]},\n{\"from\":-9, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 346.25449133038336,253.7059948525447,346.25449133038336,263.7059948525447,346.25449133038336,292.4853086140963,211.31337875086223,292.4853086140963,211.31337875086223,321.26462237564795,211.31337875086223,331.26462237564795 ]},\n{\"from\":-10, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.05664223090022,100.14074230796862,211.05664223090022,110.14074230796862,211.05664223090022,110.14074230796862,46.65625,110.14074230796862,46.65625,211.66666650772098,46.65625,221.66666650772098 ]},\n{\"from\":-10, \"to\":-9, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.05664223090022,100.14074230796862,211.05664223090022,110.14074230796862,211.05664223090022,110.14074230796862,346.25449133038336,110.14074230796862,346.25449133038336,210.83054624414626,346.25449133038336,220.83054624414626 ]},\n{\"from\":-8, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.31337875086223,364.14007098404636,211.31337875086223,374.14007098404636,211.31337875086223,397.5598852705842,312.1239689851683,397.5598852705842,312.1239689851683,420.979699557122,312.1239689851683,430.979699557122 ]},\n{\"from\":-6, \"to\":-19, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.5803069047959,224.50972104609676,211.5803069047959,234.50972104609676,211.5803069047959,235.8548605170879,211.63748168945312,235.8548605170879,211.63748168945312,237.19999998807907,211.63748168945312,247.19999998807907 ]},\n{\"from\":-19, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.63748168945312,280.0754485964775,211.63748168945312,290.0754485964775,211.63748168945312,305.6700354860627,211.31337875086223,305.6700354860627,211.31337875086223,321.26462237564795,211.31337875086223,331.26462237564795 ]},\n{\"from\":-18, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.078125,545.8754486083984,211.078125,555.8754486083984,211.078125,568.7710575580597,211.65625,568.7710575580597,211.65625,581.666666507721,211.65625,591.666666507721 ]},\n{\"from\":-13, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 312.1239689851683,463.8551481655204,312.1239689851683,473.8551481655204,312.1239689851683,529.4377243041993,285.85104699258414,529.4377243041993,259.578125,529.4377243041993,249.578125,529.4377243041993 ]},\n{\"from\":-15, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 46.65625,254.54211511611942,46.65625,264.5421151161194,46.65625,529.4377243041993,104.6171875,529.4377243041993,162.578125,529.4377243041993,172.578125,529.4377243041993 ]},\n{\"from\":-8, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.31337875086223,364.14007098404636,211.31337875086223,374.14007098404636,211.31337875086223,399.0700354920232,211.234375,399.0700354920232,211.234375,424,211.234375,434 ]},\n{\"from\":-17, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.234375,466.8754486083984,211.234375,476.8754486083984,211.234375,489.9377243041992,211.078125,489.9377243041992,211.078125,503,211.078125,513 ]},\n{\"from\":-14, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 117.234375,461.8754486083984,117.234375,471.8754486083984,117.234375,529.4377243041993,139.90625,529.4377243041993,162.578125,529.4377243041993,172.578125,529.4377243041993 ]},\n{\"from\":-8, \"to\":-14, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 211.31337875086223,364.14007098404636,211.31337875086223,374.14007098404636,211.31337875086223,396.5700354920232,117.234375,396.5700354920232,117.234375,419,117.234375,429 ]}\n ]}', '2018-04-14 05:29:05', '2018-06-21 17:03:17');
INSERT INTO `process` VALUES ('8', '装置不剪线机型', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"开始\", \"key\":-1, \"loc\":\"207 39.99999999999999\"},\n{\"category\":\"End\", \"text\":\"结束\", \"key\":-4, \"loc\":\"209 690.2676887129069\"},\n{\"text\":\"下轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-14, \"loc\":\"207.234375 115\"},\n{\"text\":\"上轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-15, \"loc\":\"207.234375 175\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-11, \"loc\":\"207.234375 238\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-13, \"loc\":\"337.234375 215\"},\n{\"text\":\"台板安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-12, \"loc\":\"207.234375 305\"},\n{\"text\":\"电控安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-7, \"loc\":\"328.234375 397\"},\n{\"text\":\"出厂检验\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-3, \"loc\":\"209.234375 630\"},\n{\"text\":\"线架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-6, \"loc\":\"11.234375 232\"},\n{\"text\":\"装置安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-17, \"loc\":\"209.234375 553\"},\n{\"text\":\"测试调试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-16, \"loc\":\"208.078125 483\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-18, \"loc\":\"207.234375 397\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-19, \"loc\":\"92.234375 395\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-14, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207,84.6046511627907,207,94.6046511627907,207,99.80232558139535,207.234375,99.80232558139535,207.234375,105,207.234375,115 ]},\n{\"from\":-14, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,147.87544860839844,207.234375,157.87544860839844,207.234375,161.4377243041992,207.234375,161.4377243041992,207.234375,165,207.234375,175 ]},\n{\"from\":-15, \"to\":-11, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,207.87544860839844,207.234375,217.87544860839844,207.234375,222.9377243041992,207.234375,222.9377243041992,207.234375,228,207.234375,238 ]},\n{\"from\":-11, \"to\":-12, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,270.8754486083984,207.234375,280.8754486083984,207.234375,287.9377243041992,207.234375,287.9377243041992,207.234375,295,207.234375,305 ]},\n{\"from\":-13, \"to\":-12, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 337.234375,247.87544860839844,337.234375,257.8754486083984,337.234375,260,337.234375,260,337.234375,284,207.234375,284,207.234375,295,207.234375,305 ]},\n{\"from\":-12, \"to\":-7, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,337.8754486083984,207.234375,347.8754486083984,207.234375,367.4377243041992,328.234375,367.4377243041992,328.234375,387,328.234375,397 ]},\n{\"from\":-3, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.234375,662.8754486083984,209.234375,672.8754486083984,209.234375,676.5715686606527,209,676.5715686606527,209,680.2676887129069,209,690.2676887129069 ]},\n{\"from\":-14, \"to\":-6, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,147.87544860839844,207.234375,157.87544860839844,207.234375,157.87544860839844,11.234375,157.87544860839844,11.234375,222,11.234375,232 ]},\n{\"from\":-14, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,147.87544860839844,207.234375,157.87544860839844,207.234375,157.87544860839844,337.234375,157.87544860839844,337.234375,205,337.234375,215 ]},\n{\"from\":-17, \"to\":-3, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 209.234375,585.8754486083984,209.234375,595.8754486083984,209.234375,607.9377243041993,209.234375,607.9377243041993,209.234375,620,209.234375,630 ]},\n{\"from\":-7, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 328.234375,429.8754486083984,328.234375,439.8754486083984,328.234375,499.4377243041992,292.0662384033203,499.4377243041992,255.89810180664062,499.4377243041992,245.89810180664062,499.4377243041992 ]},\n{\"from\":-6, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 11.234375,264.8754486083984,11.234375,274.8754486083984,11.234375,499.4377243041992,85.74626159667969,499.4377243041992,160.25814819335938,499.4377243041992,170.25814819335938,499.4377243041992 ]},\n{\"from\":-16, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 208.078125,515.8754486083984,208.078125,525.8754486083984,208.078125,534.4377243041993,209.234375,534.4377243041993,209.234375,543,209.234375,553 ]},\n{\"from\":-12, \"to\":-19, \"fromPort\":\"L\", \"toPort\":\"T\", \"points\":[ 168.734375,321.4377243041992,158.734375,321.4377243041992,92.234375,321.4377243041992,92.234375,353.21886215209963,92.234375,385,92.234375,395 ]},\n{\"from\":-19, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 92.234375,427.8754486083984,92.234375,437.8754486083984,92.234375,455.4377243041992,208.078125,455.4377243041992,208.078125,473,208.078125,483 ]},\n{\"from\":-12, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,337.8754486083984,207.234375,347.8754486083984,207.234375,367.4377243041992,207.234375,367.4377243041992,207.234375,387,207.234375,397 ]},\n{\"from\":-18, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,429.8754486083984,207.234375,439.8754486083984,207.234375,456.4377243041992,208.078125,456.4377243041992,208.078125,473,208.078125,483 ]}\n ]}', '2018-04-24 02:35:51', '2018-06-21 16:54:55');
INSERT INTO `process` VALUES ('10', '纯毛巾机型', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"开始\", \"key\":-1, \"loc\":\"207 39.99999999999999\"},\n{\"category\":\"End\", \"text\":\"结束\", \"key\":-4, \"loc\":\"207.00000000000003 699.2676887129069\"},\n{\"text\":\"下轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-15, \"loc\":\"207.234375 109\"},\n{\"text\":\"上轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-16, \"loc\":\"207.234375 161\"},\n{\"text\":\"台板安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-13, \"loc\":\"207.234375 315\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-14, \"loc\":\"331.234375 156\"},\n{\"text\":\"电控安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-8, \"loc\":\"207.234375 483\"},\n{\"text\":\"毛巾安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-17, \"loc\":\"207.234375 542\"},\n{\"text\":\"出厂检验\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-18, \"loc\":\"207.234375 613\"},\n{\"text\":\"毛巾安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-20, \"loc\":\"207.234375 242\"},\n{\"text\":\"线架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-7, \"loc\":\"102.234375 161\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-12, \"loc\":\"207.234375 399\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207,84.6046511627907,207,94.6046511627907,207,96.80232558139535,207.234375,96.80232558139535,207.234375,99,207.234375,109 ]},\n{\"from\":-15, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,141.87544860839844,207.234375,151.87544860839844,207.234375,151.87544860839844,207.234375,151,207.234375,151,207.234375,161 ]},\n{\"from\":-8, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,515.8754486083984,207.234375,525.8754486083984,207.234375,528.9377243041993,207.234375,528.9377243041993,207.234375,532,207.234375,542 ]},\n{\"from\":-15, \"to\":-14, \"fromPort\":\"R\", \"toPort\":\"T\", \"points\":[ 245.734375,125.43772430419922,255.734375,125.43772430419922,331.234375,125.43772430419922,331.234375,135.7188621520996,331.234375,146,331.234375,156 ]},\n{\"from\":-17, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,574.8754486083984,207.234375,584.8754486083984,207.234375,593.9377243041993,207.234375,593.9377243041993,207.234375,603,207.234375,613 ]},\n{\"from\":-18, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,645.8754486083984,207.234375,655.8754486083984,207.234375,672.5715686606527,207.00000000000003,672.5715686606527,207.00000000000003,689.2676887129069,207.00000000000003,699.2676887129069 ]},\n{\"from\":-16, \"to\":-20, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,193.87544860839844,207.234375,203.87544860839844,207.234375,217.9377243041992,207.234375,217.9377243041992,207.234375,232,207.234375,242 ]},\n{\"from\":-20, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,274.8754486083984,207.234375,284.8754486083984,207.234375,294.9377243041992,207.234375,294.9377243041992,207.234375,305,207.234375,315 ]},\n{\"from\":-14, \"to\":-20, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 331.234375,188.87544860839844,331.234375,198.87544860839844,331.234375,258.4377243041992,293.1443634033203,258.4377243041992,255.05435180664062,258.4377243041992,245.05435180664062,258.4377243041992 ]},\n{\"from\":-15, \"to\":-7, \"fromPort\":\"L\", \"toPort\":\"T\", \"points\":[ 168.734375,125.43772430419922,158.734375,125.43772430419922,102.234375,125.43772430419922,102.234375,138.2188621520996,102.234375,151,102.234375,161 ]},\n{\"from\":-7, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 102.234375,193.87544860839844,102.234375,203.87544860839844,102.234375,558.4377243041993,130.484375,558.4377243041993,158.734375,558.4377243041993,168.734375,558.4377243041993 ]},\n{\"from\":-13, \"to\":-12, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[207.234375,347.8754486083984,207.234375,357.8754486083984,207.234375,373.4377243041992,207.234375,373.4377243041992,207.234375,389,207.234375,399]},\n{\"from\":-12, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[207.234375,431.8754486083984,207.234375,441.8754486083984,207.234375,457.4377243041992,207.234375,457.4377243041992,207.234375,473,207.234375,483]}\n ]}', '2018-04-26 11:51:33', '2018-06-21 16:36:11');
INSERT INTO `process` VALUES ('11', '毛巾剪线机型', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"开始\", \"key\":-1, \"loc\":\"205 35.999999999999986\"},\n{\"category\":\"End\", \"text\":\"结束\", \"key\":-4, \"loc\":\"205.00000000000003 781.2676887129069\"},\n{\"text\":\"下轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-15, \"loc\":\"205.234375 93\"},\n{\"text\":\"上轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-16, \"loc\":\"205.234375 141\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-12, \"loc\":\"205.234375 190\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-14, \"loc\":\"314.234375 188\"},\n{\"text\":\"剪线安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-10, \"loc\":\"205.234375 235\"},\n{\"text\":\"毛巾安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-13, \"loc\":\"205.234375 284\"},\n{\"text\":\"出厂检验\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-17, \"loc\":\"205.234375 727\"},\n{\"text\":\"毛巾安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-18, \"loc\":\"205.234375 671\"},\n{\"text\":\"台板安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-19, \"loc\":\"205.234375 337\"},\n{\"text\":\"线架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-7, \"loc\":\"55.234375 208\"},\n{\"text\":\"电控安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-8, \"loc\":\"205.234375 507\"},\n{\"text\":\"测试调试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-21, \"loc\":\"205.078125 592\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-11, \"loc\":\"311.234375 444\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-20, \"loc\":\"205.234375 441\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 205,80.6046511627907,205,90.6046511627907,205.1171875,90.6046511627907,205.1171875,83,205.234375,83,205.234375,93 ]},\n{\"from\":-15, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 205.234375,125.87544860839844,205.234375,135.87544860839844,205.234375,135.87544860839844,205.234375,131,205.234375,131,205.234375,141 ]},\n{\"from\":-16, \"to\":-12, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 205.234375,173.87544860839844,205.234375,183.87544860839844,205.234375,183.87544860839844,205.234375,180,205.234375,180,205.234375,190 ]},\n{\"from\":-15, \"to\":-7, \"fromPort\":\"L\", \"toPort\":\"T\", \"points\":[ 166.734375,109.43772430419922,156.734375,109.43772430419922,55.234375,109.43772430419922,55.234375,153.7188621520996,55.234375,198,55.234375,208 ]},\n{\"from\":-15, \"to\":-14, \"fromPort\":\"R\", \"toPort\":\"T\", \"points\":[ 243.734375,109.43772430419922,253.734375,109.43772430419922,314.234375,109.43772430419922,314.234375,143.7188621520996,314.234375,178,314.234375,188 ]},\n{\"from\":-12, \"to\":-10, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 205.234375,222.87544860839844,205.234375,232.87544860839844,205.234375,232.87544860839844,205.234375,225,205.234375,225,205.234375,235 ]},\n{\"from\":-18, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 205.234375,703.8754486083984,205.234375,713.8754486083984,205.234375,715.4377243041993,205.234375,715.4377243041993,205.234375,717,205.234375,727 ]},\n{\"from\":-17, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 205.234375,759.8754486083984,205.234375,769.8754486083984,205.234375,770.5715686606527,205.00000000000003,770.5715686606527,205.00000000000003,771.2676887129069,205.00000000000003,781.2676887129069 ]},\n{\"from\":-14, \"to\":-19, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 314.234375,220.87544860839844,314.234375,230.87544860839844,314.234375,353.4377243041992,283.984375,353.4377243041992,253.734375,353.4377243041992,243.734375,353.4377243041992 ]},\n{\"from\":-10, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 205.234375,267.8754486083984,205.234375,277.8754486083984,205.234375,277.8754486083984,205.234375,274,205.234375,274,205.234375,284 ]},\n{\"from\":-13, \"to\":-19, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 205.234375,316.8754486083984,205.234375,326.8754486083984,205.234375,326.9377243041992,205.234375,326.9377243041992,205.234375,327,205.234375,337 ]},\n{\"from\":-8, \"to\":-21, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 205.234375,539.8754486083984,205.234375,549.8754486083984,205.234375,565.9377243041993,205.078125,565.9377243041993,205.078125,582,205.078125,592 ]},\n{\"from\":-7, \"to\":-21, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 55.234375,240.87544860839844,55.234375,250.87544860839844,55.234375,608.4377243041993,106.24626159667969,608.4377243041993,157.25814819335938,608.4377243041993,167.25814819335938,608.4377243041993 ]},\n{\"from\":-21, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 205.078125,624.8754486083984,205.078125,634.8754486083984,205.078125,647.9377243041993,205.234375,647.9377243041993,205.234375,661,205.234375,671 ]},\n{\"from\":-19, \"to\":-20, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[205.234375,369.8754486083984,205.234375,379.8754486083984,205.234375,405.4377243041992,205.234375,405.4377243041992,205.234375,431,205.234375,441]},\n{\"from\":-20, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[205.234375,473.8754486083984,205.234375,483.8754486083984,205.234375,490.4377243041992,205.234375,490.4377243041992,205.234375,497,205.234375,507]},\n{\"from\":-11, \"to\":-21, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 311.234375,476.8754486083984,311.234375,486.8754486083984,311.234375,608.4377243041993,282.40625,608.4377243041993,253.578125,608.4377243041993,243.578125,608.4377243041993 ]},\n{\"from\":-19, \"to\":-11, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[205.234375,369.8754486083984,205.234375,379.8754486083984,205.234375,406.9377243041992,311.234375,406.9377243041992,311.234375,434,311.234375,444]}\n ]}', '2018-04-26 12:02:34', '2018-06-21 16:35:32');
INSERT INTO `process` VALUES ('12', '毛巾剪线装置机型', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"开始\", \"key\":-1, \"loc\":\"204.00000000000003 29.999999999999993\"},\n{\"category\":\"End\", \"text\":\"结束\", \"key\":-4, \"loc\":\"203.99999999999997 764.2676887129069\"},\n{\"text\":\"下轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-15, \"loc\":\"204.234375 92\"},\n{\"text\":\"上轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-16, \"loc\":\"204.234375 141\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-12, \"loc\":\"204.234375 194\"},\n{\"text\":\"剪线安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-10, \"loc\":\"204.234375 246\"},\n{\"text\":\"毛巾安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-7, \"loc\":\"204.234375 298\"},\n{\"text\":\"台板安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-13, \"loc\":\"204.23437499999997 350.99999999999994\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-14, \"loc\":\"325.234375 193\"},\n{\"text\":\"电控安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-8, \"loc\":\"204.234375 458\"},\n{\"text\":\"毛巾安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-19, \"loc\":\"204.234375 660\"},\n{\"text\":\"出厂检验\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-20, \"loc\":\"204.234375 712\"},\n{\"text\":\"装置安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-2, \"loc\":\"204.234375 608\"},\n{\"text\":\"线架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-21, \"loc\":\"45.234375 196\"},\n{\"text\":\"测试调试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-17, \"loc\":\"204.078125 530\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-11, \"loc\":\"328.234375 412\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-18, \"loc\":\"204.234375 406\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204,74.6046511627907,204,84.6046511627907,204.1171875,84.6046511627907,204.1171875,82,204.234375,82,204.234375,92 ]},\n{\"from\":-15, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204.234375,124.87544860839844,204.234375,134.87544860839844,204.234375,134.87544860839844,204.234375,131,204.234375,131,204.234375,141 ]},\n{\"from\":-16, \"to\":-12, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204.234375,173.87544860839844,204.234375,183.87544860839844,204.234375,183.9377243041992,204.234375,183.9377243041992,204.234375,184,204.234375,194 ]},\n{\"from\":-12, \"to\":-10, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204.234375,226.87544860839844,204.234375,236.87544860839844,204.234375,236.87544860839844,204.234375,236,204.234375,236,204.234375,246 ]},\n{\"from\":-10, \"to\":-7, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204.234375,278.8754486083984,204.234375,288.8754486083984,204.234375,288.8754486083984,204.234375,288,204.234375,288,204.234375,298 ]},\n{\"from\":-7, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204.234375,330.8754486083984,204.234375,340.8754486083984,204.234375,340.9377243041992,204.234375,340.9377243041992,204.234375,341,204.234375,351 ]},\n{\"from\":-20, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204.234375,744.8754486083984,204.234375,754.8754486083984,204.1171875,754.8754486083984,204.1171875,754.2676887129069,203.99999999999997,754.2676887129069,203.99999999999997,764.2676887129069 ]},\n{\"from\":-15, \"to\":-14, \"fromPort\":\"R\", \"toPort\":\"T\", \"points\":[ 242.734375,108.43772430419922,252.734375,108.43772430419922,325.234375,108.43772430419922,325.234375,145.7188621520996,325.234375,183,325.234375,193 ]},\n{\"from\":-2, \"to\":-19, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204.234375,640.8754486083984,204.234375,650.8754486083984,204.234375,650.8754486083984,204.234375,650,204.234375,650,204.234375,660 ]},\n{\"from\":-19, \"to\":-20, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204.234375,692.8754486083984,204.234375,702.8754486083984,204.234375,702.8754486083984,204.234375,702,204.234375,702,204.234375,712 ]},\n{\"from\":-15, \"to\":-21, \"fromPort\":\"L\", \"toPort\":\"T\", \"points\":[ 165.734375,108.43772430419922,155.734375,108.43772430419922,45.234375,108.43772430419922,45.234375,147.2188621520996,45.234375,186,45.234375,196 ]},\n{\"from\":-14, \"to\":-7, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 325.234375,225.87544860839844,325.234375,235.87544860839844,325.234375,314.4377243041992,288.984375,314.4377243041992,252.734375,314.4377243041992,242.734375,314.4377243041992 ]},\n{\"from\":-8, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204.234375,490.8754486083984,204.234375,500.8754486083984,204.234375,510.4377243041992,204.078125,510.4377243041992,204.078125,520,204.078125,530 ]},\n{\"from\":-17, \"to\":-2, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204.078125,562.8754486083984,204.078125,572.8754486083984,204.078125,585.4377243041993,204.234375,585.4377243041993,204.234375,598,204.234375,608 ]},\n{\"from\":-21, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[ 45.234375,228.87544860839844,45.234375,238.87544860839844,45.234375,546.4377243041993,100.40625,546.4377243041993,155.578125,546.4377243041993,165.578125,546.4377243041993 ]},\n{\"from\":-13, \"to\":-11, \"fromPort\":\"R\", \"toPort\":\"T\", \"points\":[ 242.73437499999997,367.43772430419915,252.73437499999997,367.43772430419915,328.234375,367.43772430419915,328.234375,384.7188621520996,328.234375,402,328.234375,412 ]},\n{\"from\":-11, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 328.234375,444.8754486083984,328.234375,454.8754486083984,328.234375,546.4377243041993,290.40625,546.4377243041993,252.578125,546.4377243041993,242.578125,546.4377243041993 ]},\n{\"from\":-13, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204.23437499999997,383.87544860839836,204.23437499999997,393.87544860839836,204.23437499999997,394.93772430419915,204.234375,394.93772430419915,204.234375,396,204.234375,406 ]},\n{\"from\":-18, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 204.234375,438.8754486083984,204.234375,448.8754486083984,204.234375,448.8754486083984,204.234375,448,204.234375,448,204.234375,458 ]}\n ]}', '2018-04-26 12:25:41', '2018-06-21 17:02:10');
INSERT INTO `process` VALUES ('13', '毛巾不剪线装置机型', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"开始\", \"key\":-1, \"loc\":\"207 39.99999999999999\"},\n{\"category\":\"End\", \"text\":\"结束\", \"key\":-4, \"loc\":\"208.99999999999997 748.2676887129069\"},\n{\"text\":\"上轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-16, \"loc\":\"207.234375 158\"},\n{\"text\":\"下轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-15, \"loc\":\"207.234375 106\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-12, \"loc\":\"207.234375 211\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-14, \"loc\":\"315.234375 152\"},\n{\"text\":\"线架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-7, \"loc\":\"98.234375 149\"},\n{\"text\":\"毛巾安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-8, \"loc\":\"207.234375 263\"},\n{\"text\":\"台板安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-13, \"loc\":\"207.234375 314\"},\n{\"text\":\"电控安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-11, \"loc\":\"207.234375 425\"},\n{\"text\":\"装置安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-2, \"loc\":\"208.234375 554\"},\n{\"text\":\"毛巾安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-18, \"loc\":\"208.234375 623\"},\n{\"text\":\"出厂检验\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-19, \"loc\":\"208.234375 685\"},\n{\"text\":\"测试调试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-17, \"loc\":\"208.078125 491\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-20, \"loc\":\"207.234375 365\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-15, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207,84.6046511627907,207,94.6046511627907,207,95.30232558139535,207.234375,95.30232558139535,207.234375,96,207.234375,106 ]},\n{\"from\":-15, \"to\":-16, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,138.87544860839844,207.234375,148.87544860839844,207.234375,148.87544860839844,207.234375,148,207.234375,148,207.234375,158 ]},\n{\"from\":-16, \"to\":-12, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,190.87544860839844,207.234375,200.87544860839844,207.234375,200.9377243041992,207.234375,200.9377243041992,207.234375,201,207.234375,211 ]},\n{\"from\":-12, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,243.87544860839844,207.234375,253.87544860839844,207.234375,253.87544860839844,207.234375,253,207.234375,253,207.234375,263 ]},\n{\"from\":-8, \"to\":-13, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,295.8754486083984,207.234375,305.8754486083984,207.234375,305.8754486083984,207.234375,304,207.234375,304,207.234375,314 ]},\n{\"from\":-2, \"to\":-18, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 208.234375,586.8754486083984,208.234375,596.8754486083984,208.234375,604.9377243041993,208.234375,604.9377243041993,208.234375,613,208.234375,623 ]},\n{\"from\":-18, \"to\":-19, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 208.234375,655.8754486083984,208.234375,665.8754486083984,208.234375,670.4377243041993,208.234375,670.4377243041993,208.234375,675,208.234375,685 ]},\n{\"from\":-19, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 208.234375,717.8754486083984,208.234375,727.8754486083984,208.234375,733.0715686606527,208.99999999999997,733.0715686606527,208.99999999999997,738.2676887129069,208.99999999999997,748.2676887129069 ]},\n{\"from\":-15, \"to\":-7, \"fromPort\":\"L\", \"toPort\":\"T\", \"points\":[ 168.734375,122.43772430419922,158.734375,122.43772430419922,98.234375,122.43772430419922,98.234375,130.7188621520996,98.234375,139,98.234375,149 ]},\n{\"from\":-15, \"to\":-14, \"fromPort\":\"R\", \"toPort\":\"T\", \"points\":[ 245.734375,122.43772430419922,255.734375,122.43772430419922,315.234375,122.43772430419922,315.234375,132.2188621520996,315.234375,142,315.234375,152 ]},\n{\"from\":-14, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"R\", \"points\":[ 315.234375,184.87544860839844,315.234375,194.87544860839844,315.234375,279.4377243041992,285.484375,279.4377243041992,255.734375,279.4377243041992,245.734375,279.4377243041992 ]},\n{\"from\":-11, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,457.8754486083984,207.234375,467.8754486083984,207.234375,474.4377243041992,208.078125,474.4377243041992,208.078125,481,208.078125,491 ]},\n{\"from\":-17, \"to\":-2, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 208.078125,523.8754486083984,208.078125,533.8754486083984,208.078125,538.9377243041993,208.234375,538.9377243041993,208.234375,544,208.234375,554 ]},\n{\"from\":-13, \"to\":-20, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,346.8754486083984,207.234375,356.8754486083984,207.234375,356.8754486083984,207.234375,355,207.234375,355,207.234375,365 ]},\n{\"from\":-20, \"to\":-11, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.234375,397.8754486083984,207.234375,407.8754486083984,207.234375,411.4377243041992,207.234375,411.4377243041992,207.234375,415,207.234375,425 ]},\n{\"from\":-7, \"to\":-17, \"fromPort\":\"B\", \"toPort\":\"L\", \"points\":[98.234375,181.87544860839844,98.234375,191.87544860839844,98.234375,507.4377243041992,128.90625,507.4377243041992,159.578125,507.4377243041992,169.578125,507.4377243041992]}\n ]}', '2018-04-26 12:38:15', '2018-06-21 16:33:44');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
INSERT INTO `role` VALUES ('1', '超级管理员', '系统后台管理', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[],\"machine\":[\"/home/machine/machine_install_process\",\"/home/machine/machine_config_process\"],\"plan\":[],\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_quality_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":[\"/home/task/process_manage\",\"/home/task/task_content_manage\"],\"system\":[\"/home/system/user_manage\",\"/home/system/install_group_manage\",\"/home/system/market_group_manage\",\"/home/system/role_manage\",\"/home/system/device_manager\",\"/home/system/machine_type_manager\"]}');
INSERT INTO `role` VALUES ('2', '生产部管理员', '主要手机上传位置、查看机器安装状态', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":[\"/home/machine/machine_install_process\",\"/home/machine/machine_config_process\"],\"plan\":[],\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_quality_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":[\"/home/task/process_manage\",\"/home/task/task_content_manage\"],\"system\":null}');
INSERT INTO `role` VALUES ('3', '安装组长', '安装前后扫描机器', '{\"contract\":null,\"order\":null,\"machine\":null,\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('4', '生产部经理', '订单审批', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":[\"/home/machine/machine_install_process\",\"/home/machine/machine_config_process\"],\"plan\":[],\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_quality_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":[\"/home/task/process_manage\",\"/home/task/task_content_manage\"],\"system\":null}');
INSERT INTO `role` VALUES ('5', '普通员工', '浏览一般网页信息', '{\"contract\":null,\"order\":null,\"machine\":[\"/home/machine/machine_install_process\"],\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('6', '总经理', '订单审核等其他可配置权限', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[],\"machine\":[\"/home/machine/machine_install_process\",\"/home/machine/machine_config_process\"],\"plan\":[],\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_quality_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":[\"/home/task/process_manage\",\"/home/task/task_content_manage\"],\"system\":null}');
INSERT INTO `role` VALUES ('7', '销售部经理', '订单审批', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":[\"/home/machine/machine_install_process\"],\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('8', '技术部经理', '订单审批', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":[\"/home/machine/machine_install_process\",\"/home/machine/machine_config_process\"],\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('9', '销售员', '录入订单', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":null,\"plan\":null,\"abnormal\":null,\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('10', '技术员', '上传装车单，联系单', '{\"contract\":null,\"order\":[],\"machine\":[\"/home/machine/machine_install_process\"],\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('11', '质检员', 'pad上操作', '{\"contract\":null,\"order\":[],\"machine\":[\"/home/machine/machine_install_process\"],\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('12', 'PMC', '生产计划', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":[\"/home/machine/machine_install_process\",\"/home/machine/machine_config_process\"],\"plan\":[],\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_quality_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":[\"/home/task/process_manage\",\"/home/task/task_content_manage\"],\"system\":null}');
INSERT INTO `role` VALUES ('13', '成本核算员', '成本核算', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[],\"machine\":null,\"plan\":null,\"abnormal\":null,\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('14', '财务经理', '合同合规性检查', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":null,\"plan\":null,\"abnormal\":null,\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('15', '财务会计', '订金确认', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":null,\"plan\":null,\"abnormal\":null,\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('16', '质检组长', '质检组长', '{\"contract\":null,\"order\":null,\"machine\":null,\"plan\":null,\"abnormal\":null,\"task\":null,\"system\":null}');
INSERT INTO `role` VALUES ('17', '安装工', '安装工', '{\"contract\":null,\"order\":null,\"machine\":null,\"plan\":null,\"abnormal\":null,\"task\":null,\"system\":null}');

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
INSERT INTO `sign_process` VALUES ('4', '改单签核流程', '[{\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\"},{\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\"},{\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\"},{\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\"},{\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\"},{\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\"},{\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\"}]', '2017-12-12 01:14:40', '2018-06-25 16:23:00');
INSERT INTO `sign_process` VALUES ('3', '正常签核流程', '[{\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\"},{\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\"},{\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\"},{\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\"},{\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\"},{\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\"},{\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\"}]', '2017-12-11 23:57:56', '2018-05-14 16:59:06');
INSERT INTO `sign_process` VALUES ('5', '拆单流程', '[{\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\"},{\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\"},{\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\"},{\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\"},{\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\"},{\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\"},{\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\"}]', '2018-01-23 09:59:38', '2018-06-25 16:24:06');

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
  `standard_minutes` int(10) unsigned DEFAULT NULL COMMENT '该工序的标准用时,单位分钟',
  PRIMARY KEY (`id`),
  KEY `fk_t_group_id` (`group_id`),
  KEY `task_name` (`task_name`),
  KEY `fk_t_quality_user_id` (`quality_user_id`),
  CONSTRAINT `fk_t_group_id` FOREIGN KEY (`group_id`) REFERENCES `install_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('1', '上轴安装', '0', '1', '');
INSERT INTO `task` VALUES ('2', '下轴安装', '0', '2', '');
INSERT INTO `task` VALUES ('3', '驱动安装', '0', '3', '');
INSERT INTO `task` VALUES ('4', '台板安装', '0', '4', '');
INSERT INTO `task` VALUES ('6', '针杆架安装', '0', '7', '');
INSERT INTO `task` VALUES ('8', '剪线安装', '0', '9', '');
INSERT INTO `task` VALUES ('10', '框架安装', '0', '3', '');
INSERT INTO `task` VALUES ('11', '电控安装', null, '5', '');
INSERT INTO `task` VALUES ('12', '线架安装', '0', '18', '');
INSERT INTO `task` VALUES ('15', '出厂检验', '0', '14', '');
INSERT INTO `task` VALUES ('17', '测试调试', '0', '8', '');
INSERT INTO `task` VALUES ('19', '装置安装', null, '10', '');
INSERT INTO `task` VALUES ('20', '毛巾安装', '0', '16', '');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `market_group_name` varchar(255) DEFAULT NULL,
  `valid` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '员工是否在职，“1”==>在职, “0”==>离职',
  `extranet_permit` tinyint(4) NOT NULL DEFAULT '0' COMMENT '外网时，检查用户权限是否允许外网登录; 0:不允许，1:允许。',
  PRIMARY KEY (`id`),
  KEY `idx_user_role_id` (`role_id`) USING BTREE,
  KEY `fk_user_group_id` (`group_id`),
  CONSTRAINT `fk_user_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=308 DEFAULT CHARSET=utf8;

INSERT INTO `user` VALUES ('1', 'admin', 'admin', '1', '1113', '0', null, '1', '1');
INSERT INTO `user` VALUES ('16', '谢侃', '谢侃', '9', 'sinsim', '0', '外贸一部', '1', '0');
INSERT INTO `user` VALUES ('17', '郑海龙', '郑海龙', '4', 'sinsim', '0', '', '1', '0');
INSERT INTO `user` VALUES ('25', '徐锡康', '徐锡康', '3', 'sinsim', '14', '', '0', '0');
INSERT INTO `user` VALUES ('29', '王国娜', '王国娜', '3', 'sinsim', '8', '', '1', '0');
INSERT INTO `user` VALUES ('30', '李霞', '李霞', '17', 'sinsim', '8', null, '1', '0');
INSERT INTO `user` VALUES ('31', '宣小华', '宣小华', '17', 'sinsim', '8', null, '1', '0');
INSERT INTO `user` VALUES ('32', '何承凤', '何承凤', '17', 'sinsim', '8', null, '1', '0');
INSERT INTO `user` VALUES ('33', '陈小英', '陈小英', '17', 'sinsim', '8', null, '1', '0');
INSERT INTO `user` VALUES ('37', '赵燕红', '赵燕红', '17', 'sinsim', '8', null, '1', '0');
INSERT INTO `user` VALUES ('38', '赵丽霞', '赵丽霞', '17', 'sinsim', '8', null, '1', '0');
INSERT INTO `user` VALUES ('40', '孙兰华', '孙兰华', '17', 'sinsim', '8', null, '1', '0');
INSERT INTO `user` VALUES ('41', '郑国平', '郑国平', '17', 'sinsim', '8', null, '1', '0');
INSERT INTO `user` VALUES ('43', '骆利淼', '骆利淼', '17', 'sinsim', '8', null, '1', '0');
INSERT INTO `user` VALUES ('44', '胡尚连', '胡尚连', '17', 'sinsim', '8', null, '1', '0');
INSERT INTO `user` VALUES ('45', '何赵军', '何赵军', '3', 'sinsim', '2', '', '1', '0');
INSERT INTO `user` VALUES ('46', '王飞', '王飞', '17', 'sinsim', '1', '', '0', '0');
INSERT INTO `user` VALUES ('47', '陈炯苗', '陈炯苗', '17', 'sinsim', '2', '', '1', '0');
INSERT INTO `user` VALUES ('49', '张文龙', '张文龙', '17', 'sinsim', '1', null, '1', '0');
INSERT INTO `user` VALUES ('53', '郑锴', '郑锴', '17', 'sinsim', '2', '', '1', '0');
INSERT INTO `user` VALUES ('61', '陆铮', '陆铮', '17', 'sinsim', '2', null, '1', '0');
INSERT INTO `user` VALUES ('62', '宣浩龙', '宣浩龙', '17', 'sinsim', '2', null, '1', '0');
INSERT INTO `user` VALUES ('63', '韩先成', '韩先成', '17', 'sinsim', '2', null, '1', '0');
INSERT INTO `user` VALUES ('64', '陈铁威', '陈铁威', '17', 'sinsim', '2', null, '1', '0');
INSERT INTO `user` VALUES ('65', '马越柯', '马越柯', '17', 'sinsim', '2', null, '1', '0');
INSERT INTO `user` VALUES ('67', '陈益锋1', '陈益锋', '17', 'sinsim', '2', '', '1', '0');
INSERT INTO `user` VALUES ('70', '王君', '王君', '17', 'sinsim', '2', null, '1', '0');
INSERT INTO `user` VALUES ('71', '郑茗友', '郑茗友', '17', 'sinsim', '2', null, '1', '0');
INSERT INTO `user` VALUES ('72', '王阿妹', '王阿妹', '17', 'sinsim', '2', null, '1', '0');
INSERT INTO `user` VALUES ('76', '张斌', '张斌', '3', 'sinsim', '7', '', '1', '0');
INSERT INTO `user` VALUES ('77', '徐银风', '徐银风', '17', 'sinsim', '7', null, '1', '0');
INSERT INTO `user` VALUES ('78', '何洪锋', '何洪锋', '17', 'sinsim', '7', null, '1', '0');
INSERT INTO `user` VALUES ('80', '阮鑫钢', '阮鑫钢', '17', 'sinsim', '7', null, '1', '0');
INSERT INTO `user` VALUES ('81', '袁伯钿', '袁伯钿', '17', 'sinsim', '7', null, '1', '0');
INSERT INTO `user` VALUES ('82', '杨瑞', '杨瑞', '17', 'sinsim', '7', null, '1', '0');
INSERT INTO `user` VALUES ('83', '卓欢其', '卓欢其', '17', 'sinsim', '7', null, '1', '0');
INSERT INTO `user` VALUES ('88', '楼建锋', '楼建锋', '17', 'sinsim', '7', null, '1', '0');
INSERT INTO `user` VALUES ('90', '章瑜', '章瑜', '17', 'sinsim', '7', '', '1', '0');
INSERT INTO `user` VALUES ('91', '蔡辉辉', '蔡辉辉', '17', 'sinsim', '5', '', '1', '0');
INSERT INTO `user` VALUES ('92', '王烜波', '王烜波', '17', 'sinsim', '12', '', '1', '0');
INSERT INTO `user` VALUES ('93', '袁涛', '袁涛', '17', 'sinsim', '7', '', '1', '0');
INSERT INTO `user` VALUES ('95', '金少军', '金少军', '3', 'sinsim', '3', '', '1', '0');
INSERT INTO `user` VALUES ('96', '张海中', '张海中', '17', 'sinsim', '3', '', '1', '0');
INSERT INTO `user` VALUES ('98', '毛杭斌', '毛杭斌', '17', 'sinsim', '3', null, '1', '0');
INSERT INTO `user` VALUES ('101', '李润', '李润', '17', 'sinsim', '3', '', '1', '0');
INSERT INTO `user` VALUES ('103', '魏叶威', '魏叶威', '17', 'sinsim', '3', null, '1', '0');
INSERT INTO `user` VALUES ('108', '周志祥', '周志祥', '3', 'sinsim', '4', '', '1', '0');
INSERT INTO `user` VALUES ('111', '龙江', '龙江', '17', 'sinsim', '4', null, '1', '0');
INSERT INTO `user` VALUES ('113', '付中亚', '付中亚', '17', 'sinsim', '4', null, '1', '0');
INSERT INTO `user` VALUES ('114', '舒孝欢', '舒孝欢', '17', 'sinsim', '4', null, '1', '0');
INSERT INTO `user` VALUES ('115', '高欢欢', '高欢欢', '17', 'sinsim', '4', null, '1', '0');
INSERT INTO `user` VALUES ('117', '方毅', '方毅', '3', 'sinsim', '5', '', '1', '0');
INSERT INTO `user` VALUES ('118', '丁文', '丁文', '17', 'sinsim', '5', null, '1', '0');
INSERT INTO `user` VALUES ('119', '许金龙', '许金龙', '17', 'sinsim', '5', null, '1', '0');
INSERT INTO `user` VALUES ('120', '陈钱栋', '陈钱栋', '17', 'sinsim', '5', null, '1', '0');
INSERT INTO `user` VALUES ('121', '蒋峰', '蒋峰', '17', 'sinsim', '5', null, '1', '0');
INSERT INTO `user` VALUES ('122', '刘伟', '刘伟', '17', 'sinsim', '5', null, '1', '0');
INSERT INTO `user` VALUES ('124', '周光焱', '周光焱', '17', 'sinsim', '5', null, '1', '0');
INSERT INTO `user` VALUES ('125', '邬润杰', '邬润杰', '17', 'sinsim', '5', null, '1', '0');
INSERT INTO `user` VALUES ('127', '姚远平', '姚远平', '17', 'sinsim', '5', null, '1', '0');
INSERT INTO `user` VALUES ('129', '毛锡伟', '毛锡伟', '17', 'sinsim', '1', '', '1', '0');
INSERT INTO `user` VALUES ('131', '王新全', '王新全', '3', 'sinsim', '16', '', '1', '0');
INSERT INTO `user` VALUES ('132', '陈益锋2', '陈益锋2', '17', 'sinsim', '9', null, '1', '0');
INSERT INTO `user` VALUES ('134', '宣焕强', '宣焕强', '17', 'sinsim', '9', null, '1', '0');
INSERT INTO `user` VALUES ('136', '钱盛', '钱盛', '17', 'sinsim', '9', '', '1', '0');
INSERT INTO `user` VALUES ('137', '章正国', '章正国', '17', 'sinsim', '9', null, '1', '0');
INSERT INTO `user` VALUES ('139', '侯棋', '侯棋', '17', 'sinsim', '9', null, '1', '0');
INSERT INTO `user` VALUES ('141', '严玮杰', '严玮杰', '17', 'sinsim', '9', null, '1', '0');
INSERT INTO `user` VALUES ('142', '王朴卡', '王朴卡', '5', 'sinsim', '0', '', '1', '0');
INSERT INTO `user` VALUES ('145', '方陈勇', '方陈勇', '17', 'sinsim', '10', null, '1', '0');
INSERT INTO `user` VALUES ('147', '陈燕丰', '陈燕丰', '17', 'sinsim', '10', '', '1', '0');
INSERT INTO `user` VALUES ('148', '卓永福', '卓永福', '17', 'sinsim', '10', null, '1', '0');
INSERT INTO `user` VALUES ('149', '吕翔', '吕翔', '17', 'sinsim', '10', null, '1', '0');
INSERT INTO `user` VALUES ('150', '王威', '王威', '17', 'sinsim', '10', null, '1', '0');
INSERT INTO `user` VALUES ('152', '楼飞翔', '楼飞翔', '3', 'sinsim', '10', '', '1', '0');
INSERT INTO `user` VALUES ('154', '蒋立奇', '蒋立奇', '16', 'sinsim', '0', '', '1', '0');
INSERT INTO `user` VALUES ('167', '郑培军', '郑培军', '2', 'sinsim', '0', '', '1', '0');
INSERT INTO `user` VALUES ('169', '吕春蓓', '吕春蓓', '2', 'sinsim', '0', '', '1', '0');
INSERT INTO `user` VALUES ('170', '杨金魁', '杨金魁', '2', 'sinsim', '0', '', '1', '0');
INSERT INTO `user` VALUES ('173', '斯雯', '斯雯', '9', 'sinsim', '0', '外贸二部', '1', '0');
INSERT INTO `user` VALUES ('174', '周婷青', '周婷青', '9', 'sinsim', '0', '外贸二部', '1', '0');
INSERT INTO `user` VALUES ('175', '曹建挺', '曹建挺', '7', 'caocao1', '0', '外贸二部', '1', '0');
INSERT INTO `user` VALUES ('177', '姚娟芝', '姚娟芝', '21', '673101', '0', '外贸一部', '0', '0');
INSERT INTO `user` VALUES ('178', '陈佳枝', '陈佳枝', '9', 'sinsim', '0', '外贸一部', '1', '0');
INSERT INTO `user` VALUES ('179', '骆晓军', '骆晓军', '7', '15967545720', '0', '外贸一部', '1', '0');
INSERT INTO `user` VALUES ('181', '胡嘉亮', '胡嘉亮', '3', 'sinsim', '9', '', '0', '0');
INSERT INTO `user` VALUES ('182', '王铁锋', '王铁锋', '21', 'sinsim', '0', '内贸部', '0', '0');
INSERT INTO `user` VALUES ('183', '郑洁', '郑洁', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('184', '张仕均', '张仕均', '7', 'zsj13616751321', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('185', '季谢魏', '季谢魏', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('186', '陈徐彬', '陈徐彬', '7', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('187', '陶炎海', '陶炎海', '7', '0', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('188', '陈洁', '陈洁', '9', '1104', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('189', '邵理国', '邵理国', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('190', '周国勇', '周国勇', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('191', '方建永', '方建永', '9', '方建永', null, '内贸部', '1', '0');
INSERT INTO `user` VALUES ('192', '蔡天明', '蔡天明', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('193', '邱隆海', '邱隆海', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('194', '孙情', '孙情', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('195', '何绍华', '何绍华', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('196', '郭洪勇', '郭洪勇', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('197', '张烝', '张烝', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('198', '徐臣', '徐臣', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('199', '陈昌虎', '陈昌虎', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('200', '刘木清', '刘木清', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('201', '方洪生', '方洪生', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('202', '魏建忠', '魏建忠', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('203', '方鑫锋', '方鑫锋', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('204', '吴捷桁', '吴捷桁', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('205', '徐保卫', '徐保卫', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('206', '王海东', '王海东', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('207', '张汉钢', '张汉钢', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('208', '王建锋', '王建锋', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('209', '周立峰', '周立峰', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('210', '冯保锋', '冯保锋', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('211', '屈仲华', '屈仲华', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('212', '高耀叶', '高耀叶', '9', 'sinsim', '0', '内贸部', '1', '0');
INSERT INTO `user` VALUES ('213', '周琪芳', '周琪芳', '9', 'sinsim', '0', '外贸一部', '1', '0');
INSERT INTO `user` VALUES ('214', '王海江', '王海江', '6', 'sinsim537', '0', '', '1', '0');
INSERT INTO `user` VALUES ('215', '方炬江', '方炬江', '8', 'fjj1983', '0', '', '1', '0');
INSERT INTO `user` VALUES ('216', '汤能萍', '汤能萍', '14', '13018807138hmy', '0', '', '0', '0');
INSERT INTO `user` VALUES ('217', '何璐洁', '何璐洁', '15', '136065', '0', '', '1', '0');
INSERT INTO `user` VALUES ('218', '袁海琼', '袁海琼', '13', '950407', '0', '', '1', '0');
INSERT INTO `user` VALUES ('219', '楼叶平', '楼叶平', '21', '100888', '0', '', '1', '0');
INSERT INTO `user` VALUES ('220', '何晓婧', '何晓婧', '13', 'sinsim', '0', '', '1', '0');
INSERT INTO `user` VALUES ('225', '金陈飞', '金陈飞', '3', 'sinsim', '18', '', '1', '0');
INSERT INTO `user` VALUES ('226', '郭建忠', '郭建忠', '17', 'sinsim', '18', '', '1', '0');
INSERT INTO `user` VALUES ('229', '郑海龙2', '郑海龙2', '12', 'sinsim', '0', '', '1', '0');
INSERT INTO `user` VALUES ('230', '殷君可', '殷君可', '10', '12345678', '0', '', '1', '0');
INSERT INTO `user` VALUES ('232', '黄刚', '黄刚', '17', 'sinsim', '2', '', '1', '0');
INSERT INTO `user` VALUES ('234', '毛陈波', '毛陈波', '5', 'sinsim', '11', '', '1', '0');
INSERT INTO `user` VALUES ('235', '余鉴泽', '余鉴泽', '17', 'sinsim', '3', '', '1', '0');
INSERT INTO `user` VALUES ('239', '王新全2', '王新全2', '3', 'sinsim', '17', '', '1', '0');
INSERT INTO `user` VALUES ('240', '王煊波', '王煊波', '17', '', '9', '', '1', '0');
INSERT INTO `user` VALUES ('242', '金陈飞1', '金陈飞1', '17', '', '18', '', '1', '0');
INSERT INTO `user` VALUES ('243', '王新全1', '王新全1', '17', '', '16', '', '1', '0');
INSERT INTO `user` VALUES ('245', '陈维佳', '陈维佳', '17', '', '3', '', '1', '0');
INSERT INTO `user` VALUES ('246', '王海永', '王海永', '18', 'sinsim', '0', '', '1', '0');
INSERT INTO `user` VALUES ('249', '袁铁炳', '袁铁炳', '17', '', '2', '', '1', '0');
INSERT INTO `user` VALUES ('251', '马越柯1', '马越柯1', '17', '', '16', '', '1', '0');
INSERT INTO `user` VALUES ('252', '陆铮1', '陆铮1', '17', '', '16', '', '1', '0');
INSERT INTO `user` VALUES ('253', '何赵军1', '何赵军1', '17', '', '16', '', '1', '0');
INSERT INTO `user` VALUES ('254', '陈炯苗1', '陈炯苗1', '17', '', '16', '', '1', '0');
INSERT INTO `user` VALUES ('255', '刘林1', '刘林1', '17', '', '16', '', '1', '0');
INSERT INTO `user` VALUES ('256', '王飞1', '王飞1', '17', '', '16', '', '1', '0');
INSERT INTO `user` VALUES ('257', '斯校军1', '斯校军1', '17', '', '16', '', '1', '0');
INSERT INTO `user` VALUES ('259', '王炜杰', '王炜杰', '17', '', '2', '', '1', '0');
INSERT INTO `user` VALUES ('261', '杨港', '杨港', '17', '', '5', '', '1', '0');
INSERT INTO `user` VALUES ('262', '张海清', '张海清', '17', '', '16', '', '1', '0');
INSERT INTO `user` VALUES ('263', '胡嘉亮1', '胡嘉亮1', '17', '', '16', '', '1', '0');
INSERT INTO `user` VALUES ('264', '毛洪伟', '毛洪伟', '17', 'sinsim', '2', '', '1', '0');
INSERT INTO `user` VALUES ('266', '宣锡阳1', '宣锡阳1', '17', '', '16', '', '1', '0');
INSERT INTO `user` VALUES ('268', '刘林2', '刘林2', '3', 'sinsim', '14', '', '1', '0');
INSERT INTO `user` VALUES ('269', '刘林3', '刘林3', '17', '', '14', '', '1', '0');
INSERT INTO `user` VALUES ('270', '戚辰宇', '戚辰宇', '19', '985213', '0', '', '1', '0');
INSERT INTO `user` VALUES ('272', '候棋', '候棋', '17', '', '10', '', '1', '0');
INSERT INTO `user` VALUES ('273', '殷珊珊', '殷珊珊', '15', 'sinsim', null, '', '1', '0');
INSERT INTO `user` VALUES ('275', '斯校军2', '斯校军2', '17', '', '2', '', '1', '0');
INSERT INTO `user` VALUES ('278', '马炜', '马炜', '20', 'sinsim', null, '', '1', '0');
INSERT INTO `user` VALUES ('279', '陈少军', '陈少军', '20', 'sinsim', null, '', '1', '0');
INSERT INTO `user` VALUES ('280', '胡学波', '胡学波', '14', '1113', null, '', '1', '0');
INSERT INTO `user` VALUES ('281', '何赵红', '何赵红', '5', 'sinsim', '0', '', '1', '0');
INSERT INTO `user` VALUES ('282', '章校均', '章校均', '3', 'sinsim', '1', '', '1', '0');
INSERT INTO `user` VALUES ('283', '章仲铭', '章仲铭', '17', '', '1', '', '1', '0');
INSERT INTO `user` VALUES ('284', '郭海强', '郭海强', '17', '', '4', '', '1', '0');
INSERT INTO `user` VALUES ('285', '黄爽爽', '黄爽爽', '9', 'sinsim', null, '外贸二部', '1', '0');
INSERT INTO `user` VALUES ('286', '陈凯', '陈凯', '17', '', '3', '', '1', '0');
INSERT INTO `user` VALUES ('287', '丁新浩', '丁新浩', '17', '', '10', '', '1', '0');
INSERT INTO `user` VALUES ('288', '周定和', '周定和', '17', '', '10', '', '1', '0');
INSERT INTO `user` VALUES ('289', '黄利江', '黄利江', '17', '', '10', '', '1', '0');
INSERT INTO `user` VALUES ('290', '章力', '章力', '17', '', '1', '', '1', '0');
INSERT INTO `user` VALUES ('291', '郭文淼', '郭文淼', '17', '', '2', '', '1', '0');
INSERT INTO `user` VALUES ('292', '陈益锋@', '陈益锋', '17', '', '2', '', '1', '0');
INSERT INTO `user` VALUES ('294', '郭忠梁', '郭忠梁', '17', '', '10', '', '1', '0');
INSERT INTO `user` VALUES ('295', '陈震', '陈震', '2', 'sinsim', null, '', '1', '0');
INSERT INTO `user` VALUES ('296', '郭浩烽', '郭浩烽', '5', 'sinsim', '0', '', '1', '0');
INSERT INTO `user` VALUES ('297', '周腾', '周腾', '17', '', '3', '', '1', '0');
INSERT INTO `user` VALUES ('298', '阮泽清', '阮泽清', '17', '', '3', '', '1', '0');
INSERT INTO `user` VALUES ('299', '陈海峰', '陈海峰', '14', 'sinsim', null, '', '1', '0');
INSERT INTO `user` VALUES ('300', '陶韩良', '陶韩良', '17', '', '7', '', '1', '0');
INSERT INTO `user` VALUES ('301', '黄爱丽', '黄爱丽', '17', '', '8', '', '1', '0');
INSERT INTO `user` VALUES ('302', '陈小娜', '陈小娜', '17', '', '8', '', '1', '0');
INSERT INTO `user` VALUES ('303', '张美琦', '张美琦', '9', 'sinsim', '0', '外贸二部', '1', '0');
INSERT INTO `user` VALUES ('304', '丁海丽', '丁海丽', '18', 'sinsim', '0', '', '1', '0');
INSERT INTO `user` VALUES ('305', '傅筱玲', '傅筱玲', '9', 'sinsim', null, '', '1', '0');
INSERT INTO `user` VALUES ('306', '詹栋', '詹栋', '15', 'sinsim', null, '', '1', '0');
INSERT INTO `user` VALUES ('307', '吕满芳', '吕满芳', '17', '', '1', '', '1', '0');

- ----------------------------
-- Table structure for whole_install_acutual
-- ----------------------------
DROP TABLE IF EXISTS `whole_install_acutual`;
CREATE TABLE `whole_install_acutual` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '整装排产的实际反馈',
  `whole_install_plan_id` int(10) unsigned NOT NULL COMMENT '1个排产计划，可以有多次反馈，比如第一天没完成，第二天继续，每天都要反馈',
  `head_count_done` int(10) DEFAULT NULL COMMENT '这次（今天）完成的头数',
  `cmt_feedback` varchar(255) DEFAULT NULL COMMENT '这次（今天）安装组长反馈的备注',
  `create_date` datetime DEFAULT NULL COMMENT '记录的生成时间',
  `update_date` datetime DEFAULT NULL COMMENT '记录的更新时间',
  `attendance_member` varchar(255) DEFAULT NULL COMMENT '今日上班人数，这些用varchar, varchar方便处理，而且可以考虑以后改为写名字',
  `overtime_member` varchar(255) DEFAULT NULL COMMENT '今日加班人数',
  `absence_member` varchar(255) DEFAULT NULL COMMENT '请假人数',
  `attendance_tomorrow` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ig_id` (`whole_install_plan_id`),
  CONSTRAINT `fk_whole_install_plan_id` FOREIGN KEY (`whole_install_plan_id`) REFERENCES `whole_install_plan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for whole_install_plan
-- ----------------------------
DROP TABLE IF EXISTS `whole_install_plan`;
CREATE TABLE `whole_install_plan` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '整装排产',
  `install_task_id` int(10) unsigned NOT NULL COMMENT '安装任务ID',
  `install_date_plan` date NOT NULL COMMENT '安排的开始安装日期',
  `order_id` int(10) unsigned NOT NULL COMMENT '该整装记录对应的订单号',
  `machine_id` int(10) unsigned NOT NULL COMMENT '该整装记录对应的机器id',
  `cmt_send` varchar(255) DEFAULT NULL COMMENT '计划的备注',
  `valid` tinyint(4) DEFAULT NULL COMMENT '0: 无效 1:有效',
  `create_date` datetime DEFAULT NULL COMMENT '记录的生成时间',
  `update_date` datetime DEFAULT NULL COMMENT '记录的更新时间',
  `send_time` datetime DEFAULT NULL COMMENT '空表示该排产还未发送，发送后填发送时间',
  PRIMARY KEY (`id`),
  KEY `fk_ig_id` (`install_task_id`) USING BTREE,
  KEY `fk_order_id` (`order_id`),
  KEY `fk_machine_id` (`machine_id`),
  CONSTRAINT `fk_it_id` FOREIGN KEY (`install_task_id`) REFERENCES `task` (`id`),
  CONSTRAINT `fk_machine_id` FOREIGN KEY (`machine_id`) REFERENCES `machine` (`id`),
  CONSTRAINT `fk_order_id` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;