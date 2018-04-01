/*
Navicat MySQL Data Transfer

Source Server         : MyDB
Source Server Version : 50547
Source Host           : localhost:3306
Source Database       : sinsim_db

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2018-03-22 16:22:11
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of abnormal
-- ----------------------------
INSERT INTO `abnormal` VALUES ('6', '缺料', '1', '2018-03-19 11:26:21', null);
INSERT INTO `abnormal` VALUES ('8', 'test2', '0', '2018-03-19 15:04:56', '2018-03-19 16:23:46');

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
INSERT INTO `abnormal_record` VALUES ('4', '6', '54', '2', '', '仓库数据有误', '1', '2018-03-20 15:31:55', '2018-03-22 14:18:33');
INSERT INTO `abnormal_record` VALUES ('7', '8', '53', '1', '', null, null, '2018-03-22 16:09:19', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract
-- ----------------------------
INSERT INTO `contract` VALUES ('30', 'xs-201712010', '测试', '张三', '2017-12-30', '', '', '3', '2017-12-18 15:32:25', null);
INSERT INTO `contract` VALUES ('31', 'xs-201712009', '胡通', '张三', '2018-02-28', '支付宝', '', '0', '2017-12-18 15:57:06', '2018-03-15 15:06:41');
INSERT INTO `contract` VALUES ('40', 'xs-201712001', '李四', '王五', '2017-12-30', '', '', '0', '2017-12-18 16:55:14', '2018-02-11 16:07:56');
INSERT INTO `contract` VALUES ('41', 'ss-20180116', '张三', '李四', '2018-01-18', '支付宝', '无', '2', '2018-01-16 01:15:00', '2018-03-08 17:34:12');
INSERT INTO `contract` VALUES ('42', 'ss-20180120', '张三', '李四', '2018-01-31', '支付宝', '', '0', '2018-01-20 09:36:37', '2018-03-06 01:46:43');
INSERT INTO `contract` VALUES ('52', 'ss-20180209', '张三', '李四', '2018-02-24', '支付宝', '无', '0', '2018-02-09 01:09:59', '2018-02-12 11:12:45');
INSERT INTO `contract` VALUES ('53', 'HT20180316', 'zs', 'zs', '2018-03-01', 'dgdfg', 'dgdgdfgd', '0', '2018-03-15 14:37:46', null);
INSERT INTO `contract` VALUES ('54', '20180318', 'zs', 'ls', '2019-03-15', 'sfdsf', 'sdsdfsdf', '0', '2018-03-15 14:42:12', '2018-03-15 15:24:45');
INSERT INTO `contract` VALUES ('57', '20180320', 'zd', 'cd', '2019-03-16', 'sdfsdf', 'sdd', '0', '2018-03-15 14:59:40', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

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
INSERT INTO `contract_sign` VALUES ('42', '41', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"2018-02-09 16:58:24\",\"user\":\"胡通\",\"result\":1,\"comment\":\"同意\"},{\"number\":4,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"2018-03-08 16:32:53\",\"user\":\"胡通\",\"result\":1,\"comment\":\"sdvsdf\"},{\"number\":5,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"2018-03-08 17:34:12\",\"user\":\"胡通\",\"result\":1,\"comment\":\"dfdfgdfg\"}]', '签核完成', '2018-01-23 14:44:53', '2018-03-08 17:34:12');
INSERT INTO `contract_sign` VALUES ('52', '52', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '', '2018-02-09 01:09:59', null);
INSERT INTO `contract_sign` VALUES ('53', '53', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '', '2018-03-15 14:37:47', null);
INSERT INTO `contract_sign` VALUES ('54', '54', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '', '2018-03-15 14:42:12', null);
INSERT INTO `contract_sign` VALUES ('57', '57', '[{\"number\":1,\"roleId\":7,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":13,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":14,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":6,\"roleId\":6,\"signType\":\"合同签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '', '2018-03-15 14:59:40', null);

-- ----------------------------
-- Table structure for `device`
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '设备名称',
  `meid` varchar(255) NOT NULL COMMENT 'MEID地址',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine
-- ----------------------------
INSERT INTO `machine` VALUES ('16', '22', 'ABP112914371', '2080506', 'Left', '1', '1', '2017-12-26 11:29:14', '2017-12-27 01:11:28', null, null);
INSERT INTO `machine` VALUES ('17', '22', 'ABP112914752', '20180603', 'Right', '1', '1', '2017-12-26 11:29:14', '2017-12-27 01:11:28', null, null);
INSERT INTO `machine` VALUES ('18', '22', 'ABP112914563', '454543', 'tryt', '1', '1', '2017-12-26 11:29:14', '2017-12-27 01:11:28', null, null);
INSERT INTO `machine` VALUES ('19', '28', 'ABQ154551411', null, null, '1', '1', '2017-12-27 15:45:51', null, null, null);
INSERT INTO `machine` VALUES ('20', '28', 'ABQ154551452', 'rt6', 'fgh676', '0', '1', '2017-12-27 15:45:51', null, null, null);
INSERT INTO `machine` VALUES ('21', '28', 'ABQ154551433', '4534', 'tgetre', '0', '1', '2017-12-27 15:45:51', null, null, null);
INSERT INTO `machine` VALUES ('22', '49', 'ABQ154551051', '20180204', '左上', '0', '12', '2017-12-27 15:45:51', '2017-12-27 15:46:26', null, null);
INSERT INTO `machine` VALUES ('23', '49', 'ABQ154551862', null, null, '0', '12', '2017-12-27 15:45:51', '2017-12-27 15:46:26', null, null);
INSERT INTO `machine` VALUES ('24', '49', 'ABQ154551573', null, null, '0', '12', '2017-12-27 15:45:51', '2017-12-27 15:46:26', null, null);
INSERT INTO `machine` VALUES ('25', '49', 'ABQ154551224', null, null, '0', '12', '2017-12-27 15:45:51', '2017-12-27 15:46:26', null, null);
INSERT INTO `machine` VALUES ('26', '50', 'A0M094156411', null, null, '0', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('27', '50', 'A0M094156732', null, null, '1', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('28', '51', 'A0M094156191', null, null, '0', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('29', '51', 'A0M094156892', null, null, '1', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('30', '51', 'A0M094156773', null, null, '2', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('31', '56', 'A0M094156521', 'hellod', '1223', '4', '1', '2018-01-23 09:41:56', '2018-01-23 14:44:54', null, null);
INSERT INTO `machine` VALUES ('32', '52', 'A0M094156492', null, null, '3', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('33', '52', 'A0M094156333', null, null, '3', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('34', '52', 'A0M094156074', null, null, '3', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('35', '52', 'A0M094156575', null, null, '4', '1', '2018-01-23 09:41:56', null, null, null);
INSERT INTO `machine` VALUES ('36', '52', 'A0M094156526', null, null, '1', '1', '2018-01-23 09:41:56', null, null, null);

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
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine_order
-- ----------------------------
INSERT INTO `machine_order` VALUES ('22', '0712007', '0', '30', '50', '1', '3', '英语', 'SINSIM电脑绣花机', '3', '1', '11', '1', '10', '100', '200', '单机', null, null, '50000', '2017-12-31', '2017-12-31', '无', '王五', '', '2017-12-18 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('23', '0712007', '0', '31', '51', '1', '0', '英语', 'SINSIM电脑绣花机', '3', '1', '11', '1', '10', '100', '200', '单机', null, '[]', '0', '2017-12-31', '2017-12-31', '无', '王五', '', '2017-12-18 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('28', '0712001', '0', '40', '62', '1', '0', '英语', 'SINSIM电脑绣花机', '3', '1', '20', '10', '5', '100', '200', '叠机', null, '[]', '30000', '2017-12-31', '2017-12-25', '', '李四', '', '2017-12-18 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('37', '0712002', '0', '40', '71', '1', '3', '英语', 'SINSIM电脑绣花机', '4', '12', '20', '10', '5', '100', '200', '叠机', null, '[]', '30000', '2017-12-31', '2017-12-25', '', '李四', '', '2017-12-20 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('39', '0712008', '0', '31', '73', '1', '0', '英语', 'SINSIM电脑绣花机', '3', '4', '11', '1', '10', '100', '200', '单机', null, '[]', '50000', '2017-12-31', '2017-12-31', '无', '王五', '', '2017-12-20 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('47', '0712007-1', '22', '30', '81', '1', '0', '英语', 'SINSIM电脑绣花机', '4', '1', '11', '1', '10', '100', '200', '单机', null, null, '50000', '2017-12-31', '2017-12-31', '无', '王五', '', '2017-12-27 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('48', '0712007-1', '22', '30', '82', '1', '0', '英语', 'SINSIM电脑绣花机', '4', '1', '11', '1', '10', '100', '200', '单机', null, null, '50000', '2017-12-31', '2017-12-31', '无', '王五', '', '2017-12-27 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('49', '0712002-1', '37', '40', '83', '1', '0', '英语', 'SINSIM电脑绣花机', '5', '12', '20', '10', '5', '100', '200', '叠机', null, '[{\"name\":\"佳宇绳绣\",\"number\":1,\"price\":\"500\"},{\"name\":\"佳宇金片\",\"number\":1,\"price\":\"666\"}]', '30000', '2017-12-31', '2017-12-25', '', '李四', '', '2017-12-27 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('50', '20180101', '0', '41', '84', '1', '2', '英语', 'SINSIM电脑绣花机', '2', '1', '10', '20', '1', '1', '1', '单机', null, null, '50000', '2018-01-27', '2018-02-01', '', '李四', '', '2018-01-16 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('51', '20180102', '0', '41', '85', '1', '2', '英语', 'SINSIM电脑绣花机', '3', '1', '10', '20', '1', '1', '1', '单机', null, null, '65000', '2018-01-27', '2018-02-01', '', '李四', '', '2018-01-16 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('52', '20180102-1', '51', '41', '86', '1', '4', '英语', 'SINSIM电脑绣花机', '5', '1', '10', '20', '1', '1', '1', '单机', null, null, '65000', '2018-01-27', '2018-02-01', '', '李四', '', '2018-01-16 00:00:00', '2018-01-23 14:45:23', null);
INSERT INTO `machine_order` VALUES ('53', '180120-1', '0', '42', '87', '1', '0', '中国', 'SINSIM电脑绣花机', '2', '2', '1', '1', '1', '22', '33', '叠机', null, '[]', '45678', '2018-01-31', '2018-01-31', '暂无', '李四', '', '2018-01-20 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('56', '20180102-1gg', '52', '41', '90', '1', '2', '英语', 'SINSIM电脑绣花机', '1', '1', '10', '20', '1', '1', '1', '单机', null, null, '60000', '2018-01-27', '2018-02-01', '', '李四', '', '2018-01-23 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('57', '001', '0', '52', '100', '1', '0', '中文', 'SINSIM电脑绣花机', '10', '1', '11', '10', '10', '22', '22', '单机', null, '', '55555', '2018-02-28', '2018-02-28', '暂无', '李四', 'SinSim保修', '2018-02-09 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('58', '20180316', null, '53', '101', '1', '0', '中国', 'SINSIM电脑绣花机', '2', '1', '4', '4', '4', '100', '200', '叠机', null, '[{\"name\":\"佳宇金片\",\"number\":2,\"price\":\"1000\"}]', '20000', '2018-03-23', '2018-03-21', 'sdfsdfsfssd', 'zs', '代理商保修', '2018-03-15 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('59', '20180308', '0', '54', '102', '1', '0', '', 'SINSIM电脑绣花机', '2', '2', '3', '2', '3', 'xx', 'dd', '叠机', '包装备注', '[]', '5000', '2018-03-30', '2018-03-21', 'sdfsdf', 'ls', 'SinSim保修', '2018-03-15 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('60', '20180320-1', null, '57', '105', '1', '0', null, 'SINSIM电脑绣花机', '2', '2', '1', '1', '1', '20', '200', '叠机', null, '[]', '50000', '2019-03-09', '2019-03-22', 'sdfsdfsd', 'cd', '代理商保修', '2018-03-15 00:00:00', null, null);

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
INSERT INTO `order_detail` VALUES ('1', 'rred', '11', 'dd', 'ttt', 'head', 'nnbb', 'opc', null, 'ff', 'ff', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhh', null, 'rrr', 'ccc', 'hhhh', 'jj', 'yy', 'yyr', 'fttrttr', 'fr', 'rfrf', 'aaa', 'bbbb', 'st', 'aa', 'aa', 'dd', 'hol', '11', '22', 'fffffff');
INSERT INTO `order_detail` VALUES ('2', 'blue', '33', 'aa', 'ff', '11', 'bbbb', 'vv', null, 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', null, 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'rr', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('27', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', null, 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', null, 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'add-by-interface--GetID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('28', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', null, 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', null, 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'add-by-interface--GetID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('29', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', null, 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', null, 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', '222add-by-interface--GetID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('30', '2222blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', null, 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', null, 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', '222add-by-interface--GetID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('31', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', null, 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', null, 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'yizhixing-add-by-interface--returnID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('32', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', null, 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', null, 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'yizhixing-add-by-interface--returnID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('33', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', null, 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', null, 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'yizhixing-add-by-interface--returnID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('34', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', null, 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', null, 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'yizhixing-add-by-interface--returnID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('35', 'blue--add-by-interface', '33', 'aa', 'ff', '11', 'bbbb', 'vv', null, 'ff', 'xx', 'ss', 'dd', 'dd', 'ee', 'ff', 'gg', 'hhhhhhhh', null, 'rrrrrr', 'ccccc', 'hhhhhh', 'jj', 'yy', 'yizhixing-add-by-interface--returnID', 'red', 'fraa', 'fffff', 'aaaaa', 'bbbbbb', 'ssss', 'aa', 'aad', 'ddd', 'hoeww', '12', '23', 'rrrrrrrrrrr');
INSERT INTO `order_detail` VALUES ('50', '6色', '集中', '集中', '大豪', '冠军独立', '16', '528', null, '儒竞', '五相步进', '不剪线', '380V', '3个', '下点动', '伟龙款', '上塑料下铁', '11', null, '普通导轨', '三型断检', '佐伩12-R', '电机跳跃', '有', null, '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '1个托架下', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('51', '6色', '集中', '集中', '大豪', '冠军独立', '16', '528', '', '儒竞', '五相步进', '不剪线', '380V', '3个', '下点动', '伟龙款', '上塑料下铁', '11', '', '普通导轨', '三型断检', '佐伩12-R', '电机跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '1个托架下', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('62', '4色', '独立', '集中', '大豪', '普通全独立', '18', '316', null, '大豪', '五相步进', '普通剪线', '380V', '3个', '下自动', '15款信胜高速', '上塑料下复合', '14', null, '珠架导轨', '三型断检', '佐伩12-RP', '电磁铁跳跃带轴承', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '1个托架下,一个另一侧梁上长杆', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('71', '4色', '独立', '集中', '大豪', '普通全独立', '18', '316', null, '大豪', '五相步进', '普通剪线', '380V', '3个', '下自动', '15款信胜高速', '上塑料下复合', '14', null, '珠架导轨', '三型断检', '佐伩12-RP', '电磁铁跳跃带轴承', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '1个托架下,一个另一侧梁上长杆', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('73', '6色', '集中', '集中', '松下', '冠军独立', '16', '528', '', '儒竞', '五相步进', '不剪线', '380V', '3个', '下点动', '伟龙款', '上塑料下铁', '11', '', '普通导轨', '三型断检', '佐伩12-R', '电机跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '1个托架下', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('81', '6色', '集中', '集中', '大豪', '冠军独立', '16', '528', null, '儒竞', '五相步进', '不剪线', '380V', '3个', '下点动', '伟龙款', '上塑料下铁', '11', null, '普通导轨', '三型断检', '佐伩12-R', '电机跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '1个托架下', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('82', '6色', '集中', '集中', '大豪', '冠军独立', '16', '528', null, '儒竞', '五相步进', '不剪线', '380V', '3个', '下点动', '伟龙款', '上塑料下铁', '11', null, '普通导轨', '三型断检', '佐伩12-R', '电机跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '1个托架下', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('83', '4色', '独立', '集中', '大豪', '普通全独立', '18', '316', null, '大豪', '五相步进', '普通剪线', '380V', '3个', '下自动', '15款信胜高速', '上塑料下复合', '14', null, '珠架导轨', '三型断检', '佐伩12-RP', '电磁铁跳跃带轴承', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '1个托架下,一个另一侧梁上长杆', '梁下普通', '普通', '普通', '正常', '5', '5', '正常');
INSERT INTO `order_detail` VALUES ('84', '4色', '独立', '无', '大豪', '冠军独立', '16', 'M98', null, '大豪', '三相步进', '电机剪线', '380V', '1个', '下点动', '伟龙款', '上塑料下铁', '11', null, '珠架导轨', '三型断检', '佐伩12-R', '电机跳跃', '无', '暂无', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '台板上', '一个托架下,一个左侧台板下', 'LED灯', '普通', '普通', '正常', '4', '4', '正常');
INSERT INTO `order_detail` VALUES ('85', '4色', '独立', '无', '松下', '冠军独立', '16', 'M98', null, '大豪', '三相步进', '电机剪线', '380V', '1个', '下点动', '伟龙款', '上塑料下铁', '11', null, '珠架导轨', '三型断检', '佐伩12-R', '电机跳跃', '无', '暂无', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '台板上', '一个托架下,一个左侧台板下', 'LED灯', '普通', '普通', '正常', '4', '4', '正常');
INSERT INTO `order_detail` VALUES ('86', '4色', '独立', '无', '松下', '冠军独立', '16', 'M98', null, '大豪', '三相步进', '电机剪线', '380V', '1个', '下点动', '伟龙款', '上塑料下铁', '11', null, '珠架导轨', '三型断检', '佐伩12-R', '电机跳跃', '无', '暂无', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '台板上', '一个托架下,一个左侧台板下', 'LED灯', '普通', '普通', '正常', '4', '4', '正常');
INSERT INTO `order_detail` VALUES ('87', '4色', '独立', '独立', '大豪', '冠军独立', '16', 'C29', '英语', '大豪', '三相步进', '电机剪线', '380V', '3个', '下自动', '伟龙款', '上塑料下复合', '14', null, '珠架导轨', '二位底检', '广濑1.6倍高速', '电磁铁跳跃', '有', '无', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '有', '台板上', '1个托架下', 'LED灯', '普通', '宽幅(中传动)', '', '2', '2', '正常');
INSERT INTO `order_detail` VALUES ('90', '4色', '独立', '无', '大豪', '冠军独立', '16', 'M98', null, '大豪', '三相步进', '电机剪线', '380V', '1个', '下点动', '伟龙款', '上塑料下铁', '11', null, '珠架导轨', '三型断检', '佐伩12-R', '电机跳跃', '无', '暂无', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '台板上', '一个托架下,一个左侧台板下', 'LED灯', '普通', '普通', '正常', '4', '4', '正常');
INSERT INTO `order_detail` VALUES ('100', '无', '独立', '独立', '大豪', '冠军独立', '16', '', null, '', '', '', '', '', '', '伟龙款', '上下分体面板', '11', null, '珠架导轨', '三型断检', '广濑1.6倍高速', '电磁铁跳跃', '无', '暂无', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '台板上', '1个托架下', 'LED灯', '普通', '普通', '正常', '1', '1', '正常');
INSERT INTO `order_detail` VALUES ('101', '4色', '独立', '无', '松下', '冠军独立', '16', '316', '西班牙', '大豪', '三相步进', '电机剪线', '220V', '2个', '下点动', '伟龙款', '上塑料下铁', '11', null, '珠架导轨', '二位底检', '广濑1.6倍高速', '电磁铁跳跃', '有', null, '4201', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '一个托架下,一个左侧台板下', 'LED灯', '普通', '普通', '正常', '4', '4', '正常');
INSERT INTO `order_detail` VALUES ('102', '6色', '独立', '独立', '大豪', '普通全独立', '16', '316', '韩语', '松下', '三相步进', '普通剪线', '380V', 'Y驱动数+1', '上机壳旁油盒下自动', '伟龙款', '上塑料下铁', '9', 'GROZ', '珠架导轨', '三型断检', '广濑1.6倍', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '有', '梁上', '1个托架下', '梁下普通', '普通', '普通', '正常', '3', '3', '正常');
INSERT INTO `order_detail` VALUES ('105', '4色', '独立', '独立', '松下', '冠军独立', '16', '366', '葡萄牙', '松下', '五相步进', '电机剪线', '380V', '无', '下点动', '15款信胜高速', '上塑料下铁', '11', null, '珠架导轨', '三型断检', '广濑1.6倍', '电机跳跃', '有', null, '4201', '杨桉木', '浅绿', '有', '梁上', '1个托架下', '梁下普通', '普通', '普通', '正常', '3', '2', '正常');

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
INSERT INTO `order_loading_list` VALUES ('22', '56', 'D:/oll/null_56_LoadingFile_2018-02-24-09-51-37.xlsx', '2', '2018-02-12 10:27:11', '2018-03-05 13:47:09');
INSERT INTO `order_loading_list` VALUES ('23', '22', 'D:/oll/null_22_LoadingFile.xlsx', '2', '2018-02-12 10:27:11', null);
INSERT INTO `order_loading_list` VALUES ('25', '23', 'D:/oll/null_23_LoadingFile.xlsx', '2', '1973-10-02 17:33:51', '2018-03-05 13:47:09');

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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

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
INSERT INTO `order_sign` VALUES ('24', '56', '[{\"date\":\"2018-02-09 17:03:34\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"胡通\"},{\"date\":\"2018-03-08 16:32:18\",\"result\":1,\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"fgfdg\",\"user\":\"胡通\"}]', '2018-01-23 14:44:54', '2018-03-08 16:32:18');
INSERT INTO `order_sign` VALUES ('25', '57', '[{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '2018-02-09 01:09:59', null);
INSERT INTO `order_sign` VALUES ('26', '58', '[{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '2018-03-15 14:37:47', null);
INSERT INTO `order_sign` VALUES ('27', '59', '[{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '2018-03-15 14:42:12', null);
INSERT INTO `order_sign` VALUES ('28', '60', '[{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":7,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '2018-03-15 14:59:40', null);

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
INSERT INTO `process` VALUES ('1', '流程一', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"Start\", \"key\":-1, \"loc\":\"207.15625000000003 39.99999999999999\"},\n{\"category\":\"End\", \"text\":\"End\", \"key\":-4, \"loc\":\"206.5960057626528 274.2676887129068\"},\n{\"text\":\"调试电脑\", \"task_status\":\"0\", \"begin_time\":\"\", \"end_time\":\"\", \"group_id\":3, \"group_name\":\"驱动安装组\", \"key\":-3, \"loc\":\"207.51251220703125 130\"},\n{\"text\":\"安装夹线器\", \"task_status\":\"0\", \"begin_time\":\"\", \"end_time\":\"\", \"group_id\":4, \"group_name\":\"台板安装组\", \"key\":-2, \"loc\":\"207.51251220703125 196\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-3, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.15625000000003,90.67268123183139,207.15625000000003,100.67268123183139,207.15625000000003,110.3363406159157,207.51251220703125,110.3363406159157,207.51251220703125,120,207.51251220703125,130 ]},\n{\"from\":-3, \"to\":-2, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.51251220703125,162.87544860839844,207.51251220703125,172.87544860839844,207.51251220703125,179.4377243041992,207.51251220703125,179.4377243041992,207.51251220703125,186,207.51251220703125,196 ]},\n{\"from\":-2, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[ 207.51251220703125,228.87544860839844,207.51251220703125,238.87544860839844,207.51251220703125,251.57156866065264,206.5960057626528,251.57156866065264,206.5960057626528,264.2676887129069,206.5960057626528,274.2676887129069 ]}\n ]}', '2017-11-30 10:58:17', '2018-03-06 15:08:59');
INSERT INTO `process` VALUES ('2', 'processBBB', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"Start\", \"key\":-1, \"loc\":\"207.15625000000003 39.99999999999999\"},\n{\"category\":\"End\", \"text\":\"End\", \"key\":-4, \"loc\":\"220.59600576265277 456.35934754209677\"},\n{\"text\":\"喷油漆\", \"task_status\":\"0\", \"begin_time\":\"\", \"end_time\":\"\", \"leader\":\"\", \"work_list\":\"\", \"key\":-3, \"loc\":\"208.49727153655056 215.54545021708802\"},\n{\"text\":\"安装电机\", \"task_status\":\"0\", \"begin_time\":\"\", \"end_time\":\"\", \"leader\":\"\", \"work_list\":\"\", \"key\":-5, \"loc\":\"207.49727153655056 134.545450217088\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-5, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[207.15625000000003,90.41860465116278,207.15625000000003,100.41860465116278,207.15625000000003,112.48202743412538,207.49727153655056,112.48202743412538,207.49727153655056,124.54545021708799,207.49727153655056,134.545450217088]},\n{\"from\":-5, \"to\":-3, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[207.49727153655056,167.14545021708798,207.49727153655056,177.14545021708798,207.49727153655056,191.345450217088,208.49727153655056,191.345450217088,208.49727153655056,205.54545021708802,208.49727153655056,215.54545021708802]},\n{\"from\":-3, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[208.49727153655056,248.145450217088,208.49727153655056,258.145450217088,208.49727153655056,352.2523988795924,220.5960057626528,352.2523988795924,220.5960057626528,446.35934754209677,220.5960057626528,456.35934754209677]}\n ]}', '2017-11-30 10:58:43', '2018-03-06 15:08:51');
INSERT INTO `process` VALUES ('3', 'pro33', '{ \"class\": \"go.GraphLinksModel\",\n  \"linkFromPortIdProperty\": \"fromPort\",\n  \"linkToPortIdProperty\": \"toPort\",\n  \"nodeDataArray\": [ \n{\"category\":\"Start\", \"text\":\"Start\", \"key\":-1, \"loc\":\"205.15625000000006 44.99999999999999\"},\n{\"category\":\"End\", \"text\":\"End\", \"key\":-4, \"loc\":\"205.5960057626528 495.3593475420968\"},\n{\"text\":\"调试电脑\", \"task_status\":\"0\", \"begin_time\":\"\", \"end_time\":\"\", \"leader\":\"\", \"work_list\":\"\", \"key\":-3, \"loc\":\"205.49727153655056 158.545450217088\"},\n{\"text\":\"安装电机\", \"task_status\":\"0\", \"begin_time\":\"\", \"end_time\":\"\", \"leader\":\"\", \"work_list\":\"\", \"key\":-5, \"loc\":\"205.49727153655056 259.54545021708805\"},\n{\"text\":\"安裝主传动\", \"task_status\":\"0\", \"begin_time\":\"\", \"end_time\":\"\", \"leader\":\"\", \"work_list\":\"\", \"key\":-6, \"loc\":\"205.49727153655056 367.545450217088\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":-1, \"to\":-3, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[205.15625000000003,95.41860465116278,205.15625000000003,105.41860465116278,205.15625000000003,126.98202743412538,205.49727153655056,126.98202743412538,205.49727153655056,148.545450217088,205.49727153655056,158.545450217088]},\n{\"from\":-3, \"to\":-5, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[205.49727153655056,191.14545021708798,205.49727153655056,201.14545021708798,205.49727153655056,225.345450217088,205.49727153655056,225.345450217088,205.49727153655056,249.54545021708805,205.49727153655056,259.54545021708805]},\n{\"from\":-5, \"to\":-6, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[205.49727153655056,292.14545021708807,205.49727153655056,302.14545021708807,205.49727153655056,329.845450217088,205.49727153655056,329.845450217088,205.49727153655056,357.545450217088,205.49727153655056,367.545450217088]},\n{\"from\":-6, \"to\":-4, \"fromPort\":\"B\", \"toPort\":\"T\", \"points\":[205.49727153655056,400.145450217088,205.49727153655056,410.145450217088,205.49727153655056,447.7523988795924,205.5960057626528,447.7523988795924,205.5960057626528,485.3593475420968,205.5960057626528,495.3593475420968]}\n ]}', '2017-11-30 11:10:08', '2018-03-06 15:04:32');

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
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of process_record
-- ----------------------------
INSERT INTO `process_record` VALUES ('1', '36', '3', '', '', '2018-01-25 19:34:43', null);
INSERT INTO `process_record` VALUES ('2', '16', '1', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-3,\"points\":[207.15625000000003,90.67268123183139,207.15625000000003,100.67268123183139,207.15625000000003,110.3363406159157,207.51251220703125,110.3363406159157,207.51251220703125,120,207.51251220703125,130]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-2,\"points\":[207.51251220703125,162.87544860839844,207.51251220703125,172.87544860839844,207.51251220703125,179.4377243041992,207.51251220703125,179.4377243041992,207.51251220703125,186,207.51251220703125,196]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-4,\"points\":[207.51251220703125,228.87544860839844,207.51251220703125,238.87544860839844,207.51251220703125,251.57156866065264,206.5960057626528,251.57156866065264,206.5960057626528,264.2676887129069,206.5960057626528,274.2676887129069]}]', '[{\"loc\":\"207.15625000000003 39.99999999999999\",\"text\":\"Start\",\"category\":\"Start\",\"key\":-1},{\"loc\":\"206.5960057626528 274.2676887129068\",\"text\":\"End\",\"category\":\"End\",\"key\":-4},{\"loc\":\"207.51251220703125 130\",\"task_status\":1,\"group_id\":3,\"group_name\":\"驱动安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"调试电脑\",\"key\":-3},{\"loc\":\"207.51251220703125 196\",\"task_status\":\"0\",\"group_id\":4,\"group_name\":\"台板安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安装夹线器\",\"key\":-2}]', '2018-01-31 00:47:27', null);
INSERT INTO `process_record` VALUES ('3', '17', '1', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-3,\"points\":[207.15625000000003,90.67268123183139,207.15625000000003,100.67268123183139,207.15625000000003,110.3363406159157,207.51251220703125,110.3363406159157,207.51251220703125,120,207.51251220703125,130]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-2,\"points\":[207.51251220703125,162.87544860839844,207.51251220703125,172.87544860839844,207.51251220703125,179.4377243041992,207.51251220703125,179.4377243041992,207.51251220703125,186,207.51251220703125,196]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-4,\"points\":[207.51251220703125,228.87544860839844,207.51251220703125,238.87544860839844,207.51251220703125,251.57156866065264,206.5960057626528,251.57156866065264,206.5960057626528,264.2676887129069,206.5960057626528,274.2676887129069]}]', '[{\"loc\":\"207.15625000000003 39.99999999999999\",\"text\":\"Start\",\"category\":\"Start\",\"key\":-1},{\"loc\":\"206.5960057626528 274.2676887129068\",\"text\":\"End\",\"category\":\"End\",\"key\":-4},{\"loc\":\"207.51251220703125 130\",\"task_status\":\"0\",\"group_id\":3,\"group_name\":\"驱动安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"调试电脑\",\"key\":-3},{\"loc\":\"207.51251220703125 196\",\"task_status\":\"0\",\"group_id\":4,\"group_name\":\"台板安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安装夹线器\",\"key\":-2}]', '2018-03-06 15:43:08', null);
INSERT INTO `process_record` VALUES ('8', '35', '1', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-3,\"points\":[207.15625000000003,90.67268123183139,207.15625000000003,100.67268123183139,207.15625000000003,110.3363406159157,207.51251220703125,110.3363406159157,207.51251220703125,120,207.51251220703125,130]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-2,\"points\":[207.51251220703125,162.87544860839844,207.51251220703125,172.87544860839844,207.51251220703125,179.4377243041992,207.51251220703125,179.4377243041992,207.51251220703125,186,207.51251220703125,196]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-4,\"points\":[207.51251220703125,228.87544860839844,207.51251220703125,238.87544860839844,207.51251220703125,251.57156866065264,206.5960057626528,251.57156866065264,206.5960057626528,264.2676887129069,206.5960057626528,274.2676887129069]}]', '[{\"loc\":\"207.15625000000003 39.99999999999999\",\"text\":\"Start\",\"category\":\"Start\",\"key\":-1},{\"loc\":\"206.5960057626528 274.2676887129068\",\"text\":\"End\",\"category\":\"End\",\"key\":-4},{\"loc\":\"207.51251220703125 130\",\"task_status\":\"0\",\"group_id\":3,\"group_name\":\"驱动安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"调试电脑\",\"key\":-3},{\"loc\":\"207.51251220703125 196\",\"task_status\":\"0\",\"group_id\":4,\"group_name\":\"台板安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安装夹线器\",\"key\":-2}]', '2018-01-31 23:30:21', null);
INSERT INTO `process_record` VALUES ('9', '35', '1', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-3,\"points\":[207.15625000000003,90.67268123183139,207.15625000000003,100.67268123183139,207.15625000000003,110.3363406159157,207.51251220703125,110.3363406159157,207.51251220703125,120,207.51251220703125,130]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-2,\"points\":[207.51251220703125,162.87544860839844,207.51251220703125,172.87544860839844,207.51251220703125,179.4377243041992,207.51251220703125,179.4377243041992,207.51251220703125,186,207.51251220703125,196]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-4,\"points\":[207.51251220703125,228.87544860839844,207.51251220703125,238.87544860839844,207.51251220703125,251.57156866065264,206.5960057626528,251.57156866065264,206.5960057626528,264.2676887129069,206.5960057626528,274.2676887129069]}]', '[{\"loc\":\"207.15625000000003 39.99999999999999\",\"text\":\"Start\",\"category\":\"Start\",\"key\":-1},{\"loc\":\"206.5960057626528 274.2676887129068\",\"text\":\"End\",\"category\":\"End\",\"key\":-4},{\"loc\":\"207.51251220703125 130\",\"task_status\":\"0\",\"group_id\":3,\"group_name\":\"驱动安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"调试电脑\",\"key\":-3},{\"loc\":\"207.51251220703125 196\",\"task_status\":\"0\",\"group_id\":4,\"group_name\":\"台板安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安装夹线器\",\"key\":-2}]', '2018-01-31 23:30:21', null);
INSERT INTO `process_record` VALUES ('10', '33', '1', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-3,\"points\":[207.15625000000003,90.67268123183139,207.15625000000003,100.67268123183139,207.15625000000003,110.3363406159157,207.51251220703125,110.3363406159157,207.51251220703125,120,207.51251220703125,130]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-2,\"points\":[207.51251220703125,162.87544860839844,207.51251220703125,172.87544860839844,207.51251220703125,179.4377243041992,207.51251220703125,179.4377243041992,207.51251220703125,186,207.51251220703125,196]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-4,\"points\":[207.51251220703125,228.87544860839844,207.51251220703125,238.87544860839844,207.51251220703125,251.57156866065264,206.5960057626528,251.57156866065264,206.5960057626528,264.2676887129069,206.5960057626528,274.2676887129069]}]', '[{\"loc\":\"207.15625000000003 39.99999999999999\",\"text\":\"Start\",\"category\":\"Start\",\"key\":-1},{\"loc\":\"206.5960057626528 274.2676887129068\",\"text\":\"End\",\"category\":\"End\",\"key\":-4},{\"loc\":\"207.51251220703125 130\",\"task_status\":\"0\",\"group_id\":3,\"group_name\":\"驱动安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"调试电脑\",\"key\":-3},{\"loc\":\"207.51251220703125 196\",\"task_status\":\"0\",\"group_id\":4,\"group_name\":\"台板安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安装夹线器\",\"key\":-2}]', '2018-02-01 00:10:25', null);
INSERT INTO `process_record` VALUES ('11', '27', '1', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-3,\"points\":[207.15625000000003,90.67268123183139,207.15625000000003,100.67268123183139,207.15625000000003,110.3363406159157,207.51251220703125,110.3363406159157,207.51251220703125,120,207.51251220703125,130]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-2,\"points\":[207.51251220703125,162.87544860839844,207.51251220703125,172.87544860839844,207.51251220703125,179.4377243041992,207.51251220703125,179.4377243041992,207.51251220703125,186,207.51251220703125,196]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-4,\"points\":[207.51251220703125,228.87544860839844,207.51251220703125,238.87544860839844,207.51251220703125,251.57156866065264,206.5960057626528,251.57156866065264,206.5960057626528,264.2676887129069,206.5960057626528,274.2676887129069]}]', '[{\"loc\":\"207.15625000000003 39.99999999999999\",\"text\":\"Start\",\"category\":\"Start\",\"key\":-1},{\"loc\":\"206.5960057626528 274.2676887129068\",\"text\":\"End\",\"category\":\"End\",\"key\":-4},{\"loc\":\"207.51251220703125 130\",\"task_status\":\"0\",\"group_id\":3,\"group_name\":\"驱动安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"调试电脑\",\"key\":-3},{\"loc\":\"207.51251220703125 196\",\"task_status\":\"0\",\"group_id\":4,\"group_name\":\"台板安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安装夹线器\",\"key\":-2}]', '2018-02-01 01:17:15', null);
INSERT INTO `process_record` VALUES ('12', '34', '1', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-3,\"points\":[207.15625000000003,90.67268123183139,207.15625000000003,100.67268123183139,207.15625000000003,110.3363406159157,207.51251220703125,110.3363406159157,207.51251220703125,120,207.51251220703125,130]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-2,\"points\":[207.51251220703125,162.87544860839844,207.51251220703125,172.87544860839844,207.51251220703125,179.4377243041992,207.51251220703125,179.4377243041992,207.51251220703125,186,207.51251220703125,196]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-5,\"points\":[207.51251220703125,228.87544860839844,207.51251220703125,238.87544860839844,207.51251220703125,249.53772429823874,210.875,249.53772429823874,210.875,260.19999998807907,210.875,270.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-5,\"to\":-4,\"points\":[210.875,303.0754485964775,210.875,313.0754485964775,210.875,365.1715686546921,213.5960057626528,365.1715686546921,213.5960057626528,417.2676887129068,213.5960057626528,427.2676887129068]}]', '[{\"loc\":\"207.15625000000003 39.99999999999999\",\"text\":\"Start\",\"category\":\"Start\",\"key\":-1},{\"loc\":\"213.5960057626528 427.2676887129069\",\"text\":\"End\",\"category\":\"End\",\"key\":-4},{\"loc\":\"207.51251220703125 130\",\"task_status\":\"0\",\"group_id\":3,\"group_name\":\"驱动安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"调试电脑\",\"key\":-3},{\"loc\":\"207.51251220703125 196\",\"task_status\":\"0\",\"group_id\":4,\"group_name\":\"台板安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安装夹线器\",\"key\":-2},{\"loc\":\"210.875 270.19999998807907\",\"task_status\":\"0\",\"group_id\":1,\"group_name\":\"上轴安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安裝主传动\",\"key\":-5}]', '2018-02-01 01:18:44', null);
INSERT INTO `process_record` VALUES ('13', '32', '1', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-3,\"points\":[207.15625000000003,90.67268123183139,207.15625000000003,100.67268123183139,207.15625000000003,110.3363406159157,207.51251220703125,110.3363406159157,207.51251220703125,120,207.51251220703125,130]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-2,\"points\":[207.51251220703125,162.87544860839844,207.51251220703125,172.87544860839844,207.51251220703125,179.4377243041992,207.51251220703125,179.4377243041992,207.51251220703125,186,207.51251220703125,196]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-5,\"points\":[207.51251220703125,228.87544860839844,207.51251220703125,238.87544860839844,207.51251220703125,256.03772429823874,207.875,256.03772429823874,207.875,273.19999998807907,207.875,283.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-5,\"to\":-6,\"points\":[207.875,316.0754485964775,207.875,326.0754485964775,207.875,340.1377242922783,207.875,340.1377242922783,207.875,354.19999998807907,207.875,364.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-6,\"to\":-4,\"points\":[207.875,397.0754485964775,207.875,407.0754485964775,207.875,433.1715686546921,208.5960057626528,433.1715686546921,208.5960057626528,459.2676887129068,208.5960057626528,469.2676887129068]}]', '[{\"loc\":\"207.15625000000003 39.99999999999999\",\"text\":\"Start\",\"category\":\"Start\",\"key\":-1},{\"loc\":\"208.59600576265277 469.2676887129068\",\"text\":\"End\",\"category\":\"End\",\"key\":-4},{\"loc\":\"207.51251220703125 130\",\"task_status\":\"0\",\"group_id\":3,\"group_name\":\"驱动安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"调试电脑\",\"key\":-3},{\"loc\":\"207.51251220703125 196\",\"task_status\":\"0\",\"group_id\":4,\"group_name\":\"台板安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安装夹线器\",\"key\":-2},{\"loc\":\"207.875 283.19999998807907\",\"task_status\":\"0\",\"group_id\":1,\"group_name\":\"上轴安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安裝主传动\",\"key\":-5},{\"loc\":\"207.875 364.19999998807907\",\"task_status\":\"0\",\"group_id\":2,\"group_name\":\"下轴安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"喷油漆\",\"key\":-6}]', '2018-02-01 01:23:28', null);
INSERT INTO `process_record` VALUES ('14', '19', '1', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-3,\"points\":[207.15625000000003,90.67268123183139,207.15625000000003,100.67268123183139,207.15625000000003,110.3363406159157,207.51251220703125,110.3363406159157,207.51251220703125,120,207.51251220703125,130]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-2,\"points\":[207.51251220703125,162.87544860839844,207.51251220703125,172.87544860839844,207.51251220703125,179.4377243041992,208.51251220703125,179.4377243041992,208.51251220703125,186,208.51251220703125,196]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-5,\"points\":[208.51251220703125,228.87544860839844,208.51251220703125,238.87544860839844,208.51251220703125,247.53772429823874,215.875,247.53772429823874,215.875,256.19999998807907,215.875,266.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-5,\"to\":-6,\"points\":[215.875,299.0754485964775,215.875,309.0754485964775,215.875,326.6377242922783,217.875,326.6377242922783,217.875,344.19999998807907,217.875,354.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-6,\"to\":-7,\"points\":[217.875,387.0754485964775,217.875,397.0754485964775,217.875,423.1377242922783,216.875,423.1377242922783,216.875,449.19999998807907,216.875,459.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-7,\"to\":-4,\"points\":[216.875,492.0754485964775,216.875,502.0754485964775,216.875,516.2715686487318,225.5960057626528,516.2715686487318,225.5960057626528,530.467688700986,225.5960057626528,540.467688700986]}]', '[{\"loc\":\"207.15625000000003 39.99999999999999\",\"text\":\"Start\",\"category\":\"Start\",\"key\":-1},{\"loc\":\"225.59600576265282 540.467688700986\",\"text\":\"End\",\"category\":\"End\",\"key\":-4},{\"loc\":\"207.51251220703125 130\",\"task_status\":\"0\",\"group_id\":3,\"group_name\":\"驱动安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"调试电脑\",\"key\":-3},{\"loc\":\"208.51251220703128 196\",\"task_status\":\"0\",\"group_id\":4,\"group_name\":\"台板安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安装夹线器\",\"key\":-2},{\"loc\":\"215.875 266.19999998807907\",\"task_status\":\"0\",\"group_id\":2,\"group_name\":\"下轴安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"喷油漆\",\"key\":-5},{\"loc\":\"217.875 354.19999998807907\",\"task_status\":\"0\",\"group_id\":1,\"group_name\":\"上轴安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安裝主传动\",\"key\":-6},{\"loc\":\"216.875 459.19999998807907\",\"task_status\":\"0\",\"group_id\":2,\"group_name\":\"下轴安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安装电机\",\"key\":-7}]', '2018-02-01 01:27:37', null);
INSERT INTO `process_record` VALUES ('15', '35', '1', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-3,\"points\":[207.15625000000003,90.67268123183139,207.15625000000003,100.67268123183139,207.15625000000003,110.3363406159157,207.51251220703125,110.3363406159157,207.51251220703125,120,207.51251220703125,130]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-2,\"points\":[207.51251220703125,162.87544860839844,207.51251220703125,172.87544860839844,207.51251220703125,179.4377243041992,207.51251220703125,179.4377243041992,207.51251220703125,186,207.51251220703125,196]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-5,\"points\":[207.51251220703125,228.87544860839844,207.51251220703125,238.87544860839844,207.51251220703125,257.03772429823874,209.875,257.03772429823874,209.875,275.19999998807907,209.875,285.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-5,\"to\":-6,\"points\":[209.875,318.0754485964775,209.875,328.0754485964775,209.875,344.1377242922783,208.875,344.1377242922783,208.875,360.19999998807907,208.875,370.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-6,\"to\":-4,\"points\":[208.875,403.0754485964775,208.875,413.0754485964775,208.875,473.2715686487317,212.5960057626528,473.2715686487317,212.5960057626528,533.467688700986,212.5960057626528,543.467688700986]}]', '[{\"loc\":\"207.15625000000003 39.99999999999999\",\"text\":\"Start\",\"category\":\"Start\",\"key\":-1},{\"loc\":\"212.59600576265282 543.467688700986\",\"text\":\"End\",\"category\":\"End\",\"key\":-4},{\"loc\":\"207.51251220703125 130\",\"task_status\":\"0\",\"group_id\":3,\"group_name\":\"驱动安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"调试电脑\",\"key\":-3},{\"loc\":\"207.51251220703125 196\",\"task_status\":\"0\",\"group_id\":4,\"group_name\":\"台板安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安装夹线器\",\"key\":-2},{\"loc\":\"209.875 285.19999998807907\",\"task_status\":\"0\",\"group_id\":1,\"group_name\":\"上轴安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安裝主传动\",\"key\":-5},{\"loc\":\"208.875 370.19999998807907\",\"task_status\":\"0\",\"group_id\":2,\"group_name\":\"下轴安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"喷油漆\",\"key\":-6}]', '2018-02-01 01:51:00', null);
INSERT INTO `process_record` VALUES ('16', '36', '1', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-3,\"points\":[207.15625000000003,90.67268123183139,207.15625000000003,100.67268123183139,207.15625000000003,110.3363406159157,207.51251220703125,110.3363406159157,207.51251220703125,120,207.51251220703125,130]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-2,\"points\":[207.51251220703125,162.87544860839844,207.51251220703125,172.87544860839844,207.51251220703125,179.4377243041992,207.51251220703125,179.4377243041992,207.51251220703125,186,207.51251220703125,196]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-4,\"points\":[207.51251220703125,228.87544860839844,207.51251220703125,238.87544860839844,207.51251220703125,251.57156866065264,206.5960057626528,251.57156866065264,206.5960057626528,264.2676887129069,206.5960057626528,274.2676887129069]}]', '[{\"loc\":\"207.15625000000003 39.99999999999999\",\"text\":\"Start\",\"category\":\"Start\",\"key\":-1},{\"loc\":\"206.5960057626528 274.2676887129068\",\"text\":\"End\",\"category\":\"End\",\"key\":-4},{\"loc\":\"207.51251220703125 130\",\"task_status\":\"0\",\"group_id\":3,\"group_name\":\"驱动安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"调试电脑\",\"key\":-3},{\"loc\":\"207.51251220703125 196\",\"task_status\":\"0\",\"group_id\":4,\"group_name\":\"台板安装组\",\"end_time\":\"\",\"begin_time\":\"\",\"text\":\"安装夹线器\",\"key\":-2}]', '2018-02-01 01:51:37', null);
INSERT INTO `process_record` VALUES ('17', '18', '2', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-5,\"points\":[207.15625000000003,90.41860465116278,207.15625000000003,100.41860465116278,207.15625000000003,112.48202743412538,207.49727153655056,112.48202743412538,207.49727153655056,124.54545021708799,207.49727153655056,134.545450217088]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-5,\"to\":-3,\"points\":[207.49727153655056,167.14545021708798,207.49727153655056,177.14545021708798,207.49727153655056,191.345450217088,208.49727153655056,191.345450217088,208.49727153655056,205.54545021708802,208.49727153655056,215.54545021708802]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-3,\"to\":-4,\"points\":[208.49727153655056,248.145450217088,208.49727153655056,258.145450217088,208.49727153655056,352.2523988795924,220.5960057626528,352.2523988795924,220.5960057626528,446.35934754209677,220.5960057626528,456.35934754209677]}]', '[{\"category\":\"Start\",\"key\":\"-1\",\"loc\":\"207.15625000000003 39.99999999999999\",\"text\":\"Start\"},{\"category\":\"End\",\"key\":\"-4\",\"loc\":\"220.59600576265277 456.35934754209677\",\"text\":\"End\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-3\",\"leader\":\"\",\"loc\":\"208.49727153655056 215.54545021708802\",\"taskStatus\":\"0\",\"text\":\"喷油漆\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-5\",\"leader\":\"\",\"loc\":\"207.49727153655056 134.545450217088\",\"taskStatus\":\"2\",\"text\":\"安装电机\",\"workList\":\"\"}]', '2018-03-07 11:57:51', null);

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
INSERT INTO `quality_record_image` VALUES ('1', '2', 'D:/images/quality/A0M094156526_null_Quality_2018-02-12-14-18-02.png', '2017-12-05 13:25:31');
INSERT INTO `quality_record_image` VALUES ('2', '1', 'D:/images/quality/A0M094156526_null_Quality_2018-02-12-14-31-38.png', '2017-12-05 13:25:31');

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
INSERT INTO `role` VALUES ('1', '超级管理员', '系统后台管理', '{\"contract\":[\"/home/contract/contract_sign\",\"/home/contract/sign_process\"],\"order\":[],\"machine\":[\"/home/machine/machine_config_process\",\"/home/machine/machine_install_process\"],\"plan\":[],\"abnormal\":[\"/home/abnormal/abnormal_statistic_manage\",\"/home/abnormal/abnormal_type_manage\"],\"task\":[\"/home/task/task_content_manage\",\"/home/task/process_manage\"],\"system\":[\"/home/system/user_manage\",\"/home/system/install_group_manage\",\"/home/system/role_manage\",\"/home/system/device_manager\"]}');
INSERT INTO `role` VALUES ('2', '生产部管理员', '主要Pad上操作，上传位置、pad上查看流程等', '{\"contract\":[\"/home/contract/sign_process\"],\"order\":[],\"machine\":[\"/home/machine/machine_config_process\",\"/home/machine/machine_install_process\"],\"plan\":null,\"abnormal\":[],\"task\":null,\"system\":null}');
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
INSERT INTO `task` VALUES ('1', '安裝主传动', '1', '1', 'guidance111');
INSERT INTO `task` VALUES ('2', '安装电机', '2', '2', 'guidance2222');
INSERT INTO `task` VALUES ('3', '喷油漆', '1', '2', 'guidance3333');
INSERT INTO `task` VALUES ('4', '安装夹线器', '1', '4', 'guidance444');
INSERT INTO `task` VALUES ('5', '调试电脑', '1', '3', 'guidanceAAA');

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
INSERT INTO `task_plan` VALUES ('4', '3', '1', '2018-01-28 01:24:24', null, '1', '2018-01-28 01:37:20', null);
INSERT INTO `task_plan` VALUES ('5', '4', '1', '2018-01-28 02:09:48', null, '1', '2018-01-28 02:18:01', null);
INSERT INTO `task_plan` VALUES ('6', '6', '1', '2018-01-31 23:05:09', null, '1', '2018-01-31 23:10:59', null);
INSERT INTO `task_plan` VALUES ('7', '11', '1', '2018-01-31 23:58:40', null, '1', '2018-01-31 23:58:57', null);
INSERT INTO `task_plan` VALUES ('8', '12', '1', '2018-01-31 23:58:40', null, '1', '2018-01-31 23:58:57', null);
INSERT INTO `task_plan` VALUES ('9', '19', '1', '2018-02-01 01:18:46', null, '1', '2018-02-01 01:19:05', null);
INSERT INTO `task_plan` VALUES ('10', '28', '1', '2018-02-01 01:18:46', null, '1', '2018-02-01 01:19:05', null);
INSERT INTO `task_plan` VALUES ('11', '39', '1', '2018-02-07 23:34:03', null, '1', '2018-02-07 23:34:35', null);
INSERT INTO `task_plan` VALUES ('20', '40', '1', '2018-02-08 00:13:38', null, '1', '2018-02-08 00:18:14', null);
INSERT INTO `task_plan` VALUES ('21', '41', '1', '2018-02-08 00:13:38', null, '1', '2018-02-08 00:18:14', null);
INSERT INTO `task_plan` VALUES ('23', '42', '1', '2018-02-08 00:18:30', null, '1', '2018-02-08 00:23:56', null);
INSERT INTO `task_plan` VALUES ('24', '43', '1', '2018-02-08 00:27:12', null, '1', '2018-02-08 00:37:08', null);
INSERT INTO `task_plan` VALUES ('25', '44', '1', '2018-02-08 00:27:12', null, '1', '2018-02-08 00:37:08', null);
INSERT INTO `task_plan` VALUES ('26', '23', '1', '2018-03-06 14:09:17', null, '1', '2018-03-06 14:10:32', null);
INSERT INTO `task_plan` VALUES ('27', '24', '1', '2018-03-06 14:09:17', null, '1', '2018-03-06 14:10:32', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_quality_record
-- ----------------------------
INSERT INTO `task_quality_record` VALUES ('1', '3', 'nnnn', '2', 'cmtttt', '2018-02-12 14:17:27');
INSERT INTO `task_quality_record` VALUES ('2', '4', 'nn222', '3', 'cmt2222', '2018-02-12 14:17:45');

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
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_record
-- ----------------------------
INSERT INTO `task_record` VALUES ('3', '安裝主传动', '1', '0', null, null, '0', null, null, '0000-00-00 00:00:00', null);
INSERT INTO `task_record` VALUES ('4', '喷油漆', '1', '0', null, null, '1', null, null, '0000-00-00 00:00:00', null);
INSERT INTO `task_record` VALUES ('5', '调试电脑', '2', '-3', null, null, '1', null, null, null, null);
INSERT INTO `task_record` VALUES ('6', '安装夹线器', '2', '-2', null, null, '3', null, null, null, null);
INSERT INTO `task_record` VALUES ('11', '调试电脑', '8', '-3', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('12', '安装夹线器', '8', '-2', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('19', '调试电脑', '9', '-3', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('20', '安装夹线器', '9', '-2', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('23', '调试电脑', '10', '-3', null, null, '1', null, null, null, null);
INSERT INTO `task_record` VALUES ('24', '安装夹线器', '10', '-2', null, null, '1', null, null, null, null);
INSERT INTO `task_record` VALUES ('25', '调试电脑', '11', '-3', null, null, '1', null, null, null, null);
INSERT INTO `task_record` VALUES ('26', '安装夹线器', '11', '-2', null, null, '2', null, null, null, null);
INSERT INTO `task_record` VALUES ('27', '调试电脑', '12', '-3', null, null, '2', null, null, null, null);
INSERT INTO `task_record` VALUES ('28', '安装夹线器', '12', '-2', null, null, '1', null, null, null, null);
INSERT INTO `task_record` VALUES ('29', '安裝主传动', '12', '-5', null, null, '1', null, null, null, null);
INSERT INTO `task_record` VALUES ('30', '调试电脑', '13', '-3', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('31', '安装夹线器', '13', '-2', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('32', '安裝主传动', '13', '-5', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('33', '喷油漆', '13', '-6', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('34', '调试电脑', '14', '-3', null, null, '3', null, null, null, null);
INSERT INTO `task_record` VALUES ('35', '安装夹线器', '14', '-2', null, null, '2', null, null, null, null);
INSERT INTO `task_record` VALUES ('36', '喷油漆', '14', '-5', null, null, '3', null, null, null, null);
INSERT INTO `task_record` VALUES ('37', '安裝主传动', '14', '-6', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('38', '安装电机', '14', '-7', null, null, '5', null, null, null, null);
INSERT INTO `task_record` VALUES ('39', '调试电脑', '15', '-3', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('40', '安装夹线器', '15', '-2', null, null, '3', null, null, null, null);
INSERT INTO `task_record` VALUES ('41', '安裝主传动', '15', '-5', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('42', '喷油漆', '15', '-6', null, null, '5', null, null, null, null);
INSERT INTO `task_record` VALUES ('43', '调试电脑', '16', '-3', null, null, '4', null, null, null, null);
INSERT INTO `task_record` VALUES ('44', '安装夹线器', '16', '-2', null, null, '2', null, null, null, null);
INSERT INTO `task_record` VALUES ('51', '调试电脑', '3', '-3', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('52', '安装夹线器', '3', '-2', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('53', '喷油漆', '17', '-3', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('54', '安装电机', '17', '-5', null, null, '0', null, null, null, null);

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
