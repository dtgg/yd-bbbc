package com.ydqp.vspoker.room.play;

import com.cfq.connection.ISession;
import com.cfq.connection.ManagerSession;
import com.cfq.connection.NettySession;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Player;
import com.ydqp.common.entity.VsPlayerRace;
import com.ydqp.common.service.PlayerService;
import com.ydqp.common.utils.CommonUtils;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import com.ydqp.vspoker.room.VsPokerRoom;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    public void generaPlayObject(int roomType, int basePoint, int raceId, int totalRound) {
        VsPokerRoom vsPokerRoom = null;

        if (roomType == 1) {
            //生成免费赛
            RaceVsPokerPlayObject freeRaceVsPokerPlayObject = new RaceVsPokerPlayObject(basePoint,roomType, raceId, totalRound);
            freeRacePlayObjects.add(freeRaceVsPokerPlayObject);
            vsPokerRoom = freeRaceVsPokerPlayObject.generatorRoom();

        }

        if (roomType == 2) {
            //包名赛
            RaceVsPokerPlayObject zjRaceVsPokerPlayObject = new RaceVsPokerPlayObject(basePoint,roomType, raceId, totalRound);
            zjRacePlayObjects.add(zjRaceVsPokerPlayObject);
            vsPokerRoom = zjRaceVsPokerPlayObject.generatorRoom();
        }

        addPlayerInfo(vsPokerRoom, raceId);
    }

    public VsPokerBasePlay getPlayObject(int roomType, int basePoint, int raceId) {

        if (roomType == 1) {
            //目前免费赛只有一个
            for(VsPokerBasePlay vsPokerBasePlay : freeRacePlayObjects) {
                if (vsPokerBasePlay.checkTheRoomType(roomType, basePoint, raceId)) {
                    return vsPokerBasePlay;
                }
            }
        } else if (roomType == 2) {
            for(VsPokerBasePlay vsPokerBasePlay : zjRacePlayObjects) {
                if (vsPokerBasePlay.checkTheRoomType(roomType, basePoint, raceId)) {
                    return vsPokerBasePlay;
                }
            }
        }

        return null;
    }

    public void addPlayerInfo(VsPokerRoom vsPokerRoom, int raceId) {
        List<VsPlayerRace> playerRaces = VsPlayerRaceDao.getInstance().getPlayerRaceByRaceId(raceId);
        if (CollectionUtils.isEmpty(playerRaces)) return;

        List<Long> playerIds = new ArrayList<>();
        for (VsPlayerRace playerRace : playerRaces) {
            playerIds.add(playerRace.getPlayerId());
        }

        List<Player> players = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(playerIds)) {
            players = PlayerService.getInstance().getPlayerByPlayerIds(CommonUtils.longString(playerIds));
        }

        if (vsPokerRoom != null && CollectionUtils.isNotEmpty(players)) {
            for (Player player : players) {
                PlayerData playerData = new PlayerData(player);
                vsPokerRoom.enterRoomByRace(playerData, null);
            }
        }
    }
}
