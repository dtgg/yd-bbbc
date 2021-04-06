-- 赛事
CREATE TABLE `vs_race` (
  `id` int(10) NOT NULL,
  `raceType` int(10) DEFAULT 1 COMMENT '1 免费、2 报名 3 zj',
  `basePoint` int(10) DEFAULT 1,
  `maxPlayerNum` int(10) DEFAULT 1 COMMENT '最大人数',
  `curPlayerNum` int(10) DEFAULT 0 COMMENT '当前人数',
  `status` int(10) DEFAULT 0 COMMENT '0 未开始 1 正在比赛 2 比赛结束',
  `beginTime` int(10) DEFAULT NULL,
  `createTime` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;