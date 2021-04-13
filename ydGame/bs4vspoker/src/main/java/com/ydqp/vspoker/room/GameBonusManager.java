package com.ydqp.vspoker.room;

import java.util.HashMap;
import java.util.Map;

public class GameBonusManager {
    private GameBonusManager() {}

    private static final GameBonusManager instance = new GameBonusManager();

    public static GameBonusManager getInstance() {
        return instance;
    }

    private static final Map<Integer, Map<Integer, Double>> gameBonusMap = new HashMap<>();

    static {
        Map<Integer, Double> freeGameBonusMap = new HashMap<>();
        freeGameBonusMap.put(1, 3000D);
        freeGameBonusMap.put(2, 2000D);
        freeGameBonusMap.put(3, 1200D);
        freeGameBonusMap.put(4, 600D);
        freeGameBonusMap.put(5, 600D);
        freeGameBonusMap.put(6, 600D);
        freeGameBonusMap.put(7, 500D);
        freeGameBonusMap.put(8, 500D);
        freeGameBonusMap.put(9, 500D);
        freeGameBonusMap.put(10, 500D);
        gameBonusMap.put(1, freeGameBonusMap);

        Map<Integer, Double> signInGameBonusMap = new HashMap<>();
        signInGameBonusMap.put(1, 0.5);
        signInGameBonusMap.put(2, 0.3);
        signInGameBonusMap.put(3, 0.1);
        gameBonusMap.put(2, signInGameBonusMap);
    }

    public Map<Integer, Double> gameBonusMap(int roomType) {
        return gameBonusMap.get(roomType) == null ? new HashMap<>() : gameBonusMap.get(roomType);
    }

    public Double getBonus(VsPokerRoom vsPokerRoom, int rank) {
        Double aDouble = gameBonusMap.get(vsPokerRoom.getRoomType()).get(rank);
        if (aDouble == null) aDouble = 0D;
        if (vsPokerRoom.getRoomType() == 1) {
            return aDouble;
        } else if (vsPokerRoom.getRoomType() == 2) {
            return vsPokerRoom.getBonus() * aDouble;
        }
        return 0D;
    }
}
