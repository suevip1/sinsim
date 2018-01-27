/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50547
Source Host           : localhost:3306
Source Database       : sinsim_db

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2018-01-27 18:09:39
*/

SET FOREIGN_KEY_CHECKS=0;

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
