
DROP DATABASE IF EXISTS shadow;
CREATE DATABASE shadow DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shadow;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;		--取消外键检查


-- ----------------------------
-- Table structure for `sys_pk_generator`
-- ----------------------------
DROP TABLE IF EXISTS `sys_pk_generator`;
CREATE TABLE `sys_pk_generator` (
  `id` INT NOT NULL,
  `gen_name` varchar(100) NOT NULL,
  `gen_value` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- ----------------------------
-- Records of sys_pk_generator
-- ----------------------------
INSERT INTO `sys_pk_generator` VALUES (1, 'sys_user',1);
INSERT INTO `sys_pk_generator` VALUES (2, 'sys_resource',1);


DROP TABLE IF EXISTS `sys_role_resource`;
DROP TABLE IF EXISTS `sys_user_role`;
-- ----------------------------
-- Table structure for `sys_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`  (
  `rid` int NOT NULL COMMENT '资源ID',
  `code` varchar(30) COMMENT '资源名称',
  `name` varchar(100) COMMENT '资源描述',
  `parentId` int COMMENT '父资源编码->菜单',
  `uri` varchar(100) COMMENT '访问地址URL',
  `resourceType` smallint COMMENT '类型 1:菜单menu 2:资源element(rest-api) 3:资源分类',
  `method` varchar(10) COMMENT '访问方式 GET POST PUT DELETE PATCH',
  `icon` varchar(100) COMMENT '图标',
  `status` smallint NULL DEFAULT 1 COMMENT '状态   1:正常、0：禁用',
  `createTime` datetime DEFAULT NOW() COMMENT '创建时间',
  `updateTime` datetime DEFAULT NOW() COMMENT '更新时间',
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB COMMENT = '资源信息表(菜单,资源)';



-- ----------------------------
-- Records of `sys_resource`
-- ----------------------------
INSERT INTO `sys_resource` VALUES (1, 'ACCOUNT_LOGIN', '用户登录', 3, '/account/login', 2, 'POST', NULL, 1, NULL, NULL);
INSERT INTO `sys_resource` VALUES (3, 'GROUP_ACCOUNT', '账户系列', 10, '', 3, 'POST', NULL, 1, NULL, NULL);
INSERT INTO `sys_resource` VALUES (4, 'USER_MAGE', '用户管理', -1, '', 1, 'POST', 'fa fa-user', 1, NULL, NULL);
INSERT INTO `sys_resource` VALUES (6, 'RESOURCE_MAGE', '资源配置', -1, '', 1, 'POST', 'fa fa-pie-chart', 1, NULL, NULL);
INSERT INTO `sys_resource` VALUES (7, 'MENU_MANAGE', '菜单管理', 6, '/index/menu', 1, 'POST', 'fa fa-th', 1, NULL, NULL);
INSERT INTO `sys_resource` VALUES (9, 'API_MANAGE', 'API管理', 6, '/index/api', 1, NULL, 'fa fa-share', 1, '2018-04-07 09:40:24', '2018-04-07 09:40:24');
INSERT INTO `sys_resource` VALUES (10, 'CATEGORY_GROUP', '分类集合(API类别请放入此集合)', -1, NULL, 3, NULL, NULL, 1, '2018-04-07 14:27:58', '2018-04-07 14:27:58');
INSERT INTO `sys_resource` VALUES (12, 'ACCOUNT_REGISTER', '用户注册', 3, '/account/register', 2, 'POST', NULL, 1, '2018-04-07 16:21:45', '2018-04-07 16:21:45');
INSERT INTO `sys_resource` VALUES (15, 'GROUP_USER', '用户系列', 10, '', 3, 'GET', NULL, 1, '2018-04-07 16:31:01', '2018-04-07 16:31:01');
INSERT INTO `sys_resource` VALUES (17, 'ROLE_MANAGE', '角色管理', 6, '/index/role', 1, NULL, 'fa fa-adjust', 1, '2018-04-08 05:36:31', '2018-04-08 05:36:31');
INSERT INTO `sys_resource` VALUES (18, 'GROUP_RESOURCE', '资源系列', 10, NULL, 3, NULL, NULL, 1, '2018-04-09 02:29:14', '2018-04-09 02:29:14');
INSERT INTO `sys_resource` VALUES (19, 'USER_ROLE_APPID', '获取对应用户角色', 15, '/user/role/*', 2, 'GET', NULL, 1, '2018-04-12 03:07:22', '2018-04-12 03:07:22');
INSERT INTO `sys_resource` VALUES (20, 'USER_LIST', '获取用户列表', 15, '/user/list', 2, 'GET', NULL, 1, '2018-04-12 03:08:30', '2018-04-12 03:08:30');
INSERT INTO `sys_resource` VALUES (21, 'USER_AUTHORITY_ROLE', '给用户授权添加角色', 15, '/user/authority/role', 2, 'POST', NULL, 1, '2018-04-12 03:15:56', '2018-04-12 03:15:56');
INSERT INTO `sys_resource` VALUES (22, 'USER_AUTHORITY_ROLE', '删除已经授权的用户角色', 15, '/user/authority/role', 2, 'DELETE', NULL, 1, '2018-04-12 03:29:03', '2018-04-12 03:29:03');
INSERT INTO `sys_resource` VALUES (23, 'RESOURCE_AUTORITYMENU', '获取用户被授权菜单', 18, '/resource/authorityMenu', 2, 'GET', NULL, 1, '2018-04-12 05:30:03', '2018-04-12 05:30:03');
INSERT INTO `sys_resource` VALUES (24, 'RESOURCE_MENUS', '获取全部菜单列', 18, '/resource/menus', 2, 'GET', NULL, 1, '2018-04-12 05:42:46', '2018-04-12 05:42:46');
INSERT INTO `sys_resource` VALUES (25, 'RESOURCE_MENU', '增加菜单', 18, '/resource/menu', 2, 'POST', NULL, 1, '2018-04-12 06:15:39', '2018-04-12 06:15:39');
INSERT INTO `sys_resource` VALUES (26, 'RESOURCE_MENU', '修改菜单', 18, '/resource/menu', 2, 'PUT', NULL, 1, '2018-04-12 06:16:35', '2018-04-12 06:16:35');
INSERT INTO `sys_resource` VALUES (27, 'RESOURCE_MENU', '删除菜单', 18, '/resource/menu', 2, 'DELETE', NULL, 1, '2018-04-12 06:17:18', '2018-04-12 06:17:18');
INSERT INTO `sys_resource` VALUES (28, 'RESOURCE_API', '获取API list', 18, '/resource/api/*/*/*', 2, 'GET', NULL, 1, '2018-04-12 06:18:02', '2018-04-12 06:18:02');
INSERT INTO `sys_resource` VALUES (29, 'RESOURCE_API', '增加API', 18, '/resource/api', 2, 'POST', NULL, 1, '2018-04-12 06:18:42', '2018-04-12 06:18:42');
INSERT INTO `sys_resource` VALUES (30, 'RESOURCE_API', '修改API', 18, '/resource/api', 2, 'PUT', NULL, 1, '2018-04-12 06:19:32', '2018-04-12 06:19:32');
INSERT INTO `sys_resource` VALUES (31, 'RESOURCE_API', '删除API', 18, '/resource/api', 2, 'DELETE', NULL, 1, '2018-04-12 06:20:03', '2018-04-12 06:20:03');
INSERT INTO `sys_resource` VALUES (32, 'GROUP_ROLE', '角色系列', 10, NULL, 3, NULL, NULL, 1, '2018-04-12 06:22:02', '2018-04-12 06:22:02');
INSERT INTO `sys_resource` VALUES (33, 'ROLE_USER', '获取角色关联用户列表', 32, '/role/user/*/*/*', 2, 'GET', NULL, 1, '2018-04-12 06:22:59', '2018-04-12 06:22:59');
INSERT INTO `sys_resource` VALUES (34, 'ROLE_USER', '获取角色未关联用户列表', 32, '/role/user/-/*/*/*', 2, 'GET', NULL, 1, '2018-04-12 06:24:09', '2018-04-12 06:24:09');
INSERT INTO `sys_resource` VALUES (35, 'ROLE_API', '获取角色关联API资源', 32, '/role/api/*/*/*', 2, 'GET', NULL, 1, '2018-04-12 06:25:32', '2018-04-12 06:25:32');
INSERT INTO `sys_resource` VALUES (36, 'ROLE_API', '获取角色未关联API资源', 32, '/role/api/-/*/*/*', 2, 'GET', NULL, 1, '2018-04-12 06:26:12', '2018-04-12 06:26:12');
INSERT INTO `sys_resource` VALUES (37, 'ROLE_MENU', '获取角色关联菜单资源', 32, '/role/menu/*/*/*', 2, 'GET', NULL, 1, '2018-04-12 06:27:20', '2018-04-12 06:27:20');
INSERT INTO `sys_resource` VALUES (38, 'ROLE_MENU', '获取角色未关联菜单资源', 32, '/role/menu/-/*/*/*', 2, 'GET', NULL, 1, '2018-04-12 06:27:56', '2018-04-12 06:27:56');
INSERT INTO `sys_resource` VALUES (39, 'ROLE_AUTHORITY_RESOURCE', '授权资源给角色', 32, '/role/authority/resource', 2, 'POST', NULL, 1, '2018-04-12 06:29:45', '2018-04-12 06:29:45');
INSERT INTO `sys_resource` VALUES (40, 'ROLE_AUTHORITY_RESOURCE', '删除角色的授权资源', 32, '/role/authority/resource', 2, 'DELETE', NULL, 1, '2018-04-12 06:31:12', '2018-04-12 06:31:12');
INSERT INTO `sys_resource` VALUES (41, 'ROLE', '获取角色LIST', 32, '/role/*/*', 2, 'GET', NULL, 1, '2018-04-12 06:32:34', '2018-04-12 06:32:34');
INSERT INTO `sys_resource` VALUES (42, 'ROLE', '添加角色', 32, '/role', 2, 'POST', NULL, 1, '2018-04-12 06:33:25', '2018-04-12 06:33:25');
INSERT INTO `sys_resource` VALUES (43, 'USER', '更新角色', 32, '/role', 2, 'PUT', NULL, 1, '2018-04-12 06:34:27', '2018-04-12 06:34:27');
INSERT INTO `sys_resource` VALUES (44, 'ROLE', '删除角色', 32, '/role', 2, 'DELETE', NULL, 1, '2018-04-12 06:35:15', '2018-04-12 06:35:15');
INSERT INTO `sys_resource` VALUES (45, 'LOG_WATCH', '日志记录', 4, '/index/log', 1, NULL, 'fa fa-rss-square', 1, '2018-04-22 08:12:24', '2018-04-22 08:12:24');

update sys_pk_generator set gen_value=gen_value+45 where gen_name='sys_resource';

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `rid` varchar(40) NOT NULL COMMENT '角色ID',
  `role` varchar(100) COMMENT '角色名称',
  `description` varchar(255) COMMENT '角色描述',
  `status` smallint DEFAULT 1 COMMENT '状态   1:正常、9：禁用',
  `createTime` datetime DEFAULT NOW() COMMENT '创建时间',
  `updateTime` datetime DEFAULT NOW() COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE = InnoDB COMMENT = '角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`(rid,role) VALUES ('admin', '管理员角色');
INSERT INTO `sys_role`(rid,role) VALUES ('role_user', '用户角色');
INSERT INTO `sys_role`(rid,role) VALUES ('role_guest', '访客角色');
INSERT INTO `sys_role`(rid,role) VALUES ('role_anon', '非角色');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `uid` bigint NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `salt` varchar(100) NOT NULL,
  `email` varchar(100) UNIQUE DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `tel` varchar(20) DEFAULT NULL,
  `addr` varchar(200) DEFAULT NULL,
  `regtime` timestamp NOT NULL DEFAULT NOW(),
  `disabled` tinyint unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '管理员', 'admin', 'salt', 'sunwz_hrb@126.com', '', '','',NOW(), 0);


-- ----------------------------
-- Table structure for `sys_role_resource`
-- ----------------------------
CREATE TABLE `sys_role_resource` (
  `rid` varchar(40) NOT NULL,
  `pid` int NOT NULL,
  PRIMARY KEY (`rid`,`pid`),
  KEY `Reference_3_FK` (`rid`),
  KEY `Reference_4_FK` (`pid`),
  CONSTRAINT `FK_Reference_3` FOREIGN KEY (`rid`) REFERENCES `sys_role` (`rid`),
  CONSTRAINT `FK_Reference_4` FOREIGN KEY (`pid`) REFERENCES `sys_resource` (`rid`)
) ENGINE=InnoDB;


-- ----------------------------
-- Records of sys_role_resource
-- ----------------------------
INSERT INTO `sys_role_resource` (`rid`,`pid`) VALUES ('admin',1);
INSERT INTO `sys_role_resource` (`rid`,`pid`) VALUES ('admin',3);
INSERT INTO `sys_role_resource` (`rid`,`pid`) VALUES ('admin',4);
INSERT INTO `sys_role_resource` (`rid`,`pid`) VALUES ('admin',6);
INSERT INTO `sys_role_resource` (`rid`,`pid`) VALUES ('admin',7);
INSERT INTO `sys_role_resource` (`rid`,`pid`) VALUES ('admin',9);
INSERT INTO `sys_role_resource` (`rid`,`pid`) VALUES ('admin',10);
INSERT INTO `sys_role_resource` (`rid`,`pid`) VALUES ('admin',12);
INSERT INTO `sys_role_resource` (`rid`,`pid`) VALUES ('admin',15);
INSERT INTO `sys_role_resource` (`rid`,`pid`) VALUES ('admin',17);


-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
CREATE TABLE `sys_user_role` (
  `uid` bigint NOT NULL,
  `rid` varchar(40) NOT NULL,
  PRIMARY KEY (`uid`,`rid`),
  KEY `Reference_1_FK` (`uid`),
  KEY `Reference_2_FK` (`rid`),
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`uid`) REFERENCES `sys_user` (`uid`),
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`rid`) REFERENCES `sys_role` (`rid`)
) ENGINE=InnoDB;


-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` (`rid`,`uid`) VALUES ('admin',1);


-- ----------------------------
-- Table structure for sys_account_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_account_log`;
CREATE TABLE `sys_account_log`  (
  `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户账户操作日志主键',
  `logName` varchar(255) NULL DEFAULT NULL COMMENT '日志名称(login,register,logout)',
  `userID` varchar(30) NULL DEFAULT NULL COMMENT '用户id',
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `SUCCEED` tinyint(4) NULL DEFAULT NULL COMMENT '是否执行成功(0失败1成功)',
  `MESSAGE` varchar(255) NULL DEFAULT NULL COMMENT '具体消息',
  `IP` varchar(255) NULL DEFAULT NULL COMMENT '登录ip',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB COMMENT = '登录注册登出记录';

-- ----------------------------
-- Records of sys_account_log
-- ----------------------------
INSERT INTO `sys_account_log` VALUES (1, '用户登录日志', '1', '2018-04-22 13:22:39', 1, NULL, '10.0.75.2');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户操作日志主键',
  `logName` varchar(255) NULL DEFAULT NULL COMMENT '日志名称',
  `userID` varchar(30) NULL DEFAULT NULL COMMENT '用户id',
  `API` varchar(255) NULL DEFAULT NULL COMMENT 'api名称',
  `METHOD` varchar(255) NULL DEFAULT NULL COMMENT '方法名称',
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `SUCCEED` tinyint(4) NULL DEFAULT NULL COMMENT '是否执行成功(0失败1成功)',
  `MESSAGE` varchar(255) NULL DEFAULT NULL COMMENT '具体消息备注',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB COMMENT = '操作日志';

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (1, '业务操作日志', '1', '/resource/menus', 'GET', '2018-04-22 16:05:05', 1, NULL);
INSERT INTO `sys_operation_log` VALUES (2, '业务操作日志', '1', '/resource/menus', 'GET', '2018-04-22 16:05:09', 1, NULL);
INSERT INTO `sys_operation_log` VALUES (3, '业务操作日志', '1', '/resource/api/-1/1/10', 'GET', '2018-04-22 16:08:15', 1, NULL);
























