package com.ydqp.lobby.service.mall;

import com.ydqp.common.entity.PlayerWithdrawal;
import com.ydqp.lobby.dao.mall.PlayerWithdrawalDao;

import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class PlayerWithdrawalService {

    private PlayerWithdrawalService() {
    }

    private static PlayerWithdrawalService instance = new PlayerWithdrawalService();

    public static PlayerWithdrawalService getInstance() {
        if (instance == null) instance = new PlayerWithdrawalService();
        return instance;
    }

    public void savePlayerWithdrawal(Map<String, Object> params) {
        PlayerWithdrawalDao.getInstance().savePlayerWithdrawal(params);
    }

    public int withdrawalCount(long playerId) {
        long current = System.currentTimeMillis();
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        return PlayerWithdrawalDao.getInstance().withdrawalCount(playerId, new Long(zero / 1000).intValue());
    }

    public List<PlayerWithdrawal> page(long playerId, Integer page, Integer limit) {
        Integer offset = null;
        if (page != null && limit != null) {
            offset = (page - 1) * limit;
        }
        return PlayerWithdrawalDao.getInstance().page(playerId, offset, limit);
    }
}
