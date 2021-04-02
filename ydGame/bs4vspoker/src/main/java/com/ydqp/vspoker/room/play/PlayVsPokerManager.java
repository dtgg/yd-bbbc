package com.ydqp.vspoker.room.play;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PlayVsPokerManager {

    private PlayVsPokerManager(){
    }

    public static PlayVsPokerManager instance = new PlayVsPokerManager();

    public static PlayVsPokerManager getInstance() {
        if(instance == null) {
            instance = new PlayVsPokerManager();
        }
        return instance;
    }

    @Getter
    private final List<VsPokerBasePlay> freeRacePlayObjects = new ArrayList<>();
    private final List<VsPokerBasePlay> zjRacePlayObjects = new ArrayList<>();


    public void generaPlayObject(int roomType, int basePoint) {
        if (roomType == 1) {
            //生成免费赛
            RaceVsPokerPlayObject freeRaceVsPokerPlayObject = new RaceVsPokerPlayObject(basePoint,roomType);
            freeRacePlayObjects.add(freeRaceVsPokerPlayObject);
            freeRaceVsPokerPlayObject.generatorRoom();
        }

        if (roomType == 2) {
            //包名赛
            RaceVsPokerPlayObject zjRaceVsPokerPlayObject = new RaceVsPokerPlayObject(basePoint,roomType);
            zjRacePlayObjects.add(zjRaceVsPokerPlayObject);
            zjRaceVsPokerPlayObject.generatorRoom();
        }


    }

    public VsPokerBasePlay getPlayObject(int roomType, int basePoint) {

        if (roomType == 1) {
            //目前免费赛只有一个
            for(VsPokerBasePlay vsPokerBasePlay : freeRacePlayObjects) {
                if (vsPokerBasePlay.checkTheRoomType(roomType, basePoint)) {
                    return vsPokerBasePlay;
                }
            }
        } else if (roomType == 2) {
            for(VsPokerBasePlay vsPokerBasePlay : zjRacePlayObjects) {
                if (vsPokerBasePlay.checkTheRoomType(roomType, basePoint)) {
                    return vsPokerBasePlay;
                }
            }
        }

        return null;
    }
}
