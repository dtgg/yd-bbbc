package com.ydqp.lobby.handler.mission;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.lottery.PlayerPromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.data.Total;
import com.ydqp.common.entity.Player;
import com.ydqp.common.entity.PlayerPromote;
import com.ydqp.common.receiveProtoMsg.mission.ParityOrderLv;
import com.ydqp.common.sendProtoMsg.mission.ParityOrderDetailInfo;
import com.ydqp.common.sendProtoMsg.mission.ParityOrderLvSuc;
import com.ydqp.lobby.service.PlayerService;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ServerHandler(module = "mission", command = 1000072)
public class ParityOrderLvHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(ParityOrderLvHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        ParityOrderLv parityOrderLv = (ParityOrderLv) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(parityOrderLv.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        Total bonus = PlayerPromoteDao.getInstance().sumBonus(playerData.getPlayerId(), parityOrderLv.getLv());
        Total total = PlayerPromoteDao.getInstance().count(playerData.getPlayerId(), parityOrderLv.getLv());

        int offset = (parityOrderLv.getPage() - 1) * parityOrderLv.getSize();
        List<PlayerPromote> promoteList = PlayerPromoteDao.getInstance().findChildren(
                playerData.getPlayerId(), parityOrderLv.getLv(), offset, parityOrderLv.getSize());

        List<ParityOrderDetailInfo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(promoteList)) {
            Set<String> playerIds = promoteList.stream()
                    .map(playerPromote -> String.valueOf(playerPromote.getPlayerId())).collect(Collectors.toSet());

            List<Player> players = PlayerService.getInstance().findAllByIds(playerIds);
            Map<Long, Player> playerMap = players.stream().collect(Collectors.toMap(Player::getId, Function.identity()));

            for (PlayerPromote playerPromote : promoteList) {
                ParityOrderDetailInfo info = new ParityOrderDetailInfo();
                info.setPlayerId(playerPromote.getPlayerId());
                info.setNickname(playerPromote.getNickname());

                if (playerPromote.getSuperiorId() == playerData.getPlayerId()) {
                    info.setContribute(playerPromote.getSuperiorAmount() == null ? 0 : playerPromote.getSuperiorAmount().doubleValue());
                } else {
                    info.setContribute(playerPromote.getGrandAmount() == null ? 0 : playerPromote.getGrandAmount().doubleValue());
                }

                info.setRechargeAmount(playerMap.get(playerPromote.getPlayerId()).getOrderAmount());
                info.setWithdrawAmount(playerMap.get(playerPromote.getPlayerId()).getWithdrawAmount());
                info.setRecommendNumber(playerPromote.getSubNum());
                list.add(info);
            }
        }

        ParityOrderLvSuc suc = new ParityOrderLvSuc();
        suc.setInfos(list);
        suc.setPlayerId(playerData.getPlayerId());
        suc.setCount(total.getTotal());
        suc.setBonus(bonus.getSum());
        iSession.sendMessageByID(suc, parityOrderLv.getConnId());
    }
}
