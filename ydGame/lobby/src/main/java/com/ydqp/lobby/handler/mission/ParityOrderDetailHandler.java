package com.ydqp.lobby.handler.mission;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerPromoteDetailDao;
import com.ydqp.common.dao.lottery.PlayerPromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PlayerPromote;
import com.ydqp.common.entity.PlayerPromoteDetail;
import com.ydqp.common.receiveProtoMsg.mission.ParityOrderDetail;
import com.ydqp.common.sendProtoMsg.mission.ParityOrderDetailInfo;
import com.ydqp.common.sendProtoMsg.mission.ParityOrderDetailSuc;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerHandler(module = "mission", command = 5000012)
public class ParityOrderDetailHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(ParityOrderDetailHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        ParityOrderDetail detail = (ParityOrderDetail) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(detail.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        //全部的下级
        List<PlayerPromote> promoteList = PlayerPromoteDao.getInstance().findChildren(playerData.getPlayerId());
        List<Long> playerPromoteIds = new ArrayList<>();
        Map<Long, PlayerPromote> map = new HashMap<>();
        promoteList.forEach(playerPromote -> {
            playerPromoteIds.add(playerPromote.getPlayerId());
            map.put(playerPromote.getPlayerId(), playerPromote);
        });

        List<ParityOrderDetailInfo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(playerPromoteIds)) {
            //下注详情
            List<PlayerPromoteDetail> details = PlayerPromoteDetailDao.getInstance().findByPlayerIds(playerPromoteIds);
            for (PlayerPromoteDetail promoteDetail : details) {
                ParityOrderDetailInfo info = new ParityOrderDetailInfo();
                info.setPlayerId(promoteDetail.getPlayerId());
                info.setNickname(promoteDetail.getNickname());
                info.setDate(promoteDetail.getCreateTime());

                PlayerPromote playerPromote = map.get(promoteDetail.getPlayerId());
                if (playerData.getPlayerId() == playerPromote.getSuperiorId()) {
                    info.setContribute(promoteDetail.getSuperiorAmount().doubleValue());
                } else {
                    info.setContribute(promoteDetail.getGrandAmount().doubleValue());
                }
                if (info.getContribute() == 0) continue;

                list.add(info);
            }
        }

        ParityOrderDetailSuc suc = new ParityOrderDetailSuc();
        suc.setParityOrderDetailInfos(list);
        suc.setPlayerId(playerData.getPlayerId());
        iSession.sendMessageByID(suc, detail.getConnId());
    }
}
