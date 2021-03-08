CREATE TABLE `player` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `playerName` varchar(40) CHARACTER SET utf8 NOT NULL COMMENT '用户登陆名',
  `passWord` varchar(60) CHARACTER SET utf8 NOT NULL COMMENT '登陆密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户昵称',
  `headUrl` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户头像地址',
  `roomId` int(10) DEFAULT '0' COMMENT '玩家当前所在房间ID',
  `zjPoint` double(15,1) DEFAULT '0.0' COMMENT '玩家的zj',
  `coinPoint` double(15,1) DEFAULT '0.0' COMMENT '身上的金币',
  `onLineTime` int(10) DEFAULT '0',
  `createTime` int(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------- 任务 ----------------------------------------------------------

CREATE TABLE `task_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '任务名称',
  `type` int(2) NOT NULL COMMENT '任务类型',
  `reward` int(11) NOT NULL COMMENT '任务奖励',
  `nextTaskId` int(11) DEFAULT NULL COMMENT '下个任务',
  `target` int(11) DEFAULT 0 COMMENT '任务目标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务配置表';

CREATE TABLE `player_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `playerId` int(11) NOT NULL COMMENT '玩家ID',
  `taskId` int(11) NOT NULL COMMENT '任务ID',
  `acceptTime` int(10) NOT NULL COMMENT '接受任务时间',
  `progress` int(11) DEFAULT 0 COMMENT '任务进度',
  `isComplete` int(1) NOT NULL DEFAULT 0 COMMENT '是否完成任务',
  `completeTime` int(10) DEFAULT NULL COMMENT '任务完成时间',
  `isReceived` int(1) NOT NULL DEFAULT 0 COMMENT '是否领取奖励',
  `receiveTime` int(10) DEFAULT NULL COMMENT '奖励领取时间',
  PRIMARY KEY (`id`),
  KEY `pi_index` (`playerId`),
  KEY `ti_index` (`taskId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='玩家任务表';

ALTER TABLE `player_task` ADD `taskType` int(2) NOT NULL COMMENT '任务类型';

-- ----- 维护服务器 -----
CREATE TABLE `sys_close_server` (
  `id` int(10) NOT NULL,
  `closeServer` int(2) DEFAULT '0',
  `serverCode` int(10) NOT NULL COMMENT '服务器编码',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '1 关闭',
  `createTime` int(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------------------------------------------------------------------------------------------------
-- ----------------------------
-- Table structure for player_task
-- ----------------------------
alter table player_task modify column id bigint(20) NOT NULL AUTO_INCREMENT;

-- ----------------------------
-- Table structure for player_account
-- ----------------------------
DROP TABLE IF EXISTS `player_account`;
CREATE TABLE `player_account`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(11) NOT NULL COMMENT '玩家ID',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款人姓名',
  `accNo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款方银行账号',
  `ifsc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款方银行账号所在银行IFSC号',
  `mobile` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款方手机号,10位数字',
  `withdrawing` double(10, 2) NULL DEFAULT 0 COMMENT '提现中金额',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pi_index`(`playerId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '玩家收款账户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_channel_config
-- ----------------------------
DROP TABLE IF EXISTS `pay_channel_config`;
CREATE TABLE `pay_channel_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel` int(1) NOT NULL COMMENT '支付渠道（1：cashfree）',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付渠道名称',
  `appId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'appId',
  `secretKey` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'secretKey',
  `enabled` int(1) NOT NULL DEFAULT 0 COMMENT '是否启用',
  `withdrawalAudit` int(1) NOT NULL DEFAULT 1 COMMENT '体现是否需要审核',
  `paymentNotifyUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单回调地址',
  `clientId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `clientSecret` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `payoutNotifyUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_channel_config
-- ----------------------------
INSERT INTO `pay_channel_config` VALUES (1, 1, 'cashfree', '210971927004f27f647825cfe79012', '3e1cfa9db53acad32cb35c60161aa1ec331a9412', 1, 0, 'https://whw.ngrok2.xiaomiqiu.cn/api/cashFree/payment/notify', 'CF21097CDM25HICDYR626I', 'c2dca0456e7004bc0b218e98794b113e7351f75a', 'https://whw.ngrok2.xiaomiqiu.cn/api/cashFree/payout/notify');

-- ----------------------------
-- Table structure for player_order
-- ----------------------------
DROP TABLE IF EXISTS `player_order`;
CREATE TABLE `player_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单ID',
  `playerId` bigint(20) NOT NULL COMMENT '玩家ID',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `mobile` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `payerVA` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款账户',
  `productId` int(11) NOT NULL COMMENT '商品ID',
  `amount` double(15, 2) NOT NULL COMMENT '金额',
  `orderTime` int(10) NOT NULL COMMENT '下单时间',
  `payTime` int(10) NULL DEFAULT NULL COMMENT '支付时间',
  `payStatus` int(1) NULL DEFAULT NULL COMMENT '支付状态（0未支付, 1已支付）',
  `txnOrderId` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方支付流水号',
  `txnStatus` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方支付返回状态',
  `txnCompletionDate` int(10) NULL DEFAULT NULL COMMENT '交易完成时间',
  `payChannel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付渠道',
  `errorMsg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单失败原因',
  `platformPayChannel` int(1) NULL DEFAULT NULL COMMENT '平台支付渠道',
  `cftoken` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `playerId_index`(`playerId`) USING BTREE,
  INDEX `productId_index`(`productId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_withdrawal
-- ----------------------------
DROP TABLE IF EXISTS `player_withdrawal`;
CREATE TABLE `player_withdrawal`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(20) NOT NULL COMMENT '玩家ID',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款方姓名',
  `accNo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款方银行账号',
  `ifsc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款方银行账号所在银行IFSC号',
  `mobile` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款方手机号',
  `amount` double(10, 2) NOT NULL COMMENT '金额',
  `beneId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cashfree收款人ID',
  `transferId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `referenceId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `utr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `acknowledged` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int(1) NULL DEFAULT NULL COMMENT '订单状态: 0(处理中), 1(成功). 2(失败)',
  `addedOn` int(10) NULL DEFAULT NULL,
  `processedOn` int(10) NULL DEFAULT NULL,
  `errorMsg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单失败原因',
  `createTime` int(10) NULL DEFAULT NULL COMMENT '订单创建时间',
  `transferMode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提现方式',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pi_index`(`playerId`) USING BTREE,
  INDEX `referenceId_index`(`referenceId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '提现订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(2) NOT NULL COMMENT '商品类型（1：筹码；2：金币）',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `amount` double(15, 2) NOT NULL COMMENT '价格',
  `point` double(15, 2) NOT NULL COMMENT '筹码、金币数量',
  `enabled` int(1) NOT NULL DEFAULT 1 COMMENT '启用、禁用',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pt_index`(`type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, 1, '50000', 49.00, 50000.00, 1);
INSERT INTO `product` VALUES (2, 1, '1L', 99.00, 100000.00, 1);
INSERT INTO `product` VALUES (3, 1, '2L', 199.00, 200000.00, 1);
INSERT INTO `product` VALUES (4, 1, '50L', 499.00, 5000000.00, 1);
INSERT INTO `product` VALUES (5, 1, '100L', 999.00, 10000000.00, 1);
INSERT INTO `product` VALUES (6, 1, '200L', 1999.00, 20000000.00, 1);
INSERT INTO `product` VALUES (7, 2, '100', 100.00, 100.00, 1);
INSERT INTO `product` VALUES (8, 2, '200', 200.00, 200.00, 1);
INSERT INTO `product` VALUES (9, 2, '500', 500.00, 500.00, 1);
INSERT INTO `product` VALUES (10, 2, '1000', 1000.00, 1000.00, 1);
INSERT INTO `product` VALUES (11, 2, '2000', 2000.00, 2000.00, 1);
INSERT INTO `product` VALUES (12, 2, '5000', 5000.00, 5000.00, 1);

-- ----------------------------
-- Table structure for promotion
-- ----------------------------
DROP TABLE IF EXISTS `promotion`;
CREATE TABLE `promotion`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` int(11) NOT NULL COMMENT '商品id',
  `productType` int(2) NOT NULL COMMENT '商品类型（1：筹码；2：金币）',
  `promotionName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '活动商品名称',
  `promotionAmount` double(15, 2) NOT NULL COMMENT '活动价格',
  `promotionPoint` double(15, 2) NOT NULL COMMENT '活动数量',
  `limit` int(3) NULL DEFAULT NULL COMMENT '活动限制',
  `enabled` int(1) NOT NULL COMMENT '是否开启活动（0：关闭；1：开启）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of promotion
-- ----------------------------
INSERT INTO `promotion` VALUES (1, 1, 1, '1L', 49.00, 100000.00, 1, 1);
INSERT INTO `promotion` VALUES (2, 2, 1, '2L', 99.00, 200000.00, 1, 1);
INSERT INTO `promotion` VALUES (3, 3, 1, '4L', 199.00, 400000.00, 1, 1);
INSERT INTO `promotion` VALUES (4, 4, 1, '100L', 499.00, 10000000.00, 1, 1);
INSERT INTO `promotion` VALUES (5, 5, 1, '200L', 999.00, 20000000.00, 1, 1);
INSERT INTO `promotion` VALUES (6, 6, 1, '200L', 1999.00, 20000000.00, 1, 1);

-- ----------------------------
-- Table structure for product_buy_history
-- ----------------------------
DROP TABLE IF EXISTS `product_buy_history`;
CREATE TABLE `product_buy_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(20) NOT NULL COMMENT '玩家ID',
  `promotionId` int(11) NOT NULL COMMENT '活动ID',
  `buyNum` int(3) NOT NULL COMMENT '购买次数',
  `createTime` int(10) NOT NULL COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pi_index`(`playerId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_success_deal
-- ----------------------------
DROP TABLE IF EXISTS `pay_success_deal`;
CREATE TABLE `pay_success_deal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(20) NOT NULL,
  `productId` int(11) NOT NULL,
  `type` int(1) NOT NULL COMMENT '充值类型（1：筹码；2：金币）',
  `point` double(15,2) NOT NULL DEFAULT 0.00 COMMENT '金币、筹码',
  `isDeal` int(1) NOT NULL DEFAULT 0 COMMENT '是否处理（0：未处理；1：处理中；2：已处理）',
  `createTime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ------ fb login----
ALTER TABLE player ADD fbUserId varchar (20) DEFAULT '' COMMENT 'fb的userId';
ALTER TABLE player ADD fbBind  tinyint(1) DEFAULT 0 COMMENT '是否绑定fb';

-- ---- 系统配置---
CREATE TABLE `pump_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `serverCode` int(10) DEFAULT '0',
  `pump` double(10,2) DEFAULT '0.00' COMMENT '抽水比例',
  `createTime` int(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

INSERT INTO `pump_config` VALUES (1, 20, 0.15, 0);
INSERT INTO `pump_config` VALUES (2, 30, 0.15, 0);
-- -- 游戏版本号----
CREATE TABLE `game_version` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `apkVersion` varchar(10) DEFAULT '',
  `apkMandatory` tinyint(2) DEFAULT '0',
  `jsVersion` varchar(10) DEFAULT NULL,
  `apkUrl` varchar(100) DEFAULT NULL,
  `createTime` int(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- ---- 开关游戏 ----
CREATE TABLE `game_switch` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `serverCode` int(10) DEFAULT '0',
  `status` int(10) DEFAULT '1' COMMENT '0 关闭 1 开启',
  `createTime` int(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE player_account MODIFY COLUMN name VARCHAR(40) DEFAULT NULL COMMENT '收款方姓名';
ALTER TABLE player_account MODIFY COLUMN accNo VARCHAR(40) DEFAULT NULL COMMENT '收款方姓名';
ALTER TABLE player_account MODIFY COLUMN ifsc VARCHAR(40) DEFAULT NULL COMMENT '收款方姓名';
ALTER TABLE player_account MODIFY COLUMN mobile VARCHAR(40) DEFAULT NULL COMMENT '收款方姓名';
ALTER TABLE player_account ADD payMobile varchar(10) DEFAULT NULL COMMENT '付款手机号';
ALTER TABLE player_account ADD email varchar(50) DEFAULT NULL COMMENT '付款邮箱';

-- --修改playerName 字段长度 ----
alter table player modify column playerName varchar(42);

-- -- 奖励获得记录、更新商品表 ----
CREATE TABLE `player_reward_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(20) NOT NULL,
  `reward` double(15,2) NOT NULL COMMENT '奖励',
  `rewardSource` int(1) NOT NULL COMMENT '奖励来源（1：升级，2：任务）',
  `grade` int(4) DEFAULT NULL COMMENT '提升的等级',
  `experience` double(15,2) DEFAULT NULL COMMENT '提升的经验',
  `taskId` int(11) DEFAULT NULL COMMENT '完成的任务',
  `createTime` int(11) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='奖励记录表';

update product set name = '50000', amount = 49.00, point = 50000.00 where id = 1;
update product set name = '1L', amount = 98.00, point = 100000.00 where id = 2;
update product set name = '2L', amount = 196.00, point = 200000.00 where id = 3;
update product set name = '5L', amount = 490.00, point = 500000.00 where id = 4;
update product set name = '10L', amount = 980.00, point = 1000000.00 where id = 5;
update product set name = '20L', amount = 1960.00, point = 2000000.00 where id = 6;

-- ---------------------------------------
update promotion set promotionName = '1L', promotionAmount = 49.00, promotionPoint = 100000.00 where id = 1;
update promotion set promotionName = '2L', promotionAmount = 98.00, promotionPoint = 200000.00 where id = 2;
update promotion set promotionName = '4L', promotionAmount = 196.00, promotionPoint = 400000.00 where id = 3;
update promotion set promotionName = '10L', promotionAmount = 490.00, promotionPoint = 1000000.00 where id = 4;
update promotion set promotionName = '20L', promotionAmount = 980.00, promotionPoint = 2000000.00 where id = 5;
update promotion set promotionName = '40L', promotionAmount = 1960.00, promotionPoint = 4000000.00 where id = 6;

-- -- 索引
ALTER TABLE player ADD INDEX pN (playerName);
ALTER TABLE player ADD INDEX fBU (fbUserId);
-- -初始化
insert `game_switch`(`serverCode`,`status`,`createTime`) values(20,1,0);
insert `game_switch`(`serverCode`,`status`,`createTime`) values(30,1,0);
insert `game_switch`(`serverCode`,`status`,`createTime`) values(40,0,0);

ALTER TABLE `sys_close_server`
	MODIFY COLUMN `id` int(10) NOT NULL AUTO_INCREMENT FIRST;

-- ------------cashFree提现回调记录----------
CREATE TABLE `cash_free_payout_callback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event` varchar(30) DEFAULT NULL,
  `transferId` varchar(50) DEFAULT NULL,
  `referenceId` varchar(50) DEFAULT NULL,
  `eventTime` varchar(20) DEFAULT NULL,
  `acknowledged` varchar(10) DEFAULT NULL,
  `utr` varchar(30) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `ledgerBalance` varchar(255) DEFAULT NULL,
  `beneId` varchar(50) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `bankAccount` varchar(50) DEFAULT NULL,
  `ifsc` varchar(50) DEFAULT NULL,
  `signature` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- 修改字段
alter table pay_success_deal CHANGE productId orderId bigint(20) not null;

alter table pay_success_deal add column payType int(10) not null default 0;

alter table player alter column passWord set default '';

-- 修改player_account
ALTER TABLE `player_account` ADD column beneId varchar(50) DEFAULT NULL COMMENT 'beneId';
alter table `player_account` modify column `name` varchar(100);
alter table `player_account` modify column payMobile varchar(20);
alter table `player_account` modify column email varchar(200);

alter table `player_order` modify column mobile varchar(20);
alter table `player_withdrawal` modify column mobile varchar(20);

ALTER TABLE `player_withdrawal` ADD COLUMN `email` varchar(64) DEFAULT NULL COMMENT 'email' AFTER `transferMode`;


