-- 赛事

CREATE TABLE `vs_race` (
  `id` int(10) NOT NULL,
  `raceType` int(10) DEFAULT 1 COMMENT '1 免费、2 报名 3 zj',
  `basePoint` int(10) DEFAULT 1,
  `maxPlayerNum` int(10) DEFAULT 1 COMMENT '最大人数',
  `curPlayerNum` int(10) DEFAULT 0 COMMENT '当前人数',
  `totalRound` int(10) DEFAULT 15 COMMENT '本次比赛最多的局数',
  `status` int(10) DEFAULT 0 COMMENT '0 未开始 1 正在比赛 2 比赛结束',
  `beginTime` int(10) DEFAULT NULL,
  `createTime` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 报名记录
CREATE TABLE `vs_player_race`(
    `id`         int(10) NOT NULL AUTO_INCREMENT,
    `playerId`   bigint(20) DEFAULT NULL,
    `raceId`     int(10) DEFAULT NULL,
    `raceType`   int(10) DEFAULT 1,
    `basePoint`  int(10) DEFAULT 1,
    `rank`       int(10) DEFAULT 0,
    `createTime` int(10) DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE vs_player_race ADD COLUMN bonus double(15,2) DEFAULT 0.00 COMMENT '奖励';
ALTER TABLE vs_player_race ADD COLUMN point double(15,2) DEFAULT 0.00 COMMENT '赛事分数';

--
ALTER TABLE vs_player_race ADD COLUMN appId int(10) DEFAULT 1000001;
ALTER TABLE vs_player_race ADD COLUMN kfId int(10) DEFAULT 0;
ALTER TABLE vs_player_race ADD COLUMN isVir int(10) DEFAULT 0;
ALTER TABLE vs_player_race ADD COLUMN isAward int(10) DEFAULT 0;

--
ALTER TABLE player MODIFY COLUMN `isVir` int(10) DEFAULT 0;
ALTER TABLE player_promote ADD COLUMN raceNum int(10) DEFAULT 0;