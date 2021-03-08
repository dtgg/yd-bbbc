CREATE TABLE `lottery`(
    `id`         int(11) NOT NULL AUTO_INCREMENT,
    `type`       int(10) NOT NULL DEFAULT 0 COMMENT '彩种',
    `period`     int(10) NOT NULL COMMENT '开奖期数',
    `price`      varchar(10)      DEFAULT '0' COMMENT '总中奖金额',
    `number`     varchar(20)      DEFAULT NULL COMMENT '开奖数字',
    `status`     int(10) NOT NULL DEFAULT 0 COMMENT '0:未开奖，1:已计算，2:已结算',
    `createTime` int(10) NOT NULL COMMENT '时间',
    `openTime`   int(10)          DEFAULT NULL COMMENT '开奖时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `player_lottery`(
    `id`         bigint(20)     NOT NULL AUTO_INCREMENT,
    `playerId`   bigint(20)     NOT NULL COMMENT '用户ID',
    `type`       int(10)        NOT NULL COMMENT '彩种',
    `lotteryId`  int(10)        NOT NULL COMMENT '购彩ID',
    `period`     varchar(20)    NOT NULL,
    `selected`   int(10)                 DEFAULT NULL COMMENT '选择颜色(1:绿色，2:红色，3:紫色)',
    `number`     varchar(20)             DEFAULT NULL COMMENT '数字',
    `pay`        decimal(10, 2) NOT NULL COMMENT '下注金额',
    `fee`        decimal(10, 2) NOT NULL COMMENT '手续费',
    `status`     int(10)        NOT NULL DEFAULT 0 COMMENT '开奖状态(0:未开奖;1:中奖;2:未中奖)',
    `award`      decimal(10, 2)          DEFAULT 0.00 COMMENT '中奖金额',
    `createTime` int(10)        NOT NULL COMMENT '下注时间',
    `openTime`   int(10)                 DEFAULT NULL COMMENT '开奖时间',
    `result`     varchar(20) DEFAULT NULL COMMENT '开奖结果',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `lottery_config`(
    `id`          int(11)                                                      NOT NULL AUTO_INCREMENT,
    `lotteryType` int(10)                                                      NOT NULL COMMENT '彩种',
    `feeRateMin`  decimal(10, 2)                                               NOT NULL COMMENT '手续费率（小）',
    `feeRateMax`  decimal(10, 2)                                               NOT NULL COMMENT '手续费率（大）',
    `drawRange`   varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开奖区间（使用:间隔）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 DEFAULT CHARSET = utf8mb4;

INSERT INTO `lottery_config` VALUES (1, 1, 0.02, 0.10, '3-6');
INSERT INTO `lottery_config` VALUES (2, 2, 0.02, 0.10, '3-6');
INSERT INTO `lottery_config` VALUES (3, 3, 0.02, 0.10, '3-6');
INSERT INTO `lottery_config` VALUES (4, 4, 0.02, 0.10, '3-6');

-- 推广
CREATE TABLE `player_promote` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `playerId` bigint(20) NOT NULL,
      `superiorId` bigint(20) DEFAULT NULL COMMENT '上级ID',
      `grandId` bigint(20) DEFAULT NULL COMMENT '上上级ID',
      `superiorAmount` decimal(10,2) DEFAULT 0.00 COMMENT '给上级产生的总贡献',
      `grandAmount` decimal(10,2) DEFAULT 0.00 COMMENT '给上上级产生的总贡献',
      `isKf` int(1) NOT NULL DEFAULT 0 COMMENT '是否客服',
      `kfId` bigint(20) DEFAULT NULL COMMENT '客服ID',
      `isEffective` int(1) DEFAULT 0 COMMENT '是否是有效用户',
      `subNum` int(10) DEFAULT 0 COMMENT '下级有效用户数量',
      `sonNum` int(10) DEFAULT 0 COMMENT '下下级有效用户数量',
      `createTime` int(10) NOT NULL,
      PRIMARY KEY (`id`),
      KEY `playerId_index` (`playerId`),
      KEY `superiorId_index` (`superiorId`),
      KEY `grandId_index` (`grandId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `player_promote_detail` (
       `id` bigint(20) NOT NULL AUTO_INCREMENT,
       `playerId` bigint(20) NOT NULL COMMENT '用户ID',
       `nickname` varchar(50) NOT NULL COMMENT '用户昵称',
       `betAmount` decimal(10,2) NOT NULL COMMENT '下注金额',
       `superiorAmount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '给上级产生的贡献',
       `grandAmount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '给上上级产生的贡献',
       `createTime` int(10) NOT NULL COMMENT '下注时间',
       PRIMARY KEY (`id`),
       KEY `playerId_index` (`playerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `player_promote_config` (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `superiorRate` decimal(2,2) DEFAULT 0.00,
     `grandRate` decimal(2,2) DEFAULT 0.00,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- modify player_promote
ALTER TABLE player_promote ADD COLUMN nickname varchar(50) NOT NULL COMMENT '用户昵称' AFTER playerId;
ALTER TABLE player_promote MODIFY COLUMN `superiorAmount` decimal(10,3) DEFAULT 0.00 COMMENT '给上级产生的总贡献';
ALTER TABLE player_promote MODIFY COLUMN `grandAmount` decimal(10,3) DEFAULT 0.00 COMMENT '给上级产生的总贡献';

ALTER TABLE player_promote_detail MODIFY COLUMN `superiorAmount` decimal(10,3) NOT NULL DEFAULT 0.00 COMMENT '给上级产生的贡献';
ALTER TABLE player_promote_detail MODIFY COLUMN `grandAmount` decimal(10,3) NOT NULL DEFAULT 0.00 COMMENT '给上级产生的贡献';
-- modify player_promote_config
ALTER TABLE player_promote_config MODIFY COLUMN superiorRate VARCHAR(10) NOT NULL DEFAULT '0' COMMENT '上级获奖比例';
ALTER TABLE player_promote_config MODIFY COLUMN grandRate VARCHAR(10) NOT NULL DEFAULT '0' COMMENT '上上级获奖比例';
ALTER TABLE player_promote_config ADD COLUMN referralLink VARCHAR(255) NOT NULL COMMENT '推广链接';

-- modify lottery
ALTER TABLE lottery ADD COLUMN totalPay decimal(10,2) DEFAULT 0 COMMENT '下注总金额';
ALTER TABLE lottery ADD COLUMN totalAward decimal(10,2) DEFAULT 0 COMMENT '派奖总金额';
ALTER TABLE lottery ADD COLUMN totalProfit decimal(10,2) DEFAULT 0 COMMENT '盈利总金额';
ALTER TABLE lottery ADD COLUMN totalFee decimal(10,2) DEFAULT 0 COMMENT '手续费总金额';

-- modify player
ALTER TABLE player ADD COLUMN referralCode varchar(8) DEFAULT NULL;

-- modify playerAccount
ALTER TABLE player_account ADD COLUMN payChannelId int(11) DEFAULT NULL COMMENT '支付渠道ID';


--
ALTER TABLE lottery_config ADD COLUMN enabled int(1) DEFAULT NULL;
ALTER TABLE lottery_config ADD COLUMN balance int(10) DEFAULT NULL;
ALTER TABLE lottery_config ADD COLUMN frequency int(10) DEFAULT NULL;

ALTER TABLE pay_channel_config ADD COLUMN paymentUrl varchar(255) DEFAULT NULL;

--
ALTER TABLE pay_channel_config ADD COLUMN mchId VARCHAR(20) DEFAULT NULL COMMENT '商户ID';
ALTER TABLE pay_channel_config MODIFY secretKey VARCHAR(255) NOT NULL COMMENT 'secretKey';
ALTER TABLE player_order MODIFY txnOrderId VARCHAR(50) DEFAULT NULL;
ALTER TABLE player_account ADD COLUMN bankCode VARCHAR(20) DEFAULT NULL;
ALTER TABLE pay_channel_config MODIFY clientSecret VARCHAR(255) NOT NULL COMMENT 'secretKey';

--
ALTER TABLE player_account ADD COLUMN depositName VARCHAR(64) DEFAULT NULL;