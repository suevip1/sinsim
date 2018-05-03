/*
Navicat MySQL Data Transfer

Source Server         : LHF
Source Server Version : 50547
Source Host           : localhost:3306
Source Database       : sinsim_db

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2018-05-03 19:26:54
*/

SET FOREIGN_KEY_CHECKS=0;

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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract
-- ----------------------------
INSERT INTO `contract` VALUES ('7', '曹443', '阿姆利泽HITESH', '曹建挺', '2018-05-30', 'DP', '', '美元', '1111', '1', '2018-04-27 09:00:53', '2018-04-27 09:01:20', null, '1');
INSERT INTO `contract` VALUES ('8', '骆951', '乌克兰POLLARDI', '骆晓军', '2018-05-04', '10000$定金，发货前30%定金，余款寄提单之前。 ', '外贸一部', '美元', '', '5', '2018-04-28 01:20:58', '2018-05-03 17:50:35', 'admin', '0');
INSERT INTO `contract` VALUES ('9', 'LE90A', '印度迪立普', '曹建挺', '2018-04-30', 'D/P', '外贸二部', '人民币', '', '2', '2018-04-28 01:56:45', '2018-04-28 05:40:29', null, '1');
INSERT INTO `contract` VALUES ('10', 'C441', '阿富汗AF001 ', '骆晓军', '2018-04-02', 'TT', '外贸一部', '美元', '', '1', '2018-04-28 02:26:11', '2018-04-28 02:34:31', 'admin', '1');
INSERT INTO `contract` VALUES ('11', 'XS-1801062', '绍兴立浙纺织有限公司', '郭洪勇', '2018-05-25', '租赁', '内贸部', '人民币', '付款：定金8万元，发货前16.6万元，租赁30万元。', '2', '2018-04-28 02:56:23', '2018-05-03 10:50:43', null, '1');
INSERT INTO `contract` VALUES ('12', 'con-0908', '乌克兰POLLARDI', '谢侃', '2018-05-18', 'sss', '', '人民币', '', '0', '2018-05-03 11:47:53', '2018-05-03 13:51:27', 'admin', '1');
INSERT INTO `contract` VALUES ('21', 'sdfasdfasd', '绍兴立浙纺织有限公司', '斯雯', '2018-05-31', 'sdfsdf', '外贸一部', '美元', 'sdfsdf', '0', '2018-05-03 14:54:44', '2018-05-03 19:22:26', '谢侃', '0');
