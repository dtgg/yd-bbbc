package com.ydqp.lobby.dao.mall;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.PlayerAccount;

import java.util.Map;

public class PlayerAccountDao {

    private PlayerAccountDao() {}

    private static PlayerAccountDao instance = new PlayerAccountDao();

    public static PlayerAccountDao getInstance() {
        if (instance == null) {
            instance = new PlayerAccountDao();
        }
        return instance;
    }

    public void savePlayerAccount(Map<String, Object> params) {
        JdbcOrm.getInstance().insert("player_account", params);
    }

    private static final String UPDATE = "update player_account set name = ?, mobile = ?, bankName = ?, accNo = ?, ifsc = ? where playerId = ?;";
    public void updatePlayerAccount(Object[] params) {
        JdbcOrm.getInstance().updateByArray(UPDATE, params);
    }

    private static final String UPDATE_FA = "update player_account set name = ?, accNo = ?, ifsc = ?, mobile = ?, faId = ? where playerId = ?;";
    public void updatePlayerAccountWithFaId(Object[] params) {
        JdbcOrm.getInstance().updateByArray(UPDATE_FA, params);
    }

    private static final String UPDATE_PAY = "update player_account set payMobile = ?, email = ?, depositName = ? where playerId = ?;";
    public void updateAccountPayInfo(Object[] params) {
        JdbcOrm.getInstance().updateByArray(UPDATE_PAY, params);
    }

    public void updatePlayerAccountWithdrawing(double amount, long id) {
        String sql = "update player_account set withdrawing = withdrawing + " + amount + " where id = " + id + ";";
        JdbcOrm.getInstance().update(sql);
    }


    private static final String FIND_BY_PLAYERID = "select * from player_account where playerId = ";
    public PlayerAccount findByPlayerId(long playerId) {
        String sql = FIND_BY_PLAYERID + playerId + ";";
        return (PlayerAccount) JdbcOrm.getInstance().getBean(sql, PlayerAccount.class);
    }

    public void updateFaId(String faId, long playerId) {
        String sql = "update player_account set faId = '" + faId + "' where playerId = " + playerId + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public void updateBeneId(String beneId, long playerId) {
        String sql = "update player_account set beneId = '" + beneId + "' where playerId = " + playerId + ";";
        JdbcOrm.getInstance().update(sql);
    }

    private static final String UPDATE_PLAYER_ACCOUNT = "update player_account set name = ?, accNo = ?, ifsc = ?, mobile = ?, bankCode = ? where playerId = ?;";
    public void updateYaarAccount(Object[] params) {
        JdbcOrm.getInstance().updateByArray(UPDATE_PLAYER_ACCOUNT, params);
    }

    private static final String FIND_BY_ACCNO = "select * from player_account where accNo = ";
    public PlayerAccount findByAccNo(String accNo) {
        String sql = FIND_BY_ACCNO + accNo + ";";
        return (PlayerAccount) JdbcOrm.getInstance().getBean(sql, PlayerAccount.class);
    }

    private static final String UPDATE_ACCOUNT_PASSWORD = "update player_account set password = ? where playerId = ?;";
    public void updatePassword(Object[] params) {
        JdbcOrm.getInstance().updateByArray(UPDATE_ACCOUNT_PASSWORD, params);
    }
}
