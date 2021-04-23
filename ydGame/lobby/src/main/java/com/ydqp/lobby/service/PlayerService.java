package com.ydqp.lobby.service;

import com.alibaba.fastjson.JSONObject;
import com.cfq.connection.ISession;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.constant.Constant;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.dao.VsRacePromoteDao;
import com.ydqp.common.dao.lottery.PlayerPromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.data.Total;
import com.ydqp.common.entity.*;
import com.ydqp.common.receiveProtoMsg.player.*;
import com.ydqp.common.sendProtoMsg.player.*;
import com.ydqp.common.utils.ShortCodeKit;
import com.ydqp.lobby.constant.GuestRegisterConstant;
import com.ydqp.lobby.dao.GameSwitchDao;
import com.ydqp.lobby.dao.PlayerLoginDao;
import org.apache.commons.lang.StringUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class PlayerService {

    private final static Logger logger = LoggerFactory.getLogger(PlayerService.class);
    public static PlayerService instance;

    public static PlayerService getInstance() {
        if (instance == null) {
            instance = new PlayerService();
        }
        return instance;
    }

    public void login(ISession iSession, PlayerLogin playerLogin) {

        String playerName = playerLogin.getPlayerName();
        String password = playerLogin.getPassWord();
        if (playerName == null || password == null) {

            return;
        }

        //login
        Player player = PlayerLoginDao.getInstance().selectPlayerByPN(playerName);
        if (player == null) {
            //send login error
            LobbyError lobbyError = new LobbyError();
            lobbyError.setErrorCode(Constant.LOGIN_ERROR_PW);
            lobbyError.setErrorMsg(Constant.LOGIN_ERR_NOT_R);
            iSession.sendMessage(lobbyError, playerLogin);
            return;
        }
        if (player.getBanLogin() == 1) {
            LobbyError lobbyError = new LobbyError();
            lobbyError.setErrorCode(Constant.LOGIN_ERROR_PW);
            lobbyError.setErrorMsg(Constant.LOGIN_ERR_BANLOGIN);
            iSession.sendMessage(lobbyError, playerLogin);
            logger.error("封号登陆, playerId= {}", player.getId());
            return;
        }

        String pw = getMD5Str(playerLogin.getPassWord());
        if (!(player.getPassWord().equals(password) || player.getPassWord().equals(pw))) {
            //send login error
            LobbyError lobbyError = new LobbyError();
            lobbyError.setErrorCode(Constant.LOGIN_ERROR_PW);
            lobbyError.setErrorMsg(Constant.LOGIN_ERR_PW_ERROR);
            iSession.sendMessage(lobbyError, playerLogin);
            return;
        }

        //这边要踢人，将之前登陆的给踢下去
        PlayerData kickPlayer = PlayerCache.getInstance().getPlayerByPlayerID(player.getId());
        if (kickPlayer != null) {
            logger.debug("玩家登陆，通过id获取connID ，playerId = {}  connId = {}  nconndi = {}", player.getId(),
                    kickPlayer.getSessionId(), playerLogin.getConnId());
            PlayerData checkPlayer = PlayerCache.getInstance().getPlayer(kickPlayer.getSessionId());
            if (checkPlayer != null && checkPlayer.getPlayerId() == kickPlayer.getPlayerId()) {
                logger.debug("玩家登陆重复登陆，通过id获取connID ，playerId = {}  connId = {}  nconndi = {}", checkPlayer.getPlayerId(),
                        checkPlayer.getSessionId(), playerLogin.getConnId());
                //PlayerCache.getInstance().delPlayerByConnId(kickPlayer.getSessionId());
                LobbyKickPlayer lobbyKickPlayer = new LobbyKickPlayer();
                lobbyKickPlayer.setPlayerId(kickPlayer.getPlayerId());
                iSession.sendMessageByID(lobbyKickPlayer, kickPlayer.getSessionId());
            }
        }

        PlayerData playerData = new PlayerData(player);
        playerData.setSessionId(playerLogin.getConnId());
        PlayerCache.getInstance().addPlayer(playerLogin.getConnId(), playerData);
        //通过playerId找到connId
        PlayerCache.getInstance().addPlayerByPlayerId(playerData.getPlayerId(), playerData);

        logger.info("玩家登陆成功，playerId = {} ， ConnId= {} ", playerData.getPlayerId(), playerData.getSessionId());

        //发送登陆命令给业务服务器
        LoginSuccess loginSuccessBYBs = new LoginSuccess();
        if (playerData.getRoomId() == 0) {
            //return;
        } else if ((playerData.getRoomId() / 100000) == 20) {
            loginSuccessBYBs.setCommand(2010000);
            iSession.sendMessage(loginSuccessBYBs, playerLogin);
        } else if ((playerData.getRoomId() / 100000) == 30) {
            loginSuccessBYBs.setCommand(3010000);
            iSession.sendMessage(loginSuccessBYBs, playerLogin);
        }

        LoginSuccess loginSuccess = new LoginSuccess();
        loginSuccess.setCommand(1000005);
        loginSuccess.setPlayerId(playerData.getPlayerId());
        loginSuccess.setPlayerName(playerData.getPlayerName());
        loginSuccess.setPlayerNickName(playerData.getNickName());
        loginSuccess.setPlayerZJPoint(playerData.getZjPoint());
        loginSuccess.setRoomId(player.getRoomId());
        loginSuccess.setPlayerUrl(playerData.getHeadUrl());
        loginSuccess.setIsVir(player.getIsVir());
        loginSuccess.setIsRebate(player.getIsRebate());

        Total rebateAmount = VsRacePromoteDao.getInstance().sumRebateAmount(player.getId());
        loginSuccess.setRebateAmount(rebateAmount == null ? 0 : rebateAmount.getSum());

        //获取开关游戏
        String serverCodes = getGameSwitch();
        loginSuccess.setOpenGames(serverCodes);

        iSession.sendMessageByID(loginSuccess, playerLogin.getConnId());
        //更新在线状态
        PlayerLoginDao.getInstance().updateOnLineTime(playerData.getPlayerId(), 0);
    }

    public Player queryByPlayerId(Long playerId) {
        return PlayerLoginDao.getInstance().queryByCondition(playerId);
    }

    public List<Player> findAllByIds(Set<String> playIds) {
        return PlayerLoginDao.getInstance().findAllByIds(playIds);
    }

    public void updatePlayerCoinPoint(double coinPoint, long id) {
        PlayerLoginDao.getInstance().updatePlayerCoinPoint(coinPoint, id);
    }

    public int updatePlayerZjPoint(double coinPoint, long id) {
        return PlayerLoginDao.getInstance().updatePlayerZjPoint(coinPoint, id);
    }

    public void batchUpdatePlayerZjPoint(Object[][] params) {
        PlayerLoginDao.getInstance().batchUpdatePlayerZjPoint(params);
    }

    private static String randomStr(int length) {
        StringBuilder builder = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int nextInt = random.nextInt(10);
            builder.append(nextInt);
        }

        return builder.toString();
    }

    private String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance(GuestRegisterConstant.MD5);
            digest = md5.digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digest != null;
        return new BigInteger(1, digest).toString(16);
    }

    private String getGameSwitch() {
        List<GameSwitch> gameSwitches = GameSwitchDao.getInstance().getGameSwitch();
        List<Integer> serverCodes = new ArrayList<>();
        for (GameSwitch gameSwitch : gameSwitches) {
            serverCodes.add(gameSwitch.getServerCode());
        }
        return JSONObject.toJSONString(serverCodes);
    }

    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 150; i++) {
            String nickname = GuestRegisterConstant.NICKNAME_PREFIX + randomStr(GuestRegisterConstant.NICKNAME_LONGTH);
            String str = "(1015020, '18420188750', '53cafda2315b74cad11b6adeb80a5cbd', '" + nickname + "', '1', 0, 0.0, 0.0, 0, 0,1,0.0,'',0);";
            if (set.contains(str)) continue;
            System.out.println(str);
            set.add(str);
        }
    }

    public void updateHeadUrl(Object[] params) {
        PlayerDao.getInstance().updateHeadUrl(params);
    }

    public void mobileRegister(ISession iSession, MobileRegister mobileRegister) {
        MobileRegisterSuc suc = new MobileRegisterSuc();
        //推广码是否有效
        Long superiorId = null;
        long kfId = 0L;
        int appId = 1000001;
        if (StringUtils.isNotBlank(mobileRegister.getReferralCode())) {
            String s = ShortCodeKit.convertBase62ToDecimal(mobileRegister.getReferralCode());
            Long permutedId = ShortCodeKit.permutedId(Long.parseLong(s));
            Player referralPlayer = PlayerService.getInstance().queryByPlayerId(permutedId);
            if (referralPlayer == null) {
                suc.setSuccess(false);
                suc.setMessage("Referral code does not exist");
                iSession.sendMessageByID(suc, mobileRegister.getConnId());
                logger.error("注册失败，验证码错误， mobile:{}", mobileRegister.getVerificationCode());
                return;
            }
            superiorId = referralPlayer.getId();
            kfId = referralPlayer.getKfId();
            appId = referralPlayer.getAppId();
        }

        String nickname = GuestRegisterConstant.MOBILE_NICKNAME_PREFIX + randomStr(GuestRegisterConstant.NICKNAME_LONGTH);
        String password = getMD5Str(mobileRegister.getPassword());
        int createTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        Player player = new Player();
        player.setPlayerName(mobileRegister.getMobile());
        player.setPassWord(password);
        player.setNickname(nickname);
        player.setHeadUrl(GuestRegisterConstant.HEAD_URL);
        player.setRoomId(GuestRegisterConstant.ROOM_ID);
        player.setZjPoint(0);
        player.setCreateTime(createTime);
        player.setAppId(appId);
        player.setKfId(kfId);
        player.setIsVir(0);


        long playerId = PlayerLoginDao.getInstance().insertPlayer(player.getParameterMap());
        //referral code
        String referralCode = ShortCodeKit.convertDecimalToBase62(ShortCodeKit.permutedId(playerId), 8);
        PlayerDao.getInstance().updateReferralCode(new Object[] {referralCode, playerId});

        suc.setSuccess(true);
        suc.setMessage("Registered successfully");
        suc.setPlayerName(mobileRegister.getMobile());
        suc.setPassword(password);
        iSession.sendMessageByID(suc, mobileRegister.getConnId());

        //绑定推荐关系
        PlayerPromote playerPromote = new PlayerPromote();
        playerPromote.setPlayerId(playerId);
        playerPromote.setNickname(nickname);
        playerPromote.setCreateTime(createTime);
        playerPromote.setAppId(player.getAppId());
        if (superiorId != null) {
            playerPromote.setSuperiorId(superiorId);

            PlayerPromote promote = PlayerPromoteDao.getInstance().findByPlayerId(superiorId);
            if (promote != null) {
                playerPromote.setGrandId(promote.getSuperiorId());
                playerPromote.setKfId(promote.getKfId());
            }
        }
        PlayerPromoteDao.getInstance().insert(playerPromote.getParameterMap());
        logger.info("新用户注册,playerId:{},superiorId:{},grandId:{}", playerId, playerPromote.getSuperiorId(), playerPromote.getGrandId());

        //上报注册数据
//        Map<String, Object> data = new HashMap<>();
//        data.put("playerId", playerId);
//        data.put("appId", mobileRegister.getAppId());
//        data.put("registerTime", createTime);
//        data.put("type", 3);
//        ThreadManager.getInstance().getStatUploadExecutor().execute(new StatisticsUploadTask(UpLoadConstant.PLAYER_REGISTER,
//                new JSONObject(data)));
    }

    public void resetPassword(ISession iSession, PlayerResetPassword playerResetPassword) {
        String password = getMD5Str(playerResetPassword.getPassword());
        PlayerDao.getInstance().updatePassword(new Object[]{password, playerResetPassword.getMobile()});

        PlayerResetPasswordSuc suc = new PlayerResetPasswordSuc();
        suc.setPlayerName(playerResetPassword.getMobile());
        suc.setPassword(password);
        suc.setSuccess(true);
        suc.setMessage("Password reset successfully");
        iSession.sendMessageByID(suc, playerResetPassword.getConnId());
    }
}
