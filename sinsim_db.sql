/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50553
Source Host           : localhost:3306
Source Database       : sinsim_db

Target Server Type    : MYSQL
Target Server Version : 50553
File Encoding         : 65001

Date: 2018-04-03 00:28:47
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of abnormal
-- ----------------------------
INSERT INTO `abnormal` VALUES ('9', '仓库缺料', '1', '2018-04-01 10:29:18', null);

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
  `mark` text COMMENT '合同备注信息，有填单员上填入',
  `status` tinyint(4) unsigned NOT NULL COMMENT '合同状态',
  `create_time` datetime NOT NULL COMMENT '填表时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新table的时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract
-- ----------------------------
INSERT INTO `contract` VALUES ('53', 'hth0401', '客户张三', 'xsy123', '2018-04-27', '支付宝', '合同评审之备注信息123', '2', '2018-04-01 09:18:02', '2018-04-01 10:06:22');

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
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract_sign
-- ----------------------------
INSERT INTO `contract_sign` VALUES ('53', '53', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"2018-04-01 17:43:34\",\"user\":\"xsb\",\"result\":1,\"comment\":\"销售部同意\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\",\"date\":\"2018-04-01 17:58:11\",\"user\":\"cbhsy-1\",\"result\":1,\"comment\":\"成本核算正常\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"2018-04-01 17:58:54\",\"user\":\"cwjl1\",\"result\":1,\"comment\":\"财务合格\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"2018-04-01 17:59:48\",\"user\":\"zjl\",\"result\":1,\"comment\":\"总经理批准\"}]', '签核完成', '2018-04-01 09:18:02', '2018-04-01 09:59:20');

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
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine
-- ----------------------------
INSERT INTO `machine` VALUES ('54', '60', 'A30100621161', '180401001', 'A01', '3', '1', '2018-04-01 10:10:13', '2018-04-02 01:43:45', null, null);
INSERT INTO `machine` VALUES ('55', '60', 'A30100621962', 'mph123', null, '1', '1', '2018-04-01 10:06:22', null, null, null);
INSERT INTO `machine` VALUES ('56', '61', 'A30100621401', null, null, '0', '1', '2018-04-01 10:06:22', null, null, null);
INSERT INTO `machine` VALUES ('57', '61', 'A30100621072', null, null, '0', '1', '2018-04-01 10:06:22', null, null, null);
INSERT INTO `machine` VALUES ('58', '62', 'A30100621771', null, null, '0', '4', '2018-04-01 10:06:22', null, null, null);
INSERT INTO `machine` VALUES ('59', '62', 'A30100621932', null, null, '0', '4', '2018-04-01 10:06:22', null, null, null);

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
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine_order
-- ----------------------------
INSERT INTO `machine_order` VALUES ('60', 'ddh0401', '0', '53', '103', '16', '2', '中国', 'SINSIM电脑绣花机', '2', '1', '11', '12', '14', '55', '22', '单机', '包装备注1234', '[{\"name\":\"佳宇绳绣\",\"number\":2,\"price\":\"500\"}]', '60000', '2018-04-25', '2018-04-26', '其他信息的备注信息123', 'xsy123', 'SinSim保修', '2018-04-01 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('61', 'ddh0401-2', '0', '53', '104', '16', '2', '中国', 'SINSIM电脑绣花机', '2', '1', '11', '12', '14', '55', '22', '单机', '包装备注1234', '[{\"name\":\"佳宇绳绣\",\"number\":2,\"price\":\"500\"},{\"name\":\"佳宇金片\",\"number\":1,\"price\":\"1\"}]', '60000', '2018-04-25', '2018-04-26', '其他信息的备注信息123', 'xsy123', 'SinSim保修', '2018-04-01 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('62', 'ddh0401-3', null, '53', '105', '16', '2', '中国', 'SINSIM电脑绣花机', '2', '4', '11', '12', '14', '55', '22', '单机', '包装备注1234', '[]', '60000', '2018-04-25', '2018-04-26', '其他信息的备注信息，这个订单没有装置！', 'xsy123', 'SinSim保修', '2018-04-01 00:00:00', null, null);

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
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_detail
-- ----------------------------
INSERT INTO `order_detail` VALUES ('103', '无', '无', '无', '无', '无', '无', 'M98', '韩语', '大豪', '三相步进', '普通剪线', '220V', '1个', '下自动', '15款信胜普通', '上塑料下铁', '11', 'GROZ', '普通导轨', '二位底检', '佐伩12-RY', '电磁铁跳跃', '无', '上下轴 附件装置123', '4201', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', 'LED灯', '普通', '普通', '正常', '12', '11', '正常');
INSERT INTO `order_detail` VALUES ('104', '4色', '独立', '无', '无', '无', '无', 'M98', '韩语', '大豪', '三相步进', '普通剪线', '220V', '1个', '下自动', '15款信胜普通', '上塑料下铁', '11', 'GROZ', '普通导轨', '二位底检', '佐伩12-RY', '电磁铁跳跃', '无', '上下轴 订单2的附加装置123--', '4201', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', 'LED灯', '普通', '普通', '正常', '12', '11', '正常');
INSERT INTO `order_detail` VALUES ('105', '4色', '独立', '独立', '大豪', '无', '无', 'M98', '韩语', '大豪', '三相步进', '普通剪线', '220V', '1个', '下自动', '15款信胜普通', '上塑料下铁', '11', 'GROZ', '普通导轨', '二位底检', '佐伩12-RY', '电磁铁跳跃', '无', '上下轴 订单2的附加装置123--', '4201', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', 'LED灯', '普通', '普通', '正常', '12', '11', '正常');

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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_loading_list
-- ----------------------------
INSERT INTO `order_loading_list` VALUES ('26', '60', '/opt/sinsim/output/oll/ddh0401_60_LoadingFile_0.xlsx', '2', '2018-04-01 09:31:32', null);
INSERT INTO `order_loading_list` VALUES ('27', '60', '/opt/sinsim/output/oll/ddh0401_60_LoadingFile_0.xlsx', '2', '2018-04-01 09:31:49', null);
INSERT INTO `order_loading_list` VALUES ('28', '61', '/opt/sinsim/output/oll/ddh0401-2_61_LoadingFile_0.xlsx', '2', '2018-04-01 09:32:02', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_sign
-- ----------------------------
INSERT INTO `order_sign` VALUES ('28', '60', '[{\"date\":\"2018-04-01 17:50:09\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"订单2，技术部OK\",\"user\":\"jsb\"},{\"date\":\"2018-04-01 17:57:01\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"XQD3 ok\",\"user\":\"pcm-1\"},{\"date\":\"2018-04-01 18:03:23\",\"result\":1,\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"需求单1 ok\",\"user\":\"cwkj-1\"},{\"date\":\"2018-04-01 18:06:50\",\"result\":1,\"number\":8,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"订单1 OK\",\"user\":\"pcm-1\"}]', '2018-04-01 09:18:02', '2018-04-01 10:06:22');
INSERT INTO `order_sign` VALUES ('29', '61', '[{\"date\":\"2018-04-01 17:51:15\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"订单3，技术部OK\",\"user\":\"jsb\"},{\"date\":\"2018-04-01 17:56:47\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"需求单2 OK\",\"user\":\"pcm-1\"},{\"date\":\"2018-04-01 18:02:59\",\"result\":1,\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"需求单2 ok\",\"user\":\"cwkj-1\"},{\"date\":\"2018-04-01 18:06:23\",\"result\":1,\"number\":8,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"需求单2 OK\",\"user\":\"pcm-1\"}]', '2018-04-01 09:22:26', '2018-04-01 10:05:55');
INSERT INTO `order_sign` VALUES ('30', '62', '[{\"date\":\"2018-04-01 17:49:29\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"技术部OK\",\"user\":\"jsb\"},{\"date\":\"2018-04-01 17:53:55\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"需求单3 OK\",\"user\":\"pcm-1\"},{\"date\":\"2018-04-01 18:02:22\",\"result\":1,\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"财务会计 需求单3OK\",\"user\":\"cwkj-1\"},{\"date\":\"2018-04-01 18:06:05\",\"result\":1,\"number\":8,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"pmc 需求单3ok\",\"user\":\"pcm-1\"}]', '2018-04-01 09:25:29', '2018-04-01 10:05:37');

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of process
-- ----------------------------
INSERT INTO `process` VALUES ('4', '测试流程', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"开始\", \"key\":-1, \"loc\":\"209.99999999999997 44\"},\n{\"category\":\"End\", \"text\":\"结束\", \"key\":-4, \"loc\":\"221.00000000000003 607.467688700986\"},\n{\"text\":\"上轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-9, \"loc\":\"210.63748168945312 114.19999998807907\"},\n{\"text\":\"下轴安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-8, \"loc\":\"210.63748168945312 170.19999998807907\"},\n{\"text\":\"针杆架安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-5, \"loc\":\"210.63748168945312 220.19999998807907\"},\n{\"text\":\"驱动安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-7, \"loc\":\"210.63748168945312 268.19999998807907\"},\n{\"text\":\"台板安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-6, \"loc\":\"211.63748168945312 328.19999998807907\"},\n{\"text\":\"电控\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-10, \"loc\":\"212.63748168945312 384.19999998807907\"},\n{\"text\":\"剪线\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-2, \"loc\":\"214.63748168945312 440.19999998807907\"},\n{\"text\":\"金片安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-11, \"loc\":\"120.63748168945312 493.19999998807907\"},\n{\"text\":\"金片安装\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-12, \"loc\":\"316.6374816894531 493.19999998807907\"},\n{\"text\":\"调试\", \"taskStatus\":\"0\", \"beginTime\":\"\", \"endTime\":\"\", \"leader\":\"\", \"workList\":\"\", \"key\":-3, \"loc\":\"220.63748168945312 552.1999999880791\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-9, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[210,88.6046511627907,210,98.6046511627907,210,101.40232557543489,210.63748168945312,101.40232557543489,210.63748168945312,104.19999998807907,210.63748168945312,114.19999998807907]},\n{\"from\":-9, \"to\":-8, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[210.63748168945312,147.07544859647751,210.63748168945312,157.07544859647751,210.63748168945312,158.63772429227828,210.63748168945312,158.63772429227828,210.63748168945312,160.19999998807907,210.63748168945312,170.19999998807907]},\n{\"from\":-8, \"to\":-5, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[210.63748168945312,203.07544859647751,210.63748168945312,213.07544859647751,210.63748168945312,213.07544859647751,210.63748168945312,210.19999998807907,210.63748168945312,210.19999998807907,210.63748168945312,220.19999998807907]},\n{\"from\":-5, \"to\":-7, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[210.63748168945312,253.07544859647751,210.63748168945312,263.0754485964775,210.63748168945312,263.0754485964775,210.63748168945312,258.19999998807907,210.63748168945312,258.19999998807907,210.63748168945312,268.19999998807907]},\n{\"from\":-7, \"to\":-6, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[210.63748168945312,301.0754485964775,210.63748168945312,311.0754485964775,210.63748168945312,314.6377242922783,211.63748168945312,314.6377242922783,211.63748168945312,318.19999998807907,211.63748168945312,328.19999998807907]},\n{\"from\":-6, \"to\":-10, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[211.63748168945312,361.0754485964775,211.63748168945312,371.0754485964775,211.63748168945312,372.6377242922783,212.63748168945312,372.6377242922783,212.63748168945312,374.19999998807907,212.63748168945312,384.19999998807907]},\n{\"from\":-10, \"to\":-2, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[212.63748168945312,417.0754485964775,212.63748168945312,427.0754485964775,212.63748168945312,428.6377242922783,214.63748168945312,428.6377242922783,214.63748168945312,430.19999998807907,214.63748168945312,440.19999998807907]},\n{\"from\":-2, \"to\":-11, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[214.63748168945312,473.0754485964775,214.63748168945312,483.0754485964775,214.63748168945312,483.1377242922783,120.63748168945312,483.1377242922783,120.63748168945312,483.19999998807907,120.63748168945312,493.19999998807907]},\n{\"from\":-2, \"to\":-12, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[214.63748168945312,473.0754485964775,214.63748168945312,483.0754485964775,214.63748168945312,483.1377242922783,316.6374816894531,483.1377242922783,316.6374816894531,483.19999998807907,316.6374816894531,493.19999998807907]},\n{\"from\":-11, \"to\":-3, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[120.63748168945312,526.0754485964775,120.63748168945312,536.0754485964775,120.63748168945312,539.1377242922783,220.63748168945312,539.1377242922783,220.63748168945312,542.1999999880791,220.63748168945312,552.1999999880791]},\n{\"from\":-12, \"to\":-3, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[316.6374816894531,526.0754485964775,316.6374816894531,536.0754485964775,316.6374816894531,539.1377242922783,220.63748168945312,539.1377242922783,220.63748168945312,542.1999999880791,220.63748168945312,552.1999999880791]},\n{\"from\":-3, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[220.63748168945312,585.0754485964775,220.63748168945312,595.0754485964775,220.63748168945312,596.2715686487318,221,596.2715686487318,221,597.467688700986,221,607.467688700986]}\n ]}', '2018-04-01 09:25:19', '2018-04-01 09:25:19');

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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of process_record
-- ----------------------------
INSERT INTO `process_record` VALUES ('17', '54', '4', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-9,\"points\":[210,88.6046511627907,210,98.6046511627907,210,101.40232557543489,210.63748168945312,101.40232557543489,210.63748168945312,104.19999998807907,210.63748168945312,114.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-9,\"to\":-8,\"points\":[210.63748168945312,147.07544859647751,210.63748168945312,157.07544859647751,210.63748168945312,158.63772429227828,210.63748168945312,158.63772429227828,210.63748168945312,160.19999998807907,210.63748168945312,170.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-8,\"to\":-5,\"points\":[210.63748168945312,203.07544859647751,210.63748168945312,213.07544859647751,210.63748168945312,213.07544859647751,210.63748168945312,210.19999998807907,210.63748168945312,210.19999998807907,210.63748168945312,220.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-5,\"to\":-7,\"points\":[210.63748168945312,253.07544859647751,210.63748168945312,263.0754485964775,210.63748168945312,263.0754485964775,210.63748168945312,258.19999998807907,210.63748168945312,258.19999998807907,210.63748168945312,268.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-7,\"to\":-6,\"points\":[210.63748168945312,301.0754485964775,210.63748168945312,311.0754485964775,210.63748168945312,314.6377242922783,211.63748168945312,314.6377242922783,211.63748168945312,318.19999998807907,211.63748168945312,328.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-6,\"to\":-10,\"points\":[211.63748168945312,361.0754485964775,211.63748168945312,371.0754485964775,211.63748168945312,372.6377242922783,212.63748168945312,372.6377242922783,212.63748168945312,374.19999998807907,212.63748168945312,384.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-10,\"to\":-2,\"points\":[212.63748168945312,417.0754485964775,212.63748168945312,427.0754485964775,212.63748168945312,428.6377242922783,214.63748168945312,428.6377242922783,214.63748168945312,430.19999998807907,214.63748168945312,440.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-11,\"points\":[214.63748168945312,473.0754485964775,214.63748168945312,483.0754485964775,214.63748168945312,483.1377242922783,120.63748168945312,483.1377242922783,120.63748168945312,483.19999998807907,120.63748168945312,493.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-12,\"points\":[214.63748168945312,473.0754485964775,214.63748168945312,483.0754485964775,214.63748168945312,483.1377242922783,316.6374816894531,483.1377242922783,316.6374816894531,483.19999998807907,316.6374816894531,493.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-11,\"to\":-3,\"points\":[120.63748168945312,526.0754485964775,120.63748168945312,536.0754485964775,120.63748168945312,539.1377242922783,220.63748168945312,539.1377242922783,220.63748168945312,542.1999999880791,220.63748168945312,552.1999999880791]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-12,\"to\":-3,\"points\":[316.6374816894531,526.0754485964775,316.6374816894531,536.0754485964775,316.6374816894531,539.1377242922783,220.63748168945312,539.1377242922783,220.63748168945312,542.1999999880791,220.63748168945312,552.1999999880791]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-4,\"points\":[220.63748168945312,585.0754485964775,220.63748168945312,595.0754485964775,220.63748168945312,596.2715686487318,221,596.2715686487318,221,597.467688700986,221,607.467688700986]}]', '[{\"category\":\"Start\",\"key\":\"-1\",\"loc\":\"209.99999999999997 44\",\"text\":\"开始\"},{\"category\":\"End\",\"key\":\"-4\",\"loc\":\"221.00000000000003 607.467688700986\",\"text\":\"结束\"},{\"beginTime\":\"2018-04-02 09:43:42\",\"endTime\":\"\",\"key\":\"-9\",\"leader\":\"azzz-sz\",\"loc\":\"210.63748168945312 114.19999998807907\",\"taskStatus\":\"4\",\"text\":\"上轴安装\",\"workList\":\"王飞, 陈炯苗, 斯校军\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-8\",\"leader\":\"\",\"loc\":\"210.63748168945312 170.19999998807907\",\"taskStatus\":\"1\",\"text\":\"下轴安装\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-5\",\"leader\":\"\",\"loc\":\"210.63748168945312 220.19999998807907\",\"taskStatus\":\"0\",\"text\":\"针杆架安装\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-7\",\"leader\":\"\",\"loc\":\"210.63748168945312 268.19999998807907\",\"taskStatus\":\"0\",\"text\":\"驱动安装\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-6\",\"leader\":\"\",\"loc\":\"211.63748168945312 328.19999998807907\",\"taskStatus\":\"0\",\"text\":\"台板安装\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-10\",\"leader\":\"\",\"loc\":\"212.63748168945312 384.19999998807907\",\"taskStatus\":\"0\",\"text\":\"电控\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-2\",\"leader\":\"\",\"loc\":\"214.63748168945312 440.19999998807907\",\"taskStatus\":\"0\",\"text\":\"剪线\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-11\",\"leader\":\"\",\"loc\":\"120.63748168945312 493.19999998807907\",\"taskStatus\":\"0\",\"text\":\"金片安装\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-12\",\"leader\":\"\",\"loc\":\"316.6374816894531 493.19999998807907\",\"taskStatus\":\"0\",\"text\":\"金片安装\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-3\",\"leader\":\"\",\"loc\":\"220.63748168945312 552.1999999880791\",\"taskStatus\":\"0\",\"text\":\"调试\",\"workList\":\"\"}]', '2018-04-01 10:10:13', null);
INSERT INTO `process_record` VALUES ('18', '55', '4', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-9,\"points\":[210,88.6046511627907,210,98.6046511627907,210,101.40232557543489,210.63748168945312,101.40232557543489,210.63748168945312,104.19999998807907,210.63748168945312,114.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-9,\"to\":-8,\"points\":[210.63748168945312,147.07544859647751,210.63748168945312,157.07544859647751,210.63748168945312,158.63772429227828,210.63748168945312,158.63772429227828,210.63748168945312,160.19999998807907,210.63748168945312,170.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-8,\"to\":-5,\"points\":[210.63748168945312,203.07544859647751,210.63748168945312,213.07544859647751,210.63748168945312,213.07544859647751,210.63748168945312,210.19999998807907,210.63748168945312,210.19999998807907,210.63748168945312,220.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-5,\"to\":-7,\"points\":[210.63748168945312,253.07544859647751,210.63748168945312,263.0754485964775,210.63748168945312,263.0754485964775,210.63748168945312,258.19999998807907,210.63748168945312,258.19999998807907,210.63748168945312,268.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-7,\"to\":-6,\"points\":[210.63748168945312,301.0754485964775,210.63748168945312,311.0754485964775,210.63748168945312,314.6377242922783,211.63748168945312,314.6377242922783,211.63748168945312,318.19999998807907,211.63748168945312,328.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-6,\"to\":-10,\"points\":[211.63748168945312,361.0754485964775,211.63748168945312,371.0754485964775,211.63748168945312,372.6377242922783,212.63748168945312,372.6377242922783,212.63748168945312,374.19999998807907,212.63748168945312,384.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-10,\"to\":-2,\"points\":[212.63748168945312,417.0754485964775,212.63748168945312,427.0754485964775,212.63748168945312,428.6377242922783,214.63748168945312,428.6377242922783,214.63748168945312,430.19999998807907,214.63748168945312,440.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-11,\"points\":[214.63748168945312,473.0754485964775,214.63748168945312,483.0754485964775,214.63748168945312,483.1377242922783,120.63748168945312,483.1377242922783,120.63748168945312,483.19999998807907,120.63748168945312,493.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-12,\"points\":[214.63748168945312,473.0754485964775,214.63748168945312,483.0754485964775,214.63748168945312,483.1377242922783,316.6374816894531,483.1377242922783,316.6374816894531,483.19999998807907,316.6374816894531,493.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-11,\"to\":-3,\"points\":[120.63748168945312,526.0754485964775,120.63748168945312,536.0754485964775,120.63748168945312,539.1377242922783,220.63748168945312,539.1377242922783,220.63748168945312,542.1999999880791,220.63748168945312,552.1999999880791]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-12,\"to\":-3,\"points\":[316.6374816894531,526.0754485964775,316.6374816894531,536.0754485964775,316.6374816894531,539.1377242922783,220.63748168945312,539.1377242922783,220.63748168945312,542.1999999880791,220.63748168945312,552.1999999880791]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-4,\"points\":[220.63748168945312,585.0754485964775,220.63748168945312,595.0754485964775,220.63748168945312,596.2715686487318,221,596.2715686487318,221,597.467688700986,221,607.467688700986]}]', '[{\"loc\":\"209.99999999999997 44\",\"text\":\"开始\",\"category\":\"Start\",\"key\":-1},{\"loc\":\"221.00000000000003 607.467688700986\",\"text\":\"结束\",\"category\":\"End\",\"key\":-4},{\"workList\":\"\",\"leader\":\"\",\"loc\":\"210.63748168945312 114.19999998807907\",\"text\":\"上轴安装\",\"beginTime\":\"\",\"endTime\":\"\",\"taskStatus\":\"0\",\"key\":-9},{\"workList\":\"\",\"leader\":\"\",\"loc\":\"210.63748168945312 170.19999998807907\",\"text\":\"下轴安装\",\"beginTime\":\"\",\"endTime\":\"\",\"taskStatus\":\"0\",\"key\":-8},{\"workList\":\"\",\"leader\":\"\",\"loc\":\"210.63748168945312 220.19999998807907\",\"text\":\"针杆架安装\",\"beginTime\":\"\",\"endTime\":\"\",\"taskStatus\":\"0\",\"key\":-5},{\"workList\":\"\",\"leader\":\"\",\"loc\":\"210.63748168945312 268.19999998807907\",\"text\":\"驱动安装\",\"beginTime\":\"\",\"endTime\":\"\",\"taskStatus\":\"0\",\"key\":-7},{\"workList\":\"\",\"leader\":\"\",\"loc\":\"211.63748168945312 328.19999998807907\",\"text\":\"台板安装\",\"beginTime\":\"\",\"endTime\":\"\",\"taskStatus\":\"0\",\"key\":-6},{\"workList\":\"\",\"leader\":\"\",\"loc\":\"212.63748168945312 384.19999998807907\",\"text\":\"电控\",\"beginTime\":\"\",\"endTime\":\"\",\"taskStatus\":\"0\",\"key\":-10},{\"workList\":\"\",\"leader\":\"\",\"loc\":\"214.63748168945312 440.19999998807907\",\"text\":\"剪线\",\"beginTime\":\"\",\"endTime\":\"\",\"taskStatus\":\"0\",\"key\":-2},{\"workList\":\"\",\"leader\":\"\",\"loc\":\"120.63748168945312 493.19999998807907\",\"text\":\"金片安装\",\"beginTime\":\"\",\"endTime\":\"\",\"taskStatus\":\"0\",\"key\":-11},{\"workList\":\"\",\"leader\":\"\",\"loc\":\"316.6374816894531 493.19999998807907\",\"text\":\"金片安装\",\"beginTime\":\"\",\"endTime\":\"\",\"taskStatus\":\"0\",\"key\":-12},{\"workList\":\"\",\"leader\":\"\",\"loc\":\"220.63748168945312 552.1999999880791\",\"text\":\"调试\",\"beginTime\":\"\",\"endTime\":\"\",\"taskStatus\":\"0\",\"key\":-3}]', '2018-04-01 10:15:34', null);

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
INSERT INTO `role` VALUES ('2', '生产部管理员', '主要手机上传位置、查看机器安装状态', '{\"contract\":[\"/home/contract/contract_sign\"],\"order\":[],\"machine\":[\"/home/machine/machine_config_process\",\"/home/machine/machine_install_process\"],\"plan\":null,\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":[\"/home/task/task_content_manage\",\"/home/task/process_manage\"],\"system\":null}');
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
  CONSTRAINT `fk_t_group_id` FOREIGN KEY (`group_id`) REFERENCES `install_group` (`id`),
  CONSTRAINT `fk_t_quality_user_id` FOREIGN KEY (`quality_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('1', '上轴安装', '25', '1', '');
INSERT INTO `task` VALUES ('2', '下轴安装', '25', '2', '');
INSERT INTO `task` VALUES ('3', '驱动安装', '25', '3', '');
INSERT INTO `task` VALUES ('4', '台板安装', '26', '4', '');
INSERT INTO `task` VALUES ('5', '电控', '26', '5', '');
INSERT INTO `task` VALUES ('6', '针杆架安装', '26', '7', '');
INSERT INTO `task` VALUES ('7', '调试', '27', '8', '');
INSERT INTO `task` VALUES ('8', '剪线', '27', '9', '');
INSERT INTO `task` VALUES ('9', '金片安装', '27', '10', '');

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
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_plan
-- ----------------------------
INSERT INTO `task_plan` VALUES ('26', '45', '1', '2018-04-01 10:08:02', null, '1', '2018-04-01 10:13:44', null);
INSERT INTO `task_plan` VALUES ('27', '46', '1', '2018-04-01 10:08:02', null, '1', '2018-04-01 10:13:44', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_record
-- ----------------------------
INSERT INTO `task_record` VALUES ('45', '上轴安装', '17', '-9', 'azzz-sz', '王飞, 陈炯苗, 斯校军', '4', '2018-04-02 09:43:42', '2018-04-02 09:45:48', null, null);
INSERT INTO `task_record` VALUES ('46', '下轴安装', '17', '-8', null, null, '1', null, null, null, null);
INSERT INTO `task_record` VALUES ('47', '针杆架安装', '17', '-5', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('48', '驱动安装', '17', '-7', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('49', '台板安装', '17', '-6', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('50', '电控', '17', '-10', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('51', '剪线', '17', '-2', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('52', '金片安装', '17', '-11', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('53', '金片安装', '17', '-12', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('54', '调试', '17', '-3', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('55', '上轴安装', '18', '-9', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('56', '下轴安装', '18', '-8', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('57', '针杆架安装', '18', '-5', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('58', '驱动安装', '18', '-7', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('59', '台板安装', '18', '-6', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('60', '电控', '18', '-10', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('61', '剪线', '18', '-2', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('62', '金片安装', '18', '-11', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('63', '金片安装', '18', '-12', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('64', '调试', '18', '-3', null, null, '0', null, null, null, null);

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
) ENGINE=InnoDB AUTO_INCREMENT=167 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '彭胜利', '1', 'sinsim', '0', '1');
INSERT INTO `user` VALUES ('7', 'azzz-sz', 'azzz-sz', '3', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('12', 'ptyg', 'ptyg', '5', 'sinsim', '0', '1');
INSERT INTO `user` VALUES ('13', 'xsbjl', 'xsb', '7', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('14', 'jsbjl', 'jsb', '8', 'sinsim', null, '1');
INSERT INTO `user` VALUES ('15', 'zjl', 'zjl', '6', 'sinsim', '0', '1');
INSERT INTO `user` VALUES ('16', 'xsy', 'xsy', '9', 'sinsim', '0', '1');
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
INSERT INTO `user` VALUES ('40', '孙兰华', '俞红萍', '17', 'sinsim', '8', '1');
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
INSERT INTO `user` VALUES ('131', '王新全', '王新全', '3', 'sinsim', '9', '1');
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
INSERT INTO `user` VALUES ('159', 'azzz-xz', 'azzz-xz', '3', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('160', 'azzz-qd', 'azzz-qd', '3', 'sinsim', '3', '1');
INSERT INTO `user` VALUES ('161', 'azzz-tb', 'azzz-tb', '3', 'sinsim', '4', '1');
INSERT INTO `user` VALUES ('162', 'azzz-dk', 'azzz-dk', '3', 'sinsim', '5', '1');
INSERT INTO `user` VALUES ('163', 'azzz-zgj', 'azzz-zgj', '3', 'sinsim', '7', '1');
INSERT INTO `user` VALUES ('164', 'azzz-ts', 'azzz-ts', '3', 'sinsim', '8', '1');
INSERT INTO `user` VALUES ('165', 'azzz-jx', 'azzz-jx', '3', 'sinsim', '9', '1');
INSERT INTO `user` VALUES ('166', 'azzz-jp', 'azzz-jp', '3', 'sinsim', '10', '1');
