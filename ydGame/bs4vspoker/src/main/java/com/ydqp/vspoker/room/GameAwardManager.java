package com.ydqp.vspoker.room;

import java.util.HashMap;
import java.util.Map;

public class GameAwardManager {
    private GameAwardManager() {}

    private static final GameAwardManager instance = new GameAwardManager();

    public static GameAwardManager getInstance() {
        return instance;
    }

    private static final Map<Integer, Double> gameAwardMap = new HashMap<>();

    static {
        gameAwardMap.put(1, 3000D);
        gameAwardMap.put(2, 2000D);
        gameAwardMap.put(3, 1200D);
        gameAwardMap.put(4, 600D);
        gameAwardMap.put(5, 600D);
        gameAwardMap.put(6, 600D);
        gameAwardMap.put(7, 500D);
        gameAwardMap.put(8, 500D);
        gameAwardMap.put(9, 500D);
        gameAwardMap.put(10, 500D);
    }

    public Map<Integer, Double> getGameAwardMap() {
        return gameAwardMap;
    }
}
