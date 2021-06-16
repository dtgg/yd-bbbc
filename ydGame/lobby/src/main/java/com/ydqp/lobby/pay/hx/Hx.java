package com.ydqp.lobby.pay.hx;

import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.entity.*;
import com.ydqp.common.sendProtoMsg.mall.PlayerOrderSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuccess;
import com.ydqp.common.utils.HttpUtils;
import com.ydqp.lobby.pay.CommonPay;
import com.ydqp.lobby.pay.OrderPay;
import com.ydqp.lobby.pay.PayUrlUtil;
import com.ydqp.lobby.service.PlayerService;
import com.ydqp.lobby.utils.Md5Utils;
import com.ydqp.lobby.utils.RandomValueUtil;
import jodd.util.URLDecoder;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Hx extends OrderPay {

    private static final Logger logger = LoggerFactory.getLogger(OrderPay.class);

    @Override
    public PlayerOrderSuccess payment(PlayerOrder order, PayChannelConfig config) {
        Map<String, String> header = new HashMap<String, String>() {{
            put("Content-Type", "application/json");
        }};

        JSONObject params = new JSONObject();
        params.put("merchant_sn", config.getMchId());
        params.put("order_sn", order.getOrderId());

        BigDecimal amount = BigDecimal.valueOf(order.getAmount()).setScale(2, BigDecimal.ROUND_DOWN);
        params.put("amount", amount.toString());
        params.put("name", RandomValueUtil.getName());

        Player player = PlayerService.getInstance().queryByPlayerId(order.getPlayerId());
        params.put("email", player.getPlayerName() + "@gmail.com");
        params.put("phone", player.getPlayerName());
        params.put("remark", "Recharge");
        params.put("redirect_url", "https://api.paycici.com/api/redirect");

        String signStr = config.getAppId() + order.getOrderId() + amount + config.getSecretKey();
        logger.info("payment sign str:{}", signStr);
        String sign = DigestUtils.md5Hex(signStr);
        logger.info("payment sign:{}", sign);
        params.put("sign", sign);

        String proxy = PayUrlUtil.getInstance().getUrl("isDebug");

        logger.info("hx payment params:{}", params.toJSONString());
        String result = HttpUtils.getInstance().sendPostNoSsl("https://upi.best/gateway/payin/", header, params, Boolean.parseBoolean(proxy));
        logger.info("hx payment response:{}", result);

        JSONObject response = JSONObject.parseObject(result);

        PlayerOrderSuccess playerOrderSuccess = new PlayerOrderSuccess();
        if (response.getInteger("code") == 0) {
            JSONObject responseData = response.getJSONObject("data");
            order.setTxnOrderId(responseData.getString("platform_osn"));

            JSONObject data = new JSONObject();
            data.put("url", URLDecoder.decode(responseData.getString("pay_url")));
            playerOrderSuccess.setData(data.toJSONString());
            playerOrderSuccess.setSuccess(true);
            playerOrderSuccess.setPayChannel("url");
            return playerOrderSuccess;
        }

        order.setStatus(2);
        order.setMsg(response.getString("message"));

        playerOrderSuccess.setSuccess(false);
        playerOrderSuccess.setMessage("Recharge failed!");
        return playerOrderSuccess;
    }

    @Override
    public PlayerWithdrawalSuccess payout(PlayerWithdrawal withdrawal, PayWithdrawalConfig config, PlayerAccount account) {
        Map<String, String> header = new HashMap<String, String>() {{
            put("Content-Type", "application/json");
        }};

        JSONObject params = new JSONObject();
        params.put("merchant_sn", config.getMchId());
        params.put("order_sn", withdrawal.getTransferId());
        BigDecimal am = new BigDecimal(String.valueOf(withdrawal.getAmount()));
        BigDecimal amount = am.multiply(BigDecimal.ONE.subtract(config.getWithdrawFee())).setScale(2, RoundingMode.DOWN);
        params.put("amount", amount.toString());
        params.put("type", "bank");
        params.put("name", account.getName());
        params.put("account", account.getAccNo());
        params.put("ifsc", account.getIfsc());
        params.put("remark", "Withdrawal");

        String signStr = config.getClientId() + withdrawal.getTransferId() + account.getAccNo() + "" + amount + config.getSecretKey();
        logger.info("payment sign str:{}", signStr);
        String sign = DigestUtils.md5Hex(signStr);
        logger.info("payment sign:{}", sign);
        params.put("sign", sign);

        String proxy = PayUrlUtil.getInstance().getUrl("isDebug");

        logger.info("hx payout params:{}", params.toJSONString());
        String result = HttpUtils.getInstance().sendPostNoSsl("https://upi.best/gateway/payout/", header, params, Boolean.parseBoolean(proxy));
        logger.info("hx payout response:{}", result);

        JSONObject response = JSONObject.parseObject(result);

        PlayerWithdrawalSuccess playerWithdrawalSuccess = new PlayerWithdrawalSuccess();
        if (response.getInteger("code") == 0) {
            JSONObject responseData = response.getJSONObject("data");
            withdrawal.setReferenceId(responseData.getString("platform_osn"));
            withdrawal.setStatus(1);

            playerWithdrawalSuccess.setSuccess(true);
            return playerWithdrawalSuccess;
        }

        withdrawal.setStatus(2);
        withdrawal.setErrorMsg(response.getString("message"));

        playerWithdrawalSuccess.setSuccess(false);
        playerWithdrawalSuccess.setMessage("Withdrawal failed!");
        return playerWithdrawalSuccess;
    }

    public static void main(String[] args) {
//        System.out.println(DigestUtils.md5Hex("512b2e1d-0173-415a-95bd-e3585fc75fb6"+"20200101123456-A001" + "3000"));

        PayChannelConfig config = new PayChannelConfig();
//        PayWithdrawalConfig config = new PayWithdrawalConfig();
        config.setName("hx");
        config.setMchId("20210320-odxlk");
        config.setAppId("0a7e1867c26e0a2fedd46ea02d0571dc");
        config.setSecretKey("2a6284676b8ee97beaa5de20e4501605");

        config.setClientId("0a7e1867c26e0a2fedd46ea02d0571dc");
        config.setClientSecret("2a6284676b8ee97beaa5de20e4501605");
        config.setWithdrawFee(new BigDecimal("0.02"));

        PlayerOrder order = new PlayerOrder();
        order.setOrderId(UUID.randomUUID().toString().replace("-", ""));
        order.setAmount(100);
        order.setPlayerId(1);

        new CommonPay().getOrderPay(config.getName()).payment(order, config);

        PlayerWithdrawal withdrawal = new PlayerWithdrawal();
        withdrawal.setTransferId(UUID.randomUUID().toString().replace("-", ""));
        withdrawal.setAmount(10);

        PlayerAccount account = new PlayerAccount();
        account.setName("Anandhu Sabu");
        account.setAccNo("67304084219");
        account.setIfsc("SBIN0000000");
//        new CommonPay().getOrderPay(config.getName()).payout(withdrawal, config, account);
    }
}
