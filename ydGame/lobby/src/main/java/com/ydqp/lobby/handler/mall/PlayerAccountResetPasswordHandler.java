package com.ydqp.lobby.handler.mall;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.receiveProtoMsg.mall.PlayerAccountResetPassword;
import com.ydqp.common.sendProtoMsg.mall.PlayerAccountResetPasswordSuc;
import com.ydqp.lobby.service.mall.PlayerAccountService;
import org.apache.commons.codec.digest.DigestUtils;

@ServerHandler(command = 1004008, module = "mall")
public class PlayerAccountResetPasswordHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerAccountResetPasswordHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerAccountResetPassword resetPassword = (PlayerAccountResetPassword) abstartParaseMessage;
        PlayerAccountResetPasswordSuc resetPasswordSuc = new PlayerAccountResetPasswordSuc();

        PlayerData playerData = PlayerCache.getInstance().getPlayer(resetPassword.getConnId());
        if (playerData == null) {
            resetPasswordSuc.setSuccess(false);
            resetPasswordSuc.setMessage("player is not true");
            iSession.sendMessageByID(resetPasswordSuc, resetPassword.getConnId());
            logger.error("账户信息更新失败，玩家不在线，playerId: {}", resetPassword.getPlayerId());
            return;
        }

        if (!resetPassword.getPassword().equals(resetPassword.getRepeatedPassword())) {
            resetPasswordSuc.setSuccess(false);
            resetPasswordSuc.setMessage("Inconsistent passwords");
            iSession.sendMessageByID(resetPasswordSuc, resetPassword.getConnId());
            logger.error("账户信息更新失败，密码不一致，playerId:{}, password:{}, repeatedPassword:{}",
                    playerData.getPlayerId(), resetPassword.getPassword(), resetPassword.getRepeatedPassword());
            return;
        }
        String verificationCode = PlayerCache.getInstance().getVerificationCode(playerData.getPlayerName());
        if (!resetPassword.getVerificationCode().equals(verificationCode)) {
            resetPasswordSuc.setSuccess(false);
            resetPasswordSuc.setMessage("Verification code error");
            iSession.sendMessageByID(resetPasswordSuc, resetPassword.getConnId());
            logger.error("手机注册，验证码错误，playerName:{}", playerData.getPlayerName());
            return;
        }

        Object[] params = new Object[]{DigestUtils.md5Hex(resetPassword.getPassword()), resetPassword.getPlayerId()};
        PlayerAccountService.getInstance().updatePassword(params);
        resetPasswordSuc.setSuccess(true);
        resetPasswordSuc.setMessage("Reset fund password successfully");
        iSession.sendMessageByID(resetPasswordSuc, resetPassword.getConnId());
    }
}
