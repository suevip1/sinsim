/*
Navicat MySQL Data Transfer

Source Server         : MyDB
Source Server Version : 50547
Source Host           : localhost:3306
Source Database       : sinsim_db

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2018-07-25 15:49:00
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of abnormal_image
-- ----------------------------
INSERT INTO `abnormal_image` VALUES ('7', '7', '[/opt/sinsim/imagesSaved/abnormal/XS-1801111改_A5D110601611_Abnormal_2018-06-25-16-27-43_0.jpg]', '2018-06-25 16:27:17');

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
INSERT INTO `abnormal_record` VALUES ('7', '9', '77', '225', '线架缺料', '物料已到', '170', '2018-06-25 16:27:17', '2018-06-25 16:42:13');

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
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract
-- ----------------------------
INSERT INTO `contract` VALUES ('1', 'XS-1801109', '绍兴华文电脑刺绣有限公司', '公司', '2018-08-10', '定金35万，发货前付43万，租赁100万', '内贸部', '人民币', '', '2', '2018-06-01 09:30:25', '2018-06-06 15:24:58', '郑洁', '1');
INSERT INTO `contract` VALUES ('2', 'XS-1802054', '钟武杰', '孙情', '2018-07-04', '定金3万元，发货前21.1万元', '内贸部', '人民币', '', '2', '2018-06-02 11:07:28', '2018-06-13 15:45:19', '陈洁', '1');
INSERT INTO `contract` VALUES ('3', 'XS-1801047', '喜临门', '公司', '2018-06-26', '发货前6万', '内贸部', '人民币', '', '2', '2018-06-04 10:21:40', '2018-06-14 11:06:27', '郑洁', '1');
INSERT INTO `contract` VALUES ('4', '骆1033', '巴基斯坦Zeeshan', '骆晓军', '2018-07-20', '20%定金， 余款D/P, T/T', '外贸一部', '人民币', '', '2', '2018-06-04 11:00:20', '2018-06-14 11:08:33', '骆晓军', '1');
INSERT INTO `contract` VALUES ('5', '骆1033', '巴基斯坦Zeeshan', '骆晓军', '2018-07-20', '20%定金， 余款DP,TT', '外贸一部', '人民币', '', '0', '2018-06-04 11:26:42', null, '骆晓军', '1');
INSERT INTO `contract` VALUES ('6', '骆1033B', '巴基斯坦Zeeshan', '骆晓军', '2018-07-20', '20%定金， 余款DP,TT', '外贸一部', '人民币', '', '2', '2018-06-04 11:26:42', '2018-06-14 11:08:06', '骆晓军', '1');
INSERT INTO `contract` VALUES ('7', '曹454', 'COIN010', '曹建挺', '2018-07-10', '定金', '外贸二部', '美元', '', '2', '2018-06-05 09:09:22', '2018-06-09 12:42:35', '周婷青', '1');
INSERT INTO `contract` VALUES ('8', 'XS-1801114', '海城市御美健保健用品有限公司', '公司', '2018-07-02', '定金6万元，发货前24万元', '内贸部', '人民币', '', '2', '2018-06-05 13:25:46', '2018-06-09 12:43:56', '陈洁', '1');
INSERT INTO `contract` VALUES ('9', 'XS-1801113', '信泰（福建）科技有限公司', '吴捷桁', '2018-07-30', '定金30万承兑，发货验收后平安租赁付清全款', '内贸部', '人民币', '吴捷桁居间费暂定', '2', '2018-06-05 14:00:25', '2018-06-21 13:44:06', '郑洁', '1');
INSERT INTO `contract` VALUES ('10', 'XS-1801119', '冯靖', '公司', '2018-07-04', '定金2万，发货前8万', '内贸部', '人民币', '', '2', '2018-06-05 14:56:17', '2018-06-09 12:40:11', '郑洁', '1');
INSERT INTO `contract` VALUES ('11', 'XS-1801118', '绍兴夏叶绣品有限公司', '周立锋', '2018-07-16', '定金10万，发货前21.4万，租赁45万', '内贸部', '人民币', '', '2', '2018-06-06 14:53:39', '2018-06-09 12:35:12', '郑洁', '1');
INSERT INTO `contract` VALUES ('12', 'XS-1801122', '汝州市安思纺织品有限公司', '公司', '2018-07-25', '定金20万，发货前29万，租赁110万', '内贸部', '人民币', '', '2', '2018-06-07 14:30:39', '2018-06-09 12:32:48', '王铁锋', '1');
INSERT INTO `contract` VALUES ('13', 'XS-1801121', '杭州星驰刺绣厂', '公司', '2018-06-28', '定金3万，发货后一周内付14.2万', '内贸部', '人民币', '', '2', '2018-06-08 08:38:32', '2018-06-09 12:29:49', '郑洁', '1');
INSERT INTO `contract` VALUES ('14', 'XS-1802056', '卓奕杰', '孙情', '2018-08-01', '定金10万元，发货前21.5万元，按揭31.5万元', '内贸部', '人民币', '佣金3万元/台。', '2', '2018-06-08 13:14:48', '2018-06-13 15:44:39', '郑洁', '1');
INSERT INTO `contract` VALUES ('16', 'XS-1802057', '汕头市信扬智能缝制设备有限公司', '孙情', '2018-07-03', '定金2万元，发货前5.6万元', '内贸部', '人民币', '大梁左边打孔，加宽线架台板', '2', '2018-06-09 08:34:01', '2018-06-21 13:42:28', '郑洁', '1');
INSERT INTO `contract` VALUES ('17', 'XS-1801120', '海门市奇艺床上用品厂', '高耀叶', '2018-07-18', '定金3万元，发货前1.7万元，租赁11万元', '内贸部', '人民币', '', '2', '2018-06-09 08:44:01', '2018-06-16 14:15:42', '陈洁', '1');
INSERT INTO `contract` VALUES ('18', '骆1045', '巴基斯坦Zeeshan', '骆晓军', '2018-06-30', 'T/T', '外贸一部', '人民币', '', '2', '2018-06-09 09:22:21', '2018-06-16 14:18:22', '骆晓军', '1');
INSERT INTO `contract` VALUES ('19', '骆1043', '巴基斯坦Zeeshan', '骆晓军', '2018-07-20', '20%定金， 余款TT, DP', '外贸一部', '人民币', '', '2', '2018-06-09 10:43:55', '2018-06-16 14:17:42', '姚娟芝', '1');
INSERT INTO `contract` VALUES ('20', '骆1044', '巴基斯坦Zeeshan', '骆晓军', '2018-07-01', '20%定金， 余款T/T, DP', '外贸一部', '人民币', '', '2', '2018-06-09 11:06:06', '2018-06-16 14:18:06', '姚娟芝', '1');
INSERT INTO `contract` VALUES ('21', '曹456', '阿姆利则HITESH', '曹建挺', '2018-07-10', 'DP', '外贸二部', '美元', '曹456A/B/C 定金12750美金，余款D/P.', '2', '2018-06-09 14:59:21', '2018-06-16 14:15:02', '周婷青', '1');
INSERT INTO `contract` VALUES ('22', 'XS-1801116', '绍兴日杰纺织有限公司', '公司', '2018-08-03', '定金10万，发货前23.5万，剩余33.5万分11个月付清，前10个月每个月支付3万，最后一个月付清剩余货款3.5万（均承兑支付）', '内贸部', '人民币', '横梁打孔，针杆架左边打绳绣孔，右边打金片孔，每台机器送一个报警板和底检条，预留自动换底线孔位', '2', '2018-06-09 16:48:33', '2018-06-21 13:40:46', '郑洁', '1');
INSERT INTO `contract` VALUES ('24', 'XS-1801123', '绍兴柯桥同美绣品有限公司', '公司', '2018-08-06', '定金10万元，发货前14万元，租赁50万元', '内贸部', '人民币', '', '2', '2018-06-11 11:33:41', '2018-06-20 12:24:48', '陈徐彬', '1');
INSERT INTO `contract` VALUES ('55', '迪133ABCDE', 'COIN001--迪', '曹建挺', '2018-07-11', 'DP', '外贸二部', '人民币', 'LE107-108-109-110-111-112', '2', '2018-06-11 16:21:31', '2018-06-20 12:50:59', '周婷青', '1');
INSERT INTO `contract` VALUES ('56', '曹455', '银海函飞', '曹建挺', '2018-07-10', '款到发货', '外贸二部', '美元', '押金28260人民币，款到发货', '2', '2018-06-11 16:37:47', '2018-06-13 16:28:13', '斯雯', '1');
INSERT INTO `contract` VALUES ('59', 'D133FG', 'COIN001--迪', '曹建挺', '2018-07-08', 'DP', '外贸二部', '人民币', 'LE113-114', '2', '2018-06-11 17:52:36', '2018-06-21 13:49:13', '周婷青', '1');
INSERT INTO `contract` VALUES ('66', 'XS-1801111改', '绍兴茂楠绣品有限公司', '蔡天明', '2018-06-28', '无定金，发货前支付8.2万', '内贸部', '人民币', '无销售费，由蔡天明保修', '2', '2018-06-12 11:21:17', '2018-06-14 11:06:01', '郑洁', '1');
INSERT INTO `contract` VALUES ('67', 'XS-1801115', '海宁市卡迷丹布业有限公司', '刘木青', '2018-07-23', '定金6万，发货前18万，剩余10万分两次付清，于2018年10月25日前支付5万，2019年1月25日前支付5万', '内贸部', '人民币', '居间费为机器款减去运费的4%', '2', '2018-06-12 14:39:23', '2018-06-14 11:04:37', '郑洁', '1');
INSERT INTO `contract` VALUES ('68', '曹134ABC', 'COIN001--迪', '曹建挺', '2018-07-15', 'DP', '外贸二部', '人民币', 'LE115-116-117AB', '2', '2018-06-13 14:24:05', '2018-06-20 12:34:05', '周婷青', '1');
INSERT INTO `contract` VALUES ('70', '曹457', '阿姆利则HITESH', '曹建挺', '2018-06-16', '定金13200美元，余款DP', '外贸二部', '美元', '', '2', '2018-06-16 09:13:54', '2018-06-21 13:48:46', '斯雯', '1');
INSERT INTO `contract` VALUES ('71', 'XS-1801128', '绍兴柯桥意腾工艺花边有限公司', '公司', '2018-06-17', '定金5万，发货后一周内付115万，两个月内付20万', '内贸部', '人民币', '', '0', '2018-06-16 16:01:40', '2018-06-20 11:06:42', '郑洁', '1');
INSERT INTO `contract` VALUES ('72', 'XS-1801127', '杭州欢方纺织绣品有限公司', '冯保锋', '2018-08-06', '定金10万，发货前20.9万，租赁43万', '内贸部', '人民币', '无销售费，由冯保锋保修', '2', '2018-06-19 10:49:45', '2018-06-21 15:14:46', '郑洁', '1');
INSERT INTO `contract` VALUES ('73', 'XS-1802058', '汕头市信扬智能缝制设备有限公司', '孙情', '2018-08-15', '定金10万元，发货前30.5万元，剩余16.5万元于2018年9月-2019年8月每月25日之前支付13750元', '内贸部', '人民币', '', '1', '2018-06-19 15:10:36', '2018-06-25 13:47:55', '陈洁', '1');
INSERT INTO `contract` VALUES ('74', 'XS-1802059', '汕头市信扬智能缝制设备有限公司', '孙情', '2018-08-06', '定金6万元，发货前34.5万元，剩余16.5万元于2018年9月-2019年8月每月支付13750元。', '内贸部', '人民币', '', '1', '2018-06-19 15:44:42', '2018-06-25 13:47:47', '陈洁', '1');
INSERT INTO `contract` VALUES ('75', 'XS-1802060', '汕头市信扬智能缝制设备有限公司', '孙情', '2018-07-24', '定金8万元，发货前24.03万元，剩余13.8万元于2018年8月-2019年7月每月支付11500元。', '内贸部', '人民币', '', '2', '2018-06-19 15:57:53', '2018-06-21 15:16:42', '陈洁', '1');
INSERT INTO `contract` VALUES ('76', '曹459', '智利HD', '曹建挺', '2018-07-20', 'TT', '外贸二部', '美元', '', '1', '2018-06-20 16:55:09', '2018-06-23 16:14:12', '周婷青', '1');
INSERT INTO `contract` VALUES ('77', 'XS-1801129', '绍兴柯桥同美绣品有限公司', '公司', '2018-08-10', '定金10万，发货前9.78万，租赁40万', '内贸部', '人民币', '', '1', '2018-06-22 13:32:17', '2018-06-25 16:51:02', '郑洁', '1');
INSERT INTO `contract` VALUES ('78', 'XS-1803027', '东莞简钰绣纺织有限公司', '何绍华', '2018-08-22', '定金10万元，发货前15.5万元，剩余12万元于2018年9月-2019年2月每月25日前支付2万元', '内贸部', '人民币', '一套双框架费用2000元已在何绍华佣金中扣除', '1', '2018-06-23 08:52:36', '2018-06-25 16:41:03', '陈洁', '1');
INSERT INTO `contract` VALUES ('79', '曹460', '孟加拉JANN GROUP', '曹建挺', '2018-07-31', '信用证', '外贸二部', '美元', '孟加拉新客', '1', '2018-06-23 16:29:55', '2018-06-23 16:57:58', '周婷青', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract_sign
-- ----------------------------
INSERT INTO `contract_sign` VALUES ('1', '1', '[]', '', '2018-06-01 09:30:25', null);
INSERT INTO `contract_sign` VALUES ('2', '2', '[]', '', '2018-06-02 11:07:28', null);
INSERT INTO `contract_sign` VALUES ('3', '3', '[]', '', '2018-06-04 10:21:40', null);
INSERT INTO `contract_sign` VALUES ('4', '4', '[]', '', '2018-06-04 11:00:20', null);
INSERT INTO `contract_sign` VALUES ('5', '5', '[]', '', '2018-06-04 11:26:42', null);
INSERT INTO `contract_sign` VALUES ('6', '6', '[]', '', '2018-06-04 11:26:42', null);
INSERT INTO `contract_sign` VALUES ('7', '7', '[]', '', '2018-06-05 09:09:22', null);
INSERT INTO `contract_sign` VALUES ('8', '8', '[]', '', '2018-06-05 13:25:46', null);
INSERT INTO `contract_sign` VALUES ('9', '9', '[]', '', '2018-06-05 14:00:25', null);
INSERT INTO `contract_sign` VALUES ('10', '10', '[]', '', '2018-06-05 14:56:17', null);
INSERT INTO `contract_sign` VALUES ('11', '11', '[]', '', '2018-06-06 14:53:39', null);
INSERT INTO `contract_sign` VALUES ('12', '12', '[]', '', '2018-06-07 14:30:39', null);
INSERT INTO `contract_sign` VALUES ('13', '13', '[]', '', '2018-06-08 08:38:32', null);
INSERT INTO `contract_sign` VALUES ('14', '14', '[]', '', '2018-06-08 13:14:48', null);
INSERT INTO `contract_sign` VALUES ('16', '16', '[]', '', '2018-06-09 08:34:01', null);
INSERT INTO `contract_sign` VALUES ('17', '17', '[]', '', '2018-06-09 08:44:01', null);
INSERT INTO `contract_sign` VALUES ('18', '18', '[]', '', '2018-06-09 09:22:21', null);
INSERT INTO `contract_sign` VALUES ('19', '19', '[]', '', '2018-06-09 10:43:55', null);
INSERT INTO `contract_sign` VALUES ('20', '20', '[]', '', '2018-06-09 11:06:06', null);
INSERT INTO `contract_sign` VALUES ('21', '21', '[]', '', '2018-06-09 14:59:21', null);
INSERT INTO `contract_sign` VALUES ('22', '22', '[]', '', '2018-06-09 16:48:33', null);
INSERT INTO `contract_sign` VALUES ('24', '24', '[]', '', '2018-06-11 11:33:41', null);
INSERT INTO `contract_sign` VALUES ('55', '55', '[]', '', '2018-06-11 16:21:31', null);
INSERT INTO `contract_sign` VALUES ('56', '56', '[]', '', '2018-06-11 16:37:47', null);
INSERT INTO `contract_sign` VALUES ('59', '59', '[]', '', '2018-06-11 17:52:36', null);
INSERT INTO `contract_sign` VALUES ('66', '66', '[]', '', '2018-06-12 11:21:17', null);
INSERT INTO `contract_sign` VALUES ('67', '67', '[]', '', '2018-06-12 14:39:23', null);
INSERT INTO `contract_sign` VALUES ('68', '68', '[]', '', '2018-06-13 14:24:05', null);
INSERT INTO `contract_sign` VALUES ('69', '21', '[]', '', '2018-06-14 13:50:11', null);
INSERT INTO `contract_sign` VALUES ('70', '21', '[]', '', '2018-06-14 13:53:47', null);
INSERT INTO `contract_sign` VALUES ('71', '21', '[]', '', '2018-06-14 13:54:59', null);
INSERT INTO `contract_sign` VALUES ('73', '70', '[]', '', '2018-06-16 09:13:54', null);
INSERT INTO `contract_sign` VALUES ('74', '71', '[]', '', '2018-06-16 16:01:40', null);
INSERT INTO `contract_sign` VALUES ('75', '72', '[]', '', '2018-06-19 10:49:45', null);
INSERT INTO `contract_sign` VALUES ('76', '73', '[]', '', '2018-06-19 15:10:36', null);
INSERT INTO `contract_sign` VALUES ('77', '74', '[]', '', '2018-06-19 15:44:42', null);
INSERT INTO `contract_sign` VALUES ('78', '75', '[]', '', '2018-06-19 15:57:53', null);
INSERT INTO `contract_sign` VALUES ('79', '76', '[]', '', '2018-06-20 16:55:09', null);
INSERT INTO `contract_sign` VALUES ('80', '16', '[]', '', '2018-06-21 08:52:40', null);
INSERT INTO `contract_sign` VALUES ('81', '22', '[]', '', '2018-06-21 09:32:43', null);
INSERT INTO `contract_sign` VALUES ('82', '77', '[]', '', '2018-06-22 13:32:17', null);
INSERT INTO `contract_sign` VALUES ('83', '78', '[]', '', '2018-06-23 08:52:36', null);
INSERT INTO `contract_sign` VALUES ('84', '79', '[]', '', '2018-06-23 16:29:55', null);

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
-- Table structure for `install_group`
-- ----------------------------
DROP TABLE IF EXISTS `install_group`;
CREATE TABLE `install_group` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) NOT NULL COMMENT '公司部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

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
  PRIMARY KEY (`id`),
  KEY `idx_m_order_id` (`order_id`) USING BTREE,
  KEY `fk_m_machine_type` (`machine_type`),
  CONSTRAINT `fk_m_machine_type` FOREIGN KEY (`machine_type`) REFERENCES `machine_type` (`id`),
  CONSTRAINT `fk_m_order_id` FOREIGN KEY (`order_id`) REFERENCES `machine_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine
-- ----------------------------
INSERT INTO `machine` VALUES ('1', '2', 'A55152415951', '1806004', null, '0', '2', '2018-06-06 15:24:16', null, null, null);
INSERT INTO `machine` VALUES ('2', '2', 'A55152415412', '1806003', null, '0', '2', '2018-06-06 15:24:16', null, null, null);
INSERT INTO `machine` VALUES ('3', '1', 'A55152458381', '1806001', null, '0', '2', '2018-06-06 15:24:58', null, null, null);
INSERT INTO `machine` VALUES ('4', '1', 'A55152458962', '1806002', null, '0', '2', '2018-06-06 15:24:58', null, null, null);
INSERT INTO `machine` VALUES ('5', '17', 'A58122949691', '1806023', null, '0', '2', '2018-06-09 12:29:49', null, null, null);
INSERT INTO `machine` VALUES ('6', '16', 'A58123248081', '1806026', null, '0', '1', '2018-06-09 12:32:48', null, null, null);
INSERT INTO `machine` VALUES ('7', '16', 'A58123248412', '1806025', null, '0', '1', '2018-06-09 12:32:48', null, null, null);
INSERT INTO `machine` VALUES ('8', '16', 'A58123248713', '1806024', null, '0', '1', '2018-06-09 12:32:48', null, null, null);
INSERT INTO `machine` VALUES ('9', '14', 'A58123430691', '1806020', null, '0', '2', '2018-06-09 12:34:30', null, null, null);
INSERT INTO `machine` VALUES ('10', '14', 'A58123430622', '1806019', null, '0', '2', '2018-06-09 12:34:30', null, null, null);
INSERT INTO `machine` VALUES ('11', '15', 'A58123511111', '1806022', null, '0', '2', '2018-06-09 12:35:12', null, null, null);
INSERT INTO `machine` VALUES ('12', '15', 'A58123511352', '1806021', null, '0', '2', '2018-06-09 12:35:12', null, null, null);
INSERT INTO `machine` VALUES ('13', '13', 'A58123914581', '1806015', null, '0', '2', '2018-06-09 12:39:15', null, null, null);
INSERT INTO `machine` VALUES ('14', '12', 'A58124011581', '1806016', null, '0', '2', '2018-06-09 12:40:11', null, null, null);
INSERT INTO `machine` VALUES ('15', '9', 'A58124235171', '1806014', null, '0', '12', '2018-06-09 12:42:35', null, null, null);
INSERT INTO `machine` VALUES ('16', '10', 'A58124356511', '1806018', null, '0', '2', '2018-06-09 12:43:56', null, null, null);
INSERT INTO `machine` VALUES ('17', '10', 'A58124356272', '1806017', null, '0', '2', '2018-06-09 12:43:56', null, null, null);
INSERT INTO `machine` VALUES ('21', '61', 'A5C154336911', '1806031', null, '0', '2', '2018-06-13 15:43:36', '2018-06-21 08:52:40', null, null);
INSERT INTO `machine` VALUES ('22', '18', 'A5C154438611', '1806030', null, '0', '3', '2018-06-13 15:44:39', null, null, null);
INSERT INTO `machine` VALUES ('23', '18', 'A5C154438442', '1806029', null, '0', '3', '2018-06-13 15:44:39', null, null, null);
INSERT INTO `machine` VALUES ('24', '3', 'A5C154519171', '1806028', null, '0', '2', '2018-06-13 15:45:19', null, null, null);
INSERT INTO `machine` VALUES ('25', '32', 'A5C162812251', '1806027', null, '0', '2', '2018-06-13 16:28:13', null, null, null);
INSERT INTO `machine` VALUES ('26', '50', 'A5C172746481', '1806034', null, '0', '2', '2018-06-13 17:27:46', '2018-06-14 13:54:59', null, null);
INSERT INTO `machine` VALUES ('27', '46', 'A5C173209721', '1806033', null, '0', '2', '2018-06-13 17:32:10', '2018-06-14 13:53:47', null, null);
INSERT INTO `machine` VALUES ('28', '44', 'A5C173224751', '1806032', null, '0', '2', '2018-06-13 17:32:25', '2018-06-14 13:50:11', null, null);
INSERT INTO `machine` VALUES ('29', '40', 'A5D110436161', '1806038', null, '0', '2', '2018-06-14 11:04:36', null, null, null);
INSERT INTO `machine` VALUES ('30', '40', 'A5D110436712', '1806037', null, '0', '2', '2018-06-14 11:04:37', null, null, null);
INSERT INTO `machine` VALUES ('31', '39', 'A5D110601611', '1806036', 'D1', '3', '11', '2018-06-14 11:06:01', '2018-06-23 08:00:44', null, null);
INSERT INTO `machine` VALUES ('32', '4', 'A5D110626361', '1806035', null, '0', '2', '2018-06-14 11:06:27', null, null, null);
INSERT INTO `machine` VALUES ('33', '8', 'A5D110805721', '1806041', null, '0', '14', '2018-06-14 11:08:06', null, null, null);
INSERT INTO `machine` VALUES ('34', '8', 'A5D110805722', '1806040', null, '0', '14', '2018-06-14 11:08:06', null, null, null);
INSERT INTO `machine` VALUES ('35', '5', 'A5D110832431', '1806039', null, '0', '12', '2018-06-14 11:08:33', null, null, null);
INSERT INTO `machine` VALUES ('36', '21', 'A5F141541601', '1806042', null, '0', '2', '2018-06-16 14:15:42', null, null, null);
INSERT INTO `machine` VALUES ('37', '24', 'A5F141707671', '1806044', null, '0', '2', '2018-06-16 14:17:07', null, null, null);
INSERT INTO `machine` VALUES ('38', '23', 'A5F141741711', '1806043', null, '0', '2', '2018-06-16 14:17:42', null, null, null);
INSERT INTO `machine` VALUES ('39', '25', 'A5F141805541', null, null, '0', '6', '2018-06-16 14:18:06', null, null, null);
INSERT INTO `machine` VALUES ('40', '25', 'A5F141805522', null, null, '0', '6', '2018-06-16 14:18:06', null, null, null);
INSERT INTO `machine` VALUES ('41', '25', 'A5F141805853', null, null, '0', '6', '2018-06-16 14:18:06', null, null, null);
INSERT INTO `machine` VALUES ('42', '22', 'A5F141822081', '1806045', null, '0', '2', '2018-06-16 14:18:22', null, null, null);
INSERT INTO `machine` VALUES ('43', '30', 'A5J122448761', '1806067', null, '0', '2', '2018-06-20 12:24:48', null, null, null);
INSERT INTO `machine` VALUES ('44', '30', 'A5J122448532', '1806066', null, '0', '2', '2018-06-20 12:24:48', null, null, null);
INSERT INTO `machine` VALUES ('45', '62', 'A5J122621771', '1806068', null, '0', '2', '2018-06-20 12:26:22', '2018-06-21 09:32:43', null, null);
INSERT INTO `machine` VALUES ('46', '62', 'A5J122621542', '1806069', null, '0', '2', '2018-06-20 12:26:22', '2018-06-21 09:32:43', null, null);
INSERT INTO `machine` VALUES ('47', '42', 'A5J122842311', '1806049', null, '0', '2', '2018-06-20 12:28:43', null, null, null);
INSERT INTO `machine` VALUES ('48', '42', 'A5J122842162', '1806050', null, '0', '2', '2018-06-20 12:28:43', null, null, null);
INSERT INTO `machine` VALUES ('49', '41', 'A5J123315771', '1806046', null, '0', '2', '2018-06-20 12:33:15', null, null, null);
INSERT INTO `machine` VALUES ('50', '41', 'A5J123315892', '1806047', null, '0', '2', '2018-06-20 12:33:15', null, null, null);
INSERT INTO `machine` VALUES ('51', '41', 'A5J123315813', '1806048', null, '0', '2', '2018-06-20 12:33:15', null, null, null);
INSERT INTO `machine` VALUES ('52', '43', 'A5J123404111', '1806051', null, '0', '2', '2018-06-20 12:34:05', null, null, null);
INSERT INTO `machine` VALUES ('53', '38', 'A5J124546661', '1806064', null, '0', '2', '2018-06-20 12:45:47', null, null, null);
INSERT INTO `machine` VALUES ('54', '38', 'A5J124546002', '1806065', null, '0', '2', '2018-06-20 12:45:47', null, null, null);
INSERT INTO `machine` VALUES ('55', '34', 'A5J124721141', '1806056', null, '0', '3', '2018-06-20 12:47:22', null, null, null);
INSERT INTO `machine` VALUES ('56', '34', 'A5J124721072', '1806057', null, '0', '3', '2018-06-20 12:47:22', null, null, null);
INSERT INTO `machine` VALUES ('57', '34', 'A5J124721383', '1806058', null, '0', '3', '2018-06-20 12:47:22', null, null, null);
INSERT INTO `machine` VALUES ('58', '34', 'A5J124721954', '1806059', null, '0', '3', '2018-06-20 12:47:22', null, null, null);
INSERT INTO `machine` VALUES ('59', '36', 'A5J124813901', '1806062', null, '0', '2', '2018-06-20 12:48:13', null, null, null);
INSERT INTO `machine` VALUES ('60', '36', 'A5J124813052', '1806063', null, '0', '2', '2018-06-20 12:48:13', null, null, null);
INSERT INTO `machine` VALUES ('61', '35', 'A5J124924011', '1806060', null, '0', '2', '2018-06-20 12:49:25', null, null, null);
INSERT INTO `machine` VALUES ('62', '35', 'A5J124924022', '1806061', null, '0', '2', '2018-06-20 12:49:25', null, null, null);
INSERT INTO `machine` VALUES ('63', '31', 'A5J125038111', '1806054', null, '0', '2', '2018-06-20 12:50:38', null, null, null);
INSERT INTO `machine` VALUES ('64', '31', 'A5J125038202', '1806055', null, '0', '2', '2018-06-20 12:50:38', null, null, null);
INSERT INTO `machine` VALUES ('65', '33', 'A5J125058701', '1806052', null, '0', '2', '2018-06-20 12:50:59', null, null, null);
INSERT INTO `machine` VALUES ('66', '33', 'A5J125058042', '1806053', null, '0', '2', '2018-06-20 12:50:59', null, null, null);
INSERT INTO `machine` VALUES ('67', '11', 'A5K134405041', '1806079', null, '0', '11', '2018-06-21 13:44:06', null, null, null);
INSERT INTO `machine` VALUES ('68', '11', 'A5K134405412', '1806080', null, '0', '11', '2018-06-21 13:44:06', null, null, null);
INSERT INTO `machine` VALUES ('69', '11', 'A5K134405073', '1806081', null, '0', '11', '2018-06-21 13:44:06', null, null, null);
INSERT INTO `machine` VALUES ('70', '11', 'A5K134405724', '1806082', null, '0', '11', '2018-06-21 13:44:06', null, null, null);
INSERT INTO `machine` VALUES ('71', '51', 'A5K134830211', '1806084', null, '0', '4', '2018-06-21 13:48:30', null, null, null);
INSERT INTO `machine` VALUES ('72', '52', 'A5K134845031', '1806085', null, '0', '4', '2018-06-21 13:48:46', null, null, null);
INSERT INTO `machine` VALUES ('73', '37', 'A5K134913351', '1806083', null, '0', '2', '2018-06-21 13:49:13', null, null, null);
INSERT INTO `machine` VALUES ('74', '54', 'A5K151446281', '1806077', null, '0', '2', '2018-06-21 15:14:46', null, null, null);
INSERT INTO `machine` VALUES ('75', '54', 'A5K151446332', '1806078', null, '0', '2', '2018-06-21 15:14:46', null, null, null);
INSERT INTO `machine` VALUES ('76', '59', 'A5K151557071', '1806076', null, '0', '2', '2018-06-21 15:15:57', null, null, null);
INSERT INTO `machine` VALUES ('77', '58', 'A5K151619741', '1806075', null, '0', '2', '2018-06-21 15:16:19', null, null, null);
INSERT INTO `machine` VALUES ('78', '57', 'A5K151642711', '1806074', null, '0', '2', '2018-06-21 15:16:42', null, null, null);

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
  `wrap_machine` varchar(255) NOT NULL COMMENT '绕线机配置',
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
  PRIMARY KEY (`id`),
  KEY `fk_o_machine_type` (`machine_type`),
  KEY `fk_o_order_detail_id` (`order_detail_id`),
  KEY `fk_o_contract_id` (`contract_id`),
  CONSTRAINT `fk_o_contract_id` FOREIGN KEY (`contract_id`) REFERENCES `contract` (`id`),
  CONSTRAINT `fk_o_machine_type` FOREIGN KEY (`machine_type`) REFERENCES `machine_type` (`id`),
  CONSTRAINT `fk_o_order_detail_id` FOREIGN KEY (`order_detail_id`) REFERENCES `order_detail` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of machine_order
-- ----------------------------
INSERT INTO `machine_order` VALUES ('1', 'XS-1801109A', '0', '1', '1', '183', '2', '中国', 'SINSIM', '2', '2', '4', '88+2', '165', '800', '1600', '单机', '', null, '', '[{\"name\":\"单金片\",\"number\":90,\"price\":\"650\"}]', '350500', '0', '0', '0', '2018-08-10', '2018-08-10', '', '公司', '1', 'SinSim保修', '保修员S', '2018-06-01 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('2', 'XS-1801109B', '0', '1', '2', '183', '2', '中国', 'SINSIM', '2', '2', '6', '88+2', '165', '800', '1600', '单机', '', null, '', '[{\"name\":\"单针双片\",\"number\":90,\"price\":\"1350\"}]', '359500', '0', '0', '0', '2018-08-10', '2018-08-10', '', '公司', '1', 'SinSim保修', '保修员S', '2018-06-01 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('3', 'XS-1802054', '0', '2', '3', '188', '2', '中国', 'SINSIM电脑绣花机', '1', '2', '8', '60', '165', '700', '1540', '单机', '', null, '', '[]', '241000', '0', '0', '0', '2018-06-30', '2018-07-10', '大梁左边打孔，加宽线架台板', '孙情', '1', '代理商保修', '保修员D', '2018-06-02 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('4', 'XS-1801047', '0', '3', '4', '183', '2', '中国', 'SINSIM电脑绣花机', '1', '2', '6', '2', '600', '650', '900', '单机', '', null, '', '[]', '60000', '0', '0', '0', '2018-06-26', '2018-07-08', '', '公司', '1', 'SinSim保修', '保修员S', '2018-06-04 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('5', '骆1033A', '0', '4', '5', '179', '2', '巴基斯坦', 'GREAT-SINSIM', '1', '12', '9', '24', '450', '600', '1350', '单机', '', null, '', '[]', '246400', '0', '0', '0', '2018-07-20', '2018-06-07', '1-Y 驱动后方需有两个抱箍轴套；\n2-每台机器另外配送：针杆驱动器3个， 传动块4个， 压脚驱动组件4个， 开口压脚凸轮4个， 开口挑线凸轮1个， 压脚连杆4个， 挑线杆A-3个， KD102板2块， 跳跃电机1只， Y 驱动皮带1根， 64齿尼龙齿轮4个。 \n3-每台机器另配一台稳压器， 报警板3块， 底检条3根。 \n4-旋梭处加装小灯， \n5-30个金线防止缠绕塑料帽。 \n6-使用温州乐清毛巾机头。 ', '骆晓军', '1', 'SinSim保修', '保修员S', '2018-06-04 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('7', '骆1033B', null, '5', '7', '179', '0', '巴基斯坦', 'GREAT-SINSIM', '2', '14', '0', '24', '450', '600', '1350', '单机', '', null, null, '[]', '193000', '0', '0', '0', '2018-07-20', '2018-07-20', '1-Y驱动后方有两个抱箍轴套\n2-每台另外配送64齿尼龙齿轮\n3-每台另外配送一台稳压器， \n4-H轴和松线电机使用双电机\n5-使用温州乐清机头。 ', '骆晓军', '0', 'SinSim保修', '保修员S', '2018-06-04 00:00:00', '2018-06-04 11:26:55', null);
INSERT INTO `machine_order` VALUES ('8', '骆1033B', '0', '6', '8', '179', '2', '巴基斯坦', 'GREAT-SINSIM', '2', '14', '0', '24', '450', '600', '1350', '单机', '', null, '', '[]', '193000', '0', '0', '0', '2018-07-20', '2018-07-20', '1-Y驱动后方有两个抱箍轴套\n2-每台另外配送64齿尼龙齿轮4个。 \n3-每台另外配送一台稳压器， \n4-H轴和松线电机使用双电机。\n5-使用温州乐清机头。 ', '骆晓军', '1', 'SinSim保修', '保修员S', '2018-06-04 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('9', '曹454A', '0', '7', '9', '174', '2', '印度', 'SHENG', '1', '12', '915+615', '15', '500', '混合刺绣范围850', '800+', '叠机', '', null, '1-跟454B\n\n', '[{\"name\":\"单针双片\",\"number\":15,\"price\":\"200\"},{\"name\":\"单针双片\",\"number\":15,\"price\":\"200\"},{\"name\":\"铝合金框架花架\",\"number\":1,\"price\":\"460\"}]', '28740', '0', '0', '0', '2018-07-10', '2018-07-19', '1-------另配平绣机头配件：报警板、底检条、针杆驱动器、压脚驱动组件、开口压脚凸轮、各2个。 毛巾绣剪线箱感应器5个。 \n2------另配1.4针嘴，18# 毛巾机针，2mm 针板， 同机头数。   框架上打塑料花架孔， 横档及后档打连接条的孔。\n3. 每台机器 金片装置过片槽3/4/5/7 /9-        --标配一套，再额外多配1套 \n4-----花架规格，曹454B花架清单：东阳煦盛花架:\n塑框式绷架：\n每个尺寸30个圆形内圈：90/120 /150 /180 /210\n内外撑圆：每个尺寸15个：90/120/150/180/210\n单固定架：15个\n方形磁铁框架每个尺寸：30个，460*254，536*356，680*340                 ', '曹建挺', '1', '代理商保修', '保修员d', '2018-06-05 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('10', 'XS-1801114', '0', '8', '33', '188', '2', '中国', 'SINSIM电脑绣花机', '2', '2', '9', '24', '400', '600', '800', '单机', '', null, '大梁左右打孔，大梁300*500，加宽线架台板，机器宽度不超过2.2米，绕线机不配（传动块，开口凸轮，跳跃电磁铁多配一倍)', '[]', '150000', '0', '0', '0', '2018-07-02', '2018-07-02', '销售为公司，实际保修为客户自理', '王铁锋', '1', 'SinSim保修', '保修员S', '2018-06-05 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('11', 'XS-1801113', '0', '9', '34', '183', '2', '中国', 'SINSIM', '4', '11', '6', '18', '510', '850', '850', '单机', '', null, '', '[{\"name\":\"简易绳绣\",\"number\":18,\"price\":\"900\"}]', '228800', '0', '0', '0', '2018-07-30', '2018-07-30', '', '吴捷桁', '1', '代理商保修', '保修员D', '2018-06-05 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('12', 'XS-1801119', '0', '10', '35', '183', '2', '中国', 'SINSIM', '1', '2', '9', '18', '275', '600', '700', '单机', '', null, '', '[]', '100000', '0', '0', '0', '2018-07-04', '2018-07-04', '客户自提，整机配件由公司保修12个月', '公司', '1', 'SinSim保修', '保修员S', '2018-06-05 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('13', '曹454B', '0', '7', '36', '174', '2', '印度', 'SHENG', '1', '2', '9', '18', '400', '600+', '700+', '叠机', '', null, '1-跟454A叠装出货， \n\n\n', '[{\"name\":\"单针双片\",\"number\":18,\"price\":\"200\"},{\"name\":\"单针双片\",\"number\":18,\"price\":\"200\"},{\"name\":\"铝合金框架花架\",\"number\":1,\"price\":\"550\"}]', '17000', '0', '0', '0', '2018-07-11', '2018-07-19', '1.装花架处用上防滑螺丝套（每个头一个）。前后档都需每头打一个孔（ 横档也打孔），配大小磁铁标准+1每台。\n         2.防尘板处加嵌木钉（防滑螺丝座）,,客人要求用新框架，注意花架配件   \n         3. 2每台机器额外配开口压脚凸轮，开口挑线凸轮，针杆驱动器，报警板，底检条，开关灯等各3个  \n         4. 每台机器 金片装置过片槽3/4/5/7 /9-        --标配一套，再额外多配1套 \n5-花架规格，曹454B花架清单：东阳煦盛花架:\n塑框式绷架：\n每个尺寸36个圆形内圈：90/120 /150 /180 /210\n内外撑圆：每个尺寸18个：90/120/150/180/210\n单固定架： 18个\n方形磁铁框架每个尺寸：36个，460*254，536*356，680*340                     ', '曹建挺', '1', '代理商保修', '保修员d', '2018-06-06 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('14', 'XS-1801118A', '0', '11', '37', '183', '2', '中国', 'SINSIM', '2', '2', '9', '36', '330', '750', '1150', '单机', '', null, '', '[]', '186000', '0', '0', '0', '2018-07-16', '2018-07-16', '', '周立锋', '1', '代理商保修', '保修员d', '2018-06-06 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('15', 'XS-1801118B', '0', '11', '50', '183', '2', '中国', 'SINSIM', '2', '2', '9', '38', '330', '750', '1150', '单机', '', null, '', '[]', '196000', '0', '0', '0', '2018-07-16', '2018-07-16', '', '周立锋', '1', '代理商保修', '保修员d', '2018-06-06 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('16', 'XS-1801122', '0', '12', '51', '183', '2', '中国', 'SINSIM', '3', '1', '3', '132', '110', '700', '1600', '单机', '', null, '', '[{\"name\":\"单金片\",\"number\":132,\"price\":\"645\"}]', '444860', '0', '0', '0', '2018-07-25', '2018-07-27', '', '公司', '1', 'SinSim保修', '保修员S', '2018-06-07 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('17', 'XS-1801121', '0', '13', '52', '183', '2', '中国', 'SINSIM', '1', '2', '12', '25', '400', '950', '950', '单机', '', null, '', '[]', '172000', '0', '0', '0', '2018-06-28', '2018-06-28', '', '公司', '1', 'SinSim保修', '保修员S', '2018-06-08 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('18', 'XS-1802056', '0', '14', '53', '188', '2', '中国', 'SINSIM电脑绣花机', '2', '3', '3', '176', '55', '600', '1520', '单机', '', null, '', '[]', '315000', '0', '0', '0', '2018-08-01', '2018-08-01', '主轴双伺服   独立压脚  加宽线架台板（可以放2个线团）', '孙情', '1', '代理商保修', '保修员d', '2018-06-08 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('20', 'XS-1802057(改-20180621)', '0', '16', '55', '188', '3', '中国', 'SINSIM电脑绣花机', '1', '2', '6', '15', '165', '700', '900', '单机', '', null, '', '[]', '76000', '0', '0', '0', '2018-07-02', '2018-07-12', '', '孙情', '1', '代理商保修', '保修员D', '2018-06-09 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('21', 'XS-1801120', null, '17', '56', '188', '2', '中国', 'SINSIM电脑绣花机', '1', '2', '9', '30', '300', '750', '1050', '单机', '', null, null, '[]', '157000', '0', '0', '0', '2018-07-18', '2018-07-18', null, '高耀叶', '1', '代理商保修', '保修员d', '2018-06-09 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('22', '骆1045', '0', '18', '57', '177', '2', '巴基斯坦', 'GREAT-SINSIM', '1', '2', '9', '24', '400', '850', '1330', '叠机', '', null, '', '[{\"name\":\"单金片\",\"number\":24,\"price\":\"600\"}]', '135300', '0', '0', '0', '2018-07-20', '2018-07-20', '1-台板挡板发货前转上 ，Y 驱动后方需有2个抱箍轴套。 \n2-每台另送：针杆驱动器3个， 传动块4个， 压脚驱动组件4个， 开口压脚凸轮4个， 开口挑线凸轮1个， 压脚连杆4个， 挑线杆A-3个， KD102板2块， 跳跃电机1只， Y 驱动皮带1根。 \n3-每台另配一台稳压器， 另配报警板3块， 底检条3块。 \n4-旋梭处加装小灯， 开机显示Great. \n5-50个金线防止缠绕塑料帽， \n6-使用小型直档（确保装上布夹后不会碰到装置或平绣压脚） 。', '骆晓军', '1', '代理商保修', '保修员d', '2018-06-09 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('23', '骆1043A', '0', '19', '58', '177', '2', '巴基斯坦', 'Great-SINSIM', '1', '2', '9', '8', '330', '700', '1300', '叠机', '', null, '', '[]', '59700', '0', '0', '0', '2018-07-20', '2018-07-20', '1-台板档板发货前装上。 Y驱动后方需有两个抱箍轴套，\n2. 每台另外配送：针杆驱动器1个，传动块2个， 压脚驱动组件2个， 开口压脚凸轮1个， 开口挑线凸轮1个，压脚连杆2个， 挑线杆A-1个，KD102板2块，跳跃电机1只， Y 驱动皮带一根。\n3.每台另配一台稳压器，另配报警板1块，底检条1根，\n4. 旋梭处加装小灯。开机显示Great. 5.10个金线防缠绕塑料帽。 6.此批机器需配大小磁铁。', '骆晓军', '1', '代理商保修', '保修员D', '2018-06-09 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('24', '骆1043B', '0', '19', '59', '177', '2', '巴基斯坦', 'Great-SINSIM', '1', '2', '9', '8', '330', '700', '1330', '叠机', '', null, '', '[{\"name\":\"简易绳绣\",\"number\":8,\"price\":\"800\"},{\"name\":\"单金片\",\"number\":8,\"price\":\"600\"}]', '59700', '0', '0', '0', '2018-07-20', '2018-07-20', '1-台板档板发货前装上。 Y驱动后方需有两个抱箍轴套，\n2. 每台另外配送：针杆驱动器1个，传动块2个， 压脚驱动组件2个， 开口压脚凸轮1个， 开口挑线凸轮1个，压脚连杆2个， 挑线杆A-1个，KD102板2块，跳跃电机1只， Y 驱动皮带一根。\n3.每台另配一台稳压器，另配报警板1块，底检条1根，\n4. 旋梭处加装小灯。开机显示Great. 5.10个金线防缠绕塑料帽。 6.此批机器需配大小磁铁。', '骆晓军', '1', '代理商保修', '保修员D', '2018-06-09 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('25', '骆1044', '0', '20', '60', '177', '2', '巴基斯坦', 'Great-SINSIM', '3', '6', '12', '1', '0', '510', '350', '叠机', '', null, '', '[]', '21000', '0', '0', '0', '2018-07-01', '2018-07-10', '委外加工： \n1-2台机器田岛绿的，1台机器是灰色的。（业务自行安排）\n2-不要帽绣装置。 \n3-1号针装雕孔针。 ', '骆晓军', '1', '代理商保修', '保修员D', '2018-06-09 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('26', '曹456A(改-20180614)', '0', '21', '61', '173', '3', '印度', 'SINSIM', '1', '2', '6', '25', '250', '800', '1300', '叠机', '', null, '跟曹456B叠机', '[]', '16100', '0', '0', '0', '2018-07-10', '2018-07-18', '1. 每2个头打一个花架孔，横档打孔，配大小磁铁。\n2. 防尘板处加嵌木钉，\n3. 配新款的绕线机，机脚加橡皮盖子\n4. 每台机器配额外报警板，底检条各2块，针杆驱动器，开口挑线凸轮，开口压脚凸轮各3个。\n5. 旋梭出加装LED小灯。', '曹建挺', '1', 'SinSim保修', '保修员s', '2018-06-09 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('27', '曹456B(改-20180614)', '0', '21', '62', '173', '3', '印度', 'SINSIM', '1', '2', '6', '29', '180', '800', '1300', '叠机', '', null, '跟曹456A叠机', '[]', '16500', '0', '0', '0', '2018-07-10', '2018-07-18', '1. 每3个头打一个花架孔，横档打孔，配大小磁铁。\n2. 防尘板处嵌木钉。\n3. 配新款绕线机，机脚加橡皮盖子\n4. 每台机器额外配报警板，底检条各2块，针杆驱动器，开口挑线凸轮，开口压脚凸轮各3个\n5. 旋梭处加装LED小灯。', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-09 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('28', '曹456C(改-20180614)', '0', '21', '63', '173', '3', '印度', 'SINSIM', '1', '2', '9', '28', '250', '800', '1520', '单机', '', null, '', '[]', '18300', '0', '0', '0', '2018-07-10', '2018-07-18', '特别备注：主电机用2.5KW大豪电机\n1. 每2个头打一个花架孔，横档打孔，配大小磁铁\n2. 防尘板处嵌木钉\n3. 配新款绕线机，机脚加橡皮盖子\n4. 每台机器额外配报警板，底检条各2块，针杆驱动器，开口挑线凸轮，开口压脚凸轮各3个\n5. 旋梭处加装LED小灯。', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-09 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('29', 'XS-1801116(改-20180621)', '0', '22', '64', '183', '3', '中国', 'SINSIM', '2', '2', '4', '90', '168', '700', '1050', '单机', '', null, '', '[]', '335000', '0', '0', '0', '2018-08-03', '2018-08-03', '', '公司', '1', 'SinSim保修', '保修员S', '2018-06-09 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('30', 'XS-1801123', '0', '24', '66', '188', '2', '中国', 'SINSIM电脑绣花机', '2', '2', '6', '50', '300', '750', '1540', '单机', '', null, '', '[{\"name\":\"单针双片\",\"number\":50,\"price\":\"1350\"}]', '302500', '0', '0', '0', '2018-08-06', '2018-08-06', '面线夹持电磁铁装机壳后；线架前端装日光灯，自动换底线预留孔位，首尾加辅挡，大梁左右侧打孔，针杆架左侧打装置孔（销售部提供孔位），大梁上打金片盘架子孔，加宽线架台板（放一个大线团）；装25块冠军金片板，电磁阀50个，气管连接好，电源盒装好，单金片装置电机线、开关线由客户提供帮忙穿好，装梁后罩盖；压脚做相应更改（满足客户6号针加装单金片）。配送2套双框架', '公司', '1', 'SinSim保修', '保修员S', '2018-06-11 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('31', '迪133B      (LE108)', '0', '55', '97', '174', '2', '印度', 'SINSIM/PAPID1.2', '2', '2', '6', '24', '250', '650', '1300+', '叠机', '', null, '自叠', '[{\"name\":\"2mm单金片\",\"number\":24,\"price\":\"630\"}]', '95000', '0', '3000', '0', '2018-07-11', '2018-07-17', ' 1. 有绳绣装置的要用绳绣大线架，佳宇板子\n 2. Y1-X4\n 3.无装置的梁上左右过线孔也都要打', '曹建挺', '1', '代理商保修', '保修员D', '2018-06-11 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('32', '曹455', '0', '56', '98', '173', '2', '中国', 'WATTARCO', '1', '2', '9', '18', '450', '450', '750+', '单机', '', null, '', '[{\"name\":\"简易绳绣\",\"number\":18,\"price\":\"125\"},{\"name\":\"激光装置\",\"number\":2,\"price\":\"1450\"}]', '17450', '0', '0', '0', '2018-07-10', '2018-07-18', '1. 每头打一个花架孔，横档打孔，配大小磁铁\n2. 防尘板处加嵌木钉\n3. 配新款的绕线机\n4. 每台机器配额外报警板，底检条各2块，梭壳，针杆驱动器，开口压脚凸轮，开口挑线凸轮各3个。\n5. 旋梭处加装LED小灯', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-11 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('33', '迪133A      (LE107)', '0', '55', '100', '174', '2', '印度', 'SINSIM/PAPID1.2', '2', '2', '6', '27', '250', '650', '1300+', '叠机', '', null, '自叠', '[{\"name\":\"2mm单金片\",\"number\":27,\"price\":\"630\"},{\"name\":\"简易绳绣\",\"number\":27,\"price\":\"750\"}]', '100000', '0', '3000', '0', '2018-07-12', '2018-07-17', ' 1. 有绳绣装置的要用绳绣大线架，佳宇板子\n 2. Y1-X4\n 3.无装置的梁上左右过线孔也都要打', '曹建挺', '1', '代理商保修', '保修员d', '2018-06-11 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('34', '迪133C     (LE109-110)', '0', '55', '101', '174', '2', '印度', 'SINSIM/PAPID1.2', '4', '3', '4', '50', '125', '500', '1300', '叠机', '', null, '自叠', '[]', '107000', '0', '3000', '0', '2018-06-13', '2018-07-29', ' 1. 有绳绣装置的要用绳绣大线架，佳宇板子\n 2. Y1-X4\n 3.头距 250及以上无装置的梁上左右过线孔也都要打，200头距打一侧孔', '曹建挺', '1', '代理商保修', '保修员d', '2018-06-11 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('35', '迪133D    (LE111)', '0', '55', '102', '174', '2', '印度', 'SINSIM/PAPID1.2', '2', '2', '6', '27', '250', '650', '1300+', '叠机', '', null, '自叠', '[]', '100000', '0', '3000', '0', '2018-07-13', '2018-07-18', ' 1. 有绳绣装置的要用绳绣大线架，佳宇板子\n 2. Y1-X4\n 3.250头距及以上无装置的梁上左右过线孔也都要打，200头距打一侧', '曹建挺', '1', '代理商保修', '保修员d', '2018-06-11 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('36', '迪133E    (LE112)', '0', '55', '103', '174', '2', '印度', 'SINSIM/PAPID1.2', '2', '2', '6', '27', '250', '650', '1300+', '叠机', '', null, '自叠', '[{\"name\":\"简易绳绣\",\"number\":27,\"price\":\"750\"}]', '100000', '0', '3000', '0', '2018-07-13', '2018-07-21', ' 1. 有绳绣装置的要用绳绣大线架，佳宇板子\n 2. Y1-X4\n 3.250头距及以上无装置的梁上左右过线孔也都要打，200头距 打一侧', '曹建挺', '1', '代理商保修', '保修员d', '2018-06-11 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('37', '迪133F     (LE113)', '0', '59', '105', '174', '2', '印度', 'SINSIM/PAPID1.2', '1', '2', '6', '4', '250', '650', '1300+', '单机', '', null, '叠机高度设计', '[{\"name\":\"2mm单金片\",\"number\":4,\"price\":\"630\"}]', '47000', '0', '3000', '0', '2018-07-08', '2018-07-19', ' 1. 有绳绣装置的要用绳绣大线架，佳宇板子\n 2. Y1-X4\n 3.头距 250及以上无装置的梁上左右过线孔也都要打，200头距打一侧孔', '曹建挺', '1', '代理商保修', '保修员d', '2018-06-11 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('38', '迪133G      (LE114)', '0', '59', '106', '174', '2', '印度', 'SINSIM/PAPID1.2', '2', '2', '6', '35', '250', '650', '1300+', '叠机', '', null, '自叠', '[{\"name\":\"2mm单金片\",\"number\":35,\"price\":\"630\"}]', '123200', '0', '3000', '0', '2018-07-08', '2018-07-19', ' 1. 有绳绣装置的要用绳绣大线架，佳宇板子\n 2. Y1-X4\n 3.头距 250及以上无装置的梁上左右过线孔也都要打，200头距打一侧孔', '曹建挺', '1', '代理商保修', '保修员d', '2018-06-11 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('39', 'XS-1801111改', '0', '66', '113', '183', '2', '中国', 'SINSIM', '1', '11', '6', '11', '165', '700', '1000', '单机', '', null, '', '[{\"name\":\"单针双片\",\"number\":5,\"price\":\"1350\"}]', '75250', '1500', '0', '0', '2018-06-28', '2018-06-29', '机架用XS-1801111订单中原来的机架', '蔡天明', '1', '代理商保修', '保修员D', '2018-06-12 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('40', 'XS-1801115', '0', '67', '114', '183', '2', '中国', 'SINSIM', '2', '2', '15', '25', '400', '950', '1050', '单机', '', null, '', '[]', '170000', '0', '0', '0', '2018-07-23', '2018-07-23', '大梁左边打孔，加宽线架台板', '公司', '1', '代理商保修', '保修员D', '2018-06-12 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('41', '迪134A   (LE115-117A)', '0', '68', '115', '174', '2', '印度', 'SINSIM/PAPID1.2', '3', '2', '6', '27', '250', '650', '1300+', '叠机', '', null, '自叠两台，一台跟迪134C-625-250叠装', '[]', '100000', '0', '3000', '0', '2018-07-15', '2018-07-15', ' 1. 有绳绣装置的要用绳绣大线架，佳宇板子\n 2. Y1-X4\n 3.250头距及以上无装置的梁上左右过线孔也都要打，200头距打一侧', '曹建挺', '1', '代理商保修', '保修员d', '2018-06-13 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('42', '迪134B     (LE116)', '0', '68', '116', '174', '2', '印度', 'SINSIM/PAPID1.2', '2', '2', '9', '27', '250', '650', '1300+', '叠机', '', null, '自叠两台，', '[]', '102700', '0', '3000', '0', '2018-07-15', '2018-07-15', ' 1. 有绳绣装置的要用绳绣大线架，佳宇板子\n 2. Y1-X4\n 3.250头距及以上无装置的梁上左右过线孔也都要打，200头距打一侧', '曹建挺', '1', '代理商保修', '保修员d', '2018-06-13 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('43', '迪134C    (LE177B)', '0', '68', '117', '174', '2', '印度', 'SINSIM/PAPID1.2', '1', '2', '6', '25', '250', '650', '1300+', '叠机', '', null, '一台跟迪134A-627-250叠装  ，备注', '[]', '97200', '0', '3000', '0', '2018-07-15', '2018-07-15', ' 1. 有绳绣装置的要用绳绣大线架，佳宇板子\n 2. Y1-X4\n 3.250头距及以上无装置的梁上左右过线孔也都要打，200头距打一侧', '曹建挺', '1', '代理商保修', '保修员d', '2018-06-13 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('44', '曹456A', '26', '21', '118', '173', '2', '印度', 'SINSIM', '1', '2', '6', '25', '250', '800', '1300', '叠机', '', null, '跟曹456B叠机', '[]', '16100', '0', '0', '0', '2018-07-10', '2018-07-18', '1. 每2个头打一个花架孔，横档打孔，配大小磁铁。\n2. 防尘板处加嵌木钉，\n3. 配新款的绕线机，机脚加橡皮盖子\n4. 每台机器配额外报警板，底检条各2块，针杆驱动器，开口挑线凸轮，开口压脚凸轮各3个。\n5. 旋梭出加装LED小灯。', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-14 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('46', '曹456B', '27', '21', '120', '173', '2', '印度', 'SINSIM', '1', '2', '6', '29', '180', '800', '1300', '叠机', '', null, '跟曹456A叠机', '[]', '16500', '0', '0', '0', '2018-07-10', '2018-07-18', '1. 每3个头打一个花架孔，横档打孔，配大小磁铁。\n2. 防尘板处嵌木钉。\n3. 配新款绕线机，机脚加橡皮盖子\n4. 每台机器额外配报警板，底检条各2块，针杆驱动器，开口挑线凸轮，开口压脚凸轮各3个\n5. 旋梭处加装LED小灯。', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-14 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('50', '曹456C', '28', '21', '124', '173', '2', '印度', 'SINSIM', '1', '2', '9', '28', '250', '800', '1520', '单机', '', null, '', '[]', '18300', '0', '0', '0', '2018-07-10', '2018-07-18', '特别备注：主电机用2.5KW大豪电机\n1. 每2个头打一个花架孔，横档打孔，配大小磁铁\n2. 防尘板处嵌木钉\n3. 配新款绕线机，机脚加橡皮盖子\n4. 每台机器额外配报警板，底检条各2块，针杆驱动器，开口挑线凸轮，开口压脚凸轮各3个\n5. 旋梭处加装LED小灯。', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-14 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('51', '曹457A', '0', '70', '126', '173', '2', '印度', 'SINSIM', '1', '4', '6', '35', '240', '1150', '1750', '叠机', '', null, '跟曹475B叠机', '[]', '32000', '0', '0', '0', '2018-07-15', '2018-08-07', '1.毛巾机头要用乐清毛巾机头，机针16配18\n2. 防尘板处加嵌木钉，2个松线电机和2个H电机\n3. 额外配64齿尼龙凸轮，毛巾感应器各3个 ，针嘴8个。', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-16 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('52', '曹457B', '0', '70', '127', '173', '2', '印度', 'SINSIM', '1', '4', '6', '39', '240', '750', '1750', '叠机', '', null, '跟曹457A叠机', '[]', '34000', '0', '0', '0', '2018-07-15', '2018-08-07', '1.毛巾机头要用乐清毛巾机头，机针16配18\n2. 防尘板处加嵌木钉，2个松线电机和2个H电机\n3. 额外配64齿尼龙凸轮，毛巾感应器各3个 ，针嘴8个。', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-16 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('53', 'XS-1801128', null, '71', '128', '183', '5', '中国', 'SINSIM', '4', '2', '4', '84+3', '168', '1100', '1600', '单机', '', null, '', '[]', '350000', '0', '0', '0', '2018-06-17', '2018-06-17', '大梁左右打孔,带叠片功能', '公司', '1', 'SinSim保修', '保修员S', '2018-06-16 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('54', 'XS-1801127', '0', '72', '129', '183', '2', '中国', 'SINSIM', '2', '2', '4', '90', '165', '750', '1600', '单机', '', null, '', '[]', '369500', '0', '0', '0', '2018-08-06', '2018-08-06', '大梁右边打孔，打电磁阀孔，针杆架右边打冠军单金片孔，加装金片板（45块）、补绣信号线和电源线装好，梁后加装罩盖，针夹压脚和电磁阀客户提供，预留自动换底线孔位，旋梭轴套用轴承款，线架加装日光灯', '冯保锋', '1', '代理商保修', '保修员d', '2018-06-19 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('55', 'XS-1802058', '0', '73', '130', '188', '1', '中国', 'SINSIM电脑绣花机', '2', '15', '3', '176', '55', '600', '1550', '单机', '', null, '', '[]', '285000', '0', '0', '0', '2018-08-06', '2018-08-08', '加宽线架台板（可以放两个线团），独立压脚，电脑锁密码2018年9月-2019年8月', '孙情', '1', '代理商保修', '保修员S', '2018-06-19 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('56', 'XS-1802059', '0', '74', '131', '188', '1', '中国', 'SINSIM电脑绣花机', '2', '15', '3', '176', '55', '600', '1550', '单机', '', null, '', '[]', '285000', '0', '0', '0', '2018-08-06', '2018-08-08', '加宽线架台板（可以放两个线团），独立压脚，锁密码2018年9月-2019年8月', '孙情', '1', '代理商保修', '保修员d', '2018-06-19 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('57', 'XS-1802060A', '0', '75', '132', '188', '2', '中国', 'SINSIM电脑绣花机', '1', '2', '4', '60', '165', '700', '1600', '单机', '', null, '', '[]', '205000', '0', '0', '0', '2018-07-23', '2018-07-28', '大梁左边打孔，加宽线架台板，电脑锁密码2018年8月-2019年7月', '孙情', '1', '代理商保修', '保修员d', '2018-06-19 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('58', 'XS-1802060B', '0', '75', '133', '188', '2', '中国', 'SINSIM电脑绣花机', '1', '2', '6', '41', '240', '700', '1600', '单机', '', null, '', '[]', '178300', '0', '0', '0', '2018-07-23', '2018-07-28', '大梁左边打孔，加宽线架台板，电脑锁密码2018年8月-2019年7月', '孙情', '1', '代理商保修', '保修员d', '2018-06-19 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('59', 'XS-1802060C', '0', '75', '134', '188', '2', '中国', 'SINSIM电脑绣花机', '1', '2', '4', '15', '前9头165后6头240', '700', '1600', '单机', '', null, '', '[]', '75000', '0', '0', '0', '2018-07-23', '2018-07-28', '大梁左边打孔，加宽线架台板', '孙情', '1', '代理商保修', '保修员d', '2018-06-19 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('60', '曹459       ', '0', '76', '135', '174', '1', '智利', 'HD', '1', '15', '9', '10', '400', '400', '500', '单机', '', null, '要求装在小柜里发货，注意长度', '[{\"name\":\"铝合金框架花架\",\"number\":1,\"price\":\"150\"}]', '9650', '0', '0', '0', '2018-07-20', '2018-07-25', '1.装花架处用上防滑螺丝套（每个头一个）。前后档都需每头打一个孔（ 横档也打孔），配大小磁铁。\n          2.防尘板处加嵌木钉（防滑螺丝座）, \n          3东阳花架-7, 12, 15, 18, 25, 300X314MM，每头两个，大小磁铁每台多配一组，\n           4 配底检条/报警板 每台各3块，', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-20 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('61', 'XS-1802057', '20', '16', '136', '188', '2', '中国', 'SINSIM电脑绣花机', '1', '2', '6', '15', '165', '700', '900', '单机', '', null, '', '[]', '76000', '0', '0', '0', '2018-07-02', '2018-07-12', '大梁左边打孔，加宽线架台板', '孙情', '1', '代理商保修', '保修员d', '2018-06-21 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('62', 'XS-1801116', '29', '22', '137', '183', '2', '中国', 'SINSIM', '2', '2', '4', '90', '168', '700', '1050', '单机', '', null, '', '[]', '335000', '0', '0', '0', '2018-08-03', '2018-08-03', '大梁左右打装置穿线孔，针杆架左边打绳绣孔，右边打金片孔，每台机器送一个报警板和底检条，预留自动换底线孔位', '公司', '1', 'SinSim保修', '保修员S', '2018-06-21 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('63', 'XS-1801129', '0', '77', '138', '183', '1', '中国', 'SINSIM', '2', '2', '9', '45+1', '330', '750', '1540', '单机', '', '', '', '[]', '298900', '0', '0', '0', '2018-08-10', '2018-08-10', '面线夹持电磁铁装机壳后；线架前端装日光灯，自动换底线预留孔位，首尾加辅挡，大梁右侧打孔，大梁上打金片盘架子孔，1号针针杆架打单金片孔，加宽线架台板（放一个大线团）；装23块冠军金片板，电磁阀46个，气管连接好，电源盒装好，单金片装置电机线、开关线由客户提供帮忙穿好，装梁后罩盖；压脚做相应更改（满足客户1号针加装单金片）。配送2套双框架。机器长度与1801123订单650/300机型一样，末号头余量加满至600。', '公司', '1', 'SinSim保修', '保修员S', '2018-06-22 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('64', 'XS-1803027', '0', '78', '139', '188', '1', '中国', 'SINSIM', '1', '1', '4', '116', '108', '750', '1520', '单机', '', null, '', '[]', '375000', '28000', '0', '0', '2018-08-10', '2018-08-10', '线架前端装日光灯，配一套双框架（费用在何绍华佣金中扣除），,2018年9月-2019年3月锁密码', '何绍华', '1', 'SinSim保修', '保修员S', '2018-06-23 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('65', '曹460C', '0', '79', '140', '174', '1', '孟加拉国', 'SINSIM', '2', '12', '915+615', '15', '500', '混合刺绣范围850', '700+', '叠机', '', null, '\n自叠\n', '[{\"name\":\"单针双片\",\"number\":15,\"price\":\"200\"},{\"name\":\"单针双片\",\"number\":15,\"price\":\"200\"}]', '3', '0', '0', '0', '2018-07-30', '2018-07-30', '1. (每头打一个花架孔，横档打孔，配大小磁铁。）\n2.防尘板处加嵌木钉,  3-每台机器配上稳压器，\n 4-每台机器--额外配报警板，底检条，梭壳，针杆驱器，开口压脚凸轮，开口挑线凸轮各3个  。           \n 5-旋梭处加装LED小灯 , 6..额外要求每台机器多配毛巾感应器2个，毛巾64齿尼龙轮 ，毛巾针杆，毛巾压脚，针嘴定位套各 2个，毛巾针杆组件一套。 \n7.毛巾头用乐清高速款，毛巾机针装16配18，         ', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-23 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('66', '曹460D', '0', '79', '141', '174', '1', '孟加拉国', 'SINSIM', '1', '12', '906+606', '6', '500', '混合刺绣范围850', '700+', '单机', '', null, '\n叠机设计\n', '[{\"name\":\"单针双片\",\"number\":6,\"price\":\"200\"},{\"name\":\"单针双片\",\"number\":6,\"price\":\"200\"}]', '3', '0', '0', '0', '2018-07-30', '2018-07-30', '1. (每头打一个花架孔，横档打孔，配大小磁铁。）\n2.防尘板处加嵌木钉,  ,3-每台机器配上稳压器，\n 4-每台机器--额外配报警板，底检条，梭壳，针杆驱器，开口压脚凸轮，开口挑线凸轮各3个  。           \n 5-旋梭处加装LED小灯 , 6..额外要求每台机器多配毛巾感应器1个，毛巾64齿尼龙轮 ，毛巾针杆，毛巾压脚，针嘴定位套各 1个，毛巾针杆组件一套。   \n 7.毛巾头用乐清高速款，毛巾机针装16配18，     ', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-23 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('67', '曹460A', '0', '79', '142', '174', '1', '孟加拉国', 'SINSIM', '2', '2', '9', '20', '400', '装置400', '700+', '叠机', '', null, '\n自叠\n', '[{\"name\":\"单针双片\",\"number\":20,\"price\":\"200\"},{\"name\":\"单针双片\",\"number\":20,\"price\":\"200\"}]', '3', '0', '0', '0', '2018-07-30', '2018-07-30', '1. (每头打一个花架孔，横档打孔，配大小磁铁。）\n 2.防尘板处加嵌木钉, \n 3-每台机器配上稳压器， \n 4-每台机器--额外配报警板，底检条，梭壳，针杆驱器，开口压脚凸轮，开口挑线凸轮各3个  。           \n 5-旋梭处加装LED小灯', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-23 00:00:00', null, null);
INSERT INTO `machine_order` VALUES ('68', '曹460B', '0', '79', '143', '174', '1', '孟加拉国', 'SINSIM', '1', '11', '912+12', '12', '600', '混合盘带800', '700+', '单机', '', null, '\n叠机设计\n', '[{\"name\":\"单针双片\",\"number\":12,\"price\":\"200\"},{\"name\":\"单针双片\",\"number\":12,\"price\":\"200\"}]', '3', '0', '0', '0', '2018-07-30', '2018-07-30', '1. (每头打一个花架孔，横档打孔，配大小磁铁。）\n2.防尘板处加嵌木钉, \n3-每台机器配上稳压器， \n 4-每台机器--额外配报警板，底检条，梭壳，针杆驱器，开口压脚凸轮，开口挑线凸轮各3个  。           \n 5-旋梭处加装LED小灯 , 6. 盘带头要配上扁的带子嘴，各个尺寸， 配上做绳子的嘴，1.5mm， 2.0mm 和其他各尺寸。', '曹建挺', '1', 'SinSim保修', '保修员S', '2018-06-23 00:00:00', null, null);

-- ----------------------------
-- Table structure for `machine_type`
-- ----------------------------
DROP TABLE IF EXISTS `machine_type`;
CREATE TABLE `machine_type` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '机器类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

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
INSERT INTO `machine_type` VALUES ('14', '高速纯毛巾');
INSERT INTO `machine_type` VALUES ('15', '单凸轮');

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_change_record
-- ----------------------------
INSERT INTO `order_change_record` VALUES ('1', '26', '曹456A电压改为220V, 加油系统改为上油盒下手压', '174', '2018-06-14 13:54:59');
INSERT INTO `order_change_record` VALUES ('2', '27', '加油系统改为上油盒下手压', '174', '2018-06-14 13:54:59');
INSERT INTO `order_change_record` VALUES ('3', '28', '加油系统改为上油盒下手压，价格不变', '174', '2018-06-14 13:54:59');
INSERT INTO `order_change_record` VALUES ('4', '20', '增加备注', '183', '2018-06-21 08:52:40');
INSERT INTO `order_change_record` VALUES ('5', '29', '增加备注，其余不变', '183', '2018-06-21 09:32:43');

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
  `framework_light` varchar(255) DEFAULT NULL COMMENT '机架台板：日光灯',
  `driver_type` varchar(255) DEFAULT NULL COMMENT '驱动：类型',
  `driver_method` varchar(255) DEFAULT NULL COMMENT '驱动：方式',
  `driver_reel_hole` varchar(255) DEFAULT NULL COMMENT '驱动：绷架孔',
  `driver_horizon_num` tinyint(4) DEFAULT NULL COMMENT '驱动：横档数量',
  `driver_vertical_num` tinyint(4) DEFAULT NULL COMMENT '驱动：直档数量',
  `driver_reel` varchar(255) DEFAULT NULL COMMENT '驱动：绷架',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_detail
-- ----------------------------
INSERT INTO `order_detail` VALUES ('1', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '每台加装冠军单金片，单金片架子装梁上，每台机器带一套双框架，预留自动换底线孔位', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('2', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '每台加装冠军双金片，双金片架子装梁上，每台机器带一套双框架，预留自动换底线孔位', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('3', '无', '无', '无', '无', '无', '无', '528', '中文', '儒竞', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', '', '普通', '宽幅(中传动)', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('4', '无', '无', '无', '无', '无', '无', 'M98', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '广濑高速', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '0', '1', '无');
INSERT INTO `order_detail` VALUES ('5', '9色', '集中', '集中', '大豪', '无', '16', 'D19', '英语', '大豪', '伺服', '电机剪线', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '台板上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '3', '无');
INSERT INTO `order_detail` VALUES ('7', '6色', '集中', '集中', '大豪', '无', '16', 'D19', '英语', '大豪', '伺服', '无', '220V', null, '无', '无', '无', null, '16', '无', null, null, '无', '无', '无', null, '田岛绿桔纹', '杨桉木', '浅绿', '有', '台板上', '一个托架下,一个左侧台板下', null, '普通', '普通', '有', '1', '3', '无');
INSERT INTO `order_detail` VALUES ('8', '6色', '集中', '集中', '大豪', '无', '16', 'D19', '英语', '大豪', '伺服', '无', '220V', '', '无', '无', '无', '', '16', '无', '', '', '无', '无', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '台板上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '3', '无');
INSERT INTO `order_detail` VALUES ('9', '6色', '集中', '集中', '大豪', '无', '16', 'D19', '英语', '大豪', '伺服', '普通剪线', '380V', '', '凸轮换色', '上油盒下点动', '仿田岛', '', '12', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '有', '<p>左侧冠军单针双片5+5，偏心孔，右正冠军单针双片3+4MM，中心孔, （参考曹367定单装置）</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '8', '有');
INSERT INTO `order_detail` VALUES ('33', '无', '无', '无', '无', '无', '无', 'M98', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '丝杆换色', '上油盒下手压', '信胜款', '', '12', 'GROZ', '', '', '广濑1.6倍高速', '电机跳跃', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '台板上', '无', '', '普通', '普通', '无', '0', '0', '无');
INSERT INTO `order_detail` VALUES ('34', '无', '无', '无', '无', '无', '无', 'M98', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '11', '尖头针', '', '', '广濑高速', '电磁铁跳跃', '有', '<p>冠军独立盘带头，左侧冠军简易绳绣，配盘带绣配件(标配Z绣导管、标配扁带导嘴、标配绳绣导嘴)，4台机器总共赠送6套冠军简易绳绣装置</p>', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('35', '无', '无', '无', '无', '无', '无', '528', '中文', '儒竞', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '9', 'SUK', '', '', '佐文', '电磁铁跳跃', '有', '佐文高速旋梭，机针用9号针，整机长度不超过6.69米，拆线架', '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '无', '', '普通', '普通', '无', '0', '0', '无');
INSERT INTO `order_detail` VALUES ('36', '无', '无', '无', '无', '无', '无', 'M98', '英语', '大豪', '伺服', '电机剪线', '380V', '', '凸轮换色', '上油盒下点动', '仿田岛', '', '11', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '有', '<p>左正冠军单针双片5+5MM偏心孔，右正冠军单针双片3+4MM中间孔，（装置参考曹413--SJPX2，，没有铝合金花架，是塑料花架，但没有这一条，要算钱用的，只能选铝合金花架</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '9', '有');
INSERT INTO `order_detail` VALUES ('37', '无', '无', '无', '无', '无', '无', '528', '中文', '儒竞', '伺服', '电机静态剪线', '380V', '', '丝杆换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '勾刀加保护装置，机器后面台板下头尾处各装一个开关，大梁400*500，预留自动换底线孔位', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '0', '1', '无');
INSERT INTO `order_detail` VALUES ('50', '无', '无', '无', '无', '无', '无', '528', '中文', '儒竞', '伺服', '电机静态剪线', '380V', '', '丝杆换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '勾刀加保护装置，机器后面台板下头尾处各装一个开关，大梁400*500，预留自动换底线孔位', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '0', '1', '无');
INSERT INTO `order_detail` VALUES ('51', '无', '无', '无', '无', '无', '无', '528', '中文', '儒竞', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '固定式', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '<p>3号针装冠军单片（3-9mm），调试3mm，每台配冲片机和双框架，预留自动换底线孔位</p>', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('52', '无', '无', '无', '无', '无', '无', 'M98', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '广濑高速', '电机跳跃', '有', '<p>大梁左右打孔，加宽线架台板，装梁后罩盖；进口马牌皮带；带双叠片、珠绣、绳绣功能</p>', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '0', '1', '无');
INSERT INTO `order_detail` VALUES ('53', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '不剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '无', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('54', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '不剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '无', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('55', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', '', '普通', '普通', '无', '0', '0', '无');
INSERT INTO `order_detail` VALUES ('56', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', null, '凸轮换色', '上油盒下手压', '信胜款', null, '14', 'SUK', null, null, '韩大黑芯', '电磁铁跳跃', '有', null, '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '无', null, '普通', '普通', '无', '0', '0', '无');
INSERT INTO `order_detail` VALUES ('57', '无', '无', '无', '无', '无', '无', 'M98', '英语', '大豪', '伺服', '电机剪线', '220V', '', '丝杆换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '有', '<p>右侧佳宇单金片装置3mm,</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '台板上', '1个托架下', '', '普通', '普通', '有', '1', '3', '无');
INSERT INTO `order_detail` VALUES ('58', '无', '无', '无', '无', '无', '无', 'M98', '英语', '大豪', '伺服', '普通剪线', '220V', '', '丝杆换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '台板上', '1个托架下', '', '普通', '普通', '有', '1', '3', '无');
INSERT INTO `order_detail` VALUES ('59', '无', '无', '无', '无', '无', '无', 'M98', '英语', '大豪', '伺服', '普通剪线', '220V', '', '丝杆换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '有', '<p>左侧佳宇简易绳绣， 右侧佳宇单金片3mm,</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '台板上', '1个托架下', '', '普通', '普通', '有', '1', '3', '无');
INSERT INTO `order_detail` VALUES ('60', '无', '无', '无', '无', '无', '无', '285', '英语', '大豪', '三相步进', '普通剪线', '220V', '', '凸轮换色', '无', '仿田岛', '', '14', 'SUK', '', '', '佐伩12RYP', '电机跳跃', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '梁上', '1个托架下', '', '普通', '普通', '无', '0', '0', '无');
INSERT INTO `order_detail` VALUES ('61', '无', '无', '无', '无', '无', '无', 'M98', '英语', '大豪', '伺服', '不剪线', '380V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '12', '无');
INSERT INTO `order_detail` VALUES ('62', '无', '无', '无', '无', '无', '无', 'M98', '英语', '大豪', '伺服', '不剪线', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '10', '无');
INSERT INTO `order_detail` VALUES ('63', '无', '无', '无', '无', '无', '无', 'M98', '英语', '大豪', '伺服', '不剪线', '220V', '', '丝杆换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '台板上', '一个托架下,一个左侧台板下', '', '普通', '宽幅(中传动)', '有', '1', '14', '无');
INSERT INTO `order_detail` VALUES ('64', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '0', '0', '无');
INSERT INTO `order_detail` VALUES ('66', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '<p>1号针加装冠军叠片2-9mm(JP303)，调试3+5mm</p>', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('97', '无', '无', '无', '无', '无', '无', '528', '英语', '大豪', '伺服', '无', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍', '电磁铁跳跃', '无', '<p>左侧冠军单金片2MM</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '1个托架下', '', '普通', '普通', '无', '1', '10', '无');
INSERT INTO `order_detail` VALUES ('98', '无', '无', '无', '无', '无', '无', 'M98', '英语', '大豪', '伺服', '电机剪线', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '12', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '有', '<p>左侧绳绣用佳宇绳绣042，右侧激光头用广州亿和激光（1号头，2号头）</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '9', '无');
INSERT INTO `order_detail` VALUES ('100', '无', '无', '无', '无', '无', '无', '528', '英语', '大豪', '伺服', '无', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍', '电磁铁跳跃', '无', '<p>右冠军单金片2MM，配齐过片槽，左佳宇简绳 040</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '1个托架下', '', '普通', '普通', '无', '1', '10', '无');
INSERT INTO `order_detail` VALUES ('101', '无', '无', '无', '无', '无', '无', '528', '英语', '大豪', '伺服', '无', '220V', '', '凸轮换色', '无', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍', '电磁铁跳跃', '无', '<p>要求各针板间有台板，直档用小型的</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '1个托架下', '', '普通', '普通', '无', '1', '10', '无');
INSERT INTO `order_detail` VALUES ('102', '无', '无', '无', '无', '无', '无', '528', '英语', '大豪', '伺服', '无', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍', '电磁铁跳跃', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '1个托架下', '', '普通', '普通', '无', '1', '11', '无');
INSERT INTO `order_detail` VALUES ('103', '无', '无', '无', '无', '无', '无', '528', '英语', '大豪', '伺服', '无', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍', '电磁铁跳跃', '无', '<p>左佳宇简绳 040</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '1个托架下', '', '普通', '普通', '无', '1', '11', '无');
INSERT INTO `order_detail` VALUES ('105', '无', '无', '无', '无', '无', '无', '528', '英语', '大豪', '伺服', '无', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍', '电磁铁跳跃', '无', '<p>左侧冠军2MM单金片，配齐所有过片槽</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '1个托架下', '', '普通', '普通', '无', '1', '2', '无');
INSERT INTO `order_detail` VALUES ('106', '无', '无', '无', '无', '无', '无', '528', '英语', '大豪', '伺服', '无', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍', '电磁铁跳跃', '无', '<p>左侧冠军2MM单金片，配齐所有过片槽</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '1个托架下', '', '普通', '普通', '无', '1', '14', '无');
INSERT INTO `order_detail` VALUES ('113', '无', '无', '无', '无', '无', '无', 'M98', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '<p>偶数头右边装冠军叠片（3-9mm），2、4、6头装冠军叠片，调试3+3；8、10头装冠军叠片，调试3+5。第11号头装冠军盘带头，10号头与11号盘带头头距330</p>', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '0', '0', '无');
INSERT INTO `order_detail` VALUES ('114', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '广濑高速', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '0', '1', '无');
INSERT INTO `order_detail` VALUES ('115', '无', '无', '无', '无', '无', '无', '528', '英语', '大豪', '伺服', '无', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍', '电磁铁跳跃', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '1个托架下', '', '普通', '普通', '无', '1', '11', '无');
INSERT INTO `order_detail` VALUES ('116', '无', '无', '无', '无', '无', '无', '528', '英语', '大豪', '伺服', '无', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍', '电磁铁跳跃', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '1个托架下', '', '普通', '普通', '无', '1', '11', '无');
INSERT INTO `order_detail` VALUES ('117', '无', '无', '无', '无', '无', '无', '528', '英语', '大豪', '伺服', '无', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍', '电磁铁跳跃', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '1个托架下', '', '普通', '普通', '无', '1', '11', '无');
INSERT INTO `order_detail` VALUES ('118', '无', '无', '无', '无', '无', '无', 'M98', '英语', '大豪', '伺服', '不剪线', '220V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '12', '无');
INSERT INTO `order_detail` VALUES ('120', '无', '无', '无', '无', '无', '无', 'M98', '英语', '大豪', '伺服', '不剪线', '220V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '10', '无');
INSERT INTO `order_detail` VALUES ('124', '无', '无', '无', '无', '无', '无', 'M98', '英语', '大豪', '伺服', '不剪线', '220V', '', '丝杆换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '台板上', '一个托架下,一个左侧台板下', '', '普通', '宽幅(中传动)', '有', '1', '14', '无');
INSERT INTO `order_detail` VALUES ('126', '6色', '集中', '集中', '大豪', '无', '16', 'D19', '英语', '大豪', '伺服', '普通剪线', '220V', '', '凸轮换色', '无', '无', '', '无', '无', '', '', '无', '无', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '台板上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '13', '无');
INSERT INTO `order_detail` VALUES ('127', '6色', '集中', '集中', '大豪', '无', '16', 'D19', '英语', '大豪', '伺服', '普通剪线', '220V', '', '凸轮换色', '无', '无', '', '无', '无', '', '', '无', '无', '无', '', '田岛绿桔纹', '杨桉木', '浅绿', '有', '台板上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '13', '无');
INSERT INTO `order_detail` VALUES ('128', '无', '无', '无', '无', '无', '无', '528', '中文', '儒竞', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('129', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('130', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '不剪线', '380V', '', '凸轮换色', '上油盒下手压', '固定式', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '无', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('131', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '不剪线', '380V', '', '凸轮换色', '上油盒下手压', '固定式', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '无', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('132', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('133', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('134', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '无', '信胜款', '', '14', 'SUK', '', '', '广濑1.6倍高速', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('135', '无', '无', '无', '无', '无', '无', '528', '英语', '大豪', '伺服', '普通剪线', '220V', '', '凸轮换色', '无', '信胜款', '', '11', 'SUK', '', '', '广濑ATR(MG1)', '电磁铁跳跃', '无', '<p>没有铝合金花架，是塑料花架</p>', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '有', '梁上', '一个左侧台板下', '', '普通', '普通', '有', '1', '5', '有');
INSERT INTO `order_detail` VALUES ('136', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '鲁冰花浅灰边', '无', '梁上', '无', '', '普通', '普通', '无', '0', '0', '无');
INSERT INTO `order_detail` VALUES ('137', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '0', '0', '无');
INSERT INTO `order_detail` VALUES ('138', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '信胜款', '', '14', 'SUK', '', '', '韩大黑芯', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '1', '0', '无');
INSERT INTO `order_detail` VALUES ('139', '无', '无', '无', '无', '无', '无', 'A18', '中文', '大豪', '伺服', '电机静态剪线', '380V', '', '凸轮换色', '上油盒下手压', '固定式', '', '14', 'SUK', '', '', '广濑1.6倍高速', '电磁铁跳跃', '有', '', '田岛绿桔纹', '杨桉木', '浅绿', '无', '台板上', '无', '', '普通', '普通', '无', '1', '1', '无');
INSERT INTO `order_detail` VALUES ('140', '6色', '集中', '集中', '大豪', '无', '16', 'D19', '英语', '大豪', '伺服', '电机剪线', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '12', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '有', '<p>302左侧冠军单针双片5+5，302右侧冠军单针双片5+5MM，中心孔, （配齐所有3、4、7、9过片槽）</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '8', '无');
INSERT INTO `order_detail` VALUES ('141', '6色', '集中', '集中', '大豪', '无', '16', 'D19', '英语', '大豪', '伺服', '电机剪线', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '12', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '有', '<p>302左侧冠军单针双片5+5，302右侧冠军单针双片5+5MM，中心孔, （配齐所有3、4、7、9过片槽）</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '3', '无');
INSERT INTO `order_detail` VALUES ('142', '无', '无', '无', '无', '无', '无', 'M98', '英语', '大豪', '伺服', '电机剪线', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '12', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '有', '<p>302左侧冠军单针双片5+5，302右侧冠军单针双片5+5MM，中心孔, （配齐所有3、4、7、9过片槽）</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '10', '无');
INSERT INTO `order_detail` VALUES ('143', '无', '无', '无', '无', '冠军独立', '无', 'M98', '英语', '大豪', '伺服', '电机剪线', '220V', '', '凸轮换色', '上油盒下点动', '信胜款', '', '12', 'SUK', '', '', '广濑ATR(MG1F)黑心', '电机跳跃', '有', '<p>302左侧冠军单针双片5+5，302右侧冠军单针双片5+5MM，中心孔, （配齐所有3、4、7、9过片槽）</p>', '田岛绿桔纹', '杨桉木', '浅绿', '有', '梁上', '一个托架下,一个左侧台板下', '', '普通', '普通', '有', '1', '6', '无');

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
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_loading_list
-- ----------------------------
INSERT INTO `order_loading_list` VALUES ('1', '17', '/opt/sinsim/output/oll/XS-1801121_17_LoadingFile__0.xlsx', '1', '2018-06-12 13:20:44', null);
INSERT INTO `order_loading_list` VALUES ('2', '10', '/opt/sinsim/output/oll/XS-1801114_10_LoadingFile__0.xlsx', '1', '2018-06-12 13:25:03', null);
INSERT INTO `order_loading_list` VALUES ('3', '12', '/opt/sinsim/output/oll/XS-1801119_12_LoadingFile__0.xlsx', '1', '2018-06-12 16:22:03', null);
INSERT INTO `order_loading_list` VALUES ('4', '13', '/opt/sinsim/output/oll/曹454B_13_LoadingFile__0.xlsx', '1', '2018-06-14 14:29:54', null);
INSERT INTO `order_loading_list` VALUES ('5', '9', '/opt/sinsim/output/oll/曹454A_9_LoadingFile__0.xlsx', '1', '2018-06-14 14:30:54', null);
INSERT INTO `order_loading_list` VALUES ('6', '14', '/opt/sinsim/output/oll/XS-1801118A_14_LoadingFile__0.xlsx', '1', '2018-06-14 14:32:16', null);
INSERT INTO `order_loading_list` VALUES ('7', '15', '/opt/sinsim/output/oll/XS-1801118B_15_LoadingFile__0.xlsx', '1', '2018-06-14 14:33:02', null);
INSERT INTO `order_loading_list` VALUES ('8', '39', '/opt/sinsim/output/oll/XS-1801111改_39_LoadingFile__0.xlsx', '1', '2018-06-16 07:31:50', null);
INSERT INTO `order_loading_list` VALUES ('9', '3', '/opt/sinsim/output/oll/XS-1802054_3_LoadingFile__0.xlsx', '1', '2018-06-16 07:32:43', null);
INSERT INTO `order_loading_list` VALUES ('10', '4', '/opt/sinsim/output/oll/XS-1801047_4_LoadingFile__0.xlsx', '1', '2018-06-16 15:52:43', null);
INSERT INTO `order_loading_list` VALUES ('11', '44', '/opt/sinsim/output/oll/曹456A_44_LoadingFile__0.xlsx', '1', '2018-06-20 07:39:36', null);
INSERT INTO `order_loading_list` VALUES ('12', '46', '/opt/sinsim/output/oll/曹456B_46_LoadingFile__0.xlsx', '1', '2018-06-20 07:39:59', null);
INSERT INTO `order_loading_list` VALUES ('13', '50', '/opt/sinsim/output/oll/曹456C_50_LoadingFile__0.xlsx', '1', '2018-06-22 08:28:37', null);
INSERT INTO `order_loading_list` VALUES ('14', '40', '/opt/sinsim/output/oll/XS-1801115_40_LoadingFile__0.xlsx', '1', '2018-06-22 08:29:43', null);
INSERT INTO `order_loading_list` VALUES ('15', '32', '/opt/sinsim/output/oll/曹455_32_LoadingFile__0.xlsx', '1', '2018-06-22 08:30:43', null);
INSERT INTO `order_loading_list` VALUES ('16', '22', '/opt/sinsim/output/oll/骆1045_22_LoadingFile__0.xlsx', '1', '2018-06-22 08:31:17', null);
INSERT INTO `order_loading_list` VALUES ('17', '61', '/opt/sinsim/output/oll/XS-1802057_61_LoadingFile__0.xlsx', '1', '2018-06-22 16:31:24', null);
INSERT INTO `order_loading_list` VALUES ('18', '33', '/opt/sinsim/output/oll/迪133A      (LE107)_33_LoadingFile__0.xlsx', '1', '2018-06-22 16:33:18', null);
INSERT INTO `order_loading_list` VALUES ('19', '35', '/opt/sinsim/output/oll/迪133D    (LE111)_35_LoadingFile__0.xlsx', '1', '2018-06-22 16:33:45', null);
INSERT INTO `order_loading_list` VALUES ('20', '36', '/opt/sinsim/output/oll/迪133E    (LE112)_36_LoadingFile__0.xlsx', '1', '2018-06-22 16:34:16', null);
INSERT INTO `order_loading_list` VALUES ('21', '41', '/opt/sinsim/output/oll/迪134A   (LE115-117A)_41_LoadingFile__0.xlsx', '1', '2018-06-22 16:35:06', null);
INSERT INTO `order_loading_list` VALUES ('22', '42', '/opt/sinsim/output/oll/迪134B     (LE116)_42_LoadingFile__0.xlsx', '1', '2018-06-22 16:35:21', null);
INSERT INTO `order_loading_list` VALUES ('23', '43', '/opt/sinsim/output/oll/迪134C    (LE177B)_43_LoadingFile__0.xlsx', '1', '2018-06-23 14:18:21', null);
INSERT INTO `order_loading_list` VALUES ('24', '38', '/opt/sinsim/output/oll/迪133G      (LE114)_38_LoadingFile__0.xlsx', '1', '2018-06-23 14:19:20', null);
INSERT INTO `order_loading_list` VALUES ('25', '21', '/opt/sinsim/output/oll/XS-1801120_21_LoadingFile__0.xlsx', '1', '2018-06-23 14:20:06', null);
INSERT INTO `order_loading_list` VALUES ('26', '62', '/opt/sinsim/output/oll/XS-1801116_62_LoadingFile__0.xlsx', '1', '2018-06-25 17:08:37', null);
INSERT INTO `order_loading_list` VALUES ('27', '31', '/opt/sinsim/output/oll/迪133B      (LE108)_31_LoadingFile__0.xlsx', '1', '2018-06-25 17:09:21', null);
INSERT INTO `order_loading_list` VALUES ('28', '23', '/opt/sinsim/output/oll/骆1043A_23_LoadingFile__0.xlsx', '1', '2018-06-25 17:10:01', null);
INSERT INTO `order_loading_list` VALUES ('29', '24', '/opt/sinsim/output/oll/骆1043B_24_LoadingFile__0.xlsx', '1', '2018-06-25 17:10:24', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_sign
-- ----------------------------
INSERT INTO `order_sign` VALUES ('1', '1', '签核完成', '[{\"date\":\"2018-06-04 16:43:20\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"金片装置价格更改，驱动方式改为普通\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-04 16:44:14\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"横档加上！\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-05 09:02:35\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"斯华锋\"},{\"result\":1,\"date\":\"2018-06-06 07:24:48\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-06 09:26:13\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-06 11:22:48\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-5-31 定金20万承兑，11.24%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-06 13:31:35\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金19.66%，发货前达到43.8%，余融资租赁\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-06 15:25:11\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"王海江\"}]', '2018-06-01 09:30:25', '2018-06-06 15:24:58');
INSERT INTO `order_sign` VALUES ('2', '2', '签核完成', '[{\"date\":\"2018-06-04 16:43:32\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"金片装置价格有误，驱动方式改为普通\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-04 16:45:03\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"横档加上！\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-05 09:02:15\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"斯华锋\"},{\"result\":1,\"date\":\"2018-06-06 07:26:57\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-06 09:24:45\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-06 11:23:28\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-5-31 定金20万承兑，11.24%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-06 13:32:16\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金19.66%，发货前达到43.8%，余融资租赁\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-06 15:24:28\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"王海江\"}]', '2018-06-01 09:30:25', '2018-06-06 15:24:16');
INSERT INTO `order_sign` VALUES ('3', '3', '签核完成', '[{\"date\":\"2018-06-09 17:35:22\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核查无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-11 08:27:35\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-11 14:56:51\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"图纸加急！\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-11 14:59:29\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-11 16:05:41\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"date\":\"2018-06-12 09:06:36\",\"result\":1,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"17年有余款5.5万，其中3万为本单定金，12.45%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-12 09:36:27\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金12.45%，发货前达到100%\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-13 15:45:36\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-02 11:07:28', '2018-06-13 15:45:19');
INSERT INTO `order_sign` VALUES ('4', '4', '签核完成', '[{\"date\":\"2018-06-12 16:19:16\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核查无误，尽快交货\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-13 09:58:27\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"方炬江\"},{\"date\":\"2018-06-13 10:39:19\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-13 10:39:56\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-13 14:53:25\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"date\":\"2018-06-13 16:35:15\",\"result\":1,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-6-12 交行6万，100%\",\"user\":\"袁海琼\"},{\"date\":\"2018-06-14 09:10:40\",\"result\":1,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"款已收到\",\"user\":\"汤能萍\"},{\"date\":\"2018-06-14 11:06:27\",\"result\":1,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-04 10:21:40', '2018-06-14 11:06:27');
INSERT INTO `order_sign` VALUES ('5', '5', '签核完成', '[{\"date\":\"2018-06-12 15:41:55\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"被不小心驳回， 重新提交审核。 \",\"user\":\"骆晓军\"},{\"date\":\"2018-06-12 15:50:17\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"梭箱体用双玉 田岛剪线 63宽梭箱体。\",\"user\":\"方炬江\"},{\"date\":\"2018-06-12 16:11:32\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-12 16:12:06\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-13 14:49:56\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"date\":\"2018-06-13 18:30:00\",\"result\":1,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.04，中信银行收定金￥126414，定金率20%（订单L1033A/B,订单总金额￥632400）\",\"user\":\"何璐洁\"},{\"date\":\"2018-06-14 09:11:03\",\"result\":1,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金126414，订单总金额632400，定金20%，余款TT+DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-14 11:08:33\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-04 11:00:20', '2018-06-14 11:08:33');
INSERT INTO `order_sign` VALUES ('7', '7', null, '[{\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '2018-06-04 11:26:42', null);
INSERT INTO `order_sign` VALUES ('8', '8', '签核完成', '[{\"date\":\"2018-06-12 15:41:32\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"被不小心驳回 ， 重新提交审核。\",\"user\":\"骆晓军\"},{\"date\":\"2018-06-12 15:50:41\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-12 16:11:21\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-12 16:11:58\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-13 14:47:08\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"date\":\"2018-06-13 18:30:20\",\"result\":1,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.04，中信银行收定金￥126414，定金率20%（订单L1033A/B,订单总金额￥632400）\",\"user\":\"何璐洁\"},{\"date\":\"2018-06-14 09:11:10\",\"result\":1,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金126414，订单总金额632400，定金20%，余款TT+DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-14 11:08:06\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-04 11:26:42', '2018-06-14 11:08:06');
INSERT INTO `order_sign` VALUES ('9', '9', '签核完成', '[{\"date\":\"2018-06-07 10:47:02\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"曹建挺\"},{\"result\":1,\"date\":\"2018-06-07 11:23:09\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"右侧正面单针双片的垫条要定制（加厚1CM）\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-08 13:16:05\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-08 13:18:15\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-08 13:35:35\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-07 16:05:33\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018年6月4日，建设银行收定金$20000，定金率33.36%\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-09 09:14:52\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金33%，余款交单后DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-09 12:42:49\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"王海江\"}]', '2018-06-05 09:09:22', '2018-06-09 12:42:35');
INSERT INTO `order_sign` VALUES ('10', '10', '签核完成', '[{\"date\":\"2018-06-06 14:18:30\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核对无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-06 17:13:50\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-07 08:44:46\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-07 08:58:58\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-07 09:25:36\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"date\":\"2018-06-08 09:37:45\",\"result\":1,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"18.6.6 交行6万，20%\",\"user\":\"袁海琼\"},{\"date\":\"2018-06-08 14:55:49\",\"result\":1,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金20%，发货前达到100%\",\"user\":\"汤能萍\"},{\"date\":\"2018-06-09 12:44:10\",\"result\":1,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\" \",\"user\":\"王海江\"}]', '2018-06-05 13:25:46', '2018-06-09 12:43:56');
INSERT INTO `order_sign` VALUES ('11', '11', '签核完成', '[{\"date\":\"2018-06-15 17:05:41\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核查无误\",\"user\":\"王铁锋\"},{\"result\":1,\"date\":\"2018-06-16 17:05:35\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-19 09:49:22\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-19 09:49:53\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-19 13:48:32\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-20 08:31:52\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-6-13 承兑30万，30.61%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-21 07:49:19\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金30.6%，发货验收后付清\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-21 13:44:11\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-05 14:00:25', '2018-06-21 13:44:06');
INSERT INTO `order_sign` VALUES ('12', '12', '签核完成', '[{\"date\":\"2018-06-06 15:08:49\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核对无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-06 17:19:44\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-07 08:45:03\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-07 08:58:40\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-07 09:32:13\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-08 09:34:32\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"18.6.4 交行2万，20%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-08 14:56:29\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金20%，发货前达到100%\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-09 12:40:26\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\" \",\"user\":\"王海江\"}]', '2018-06-05 14:56:17', '2018-06-09 12:40:11');
INSERT INTO `order_sign` VALUES ('13', '13', '签核完成', '[{\"date\":\"2018-06-08 09:08:03\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-08 09:18:15\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"附加装置呢？ 铝合金框架花架是什么？请在备注注明！\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-08 13:15:48\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-08 13:18:08\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-08 13:35:13\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-07 16:02:59\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018年6月4日，建设银行收定金$20000，定金率33.36%\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-09 09:14:23\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金33%，余款交单后DP。下次付款方式一定要明确写明\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-09 12:39:29\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"请财务此机型的成本核算\",\"user\":\"王海江\"}]', '2018-06-06 10:25:15', '2018-06-09 12:39:15');
INSERT INTO `order_sign` VALUES ('14', '14', '签核完成', '[{\"date\":\"2018-06-08 09:57:29\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核对无误\",\"user\":\"王铁锋\"},{\"result\":1,\"date\":\"2018-06-08 10:23:38\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-08 13:16:32\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-08 13:17:56\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-08 13:32:08\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-09 08:27:15\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-09 09:11:01\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-09 12:34:44\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"王海江\"}]', '2018-06-06 14:53:39', '2018-06-09 12:34:30');
INSERT INTO `order_sign` VALUES ('15', '15', '签核完成', '[{\"date\":\"2018-06-08 09:54:39\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核对无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-08 10:22:30\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-08 13:16:22\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-08 13:17:45\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-08 13:31:53\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"date\":\"2018-06-09 08:26:51\",\"result\":1,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-6-8 交行10万，13.09%\",\"user\":\"袁海琼\"},{\"date\":\"2018-06-09 09:10:35\",\"result\":1,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金13.09%，发货前达到41%，余租赁\",\"user\":\"汤能萍\"},{\"date\":\"2018-06-09 12:35:26\",\"result\":1,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"王海江\"}]', '2018-06-06 15:36:52', '2018-06-09 12:35:12');
INSERT INTO `order_sign` VALUES ('16', '16', '签核完成', '[{\"date\":\"2018-06-08 11:00:05\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核对无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-08 11:02:41\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"附加装置几毫米片！\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-08 13:14:21\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-08 13:17:35\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-08 13:31:18\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-08 14:30:02\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-5-19 承兑20万，12.58%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-08 14:58:29\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金12.58%，发货前过到30.8%，余融资租赁。\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-09 12:33:02\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"设计方案要求技术部，开一次评审会\",\"user\":\"王海江\"}]', '2018-06-07 14:30:39', '2018-06-09 12:32:48');
INSERT INTO `order_sign` VALUES ('17', '17', '签核完成', '[{\"date\":\"2018-06-08 10:00:57\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核对无误。交货期短，望各部门间快速流转，保证货期\",\"user\":\"王铁锋\"},{\"result\":1,\"date\":\"2018-06-08 10:33:45\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-08 13:15:06\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-08 13:17:28\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-08 13:28:06\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-08 14:23:17\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.6.7 交行3万，17.44%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-08 14:59:07\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金17.44%，发货后一周内付清余款\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-09 12:30:04\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"动框要求用红外线调试出厂\",\"user\":\"王海江\"}]', '2018-06-08 08:38:32', '2018-06-09 12:29:49');
INSERT INTO `order_sign` VALUES ('18', '18', '签核完成', '[{\"date\":\"2018-06-09 17:33:33\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核对无误\",\"user\":\"王铁锋\"},{\"result\":1,\"date\":\"2018-06-11 08:33:58\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"普通平绣改成单凸轮\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-11 14:57:09\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-11 14:59:43\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-11 16:02:24\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-12 09:13:31\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.4.28 交行10万，15.87%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-12 09:32:57\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金15.87%，发货前达到50%，余银行按谒\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-13 15:44:56\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-08 13:14:48', '2018-06-13 15:44:39');
INSERT INTO `order_sign` VALUES ('20', '20', '签核完成', '[{\"date\":\"2018-06-09 17:34:06\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核对无误\",\"user\":\"王铁锋\"},{\"result\":1,\"date\":\"2018-06-11 08:36:50\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\\n\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-11 14:57:34\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-11 15:00:16\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-11 15:58:00\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-12 09:23:33\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.6.8交行2万，26.32%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-12 09:30:03\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金26.3%，发货前达到100%\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-13 15:43:53\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-09 08:34:01', '2018-06-13 15:43:36');
INSERT INTO `order_sign` VALUES ('21', '21', '签核完成', '[{\"date\":\"2018-06-09 17:15:33\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核对无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-11 08:39:20\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"方炬江\"},{\"date\":\"2018-06-11 14:57:50\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-11 15:00:26\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-11 15:55:21\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"date\":\"2018-06-15 09:42:25\",\"result\":1,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-6-7 交行3万，19.11%\",\"user\":\"袁海琼\"},{\"date\":\"2018-06-15 14:14:12\",\"result\":1,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金19.1%，发货前达到30%，余融资租赁\",\"user\":\"汤能萍\"},{\"date\":\"2018-06-16 14:15:44\",\"result\":1,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-09 08:44:01', '2018-06-16 14:15:42');
INSERT INTO `order_sign` VALUES ('22', '22', '签核完成', '[{\"date\":\"2018-06-09 10:46:41\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"备注栏内容未填\",\"user\":\"骆晓军\"},{\"result\":1,\"date\":\"2018-06-09 14:15:31\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-11 14:58:04\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-11 15:00:38\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-11 15:35:46\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-15 13:36:54\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.12，中信银行收定金￥29866，定金率20%\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-15 14:12:00\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金20%，余款TT\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-16 14:18:24\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-09 09:22:21', '2018-06-16 14:18:22');
INSERT INTO `order_sign` VALUES ('23', '23', '签核完成', '[{\"date\":\"2018-06-11 11:11:55\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意。 \",\"user\":\"骆晓军\"},{\"result\":1,\"date\":\"2018-06-11 13:39:36\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-11 14:58:21\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-11 15:00:46\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-11 15:34:52\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-15 13:10:24\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.12，中信银行收定金￥26120，定金率20%\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-15 14:12:49\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"20%定金， 余款TT, DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-16 14:17:43\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-09 10:43:55', '2018-06-16 14:17:42');
INSERT INTO `order_sign` VALUES ('24', '24', '签核完成', '[{\"date\":\"2018-06-11 11:11:27\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意。 \",\"user\":\"骆晓军\"},{\"result\":1,\"date\":\"2018-06-11 13:38:08\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-11 14:58:32\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-11 15:00:56\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-11 15:31:02\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-15 13:10:07\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.12，中信银行收定金￥26120，定金率20%\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-15 14:13:05\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"20%定金， 余款TT, DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-16 14:17:09\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-09 10:52:16', '2018-06-16 14:17:07');
INSERT INTO `order_sign` VALUES ('25', '25', '签核完成', '[{\"date\":\"2018-06-13 13:38:28\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意。　\",\"user\":\"骆晓军\"},{\"result\":1,\"date\":\"2018-06-13 13:51:50\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"外协\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-13 14:38:39\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-13 15:22:47\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-14 14:53:21\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-15 13:29:55\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.12，中信银行收定金￥12600，定金率20%\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-15 14:12:22\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"20%定金， 余款T/T, DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-16 14:18:08\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-09 11:06:06', '2018-06-16 14:18:06');
INSERT INTO `order_sign` VALUES ('26', '26', '签核完成', '[{\"date\":\"2018-06-11 14:14:24\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\".同意\",\"user\":\"曹建挺\"},{\"result\":1,\"date\":\"2018-06-11 16:43:54\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-12 10:20:08\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-12 10:23:14\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-12 10:56:29\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-12 16:02:41\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.06，中信银行收定金$12750,定金率25%\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-12 16:04:49\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金25%，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-13 17:32:24\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"价格太低，后续财务从新核价，以后重新定价\",\"user\":\"王海江\"}]', '2018-06-09 14:59:21', '2018-06-13 17:32:25');
INSERT INTO `order_sign` VALUES ('27', '27', '签核完成', '[{\"date\":\"2018-06-11 14:13:41\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\".同意\",\"user\":\"曹建挺\"},{\"result\":1,\"date\":\"2018-06-11 16:48:04\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-12 10:19:39\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-12 10:22:57\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-12 10:56:01\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-12 16:03:03\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.06，中信银行收定金$12750,定金率25%\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-12 16:05:23\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金25%，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-13 17:32:09\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"价格太低，后续财务从新核价，以后重新定价。\",\"user\":\"王海江\"}]', '2018-06-09 15:06:49', '2018-06-13 17:32:10');
INSERT INTO `order_sign` VALUES ('28', '28', '签核完成', '[{\"date\":\"2018-06-11 16:59:06\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\".同意\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-11 16:59:13\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-12 10:21:10\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-12 10:22:38\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-12 10:55:27\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-12 16:02:54\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.06，中信银行收定金$12750,定金率25%\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-12 16:05:14\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金25%，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-13 17:27:46\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"价格太低，财务后续成本核算，以后从新定价\",\"user\":\"王海江\"}]', '2018-06-09 15:12:53', '2018-06-13 17:27:46');
INSERT INTO `order_sign` VALUES ('29', '29', '签核完成', '[{\"date\":\"2018-06-15 15:13:45\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"尽量七月底前交货\",\"user\":\"王铁锋\"},{\"result\":1,\"date\":\"2018-06-15 16:20:03\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-16 13:56:08\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-16 14:13:07\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-16 16:42:33\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-19 09:46:22\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-6-13 承兑10万，14.93%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-19 13:34:55\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金14.9%，发货前达到50%，余分11个月付清（承兑支付）\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-20 12:26:26\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-09 16:48:33', '2018-06-20 12:26:22');
INSERT INTO `order_sign` VALUES ('30', '30', '签核完成', '[{\"date\":\"2018-06-15 15:16:04\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核对无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-15 16:24:53\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-16 13:55:24\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-16 14:14:08\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-16 16:41:09\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"date\":\"2018-06-19 09:50:28\",\"result\":1,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-6-11 交行10万，13.51%\\n\",\"user\":\"袁海琼\"},{\"date\":\"2018-06-19 13:33:43\",\"result\":1,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金13.51%，发货前达到32%，余融资租赁\",\"user\":\"汤能萍\"},{\"date\":\"2018-06-20 12:24:52\",\"result\":1,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-11 11:33:41', '2018-06-20 12:24:48');
INSERT INTO `order_sign` VALUES ('31', '31', '签核完成', '[{\"date\":\"2018-06-15 09:28:56\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\".同意\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-15 10:38:26\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-15 11:09:31\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-15 11:10:59\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-15 14:39:19\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"系统设计计算错误    同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-19 09:31:51\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-19 13:36:40\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"迪立普定金无，要求下单订金在20%左右，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-20 12:50:43\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\" 1. 有绳绣装置的要用绳绣大线架，佳宇板子。这里没有绳绣板子\",\"user\":\"王海江\"}]', '2018-06-11 16:21:31', '2018-06-20 12:50:38');
INSERT INTO `order_sign` VALUES ('32', '32', '签核完成', '[{\"date\":\"2018-06-11 17:07:14\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\".同意\",\"user\":\"曹建挺\"},{\"result\":1,\"date\":\"2018-06-11 20:42:14\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-12 10:18:29\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-12 10:22:15\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-12 10:51:02\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-12 15:24:16\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.07，交通银行收押金￥28260，美元进来后退还\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-12 15:27:31\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"款到发货\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-13 16:28:12\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-11 16:37:47', '2018-06-13 16:28:13');
INSERT INTO `order_sign` VALUES ('33', '33', '签核完成', '[{\"date\":\"2018-06-15 09:28:09\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\".同意\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-15 10:37:56\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-15 11:09:23\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-15 11:11:07\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-15 14:39:00\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"系统设计计算错误    同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-19 09:32:00\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-19 13:36:25\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"迪立普定金无，要求下单订金在20%左右，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-20 12:51:03\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\" \",\"user\":\"王海江\"}]', '2018-06-11 17:37:19', '2018-06-20 12:50:59');
INSERT INTO `order_sign` VALUES ('34', '34', '签核完成', '[{\"date\":\"2018-06-15 09:29:42\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\".同意\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-15 10:38:37\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-15 11:09:42\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-15 11:10:52\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-15 14:39:33\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"系统设计计算错误     同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-19 09:31:12\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-19 13:37:18\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"迪立普定金无，要求下单订金在20%左右，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-20 12:47:26\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\" 1. 有绳绣装置的要用绳绣大线架，佳宇板子。这里没有绳绣板\",\"user\":\"王海江\"}]', '2018-06-11 17:37:19', '2018-06-20 12:47:22');
INSERT INTO `order_sign` VALUES ('35', '35', '签核完成', '[{\"date\":\"2018-06-15 09:30:36\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\".同意\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-15 10:38:49\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"方炬江\"},{\"date\":\"2018-06-15 11:09:51\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-15 11:10:45\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-15 14:39:47\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"系统设计计算错误     同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-19 09:31:34\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-19 13:36:51\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"迪立普定金无，要求下单订金在20%左右，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-20 12:49:29\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\" 1. 有绳绣装置的要用绳绣大线架，佳宇板子。这里没有绳绣板子\",\"user\":\"王海江\"}]', '2018-06-11 17:40:04', '2018-06-20 12:49:24');
INSERT INTO `order_sign` VALUES ('36', '36', '签核完成', '[{\"date\":\"2018-06-15 09:31:20\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\".同意\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-15 10:39:21\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"方炬江\"},{\"date\":\"2018-06-15 11:10:03\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-15 11:10:34\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-15 14:39:59\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"系统设计计算错误      同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-19 09:31:23\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-19 13:37:03\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"迪立普定金无，要求下单订金在20%左右，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-20 12:48:17\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\" \",\"user\":\"王海江\"}]', '2018-06-11 17:42:13', '2018-06-20 12:48:13');
INSERT INTO `order_sign` VALUES ('37', '37', '签核完成', '[{\"date\":\"2018-06-16 09:33:19\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\".同意\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-16 15:45:25\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-19 10:54:34\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-19 13:30:48\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-19 13:48:21\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-20 08:07:28\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-21 07:50:07\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"无定金，DP支付，建议下单付20%定金\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-21 13:49:19\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-11 17:52:36', '2018-06-21 13:49:13');
INSERT INTO `order_sign` VALUES ('38', '38', '签核完成', '[{\"date\":\"2018-06-15 09:33:47\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\".同意\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-15 10:39:43\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-16 13:54:34\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-16 14:14:51\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-16 16:39:46\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-19 08:39:37\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-19 13:37:38\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"迪立普定金无，要求下单订金在20%左右，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-20 12:45:51\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\" 1. 有绳绣装置的要用绳绣大线架，佳宇板子。这里没有绳绣板的\",\"user\":\"王海江\"}]', '2018-06-11 17:54:42', '2018-06-20 12:45:47');
INSERT INTO `order_sign` VALUES ('39', '39', '签核完成', '[{\"date\":\"2018-06-13 10:15:36\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核查无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-13 10:17:14\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-13 10:33:30\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-13 10:39:48\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-13 14:44:44\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-13 16:45:29\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"OK，原xs-1801111改单，本单无定金\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-14 09:12:31\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"无定金，发货前支付8.2万\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-14 11:06:02\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-12 11:21:17', '2018-06-14 11:06:01');
INSERT INTO `order_sign` VALUES ('40', '40', '签核完成', '[{\"date\":\"2018-06-13 09:59:42\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核对无误\",\"user\":\"王铁锋\"},{\"result\":1,\"date\":\"2018-06-13 10:08:04\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-13 10:35:03\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-13 10:39:42\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-13 14:15:21\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-13 16:39:42\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-6-12 交行6万，17.65%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-14 09:13:56\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金17.65%，发货前达到70.6%，剩余10万分两次付清，于2018年10月25日前支付5万，2019年1月25日前支付5万\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-14 11:04:37\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-12 14:39:23', '2018-06-14 11:04:36');
INSERT INTO `order_sign` VALUES ('41', '41', '签核完成', '[{\"date\":\"2018-06-15 09:27:04\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-15 10:36:51\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-16 13:53:52\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-16 14:15:07\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-16 16:30:57\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-19 08:06:55\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-19 13:38:17\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"迪立普定金无，要求下单定金在20%左右，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-20 12:33:20\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\" \",\"user\":\"王海江\"}]', '2018-06-13 14:24:05', '2018-06-20 12:33:15');
INSERT INTO `order_sign` VALUES ('42', '42', '签核完成', '[{\"date\":\"2018-06-15 09:26:09\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-15 10:35:12\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"方炬江\"},{\"date\":\"2018-06-16 13:52:42\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-16 14:15:26\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-16 16:30:11\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-19 08:06:35\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-19 13:38:27\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"迪立普定金无，要求下单定金在20%左右，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-20 12:28:47\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\" \",\"user\":\"王海江\"}]', '2018-06-13 14:24:05', '2018-06-20 12:28:43');
INSERT INTO `order_sign` VALUES ('43', '43', '签核完成', '[{\"date\":\"2018-06-15 09:26:38\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"1\",\"user\":\"曹建挺\"},{\"result\":1,\"date\":\"2018-06-15 10:36:03\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-16 13:53:17\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-16 14:15:17\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-16 16:30:31\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-19 08:07:10\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-19 13:38:03\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"迪立普定金无，要求下单定金在20%左右，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-20 12:34:09\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\" \",\"user\":\"王海江\"}]', '2018-06-13 14:34:45', '2018-06-20 12:34:05');
INSERT INTO `order_sign` VALUES ('44', '44', '签核完成', '[{\"date\":\"2018-06-14 14:22:54\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"曹建挺\"},{\"result\":1,\"date\":\"2018-06-14 14:49:40\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-14 14:52:11\",\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-14 16:51:42\",\"number\":4,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金25%，余款D/P.\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-16 14:14:00\",\"number\":5,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-14 13:50:11', '2018-06-16 14:13:58');
INSERT INTO `order_sign` VALUES ('46', '46', '签核完成', '[{\"date\":\"2018-06-14 14:22:22\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"曹建挺\"},{\"result\":1,\"date\":\"2018-06-14 14:49:08\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-14 14:52:22\",\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-14 16:51:24\",\"number\":4,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金25%，余款D/P.\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-16 14:15:04\",\"number\":5,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-14 13:53:47', '2018-06-16 14:15:02');
INSERT INTO `order_sign` VALUES ('50', '50', '签核完成', '[{\"date\":\"2018-06-14 14:21:52\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"曹建挺\"},{\"result\":1,\"date\":\"2018-06-14 14:48:36\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-14 14:52:34\",\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-14 16:51:12\",\"number\":4,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金25%，余款D/P.\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-16 14:14:26\",\"number\":5,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-14 13:54:59', '2018-06-16 14:14:24');
INSERT INTO `order_sign` VALUES ('51', '51', '签核完成', '[{\"date\":\"2018-06-16 11:14:35\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"曹建挺\"},{\"result\":1,\"date\":\"2018-06-16 17:03:52\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-19 10:53:17\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"注：跟曹457B叠机\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-19 13:31:04\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"注：跟曹457B叠机\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-19 13:44:18\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-20 08:23:53\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.14，中信银行收定金$13200,定金率20%\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-21 07:50:57\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金20%，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-21 13:48:36\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-16 09:13:54', '2018-06-21 13:48:30');
INSERT INTO `order_sign` VALUES ('52', '52', '签核完成', '[{\"date\":\"2018-06-19 13:46:06\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意，机器很急，请务必在7月底之前完成，谢谢\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-19 13:50:08\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-19 13:51:29\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"注：跟曹457A叠机\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-19 13:52:11\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"注：跟曹457A叠机\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-20 13:06:47\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-20 15:37:43\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.14，中信银行收定金$13200,定金率20%\",\"user\":\"何璐洁\"},{\"result\":1,\"date\":\"2018-06-21 07:50:49\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金20%，余款DP\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-21 13:48:51\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-16 09:17:09', '2018-06-21 13:48:46');
INSERT INTO `order_sign` VALUES ('53', '53', '销售部经理', '[{\"comment\":\"1\",\"date\":1529464487000,\"number\":1,\"result\":0,\"roleId\":7,\"signType\":\"需求单签核\",\"user\":\"王铁锋\"},{\"comment\":\"\",\"number\":2,\"result\":0,\"roleId\":8,\"signType\":\"需求单签核\",\"user\":\"\"},{\"comment\":\"\",\"number\":3,\"result\":0,\"roleId\":12,\"signType\":\"需求单签核\",\"user\":\"\"},{\"comment\":\"\",\"number\":4,\"result\":0,\"roleId\":4,\"signType\":\"需求单签核\",\"user\":\"\"},{\"comment\":\"\",\"number\":5,\"result\":0,\"roleId\":13,\"signType\":\"需求单签核\",\"user\":\"\"},{\"comment\":\"\",\"number\":6,\"result\":0,\"roleId\":15,\"signType\":\"需求单签核\",\"user\":\"\"},{\"comment\":\"\",\"number\":7,\"result\":0,\"roleId\":14,\"signType\":\"需求单签核\",\"user\":\"\"},{\"comment\":\"\",\"number\":8,\"result\":0,\"roleId\":6,\"signType\":\"需求单签核\",\"user\":\"\"}]', '2018-06-16 16:01:40', '2018-06-20 11:06:42');
INSERT INTO `order_sign` VALUES ('54', '54', '签核完成', '[{\"date\":\"2018-06-21 09:39:38\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核查无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-21 09:47:47\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-21 10:45:53\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-21 10:46:31\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-21 11:07:48\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同 意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-21 13:24:48\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-6-21 交行现金缴款10万，13.53%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-21 14:06:40\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金13.5%，发货前达到41.8%，余融资租赁\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-21 15:14:51\",\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-19 10:49:45', '2018-06-21 15:14:46');
INSERT INTO `order_sign` VALUES ('55', '55', '总经理', '[{\"date\":\"2018-06-21 17:10:22\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核查无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-21 17:06:27\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-22 16:46:25\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-22 16:50:39\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-25 09:07:19\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-25 13:43:20\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-6-19 交行10万，17.54%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-25 13:47:53\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金10.5%，发货前达到71%，余款分12个月付清\",\"user\":\"汤能萍\"},{\"result\":0,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"}]', '2018-06-19 15:10:36', '2018-06-25 13:47:55');
INSERT INTO `order_sign` VALUES ('56', '56', '总经理', '[{\"date\":\"2018-06-21 17:09:58\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核查无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-21 17:06:17\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"ok\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-22 16:46:09\",\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"result\":1,\"date\":\"2018-06-22 16:50:48\",\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-25 09:06:35\",\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"result\":1,\"date\":\"2018-06-25 13:45:48\",\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-6-19 交行6万，10.53%\",\"user\":\"袁海琼\"},{\"result\":1,\"date\":\"2018-06-25 13:47:45\",\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金10.5%，发货前达到71%，余款分12个月付清\",\"user\":\"汤能萍\"},{\"result\":0,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"}]', '2018-06-19 15:44:42', '2018-06-25 13:47:47');
INSERT INTO `order_sign` VALUES ('57', '57', '签核完成', '[{\"date\":\"2018-06-20 11:09:29\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核查无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-20 15:44:39\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-20 16:46:43\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-20 16:49:06\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-21 11:08:37\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"date\":\"2018-06-21 13:42:41\",\"result\":1,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"袁海琼\"},{\"date\":\"2018-06-21 14:02:14\",\"result\":1,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金17.45%，发货前达到70%，余款分期\",\"user\":\"汤能萍\"},{\"date\":\"2018-06-21 15:16:48\",\"result\":1,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-19 15:57:53', '2018-06-21 15:16:42');
INSERT INTO `order_sign` VALUES ('58', '58', '签核完成', '[{\"date\":\"2018-06-20 11:10:44\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核查无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-20 15:46:26\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-20 16:47:24\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-20 16:48:55\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-21 11:08:53\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"date\":\"2018-06-21 13:42:30\",\"result\":1,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"袁海琼\"},{\"date\":\"2018-06-21 14:04:05\",\"result\":1,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金17.45%，发货前达到70%，余款分12个月付清\",\"user\":\"汤能萍\"},{\"date\":\"2018-06-21 15:16:24\",\"result\":1,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-19 16:11:47', '2018-06-21 15:16:19');
INSERT INTO `order_sign` VALUES ('59', '59', '签核完成', '[{\"date\":\"2018-06-20 11:11:49\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核查无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-20 15:48:25\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-20 16:48:23\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-20 16:48:46\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"图纸加急\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-21 11:11:40\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"date\":\"2018-06-21 13:42:17\",\"result\":1,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018-6-19 交行8万，17.46%\",\"user\":\"袁海琼\"},{\"date\":\"2018-06-21 14:04:19\",\"result\":1,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金17.45%，发货前达到70%，余款分12个月付清\",\"user\":\"汤能萍\"},{\"date\":\"2018-06-21 15:16:03\",\"result\":1,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-19 16:35:38', '2018-06-21 15:15:57');
INSERT INTO `order_sign` VALUES ('60', '60', '总经理', '[{\"date\":\"2018-06-21 14:35:15\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"曹建挺\"},{\"date\":\"2018-06-21 15:31:41\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-21 16:57:56\",\"result\":1,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙2\"},{\"date\":\"2018-06-21 16:58:53\",\"result\":1,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-22 08:10:40\",\"result\":1,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"楼叶平\"},{\"date\":\"2018-06-22 11:24:58\",\"result\":1,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"2018.06.19，收中信银行定金$3000,定金率30.61%\",\"user\":\"何璐洁\"},{\"date\":\"2018-06-23 16:14:11\",\"result\":1,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"定金30.6%，余款TT\",\"user\":\"汤能萍\"},{\"date\":\"\",\"result\":0,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"}]', '2018-06-20 16:55:09', '2018-06-23 16:14:12');
INSERT INTO `order_sign` VALUES ('61', '61', '签核完成', '[{\"date\":\"2018-06-21 09:34:50\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"审核无误\",\"user\":\"王铁锋\"},{\"result\":1,\"date\":\"2018-06-21 09:47:17\",\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"result\":1,\"date\":\"2018-06-21 09:54:29\",\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"result\":1,\"date\":\"2018-06-21 13:13:29\",\"number\":4,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"汤能萍\"},{\"result\":1,\"date\":\"2018-06-21 13:42:34\",\"number\":5,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-21 08:52:40', '2018-06-21 13:42:28');
INSERT INTO `order_sign` VALUES ('62', '62', '签核完成', '[{\"date\":\"2018-06-21 09:34:17\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"审核无误\",\"user\":\"王铁锋\"},{\"date\":\"2018-06-21 09:47:04\",\"result\":1,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"方炬江\"},{\"date\":\"2018-06-21 09:54:43\",\"result\":1,\"number\":3,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"郑海龙\"},{\"date\":\"2018-06-21 13:14:17\",\"result\":1,\"number\":4,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"同意\",\"user\":\"汤能萍\"},{\"date\":\"2018-06-21 13:40:52\",\"result\":1,\"number\":5,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"OK\",\"user\":\"王海江\"}]', '2018-06-21 09:32:43', '2018-06-21 13:40:46');
INSERT INTO `order_sign` VALUES ('63', '63', '技术部经理', '[{\"date\":\"2018-06-25 16:58:59\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"核查无误\",\"user\":\"王铁锋\"},{\"date\":\"\",\"result\":0,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"result\":0,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"result\":0,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"result\":0,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"result\":0,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"result\":0,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"result\":0,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"}]', '2018-06-22 13:32:17', '2018-06-25 16:51:02');
INSERT INTO `order_sign` VALUES ('64', '64', '技术部经理', '[{\"date\":\"2018-06-25 16:49:00\",\"result\":1,\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"comment\":\"1\\n\",\"user\":\"王铁锋\"},{\"date\":\"\",\"result\":0,\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"result\":0,\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"result\":0,\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"result\":0,\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"result\":0,\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"result\":0,\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"},{\"date\":\"\",\"result\":0,\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"comment\":\"\",\"user\":\"\"}]', '2018-06-23 08:52:36', '2018-06-25 16:41:03');
INSERT INTO `order_sign` VALUES ('65', '65', '销售部经理', '[{\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '2018-06-23 16:29:55', null);
INSERT INTO `order_sign` VALUES ('66', '66', '销售部经理', '[{\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '2018-06-23 16:35:15', null);
INSERT INTO `order_sign` VALUES ('67', '67', '销售部经理', '[{\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '2018-06-23 16:41:32', null);
INSERT INTO `order_sign` VALUES ('68', '68', '销售部经理', '[{\"number\":1,\"roleId\":7,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":2,\"roleId\":8,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":3,\"roleId\":12,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":4,\"roleId\":4,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":5,\"roleId\":13,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":6,\"roleId\":15,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":7,\"roleId\":14,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"},{\"number\":8,\"roleId\":6,\"signType\":\"需求单签核\",\"date\":\"\",\"user\":\"\",\"result\":0,\"comment\":\"\"}]', '2018-06-23 16:49:05', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of process_record
-- ----------------------------
INSERT INTO `process_record` VALUES ('7', '31', '4', '[{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-1,\"to\":-8,\"points\":[209.99999999999994,88.6046511627907,209.99999999999994,98.6046511627907,209.99999999999994,99.40232557543489,209.63748168945312,99.40232557543489,209.63748168945312,100.19999998807907,209.63748168945312,110.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-8,\"to\":-16,\"points\":[209.63748168945312,143.07544859647751,209.63748168945312,153.07544859647751,209.63748168945312,155.03772429823874,209.234375,155.03772429823874,209.234375,157,209.234375,167]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-16,\"to\":-5,\"points\":[209.234375,199.87544860839844,209.234375,209.87544860839844,209.234375,212.03772429823874,209.63748168945312,212.03772429823874,209.63748168945312,214.19999998807907,209.63748168945312,224.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-8,\"to\":-12,\"points\":[209.63748168945312,143.07544859647751,209.63748168945312,153.07544859647751,209.63748168945312,156,52.234375,156,52.234375,230,52.234375,240]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-7,\"to\":-6,\"points\":[332.6374816894531,252.07544859647751,332.6374816894531,262.0754485964775,332.6374816894531,260,332.6374816894531,260,332.6374816894531,316,210.63748168945312,316,210.63748168945312,339.19999998807907,210.63748168945312,349.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-2,\"to\":-4,\"points\":[210.234375,707.8754486083984,210.234375,717.8754486083984,210.234375,723.6715686546922,210.00000000000003,723.6715686546922,210.00000000000003,729.467688700986,210.00000000000003,739.467688700986]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-18,\"to\":-2,\"points\":[210.234375,652.8754486083984,210.234375,662.8754486083984,210.234375,663.9377243041993,210.234375,663.9377243041993,210.234375,665,210.234375,675]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-6,\"to\":-9,\"points\":[210.63748168945312,382.0754485964775,210.63748168945312,392.0754485964775,210.63748168945312,412.53772429823874,305.234375,412.53772429823874,305.234375,433,305.234375,443]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-8,\"to\":-7,\"points\":[209.63748168945312,143.07544859647751,209.63748168945312,153.07544859647751,209.63748168945312,156,332.6374816894531,156,332.6374816894531,209.19999998807907,332.6374816894531,219.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-5,\"to\":-19,\"points\":[209.63748168945312,257.0754485964775,209.63748168945312,267.0754485964775,209.63748168945312,267.0754485964775,209.63748168945312,265.19999998807907,209.63748168945312,265.19999998807907,209.63748168945312,275.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-19,\"to\":-6,\"points\":[209.63748168945312,308.0754485964775,209.63748168945312,318.0754485964775,209.63748168945312,328.6377242922783,210.63748168945312,328.6377242922783,210.63748168945312,339.19999998807907,210.63748168945312,349.19999998807907]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-15,\"to\":-18,\"points\":[210.078125,560.8754486083984,210.078125,570.8754486083984,210.078125,590.4377243041993,210.234375,590.4377243041993,210.234375,610,210.234375,620]},{\"fromPort\":\"B\",\"toPort\":\"R\",\"from\":-9,\"to\":-15,\"points\":[305.234375,475.8754486083984,305.234375,485.8754486083984,305.234375,544.4377243041993,281.5662384033203,544.4377243041993,257.8981018066406,544.4377243041993,247.89810180664062,544.4377243041993]},{\"fromPort\":\"B\",\"toPort\":\"L\",\"from\":-12,\"to\":-15,\"points\":[52.234375,272.8754486083984,52.234375,282.8754486083984,52.234375,544.4377243041993,107.24626159667969,544.4377243041993,162.25814819335938,544.4377243041993,172.25814819335938,544.4377243041993]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-6,\"to\":-13,\"points\":[210.63748168945312,382.0754485964775,210.63748168945312,392.0754485964775,210.63748168945312,409.53772429823874,210.234375,409.53772429823874,210.234375,427,210.234375,437]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-13,\"to\":-15,\"points\":[210.234375,469.8754486083984,210.234375,479.8754486083984,210.234375,498.9377243041992,210.078125,498.9377243041992,210.078125,518,210.078125,528]},{\"fromPort\":\"B\",\"toPort\":\"T\",\"from\":-6,\"to\":-11,\"points\":[210.63748168945312,382.0754485964775,210.63748168945312,392.0754485964775,210.63748168945312,412.53772429823874,120.234375,412.53772429823874,120.234375,433,120.234375,443]},{\"fromPort\":\"B\",\"toPort\":\"L\",\"from\":-11,\"to\":-15,\"points\":[120.234375,475.8754486083984,120.234375,485.8754486083984,120.234375,544.4377243041993,140.90625,544.4377243041993,161.578125,544.4377243041993,171.578125,544.4377243041993]}]', '[{\"category\":\"Start\",\"key\":\"-1\",\"loc\":\"209.99999999999997 44\",\"text\":\"开始\"},{\"category\":\"End\",\"key\":\"-4\",\"loc\":\"210.00000000000003 739.467688700986\",\"text\":\"结束\"},{\"beginTime\":\"2018-06-23 08:00:30\",\"endTime\":\"2018-06-23 13:35:20\",\"key\":\"-8\",\"leader\":\"何赵军\",\"loc\":\"209.63748168945312 110.19999998807907\",\"taskStatus\":\"6\",\"text\":\"下轴安装\",\"workList\":\"韩先成, 黄刚, 陈益锋, 宣浩龙, 徐迪, 余晖\"},{\"beginTime\":\"2018-06-25 14:00:55\",\"endTime\":\"2018-06-25 15:11:22\",\"key\":\"-5\",\"leader\":\"张斌\",\"loc\":\"209.63748168945312 224.19999998807907\",\"taskStatus\":\"6\",\"text\":\"针杆架安装\",\"workList\":\"徐银风, 陶百伟\"},{\"beginTime\":\"2018-06-23 16:32:15\",\"endTime\":\"2018-06-24 10:36:40\",\"key\":\"-7\",\"leader\":\"金少军\",\"loc\":\"332.6374816894531 219.19999998807907\",\"taskStatus\":\"6\",\"text\":\"驱动安装\",\"workList\":\"毛杭斌, 伍瑞林, 宣汉江, 魏叶威, 何天钢\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-6\",\"leader\":\"\",\"loc\":\"210.6374816894531 349.199999988079\",\"taskStatus\":\"1\",\"text\":\"台板安装\",\"workList\":\"\"},{\"beginTime\":\"2018-06-24 16:28:42\",\"endTime\":\"2018-06-25 09:23:10\",\"key\":\"-16\",\"leader\":\"王飞\",\"loc\":\"209.234375 167\",\"taskStatus\":\"6\",\"text\":\"上轴安装\",\"workList\":\"何海潮, 郑锴, 方泽锋, 章钟铭\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-9\",\"leader\":\"\",\"loc\":\"305.234375 443\",\"taskStatus\":\"1\",\"text\":\"电控安装\",\"workList\":\"\"},{\"beginTime\":\"2018-06-25 16:23:37\",\"endTime\":\"2018-06-25 16:51:11\",\"key\":\"-12\",\"leader\":\"金陈飞\",\"loc\":\"52.234375 240\",\"taskStatus\":\"6\",\"text\":\"线架安装\",\"workList\":\"郭建忠\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-2\",\"leader\":\"\",\"loc\":\"210.234375 675\",\"taskStatus\":\"0\",\"text\":\"出厂检验\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-18\",\"leader\":\"\",\"loc\":\"210.234375 620\",\"taskStatus\":\"0\",\"text\":\"装置安装\",\"workList\":\"\"},{\"beginTime\":\"2018-06-25 15:11:18\",\"endTime\":\"\",\"key\":\"-19\",\"leader\":\"\",\"loc\":\"209.63748168945312 275.19999998807907\",\"taskStatus\":\"2\",\"text\":\"剪线安装\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-15\",\"leader\":\"\",\"loc\":\"210.078125 528\",\"taskStatus\":\"0\",\"text\":\"测试调试\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-11\",\"leader\":\"\",\"loc\":\"120.234375 443\",\"taskStatus\":\"0\",\"text\":\"针杆架安装\",\"workList\":\"\"},{\"beginTime\":\"\",\"endTime\":\"\",\"key\":\"-13\",\"leader\":\"\",\"loc\":\"210.234375 437\",\"taskStatus\":\"0\",\"text\":\"驱动安装\",\"workList\":\"\"}]', '2018-06-22 09:55:21', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_plan
-- ----------------------------
INSERT INTO `task_plan` VALUES ('71', '71', '1', '2018-06-23 00:00:00', null, '169', '2018-06-22 09:55:35', null);
INSERT INTO `task_plan` VALUES ('83', '72', '1', '2018-06-24 00:00:00', null, '169', '2018-06-23 15:09:18', null);
INSERT INTO `task_plan` VALUES ('84', '73', '1', '2018-06-24 00:00:00', null, '169', '2018-06-23 15:09:18', null);
INSERT INTO `task_plan` VALUES ('85', '75', '1', '2018-06-24 00:00:00', null, '169', '2018-06-23 15:09:18', null);
INSERT INTO `task_plan` VALUES ('86', '77', '1', '2018-06-24 00:00:00', null, '169', '2018-06-23 15:09:18', null);
INSERT INTO `task_plan` VALUES ('87', '74', '1', '2018-06-26 00:00:00', null, '169', '2018-06-25 07:41:06', null);
INSERT INTO `task_plan` VALUES ('88', '76', '1', '2018-06-26 00:00:00', null, '169', '2018-06-25 07:41:06', null);
INSERT INTO `task_plan` VALUES ('89', '80', '1', '2018-06-26 00:00:00', null, '169', '2018-06-25 07:41:06', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_record
-- ----------------------------
INSERT INTO `task_record` VALUES ('71', '下轴安装', '7', '-8', '何赵军', '韩先成, 黄刚, 陈益锋, 宣浩龙, 徐迪, 余晖', '6', '2018-06-23 08:00:30', '2018-06-23 13:35:20', '2018-06-23 13:35:20', '2018-06-23 13:35:20');
INSERT INTO `task_record` VALUES ('72', '针杆架安装', '7', '-5', '张斌', '徐银风, 陶百伟', '6', '2018-06-25 14:00:55', '2018-06-25 15:11:22', '2018-06-25 15:11:22', '2018-06-25 15:11:22');
INSERT INTO `task_record` VALUES ('73', '驱动安装', '7', '-7', '金少军', '毛杭斌, 伍瑞林, 宣汉江, 魏叶威, 何天钢', '6', '2018-06-23 16:32:15', '2018-06-24 10:36:40', '2018-06-24 10:36:40', '2018-06-24 10:36:40');
INSERT INTO `task_record` VALUES ('74', '台板安装', '7', '-6', null, null, '1', null, null, null, null);
INSERT INTO `task_record` VALUES ('75', '上轴安装', '7', '-16', '王飞', '何海潮, 郑锴, 方泽锋, 章钟铭', '6', '2018-06-24 16:28:42', '2018-06-25 09:23:10', '2018-06-25 09:23:10', '2018-06-25 09:23:10');
INSERT INTO `task_record` VALUES ('76', '电控安装', '7', '-9', null, null, '1', null, null, null, null);
INSERT INTO `task_record` VALUES ('77', '线架安装', '7', '-12', '金陈飞', '郭建忠', '6', '2018-06-25 16:23:37', '2018-06-25 16:51:11', '2018-06-25 16:51:11', '2018-06-25 16:51:11');
INSERT INTO `task_record` VALUES ('78', '出厂检验', '7', '-2', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('79', '装置安装', '7', '-18', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('80', '剪线安装', '7', '-19', null, null, '2', null, null, null, null);
INSERT INTO `task_record` VALUES ('81', '测试调试', '7', '-15', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('82', '针杆架安装', '7', '-11', null, null, '0', null, null, null, null);
INSERT INTO `task_record` VALUES ('83', '驱动安装', '7', '-13', null, null, '0', null, null, null, null);

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
  PRIMARY KEY (`id`),
  KEY `idx_user_role_id` (`role_id`) USING BTREE,
  KEY `fk_user_group_id` (`group_id`),
  CONSTRAINT `fk_user_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', 'admin', '1', 'sinsim', '0', null, '1');
INSERT INTO `user` VALUES ('16', '谢侃', '谢侃', '9', 'sinsim', '0', '外贸一部', '1');
INSERT INTO `user` VALUES ('17', '郑海龙', '郑海龙', '4', 'sinsim', '0', '', '1');
INSERT INTO `user` VALUES ('25', '徐锡康', '徐锡康', '3', 'sinsim', '14', '', '1');
INSERT INTO `user` VALUES ('28', '孟佳飞', '孟佳飞', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('29', '王国娜', '王国娜', '3', 'sinsim', '8', '', '1');
INSERT INTO `user` VALUES ('30', '李霞', '李霞', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('31', '宣小华', '宣小华', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('32', '何承凤', '何承凤', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('33', '陈小英', '陈小英', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('34', '王丹飞', '王丹飞', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('35', '骆钰洁', '骆钰洁', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('36', '陈秀琴', '陈秀琴', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('37', '赵燕红', '赵燕红', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('38', '赵丽霞', '赵丽霞', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('39', '俞红萍', '俞红萍', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('40', '孙兰华', '孙兰华', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('41', '郑国平', '郑国平', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('42', '饶桂枝', '饶桂枝', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('43', '骆利淼', '骆利淼', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('44', '胡尚连', '胡尚连', '17', 'sinsim', '8', null, '1');
INSERT INTO `user` VALUES ('45', '何赵军', '何赵军', '3', 'sinsim', '2', '', '1');
INSERT INTO `user` VALUES ('46', '王飞', '王飞', '3', 'sinsim', '1', '', '1');
INSERT INTO `user` VALUES ('47', '陈炯苗', '陈炯苗', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('48', '斯校军', '斯校军', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('49', '张文龙', '张文龙', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('50', '何海潮', '何海潮', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('51', '章方斌', '章方斌', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('52', '张强', '张强', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('53', '郑锴', '郑锴', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('54', '方泽锋', '方泽锋', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('55', '章钟铭', '章钟铭', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('56', '王艳', '王艳', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('57', '王荣燕', '王荣燕', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('58', '张叶峰', '张叶峰', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('59', '贺伟', '贺伟', '17', 'sinsim', '1', null, '1');
INSERT INTO `user` VALUES ('60', '陈镇波', '陈镇波', '17', 'sinsim', '1', '', '1');
INSERT INTO `user` VALUES ('61', '陆铮', '陆铮', '17', 'sinsim', '2', null, '1');
INSERT INTO `user` VALUES ('62', '宣浩龙', '宣浩龙', '17', 'sinsim', '2', null, '1');
INSERT INTO `user` VALUES ('63', '韩先成', '韩先成', '17', 'sinsim', '2', null, '1');
INSERT INTO `user` VALUES ('64', '陈铁威', '陈铁威', '17', 'sinsim', '2', null, '1');
INSERT INTO `user` VALUES ('65', '马越柯', '马越柯', '17', 'sinsim', '2', null, '1');
INSERT INTO `user` VALUES ('66', '徐佳龙', '徐佳龙', '17', 'sinsim', '2', null, '1');
INSERT INTO `user` VALUES ('67', '陈益锋', '陈益锋', '17', 'sinsim', '2', null, '1');
INSERT INTO `user` VALUES ('68', '章建达', '章建达', '17', 'sinsim', '2', null, '1');
INSERT INTO `user` VALUES ('69', '徐迪', '徐迪', '17', 'sinsim', '2', null, '1');
INSERT INTO `user` VALUES ('70', '王君', '王君', '17', 'sinsim', '2', null, '1');
INSERT INTO `user` VALUES ('71', '郑茗友', '郑茗友', '17', 'sinsim', '2', null, '1');
INSERT INTO `user` VALUES ('72', '王阿妹', '王阿妹', '17', 'sinsim', '2', null, '1');
INSERT INTO `user` VALUES ('76', '张斌', '张斌', '3', 'sinsim', '7', '', '1');
INSERT INTO `user` VALUES ('77', '徐银风', '徐银风', '17', 'sinsim', '7', null, '1');
INSERT INTO `user` VALUES ('78', '何洪锋', '何洪锋', '17', 'sinsim', '7', null, '1');
INSERT INTO `user` VALUES ('79', '韩海强', '韩海强', '17', 'sinsim', '7', null, '1');
INSERT INTO `user` VALUES ('80', '阮鑫钢', '阮鑫钢', '17', 'sinsim', '7', null, '1');
INSERT INTO `user` VALUES ('81', '袁伯钿', '袁伯钿', '17', 'sinsim', '7', null, '1');
INSERT INTO `user` VALUES ('82', '杨瑞', '杨瑞', '17', 'sinsim', '7', null, '1');
INSERT INTO `user` VALUES ('83', '卓欢其', '卓欢其', '17', 'sinsim', '7', null, '1');
INSERT INTO `user` VALUES ('84', '郑国涛', '郑国涛', '17', 'sinsim', '7', null, '1');
INSERT INTO `user` VALUES ('85', '魏权峰', '魏权峰', '17', 'sinsim', '7', null, '1');
INSERT INTO `user` VALUES ('86', '方颖丰', '方颖丰', '17', 'sinsim', '7', null, '1');
INSERT INTO `user` VALUES ('87', '陶百伟', '陶百伟', '17', 'sinsim', '7', null, '1');
INSERT INTO `user` VALUES ('88', '楼建锋', '楼建锋', '17', 'sinsim', '7', null, '1');
INSERT INTO `user` VALUES ('90', '章瑜', '章瑜', '17', 'sinsim', '7', '', '1');
INSERT INTO `user` VALUES ('91', '蔡辉辉', '蔡辉辉', '17', 'sinsim', '7', '', '1');
INSERT INTO `user` VALUES ('92', '王烜波', '王烜波', '17', 'sinsim', '12', '', '1');
INSERT INTO `user` VALUES ('93', '袁涛', '袁涛', '17', 'sinsim', '7', '', '1');
INSERT INTO `user` VALUES ('94', '马雄伟', '马雄伟', '17', 'sinsim', '7', '', '1');
INSERT INTO `user` VALUES ('95', '金少军', '金少军', '3', 'sinsim', '3', '', '1');
INSERT INTO `user` VALUES ('96', '张海中', '张海中', '17', 'sinsim', '3', '', '1');
INSERT INTO `user` VALUES ('97', '余斌江', '余斌江', '17', 'sinsim', '3', '', '1');
INSERT INTO `user` VALUES ('98', '毛杭斌', '毛杭斌', '17', 'sinsim', '3', null, '1');
INSERT INTO `user` VALUES ('100', '伍瑞林', '伍瑞林', '17', 'sinsim', '3', null, '1');
INSERT INTO `user` VALUES ('101', '李润', '李润', '17', 'sinsim', '3', '', '1');
INSERT INTO `user` VALUES ('102', '宣汉江', '宣汉江', '17', 'sinsim', '3', null, '1');
INSERT INTO `user` VALUES ('103', '魏叶威', '魏叶威', '17', 'sinsim', '3', null, '1');
INSERT INTO `user` VALUES ('104', '陈天龙', '陈天龙', '17', 'sinsim', '3', '', '1');
INSERT INTO `user` VALUES ('107', '何天钢', '何天钢', '17', 'sinsim', '3', null, '1');
INSERT INTO `user` VALUES ('108', '周志祥', '周志祥', '3', 'sinsim', '4', '', '1');
INSERT INTO `user` VALUES ('109', '宣言梁', '宣言梁', '17', 'sinsim', '4', null, '1');
INSERT INTO `user` VALUES ('110', '郭海强', '郭海强', '17', 'sinsim', '4', null, '1');
INSERT INTO `user` VALUES ('111', '龙江', '龙江', '17', 'sinsim', '4', null, '1');
INSERT INTO `user` VALUES ('112', '吴凡', '吴凡', '17', 'sinsim', '4', null, '1');
INSERT INTO `user` VALUES ('113', '付中亚', '付中亚', '17', 'sinsim', '4', null, '1');
INSERT INTO `user` VALUES ('114', '舒孝欢', '舒孝欢', '17', 'sinsim', '4', null, '1');
INSERT INTO `user` VALUES ('115', '高欢欢', '高欢欢', '17', 'sinsim', '4', null, '1');
INSERT INTO `user` VALUES ('116', '曾祥平', '曾祥平', '17', 'sinsim', '4', null, '1');
INSERT INTO `user` VALUES ('117', '方毅', '方毅', '3', 'sinsim', '5', '', '1');
INSERT INTO `user` VALUES ('118', '丁文', '丁文', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('119', '许金龙', '许金龙', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('120', '陈钱栋', '陈钱栋', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('121', '蒋峰', '蒋峰', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('122', '刘伟', '刘伟', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('123', '汤剑', '汤剑', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('124', '周光焱', '周光焱', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('125', '邬润杰', '邬润杰', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('126', '陈可女', '陈可女', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('127', '姚远平', '姚远平', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('128', '杨海军', '杨海军', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('129', '毛锡伟', '毛锡伟', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('130', '李坤鹏', '李坤鹏', '17', 'sinsim', '5', null, '1');
INSERT INTO `user` VALUES ('131', '王新全', '王新全', '3', 'sinsim', '16', '', '1');
INSERT INTO `user` VALUES ('132', '陈益锋2', '陈益锋2', '17', 'sinsim', '9', null, '1');
INSERT INTO `user` VALUES ('133', '余铮宇', '余铮宇', '17', 'sinsim', '9', null, '1');
INSERT INTO `user` VALUES ('134', '宣焕强', '宣焕强', '17', 'sinsim', '9', null, '1');
INSERT INTO `user` VALUES ('135', '阮少杰', '阮少杰', '17', 'sinsim', '9', null, '1');
INSERT INTO `user` VALUES ('136', '钱盛', '钱盛', '17', 'sinsim', '9', null, '1');
INSERT INTO `user` VALUES ('137', '章正国', '章正国', '17', 'sinsim', '9', null, '1');
INSERT INTO `user` VALUES ('138', '周桂新', '周桂新', '17', 'sinsim', '9', null, '1');
INSERT INTO `user` VALUES ('139', '侯棋', '侯棋', '17', 'sinsim', '9', null, '1');
INSERT INTO `user` VALUES ('140', '宣锡阳', '宣锡阳', '17', 'sinsim', '9', null, '1');
INSERT INTO `user` VALUES ('141', '严玮杰', '严玮杰', '17', 'sinsim', '9', null, '1');
INSERT INTO `user` VALUES ('142', '王朴卡', '王朴卡', '3', 'sinsim', '10', '', '1');
INSERT INTO `user` VALUES ('143', '胡夏飞', '胡夏飞', '17', 'sinsim', '10', null, '1');
INSERT INTO `user` VALUES ('144', '徐孝栋', '徐孝栋', '17', 'sinsim', '10', null, '1');
INSERT INTO `user` VALUES ('145', '方陈勇', '方陈勇', '17', 'sinsim', '10', null, '1');
INSERT INTO `user` VALUES ('146', '郭忠梁', '郭忠梁', '17', 'sinsim', '10', null, '1');
INSERT INTO `user` VALUES ('147', '陈燕丰', '陈燕丰', '17', 'sinsim', '10', null, '1');
INSERT INTO `user` VALUES ('148', '卓永福', '卓永福', '17', 'sinsim', '10', null, '1');
INSERT INTO `user` VALUES ('149', '吕翔', '吕翔', '17', 'sinsim', '10', null, '1');
INSERT INTO `user` VALUES ('150', '王威', '王威', '17', 'sinsim', '10', null, '1');
INSERT INTO `user` VALUES ('151', '杨忠', '杨忠', '17', 'sinsim', '10', null, '1');
INSERT INTO `user` VALUES ('152', '楼飞翔', '楼飞翔', '17', 'sinsim', '10', null, '1');
INSERT INTO `user` VALUES ('153', '吴明枝', '吴明枝', '17', 'sinsim', '10', null, '1');
INSERT INTO `user` VALUES ('154', '蒋立奇', '蒋立奇', '16', 'sinsim', '0', '', '1');
INSERT INTO `user` VALUES ('167', '郑培军', '郑培军', '2', '666666', '0', '', '1');
INSERT INTO `user` VALUES ('168', '王杰', '王杰', '17', 'sinsim', '8', '', '1');
INSERT INTO `user` VALUES ('169', '吕春蓓', '吕春蓓', '2', 'sinsim', '0', '', '1');
INSERT INTO `user` VALUES ('170', '杨金魁', '杨金魁', '2', 'sinsim', '0', '', '1');
INSERT INTO `user` VALUES ('171', '斯华锋', '斯华锋', '12', '1', '0', '', '1');
INSERT INTO `user` VALUES ('172', 'victor', '彭胜利', '1', 'sheng.5566', null, null, '1');
INSERT INTO `user` VALUES ('173', '斯雯', '斯雯', '9', 'sinsim', '0', '外贸二部', '1');
INSERT INTO `user` VALUES ('174', '周婷青', '周婷青', '9', 'sinsim', '0', '外贸二部', '1');
INSERT INTO `user` VALUES ('175', '曹建挺', '曹建挺', '7', 'caocao', '0', '外贸二部', '1');
INSERT INTO `user` VALUES ('177', '姚娟芝', '姚娟芝', '9', '673101', '0', '外贸一部', '1');
INSERT INTO `user` VALUES ('178', '陈佳枝', '陈佳枝', '9', 'sinsim', '0', '外贸一部', '1');
INSERT INTO `user` VALUES ('179', '骆晓军', '骆晓军', '7', 'lyx0123456', '0', '外贸一部', '1');
INSERT INTO `user` VALUES ('181', '胡嘉亮', '胡嘉亮', '3', 'sinsim', '9', '', '1');
INSERT INTO `user` VALUES ('182', '王铁锋', '王铁锋', '7', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('183', '郑洁', '郑洁', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('184', '张仕均', '张仕均', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('185', '季谢魏', '季谢魏', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('186', '陈徐彬', '陈徐彬', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('187', '陶炎海', '陶炎海', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('188', '陈洁', '陈洁', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('189', '邵理国', '邵理国', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('190', '周国勇', '周国勇', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('191', '方建永', '方建永', '9', '方建永', null, '内贸部', '1');
INSERT INTO `user` VALUES ('192', '蔡天明', '蔡天明', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('193', '邱隆海', '邱隆海', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('194', '孙情', '孙情', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('195', '何绍华', '何绍华', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('196', '郭洪勇', '郭洪勇', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('197', '张烝', '张烝', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('198', '徐臣', '徐臣', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('199', '陈昌虎', '陈昌虎', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('200', '刘木清', '刘木清', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('201', '方洪生', '方洪生', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('202', '魏建忠', '魏建忠', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('203', '方鑫锋', '方鑫锋', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('204', '吴捷桁', '吴捷桁', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('205', '徐保卫', '徐保卫', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('206', '王海东', '王海东', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('207', '张汉钢', '张汉钢', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('208', '王建锋', '王建锋', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('209', '周立峰', '周立峰', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('210', '冯保锋', '冯保锋', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('211', '屈仲华', '屈仲华', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('212', '高耀叶', '高耀叶', '9', 'sinsim', '0', '内贸部', '1');
INSERT INTO `user` VALUES ('213', '周琪芳', '周琪芳', '9', 'sinsim', '0', '外贸一部', '1');
INSERT INTO `user` VALUES ('214', '王海江', '王海江', '6', 'sinsim537', '0', '', '1');
INSERT INTO `user` VALUES ('215', '方炬江', '方炬江', '8', 'fjj1983', '0', '', '1');
INSERT INTO `user` VALUES ('216', '汤能萍', '汤能萍', '14', '13018807138hmy', '0', '', '1');
INSERT INTO `user` VALUES ('217', '何璐洁', '何璐洁', '15', '136065', '0', '', '1');
INSERT INTO `user` VALUES ('218', '袁海琼', '袁海琼', '15', 'sinsim', '0', '', '1');
INSERT INTO `user` VALUES ('219', '楼叶平', '楼叶平', '13', '100888', '0', '', '1');
INSERT INTO `user` VALUES ('220', '何晓婧', '何晓婧', '13', 'sinsim', '0', '', '1');
INSERT INTO `user` VALUES ('223', '张斌2', '张斌2', '3', 'sinsim', '12', '', '1');
INSERT INTO `user` VALUES ('224', '金少军2', '金少军2', '3', 'sinsim', '19', '', '1');
INSERT INTO `user` VALUES ('225', '金陈飞', '金陈飞', '3', 'sinsim', '18', '', '1');
INSERT INTO `user` VALUES ('226', '郭建忠', '郭建忠', '17', 'sinsim', '18', '', '1');
INSERT INTO `user` VALUES ('227', '徐锡康2', '徐锡康2', '17', 'sinsim', '14', '', '1');
INSERT INTO `user` VALUES ('229', '郑海龙2', '郑海龙2', '12', 'sinsim', '0', '', '1');
INSERT INTO `user` VALUES ('230', '殷君可', '殷君可', '10', '12345678', '0', '', '1');
INSERT INTO `user` VALUES ('231', '余晖', '余晖', '17', 'sinsim', '2', '', '1');
INSERT INTO `user` VALUES ('232', '黄刚', '黄刚', '17', 'sinsim', '2', '', '1');
INSERT INTO `user` VALUES ('233', '张强1', '张强1', '17', 'sinsim', '2', '', '1');
INSERT INTO `user` VALUES ('234', '毛陈波', '毛陈波', '5', 'sinsim', '11', '', '1');
INSERT INTO `user` VALUES ('235', '余鉴泽', '余鉴泽', '5', 'sinsim', '3', '', '1');
INSERT INTO `user` VALUES ('239', '王新全2', '王新全2', '3', 'sinsim', '17', '', '1');
INSERT INTO `user` VALUES ('240', '王煊波', '王煊波', '17', '', '7', '', '1');
