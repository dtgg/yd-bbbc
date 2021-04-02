package com.ydqp.vspoker.handler;

import com.alibaba.fastjson.JSONObject;
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
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerEnterRoom;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;

@ServerHandler(module = "vsPoker", command = 7000005)
public class VsPokerEnterRoomHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerXiazhuHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerEnterRoom vsPokerEnterRoom = (VsPokerEnterRoom) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(vsPokerEnterRoom.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        // 只有zj 句我们才去获取用户身上的钱
        if (vsPokerEnterRoom.getRoomType() == 3) {
            Player player = PlayerDao.getInstance().queryById(playerData.getPlayerId());
            if(player == null) {
                logger.error("no player");
                return;
            }
            playerData.setZjPoint(player.getZjPoint());
        }

        VsPokerBasePlay vsPokerBasePlay = PlayVsPokerManager.getInstance().getPlayObject(vsPokerEnterRoom.getRoomType(), 1,
                vsPokerEnterRoom.getRaceId());
        if (vsPokerBasePlay == null) {
            logger.error("未找到对应的玩法类型，{} ", JSONObject.toJSONString(vsPokerEnterRoom));
            return;
        }
        vsPokerBasePlay.enterRoom(playerData, iSession, 0);
    }
}
