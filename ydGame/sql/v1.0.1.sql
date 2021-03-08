
alter table player add column appId int(10) default 1000001 comment '应用id';

-- 任务配置
ALTER TABLE `task_config` ADD COLUMN `rewardType` int(1) DEFAULT 1 COMMENT '奖励类型(1: 筹码, 2:金币)' AFTER `reward`;
ALTER TABLE `task_config` ADD COLUMN `enabled` int(1) DEFAULT 0 COMMENT '是否启用';
INSERT INTO `task_config` VALUES (8, 'Day1', 1, 1, 2, 9, 0, 1);
INSERT INTO `task_config` VALUES (9, 'Day2', 1, 1, 2, 10, 0, 1);
INSERT INTO `task_config` VALUES (10, 'Day3', 1, 1, 2, 11, 0, 1);
INSERT INTO `task_config` VALUES (11, 'Day4', 1, 2, 2, 12, 0, 1);
INSERT INTO `task_config` VALUES (12, 'Day5', 1, 2, 2, 13, 0, 1);
INSERT INTO `task_config` VALUES (13, 'Day6', 1, 3, 2, 14, 0, 1);
INSERT INTO `task_config` VALUES (14, 'Day7', 1, 3, 2, NULL, 0, 1);

ALTER TABLE `player_reward_history` ADD COLUMN `rewardType` int(1) DEFAULT 1 COMMENT '奖励类型(1: 筹码, 2:金币)' AFTER `rewardSource`;

-- 渠道、注册时间
ALTER TABLE `player_order` ADD COLUMN `appId` int(10) DEFAULT NULL COMMENT '用户渠道';
ALTER TABLE `player_order` ADD COLUMN `registerTime` int(10) DEFAULT NULL COMMENT '用户注册时间';
ALTER TABLE `player_withdrawal` ADD COLUMN `appId` int(10) DEFAULT NULL COMMENT '用户渠道';
ALTER TABLE `player_withdrawal` ADD COLUMN `registerTime` int(10) DEFAULT NULL COMMENT '用户注册时间';

ALTER TABLE `player`
	MODIFY COLUMN `zjPoint` double(15,2)  NULL DEFAULT 0.0 COMMENT '玩家的zj' AFTER `roomId`;
alter table pay_channel_config add column businessAccount varchar(255) default null;
alter table player_account add column faId varchar(50) default null;

