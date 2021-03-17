package com.ydqp.lobby.service.mall;

import com.ydqp.common.entity.PlayerAccount;
import com.ydqp.lobby.dao.mall.PlayerAccountDao;

import java.util.Map;

public class PlayerAccountService {

    private PlayerAccountService() {}

    private static PlayerAccountService instance = new PlayerAccountService();

    public static PlayerAccountService getInstance() {
        if (instance == null) {
            instance = new PlayerAccountService();
        }
        return instance;
    }

    public void savePlayerAccount(Map<String, Object> params) {
        PlayerAccountDao.getInstance().savePlayerAccount(params);
    }

    public void updatePlayerAccount(Object[] params) {
        PlayerAccountDao.getInstance().updatePlayerAccount(params);
    }

    public void updatePlayerAccountWithFaId(Object[] params) {
        PlayerAccountDao.getInstance().updatePlayerAccountWithFaId(params);
    }

    public void updateAccountPayInfo(Object[] params) {
        PlayerAccountDao.getInstance().updateAccountPayInfo(params);
    }

    public void updatePlayerAccountWithdrawing(double amount, long id) {
        PlayerAccountDao.getInstance().updatePlayerAccountWithdrawing(amount, id);
    }

    public PlayerAccount findByPlayerId(long playerId) {
        return PlayerAccountDao.getInstance().findByPlayerId(playerId);
    }

    public void updateFaId(String faId, long playerId) {
        PlayerAccountDao.getInstance().updateFaId(faId, playerId);
    }

    public void updateBeneId(String beneId, long playerId) {
        PlayerAccountDao.getInstance().updateBeneId(beneId, playerId);
    }

    public void updateYaarAccount(Object[] params) {
        PlayerAccountDao.getInstance().updateYaarAccount(params);
    }

    public PlayerAccount findByAccNo(String accNo) {
        return PlayerAccountDao.getInstance().findByAccNo(accNo);
    }

    public void updatePassword(Object[] params) {
        PlayerAccountDao.getInstance().updatePassword(params);
    }
}
