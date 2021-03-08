
alter table game_version add column appId int(10) default 1000001 comment '应用id';

ALTER TABLE player ADD COLUMN `banLogin` int(10) DEFAULT 0 COMMENT '禁止登陆';

-- 提现手续费配置
ALTER TABLE `pay_channel_config` ADD COLUMN `withdrawFee` decimal(2,2) DEFAULT 0 COMMENT '提现手续费';