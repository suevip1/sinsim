/*
Navicat MySQL Data Transfer

Source Server         : MyDB
Source Server Version : 50547
Source Host           : localhost:3306
Source Database       : sinsim_db

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2018-01-23 15:01:18
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
  `abnormal_type` tinyint(4) unsigned NOT NULL COMMENT '异常类型',
  `task_record_id` int(10) unsigned NOT NULL COMMENT '作业工序',
  `submit_user` int(10) unsigned NOT NULL COMMENT '提交异常的用户ID',
  `comment` text NOT NULL COMMENT '异常备注',
  `solution` text NOT NULL COMMENT '解决办法',
  `solution_user` int(10) unsigned NOT NULL COMMENT '解决问题的用户对应的ID',
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
  `mark` text COMMENT '合同备注信息，有填单员上填入',
  `status` tinyint(4) unsigned NOT NULL COMMENT '合同状态',
  `create_time` datetime NOT NULL COMMENT '填表时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新table的时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract
-- ----------------------------
INSERT INTO `contract` VALUES ('30', 'xs-201712010', '测试', '张三', '2017-12-30', '', '', '3', '2017-12-18 15:32:25', null);
INSERT INTO `contract` VALUES ('31', 'xs-201712009', '胡通', '张三', '2018-02-28', '支付宝', '', '1', '2017-12-18 15:57:06', null);
INSERT INTO `contract` VALUES ('40', 'xs-201712001', '李四', '王五', '2017-12-30', '', '', '1', '2017-12-18 16:55:14', null);
INSERT INTO `contract` VALUES ('41', 'ss-20180116', '张三', '李四', '2018-01-18', '支付宝', '无', '1', '2018-01-16 01:15:00', '2018-01-23 14:47:21');
INSERT INTO `contract` VALUES ('42', 'ss-20180120', '张三', '李四', '2018-01-31', '支付宝', '', '0', '2018-01-20 09:36:37', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract_sign
-- ----------------------------
INSERT INTO `contract_sign` VALUES ('18', '30', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"2017-12-26 09:15:14\",\"user\":\"张三\",\"comment\":\"approve\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"李四\",\"comment\":\"approve\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"王五\",\"comment\":\"approve\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"2017-12-26 11:29:14\",\"user\":\"王总\",\"comment\":\"approve\"}]', '签核完成', '2017-12-18 15:32:25', '2017-12-27 01:11:12');
INSERT INTO `contract_sign` VALUES ('19', '31', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"}]', '销售部经理', '2017-12-18 15:57:06', '2018-01-15 12:19:18');
INSERT INTO `contract_sign` VALUES ('28', '40', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"2017-12-22 14:24:28\",\"user\":\"科比\",\"comment\":\"同意\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\",\"date\":\"2017-12-26 09:12:32\",\"user\":\"艾弗森\",\"comment\":\"同意\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"2017-12-27 15:45:37\",\"user\":\"GG\",\"comment\":\"同意\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"2017-12-27 15:45:51\",\"user\":\"HH\",\"comment\":\"同意\"}]', '签核完成', '2017-12-18 16:55:14', '2017-12-27 15:46:26');
INSERT INTO `contract_sign` VALUES ('35', '40', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"2018-01-12 10:11:39\",\"user\":\"Hu Tong\",\"comment\":\"同意\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"}]', '生产部经理', '2017-12-27 15:46:26', '2018-01-12 10:11:39');
INSERT INTO `contract_sign` VALUES ('36', '41', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '销售部经理', '2018-01-16 01:15:00', '2018-01-16 01:35:11');
INSERT INTO `contract_sign` VALUES ('37', '41', '[{\"comment\":\"同意\",\"date\":\"2018-01-22 15:53:16\",\"number\":1,\"result\":1,\"roleId\":7,\"signType\":\"合同签核\",\"user\":\"TT\"},{\"comment\":\"同意\",\"date\":\"2018-01-23 09:40:45\",\"number\":4,\"result\":1,\"roleId\":13,\"signType\":\"合同签核\",\"user\":\"张三\"},{\"comment\":\"同意\",\"date\":\"2018-01-23 09:40:51\",\"number\":5,\"result\":1,\"roleId\":14,\"signType\":\"合同签核\",\"user\":\"李四\"},{\"comment\":\"同意\",\"number\":6,\"result\":1,\"roleId\":6,\"signType\":\"合同签核\",\"user\":\"WHJ\",\"date\":\"2018-01-23 09:41:00\"}]', '签核完成', '2018-01-16 01:36:31', '2018-01-23 09:41:00');
INSERT INTO `contract_sign` VALUES ('38', '42', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '', '2018-01-20 09:36:37', null);
INSERT INTO `contract_sign` VALUES ('39', '41', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '', '2018-01-23 13:34:34', null);
INSERT INTO `contract_sign` VALUES ('42', '41', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '销售部经理', '2018-01-23 14:44:53', '2018-01-23 14:47:21');

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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine
-- ----------------------------
INSERT INTO `machine` VALUES ('16', '22', 'ABP112914371', null, null, '0', '1', '2017-12-26 11:29:14', '2017-12-27 01:11:28', null, null);
INSERT INTO `machine` VALUES ('17', '22', 'ABP112914752', null, null, '0', '1', '2017-12-26 11:29:14', '2017-12-27 01:11:28', null, null);
INSERT INTO `machine` VALUES ('18', '22', 'ABP112914563', null, null, '0', '1', '2017-12-26 11:29:14', '2017-12-27 01:11:28', null, null);
INSERT INTO `machine` VALUES ('19', '28', 'ABQ154551411', null, null, '0', '1', '2017-12-27 15:45:51', null, null, null);
INSERT INTO `machine` VALUES ('20', '28', 'ABQ154551452', null, null, '0', '1', '2017-12-27 15:45:51', null, null, null);
INSERT INTO `machine` VALUES ('21', '28', 'ABQ154551433', null, null, '0', '1', '2017-12-27 15:45:51', null, null, null);
INSERT INTO `machine` VALUES ('22', '49', 'ABQ154551051', null, null, '0', '12', '2017-12-27 15:45:51', '2017-12-27 15:46:26', null, null);
INSERT INTO `machine` VALUES ('23', '49', 'ABQ154551862', null, null, '0', '12', '2017-12-27 15:45:51', '2017-12-27 15:46:26', null, null);
INSERT INTO `machine` VALUES ('24', '49', 'ABQ154551573', null, null, '0', '12', '2017-12-27 15:45:51', '2017-12-27 15:46:26', null, null);
INSERT INTO `machine` VALUES ('25', '49', 'ABQ154551224', null, null, '0', '12', '2017-12-27 15:45:51', '2017-12-27 15:46:26', null, null);
INSERT INTO `machine` VALUES ('26', '50', 'A0M094156411', null, null, '0', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('27', '50', 'A0M094156732', null, null, '0', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('28', '51', 'A0M094156191', null, null, '0', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('29', '51', 'A0M094156892', null, null, '0', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('30', '51', 'A0M094156773', null, null, '0', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('31', '56', 'A0M094156521', '', '', '4', '1', '2018-01-23 09:41:56', '2018-01-23 14:44:54', null, null);
INSERT INTO `machine` VALUES ('32', '52', 'A0M094156492', null, null, '0', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('33', '52', 'A0M094156333', null, null, '0', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('34', '52', 'A0M094156074', null, null, '0', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('35', '52', 'A0M094156575', null, null, '0', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('36', '52', 'A0M094156526', null, null, '0', '1', '2018-01-23 09:41:56', null, null, null);

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
  `machine_price` varchar(255) NOT NULL COMMENT '机器价格（不包括装置）',
  `contract_ship_date` date NOT NULL,
  `plan_ship_date` date NOT NULL,
  `mark` text COMMENT '备注信息',
  `sellman` varchar(255) DEFAULT NULL COMMENT '订单中文字输入的销售员，一般以创建订单销售员作为sellman，这边是sinsim的特殊需求',
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
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine_order
-- ----------------------------
INSERT INTO `machine_order` VALUES ('22', '0712007', '0', '30', '50', '1', '3', '英语', 'SINSIM电脑绣花机', '3', '1', '11', '1', '10', '100', '200', '单机', '50000', '2017-12-31', '2017-12-31', '无', '王五', '2017-12-18 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('23', '0712007', '0', '31', '51', '1', '0', '英语', 'SINSIM电脑绣花机', '3', '1', '11', '1', '10', '100', '200', '单机', '50000', '2017-12-31', '2017-12-31', '无', '王五', '2017-12-18 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('28', '0712001', '0', '40', '62', '1', '0', '英语', 'SINSIM电脑绣花机', '3', '1', '20', '10', '5', '100', '200', '叠机', '30000', '2017-12-31', '2017-12-25', '', '李四', '2017-12-18 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('37', '0712002', '0', '40', '71', '1', '3', '英语', 'SINSIM电脑绣花机', '4', '12', '20', '10', '5', '100', '200', '叠机', '30000', '2017-12-31', '2017-12-25', '', '李四', '2017-12-20 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('39', '0712008', '0', '31', '73', '1', '0', '英语', 'SINSIM电脑绣花机', '3', '4', '11', '1', '10', '100', '200', '单机', '50000', '2017-12-31', '2017-12-31', '无', '王五', '2017-12-20 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('47', '0712007-1', '22', '30', '81', '1', '0', '英语', 'SINSIM电脑绣花机', '4', '1', '11', '1', '10', '100', '200', '单机', '50000', '2017-12-31', '2017-12-31', '无', '王五', '2017-12-27 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('48', '0712007-1', '22', '30', '82', '1', '0', '英语', 'SINSIM电脑绣花机', '4', '1', '11', '1', '10', '100', '200', '单机', '50000', '2017-12-31', '2017-12-31', '无', '王五', '2017-12-27 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('49', '0712002-1', '37', '40', '83', '1', '0', '英语', 'SINSIM电脑绣花机', '5', '12', '20', '10', '5', '100', '200', '叠机', '30000', '2017-12-31', '2017-12-25', '', '李四', '2017-12-27 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('50', '20180101', '0', '41', '84', '1', '2', '英语', 'SINSIM电脑绣花机', '2', '1', '10', '20', '1', '1', '1', '单机', '50000', '2018-01-27', '2018-02-01', '', '李四', '2018-01-16 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('51', '20180102', '0', '41', '85', '1', '2', '英语', 'SINSIM电脑绣花机', '3', '1', '10', '20', '1', '1', '1', '单机', '65000', '2018-01-27', '2018-02-01', '', '李四', '2018-01-16 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('52', '20180102-1', '51', '41', '86', '1', '4', '英语', 'SINSIM电脑绣花机', '5', '1', '10', '20', '1', '1', '1', '单机', '65000', '2018-01-27', '2018-02-01', '', '李四', '2018-01-16 00:00:00', '2018-01-23 14:45:23', null);
INSERT INTO `machine_order` VALUES ('53', '180120-1', null, '42', '87', '1', '0', '英语', 'SINSIM电脑绣花机', '2', '2', '1', '1', '1', '22', '33', '叠机', '45678', '2018-01-31', '2018-01-31', '暂无', '李四', '2018-01-20 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('56', '20180102-1', '52', '41', '90', '1', '1', '英语', 'SINSIM电脑绣花机', '1', '1', '10', '20', '1', '1', '1', '单机', '60000', '2018-01-27', '2018-02-01', '', '李四', '2018-01-23 00:00:00', null, null);

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
INSERT INTO `order_change_record` VALUES ('1', '22', '增加机器数至5台', '1', '2017-12-27 01:11:13');
INSERT INTO `order_change_record` VALUES ('2', '22', '增加机器数至5台', '1', '2017-12-27 01:11:29');
INSERT INTO `order_change_record` VALUES ('3', '37', '5', '1', '2017-12-27 15:46:26');
INSERT INTO `order_change_record` VALUES ('4', '51', '增加机器台数至6台', '1', '2018-01-16 01:36:31');

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
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8;

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
INSERT INTO `order_detail` VALUES ('50', '6色', '集中', '集中', '大豪', '冠军独立', '16', '528', '儒竞', '五相步进', '不剪线', '380V', '3个', '下点动', '伟龙款', '上塑料下铁', '11', '普通导轨', '三型断检', '佐伩12-R', '电机跳跃', '有', null, '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '1个托架下', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('51', '6色', '集中', '集中', '大豪', '冠军独立', '16', '528', '儒竞', '五相步进', '不剪线', '380V', '3个', '下点动', '伟龙款', '上塑料下铁', '11', '普通导轨', '三型断检', '佐伩12-R', '电机跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '1个托架下', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('62', '4色', '独立', '集中', '大豪', '普通全独立', '18', '316', '大豪', '五相步进', '普通剪线', '380V', '3个', '下自动', '15款信胜高速', '上塑料下复合', '14', '珠架导轨', '三型断检', '佐伩12-RP', '电磁铁跳跃带轴承', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '1个托架下,一个另一侧梁上长杆', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('71', '4色', '独立', '集中', '大豪', '普通全独立', '18', '316', '大豪', '五相步进', '普通剪线', '380V', '3个', '下自动', '15款信胜高速', '上塑料下复合', '14', '珠架导轨', '三型断检', '佐伩12-RP', '电磁铁跳跃带轴承', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '1个托架下,一个另一侧梁上长杆', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('73', '6色', '集中', '集中', '松下', '冠军独立', '16', '528', '儒竞', '五相步进', '不剪线', '380V', '3个', '下点动', '伟龙款', '上塑料下铁', '11', '普通导轨', '三型断检', '佐伩12-R', '电机跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '1个托架下', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('81', '6色', '集中', '集中', '大豪', '冠军独立', '16', '528', '儒竞', '五相步进', '不剪线', '380V', '3个', '下点动', '伟龙款', '上塑料下铁', '11', '普通导轨', '三型断检', '佐伩12-R', '电机跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '1个托架下', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('82', '6色', '集中', '集中', '大豪', '冠军独立', '16', '528', '儒竞', '五相步进', '不剪线', '380V', '3个', '下点动', '伟龙款', '上塑料下铁', '11', '普通导轨', '三型断检', '佐伩12-R', '电机跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '1个托架下', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('83', '4色', '独立', '集中', '大豪', '普通全独立', '18', '316', '大豪', '五相步进', '普通剪线', '380V', '3个', '下自动', '15款信胜高速', '上塑料下复合', '14', '珠架导轨', '三型断检', '佐伩12-RP', '电磁铁跳跃带轴承', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '1个托架下,一个另一侧梁上长杆', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('84', '4色', '独立', '无', '大豪', '冠军独立', '16', 'M98', '大豪', '三相步进', '电机剪线', '380V', '1个', '下点动', '伟龙款', '上塑料下铁', '11', '珠架导轨', '三型断检', '佐伩12-R', '电机跳跃', '无', '暂无', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '台板上', '一个托架下,一个左侧台板下', 'LED灯', '普通', '普通', '正常', '4', '4', '正常');
INSERT INTO `order_detail` VALUES ('85', '4色', '独立', '无', '松下', '冠军独立', '16', 'M98', '大豪', '三相步进', '电机剪线', '380V', '1个', '下点动', '伟龙款', '上塑料下铁', '11', '珠架导轨', '三型断检', '佐伩12-R', '电机跳跃', '无', '暂无', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '台板上', '一个托架下,一个左侧台板下', 'LED灯', '普通', '普通', '正常', '4', '4', '正常');
INSERT INTO `order_detail` VALUES ('86', '4色', '独立', '无', '松下', '冠军独立', '16', 'M98', '大豪', '三相步进', '电机剪线', '380V', '1个', '下点动', '伟龙款', '上塑料下铁', '11', '珠架导轨', '三型断检', '佐伩12-R', '电机跳跃', '无', '暂无', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '台板上', '一个托架下,一个左侧台板下', 'LED灯', '普通', '普通', '正常', '4', '4', '正常');
INSERT INTO `order_detail` VALUES ('87', '4色', '独立', '独立', '大豪', '冠军独立', '16', 'C29', '大豪', '三相步进', '电机剪线', '380V', '3个', '下自动', '伟龙款', '上塑料下复合', '14', '珠架导轨', '二位底检', '广濑1.6倍高速', '电磁铁跳跃', '有', '无', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '有', '台板上', '1个托架下', 'LED灯', '普通', '宽幅(中传动)', '正常', '2', '2', '正常');
INSERT INTO `order_detail` VALUES ('90', '4色', '独立', '无', '大豪', '冠军独立', '16', 'M98', '大豪', '三相步进', '电机剪线', '380V', '1个', '下点动', '伟龙款', '上塑料下铁', '11', '珠架导轨', '三型断检', '佐伩12-R', '电机跳跃', '无', '暂无', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '台板上', '一个托架下,一个左侧台板下', 'LED灯', '普通', '普通', '正常', '4', '4', '正常');

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
  `sign_content` text NOT NULL COMMENT '签核内容，以json格式的数组形式存放, 所有项完成后更新status为完成\r\n[ \r\n    {"role_id": 1, "role_name":"技术部"，“person”：“张三”，”comment“: "同意"， ”update_time“:"2017-11-05 12:08:55"},\r\n    {"role_id":2, "role_name":"PMC"，“person”：“李四”，”comment“: "同意，但是部分配件需要新设计"， ”update_time“:"2017-11-06 12:08:55"}\r\n]',
  `create_time` datetime NOT NULL COMMENT '签核流程开始时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `fk_os_order_id` (`order_id`),
  CONSTRAINT `fk_os_order_id` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_sign
-- ----------------------------
INSERT INTO `order_sign` VALUES ('1', '28', '[{\"date\":\"2017-12-22 14:57:25\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"奥尼尔\"},{\"date\":\"2017-12-26 09:13:29\",\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"姚明\"},{\"date\":\"2017-12-27 15:45:28\",\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"HT\"}]', '2017-12-18 16:55:14', '2017-12-27 15:45:29');
INSERT INTO `order_sign` VALUES ('5', '37', '[{\"date\":\"2017-12-22 14:58:22\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"奥尼尔\"},{\"date\":\"2017-12-26 09:11:31\",\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"姚明\"},{\"date\":\"2017-12-27 15:45:13\",\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"HT\"}]', '2017-12-20 23:21:43', '2017-12-27 15:46:26');
INSERT INTO `order_sign` VALUES ('7', '39', '[{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"},{\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"}]', '2017-12-21 00:00:08', null);
INSERT INTO `order_sign` VALUES ('15', '47', '[{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"},{\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"}]', '2017-12-27 01:11:11', null);
INSERT INTO `order_sign` VALUES ('16', '48', '[{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"},{\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"comment\":\"\"}]', '2017-12-27 01:11:29', null);
INSERT INTO `order_sign` VALUES ('17', '49', '[{\"date\":\"2018-01-12 10:18:22\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"吴学敏\"},{\"date\":\"\",\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"}]', '2017-12-27 15:46:26', '2018-01-12 10:18:23');
INSERT INTO `order_sign` VALUES ('18', '50', '[{\"date\":\"2018-01-23 09:39:30\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"胡通\"},{\"date\":\"2018-01-23 09:40:28\",\"result\":1,\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"approve\",\"user\":\"吴学敏\"},{\"result\":1,\"date\":\"2018-01-23 09:41:56\",\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"approve\",\"user\":\"SS\"}]', '2018-01-16 01:15:00', '2018-01-23 09:41:56');
INSERT INTO `order_sign` VALUES ('19', '51', '[{\"date\":\"2018-01-22 08:53:04\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"胡通\"},{\"date\":\"2018-01-23 09:40:17\",\"result\":1,\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"YRS\"},{\"date\":\"2018-01-23 09:41:36\",\"result\":1,\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"SS\"}]', '2018-01-16 01:16:14', '2018-01-23 09:41:36');
INSERT INTO `order_sign` VALUES ('20', '52', '[{\"date\":\"2018-01-23 09:39:38\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"胡通\"},{\"date\":\"2018-01-23 09:40:36\",\"result\":1,\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"approve\",\"user\":\"吴学敏\"},{\"result\":1,\"date\":\"2018-01-23 09:41:20\",\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"SS\"}]', '2018-01-16 01:36:31', '2018-01-23 09:41:20');
INSERT INTO `order_sign` VALUES ('21', '53', '[{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '2018-01-20 09:36:37', null);
INSERT INTO `order_sign` VALUES ('24', '56', '[{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '2018-01-23 14:44:54', null);

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
INSERT INTO `order_split_record` VALUES ('1', '52', '主电机改成DH', '1', '2018-01-23 14:44:58');

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
  `image` varchar(255) NOT NULL COMMENT '异常图片名称（包含路径）,以后这部分数据是最大的，首先pad上传时候时候需要压缩，以后硬盘扩展的话，可以把几几年的图片放置到另外一个硬盘，然后pad端响应升级（根据时间加上图片的路径）',
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '超级管理员', '系统后台管理', '');
INSERT INTO `role` VALUES ('2', '生产部管理员', '主要Pad上操作，上传位置、pad上查看流程等', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[\"/home/order/order_sign\",\"/home/order/order_manage\"],\"plan\":[],\"abnormal\":[],\"task\":[\"/home/task/process_manage\"],\"system\":[]}');
INSERT INTO `role` VALUES ('3', '安装组长', '安装前后扫描机器', null);
INSERT INTO `role` VALUES ('4', '生产部经理', '订单审批', null);
INSERT INTO `role` VALUES ('5', '普通员工', '浏览一般网页信息', null);
INSERT INTO `role` VALUES ('6', '总经理', '订单审核等其他可配置权限', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[\"/home/order/order_sign\",\"/home/order/order_manage\"],\"plan\":[],\"abnormal\":[],\"task\":[\"/home/task/task_content_manage\",\"/home/task/process_manage\"],\"system\":[]}');
INSERT INTO `role` VALUES ('7', '销售部经理', '订单审批', null);
INSERT INTO `role` VALUES ('8', '技术部经理', '订单审批', null);
INSERT INTO `role` VALUES ('9', '销售员', '录入订单', null);
INSERT INTO `role` VALUES ('10', '技术员', '上传装车单，联系单', null);
INSERT INTO `role` VALUES ('11', '质检员', 'pad上操作', null);
INSERT INTO `role` VALUES ('12', 'PMC', '生产计划', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[\"/home/order/order_sign\",\"/home/order/order_manage\"],\"plan\":[],\"abnormal\":[],\"task\":[],\"system\":[]}');
INSERT INTO `role` VALUES ('13', '成本核算员', '成本核算', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[\"/home/order/order_sign\",\"/home/order/order_manage\"],\"plan\":[],\"abnormal\":[],\"task\":[],\"system\":[]}');
INSERT INTO `role` VALUES ('14', '财务经理', '合同合规性检查', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[\"/home/order/order_sign\",\"/home/order/order_manage\"],\"plan\":[],\"abnormal\":[],\"task\":[],\"system\":[]}');
INSERT INTO `role` VALUES ('15', '财务会计', '订金确认', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[\"/home/order/order_sign\",\"/home/order/order_manage\"],\"plan\":[],\"abnormal\":[],\"task\":[],\"system\":[]}');

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
INSERT INTO `sign_process` VALUES ('3', '正常签核流程', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\"},{\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\"}]', '2017-12-11 23:57:56', '2017-12-15 20:30:47');
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('1', 'tsk111', '1', '1', 'guidance111');
INSERT INTO `task` VALUES ('2', 'tskName222Have', '1', '2', 'guidance2222');
INSERT INTO `task` VALUES ('3', 'tskName333Have', '1', '2', 'guidance3333');
INSERT INTO `task` VALUES ('4', 'tsk44', '1', '4', 'guidance444');
INSERT INTO `task` VALUES ('5', 'tskAbc', '1', '3', 'guidanceAAA');

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
  `task_record_id` int(10) unsigned NOT NULL COMMENT '对应安装项ID',
  `name` varchar(255) NOT NULL COMMENT '质检员名字',
  `status` tinyint(4) NOT NULL COMMENT '质检结果: "1"==>通过； “0”==>不通过',
  `comment` text COMMENT '质检备注',
  `create_time` datetime NOT NULL COMMENT '添加质检结果的时间',
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
  `begin_time` datetime DEFAULT NULL COMMENT 'task开始时间',
  `end_time` datetime DEFAULT NULL COMMENT 'task结束时间',
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
INSERT INTO `user` VALUES ('11', 'hutong', '胡通', '8', 'sinsim', '1', '1');
INSERT INTO `user` VALUES ('12', 'test', '张三', '5', 'sinsim', null, '1');
