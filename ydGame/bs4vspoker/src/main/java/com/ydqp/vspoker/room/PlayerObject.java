package com.ydqp.vspoker.room;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerObject {

    @Setter
    @Getter
    private double betPool = 0;

    @Getter
    @Setter
    private Map<Long, Double> betBattleRoleId = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private int win = 1;

}
