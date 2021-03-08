package com.ydqp.lottery.task;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.dao.lottery.LotteryConfigDao;
import com.ydqp.common.dao.lottery.LotteryDao;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.entity.LotteryConfig;
import com.ydqp.common.utils.LotteryUtil;
import org.apache.commons.collections.CollectionUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成新一期彩票的基本信息
 */
public class LotteryGenerateTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(LotteryGenerateTask.class);

    @Override
    public void run() {
        //查询当天是否已生成过,未生成生成两天的数据，已生成生成一天的数据
        Lottery lottery = LotteryDao.getInstance().findLast();

        int size = 480;
        int zeroTimestamp = Math.toIntExact(getZeroTimestamp());
        if (lottery == null) {
            size = 960;
        } else if (lottery.getCreateTime() - new Long(System.currentTimeMillis() / 1000).intValue() > 24 * 60 * 60){
            return;
        } else {
            zeroTimestamp = zeroTimestamp + 24 * 60 * 60;
        }

        logger.info("----------------------开始生成彩票期数信息----------------------");

        //获取彩票配置
        List<LotteryConfig> lotteryConfigs = LotteryConfigDao.getInstance().findAll();

        //当前时间戳
        int nowTimestamp = new Long(System.currentTimeMillis() / 1000).intValue();

        //生成一天480期
        List<Object[]> lotteryList = new ArrayList<>();
        int period = 1;
        for (LotteryConfig config : lotteryConfigs) {
            for (int i = 0; i < size; i++) {
                int createTime = zeroTimestamp + i * 180;
                Object[] obj;
                if (nowTimestamp - createTime > 150) {
                    obj = new Object[]{config.getLotteryType(), period, LotteryUtil.getPrice() + period,
                            LotteryUtil.getDrawNum(), 2, createTime, createTime + 180};
                } else {
                    obj = new Object[]{config.getLotteryType(), period, null, null, 0, createTime, null};
                }
                lotteryList.add(obj);
                if (size > 480 && i == 479) {
                    period = 1;
                } else {
                    period += 1;
                }
            }
            period = 1;
        }
        if (CollectionUtils.isNotEmpty(lotteryList)) {
            //拼接sql字符串
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lotteryList.size(); i++) {
                Object[] objects = lotteryList.get(i);
                builder.append("(");
                for (int j = 0; j < objects.length; j++) {
                    if (j == objects.length - 1) builder.append(objects[j]);
                    else builder.append(objects[j]).append(",");
                }
                if (i == lotteryList.size() - 1) builder.append(")");
                else builder.append("),");
            }
            String condition = builder.toString();
            LotteryDao.getInstance().batchInsert(condition);
        }
        logger.info("----------------------彩票期数信息生成结束----------------------");
    }

    public static long getZeroTimestamp() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime ydZonedDateTime = now.withZoneSameLocal(ZoneId.of("+05:30"));
        ZonedDateTime todayZero = ydZonedDateTime.truncatedTo(ChronoUnit.DAYS);
        return todayZero.toEpochSecond();
    }
}
