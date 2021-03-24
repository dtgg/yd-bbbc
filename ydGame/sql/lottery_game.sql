/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : lottery_game

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2021-03-18 17:13:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for game_switch
-- ----------------------------
DROP TABLE IF EXISTS `game_switch`;
CREATE TABLE `game_switch` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `serverCode` int(10) DEFAULT 0,
  `status` int(10) DEFAULT 1 COMMENT '0 关闭 1 开启',
  `createTime` int(10) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for game_version
-- ----------------------------
DROP TABLE IF EXISTS `game_version`;
CREATE TABLE `game_version` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `apkVersion` varchar(10) DEFAULT '',
  `apkMandatory` tinyint(2) DEFAULT 0,
  `jsVersion` varchar(10) DEFAULT NULL,
  `apkUrl` varchar(100) DEFAULT NULL,
  `createTime` int(10) DEFAULT 0,
  `appId` int(10) DEFAULT 1000001 COMMENT '应用id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for lottery
-- ----------------------------
DROP TABLE IF EXISTS `lottery`;
CREATE TABLE `lottery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(10) NOT NULL DEFAULT 0 COMMENT '彩种',
  `period` int(10) NOT NULL COMMENT '开奖期数',
  `price` varchar(10) DEFAULT '0' COMMENT '总中奖金额',
  `number` varchar(20) DEFAULT NULL COMMENT '开奖数字',
  `status` int(10) NOT NULL DEFAULT 0 COMMENT '0:未开奖，1:已计算，2:已结算',
  `createTime` int(10) NOT NULL COMMENT '时间',
  `openTime` int(10) DEFAULT NULL COMMENT '开奖时间',
  `totalPay` decimal(10,2) DEFAULT 0.00 COMMENT '下注总金额',
  `totalAward` decimal(10,2) DEFAULT 0.00 COMMENT '派奖总金额',
  `totalProfit` decimal(10,2) DEFAULT 0.00 COMMENT '盈利总金额',
  `totalFee` decimal(10,2) DEFAULT 0.00 COMMENT '手续费总金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11521 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for lottery_config
-- ----------------------------
DROP TABLE IF EXISTS `lottery_config`;
CREATE TABLE `lottery_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lotteryType` int(10) NOT NULL COMMENT '彩种',
  `feeRateMin` decimal(10,2) NOT NULL COMMENT '手续费率（小）',
  `feeRateMax` decimal(10,2) NOT NULL COMMENT '手续费率（大）',
  `drawRange` varchar(10) NOT NULL COMMENT '开奖区间（使用:间隔）',
  `enabled` int(1) DEFAULT NULL,
  `balance` int(10) DEFAULT NULL,
  `frequency` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for lottery_platform_info
-- ----------------------------
DROP TABLE IF EXISTS `lottery_platform_info`;
CREATE TABLE `lottery_platform_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `totalNum` int(10) DEFAULT 0,
  `newNum` int(10) DEFAULT 0,
  `createTime` int(10) DEFAULT 0,
  `appId` int(10) DEFAULT 1000001,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for lottery_promote_info
-- ----------------------------
DROP TABLE IF EXISTS `lottery_promote_info`;
CREATE TABLE `lottery_promote_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `promoteNum` int(10) DEFAULT 0,
  `lv1Pump` decimal(10,0) DEFAULT 0,
  `lv2Pump` decimal(10,0) DEFAULT 0,
  `createTime` int(10) DEFAULT 0,
  `appId` int(10) DEFAULT 1000001,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for lottery_turnover_info
-- ----------------------------
DROP TABLE IF EXISTS `lottery_turnover_info`;
CREATE TABLE `lottery_turnover_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `payNum` int(10) DEFAULT 0,
  `payAmount` decimal(10,0) DEFAULT 0,
  `withdrawNum` int(10) DEFAULT 0,
  `withdrawAmount` decimal(10,0) DEFAULT 0,
  `withdrawSucNum` int(10) DEFAULT 0,
  `withdrawSucAmount` decimal(10,0) DEFAULT 0,
  `lotteryNum` int(10) DEFAULT 0,
  `lotteryFee` decimal(10,0) DEFAULT 0,
  `lotteryPay` decimal(10,0) DEFAULT 0,
  `lotteryAward` decimal(10,0) DEFAULT 0,
  `lotteryProfit` decimal(10,0) DEFAULT 0,
  `balance` decimal(10,0) DEFAULT 0,
  `createTime` decimal(10,0) DEFAULT 0,
  `appId` int(10) DEFAULT 1000001,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pay_channel_config
-- ----------------------------
DROP TABLE IF EXISTS `pay_channel_config`;
CREATE TABLE `pay_channel_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel` int(1) NOT NULL COMMENT '支付渠道（1：cashfree）',
  `name` varchar(20) NOT NULL COMMENT '支付渠道名称',
  `aliasName` varchar(20) NOT NULL COMMENT '支付渠道别名',
  `appId` varchar(200) NOT NULL COMMENT 'appId',
  `secretKey` varchar(200) NOT NULL COMMENT 'secretKey',
  `enabled` int(1) NOT NULL DEFAULT 0 COMMENT '是否启用',
  `withdrawalAudit` int(1) NOT NULL DEFAULT 1 COMMENT '体现是否需要审核',
  `paymentNotifyUrl` varchar(255) NOT NULL COMMENT '订单回调地址',
  `clientId` varchar(200) NOT NULL,
  `clientSecret` varchar(200) NOT NULL,
  `payoutNotifyUrl` varchar(255) NOT NULL,
  `businessAccount` varchar(255) DEFAULT NULL,
  `withdrawFee` decimal(2,2) DEFAULT 0.00 COMMENT '提现手续费',
  `paymentUrl` varchar(255) DEFAULT NULL,
  `mchId` varchar(50) DEFAULT NULL COMMENT '商户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for pay_success_deal
-- ----------------------------
DROP TABLE IF EXISTS `pay_success_deal`;
CREATE TABLE `pay_success_deal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(20) NOT NULL,
  `orderId` varchar(50) NOT NULL,
  `type` int(1) NOT NULL DEFAULT 2 COMMENT '充值类型（1：筹码；2：金币）',
  `point` double(15,2) NOT NULL DEFAULT 0.00 COMMENT '金币、筹码',
  `isDeal` int(1) NOT NULL DEFAULT 0 COMMENT '是否处理（0：未处理；1：处理中；2：已处理）',
  `createTime` int(11) NOT NULL,
  `payType` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for pay_withdrawal_config
-- ----------------------------
DROP TABLE IF EXISTS `pay_withdrawal_config`;
CREATE TABLE `pay_withdrawal_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel` int(1) NOT NULL COMMENT '支付渠道（1：cashfree）',
  `name` varchar(20) NOT NULL COMMENT '支付渠道名称',
  `aliasName` varchar(20) NOT NULL COMMENT '支付渠道别名',
  `appId` varchar(50) NOT NULL COMMENT 'appId',
  `secretKey` varchar(50) NOT NULL COMMENT 'secretKey',
  `enabled` int(1) NOT NULL DEFAULT 0 COMMENT '是否启用',
  `withdrawalAudit` int(1) NOT NULL DEFAULT 1 COMMENT '体现是否需要审核',
  `paymentNotifyUrl` varchar(255) NOT NULL COMMENT '订单回调地址',
  `clientId` varchar(50) NOT NULL,
  `clientSecret` varchar(50) NOT NULL,
  `payoutNotifyUrl` varchar(255) NOT NULL,
  `businessAccount` varchar(255) DEFAULT NULL,
  `withdrawFee` decimal(2,2) DEFAULT 0.00 COMMENT '提现手续费',
  `paymentUrl` varchar(255) DEFAULT NULL,
  `mchId` varchar(50) DEFAULT NULL COMMENT '商户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `playerName` varchar(42) DEFAULT NULL,
  `passWord` varchar(60) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '登陆密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户昵称',
  `headUrl` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户头像地址',
  `roomId` int(10) DEFAULT 0 COMMENT '玩家当前所在房间ID',
  `zjPoint` double(15,2) DEFAULT 0.00 COMMENT '玩家的zj',
  `onLineTime` int(10) DEFAULT 0,
  `createTime` int(10) DEFAULT 0,
  `appId` int(10) DEFAULT 1000001 COMMENT '应用id',
  `banLogin` int(10) DEFAULT 0 COMMENT '禁止登陆',
  `referralCode` varchar(8) DEFAULT '',
  `kfId` bigint(20) DEFAULT 0,
  `orderAmount` double(15,0) DEFAULT 0,
  `withdrawAmount` double(15,2) DEFAULT 0.00,
  PRIMARY KEY (`id`),
  KEY `pN` (`playerName`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for player_account
-- ----------------------------
DROP TABLE IF EXISTS `player_account`;
CREATE TABLE `player_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(11) NOT NULL COMMENT '玩家ID',
  `name` varchar(100) DEFAULT NULL,
  `accNo` varchar(40) DEFAULT NULL COMMENT '收款方姓名',
  `ifsc` varchar(40) DEFAULT NULL COMMENT '收款方姓名',
  `mobile` varchar(40) DEFAULT NULL COMMENT '收款方姓名',
  `enabled` int(1) DEFAULT 0,
  `accountId` varchar(50) DEFAULT '' COMMENT 'beneId',
  `password` varchar(50) DEFAULT '',
  `bankName` varchar(20) DEFAULT '',
  `isUpi` int(1) DEFAULT 0 COMMENT '支付渠道ID',
  `upiId` varchar(50) DEFAULT NULL,
  `upiName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `pi_index` (`playerId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='玩家收款账户信息';

-- ----------------------------
-- Table structure for player_bonus_draw
-- ----------------------------
DROP TABLE IF EXISTS `player_bonus_draw`;
CREATE TABLE `player_bonus_draw` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(20) NOT NULL,
  `orderId` varchar(50) NOT NULL,
  `amount` decimal(10,0) NOT NULL DEFAULT 0,
  `status` int(10) NOT NULL DEFAULT 0,
  `createTime` int(10) NOT NULL DEFAULT 0,
  `appId` int(10) NOT NULL DEFAULT 1000001,
  `kfId` bigint(20) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for player_lottery
-- ----------------------------
DROP TABLE IF EXISTS `player_lottery`;
CREATE TABLE `player_lottery` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(20) NOT NULL COMMENT '用户ID',
  `type` int(10) NOT NULL COMMENT '彩种',
  `lotteryId` int(10) NOT NULL COMMENT '购彩ID',
  `period` varchar(20) NOT NULL,
  `selected` int(10) DEFAULT NULL COMMENT '选择颜色(1:绿色，2:红色，3:紫色)',
  `number` varchar(20) DEFAULT NULL COMMENT '数字',
  `pay` decimal(10,2) NOT NULL COMMENT '下注金额',
  `fee` decimal(10,2) NOT NULL COMMENT '手续费',
  `status` int(10) NOT NULL DEFAULT 0 COMMENT '开奖状态(0:未开奖;1:中奖;2:未中奖)',
  `award` decimal(10,2) DEFAULT 0.00 COMMENT '中奖金额',
  `createTime` int(10) NOT NULL COMMENT '下注时间',
  `openTime` int(10) DEFAULT NULL COMMENT '开奖时间',
  `result` varchar(20) DEFAULT NULL COMMENT '开奖结果',
  `appId` int(10) DEFAULT 1000001,
  `kfId` bigint(20) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for player_order
-- ----------------------------
DROP TABLE IF EXISTS `player_order`;
CREATE TABLE `player_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderId` varchar(50) NOT NULL COMMENT '订单ID',
  `playerId` bigint(20) NOT NULL COMMENT '玩家ID',
  `productId` int(11) NOT NULL COMMENT '商品ID',
  `amount` double(15,2) NOT NULL COMMENT '金额',
  `status` int(1) DEFAULT 0 COMMENT '支付状态（0未支付, 1已支付）',
  `txnOrderId` varchar(50) DEFAULT '',
  `createTime` int(10) NOT NULL COMMENT '下单时间',
  `completeTime` int(10) DEFAULT 0 COMMENT '支付时间',
  `payChannel` varchar(50) DEFAULT '' COMMENT '支付渠道',
  `msg` varchar(255) DEFAULT '' COMMENT '订单失败原因',
  `appId` int(10) DEFAULT 1000001 COMMENT '用户渠道',
  `registerTime` int(10) DEFAULT 0 COMMENT '用户注册时间',
  `kfId` bigint(20) DEFAULT 0 COMMENT '第三方支付返回状态',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `playerId_index` (`playerId`) USING BTREE,
  KEY `productId_index` (`productId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='支付订单';

-- ----------------------------
-- Table structure for player_promote
-- ----------------------------
DROP TABLE IF EXISTS `player_promote`;
CREATE TABLE `player_promote` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(20) NOT NULL,
  `nickname` varchar(50) NOT NULL COMMENT '用户昵称',
  `superiorId` bigint(20) DEFAULT NULL COMMENT '上级ID',
  `grandId` bigint(20) DEFAULT NULL COMMENT '上上级ID',
  `superiorAmount` decimal(10,2) DEFAULT 0.00 COMMENT '给上级产生的总贡献',
  `grandAmount` decimal(10,2) DEFAULT 0.00 COMMENT '给上级产生的总贡献',
  `isKf` int(1) NOT NULL DEFAULT 0 COMMENT '是否客服',
  `kfId` bigint(20) DEFAULT 0 COMMENT '客服ID',
  `isEffective` int(1) DEFAULT 0 COMMENT '是否是有效用户',
  `subNum` int(10) DEFAULT 0 COMMENT '下级有效用户数量',
  `sonNum` int(10) DEFAULT 0 COMMENT '下下级有效用户数量',
  `createTime` int(10) NOT NULL,
  `effectiveNum` int(10) DEFAULT 0,
  `appId` int(10) DEFAULT 1000001,
  `bonusAmount` decimal(10,2) DEFAULT 0.00,
  PRIMARY KEY (`id`),
  KEY `playerId_index` (`playerId`),
  KEY `superiorId_index` (`superiorId`),
  KEY `grandId_index` (`grandId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for player_promote_config
-- ----------------------------
DROP TABLE IF EXISTS `player_promote_config`;
CREATE TABLE `player_promote_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `superiorRate` varchar(10) NOT NULL DEFAULT '0' COMMENT '上级获奖比例',
  `grandRate` varchar(10) NOT NULL DEFAULT '0' COMMENT '上上级获奖比例',
  `referralLink` varchar(255) NOT NULL COMMENT '推广链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for player_promote_detail
-- ----------------------------
DROP TABLE IF EXISTS `player_promote_detail`;
CREATE TABLE `player_promote_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(20) NOT NULL COMMENT '用户ID',
  `nickname` varchar(50) NOT NULL COMMENT '用户昵称',
  `betAmount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '下注金额',
  `superiorAmount` decimal(10,3) NOT NULL DEFAULT 0.000 COMMENT '给上级产生的贡献',
  `grandAmount` decimal(10,3) NOT NULL DEFAULT 0.000 COMMENT '给上级产生的贡献',
  `createTime` int(10) NOT NULL COMMENT '下注时间',
  `appId` int(10) NOT NULL DEFAULT 1000001,
  PRIMARY KEY (`id`),
  KEY `playerId_index` (`playerId`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for player_reward_history
-- ----------------------------
DROP TABLE IF EXISTS `player_reward_history`;
CREATE TABLE `player_reward_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(20) NOT NULL,
  `reward` double(15,2) NOT NULL COMMENT '奖励',
  `source` int(1) NOT NULL COMMENT '奖励来源（1：升级，2：任务）',
  `taskId` int(11) DEFAULT NULL COMMENT '完成的任务',
  `createTime` int(11) DEFAULT NULL COMMENT '时间',
  `appId` int(10) DEFAULT 1000001,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='奖励记录表';

-- ----------------------------
-- Table structure for player_withdrawal
-- ----------------------------
DROP TABLE IF EXISTS `player_withdrawal`;
CREATE TABLE `player_withdrawal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(20) NOT NULL COMMENT '玩家ID',
  `name` varchar(40) NOT NULL COMMENT '收款方姓名',
  `accNo` varchar(50) NOT NULL COMMENT '收款方银行账号',
  `ifsc` varchar(50) NOT NULL COMMENT '收款方银行账号所在银行IFSC号',
  `mobile` varchar(20) DEFAULT NULL,
  `amount` double(10,2) NOT NULL COMMENT '金额',
  `transferId` varchar(50) DEFAULT NULL,
  `referenceId` varchar(50) DEFAULT NULL,
  `status` int(1) DEFAULT 0 COMMENT '订单状态: 0(处理中), 1(成功). 2(失败)',
  `errorMsg` varchar(255) DEFAULT NULL COMMENT '订单失败原因',
  `createTime` int(10) DEFAULT 0 COMMENT '订单创建时间',
  `appId` int(10) DEFAULT 1000001 COMMENT '用户渠道',
  `registerTime` int(10) DEFAULT 0 COMMENT '用户注册时间',
  `payChannel` varchar(20) DEFAULT NULL,
  `kfId` int(10) DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `pi_index` (`playerId`) USING BTREE,
  KEY `referenceId_index` (`referenceId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='提现订单';

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(2) NOT NULL COMMENT '商品类型（1：筹码；2：金币）',
  `name` varchar(50) NOT NULL COMMENT '商品名称',
  `amount` double(15,2) NOT NULL COMMENT '价格',
  `point` double(15,2) NOT NULL COMMENT '筹码、金币数量',
  `enabled` int(1) NOT NULL DEFAULT 1 COMMENT '启用、禁用',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `pt_index` (`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for product_buy_history
-- ----------------------------
DROP TABLE IF EXISTS `product_buy_history`;
CREATE TABLE `product_buy_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `playerId` bigint(20) NOT NULL COMMENT '玩家ID',
  `promotionId` int(11) NOT NULL COMMENT '活动ID',
  `buyNum` int(3) NOT NULL COMMENT '购买次数',
  `createTime` int(10) NOT NULL COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `pi_index` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for promotion
-- ----------------------------
DROP TABLE IF EXISTS `promotion`;
CREATE TABLE `promotion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` int(11) NOT NULL COMMENT '商品id',
  `productType` int(2) NOT NULL COMMENT '商品类型（1：筹码；2：金币）',
  `promotionName` varchar(50) NOT NULL COMMENT '活动商品名称',
  `promotionAmount` double(15,2) NOT NULL COMMENT '活动价格',
  `promotionPoint` double(15,2) NOT NULL COMMENT '活动数量',
  `limit` int(3) DEFAULT NULL COMMENT '活动限制',
  `enabled` int(1) NOT NULL COMMENT '是否开启活动（0：关闭；1：开启）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='商品活动表';

-- ----------------------------
-- Table structure for sys_close_server
-- ----------------------------
DROP TABLE IF EXISTS `sys_close_server`;
CREATE TABLE `sys_close_server` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `closeServer` int(2) DEFAULT 0,
  `serverCode` int(10) NOT NULL COMMENT '服务器编码',
  `status` int(2) NOT NULL DEFAULT 0 COMMENT '1 关闭',
  `createTime` int(10) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for task_config
-- ----------------------------
DROP TABLE IF EXISTS `task_config`;
CREATE TABLE `task_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '任务名称',
  `type` int(2) NOT NULL COMMENT '任务类型(1:分享任务)',
  `reward` int(11) NOT NULL COMMENT '任务奖励',
  `target` int(11) NOT NULL DEFAULT 0 COMMENT '任务目标',
  `enabled` int(1) NOT NULL DEFAULT 0 COMMENT '是否启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='任务配置表';

-- 公告
CREATE TABLE `game_announcement` (
  `id` int(10) NOT NULL,
  `appId` int(10) DEFAULT 1000001,
  `announcement` varchar(200) DEFAULT NULL,
  `createTime` varchar(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 客服配置
CREATE TABLE `service_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `appId` int(10) DEFAULT NULL,
  `telegram` varchar(256) DEFAULT NULL,
  `whatsapp` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



--
CREATE TABLE `agent_quota` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `appId` varchar(255) NOT NULL,
   `maxQuota` float NOT NULL DEFAULT 0,
   `availableQuota` float NOT NULL DEFAULT 0,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `agent_withdraw` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `amount` float(15,2) NOT NULL,
  `appId` varchar(10) NOT NULL,
  `createTime` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table lottery add index `st_index` (`status`,`type`);
alter table pay_success_deal add index `deal_idx` (`isDeal`);
alter table player_account add index `pi_idx` (`playerId`);
alter table player_account add index `ac_idx` (`accNo`);
alter table player_bonus_draw add index `pi_idx` (`playerId`);
alter table player_lottery add index `lt_idx` (`lotteryId`);
alter table player_lottery add index `status_idx` (`status`);
alter table player_lottery add index `pi_idx` (`playerId`);
alter table player_order add index `pi_idx` (`playerId`);
alter table player_order add index `status_idx` (`status`);
alter table player_promote_detail add index `playerId_index` (`playerId`);
alter table player_reward_history add index `pi_idx` (`playerId`);
alter table player_reward_history add index `ti_idx` (`taskId`);
alter table player_withdrawal add index `pi_idx` (`playerId`);
alter table player_withdrawal add index `status_idx` (`status`);

--
ALTER TABLE lottery_config ADD COLUMN probability int(10) DEFAULT 0;
ALTER TABLE player ADD COLUMN isVir int(10) DEFAULT 1;
ALTER TABLE player_lottery ADD COLUMN isVir int(10) DEFAULT 1;
