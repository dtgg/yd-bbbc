package com.ydqp.common.service;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.entity.Player;

import java.util.List;


public class PlayerService {

    private final static Logger logger = LoggerFactory.getLogger(PlayerService.class);
    public static PlayerService instance;

    public static PlayerService getInstance(){
        if (instance == null) {
            instance = new PlayerService();
        }
        return instance;
    }


    public Player queryByCondition(String queryCondition) {
        return PlayerDao.getInstance().queryByCondition(queryCondition);
    }

    public void updatePlayerCoinPoint(double coinPoint, long id) {
        PlayerDao.getInstance().updatePlayerCoinPoint(coinPoint, id);
    }

    public int updatePlayerZjPoint(double coinPoint, long id) {
        return PlayerDao.getInstance().updatePlayerZjPoint(coinPoint, id);
    }

    public void updatePlayerGrade(long playerId, int grade, double experience) {
        PlayerDao.getInstance().updatePlayerGrade(playerId, grade, experience);
    }

    public void updatePlayerRoomId(long playerId, int roomId) {
        PlayerDao.getInstance().updatePlayerRoomId(playerId, roomId);
    }

    public void batchUpdate(Object[][] params) {
        PlayerDao.getInstance().batchUpdate(params);
    }

    public Player queryByPlayerId (long playerId) {
        return PlayerDao.getInstance().queryById(playerId);
    }

    public List<Player> getPlayerByPlayerIds(String longString) {
        return PlayerDao.getInstance().getPlayerByPlayerIds(longString);
    }
}
