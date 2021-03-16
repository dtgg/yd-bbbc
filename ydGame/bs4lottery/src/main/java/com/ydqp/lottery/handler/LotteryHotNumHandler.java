package com.ydqp.lottery.handler;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.dao.lottery.LotteryDao;
import com.ydqp.common.sendProtoMsg.lottery.LotteryHotNumSuc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerHandler(module = "lottery", command = 5000018)
public class LotteryHotNumHandler implements IServerHandler {
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        List<Map<String, Object>> hotNums = LotteryDao.getInstance().getHotNum();

        Map<String, List<Map<String, Object>>> typeHotNumMap = new HashMap<>();
        hotNums.forEach(map -> {
            String type = String.valueOf(map.get("type"));
            if (typeHotNumMap.get(type) == null) {
                map.remove("type");
                typeHotNumMap.put(type, new ArrayList<Map<String, Object>>(){{add(map);}});
            } else {
                typeHotNumMap.get(type).add(map);
            }
        });

        Map<String, String> map = new HashMap<>();
        typeHotNumMap.forEach((k, v) -> {
            map.put(k, JSONObject.toJSONString(v));
        });

        LotteryHotNumSuc hotNumSuc = new LotteryHotNumSuc();
        hotNumSuc.setHotNums(map);
        iSession.sendMessageByID(hotNumSuc, abstartParaseMessage.getConnId());
    }
}
