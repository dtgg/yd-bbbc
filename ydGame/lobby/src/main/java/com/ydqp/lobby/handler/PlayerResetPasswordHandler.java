package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.entity.Player;
import com.ydqp.common.receiveProtoMsg.player.PlayerResetPassword;
import com.ydqp.common.sendProtoMsg.player.MobileRegisterSuc;
import com.ydqp.common.sendProtoMsg.player.PlayerResetPasswordSuc;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.dao.PlayerLoginDao;
import com.ydqp.lobby.service.PlayerService;
import com.ydqp.lobby.utils.MessageCheckUtil;
import org.apache.commons.lang.StringUtils;

@ServerHandler(module = "playerLogin", command = 1000033)
public class PlayerResetPasswordHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlayerResetPasswordHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerResetPassword playerResetPassword = (PlayerResetPassword) abstartParaseMessage;

        //login
        Player player = PlayerLoginDao.getInstance().selectPlayerByPN(playerResetPassword.getMobile());
        if (player == null) {
            MobileRegisterSuc suc = new MobileRegisterSuc();
            suc.setSuccess(true);
            suc.setMessage("The number has not been registered, the password cannot be reset");
            iSession.sendMessageByID(suc, playerResetPassword.getConnId());
            return;
        }

        PlayerResetPasswordSuc suc = new PlayerResetPasswordSuc();
        if (StringUtils.isBlank(playerResetPassword.getMobile())) {
            suc.setSuccess(false);
            suc.setMessage("Phone number cannot be empty");
            iSession.sendMessageByID(suc, playerResetPassword.getConnId());
            logger.error("手机注册，手机号不能为空");
            return;
        }
        if (StringUtils.isBlank(playerResetPassword.getPassword())) {
            suc.setSuccess(false);
            suc.setMessage("Password cannot be empty");
            iSession.sendMessageByID(suc, playerResetPassword.getConnId());
            logger.error("手机注册，密码不能为空， mobile:{}", playerResetPassword.getMobile());
            return;
        }
        if (StringUtils.isBlank(playerResetPassword.getVerificationCode())) {
            suc.setSuccess(false);
            suc.setMessage("Verification code cannot be empty");
            iSession.sendMessageByID(suc, playerResetPassword.getConnId());
            logger.error("手机注册，验证码不能为空， mobile:{}", playerResetPassword.getMobile());
            return;
        }

        if (!MessageCheckUtil.checkMobile(playerResetPassword.getMobile())) {
            suc.setSuccess(false);
            suc.setMessage("Incorrect number format");
            iSession.sendMessageByID(suc, playerResetPassword.getConnId());
            logger.error("手机注册，手机号格式错误， mobile:{}", playerResetPassword.getMobile());
            return;
        }
        //两次输入是否一致
        if (!playerResetPassword.getPassword().equals(playerResetPassword.getRepeatPassword())) {
            suc.setSuccess(false);
            suc.setMessage("Password input is inconsistent");
            iSession.sendMessageByID(suc, playerResetPassword.getConnId());
            logger.error("手机注册，两次密码不一致， password:{}， repeatPassword", playerResetPassword.getPassword(), playerResetPassword.getRepeatPassword());
            return;
        }
        //校验密码格式
        if (!MessageCheckUtil.checkPassword(playerResetPassword.getPassword())) {
            suc.setSuccess(false);
            suc.setMessage("The password format is wrong, please enter a combination of 8 or more English and numbers");
            iSession.sendMessageByID(suc, playerResetPassword.getConnId());
            logger.error("手机注册，密码格式错误， mobile:{}", playerResetPassword.getPassword());
            return;
        }
        //校验验证码
        String verificationCode = PlayerCache.getInstance().getVerificationCode(playerResetPassword.getMobile());
        if (!playerResetPassword.getVerificationCode().equals(verificationCode)) {
            suc.setSuccess(false);
            suc.setMessage("Verification code error");
            iSession.sendMessageByID(suc, playerResetPassword.getConnId());
            logger.error("手机注册，验证码错误， mobile:{}", playerResetPassword.getVerificationCode());
            return;
        }

        PlayerService.getInstance().resetPassword(iSession, playerResetPassword);
    }
}
