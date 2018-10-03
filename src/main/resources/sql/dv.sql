/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : dv

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 04/10/2018 03:04:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dv_category
-- ----------------------------
DROP TABLE IF EXISTS `dv_category`;
CREATE TABLE `dv_category`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父分类名称',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '类别表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dv_category
-- ----------------------------
INSERT INTO `dv_category` VALUES ('132eb644298221f9543a3864508bb435', '111', NULL, '\\番剧\\连载动画\\111', '6c2399deb4c4d0827f4c3735581ce714', '2018-10-04 03:03:11');
INSERT INTO `dv_category` VALUES ('243c9dc1d65c3bcde9eca0b809a912da', '舞蹈', NULL, '\\舞蹈', NULL, '2018-10-04 03:03:11');
INSERT INTO `dv_category` VALUES ('4827e531099e6b58edd8a73fa3169d81', '三次元', NULL, '\\舞蹈\\三次元', '243c9dc1d65c3bcde9eca0b809a912da', '2018-10-04 03:03:11');
INSERT INTO `dv_category` VALUES ('6c2399deb4c4d0827f4c3735581ce714', '连载动画', NULL, '\\番剧\\连载动画', '8947784358c3e3122c40438001459a44', '2018-10-04 03:03:11');
INSERT INTO `dv_category` VALUES ('86828051991ac00605239c3bb3c4f3b7', 'MAD', NULL, '\\动画\\MAD', 'f976283a4e8f5d0b6ffc6b091dd4be66', '2018-10-04 03:03:10');
INSERT INTO `dv_category` VALUES ('8903f254b1126acf8c2c7e10f85e53fc', '宅舞', NULL, '\\舞蹈\\宅舞', '243c9dc1d65c3bcde9eca0b809a912da', '2018-10-04 03:03:11');
INSERT INTO `dv_category` VALUES ('8947784358c3e3122c40438001459a44', '番剧', NULL, '\\番剧', NULL, '2018-10-04 03:03:10');
INSERT INTO `dv_category` VALUES ('91d3efa6f73bdc7d8710e674edeb3e6c', '完结动画', NULL, '\\番剧\\完结动画', '8947784358c3e3122c40438001459a44', '2018-10-04 03:03:10');
INSERT INTO `dv_category` VALUES ('f976283a4e8f5d0b6ffc6b091dd4be66', '动画', NULL, '\\动画', NULL, '2018-10-04 03:03:10');
INSERT INTO `dv_category` VALUES ('fede1829decdb60eebee1eddeef3d445', '综合', NULL, '\\动画\\综合', 'f976283a4e8f5d0b6ffc6b091dd4be66', '2018-10-04 03:03:10');

-- ----------------------------
-- Table structure for dv_content
-- ----------------------------
DROP TABLE IF EXISTS `dv_content`;
CREATE TABLE `dv_content`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `category` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属分类',
  `parent_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父路径',
  `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `size` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '大小',
  `thumbnail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩略图路径',
  `duration` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时长',
  `resolution` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分辨率',
  `frame_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帧率',
  `author` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传用户',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dc_category`(`category`) USING BTREE,
  CONSTRAINT `dc_category` FOREIGN KEY (`category`) REFERENCES `dv_category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '内容表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dv_content
-- ----------------------------
INSERT INTO `dv_content` VALUES ('301ce493081d317979e72d0ab2d02b7a', 'Dota 2 2018_5_12 14_07_31.mp4', '8903f254b1126acf8c2c7e10f85e53fc', '\\舞蹈\\宅舞', 'video', '41.20MB', NULL, '00:00:31', '1920x1080', '0.09 fps', NULL, '2018-05-12 14:08:02');
INSERT INTO `dv_content` VALUES ('9fa061d3722e5b35a669d61aff4ec8ee', 'Dota 2 2018_5_12 13_59_51.mp4', '91d3efa6f73bdc7d8710e674edeb3e6c', '\\番剧\\完结动画', 'video', '29.07MB', NULL, '00:00:21', '1920x1080', '0.13 fps', NULL, '2018-05-12 14:00:13');

-- ----------------------------
-- Table structure for sys_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_login`;
CREATE TABLE `sys_login`  (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `role_id` int(10) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login
-- ----------------------------
INSERT INTO `sys_login` VALUES ('admin', '$2a$10$/yywfCkbZdkK4Rxat/mCK.yPyvqEQ0fJ1L8dNmBK09mK48K8toqeK', 2);
INSERT INTO `sys_login` VALUES ('user', '$2a$10$/yywfCkbZdkK4Rxat/mCK.yPyvqEQ0fJ1L8dNmBK09mK48K8toqeK', 0);
INSERT INTO `sys_login` VALUES ('vip', '$2a$10$/yywfCkbZdkK4Rxat/mCK.yPyvqEQ0fJ1L8dNmBK09mK48K8toqeK', 1);

-- ----------------------------
-- Table structure for sys_setting
-- ----------------------------
DROP TABLE IF EXISTS `sys_setting`;
CREATE TABLE `sys_setting`  (
  `k` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `v` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`k`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_setting
-- ----------------------------
INSERT INTO `sys_setting` VALUES ('PICTURE_NUM', '26772');
INSERT INTO `sys_setting` VALUES ('RES_IP', 'http://192.168.100.103:4097');
INSERT INTO `sys_setting` VALUES ('RES_ROOT', 'E:\\test');
INSERT INTO `sys_setting` VALUES ('VIDEO_NUM', '5163');

SET FOREIGN_KEY_CHECKS = 1;
