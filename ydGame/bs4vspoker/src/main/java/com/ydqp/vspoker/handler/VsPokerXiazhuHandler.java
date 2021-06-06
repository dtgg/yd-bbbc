package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Player;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerXiazhu;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.service.PlayerService;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;

@ServerHandler(module = "vsPoker", command = 7000007)
public class VsPokerXiazhuHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerXiazhuHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerXiazhu vsPokerXiazhu = (VsPokerXiazhu) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(vsPokerXiazhu.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        VsPokerRoom vsPokerRoom = RoomManager.getInstance().getRoom(vsPokerXiazhu.getRoomId());
        if (vsPokerRoom.getStatus() != 2) {
            logger.error("玩家下注，房间不在下注时间内");
            return;
        }

        Player player = PlayerDao.getInstance().queryById(playerData.getPlayerId());
        if (vsPokerRoom.getRoomType() == 3) {
            if(player == null) {
                logger.error("no player");
                return;
            }
            playerData.setZjPoint(player.getZjPoint());

            if(vsPokerXiazhu.getMoney() > player.getZjPoint()) {
                logger.error("玩家下注，身上钱不够，playerId = {}， money = {} playerMoney= {}", player.getId(), vsPokerXiazhu.getMoney(),
                        player.getZjPoint());
                return;
            }
        }

        BattleRole battleRole = vsPokerRoom.getBattleRoleMap().get(playerData.getPlayerId());
        if (battleRole == null) {
            logger.error("玩家下注，获取不到玩家，playerId = {}， money = {} playerMoney= {}", playerData.getPlayerId());
            return;
        }
        if (vsPokerRoom.getRoomType() == 1 ||vsPokerRoom.getRoomType() == 2) {
            if(vsPokerXiazhu.getMoney() > battleRole.getPlayerZJ()) {
                logger.error("玩家下注，身上钱不够，playerId = {}， money = {} playerMoney= {}", battleRole.getPlayerId(), vsPokerXiazhu.getMoney(),
                        battleRole.getPlayerZJ());
                return;
            }
        }

        vsPokerRoom.playerXiazhu(player, battleRole, vsPokerXiazhu);
    }
}
