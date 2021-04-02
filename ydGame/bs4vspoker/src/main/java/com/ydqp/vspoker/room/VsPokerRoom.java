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
    private double aBetPool;
    @Setter
    @Getter
    private double bBetPool;
    @Setter
    @Getter
    private double cBetPool;
    @Setter
    @Getter
    private double dBetPool;

    @Getter
    @Setter
    private Map<Long, Double> aBetBattleRoleId = new ConcurrentHashMap<>();
    @Getter
    @Setter
    private Map<Long, Double> bBetBattleRoleId = new ConcurrentHashMap<>();
    @Getter
    @Setter
    private Map<Long, Double> cBetBattleRoleId = new ConcurrentHashMap<>();
    @Getter
    @Setter
    private Map<Long, Double> dBetBattleRoleId = new ConcurrentHashMap<>();
    @Getter
    @Setter
    private int aWin = 1;
    @Getter
    @Setter
    private int bWin = 1;
    @Getter
    @Setter
    private int cWin = 1;
    @Getter
    @Setter
    private int dWin = 1;

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
        //初始化用户信息
        BattleRole battleRole = this.enterRoom(playerData, iSession);
        //设置房间信息
        this.getVsPokerRoomInfo(battleRole);
    }

    public void getVsPokerRoomInfo (BattleRole battleRole){
        SVsPokerRoomInfo sVsPokerRoomInfo = new SVsPokerRoomInfo();
        sVsPokerRoomInfo.setRoomId(this.getRoomId());
        sVsPokerRoomInfo.setCurWaitTime(this.getCurWaitTime());
        sVsPokerRoomInfo.setRoomStatus(this.getStatus());
        sVsPokerRoomInfo.setBattleRoleMoney(battleRole.getPlayerZJ());

        SPlayerInfo AsPlayerInfo = new SPlayerInfo();
        AsPlayerInfo.setBetPool(this.getABetPool());

        SPlayerInfo BsPlayerInfo = new SPlayerInfo();
        BsPlayerInfo.setBetPool(this.getBBetPool());

        SPlayerInfo CsPlayerInfo = new SPlayerInfo();
        CsPlayerInfo.setBetPool(this.getCBetPool());

        SPlayerInfo DsPlayerInfo = new SPlayerInfo();
        DsPlayerInfo.setBetPool(this.getDBetPool());



        if (this.getStatus() == 2 || this.getStatus() == 3) {
            AsPlayerInfo.setPoker(this.getPokerMap().get(1));
            AsPlayerInfo.setWin(this.aWin);

            BsPlayerInfo.setPoker(this.getPokerMap().get(1));
            BsPlayerInfo.setWin(this.aWin);

            CsPlayerInfo.setPoker(this.getPokerMap().get(1));
            CsPlayerInfo.setWin(this.aWin);

            DsPlayerInfo.setPoker(this.getPokerMap().get(1));
            DsPlayerInfo.setWin(this.aWin);

            sVsPokerRoomInfo.setBankPoker(this.getPokerMap().get(0));
        }

        sVsPokerRoomInfo.setAPlayer(AsPlayerInfo);
        sVsPokerRoomInfo.setBPlayer(BsPlayerInfo);
        sVsPokerRoomInfo.setCPlayer(CsPlayerInfo);
        sVsPokerRoomInfo.setDPlayer(DsPlayerInfo);

        this.sendMessageToBattle(sVsPokerRoomInfo, battleRole.getPlayerId());
    }

    public void playerXiazhu(Player player, BattleRole battleRole, VsPokerXiazhu vsPokerXiazhu) {

        battleRole.setPlayerZJ(battleRole.getPlayerZJ() - vsPokerXiazhu.getMoney());
        SVsPlayerXiazhu sVsPlayerXiazhu = new SVsPlayerXiazhu();
        sVsPlayerXiazhu.setRoomId(this.getRoomId());
        sVsPlayerXiazhu.setPlayerId(battleRole.getPlayerId());
        sVsPlayerXiazhu.setBetMoney(vsPokerXiazhu.getMoney());

        if (vsPokerXiazhu.getPlayType() == 1) {
            this.setABetPool(this.getABetPool() + vsPokerXiazhu.getMoney());
            sVsPlayerXiazhu.setBetPool(this.getABetPool());
            sVsPlayerXiazhu.setPlayType(1);
        } else if (vsPokerXiazhu.getPlayType() == 2) {
            this.setBBetPool(this.getBBetPool() + vsPokerXiazhu.getMoney());
            sVsPlayerXiazhu.setBetPool(this.getBBetPool());
            sVsPlayerXiazhu.setPlayType(2);
        } else if (vsPokerXiazhu.getPlayType() == 3) {
            this.setCBetPool(this.getCBetPool() + vsPokerXiazhu.getMoney());
            sVsPlayerXiazhu.setBetPool(this.getCBetPool());
            sVsPlayerXiazhu.setPlayType(3);
        } else if (vsPokerXiazhu.getPlayType() == 4) {
            this.setDBetPool(this.getBBetPool() +vsPokerXiazhu.getMoney());
            sVsPlayerXiazhu.setBetPool(this.getDBetPool());
            sVsPlayerXiazhu.setPlayType(4);
        }

        this.sendMessageToBattlesByFilter(sVsPlayerXiazhu, battleRole.getPlayerId());

        sVsPlayerXiazhu.setBattleRoleMoney(battleRole.getPlayerZJ());
        this.sendMessageToBattle(sVsPlayerXiazhu, battleRole.getPlayerId());

    }

}
