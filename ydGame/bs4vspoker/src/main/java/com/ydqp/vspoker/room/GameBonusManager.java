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
        freeGameBonusMap.put(1, 2000D);
        freeGameBonusMap.put(2, 1200D);
        freeGameBonusMap.put(3, 800D);
        freeGameBonusMap.put(4, 600D);
        freeGameBonusMap.put(5, 400D);
        for (int i = 6; i <= 30; i++) {
            freeGameBonusMap.put(i, 200D);
        }
        gameBonusMap.put(1, freeGameBonusMap);

        Map<Integer, Double> signInGameBonusMap = new HashMap<>();
        signInGameBonusMap.put(1, 2000D);
        signInGameBonusMap.put(2, 1200D);
        signInGameBonusMap.put(3, 800D);
        signInGameBonusMap.put(4, 600D);
        signInGameBonusMap.put(5, 400D);
        signInGameBonusMap.put(6, 200D);
        signInGameBonusMap.put(7, 200D);
        signInGameBonusMap.put(8, 200D);
        signInGameBonusMap.put(9, 200D);
        signInGameBonusMap.put(10, 200D);
        gameBonusMap.put(2, signInGameBonusMap);
    }

    public Map<Integer, Double> gameBonusMap(int roomType) {
        return gameBonusMap.get(roomType) == null ? new HashMap<>() : gameBonusMap.get(roomType);
    }

    public Double getBonus(VsPokerRoom vsPokerRoom, int rank) {
        Double aDouble = gameBonusMap.get(vsPokerRoom.getRoomType()).get(rank);
        if (aDouble == null) aDouble = 0D;
        return aDouble;
//        if (vsPokerRoom.getRoomType() == 1) {
//            return aDouble;
//        } else if (vsPokerRoom.getRoomType() == 2) {
//            return vsPokerRoom.getBonus() * aDouble;
//        }
//        return 0D;
    }
}
