package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.entity.Player;
import com.ydqp.common.receiveProtoMsg.player.MobileRegister;
import com.ydqp.common.sendProtoMsg.player.MobileRegisterSuc;
import com.ydqp.lobby.dao.PlayerLoginDao;
import com.ydqp.lobby.service.PlayerService;
import com.ydqp.lobby.utils.MessageCheckUtil;
import org.apache.commons.lang.StringUtils;

/**
 * 游客注册
 */
@ServerHandler(module = "playerLogin", command = 1000031)
public class MobileRegisterHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(MobileRegisterHandler.class);
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        MobileRegister mobileRegister = (MobileRegister) abstartParaseMessage;

        //login
        Player player = PlayerLoginDao.getInstance().selectPlayerByPN(mobileRegister.getMobile());
        if (player != null) {
            MobileRegisterSuc suc = new MobileRegisterSuc();
            suc.setSuccess(false);
            suc.setMessage("Repeat registration");
            suc.setPassword(player.getPassWord());
            suc.setPlayerName(mobileRegister.getMobile());
            iSession.sendMessageByID(suc, mobileRegister.getConnId());
            return;
        }

        MobileRegisterSuc suc = new MobileRegisterSuc();
        if (StringUtils.isBlank(mobileRegister.getMobile())) {
            suc.setSuccess(false);
            suc.setMessage("Phone number cannot be empty");
            iSession.sendMessageByID(suc, mobileRegister.getConnId());
            logger.error("手机注册，手机号不能为空");
            return;
        }
        if (StringUtils.isBlank(mobileRegister.getPassword())) {
            suc.setSuccess(false);
            suc.setMessage("Password cannot be empty");
            iSession.sendMessageByID(suc, mobileRegister.getConnId());
            logger.error("手机注册，密码不能为空， mobile:{}", mobileRegister.getMobile());
            return;
        }
        if (StringUtils.isBlank(mobileRegister.getVerificationCode())) {
            suc.setSuccess(false);
            suc.setMessage("Verification code cannot be empty");
            iSession.sendMessageByID(suc, mobileRegister.getConnId());
            logger.error("手机注册，验证码不能为空， mobile:{}", mobileRegister.getMobile());
            return;
        }

        if (!MessageCheckUtil.checkMobile(mobileRegister.getMobile())) {
            suc.setSuccess(false);
            suc.setMessage("Incorrect number format");
            iSession.sendMessageByID(suc, mobileRegister.getConnId());
            logger.error("手机注册，手机号格式错误， mobile:{}", mobileRegister.getMobile());
            return;
        }
        //两次输入是否一致
        if (!mobileRegister.getPassword().equals(mobileRegister.getRepeatPassword())) {
            suc.setSuccess(false);
            suc.setMessage("Password input is inconsistent");
            iSession.sendMessageByID(suc, mobileRegister.getConnId());
            logger.error("手机注册，两次密码不一致， password:{}， repeatPassword", mobileRegister.getPassword(), mobileRegister.getRepeatPassword());
            return;
        }
        //校验密码格式
        if (mobileRegister.getPassword().length() < 6) {
            suc.setSuccess(false);
            suc.setMessage("The password format is wrong, please enter a combination of 6 or more");
            iSession.sendMessageByID(suc, mobileRegister.getConnId());
            logger.error("手机注册，密码格式错误， mobile:{}", mobileRegister.getPassword());
            return;
        }
        //校验验证码
        String verificationCode = PlayerCache.getInstance().getVerificationCode(mobileRegister.getMobile());
        if (!mobileRegister.getVerificationCode().equals(verificationCode)) {
            suc.setSuccess(false);
            suc.setMessage("Verification code error");
            iSession.sendMessageByID(suc, mobileRegister.getConnId());
            logger.error("手机注册，验证码错误， mobile:{}", mobileRegister.getVerificationCode());
            return;
        }

        PlayerService.getInstance().mobileRegister(iSession, mobileRegister);
    }
}
