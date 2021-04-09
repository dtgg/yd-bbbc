package com.ydqp.vspoker.room;

import com.cfq.connection.ISession;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.util.StackTraceUtil;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Player;
import com.ydqp.common.poker.Poker;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.poker.room.Room;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerXiazhu;
import com.ydqp.common.sendProtoMsg.vspoker.SPlayerInfo;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPlayerXiazhu;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRoomInfo;
import com.ydqp.vspoker.cache.RankingCache;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VsPokerRoom extends Room {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerRoom.class);

    // 0 发牌、 1 下注 15s, 2 发送笔牌结果 3 等待客户端播完动画效果 4 发送结算信息
    private final IRoomStatusHandler[] roomStatusHandlers = new IRoomStatusHandler[]{new VsPokerFaPaiHandler(),
    new VsPokerBetHandler(), new VsPokerCompareHandler(), new VsPokerWaitHandler(), new VsPokerSettlementHandler()};

    @Setter
    @Getter
    private Map<Integer, Poker> pokerMap = new ConcurrentHashMap<>(5);

    @Setter
    @Getter
    private int round = 0; //当前局数

    @Setter
    @Getter
    private Map<Integer, PlayerObject> playerObjectMap = new ConcurrentHashMap<>(4); //下注玩家

    @Getter
    @Setter
    private int totalRounds = 15;

    @Override
    public void monitor() {
        try {
            roomStatusHandlers[this.getStatus()].doHandler(this);
        } catch (Exception e) {
            e.printStackTrace();

            logger.error(StackTraceUtil.getStackTrace(e));
        }
    }

    public void vsEnterRoom (PlayerData playerData, ISession iSession) {
        BattleRole battleRole;
        if (this.getRoomType() == 1 || this.getRoomType() == 2) {
            //初始化用户信息
            battleRole = this.enterRoomByRace(playerData, iSession);
        } else {
            //初始化用户信息
            battleRole = this.enterRoom(playerData, iSession);
        }
        if (battleRole == null) {
            return;
        }

        //设置房间信息
        this.getVsPokerRoomInfo(battleRole);
    }

    public void getVsPokerRoomInfo (BattleRole battleRole){
        SVsPokerRoomInfo sVsPokerRoomInfo = new SVsPokerRoomInfo();
        sVsPokerRoomInfo.setRoomId(this.getRoomId());
        sVsPokerRoomInfo.setCurWaitTime(this.getCurWaitTime());
        sVsPokerRoomInfo.setRoomStatus(this.getStatus());
        sVsPokerRoomInfo.setBattleRoleMoney(battleRole.getPlayerZJ());
        Long rankNo = RankingCache.getInstance().getRankNo(this.getRaceId(), battleRole.getPlayerId());
        sVsPokerRoomInfo.setRank(rankNo.intValue() + 1);
        sVsPokerRoomInfo.setRoomType(this.getRoomType());
        sVsPokerRoomInfo.setRound(this.getRound());
        sVsPokerRoomInfo.setRaceId(this.getRaceId());

        for (int i = 1; i <= 4; i++) {
            PlayerObject playerObject = this.getPlayerObjectMap().get(i);
            SPlayerInfo sPlayerInfo = new SPlayerInfo();

            Map<Long, Double> betBattleRoleId = playerObject.getBetBattleRoleId();
            if (betBattleRoleId == null) {
                continue;
            }
            for (Map.Entry<Long, Double> entry : betBattleRoleId.entrySet()) {
                if (entry.getKey() == battleRole.getPlayerId()) {
                    sPlayerInfo.setBetPool(entry.getValue());
                }
            }

            if (this.getStatus() == 2 || this.getStatus() == 3) {
                sPlayerInfo.setPoker(this.getPokerMap().get(i + 1));
                sPlayerInfo.setWin(playerObject.getWin());
                sPlayerInfo.setWinBetPool(sPlayerInfo.getBetPool() * 2);
            }
            sVsPokerRoomInfo.getSPlayerInfoMap().put(i, sPlayerInfo);
        }
        //设置庄家的牌
        if (this.getStatus() == 2 || this.getStatus() == 3) {
            sVsPokerRoomInfo.setBankPoker(this.getPokerMap().get(1));
        }

        this.sendMessageToBattle(sVsPokerRoomInfo, battleRole.getPlayerId());
    }

    public void playerXiazhu(Player player, BattleRole battleRole, VsPokerXiazhu vsPokerXiazhu) {

        battleRole.setPlayerZJ(battleRole.getPlayerZJ() - vsPokerXiazhu.getMoney());
        SVsPlayerXiazhu sVsPlayerXiazhu = new SVsPlayerXiazhu();
        sVsPlayerXiazhu.setRoomId(this.getRoomId());
        sVsPlayerXiazhu.setPlayerId(battleRole.getPlayerId());
        sVsPlayerXiazhu.setBetMoney(vsPokerXiazhu.getMoney());

        PlayerObject playerObject = this.getPlayerObjectMap().get(vsPokerXiazhu.getPlayType());
        if (playerObject == null) {
            logger.error("用户下注,传入类型不对, playtype = {}", vsPokerXiazhu.getPlayType());
            return;
        }
        playerObject.setBetPool(playerObject.getBetPool() + vsPokerXiazhu.getMoney());

        Double betMoney = playerObject.getBetBattleRoleId().get(battleRole.getPlayerId());
        if (betMoney!= null) {
            playerObject.getBetBattleRoleId().put(battleRole.getPlayerId(), betMoney + vsPokerXiazhu.getMoney());
        } else {
            playerObject.getBetBattleRoleId().put(battleRole.getPlayerId(), (double)vsPokerXiazhu.getMoney());
        }

//        sVsPlayerXiazhu.setBetPool(playerObject.getBetPool());
        sVsPlayerXiazhu.setBetPool(playerObject.getBetBattleRoleId().get(battleRole.getPlayerId()));
        sVsPlayerXiazhu.setPlayType(vsPokerXiazhu.getPlayType());

        this.sendMessageToBattlesByFilter(sVsPlayerXiazhu, battleRole.getPlayerId());

        sVsPlayerXiazhu.setBattleRoleMoney(battleRole.getPlayerZJ());
        if (battleRole.getIsVir() == 0) {
            this.sendMessageToBattle(sVsPlayerXiazhu, battleRole.getPlayerId());
        }
    }
}
