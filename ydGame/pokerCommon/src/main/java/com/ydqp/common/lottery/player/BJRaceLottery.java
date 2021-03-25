package com.ydqp.common.lottery.player;

import com.alibaba.fastjson.JSON;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.data.LotteryDrawData;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.entity.LotteryConfig;
import com.ydqp.common.entity.PlayerLottery;
import com.ydqp.common.lottery.LotteryColorConstant;
import com.ydqp.common.utils.DateUtil;
import com.ydqp.common.utils.LotteryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BJRaceLottery extends AbstractLottery {

    private static final Logger logger = LoggerFactory.getLogger(BJRaceLottery.class);

    @Override
    public LotteryDrawData lotteryDraw(Lottery lottery, List<PlayerLottery> playerLotteries) {
        //计算不同号码的中奖总金额
        Map<Integer, BigDecimal> drawNumMap = settleAwardPrice(playerLotteries);
        //根据value获取key排序
        List<Integer> numList = sortMapByValues(drawNumMap);
        logger.info("计算不同号码的中奖总金额：{}", JSON.toJSONString(drawNumMap));
        logger.info("号码根据下注额排序：{}", JSON.toJSONString(numList));

        LotteryConfig config = ManageLottery.getInstance().getConfig(lottery.getType());

        //下注总金额
        BigDecimal amount = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(playerLotteries)) {
            for (PlayerLottery playerLottery : playerLotteries) {
                if (playerLottery.getIsVir() == 1) continue;
                amount = amount.add(playerLottery.getPay());
            }
        }
        logger.info("下注总额：{}", amount);
        //判断开奖数字
        Integer drawNum = getDrawNum(numList, config, amount, lottery.getPeriod(), lottery.getId());

        String period = DateUtil.timestampToStr(lottery.getCreateTime()) + LotteryUtil.intToPeriod(lottery.getPeriod());
        logger.info("{}期开奖{}号码为：{}", period, lottery.getType(), drawNum);

        LotteryDrawData drawData = new LotteryDrawData();
        drawData.setDrawNum(String.valueOf(drawNum));
        drawData.setDrawNumMap(drawNumMap);
        return drawData;
    }

    @Override
    public Map<Integer, BigDecimal> settleAwardPrice(List<PlayerLottery> playerLotteries) {
        Map<Integer, BigDecimal> drawNumMap = new HashMap<Integer, BigDecimal>() {{
            put(1, BigDecimal.ZERO);
            put(2, BigDecimal.ZERO);
            put(3, BigDecimal.ZERO);
            put(4, BigDecimal.ZERO);
            put(5, BigDecimal.ZERO);
            put(6, BigDecimal.ZERO);
            put(7, BigDecimal.ZERO);
            put(8, BigDecimal.ZERO);
            put(9, BigDecimal.ZERO);
            put(10, BigDecimal.ZERO);
        }};

        if (CollectionUtils.isEmpty(playerLotteries)) return drawNumMap;

        for (PlayerLottery playerLottery : playerLotteries) {
            //有效下注
            BigDecimal effectiveBet = playerLottery.getPay().subtract(playerLottery.getFee());

            //下注大小
            if (playerLottery.getSelected() != null) {
                //小
                if (LotteryColorConstant.SMALL.equals(playerLottery.getSelected())) {
                    drawNumMap.put(1, drawNumMap.get(1).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(2, drawNumMap.get(2).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(3, drawNumMap.get(3).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(4, drawNumMap.get(4).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(5, drawNumMap.get(5).add(effectiveBet.multiply(new BigDecimal("2"))));
                } else {
                    //大
                    drawNumMap.put(6, drawNumMap.get(6).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(7, drawNumMap.get(7).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(8, drawNumMap.get(8).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(9, drawNumMap.get(9).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(10, drawNumMap.get(10).add(effectiveBet.multiply(new BigDecimal("2"))));
                }
            } else {
                //数字
                int num = Integer.parseInt(playerLottery.getNumber());
                drawNumMap.put(num, drawNumMap.get(num).add(effectiveBet.multiply(new BigDecimal("9"))));
            }
        }
        return drawNumMap;
    }

    @Override
    public PlayerLottery playerLotteryCheck(Lottery lottery, PlayerLottery playerLottery, Integer calculateTime) {
        //未中奖
        int drawNum = Integer.parseInt(lottery.getNumber());
        List<Integer> sizeList = drawSizeByNum(drawNum);
        if ((playerLottery.getSelected() != null && !sizeList.contains(playerLottery.getSelected())) ||
                (StringUtils.isNotBlank(playerLottery.getNumber()) && !playerLottery.getNumber().equals(lottery.getNumber()))) {
            playerLottery.setStatus(2);
            playerLottery.setOpenTime(calculateTime + 20);
            playerLottery.setAward(BigDecimal.ZERO.subtract(playerLottery.getPay()));
        } else {
            BigDecimal effectiveBet = playerLottery.getPay().subtract(playerLottery.getFee());
            BigDecimal award = BigDecimal.ZERO;
            //下注颜色中奖
            if (playerLottery.getSelected() != null && sizeList.contains(playerLottery.getSelected())) {
                //大小
                award = effectiveBet.multiply(new BigDecimal("2"));
            } else if (StringUtils.isNotBlank(playerLottery.getNumber()) && playerLottery.getNumber().equals(lottery.getNumber())) {
                //下注数字中奖
                award = effectiveBet.multiply(new BigDecimal("9"));
            }
            playerLottery.setStatus(1);
            playerLottery.setOpenTime(calculateTime + 20);
            playerLottery.setAward(award);
        }
        playerLottery.setResult(lottery.getNumber());
        return playerLottery;
    }

    private List<Integer> drawSizeByNum(int num) {
        List<Integer> sizeList = new ArrayList<>();
        if (num <= 5) {
            sizeList.add(LotteryColorConstant.SMALL);
        } else {
            sizeList.add(LotteryColorConstant.BIG);
        }
        return sizeList;
    }
}
