package com.ydqp.vspoker.room.play;

import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Player;
import com.ydqp.common.entity.VsPlayerRace;
import com.ydqp.common.entity.VsRace;
import com.ydqp.common.service.PlayerService;
import com.ydqp.common.utils.CommonUtils;
import com.ydqp.vspoker.cache.RankingCache;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import com.ydqp.vspoker.room.VsPokerRoom;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

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
    private final List<Integer> fastRaceList = new ArrayList<>();
    @Getter
    private final List<VsPokerBasePlay> freeRacePlayObjects = new ArrayList<>();

    private final List<VsPokerBasePlay> zjRacePlayObjects = new ArrayList<>();

    private final List<VsPokerBasePlay> zjPlayObjects = new ArrayList<>();


    public void generaPlayObject(int roomType, int basePoint, VsRace vsRace, int totalRound) {
        VsPokerRoom vsPokerRoom = null;

        if (roomType == 1) {
            //生成免费赛
            RaceVsPokerPlayObject freeRaceVsPokerPlayObject = new RaceVsPokerPlayObject(basePoint,roomType, vsRace.getId(), totalRound);
            freeRacePlayObjects.add(freeRaceVsPokerPlayObject);
            vsPokerRoom = freeRaceVsPokerPlayObject.generatorRoom();

        }

        if (roomType == 2) {
            //包名赛
            RaceVsPokerPlayObject zjRaceVsPokerPlayObject = new RaceVsPokerPlayObject(basePoint,roomType, vsRace.getId(), totalRound);
            zjRacePlayObjects.add(zjRaceVsPokerPlayObject);
            vsPokerRoom = zjRaceVsPokerPlayObject.generatorRoom();
            vsPokerRoom.setBonus(vsRace.getBasePoint() * vsRace.getCurPlayerNum());
        }

        addPlayerInfo(vsPokerRoom, vsRace.getId());
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
        } else if (roomType == 3) {
            for(VsPokerBasePlay vsPokerBasePlay : zjPlayObjects) {
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

                RankingCache.getInstance().addRank(raceId, 0D, player.getId());
            }
        }
    }

    public void loadFastRaceConfig () {
        fastRaceList.add(100);
        fastRaceList.add(500);
        fastRaceList.add(1000);

        for (Integer integer : fastRaceList) {
            FastRaceVsPlayObject playChipObject = new FastRaceVsPlayObject(integer, 2);
            zjRacePlayObjects.add(playChipObject);
        }
    }
}
