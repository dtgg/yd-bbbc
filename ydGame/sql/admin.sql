-- 统计表
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player`
(
    `id`            bigint(11) NOT NULL AUTO_INCREMENT,
    `player_id`     bigint(42) NULL DEFAULT NULL,
    `app_id`        int(10)    NULL DEFAULT 1000001 COMMENT '应用id',
    `register_time` int(10)    NULL DEFAULT NULL,
    `type`          int(1)     NULL DEFAULT NULL COMMENT '1:游戏2:fb',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `player_enter_room`;
CREATE TABLE `player_enter_room`
(
    `id`              bigint(20)    NOT NULL AUTO_INCREMENT,
    `player_id`       bigint(20)    NULL DEFAULT NULL,
    `app_id`          int(10)       NULL DEFAULT NULL,
    `register_time`   int(10)       NULL DEFAULT NULL,
    `enter_time`      int(10)       NULL DEFAULT NULL,
    `room_base_point` double(10, 2) NULL DEFAULT NULL,
    `room_type`       int(10)       NULL DEFAULT NULL COMMENT '1:金币；2:筹码',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `player_order`;
CREATE TABLE `player_order`
(
    `id`            bigint(20)    NOT NULL AUTO_INCREMENT,
    `player_id`     bigint(20)    NULL DEFAULT NULL COMMENT '玩家ID',
    `amount`        double(10, 2) NULL DEFAULT NULL COMMENT '金额',
    `create_time`   int(10)       NULL DEFAULT NULL COMMENT '下单时间',
    `app_id`        int(10)       NULL DEFAULT NULL COMMENT '用户渠道',
    `register_time` int(10)       NULL DEFAULT NULL COMMENT '用户注册时间',
    `type`          int(10)       NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `playerId_index` (`player_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '支付订单'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `player_pump`;
CREATE TABLE `player_pump`
(
    `id`              int(11)       NOT NULL AUTO_INCREMENT,
    `player_id`       bigint(11)    NULL DEFAULT NULL,
    `app_id`          int(10)       NULL DEFAULT 1000001 COMMENT '应用id',
    `register_time`   int(10)       NULL DEFAULT 0 COMMENT '注册时间',
    `pump`            double(10, 2) NULL DEFAULT 0,
    `room_base_point` double(10, 2) NULL DEFAULT NULL,
    `room_type`       int(10)       NULL DEFAULT NULL,
    `coin_type`       int(10)       NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `playerId_index` (`player_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `player_withdrawal`;
CREATE TABLE `player_withdrawal`
(
    `id`            bigint(20)                                                   NOT NULL AUTO_INCREMENT,
    `player_id`     bigint(20)                                                   NULL DEFAULT NULL COMMENT '玩家ID',
    `transfer_id`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `create_time`   int(10)                                                      NULL DEFAULT NULL COMMENT '订单创建时间',
    `app_id`        int(10)                                                      NULL DEFAULT NULL COMMENT '用户渠道',
    `register_time` int(10)                                                      NULL DEFAULT NULL COMMENT '用户注册时间',
    `amount`        double(10, 2)                                                NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `pi_index` (`player_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '提现订单'
  ROW_FORMAT = Dynamic;

-- 统计表
DROP TABLE IF EXISTS `player_new_stat`;
CREATE TABLE `player_new_stat`
(
    `id`              int(10)       NOT NULL AUTO_INCREMENT,
    `app_id`          int(10)       NOT NULL COMMENT 'appId',
    `register_time`   int(10)       NOT NULL COMMENT '注册日期',
    `dau`             int(10)       NULL DEFAULT 0 COMMENT 'dau',
    `game_num`        int(10)       NULL DEFAULT 0 COMMENT '新用户-游戏',
    `fb_num`          int(10)       NULL DEFAULT 0 COMMENT '新用户-FB',
    `pay_num`         int(10)       NULL DEFAULT 0 COMMENT '充值人数',
    `pay_count`       int(10)       NULL DEFAULT 0 COMMENT '充值笔数',
    `pay_amount`      double(10, 2) NULL DEFAULT 0 COMMENT '充值金额',
    `pay_ratio`       double(10, 2) NULL DEFAULT 0 COMMENT '付费比',
    `pay_average`     double(10, 2) NULL DEFAULT 0 COMMENT '平均充值',
    `pay_average_new` double(10, 2) NULL DEFAULT 0 COMMENT '平均充值-新',
    `pay_average_old` double(10, 2) NULL DEFAULT 0 COMMENT '平均充值-旧',
    `coin_amount`     double(10, 2) NULL DEFAULT 0 COMMENT '赠送金币',
    `withdraw_num`    int(10)       NULL DEFAULT 0 COMMENT '提现人数',
    `withdraw_count`  int(10)       NULL DEFAULT 0 COMMENT '提现笔数',
    `withdraw_amount` double(10, 2) NULL DEFAULT 0 COMMENT '提现金额',
    `pump_num`        int(10)       NULL DEFAULT 0 COMMENT '抽水人数',
    `pump_count`      int(10)       NULL DEFAULT 0 COMMENT '抽水次数',
    `pump_amount`     double(10, 2) NULL DEFAULT 0 COMMENT '抽水金额',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `player_whole_stat`;
CREATE TABLE `player_whole_stat`
(
    `id`              int(11)       NOT NULL AUTO_INCREMENT,
    `app_id`          int(10)       NOT NULL COMMENT 'appId',
    `game_num`        int(10)       NULL DEFAULT 0 COMMENT '新增玩家数-游戏',
    `fb_num`          int(10)       NULL DEFAULT 0 COMMENT '新增玩家数-FB',
    `pay_num`         int(10)       NULL DEFAULT 0 COMMENT '充值人数',
    `pay_count`       int(10)       NULL DEFAULT 0 COMMENT '充值次数',
    `pay_coin_amount` double(10, 2) NULL DEFAULT 0 COMMENT '充值金额',
    `withdraw_num`    int(10)       NULL DEFAULT 0 COMMENT '提现人数',
    `withdraw_count`  int(10)       NULL DEFAULT 0 COMMENT '提现次数',
    `withdraw_amount` double(10, 2) NULL DEFAULT 0 COMMENT '提现金额',
    `pump_num`        int(10)       NULL DEFAULT 0 COMMENT '抽水人数',
    `pump_count`      int(10)       NULL DEFAULT 0 COMMENT '抽水次数',
    `pump_amount`     double(10, 2) NULL DEFAULT 0 COMMENT '抽水金额',
    `dau`             int(10)       NULL DEFAULT 0 COMMENT 'dau',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;