package com.ydqp.lobby.service.mall;

import com.ydqp.common.entity.PlayerOrder;
import com.ydqp.common.entity.PlayerWithdrawal;
import com.ydqp.lobby.dao.mall.PlayerOrderDao;
import com.ydqp.lobby.dao.mall.PlayerWithdrawalDao;

import java.util.List;
import java.util.Map;

public class PlayerOrderService {

    private PlayerOrderService() {}

    private static PlayerOrderService instance = new PlayerOrderService();

    public static PlayerOrderService getInstance() {
        if (instance == null) instance = new PlayerOrderService();
        return instance;
    }

    public void saveOrder(Map<String, Object> params) {
        PlayerOrderDao.getInstance().saveOrder(params);
    }

    public PlayerOrder queryOrder(String txnOrderId) {
        return PlayerOrderDao.getInstance().queryOrder(txnOrderId);
    }

    public void updateOrder(Object[] params) {
        PlayerOrderDao.getInstance().updateOrder(params);
    }

    public List<PlayerOrder> page(long playerId, Integer page, Integer limit) {
        Integer offset = null;
        if (page != null && limit != null) {
            offset = (page - 1) * limit;
        }
        return PlayerOrderDao.getInstance().page(playerId, offset, limit);
    }
}
